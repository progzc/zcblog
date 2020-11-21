<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('NewVisitors')">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon name="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">New Visitors</div>
          <count-to :start-val="0" :end-val="visitorNum" :duration="2700" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('Comments')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon name="message" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">Comments</div>
          <count-to :start-val="0" :end-val="commentNum" :duration="3000" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('Likes')">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon name="like" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">Likes</div>
          <count-to :start-val="0" :end-val="likeNum" :duration="3300" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('Articles')">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg-icon name="articles" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">Articles</div>
          <count-to :start-val="0" :end-val="articleNum" :duration="3600" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script type="text/ecmascript-6">
import CountTo from 'vue-count-to'
export default {
  name: 'PanelGroup',
  components: {
    'count-to': CountTo
  },
  computed: {
    visitorNum: {
      get () { return this.$store.state.user.visitorNum }
    },
    commentNum: {
      get () { return this.$store.state.user.commentNum }
    },
    likeNum: {
      get () { return this.$store.state.user.likeNum }
    },
    articleNum: {
      get () { return this.$store.state.user.articleNum }
    }
  },
  methods: {
    handleSetLineChartData (type) {
      this.$emit('handleSetLineChartData', type) // 向父组件发射事件
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>
  .panel-group {
    margin-top: 18px;
    .card-panel-col{
      margin-bottom: 32px;
    }
    .card-panel {
      height: 108px;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .1);
      border-color: rgba(0, 0, 0, .1);
      &:hover {
        .card-panel-icon-wrapper {
          color: #fff;
        }
        .icon-people {
          background: #40c9c6;
        }
        .icon-message {
          background: #36a3f7;
        }
        .icon-money {
          background: #f4516c;
        }
        .icon-shopping {
          background: #34bfa3
        }
      }
      .icon-people {
        color: #40c9c6;
      }
      .icon-message {
        color: #36a3f7;
      }
      .icon-money {
        color: #f4516c;
      }
      .icon-shopping {
        color: #34bfa3
      }
      .card-panel-icon-wrapper {
        float: left;
        margin: 14px 0 0 14px;
        padding: 16px;
        transition: all 0.38s ease-out;
        border-radius: 6px;
      }
      .card-panel-icon {
        float: left;
        font-size: 48px;
      }
      .card-panel-description {
        float: right;
        font-weight: bold;
        margin: 26px 26px 26px 0;
        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }
        .card-panel-num {
          font-size: 20px;
        }
      }
    }
  }
</style>
