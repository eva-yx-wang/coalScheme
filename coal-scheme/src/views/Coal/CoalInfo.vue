<template>
  <div>
    <!-- html元素el-card对应Card组件, el-card内部对CoalInfo封闭
    
   所有在当前template内使用的html元素(如el-card)的作用域是html元素CoalInfo的data和method区域
   如el-card @click绑定的函数toAnalyse是去CoalInfo内demethod域中查找-->
    <el-card shadow="never">
      <!-- 添加煤种 
      1 点击按钮则显示弹窗
      -->
      <el-button type="success" @click="addFormVisible = true" class="add-btn"
        >添加煤种</el-button
      >
      <!-- 跳转查询煤种 -->
      <el-button
        type="primary"
        icon="el-icon-search"
        @click="toAnalyse"
        :disabled="selectedItems.length <= 1"
        class="analysis-btn"
        >分析煤质数据</el-button
      >

      <el-dialog title="新建煤种信息" :visible.sync="addFormVisible">
        <!-- 2 弹窗显示表单 -->
        <el-form :model="newCoal">
          <!-- 2.1 表单项 title & input
          v-model 绑定到data()内数据：coalInplo.coalId-->
          <el-form-item label="煤种编号" :label-width="formLabelWidth">
            <el-input v-model="newCoal.coalId" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="煤种名称" :label-width="formLabelWidth">
            <el-input v-model="newCoal.coalName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="煤种单价" :label-width="formLabelWidth">
            <el-input v-model="newCoal.coalPrice" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="煤种存量" :label-width="formLabelWidth">
            <el-input v-model="newCoal.coalLoad" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="煤种产地" :label-width="formLabelWidth">
            <el-input
              v-model="newCoal.coalOrigin"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-form>
        <!-- 3 弹出框的操作按钮 
        点击任何一个都关闭弹窗-->
        <div slot="footer" class="dialog-footer">
          <el-button @click="addFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addCoal">确 定</el-button>
        </div>
      </el-dialog>

      <!-- 煤种表格 
      @selection-change:每次勾选变化时都会触发这个事件
      -->
      <el-table
        ref="multipleTable"
        @selection-change="handleSelectionChange"
        :data="coalList"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="handleDisabled"
        ></el-table-column>
        <el-table-column prop="coalId" label="煤种编号" width="100">
        </el-table-column>
        <el-table-column prop="coalName" label="煤种名称" width="150">
        </el-table-column>
        <el-table-column prop="coalPrice" label="煤种单价" width="150">
        </el-table-column>
        <el-table-column prop="coalLoad" label="煤种存量" width="150">
        </el-table-column>
        <el-table-column prop="coalOrigin" label="煤种产地" width="120">
        </el-table-column>
        <el-table-column label="操作" width="180">
          <!-- slot-scope
            每行点击传入行索引scope.$index和行数据scope.row -->
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="editCoal(scope.row)"
              type="primary"
              icon="el-icon-edit"
              circle
            ></el-button>
            <el-button
              size="mini"
              @click="deleteCoal(scope.row)"
              type="danger"
              icon="el-icon-delete"
              circle
            ></el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 修改煤种弹窗 -->
      <el-dialog title="修改煤种信息" :visible.sync="editFormVisible">
        <!-- 2 弹窗显示表单 -->
        <el-form :model="coalInplo">
          <!-- 2.1 表单项 title & input
          v-model 绑定到data()内数据：coalInplo.coalId-->
          <el-form-item label="煤种编号" :label-width="formLabelWidth">
            <el-input v-model="coalInplo.coalId" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="煤种名称" :label-width="formLabelWidth">
            <el-input
              v-model="coalInplo.coalName"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="煤种单价" :label-width="formLabelWidth">
            <el-input
              v-model="coalInplo.coalPrice"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="煤种存量" :label-width="formLabelWidth">
            <el-input
              v-model="coalInplo.coalLoad"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="煤种产地" :label-width="formLabelWidth">
            <el-input
              v-model="coalInplo.coalOrigin"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-form>
        <!-- 3 弹出框的操作按钮 
        点击任何一个都关闭弹窗-->
        <div slot="footer" class="dialog-footer">
          <el-button @click="editFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateCoal">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
//1.导入echarts
//import echarts from 'echarts';

export default {
  name: "CoalInfo",
  data() {
    return {
      coalList: [],
      //页面刚加载时弹出框不显示, 每次点击按钮后才改为true
      dialogTableVisible: false,
      addFormVisible: false,
      editFormVisible: false,
      //添加煤种的数据
      newCoal: {
        coalId: "",
        coalName: "",
        coalPrice: 0,
        coalLoad: 0,
        coalOrigin: "",
      },
      coalInplo: {
        coalId: "",
        coalName: "",
        coalPrice: "",
        coalLoad: "",
        coalOrigin: "",
      },
      formLabelWidth: "120px",
      selectedItems: [],
    };
  },
  mounted() {
    //执行方法必须加()
    //mounted在DOM渲染之前创建，如果更新mounted必须刷新页面
    this.initCoalList();
  },

  methods: {
    initCoalList() {
      //跨域获取coalList
      this.getRequest("/coal/list/get").then((response) => {
        if (response) {
          this.coalList = response;
        }
      });
    },
    //commit addition form
    addCoal() {
      if (this.newCoal) {
        this.postRequest("/coal/list/add", this.newCoal).then((response) => {
          if (response) {
            //重新获取coalList
            this.initCoalList();
            this.addFormVisible = false;
          }
        });
      }
    },
    //open edition form
    editCoal(coalData) {
      //this.coalInplo浅拷贝coalData，而不是动态绑定到coalData
      Object.assign(this.coalInplo, coalData);
      //显示编辑弹窗
      this.editFormVisible = true;
    },
    //commit update to back
    updateCoal() {
      if (this.coalInplo) {
        this.$confirm("确认提交煤种信息?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.putRequest("/coal/list/update", this.coalInplo).then(
              (response) => {
                //成功更新
                if (response) {
                  //重新获取coalList
                  this.initCoalList();
                  this.editFormVisible = false;
                }
              }
            );
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消提交",
            });
          });
      }
    },
    deleteCoal(coalData) {
      this.$confirm("确认删除该煤种信息?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.deleteRequest("/coal/list/deleteSingle/" + coalData.coalId).then(
          (response) => {
            //成功更新
            if (response) {
              //重新获取coalList
              this.initCoalList();
            }
          }
        );
      });
    },

    //任意复选框发生状态变化则执行此函数
    handleSelectionChange(selectedRows) {
      //添加到列表selectedItems
      this.selectedItems = selectedRows;
    },
    handleDisabled() {
      return true;
      // if (this.selectedItems.length <= 2) {
      // }
    },
    toAnalyse() {
      //清理之前操作的选择的煤种缓存
      const token = window.sessionStorage.getItem("tokenStr");
      window.sessionStorage.clear();
      window.sessionStorage.setItem("tokenStr", token);

      let ids = "?";
      this.selectedItems.forEach((item) => {
        ids += "ids=" + item.coalId + "&";
        window.sessionStorage.setItem(item.coalId, item.coalName);
      });
      window.sessionStorage.setItem("coalIds", ids);
      //跳转到analysis查看分析图
      this.$router.push("/coal/analysis");
    },
  },
};
</script>

<style lang="less" scoped>
.add-btn,
.analysis-btn {
  float: right;
  margin-right: 30px;
}

.el-table .cell,
.el-table--border td:first-child .cell,
.el-table--border th:first-child .cell {
  text-align: center;
}
</style>
