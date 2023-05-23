import { id, secret } from './params'
import { compress, decompress } from '../shared/compression'
import { rootLogger as logger } from '../shared/logger'
import { safeClose } from '../shared/safeClose'
import { BufferedQueue } from '../shared/bufferedQueue'

const display = (() => {
  const element = document.getElementById('display')
  if (element === null) throw new Error()
  return element
})()

display.innerText = id

let serverSocket: WebSocket | undefined
const sendQueue = new BufferedQueue<any>()
sendQueue.on((messages) => {
  const compressed = compress(messages)
  logger.info('sending', compressed.byteLength, 'bytes')
  serverSocket?.send(compressed)
})

const locals = new Map<number, WebSocket>()

connect()
function connect (): void {
  const params = new URLSearchParams()
  params.append('id', id)
  params.append('secret', secret ?? '')
  const location = window.location
  const pathname = location.pathname
  const url = (location.protocol === 'https:' ? 'wss://' : 'ws://') +
    location.host + pathname.substring(0, pathname.lastIndexOf('/')) +
    `/client?${params.toString()}`

  serverSocket = new WebSocket(url)
  serverSocket.binaryType = 'arraybuffer'
  serverSocket.onopen = () => {
    display.style.color = 'white'
  }
  serverSocket.onmessage = (e) => {
    logger.info('received', e.data.byteLength, 'bytes')
    const uint8View = new Uint8Array(e.data)
    const messages = decompress(uint8View)
    for (const message of messages) {
      handleServerMessage(message)
    }
  }

  serverSocket.onclose = (event) => {
    serverSocket = undefined
    display.style.color = 'red'
    Array.from(locals.values()).forEach(local => { safeClose(local, event.code, event.reason) })
    setTimeout(connect, 1000)
  }
}

function handleServerMessage (message: any[]): void {
  const op: number = message[0]
  const controllerNumber = message[1]
  const local = locals.get(controllerNumber)
  if (op === 0) createLocal(controllerNumber, message[2])
  if (local === undefined) return
  if (op === 1) local.send(JSON.stringify(message[2]))
  if (op === 2) safeClose(local, message[2], message[3])
}

function createLocal (controllerNumber: number, target: string): void {
  const socket = new WebSocket(target)
  locals.set(controllerNumber, socket)
  socket.onopen = () => {
    display.style.color = 'green'
    sendQueue.queue([0, controllerNumber])
  }
  socket.onmessage = (e) => {
    const json = JSON.parse(e.data)
    sendQueue.queue([1, controllerNumber, json])
  }
  socket.onclose = (e) => {
    sendQueue.queue([2, controllerNumber, e.code, e.reason])
    locals.delete(controllerNumber)
    if (locals.size === 0) display.style.color = 'white'
  }
}
