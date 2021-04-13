import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./plugins/element.js";
import "./assets/css/global.css";

//引入axios
import axios from "axios"; 
//request root path
axios.defaults.baseURL = `http://127.0.0.1:8888/api/private/v1/`;
//挂载到Vue对象
Vue.prototype.$http = axios; 

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
