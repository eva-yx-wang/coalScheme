import Vue from "vue";
import VueRouter from "vue-router";
//1.import Login (注意";"结尾)
import Login from "../components/Login.vue";
//4.import Login
import Home from "../components/Home.vue";
import Welcome from "../views/Welcome/Welcome.vue";
import StandSettings from "../views/Environment/StandSettings";
import CostSettings from "../views/Environment/CostSettings";
import CoalInfo from "../views/Coal/CoalInfo.vue";
import CoalAnalysis from "../views/Coal/CoalAnalysis.vue";

Vue.use(VueRouter);
const routes = [
  //注意：path只能单引号
  //3.url重定向
  //{ path: "/", redirect: "/login" },
  //2. 注册router：跳转到Login组件
  { path: "/login", component: Login },
  //5. Home注册router
  {
    path: "/home",
    component: Home,
    //页面不跳只内容刷新
    children: [
      { path: "/welcome", name: "Welcome", component: Welcome },
      { path: "/coal/list", name: "CoalInfo", component: CoalInfo },
      { path: "/coal/analysis", name: "CoalAnalysis", component: CoalAnalysis },
      {
        path: "/env/standard",
        name: "StandSettings",
        component: StandSettings,
      },
      { path: "/env/cost", name: "CostSettings", component: CostSettings },
    ],
  },
];

const router = new VueRouter({
  routes,
});

//6.router defender
//to为将要访问的path, from为发出访问请求的path, next为放行方法
//=>表示接受回调函数

export default router;
