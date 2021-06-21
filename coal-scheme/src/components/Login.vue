<template>
  <div class="login_container">
    <div class="login_box">
      <!-- avatar area -->
      <div class="avatar_box">
        <img src="../assets/logo.png" alt="" />
      </div>
      <!-- input form -->
      <div>
        <!-- :model=表单对象 ref=表单的Vue对象-->
        <el-form
          ref="ruleFormRef"
          :model="ruleForm"
          :rules="rules"
          label-width="0px"
          class="demo-ruleForm input-form"
        >
          <!--username -->
          <!-- 动态绑定到ruleForm.username -->
          <el-form-item prop="username">
            <el-input
              placeholder="请输入用户名"
              prefix-icon="el-icon-user-solid"
              v-model="ruleForm.username"
            ></el-input>
          </el-form-item>
          <!-- password -->
          <!-- 动态绑定到ruleForm.password -->
          <el-form-item prop="password">
            <el-input
              placeholder="请输入密码"
              prefix-icon="el-icon-key"
              v-model="ruleForm.password"
              type="password"
            ></el-input>
          </el-form-item>
          <!-- button -->
          <el-form-item>
            <!-- @click事件绑定submitForm方法 传入loginFormRef-->
            <el-button class="input-button" type="primary" @click="submitForm()"
              >登录</el-button
            >
            <!-- @click事件绑定resetForm方法 传入表单名字ruleFormRef-->
            <el-button class="input-button" @click="resetForm()"
              >重置</el-button
            >
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      //表单数据
      ruleForm: {
        username: "",
        password: "",
      },
      //表单验证规则
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }, //'blur'为鼠标失焦时
          //{ min: 3, max: 5, message: 'Length should be 3 to 5', trigger: 'blur' }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          //{ min: 3, max: 5, message: 'Length should be 3 to 5', trigger: 'blur' }
        ],
      },
    };
  },
  methods: {
    //预验证: 提交前检查验证规则
    submitForm() {
      //查看预验证
      this.$refs.ruleFormRef.validate((valid) => {
        //1 预验证通过，则post request (ps:valid为预验证结果)
        if (valid) {
          //1.1 自制postRequest同一处理response结果
          // 用插件属性时记得+this!
          //注意传入this.ruleForm 作为paras对象！！！
          //因为默认端口号是8080, 因此要重设端口号为8081,否则会提示404 not found
          this.$axios.defaults.baseURL = "http://localhost:8081";
          this.postRequest("/login", this.ruleForm).then((response) => {
            if (response) {
              //1.1.1 拿到token并存在sessionStorage内，对应keyName为"tokenStr"
              const tokenStr =
                response.object.tokenHead + response.object.token;
              window.sessionStorage.setItem("tokenStr", tokenStr);
              //1.1.2 跳转url
              //默认replace的端口号是8080, 不用push，避免返回/login页面
              this.$router.replace("/home");
            } 
            //else在api.js统一处理
          });
        } else {
          this.$message.error("输入非法!");
          return false;
        }
      });
    },
    //重置
    resetForm() {
      this.$refs.ruleFormRef.resetFields();
    },
  },
};
</script>

<style lang="less" scoped>
//全局
.login_container {
  background-color: #2b4b6b;
  height: 100%;
}

//登录框
.login_box {
  position: absolute;
  left: 50%; //1.先移动全局的50%
  top: 50%;
  transform: translate(-50%, -50%); //2.再向右下移动自身距离-50%
  width: 450px;
  height: 300px;
  background-color: #fff;
  border-radius: 3px;

  //头像
  .avatar_box {
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    height: 130px;
    width: 130px;
    padding: 10px;
    border: 1px solid #fff;
    border-radius: 50%; //圆形边框
    background-color: #fff;
    box-shadow: 0 0 10px #ddd; //box阴影
    img {
      height: 100%;
      widows: 100%;
      border-radius: 50%; //图像在圆内
      background-color: #eee;
    }
  }
}

//form
.input-form {
  margin-right: 10%;
  margin-left: 10%;
  margin-top: 20%;
}

//button
.input-button {
  margin: 10px 70px;
}
</style>
