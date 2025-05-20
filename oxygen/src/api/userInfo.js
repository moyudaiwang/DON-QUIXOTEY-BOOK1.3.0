import axios from 'axios'

// 查询个人信息
export const queryByUserId = params => {
  return axios.post('/api/web/userInfo/queryByUserId', params)
}
// 修改个人信息
export const updateInfo = params => {
  return axios.post('/api/web/userInfo/updateInfo', params)
}
// 修改头像
export const updateAvatar = params => {
  return axios.post('/api/web/userInfo/updateAvatar', params)
}
