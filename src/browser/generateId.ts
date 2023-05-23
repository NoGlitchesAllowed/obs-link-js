// https://stackoverflow.com/a/27747377

// dec2hex :: Integer -> String
// i.e. 0-255 -> '00'-'ff'
function dec2hex (dec: number): string {
  return dec.toString(16).padStart(2, '0')
}

// generateId :: Integer -> String
export function generateId (len: number = 10): string {
  const arr = new Uint8Array(len / 2)
  window.crypto.getRandomValues(arr)
  return Array.from(arr, dec2hex).join('')
}
