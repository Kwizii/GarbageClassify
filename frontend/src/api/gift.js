import api from './index'
import { axios } from '../utils/request'

export function getGiftList () {
  return axios({
    url: api.GiftList,
    method: 'get'
  })
}

export function createGiftOrder (param) {
  return axios({
    url: api.CreateGiftOrder,
    data: param,
    method: 'post'
  })
}

export function getGiftOrderList (param) {
  return axios({
    url: api.GetGiftOrderList,
    method: 'get',
    params: param
  })
}
