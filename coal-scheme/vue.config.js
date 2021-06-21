//此文件发生任何非注释修改都要重启项目才会生效！
module.exports = {
  //基本请求路径, 相当于axios.defaults.baseURL = "http://localhost:8082";
  devServer: {
    //主机名
    host: "localhost",
    //vue 项目运行的端口号
    port: 8082,
    //关闭自动打开页面
    open: false,
    //设置代理
    //属性名必须用proxy，否则无法启动项目！
    proxy: {
      //拦截以"/"开头的请求
      "/": {
        //设置真正要请求的协议+域名+端口号，在后面直接拼接axio.get("url")内的url
        target: "http://localhost:8081",
        //是否开跨域
        changeOrigin: true,
        // 是否启用  websockets;
        ws: true,
      },
    },
  },
};
