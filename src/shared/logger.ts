abstract class LoggerBase {
  abstract log (level: ('info' | 'warn' | 'error'), ...args: any[]): void
  info = (...args: any[]): void => { this.log('info', ...args) }
  warn = (...args: any[]): void => { this.log('warn', ...args) }
  error = (...args: any[]): void => { this.log('error', ...args) }
  child (...extraArgs: any[]): LoggerBase {
    return new ForwardingLogger(this, extraArgs)
  }
}

class ForwardingLogger extends LoggerBase {
  parent: LoggerBase
  extraArgs: string[]
  constructor (parent: LoggerBase, extraArgs: string[]) {
    super()
    this.parent = parent
    this.extraArgs = extraArgs
  }

  log (level: 'info' | 'warn' | 'error', ...args: any[]): void {
    this.parent.log(level, ...this.extraArgs, ...args)
  }
}

const timestamp = (): string => new Date().toISOString().substring(11, 19)
class RootLogger extends LoggerBase {
  log = (level: 'info' | 'warn' | 'error', ...args: any[]): void => {
    console[level](timestamp(), level.toUpperCase(), ...args)
  }
}
export const rootLogger = new RootLogger()
