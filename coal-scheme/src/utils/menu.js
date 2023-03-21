//import router from "../router/index";
import { getRequest } from "./api";

export const initMenu = (router, store) => {
  //初始化后不初始化
  if (store.state.routes.length > 0) {
    return;
  }

  getRequest("").then((data) => {
    if (data) {
      //格式化data为formatedRoutes
      let formatedRoutes = formatRoutes(data);
      //添加主路由router
      router.addRoutes(formatedRoutes);
      //存formatedRoutes到store/index.js/Vuex
      store.commit("initRoutes", formatedRoutes);
    }
  });
};

//格式化data函数
export const formatRoutes = (routes) => {
  let formatedRoutes = [];
  routes.forEach((router) => {
    let { component, children } = router;
    if (children && children instanceof Array) {
      children = formatRoutes(children);
    }
    //导入所有views层子组件
    let formatedRoutes = {
      component(resolve) {
        require(["./views" + component + ".Vue"], resolve);
      },
    };
    //注册formatedRoutes到router/index.js
    formatedRoutes.push(formatedRoutes);
  });
  return formatedRoutes;
};
