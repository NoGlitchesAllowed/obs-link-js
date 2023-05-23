import { Signal } from './signal'

export class BufferedQueue<T = any> {
  _handler: ((messages: T[]) => void) | undefined = undefined
  _buffer: T[] = []
  _currentTask: Signal | undefined
  _delay = 0

  on (handler: (messages: T[]) => void): void {
    if (this._handler !== null) throw new Error('Handler already set')
    this._handler = handler
  }

  queue (message: T): void {
    if (this._handler == null) throw new Error('No handler set')
    this._buffer.push(message)
    if (this._currentTask !== undefined) return
    this._currentTask = new Signal()
    setTimeout(() => {
      void this.flush()
    }, 100)
  }

  async waitForFinish (): Promise<void> {
    const task = this._currentTask
    if (task === undefined) return
    await task.promise
  }

  async flush (): Promise<void> {
    const bufferCopy = [...this._buffer]
    this._buffer = []
    this._handler?.(bufferCopy)
    this._currentTask?.resolve()
    this._currentTask = undefined
  }
}
