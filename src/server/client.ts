import type ws from 'ws'
import config from 'config'
import { rootLogger } from '../shared/logger'
import { type Signal } from '../shared/signal'
import { compress, decompress } from '../shared/compression'
import { BufferedQueue } from '../shared/bufferedQueue'
import { safeClose } from '../shared/safeClose'
import { sendClientIds as updateManager } from './manager'

export const clientSecret = config.get('secrets.client')
export function initializeClientConnection (socket: ws.WebSocket, parameters: URLSearchParams): Client | undefined {
  const secret = parameters.get('secret')
  const id = parameters.get('id') ?? ''
  const logger = rootLogger.child(`client ${id}`)
  if (id === null || secret !== clientSecret) {
    logger.warn(`Failed to connect: missing or invalid parameters: ${parameters.toString()}`)
    socket.close(1008, 'missing or invalid parameters')
    return
  }

  if (Client.get(id) !== undefined) {
    logger.warn('Failed to connect: Duplicate client')
    socket.close(1008, 'A client with this ID is already connected')
    return
  }

  const client = new Client(socket, id, logger)
  logger.info('Connected')
  return client
}

export class Client {
  private static readonly clients = new Map<string, Client>()
  static get = (clientId: string): Client | undefined => {
    return this.clients.get(clientId)
  }

  static all = (): Client[] => {
    return Array.from(Client.clients.values())
  }

  socket: ws.WebSocket
  id: string
  private readonly logger: any
  sendQueue = new BufferedQueue()
  controllers = new Map<number, { socket: ws.WebSocket, start: Signal<boolean> }>()

  constructor (socket: ws.WebSocket, id: string, logger: any) {
    this.socket = socket
    this.id = id
    this.logger = logger
    this.sendQueue.on(messages => {
      const compressedMessage = compress(messages)
      socket.send(compressedMessage)
    })

    socket.on('message', this.onMessage.bind(this))
    socket.on('close', this.onClose.bind(this))
    Client.clients.set(id, this)
    updateManager()
  }

  onMessage (buffer: Buffer): void {
    const messages = decompress(buffer)
    for (const message of messages) {
      this.handleClientMessage(message)
    }
  }

  onClose (event: ws.CloseEvent): void {
    const controllers = Array.from(this.controllers.values())
    for (const controller of controllers) {
      controller.socket.close(event.code, event.reason)
    }
    Client.clients.delete(this.id)
    this.logger.info(`Disconnected (${event.code}/${event.reason})`)
    updateManager()
  }

  handleClientMessage (message: any[]): void {
    const op: number = message[0]
    const controllerNumber: number = message[1]
    const controller = this.controllers.get(controllerNumber)
    if (controller === undefined) return
    if (op === 0) controller.start.resolve(true)
    if (op === 1) controller.socket.send(JSON.stringify(message[2]))
    if (op === 2) {
      controller.start.resolve(false)
      safeClose(controller.socket, message[2], message[3])
    }
  }
}
