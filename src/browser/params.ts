import { generateId } from './generateId'

const params = new URLSearchParams(window.location.search.substring(1))
export const secret = params.get('secret')
export const id: string = (() => {
  const paramsId = params.get('id')
  if (paramsId !== null) return paramsId

  const storage = window.localStorage
  const existingId = storage.getItem('id')
  if (existingId !== null) return existingId

  const newId = generateId()
  storage.setItem('id', newId)
  return newId
})()
