import sourceMapSupport from 'source-map-support'

import express from 'express'
import cookieParser from 'cookie-parser'
import http from 'http'
import config from 'config'
import { rootLogger } from '../shared/logger'
import { initializeClientConnection, clientSecret } from './client'
import { initializeControllerConnection } from './controller'
import { initializeManagerConnection } from './manager'
import { WebSocketServer } from 'ws'
sourceMapSupport.install()

const app = express()
app.use(cookieParser())
app.use((req, res, next) => {
  const cookieKey = 'OBSLINK_CLIENT_SECRET'
  if (req.cookies[cookieKey] === clientSecret) { next(); return }
  if (req.query.secret === clientSecret) {
    res.cookie(cookieKey, clientSecret)
    next(); return
  }
  res.sendStatus(401)
})
app.use(express.static('build/browser'))

const wsRoutes = new Map(
  Object.entries({
    '/client': initializeClientConnection,
    '/controller': initializeControllerConnection,
    '/manager': initializeManagerConnection
  }).map(([route, handler]) => {
    return [route, { handler, server: new WebSocketServer({ noServer: true }) }]
  })
)

const server = http.createServer(app)
server.on('upgrade', (request, socket, head) => {
  const url = new URL(request.url ?? '', `http://${request.headers.host ?? ''}`)
  const wsRoute = wsRoutes.get(url.pathname)
  if (wsRoute == null) return
  wsRoute.server.handleUpgrade(request, socket, head, (ws) => {
    try {
      void wsRoute.handler(ws, url.searchParams)
    } catch (error) {
      rootLogger.error('Error in websocket initialization', url.pathname, url.searchParams, error)
    }
  })
})

const port = config.get<number>('port')
server.listen(port, () => {
  rootLogger.info(`Running at http://localhost:${port}`)
})
