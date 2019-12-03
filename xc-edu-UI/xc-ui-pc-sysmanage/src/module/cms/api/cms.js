import http from './../../../base/api/public'
import querystring from 'querystring'
let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre;

//页面查询
export const page_list = (page, size, params) => {
  //使用js拼接参数
  let para = querystring.stringify(params);
  //请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl + '/cms/page/list/' + page + '/' + size + '?' + para);
}
//添加页面
export const add = (params) => {
  return http.requestPost(apiUrl + '/cms/page/add', params);
}
//查询单个页面信息
export const fingByid = (params) => {
  return http.requestQuickGet(apiUrl + '/cms/page/findById/' + params);
}
//请求修改
export const edit = (pageId, pageForm) => {
  return http.requestPut(apiUrl + '/cms/page/edit/' + pageId, pageForm);
}

//删除页面
export const deleteByid=(pageId) =>{
  return http.requestDelete(apiUrl + '/cms/page/del/' + pageId);
}

//页面发布
export const postpage=(pageid)=>{
  return http.requestPost(apiUrl+'/cms/page/postPage/'+pageid);
}




