<template>
  <div>
    <el-card>
      <!-- 6个条件 conditions -->
      <div class="dashboard-box">
        <el-tag type="success">挥发分Vdaf</el-tag>
        <el-progress
          type="dashboard"
          :percentage="sVdafValue"
          :color="sVdafColors"
          ref="sVdafRef"
        ></el-progress>
        <div>
          <el-input-number
            v-model="sVdafValue"
            @change="sVdafChange"
            :min="20"
          ></el-input-number>
        </div>
      </div>
      <div class="dashboard-box">
        <el-tag>水分Mar</el-tag>
        <el-progress
          type="dashboard"
          :percentage="sMarValue"
          :color="sMarColors"
        ></el-progress>
        <div>
          <el-input-number
            v-model="sMarValue"
            @change="sMarChange"
            :min="0"
            :max="12"
          ></el-input-number>
        </div>
      </div>
      <div class="dashboard-box">
        <el-tag type="info">灰分Aar</el-tag>
        <el-progress
          type="dashboard"
          :percentage="sAarValue"
          :color="sAarColors"
        ></el-progress>
        <div>
          <el-input-number
            v-model="sAarValue"
            @change="sAarChange"
            :min="1"
            :max="20"
          ></el-input-number>
        </div>
      </div>
      <div class="dashboard-box">
        <el-tag type="warning">硫分Sad</el-tag>
        <el-progress
          type="dashboard"
          :percentage="sSadValue"
          :color="sSadColors"
        ></el-progress>
        <div>
          <el-input-number
            v-model="sSadValue"
            @change="sSadChange"
            :precision="2"
            :step="0.1"
            :min="0"
            :max="0.68"
          ></el-input-number>
        </div>
      </div>
      <div class="dashboard-box input-box">
        <el-tag type="danger" effect="dark" class="input-tag"
          >低位发热量Qar,net</el-tag
        >
        <el-input-number
          v-model="sQarValue"
          @change="qarnetChange"
          :min="4400"
        ></el-input-number>
      </div>
      <div class="dashboard-box input-box">
        <el-tag type="info" effect="dark" class="input-tag">软化温度ST</el-tag>
        <el-input-number
          v-model="sSoftTValue"
          @change="softTChange"
          :min="1300"
        ></el-input-number>
      </div>
    </el-card>
    <!-- 展示环保标准对比 -->
    <el-card>
      <!-- 为echarts准备一个DOM -->
      <div class="compare-chart" ref="compare"></div>
      <el-button type="success" @click="checkStandard" class="checks-btn"
        >自动检查混煤标准</el-button
      >
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      sVdafValue: 20,
      sVdafColors: [
        //颜色最大值为100
        { color: "#67C23A", percentage: 40 },
        { color: "#e6a23c", percentage: 100 },
      ],
      sMarValue: 12,
      sMarColors: [{ color: "#409EFF", percentage: 12 }],
      sAarValue: 20,
      sAarColors: [{ color: "#909399", percentage: 20 }],
      sSadValue: 0.68,
      sSadColors: [{ color: "#E6A23C", percentage: 20 }],
      sQarValue: 4400,
      sSoftTValue: 1300,
      mixedVdaf: 0,
    };
  },
  mounted() {
    this.compareChart = this.$echarts.init(this.$refs.compare);
    //图标
    this.pathSymbols = {
      //path为图标库对应icon的svg代码，
      //如果一个icon的svg有多个path标签,则需要用空格把所有d命令内容<path d="xxx"></path> 合成一个
      aar:
        "path://M811.707317 1024h-599.414634C94.907317 1024 0 929.092683 0 811.707317v-599.414634C0 94.907317 94.907317 0 212.292683 0h599.414634C929.092683 0 1024 94.907317 1024 212.292683v599.414634c0 117.385366-94.907317 212.292683-212.292683 212.292683zM212.292683 24.97561C109.892683 24.97561 24.97561 109.892683 24.97561 212.292683v599.414634C24.97561 914.107317 109.892683 999.02439 212.292683 999.02439h599.414634c102.4 0 187.317073-84.917073 187.317073-187.317073v-599.414634C999.02439 109.892683 914.107317 24.97561 811.707317 24.97561h-599.414634z M979.043902 934.087805H749.268293l-74.92683-194.809756H329.678049l-69.931708 194.809756H39.960976L409.6 74.926829h182.321951l387.121951 859.160976zM616.897561 594.419512L499.512195 274.731707l-114.887805 319.687805h232.273171z",
      mar:
        "path://M811.707317 1024h-599.414634C94.907317 1024 0 929.092683 0 811.707317v-599.414634C0 94.907317 94.907317 0 212.292683 0h599.414634C929.092683 0 1024 94.907317 1024 212.292683v599.414634c0 117.385366-94.907317 212.292683-212.292683 212.292683zM212.292683 24.97561C109.892683 24.97561 24.97561 109.892683 24.97561 212.292683v599.414634C24.97561 914.107317 109.892683 999.02439 212.292683 999.02439h599.414634c102.4 0 187.317073-84.917073 187.317073-187.317073v-599.414634C999.02439 109.892683 914.107317 24.97561 811.707317 24.97561h-599.414634z M84.917073 949.073171L239.765854 89.912195h162.341463L499.512195 676.839024l97.404878-586.926829h162.341464L914.107317 949.073171h-162.341463l-72.429269-674.341464-97.404878 674.341464h-167.336585l-94.907317-674.341464-74.926829 674.341464z",
      vdaf:
        "path://M811.707317 1024h-599.414634C94.907317 1024 0 929.092683 0 811.707317v-599.414634C0 94.907317 94.907317 0 212.292683 0h599.414634C929.092683 0 1024 94.907317 1024 212.292683v599.414634c0 117.385366-94.907317 212.292683-212.292683 212.292683zM212.292683 24.97561C109.892683 24.97561 24.97561 109.892683 24.97561 212.292683v599.414634C24.97561 914.107317 109.892683 999.02439 212.292683 999.02439h599.414634c102.4 0 187.317073-84.917073 187.317073-187.317073v-599.414634C999.02439 109.892683 914.107317 24.97561 811.707317 24.97561h-599.414634z M407.102439 949.073171L99.902439 89.912195h187.317073l217.287805 636.878049 209.795122-636.878049h212.292683l-42.458537 42.458537L591.921951 949.073171z",
      sad:
        "path://M811.707317 1024h-599.414634C94.907317 1024 0 929.092683 0 811.707317v-599.414634C0 94.907317 94.907317 0 212.292683 0h599.414634C929.092683 0 1024 94.907317 1024 212.292683v599.414634c0 117.385366-94.907317 212.292683-212.292683 212.292683zM212.292683 24.97561C109.892683 24.97561 24.97561 109.892683 24.97561 212.292683v599.414634C24.97561 914.107317 109.892683 999.02439 212.292683 999.02439h599.414634c102.4 0 187.317073-84.917073 187.317073-187.317073v-599.414634C999.02439 109.892683 914.107317 24.97561 811.707317 24.97561h-599.414634z M124.878049 664.35122h92.409756c27.473171-2.497561 64.936585-7.492683 117.385366-12.487805 9.990244 57.443902 29.970732 97.404878 62.439024 124.878048 29.970732 27.473171 72.429268 39.960976 124.878049 39.960976 54.946341 0 97.404878-12.487805 127.37561-34.965854s42.458537-52.44878 42.458536-82.419512c0-19.980488-4.995122-37.463415-17.482927-52.44878s-32.468293-27.473171-62.439024-37.463415c-19.980488-7.492683-67.434146-19.980488-139.863415-37.463415-92.409756-22.478049-157.346341-52.44878-194.809756-84.917073C224.780488 442.068293 199.804878 384.62439 199.804878 314.692683c0-44.956098 12.487805-84.917073 37.463415-122.380488s59.941463-67.434146 107.395122-87.414634S447.063415 74.926829 514.497561 74.926829c107.395122 0 189.814634 22.478049 242.263415 69.931708s82.419512 109.892683 84.917073 189.814634l-172.331708 7.492683c-7.492683-44.956098-22.478049-74.926829-47.453658-94.907317s-59.941463-29.970732-109.892683-29.970732c-49.95122 0-89.912195 9.990244-117.385366 29.970732-17.482927 12.487805-27.473171 29.970732-27.473171 52.44878 0 19.980488 7.492683 37.463415 24.97561 52.448781 22.478049 17.482927 74.926829 37.463415 159.843903 57.443902s147.356098 39.960976 187.317073 62.439024 72.429268 49.95122 94.907317 87.414635 34.965854 82.419512 34.965854 137.365853c0 49.95122-12.487805 94.907317-39.960976 137.365854-27.473171 42.458537-64.936585 74.926829-114.887805 94.907317s-112.390244 32.468293-187.317073 32.468293c-107.395122 0-192.312195-24.97561-249.756098-74.92683-44.956098-39.960976-77.42439-92.409756-92.409756-159.843902-9.990244-14.985366-57.443902-39.960976-59.941463-62.439024z",
      qarnet:
        "path://M811.707317 1024h-599.414634C94.907317 1024 0 929.092683 0 811.707317v-599.414634C0 94.907317 94.907317 0 212.292683 0h599.414634C929.092683 0 1024 94.907317 1024 212.292683v599.414634c0 117.385366-94.907317 212.292683-212.292683 212.292683zM212.292683 24.97561C109.892683 24.97561 24.97561 109.892683 24.97561 212.292683v599.414634C24.97561 914.107317 109.892683 999.02439 212.292683 999.02439h599.414634c102.4 0 187.317073-84.917073 187.317073-187.317073v-599.414634C999.02439 109.892683 914.107317 24.97561 811.707317 24.97561h-599.414634z M801.717073 814.204878c42.458537 29.970732 139.863415 84.917073 189.814634 102.4l-114.887805 92.409756c-24.97561-7.492683-52.44878-17.482927-77.42439-32.468293-4.995122-2.497561-44.956098-27.473171-114.887805-74.926829-57.443902 24.97561-117.385366 37.463415-187.317073 37.463415-132.370732 0-234.770732-39.960976-309.697561-117.385366S74.926829 634.380488 74.926829 494.517073s37.463415-249.756098 112.390244-327.180488S364.643902 49.95122 492.019512 49.95122s227.278049 39.960976 302.204878 117.385365 112.390244 187.317073 112.390244 327.180488c0 74.926829-9.990244 139.863415-29.970732 194.809756-14.985366 44.956098-39.960976 84.917073-74.926829 124.878049z m-137.365853-97.404878c22.478049-24.97561 39.960976-57.443902 49.951219-94.907317s17.482927-79.921951 17.482927-127.37561c0-99.902439-22.478049-172.331707-64.936586-222.282927S564.44878 199.804878 494.517073 199.804878s-127.37561 24.97561-172.331707 74.926829-64.936585 122.380488-64.936586 222.282927 22.478049 174.829268 64.936586 224.780488 99.902439 74.926829 167.336585 74.926829c24.97561 0 47.453659-4.995122 69.931708-12.487805-34.965854-22.478049-69.931707-39.960976-107.395122-54.946341l47.453658-99.902439c57.443902 19.980488 112.390244 49.95122 164.839025 87.414634z",
      softt:
        "path://M327.3 929.1c-1-1.4-91.5-138.4-31.6-332.7 12.3-40 25.1-75.3 37.5-109.5 29.2-80.8 55-151.8 55-233.7 0-62.6-15.1-131.6-55.2-215.9-13.9-29.1-48.6-41.5-77.7-27.6-29 13.9-41.3 48.6-27.5 77.7 71.6 150.5 44.7 224.8-4.1 359.8-12.2 33.8-26.1 72.2-39.3 114.8-76.3 247.2 41.4 424.9 46.4 432.5 18.1 26.7 54.3 33.6 81 15.6 26.6-18.1 33.6-54.4 15.5-81zM704.7 87.4c71.6 150.5 44.6 224.8-4.1 359.8-12.2 33.8-26.1 72.2-39.3 114.8-76.3 247.2 41.4 424.9 46.4 432.5 18.1 26.7 54.3 33.6 81 15.6 26.6-18.1 33.5-54.4 15.6-81-0.9-1.4-91.6-138.4-31.6-332.7 12.3-40 25.1-75.3 37.5-109.5 29.2-80.8 55-151.8 55-233.7 0-62.6-15.1-131.6-55.2-215.9-13.9-29.1-48.6-41.5-77.7-27.6-29.1 13.9-41.4 48.6-27.6 77.7z m-238.5 0c71.6 150.5 44.7 224.8-4.1 359.8-12.2 33.8-26.1 72.2-39.3 114.8-76.3 247.2 41.4 425 46.4 432.5 18.1 26.7 54.3 33.6 81 15.6 26.7-18.1 33.5-54.4 15.6-81-1-1.4-91.6-138.4-31.6-332.7 12.3-40 25.1-75.3 37.5-109.5 29.2-80.8 55-151.8 55-233.7 0-62.6-15.1-131.6-55.2-215.9-13.9-29.1-48.6-41.5-77.7-27.6-29.1 13.9-41.4 48.6-27.6 77.7z",
    };

    const labelSetting = {
      normal: {
        show: true,
        position: "right",
        offset: [10, 0],
        textStyle: {
          fontSize: 16,
        },
      },
    };

    let initOption = {
      title: {
        text: "混煤标准对比图",
      },

      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow",
        },
      },
      grid: {
        containLabel: true,
        left: 20,
      },
      yAxis: {
        data: [
          "Vdaf(min)",
          "Mar(max)",
          "Aar(max)",
          "Sad(max)",
          "Qarnet(min)",
          "soft T(min)",
        ],
        inverse: true,
        axisLine: { show: false },
        axisTick: { show: false },
        axisLabel: {
          margin: 30,
          fontSize: 16,
        },
        axisPointer: {
          label: {
            show: true,
            margin: 30,
          },
        },
      },
      xAxis: {
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false },
      },
      series: [
        {
          type: "pictorialBar",
          label: labelSetting,
          symbolRepeat: true,
          symbolSize: ["80%", "60%"],
          barCategoryGap: "40%",
        },
        {
          name: "标准",
          type: "pictorialBar",
          barGap: "10%",
          label: labelSetting,
          symbolRepeat: true,
          symbolSize: ["80%", "60%"],
          data: [
            {
              value: this.sVdafValue,
              symbol: this.pathSymbols.vdaf,
            },
            {
              value: this.sMarValue,
              symbol: this.pathSymbols.mar,
            },
            {
              value: this.sAarValue,
              symbol: this.pathSymbols.aar,
            },
            {
              value: this.sSadValue,
              symbol: this.pathSymbols.sad,
            },
            {
              value: this.sQarValue,
              symbol: this.pathSymbols.qarnet,
            },
            {
              value: this.sSoftTValue,
              symbol: this.pathSymbols.softt,
            },
          ],
        },
      ],
    };
    this.compareChart.setOption(initOption);

    this.dataOption = {
      legend: {
        left: "50%",
        top: "0%",
        data: ["混煤", "标准"],
      },
      series: [
        {
          name: "混煤",
          data: [],
        },
      ],
    };

    this.getMixedData(this.dataOption, this.pathSymbols);
  },
  methods: {
    //动态绑定6个条件：vdaf, mar, aar, sar, qarnet, softT输入值
    //同一更新所有标准
    updateStandard() {
      const pathSymbols = this.pathSymbols;
      let updateOption = {
        series: [
          {},
          {
            data: [
              {
                value: this.sVdafValue,
                symbol: pathSymbols.vdaf,
              },
              {
                value: this.sMarValue,
                symbol: pathSymbols.mar,
              },
              {
                value: this.sAarValue,
                symbol: pathSymbols.aar,
              },
              {
                value: this.sSadValue,
                symbol: pathSymbols.sad,
              },
              {
                value: this.sQarValue,
                symbol: pathSymbols.qarnet,
              },
              {
                value: this.sSoftTValue,
                symbol: pathSymbols.softt,
              },
            ],
          },
        ],
      };
      this.compareChart.setOption(updateOption);
    },
    sVdafChange(inputValue) {
      if (parseFloat(inputValue) <= parseFloat(this.result.mixedVDaf)) {
        this.sVdafValue = inputValue;
        this.updateStandard();
        return 1;
      } else {
        this.$message({
          type: "error",
          message: "超出最小Vdaf，请重新设置标准或重选混煤方案",
        });
        return 0;
      }
    },
    sMarChange(inputValue) {
      if (parseFloat(this.result.mixedMAr) <= parseFloat(inputValue)) {
        this.sMarValue = inputValue;
        this.updateStandard();
        return 1;
      } else {
        this.$message({
          type: "error",
          message: "超出最大Mar，请重新设置标准或重选混煤方案",
        });
        return 0;
      }
    },
    sAarChange(inputValue) {
      if (parseFloat(this.result.mixedAAr) <= parseFloat(inputValue)) {
        this.sAarValue = inputValue;
        this.updateStandard();
        return 1;
      } else {
        this.$message({
          type: "error",
          message: "超出最大Aar，请重新设置标准或重选混煤方案",
        });
        return 0;
      }
    },
    sSadChange(inputValue) {
      if (parseFloat(this.result.mixedSAd) <= parseFloat(inputValue)) {
        this.sSadValue = inputValue;
        this.updateStandard();
        return 1;
      } else {
        this.$message({
          type: "error",
          message: "超出最大Sad，请重新设置标准或重选混煤方案",
        });
        return 0;
      }
    },
    qarnetChange(inputValue) {
      if (parseFloat(inputValue) <= parseFloat(this.result.mixedQnetAr)) {
        this.sQarValue = inputValue;
        this.updateStandard();
        return 1;
      } else {
        this.$message({
          type: "error",
          message: "超出最小Qar，请重新设置标准或重选混煤方案",
        });
        return 0;
      }
    },
    softTChange(inputValue) {
      if (parseFloat(inputValue) <= parseFloat(this.result.softT)) {
        this.sSoftTValue = inputValue;
        this.updateStandard();
        return 1;
      } else {
        this.$message({
          type: "error",
          message: "超出最大soft T，请重新设置标准或重选混煤方案",
        });
        return 0;
      }
    },
    async getMixedData(dataOption, pathSymbols) {
      if (window.sessionStorage.getItem("mixedName") != null) {
        this.result = await this.$axios.get("/mixed-analysis/get", {
          params: { mixedName: window.sessionStorage.getItem("mixedName") },
        });
        const result = this.result;
        dataOption.legend.data = [result.mixedName, "标准"];
        dataOption.series
          .filter(function (seriesItem, sId) {
            return sId == 0;
          })
          .map(function (seriesItem) {
            seriesItem.name = result.mixedName;
            seriesItem.data.push({
              value: result.mixedVDaf,
              symbol: pathSymbols.vdaf,
            });
            seriesItem.data.push({
              value: result.mixedMAr,
              symbol: pathSymbols.mar,
            });
            seriesItem.data.push({
              value: result.mixedAAr,
              symbol: pathSymbols.aar,
            });
            seriesItem.data.push({
              value: result.mixedSAd,
              symbol: pathSymbols.sad,
            });
            seriesItem.data.push({
              value: result.mixedQnetAr,
              symbol: pathSymbols.qarnet,
            });
            seriesItem.data.push({
              value: result.softT,
              symbol: pathSymbols.softt,
            });
          });
        this.compareChart.setOption(dataOption);
      } else {
        this.$message({
          type: "info",
          message: "未保存任何混煤数据",
        });
      }
    },
    checkStandard() {
      //判断6个标准是否全满足
      if (
        this.sVdafChange(this.sVdafValue) &&
        this.sMarChange(this.sMarValue) &&
        this.sAarChange(this.sAarValue) &&
        this.sSadChange(this.sSadValue) &&
        this.qarnetChange(this.sQarValue) &&
        this.softTChange(this.sSoftTValue)
      ) {
        const scheme = {};
        scheme.maxAAr = this.sAarValue;
        scheme.maxMAr = this.sMarValue;
        scheme.maxSAd = this.sSadValue;
        scheme.minQnetAr = this.sQarValue;
        scheme.minVDaf = this.sVdafValue;
        scheme.mixedId = this.result.mixedId;
        //符合标准，存储标准数据到数据库
        this.postRequest("/mixed-analysis/add-scheme", scheme).then(
          (response) => {
            if (response) {
              window.sessionStorage.setItem("schemeId", parseInt(response));
            }
          }
        );
        this.$message({
          type: "success",
          message: "混煤符合煤质标准, 可进行成本分析",
        });
      }
    },
  },
};
</script>

<style lang="less" scoped>
.dashboard-box {
  float: left;
  width: 150px;
  height: 230px;
  margin: 0 10px;
}
.input-box {
  height: 115px;
}
.el-tag {
  display: block;
  margin: 5px 20px;
  text-align: center;
}
.input-tag {
  margin-bottom: 19px;
}
div.el-progress {
  margin-left: 10px;
}
.el-input-number {
  width: 150px;
}

//必须设置chart大小，否则默认大小为为0*0
.compare-chart {
  float: left;
  width: 650px;
  height: 600px;
}

.checks-btn {
  float: left;
}
</style>
