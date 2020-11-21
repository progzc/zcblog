<template>
  <div class="mod-home">
    <h3>博客管理系统</h3>
    <panel-group @handleSetLineChartData="handleSetLineChartData"></panel-group>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="chartData"/>
    </el-row>
  </div>
</template>

<script type="text/ecmascript-6">
import PanelGroup from 'components/content/PanelGroup'
import LineChart from 'components/content/LineChart'

const chartDatas = {
  newVisitors: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  comments: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  likes: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  articles: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}
export default {
  name: 'home',
  components: {
    'panel-group': PanelGroup,
    'line-chart': LineChart
  },
  data () {
    return {
      chartData: chartDatas.newVisitors
    }
  },
  computed: {
    sidebarFold: {
      get () { return this.$store.state.common.sidebarFold }
    }
  },
  methods: {
    handleSetLineChartData (type) {
      type = type.replace(type[0], type[0].toLowerCase())
      this.chartData = chartDatas[type]
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>
  .mod-home {
    line-height: 1.5;
  }
</style>
