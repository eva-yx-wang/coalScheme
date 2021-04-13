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
            <el-button
              class="input-button"
              type="primary"
              @click="submitForm('ruleFormRef')"
              >登录</el-button
            >
            <!-- @click事件绑定resetForm方法 传入表单名字ruleFormRef-->
            <el-button class="input-button" @click="resetForm('ruleFormRef')"
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
  data() {
    return {
      //表单数据
      ruleForm: {
        username: "admin",
        password: "123456",
      },
      //表单验证规则对象
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
    submitForm(formName) {
      //valid为预验证结果, async+await异步解析成可查看数据
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          //请求路径:login, 传入表单数据this.ruleForm, 注意是表单数据不是表单引用！！！
          //只拿返回对象中属性中的的data对象，重名为result
          const { data: result } = await this.$http.post(
            "login",
            this.ruleForm
          );
          if (result.meta.status !== 200) {
            return this.$message.error("登录失败！");
          } else {
            this.$message.success("登录成功！");
            //storage token到Browser
            window.sessionStorage.setItem("token", result.data.token);
            //跳转到/home页面
            this.$router.push("/home");
          }
        } else {
          this.$message.error("输入非法!");
          return false;
        }
      });
    },
    //重置
    resetForm(formName) {
      this.$refs[formName].resetFields();
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
