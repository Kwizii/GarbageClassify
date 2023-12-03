import api from './index'
import { axios } from '../utils/request'

export function getGiftList () {
  return axios({
    url: api.GiftList,
    method: 'get'
  })
}

export function createGift (param) {
  return axios({
    url: api.CreateGift,
    method: 'post',
    data: param
  })
}

export function updateGift (param) {
  return axios({
    url: api.UpdateGift,
    method: 'post',
    data: param
  })
}

export function delGift (giftId) {
  return axios({
    url: api.DelGift,
    method: 'post',
    params: {
      'giftId': giftId
    }
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
