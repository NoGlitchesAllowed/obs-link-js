import type ws from 'ws'
import config from 'config'
import { rootLogger } from '../shared/logger'
import { Client } from './client'
import { Signal } from '../shared/signal'

let nextControllerNumber = 0
export async function initializeControllerConnection (
  socket: ws.WebSocket,
  parameters: URLSearchParams
): Promise<void> {
  const ident = parameters.get('ident') ?? 'noident'
  const secret = parameters.get('secret')
  const clientId = parameters.get('clientId')
  const target = parameters.get('target') ?? config.get<string>('defaultTarget')
  const logger = rootLogger.child(`controller ${ident}->${clientId ?? '?'}->${target}`)

  if (clientId === null || secret !== config.get('secrets.controller')) {
    logger.warn(`Failed to connect: missing or invalid parameters: ${parameters.toString()}`)
    socket.close(1008, 'missing or invalid parameters')
    return
  }

  const client = Client.get(clientId)
  if (client == null) {
    logger.warn(`Failed to connect: invalid targetId: ${clientId}`)
    socket.close(1008, 'invalid targetId')
    return
  }

  const controllerNumber = nextControllerNumber++

  const messageBuffer: Buffer[] = []
  const bufferMessage: (msg: Buffer) => void = (msg) => messageBuffer.push(msg)
  let messageHandler = bufferMessage
  socket.on('message', (msg: Buffer) => { messageHandler(msg) })

  socket.on('close', (event: ws.CloseEvent) => {
    client.controllers.delete(controllerNumber)
    if (client.socket.readyState === 1) {
      client.sendQueue.queue([2, controllerNumber, event.code, event.reason])
    }
    logger.info(`Disconnected (${event.code}/${event.reason})`)
  })

  logger.info('Connecting to client...')
  const start = new Signal<boolean>()
  client.controllers.set(controllerNumber, { socket, start })
  client.sendQueue.queue([0, controllerNumber, target])
  const startResult = await start.promise
  if (!startResult) {
    logger.warn('Failed to connect: client refused local connection')
    return
  }

  const sendMessage = (msg: Buffer): void => {
    const json = JSON.parse(msg.toString())
    client.sendQueue.queue([1, controllerNumber, json])
  }

  messageBuffer.forEach(sendMessage)
  messageHandler = sendMessage
  logger.info('Connected')
}
