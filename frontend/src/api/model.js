import api from './index'
import { axios } from '../utils/request'

export function classify (parameter) {
  return axios({
    url: api.Classify,
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: parameter
  })
}
