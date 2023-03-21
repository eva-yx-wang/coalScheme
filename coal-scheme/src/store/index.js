import Vue from "vue";
import Vuex from "vuex";
//import coal from "./modules/coal.js";
Vue.use(Vuex);

export default new Vuex.Store({
  //Vuex模块化
  //  modules: {
  //    coal,
  //  },

  state: {
    routes: [],
  },
  //监听state对象的值的最新状态(computed)
  getters: {},
  //修改state内值的method(同步但阻塞了)
  mutations: {
    //初始化菜单Routes
    initRoutes(state, data){
      state.routes = data;
    }
  },
  //修改state内值的method(异步)
  actions: {},
  modules: {},
});
