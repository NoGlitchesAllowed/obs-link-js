import pako from 'pako'

const encoder = new TextEncoder()
const decoder = new TextDecoder()
export function compress (message: any): Uint8Array {
  const string = JSON.stringify(message)
  const uncompressedBuffer = encoder.encode(string)
  const compressedBuffer = pako.deflateRaw(uncompressedBuffer, { level: 9 })
  return compressedBuffer
}

export function decompress (compressedBuffer: Uint8Array): any {
  const uncompressedBuffer = pako.inflateRaw(compressedBuffer)
  const string = decoder.decode(uncompressedBuffer)
  const message = JSON.parse(string)
  return message
}
