export class Signal<T = void> {
  promise: Promise<T>
  resolve!: (t: T) => void
  reject!: (reason?: any) => void
  constructor () {
    this.promise = new Promise<T>((resolve, reject) => {
      this.resolve = resolve
      this.reject = reject
    })
  }
}
