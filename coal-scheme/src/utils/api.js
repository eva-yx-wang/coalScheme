//专门处理AJAX

//1 导入axios包
import axios from "axios";
//2 用element ui的message组件做弹窗
//js文件中必须单独引入{ Message }组件
import { Message } from "element-ui";
import router from "../router";

//6 response 拦截器的配置信息
axios.interceptors.request.use(
  (config) => {
    //如果sessionStorage存在token
    if (window.sessionStorage.getItem("tokenStr")) {
      //设置token到requestHeader
      //根据后端设置的header参数，详情查看doc.html>Authoriza>参数名称和in
      config.headers["Authorization"] = window.sessionStorage.getItem(
        "tokenStr"
      );
    }
    return config;
  },
  (error) => {
    console.log(error);
  }
);

//3 response 拦截器的配置信息
//3.1 判断服务器是否正常>请求是否成功>业务逻辑是否正确
axios.interceptors.response.use(
  //3.1.1 正常访问接口, 拿到json对象即success.data
  (success) => {
    //存在接口且返回状态码为200
    if (success.status && success.status == 200) {
      //返回(1)响应码code, (2)响应信息message, (3)响应后台数据object
      //3.1.1.1 业务逻辑错误, 不能操作
      if (
        success.data.code == 500 ||
        success.data.code == 401 ||
        success.data.code == 403 ||
        success.data.message == "用户名或密码错误！"
      ) {
        Message.error({ message: success.data.message });
        return;
      }
      //3.1.1.2 内部逻辑正确, 执行操作
      //有返回操作信息则显示
      if (success.data.message) {
        Message.success({ message: success.data.message });
      }
    }
    //3.1.2 返回json对象
    return success.data;
  },
  //3.2 服务器异常正常工作
  (error) => {
    //3.2.1 有明确状态码的错误
    if (error.response.code == 504 || error.response.code == 404) {
      Message.error({ message: "服务器在外太空o(╯□╰)o" });
    } else if (error.response.code == 403) {
      Message.error({ message: "未授权，请联系管理员" });
    } else if (error.response.code == 401) {
      Message.error({ message: "未登录，请登录" });
      //重定向登录
      router.replace("/");
    } else {
      //3.2.2 无明确状态码的错误
      if (error.response.data.message) {
        Message.error({ message: error.response.data.message });
      } else {
        Message.error({ message: "未知错误" });
      }
    }
    //3.2.3 返回
    return;
  }
);

//4 request 拦截器
//备用后期加前置路径
let base = "";

//不同request方式的config
//post request 
export const postRequest = (url, paras) => {
  return axios({
    method: "post",
    //注意用的是``不是""
    url: `${base}${url}`,
    data: paras,
  });
};

//5. 用RequestBody封装参数的请求
//put request
export const putRequest = (url, paras) => {
  return axios({
    method: "put",
    url: `${base}${url}`,
    //请求携带的参数
    data: paras,
  });
};

//get request
export const getRequest = (url, paras) => {
  return axios({
    method: "get",
    url: `${base}${url}`,
    data: paras,
  });
};

//delete request
export const deleteRequest = (url, paras) => {
  return axios({
    method: "delete",
    url: `${base}${url}`,
    data: paras,
  });
};

//没用RequestBody封装参数时用的axios原生请求
//原装axios request
/* 注意params, headers都是固定写法
this.$axios.get("your url", {
  params:{
    api_paraName: api_paraValue
  },
  headers:{}
})*/
