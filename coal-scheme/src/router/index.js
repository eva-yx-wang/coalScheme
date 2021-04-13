import Vue from "vue";
import VueRouter from "vue-router";
//1.import Login (注意";"结尾)
import Login from "../components/Login.vue";
//4.import Login
import Home from "../components/Home.vue";

Vue.use(VueRouter);
const routes = [
  //注意：path只能单引号
  //3.url重定向
  { path: "/", redirect: "/login" },
  //2. Login注册router
  { path: "/login", component: Login },
  //5. Home注册router
  { path: "/home", component: Home },
];

const router = new VueRouter({
  routes,
});

//6.router defender
//to为将要访问的path, from为发出访问请求的path, next为放行方法
//=>表示接受回调函数
router.beforeEach((to, from, next) => {
  //6.1 登录过直接放行
  if (to.path === "/login") {
    return next();
  } else {
    //6.2 未登录直接跳/login
    const result = window.sessionStorage.getItem("token");
    if (result == null) {
      return next("/login");
    } else next();
  }
});

export default router;
