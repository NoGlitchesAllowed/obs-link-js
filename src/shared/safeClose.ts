export function safeClose (
  socket: any,
  code: number,
  reason: any
): void {
  let _code = 1000
  const validCode = (
    code >= 1000 &&
    code <= 1014 &&
    code !== 1004 &&
    code !== 1005 &&
    code !== 1006
  ) || (code >= 3000 && code <= 4999)
  if (code !== undefined && validCode) {
    _code = code
  }

  let _reason = `Connection closed (${code})`
  if (reason !== undefined) {
    _reason = reason.toString()
  }

  socket.close(_code, _reason)
}
