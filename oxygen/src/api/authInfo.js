import axios from 'axios'

//--------------注册登录-----------------
//--------------账号-----------------
// 修改密码
export const updatePwd = params => {
  return axios.post('/api/web/accountInfo/updatePwd', params)
}

//--------------角色-----------------


//--------------菜单-----------------
export const getMenuList = data => axios.post('/api/web/menu/list', data)
// 系统初始化菜单
export const queryMenuTree = params => {
  return axios.post('/api/web/menu/queryMenuTree', params)
}

export const addMenu = data => axios.post('/api/web/menu/add', data)

export const updateMenu = data => axios.post('/api/web/menu/update', data)

export const deleteMenu = menuId => axios.post('/api/web/menu/delete', { menuId })

