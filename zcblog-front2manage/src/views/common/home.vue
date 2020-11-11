<template>
  <div class="mod-home">
    <h3>博客后台管理系统</h3>
    <panel-group></panel-group>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData"/>
    </el-row>
  </div>
</template>

<script type="text/ecmascript-6">
import PanelGroup from 'components/content/PanelGroup'
import LineChart from 'components/content/LineChart'
export default {
  name: 'home',
  components: {
    'panel-group': PanelGroup,
    'line-chart': LineChart
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
