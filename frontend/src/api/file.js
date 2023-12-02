import api from './index'
import { axios } from '../utils/request'

export function uploadImage (file) {
  const formData = new FormData()
  formData.append('smfile', file)
  return axios({
    url: api.UploadFile,
    method: 'post',
    headers: {
      'Authorization': 'j9SFJy5cQryzBpMp7Bc1oxioIhZW4s6g',
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
  // if (resp.code === 'image_repeated') { return resp.images }
  // if (resp.success) {
  //   return resp.data.url
  // }
  // return ''
}

export function postprocess (response) {
  if (response.code === 'image_repeated') { return response.images }
  if (response.success) {
    return response.data.url
  }
  return ''
}
