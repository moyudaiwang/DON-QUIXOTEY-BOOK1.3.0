import axios from 'axios'

// 根据模块查询树结构（用于下拉框）
export const queryByModule = params => {
  return axios.post('/api/web/treeRel/queryByModule', params)
}
