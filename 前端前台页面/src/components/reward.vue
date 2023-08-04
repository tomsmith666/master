<!-- 记录模块 -->
<template>
  <div class="tRewardBox tcommonBox">
    <header>
      <h1>
        <a href="#/DetailShare" target="_blank"> 学习记录 </a>
      </h1>
    </header>
    <section>
      <div>
        <!-- <img
          src="static/img/heart.png"
          alt=""
          style="max-width: 20%"
          class="coffee"
        /> -->
        <h2 class="studytext">
            黑马程序员pink的html，css，js<br>
            尚硅谷张天禹Vue教程<br>
            前端项目 <br>
            前台商城购买系统尚品汇(前端)<br>
            后台酒店管理系统(前端)<br>

            动力节点尚硅谷java基础<br>
            动力节点mysql数据库<br>
            尚硅谷的ssm课程<br>
            尚硅谷雷神的部分SpringBoot3教程<br>
            刘老师SpringBoot+Vue开发<br>
            sangeng的部分SpringSecurity<br>
            后端项目 <br>
            sangeng的前后端分离项目(后端)
        </h2>
      </div>
  

      <hr />

      <el-row :gutter="30">
        <el-col :span="12" class="donate-item">
          <div class="donate-tip">
            <img
              :src="
                rewardData.wechat_image
                  ? rewardData.wechat_image
                  : 'static/img/robotcat.jpg'
              "
              :onerror="$store.state.errorImg"
            />
            <!-- 这里是绿色字体 -->
            <span> </span>
          </div>
    
        </el-col>
        <el-col :span="12" class="donate-item">
          <div class="donate-tip">
            <img
              :src="
                rewardData.alipay_image
                  ? rewardData.alipay_image
                  : 'static/img/robotcat.jpg'
              "
              :onerror="$store.state.errorImg"
            />
            <!-- 蓝色字体 -->
            <span style="padding-top: 14px"></span>
          </div>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script>
export default {
  data() {
    //选项 / 数据
    return {
      rewardData: "", //赞赏二维码
      tableData: [
        {
          //赞赏数据
          "payTime":"2021-10-23",
          "name":"智障少女欢乐多",
          "money":"188"
        },
        {
          "payTime":"2021-10-28",
          "name":"刺猬",
          "money":"50"
        },
        {
          "payTime":"2021-11-03",
          "name":"迅捷小鹿",
          "money":"30"
        },
      ],
    };
  },
  methods: {
    //事件处理器
    showInitDate: function (date, full) {
      //时间处理
      return initDate(date, full);
    },
  },
  components: {
    //定义组件
  },
  created() {
    //生命周期函数
    var that = this;
  },
};
</script>

<style>
.studytext{
  margin-left: 300px;
  font-family:  sans-serif;
  font-size: 18px;
  font-weight: bold;

}
.tRewardBox section {
  padding-bottom: 20px;
}
.tRewardBox section h1 {
  margin: 10px 0;
  font-size: 25px;
  font-weight: 700;
  /*text-align: center;*/
  line-height: 30px;
}
.tRewardBox section p {
  margin: 10px 0;
  line-height: 24px;
}
.tRewardBox section hr {
  background: #ccc;
  margin-bottom: 30px;
}

.tRewardBox .donate-item {
  text-align: right;
  color: #44b549;
}
.tRewardBox .donate-item:last-child {
  text-align: left;
  color: #00a0e9;
}
.tRewardBox .donate-item img {
  width: 100%;
  display: block;
  height: auto;
}
.tRewardBox .donate-item div {
  display: inline-block;
  width: 150px;
  padding: 0 6px;
  text-align: center;
  box-sizing: border-box;
}
.tRewardBox .donate-item div span {
  display: inline-block;
  width: 100%;
  margin: 10px 0;
  text-align: center;
}
.tRewardBox .el-table__body-wrapper {
  overflow: hidden;
}
.el-table--enable-row-hover .el-table__body tr:hover > td {
  background: transparent;
}
</style>
