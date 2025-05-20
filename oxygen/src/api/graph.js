import axios from 'axios'

// 查询红楼梦信息
export const queryHlmInfo = params => {
  return axios.post('/api/web/graph/hlmInfo/queryHlmInfo', params)
}

// 查询红楼梦关系
export const queryHlmRel = params => {
  return axios.post('/api/web/graph/hlmRel/queryHlmRel', params)
}
