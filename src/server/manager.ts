import type ws from 'ws'
import config from 'config'
import { Client } from './client'
import { rootLogger } from '../shared/logger'

const managers = new Set<ws.WebSocket>()
export async function initializeManagerConnection (
  socket: ws.WebSocket,
  parameters: URLSearchParams
): Promise<void> {
  const ident = parameters.get('ident') ?? 'noident'
  const secret = parameters.get('secret')
  const logger = rootLogger.child(`manager ${ident}`)

  if (secret !== config.get('secrets.manager')) {
    logger.warn(`Failed to connect: missing or invalid parameters: ${parameters.toString()}`)
    socket.close(1008, 'missing or invalid parameters')
    return
  }

  managers.add(socket)
  socket.on('close', () => {
    managers.delete(socket)
  })

  sendClientIds([socket])
}

export function sendClientIds (
  sockets: ws.WebSocket[] = Array.from(managers.values())
): void {
  const ids = Client.all().map(client => client.id)
  sockets.forEach(socket => { socket.send(JSON.stringify(ids)) })
}
