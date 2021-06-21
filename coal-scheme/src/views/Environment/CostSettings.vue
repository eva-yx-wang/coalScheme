<template>
  <div>
    <el-card>
      <div class="input-price">
        <el-tag type="danger">混煤单价(元/t)</el-tag>
        <el-input-number
          :precision="1"
          :step="0.1"
          :min="0"
          v-model="mixedPrice"
          disabled
        >
        </el-input-number>
      </div>
      <div class="input-price">
        <el-tag type="warning">石灰石单价(元/t)</el-tag>
        <el-input-number
          :precision="1"
          :step="0.1"
          :min="0"
          v-model="limePrice"
        >
        </el-input-number>
      </div>
      <div class="input-price">
        <el-tag>液氨单价(元/t)</el-tag>
        <el-input-number
          :precision="2"
          :step="0.01"
          :min="0"
          v-model="nh3Price"
        >
        </el-input-number>
      </div>
      <div class="input-price">
        <el-tag type="success">上网电价(元/kwh)</el-tag>
        <el-input-number
          :precision="4"
          :step="0.001"
          :min="0"
          v-model="powerPrice"
        >
        </el-input-number>
      </div>
      <el-button type="success" @click="setPrice" class="analysis-btn"
        >分析成本</el-button
      >
    </el-card>
    <el-card>
      <div class="pie-chart" ref="pieChart"></div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      mixedPrice: 337.4,
      limePrice: 53.8,
      nh3Price: 2457,
      powerPrice: 0.339,
    };
  },
  mounted() {
    //绑定echarts要挂载的DOM对象
    this.pieChart = this.$echarts.init(this.$refs.pieChart);
    const pieChart = this.pieChart;
    setTimeout(function () {
      let option = {
        legend: {},
        tooltip: {
          trigger: "axis",
          showContent: false,
        },
        dataZoom: [
          {
            id: "dataZoomX",
            type: "slider",
            xAxisIndex: [0],
            filterMode: "filter",
          },
          {
            id: "dataZoomY",
            type: "slider",
            yAxisIndex: [0],
            filterMode: "empty",
          },
        ],
        dataset: {
          source: [
            //测试数据
            //设置第2维度(类似表格第2~n行)
            //"脱硝成本"为第2行名称, 56.5等为各列值
            ["煤种1占比", "3", "4", "5", "6", "7", "8"],
            //设置第2维度(类似表格第2~n行)
            //"脱硝成本"为第2行名称, 56.5等为各列值
            ["脱硝成本", 3046.7],
            //3046.7, 3022.1, 3022.1, 3022.1, 3022.1, 3022.1
            ["厂用电成本", 94133.4],
            // 94133.4, 94133.5, 94133.5, 94133.5,94133.5, 94133.5,
            ["脱硫成本", 1511.8],
            //1511.8, 1452.6, 1420.3, 1388, 1328.9, 1302
            ["燃料成本", 647882.2],
            //648150.0,648514.0,648855.6, 649182.8,649509.3,
          ],
        },
        //对应到 dataset 第一列
        xAxis: {
          type: "category",
          name: "煤种1占比: 1/10",
          nameLocation: "center",
        },
        yAxis: {
          gridIndex: 0,
          name: "日供电成本：元/天",
        },
        grid: { top: "55%" },
        series: [
          //每个对象自动对应到 dataset 的第2列开始的每一列
          {
            type: "line",
            smooth: true,
            seriesLayoutBy: "row",
            emphasis: { focus: "series" },
          },
          {
            type: "line",
            smooth: true,
            seriesLayoutBy: "row",
            emphasis: { focus: "series" },
          },
          {
            type: "line",
            smooth: true,
            seriesLayoutBy: "row",
            emphasis: { focus: "series" },
          },
          {
            type: "line",
            smooth: true,
            seriesLayoutBy: "row",
            emphasis: { focus: "series" },
          },
          //默认静态展示2021.05
          {
            type: "pie",
            id: "pie",
            radius: "30%",
            center: ["50%", "25%"],
            emphasis: { focus: "data" },
            label: {
              formatter: "{b}: {@2} ({d}%)",
            },
            encode: {
              itemName: "product",
              value: "2",
              tooltip: "2",
            },
          },
        ],
      };

      pieChart.on("updateAxisPointer", function (event) {
        var xAxisInfo = event.axesInfo[0];
        if (xAxisInfo) {
          var dimension = xAxisInfo.value + 1;
          pieChart.setOption({
            series: {
              id: "pie",
              label: {
                formatter: "{b}: {@[" + dimension + "]} ({d}%)",
              },
              encode: {
                value: dimension,
                tooltip: dimension,
              },
            },
          });
        }
      });

      pieChart.setOption(option);
    });
  },
  methods: {
    async setPrice() {
      const price = {};
      price.schemeId = window.sessionStorage.getItem("schemeId");
      price.limePrice = this.limePrice;
      price.nh3Price = this.nh3Price;
      price.powerPrice = this.powerPrice;
      this.postRequest("/coal-scheme/get-data", price).then((response) => {
        if (response) {
          let mixedCost = (
            response.mixedConsumpKwh *
            response.powerSupply *
            0.01 *
            response.mixedPrice
          ).toFixed(1);
          let ownCost = (
            response.powerCostDay -
            response.limeCost -
            response.nh3Cost -
            mixedCost
          ).toFixed(1);
          let option = {
            dataset: {
              source: [
                //设置第1维度(类似表格第1行)："product"为首行名称, "2021.05"等为各列名
                ["煤种1占比", "3", "4", "5", "6", "7", "8"],
                //设置第2维度(类似表格第2~n行)
                //"脱硝成本"为第2行名称, 56.5等为各列值
                [
                  "脱硝成本",
                  response.nh3Cost,
                  3022.1,
                  3022.1,
                  3022.1,
                  3022.1,
                  3022.1,
                ],
                [
                  "厂用电成本",
                  ownCost,
                  94133.5,
                  94133.5,
                  94133.5,
                  94133.5,
                  94133.5,
                ],
                [
                  "脱硫成本",
                  response.limeCost,
                  1452.6,
                  1420.3,
                  1388,
                  1328.9,
                  1302,
                ],
                [
                  "燃料成本",
                  mixedCost,
                  648150.0,
                  648514.0,
                  648855.6,
                  649182.8,
                  649509.3,
                ],
              ],
            },

            series: [
              //每个对象自动对应到 dataset 的第2列开始的每一列
              {},
              {},
              {},
              {},
              //默认静态展示2021.05
              {
                label: {
                  formatter: "{b}: {@3} ({d}%)",
                },
                encode: {
                  itemName: "product",
                  value: "3",
                  tooltip: "3",
                },
              },
            ],
          };

          this.pieChart.on("updateAxisPointer", function (event) {
            var xAxisInfo = event.axesInfo[0];
            if (xAxisInfo) {
              let dimension = xAxisInfo.value + 1;
              this.pieChart.setOption({
                series: {
                  id: "pie",
                  label: {
                    formatter: "{b}: {@[" + dimension + "]} ({d}%)",
                  },
                  encode: {
                    value: dimension,
                    tooltip: dimension,
                  },
                },
              });
            }
          });

          this.pieChart.setOption(option);
        }
      });
    },
    drawPriceCurve() {},
  },
};
</script>

<style lang="less" scoped>
.pie-chart {
  width: 800px;
  height: 600px;
}
.input-price {
  float: left;
  width: 150px;
  height: 150px;
  margin-top: 10px;
  margin-right: 40px;
}

.input-price .el-tag {
  margin-top: 10px;
  margin-bottom: 30px;
  margin-left: 40px;
}
.analysis-btn {
  margin-top: 20px;
  margin-left: 20px;
}
</style>
