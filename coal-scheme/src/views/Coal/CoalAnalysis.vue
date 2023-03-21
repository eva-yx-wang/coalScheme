<template>
  <div>
    <el-card>
      <div class="block">
        <span class="demonstration slider-info"
          >当前混煤比例：{{ ratio }}%</span
        >
        <!-- 属性说明
          v-model="ratio"动态绑定slider的值到this.ratio
          @change="updateMixed" 监听鼠标拖动事件 -->
        <el-slider
          v-model="ratio"
          :step="10"
          show-stops
          ref="slider_ref"
          @change="updateMixed"
        ></el-slider>
        <el-button type="success" @click="saveMixedData"
          >保存混煤数据</el-button
        >
      </div>
    </el-card>
    <el-card>
      <!-- 为echarts准备一个DOM -->
      <div class="main" ref="funnels"></div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "CoalAnalysis",
  data() {
    return {
      funnel: {
        common: {
          type: "funnel",
          left: "5%",
          top: "5%",
          width: "45%",
          height: "30%",
          data_min: 0,
          data_max: 100,
          row_gap: 2,
          label: {
            show: true,
            position: "inside",
          },
          labelLine: {
            length: 1,
            lineStyle: {
              width: 1,
              type: "solid",
            },
          },
          itemStyle: {
            borderColor: "#fff",
            borderWidth: 1,
          },
          //悬浮时强调字体
          emphasis: {
            label: {
              fontSize: 20,
            },
          },
          //json key字段
          colunmName: [
            "收到基C",
            "收到基H",
            "收到基O",
            "收到基S",
            "收到基A",
            "收到基M",
          ],
        },
        //动态绑定后端返回的数据
        //自动根据数据大小排序
      },
      //页面刚加载时默认计算混煤数据的比例
      ratio: 30,
    };
  },
  //初始化和更新DOM元素
  mounted() {
    //1 绑定DOM元素
    if (window.sessionStorage.getItem("coalIds") == null) {
      this.$message({
        type: "warning",
        message: "请先选择2种煤",
      });
      this.$router.push("/coal/list");
    }
    this.initFunnelChart();
    //2 设置位置样式参数: 图标位置、图例大小等不请求server的数据
    const initOption = {
      title: {
        left: "50%",
        top: "0%",
        text: "煤质分析",
        subtext: "",
      },
      //缓存按钮
      tooltip: {
        trigger: "item",
        formatter: "{a} <br/>{b} : {c}%",
      },
      toolbox: {
        feature: {
          dataView: { readOnly: false },
          restore: {},
          saveAsImage: {},
        },
      },
      //图标图例
      legend: {
        orient: "vertical",
        left: "left",
      },
      series: [
        {
          type: this.funnel.common.type,
          left: this.funnel.common.left,
          top: this.funnel.common.top,
          width: this.funnel.common.width,
          height: this.funnel.common.height,
          funnelAlign: "right",
          // height: {totalHeight} - y - y2,
          min: this.funnel.data_min,
          max: this.funnel.data_max,
          minSize: "0%",
          maxSize: "100%",
          sort: "ascending",
          gap: this.funnel.common.row_gap,
          label: this.funnel.common.label,
          labelLine: this.funnel.common.labelLine,
          itemStyle: this.funnel.common.itemStyle,
          emphasis: this.funnel.common.emphasis,
        },
        //煤种2
        {
          type: this.funnel.common.type,
          left: this.funnel.common.left,
          top: "37%",
          width: this.funnel.common.width,
          height: this.funnel.common.height,
          funnelAlign: "right",
          // height: {totalHeight} - y - y2,
          min: this.funnel.data_min,
          max: this.funnel.data_max,
          minSize: "0%",
          maxSize: "100%",
          sort: "descending",
          gap: this.funnel.common.row_gap,
          label: this.funnel.common.label,
          labelLine: this.funnel.common.labelLine,
          itemStyle: this.funnel.common.itemStyle,
          emphasis: this.funnel.common.emphasis,
          data: [],
        },
        //混煤
        {
          type: this.funnel.common.type,
          left: "55%",
          top: "22%",
          width: "40%",
          height: "45%",
          // height: {totalHeight} - y - y2,
          min: this.funnel.data_min,
          max: this.funnel.data_max,
          minSize: "0%",
          maxSize: "100%",
          sort: "descending",
          gap: this.funnel.common.row_gap,
          label: this.funnel.common.label,
          labelLine: this.funnel.common.labelLine,
          itemStyle: this.funnel.common.itemStyle,
          emphasis: this.funnel.common.emphasis,
        },
      ],
    };
    //2.1 初始化funnel
    this.funnelChart.setOption(initOption);

    //3 设置数据参数配置
    //绑定到Vue对象属性中
    this.dataOption = {
      legend: {
        data: this.funnel.common.colunmName,
      },
      series: [
        //煤种1
        {
          name: "1",
          data: [],
        },
        //煤种2
        {
          name: "2",
          data: [],
        },
        //混煤
        {
          name: "混合",
          data: [],
        },
      ],
    };
    //设置初始比例
    //绑定slider的value到this.ratio
    this.ratio = this.$refs.slider_ref.$options.propsData.value;
    //监听slider数值this.$refs.slider_ref.$options.propsData.value

    this.initAnalysis(this.dataOption, this.ratio);
    /**尽管此处是在initAnalysis后执行的funnelChart.setOption, 但因为是跨域请求,
    在initAnalysis中回调response拿到返回的数据赋值给option.series时,
    主线程中的"funnelChart.setOption(option);"就已经执行完了**/
    //(不能在此时初始化funnelChart)funnelChart.setOption(option);
    //window.addEventListener("ratioChange", this.getMixedData());
  },
  methods: {
    //把DOM元素绑定到Vue对象的funnelChart对象上
    initFunnelChart() {
      this.funnelChart = this.$echarts.init(this.$refs.funnels);
    },

    //异步获取server数据, 保证用户体验
    async initAnalysis(dataOption, ratio) {
      //1 跨域获取coalAnalysis
      //在函数内创建的变量如果没有绑定到函数外部对象上，则随函数执行完被销毁
      /**1.1 设置跨域url
      在vue.config.js中proxy.target中设置跨域url**/
      /**1.2 发起跨域请求获取后台数据
      传入参数: 储存在sessionStorage的参数coalIds(由后台接口决定传参数格式和内容),
      传入格式详见CoalInplo.vue中定义的toAnalyse()
      **/
      const result = await this.getRequest(
        "/coal/analysis/getTwo/" + window.sessionStorage.getItem("coalIds")
      );
      //2 设置煤种参数
      //2.1 单煤参数
      result.forEach((resultItem, rId) => {
        dataOption.series
          .filter(function (seriesItem, sId) {
            return rId == sId;
          })
          .map(function (seriesItem) {
            seriesItem.name = window.sessionStorage.getItem(resultItem.coalId);
            seriesItem.data.push({
              value: resultItem.coalCAr,
              name: "收到基C",
            });
            seriesItem.data.push({
              value: resultItem.coalHAr,
              name: "收到基H",
            });
            seriesItem.data.push({
              value: resultItem.coalOAr,
              name: "收到基O",
            });
            seriesItem.data.push({
              value: resultItem.coalSAr,
              name: "收到基S",
            });
            seriesItem.data.push({
              value: resultItem.coalAAr,
              name: "收到基A",
            });
            seriesItem.data.push({
              value: resultItem.coalMAr,
              name: "收到基M",
            });
          });
      });
      //保存请求数据备用
      this.responseData = result;

      //2.2 默认比例计算混煤参数并设置
      this.getMixedData(dataOption, ratio);
      //注意必须在异步函数之内，先用请求到的数据默认初始化funnelChart
      //否则在mounted内的语句作为主线程会先于异步请求执行，其初始化funnelChart先于异步函数
      //this.funnelChart.setOption(dataOption);
    },

    //计算混煤参数并设置
    getMixedData(dataOption, ratio) {
      //不能直接用this.ratio计算，否则会undefined, 为什么我也不知道
      //不知道为什么用dataOption.series.filter拿到的data是undefined, 如果不这样会undefined
      let coal3Data = [];
      let count = 0;
      //计算混煤数据
      dataOption.series.reduce(function (lastCoal, currentCoal) {
        //技巧糖：设置锚点
        count++;
        if (count == 2) {
          //2.2.1 设置混合煤名称
          dataOption.series.find((item, id) => {
            if (id == 2) {
              //默认20%煤种1
              item.name =
                lastCoal.name +
                ratio +
                "% + " +
                currentCoal.name +
                (100 - ratio) +
                "%";
            }
          });
          //2.2.2 设置混合煤煤质数据
          lastCoal.data.forEach((coal1_data) => {
            //遍历找到单煤煤质对应的值
            currentCoal.data
              .filter(function (coal2_data) {
                return coal1_data.name == coal2_data.name;
              })
              //计算混煤质
              .map(function (coal2_data) {
                //注意必须转小数否则计算结果为NAN
                let coal1_value = parseFloat(coal1_data.value);
                let coal2_value = parseFloat(coal2_data.value);
                let mixed_value =
                  (coal1_value * ratio + (100 - ratio) * coal2_value) / 100;
                mixed_value = Math.round(mixed_value * 100) / 100;
                coal3Data.push({
                  value: mixed_value,
                  name: coal1_data.name,
                });
              });
          });
        }
        //一定记得return, 否则lastCoal会是undefined
        return currentCoal;
      }, null);
      //为混煤data绑定计算的数据
      dataOption.series.find((item, id) => {
        if (id == 2) {
          item.data = coal3Data;
        }
      });
      //每次算完混煤数据后都要刷新funnelChart设置
      this.funnelChart.setOption(dataOption);
    },

    //监听slider拖动鼠标，鼠标松开后触发updateMixed
    updateMixed() {
      //用已经获取完后台数据的this.dataOption再初始化Vue对象，计算MixedData
      this.getMixedData(this.dataOption, this.ratio);
    },

    elementMixed(e1, e2, ratio) {
      return Math.round(((ratio * e1 + (100 - ratio) * e2) / 100) * 100) / 100;
    },
    ashCompoundMixed(ashC1, ashC2, aar1, aar2, ratio) {
      return (
        Math.round(
          ((ratio * aar1 * ashC1 + (100 - ratio) * aar2 * ashC2) /
            (ratio * aar1 + (100 - ratio) * aar2)) *
            100
        ) / 100
      );
    },
    saveMixedData() {
      //1 获取自变量：煤质数据和比率
      //配煤比率
      const ratio = this.ratio;
      //两个煤种的详细数据
      const coalsData = this.responseData;
      //当前Vue对象
      const vueObj = this;

      this.$confirm("确认保存此比例的混煤数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //此处的this不是当前Vue对象，vueObj才是Vue对象
          let count = 0;
          coalsData.reduce(function (lastCoal, currentCoal) {
            count++;
            if (count == 2) {
              let mixedCoal = {};
              //混煤编号
              mixedCoal.mixedId = 0;
              //混煤名称
              vueObj.dataOption.series.find((item, id) => {
                if (id == 2) {
                  mixedCoal.mixedName = item.name;
                }
              });
              window.sessionStorage.setItem("mixedName", mixedCoal.mixedName);
              //2个煤种编号
              mixedCoal.coal1Id = lastCoal.coalId;
              mixedCoal.coal2Id = currentCoal.coalId;
              mixedCoal.coal1Ratio = ratio;
              mixedCoal.mixedCAr = vueObj.elementMixed(
                lastCoal.coalCAr,
                currentCoal.coalCAr,
                ratio
              );
              mixedCoal.mixedHAr = vueObj.elementMixed(
                lastCoal.coalHAr,
                currentCoal.coalHAr,
                ratio
              );
              mixedCoal.mixedOAr = vueObj.elementMixed(
                lastCoal.coalOAr,
                currentCoal.coalOAr,
                ratio
              );
              mixedCoal.mixedNAr = vueObj.elementMixed(
                lastCoal.coalNAr,
                currentCoal.coalNAr,
                ratio
              );
              mixedCoal.mixedSAr = vueObj.elementMixed(
                lastCoal.coalSAr,
                currentCoal.coalSAr,
                ratio
              );
              mixedCoal.mixedAAr = vueObj.elementMixed(
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.mixedMAr = vueObj.elementMixed(
                lastCoal.coalMAr,
                currentCoal.coalMAr,
                ratio
              );
              //煤1收到基外部水
              lastCoal.coalMf =
                (100 * (lastCoal.coalMAr - lastCoal.coalMAd)) /
                (100 - lastCoal.coalMAd);
              //煤2收到基外部水
              currentCoal.coalMf =
                (100 * (currentCoal.coalMAr - currentCoal.coalMAd)) /
                (100 - currentCoal.coalMAd);
              //混煤空干基水分
              mixedCoal.mixedMAd =
                Math.round(
                  ((ratio * lastCoal.coalMAd * (100 - lastCoal.coalMf) +
                    (100 - ratio) *
                      currentCoal.coalMAd *
                      (100 - currentCoal.coalMf)) /
                    (ratio * (100 - lastCoal.coalMf) +
                      (100 - ratio) * (100 - currentCoal.coalMf))) *
                    100
                ) / 100;
              //混煤空干基硫分
              mixedCoal.mixedSAd =
                Math.round(
                  (((100 - mixedCoal.mixedMAd) * mixedCoal.mixedSAr) /
                    (100 - mixedCoal.mixedMAr)) *
                    100
                ) / 100;
              mixedCoal.mixedVDaf = vueObj.elementMixed(
                lastCoal.coalVDaf,
                currentCoal.coalVDaf,
                ratio
              );
              mixedCoal.mixedQnetAr = vueObj.elementMixed(
                lastCoal.coalQnet,
                currentCoal.coalQnet,
                ratio
              );
              //以下数据用于后台计算混煤软化温度ST
              mixedCoal.ashSio2Ar = vueObj.ashCompoundMixed(
                lastCoal.ashSio2Ar,
                currentCoal.ashSio2Ar,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.ashAl2o3Ar = vueObj.ashCompoundMixed(
                lastCoal.ashAl2o3Ar,
                currentCoal.ashAl2o3Ar,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.ashFe2o3Ar = vueObj.ashCompoundMixed(
                lastCoal.ashFe2o3Ar,
                currentCoal.ashFe2o3Ar,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.ashTio2 = vueObj.ashCompoundMixed(
                lastCoal.ashTio2,
                currentCoal.ashTio2,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.ashCao = vueObj.ashCompoundMixed(
                lastCoal.ashCao,
                currentCoal.ashCao,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.ashMgo = vueObj.ashCompoundMixed(
                lastCoal.ashMgo,
                currentCoal.ashMgo,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.ashSo3 = vueObj.ashCompoundMixed(
                lastCoal.ashSo3,
                currentCoal.ashSo3,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.ashK2o = vueObj.ashCompoundMixed(
                lastCoal.ashK2o,
                currentCoal.ashK2o,
                lastCoal.coalAAr,
                currentCoal.coalAAr,
                ratio
              );
              mixedCoal.softT = 0;
              vueObj.postRequest("/mixed-analysis/add", mixedCoal);
            }
            return currentCoal;
          }, 0);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消保存",
          });
        });
    },
  },
  computed: {},
};
</script>

<style lang="less" scoped>
.main {
  width: 800px;
  height: 700px;
}

//滑动条
.el-slider {
  float: left;
  width: 50%;
  margin-left: 20px;
}
//滑动条前的提示信息
.slider-info {
  float: left;
}

//保存混煤按钮
.el-button {
  margin-left: 30px;
}
</style>
