//从node_modules导出的Vue instance引用
import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./plugins/element.js";
import "./assets/css/global.css";
import VueResource from "vue-resource";
//导入5.x版本Echarts
import * as echarts from "echarts";
Vue.prototype.$echarts = echarts;
//引入Vuex
import Vuex from "vuex";
//1.1 引入axios
import axios from "axios";
//所有axios发出的请求都以此为baseURL, 不同于路由跳转(如this.$router)仅在同域跳转
//1.3 axio挂载到Vue对象上, 通过this.$axio访问
Vue.prototype.$axios = axios;
Vue.use(Vuex);
Vue.config.productionTip = false;


//暴露拦截器ref，拦截器在utils/api.js内定义
import {
  postRequest,
  getRequest,
  putRequest,
  deleteRequest,
} from "./utils/api";
//请求拦截器设置到Vue对象中
Vue.prototype.postRequest = postRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.deleteRequest = deleteRequest;

//element ui
// import ElementUI from 'element-ui';
// Vue.use(ElementUI);

//倒入echarts
Vue.use(VueResource);
new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
