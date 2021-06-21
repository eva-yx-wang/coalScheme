<template>
  <!-- Header -->
  <el-container>
    <el-header>
      <!-- Home title -->
      <div class="logo-box">
        <img class="logo" src="../assets/ncepu_logo.jpg" alt="" />
        <span>配煤管理系统</span>
      </div>
      <el-button class="logout-btn" type="danger" @click="logout"
        >退出</el-button
      >
    </el-header>
    <!-- Main -->
    <el-container>
      <!-- Aside -->
      <!--router 使inde可以直接访问路径-->
      <el-aside width="200px">
        <el-menu
          default-active="2"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          background-color="#333744"
          text-color="#fff"
          active-text-color="#ffd04b"
          router
        >
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-folder-opened"></i>
              <span>煤仓数据管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/coal/list">
                <!-- 跳转到CoalInfo -->
                <i class="el-icon-search"></i>
                煤种预选
              </el-menu-item>
              <el-menu-item index="/coal/analysis">
                <!-- 跳转CoalAnalysis -->
                <i class="el-icon-data-line"></i>
                煤质分析
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-scissors"></i>
              <span>环境参数控制</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/env/standard">
                <!-- 跳转到CoalInfo -->
                <i class="el-icon-thumb"></i>
                煤质标准检查
              </el-menu-item>
              <el-menu-item index="/env/cost">
                <i class="el-icon-pie-chart"></i>
                <!-- 跳转CoalAnalysis -->供电成本分析
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-collection"></i>
              <span>配煤方案管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="">
                <!-- 跳转到CoalInfo -->
                <i class="el-icon-data-analysis"></i>
                生产效益分析
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="">
                <!-- 跳转到CoalInfo -->
                <i class="el-icon-postcard"></i>
                账号信息
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <!-- 只有main中的内容进行了路由跳转, 其余组分和Home界面一致 -->
        <el-main>
          <router-view> </router-view>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
export default {
  mounted(){
    this.$router.push("/welcome");
  },
  methods: {
    //1.退出则清空token, 并返回login
    logout() {
      window.sessionStorage.clear();
      this.$router.push("/login");
    },
    //一级标签打开关闭
    handleOpen() {},
    handleClose() {},
  },
};
</script>

<style lang="less" scoped>
//Container
.el-container {
  height: 100%;
}
//Header
.el-header {
  display: flex; //button靠右
  justify-content: space-between;
  align-items: center; //header内部按钮居中
  background-color: #373d41;

  .logo-box {
    display: flex;
    align-items: center;
    color: #fff;
    .logo {
      height: 60px;
      width: 60px;
      border-radius: 50%;
    }
    //Header title
    span {
      font-size: 22px;
      margin-left: 10px;
    }
  }
}

//Aside
.el-menu-item-group__title {
  display: none;
}
.el-aside {
  background-color: #333744;
}
//Main
.el-main {
  background-color: #eaedf1;
}
</style>
