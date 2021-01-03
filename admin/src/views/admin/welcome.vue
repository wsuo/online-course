<template>
  <div>
    <div class="space-12"></div>
    <div class="row">
      <div class="col-xs-12">
        <div class="text-center">
          <span class="btn btn-app btn-sm btn-light no-hover">
            <span class="line-height-1 bigger-170 blue"> 1,411 </span>
					  <br/>
					  <span class="line-height-1 smaller-90"> 课程 </span>
					</span>
          <span class="btn btn-app btn-sm btn-yellow no-hover">
            <span class="line-height-1 bigger-170"> 32 </span>
						<br/>
						<span class="line-height-1 smaller-90"> 大章 </span>
					</span>
          <span class="btn btn-app btn-sm btn-pink no-hover">
            <span class="line-height-1 bigger-170"> 4 </span>
            <br/>
            <span class="line-height-1 smaller-90"> 小节 </span>
          </span>
          <span class="btn btn-app btn-sm btn-grey no-hover">
            <span class="line-height-1 bigger-170"> 23 </span>
            <br/>
            <span class="line-height-1 smaller-90"> 讲师 </span>
          </span>
          <span class="btn btn-app btn-sm btn-success no-hover">
            <span class="line-height-1 bigger-170"> 7 </span>
            <br/>
            <span class="line-height-1 smaller-90"> 报名 </span>
          </span>
          <span class="btn btn-app btn-sm btn-primary no-hover">
            <span class="line-height-1 bigger-170"> 55 </span>
            <br/>
            <span class="line-height-1 smaller-90"> 评论 </span>
          </span>
        </div>
        <div class="space-12"></div>
        <div class="hr hr2 hr-double"></div>
        <div class="space-12"></div>
      </div>
      <div class="col-xs-12 infobox-container">
        <div class="infobox infobox-green">
          <div class="infobox-icon">
            <i class="ace-icon fa fa-comments"></i>
          </div>

          <div class="infobox-data">
            <span class="infobox-data-number">32</span>
            <div class="infobox-content">新增评论</div>
          </div>

          <div class="stat stat-success">8%</div>
        </div>

        <div class="infobox infobox-blue">
          <div class="infobox-icon">
            <i class="ace-icon fa fa-twitter"></i>
          </div>

          <div class="infobox-data">
            <span class="infobox-data-number">11</span>
            <div class="infobox-content">新注册会员</div>
          </div>

          <div class="badge badge-success">
            +32%
            <i class="ace-icon fa fa-arrow-up"></i>
          </div>
        </div>

        <div class="infobox infobox-pink">
          <div class="infobox-icon">
            <i class="ace-icon fa fa-shopping-cart"></i>
          </div>

          <div class="infobox-data">
            <span class="infobox-data-number">8</span>
            <div class="infobox-content">课程报名</div>
          </div>
          <div class="stat stat-important">4%</div>
        </div>
        <div class="space-12"></div>
        <div class="hr hr2 hr-double"></div>
        <div class="space-12"></div>
      </div>
      <div class="col-sm-12">
        <div class="widget-box transparent">
          <div class="widget-header widget-header-flat">
            <h4 class="widget-title lighter">
              <i class="ace-icon fa fa-signal"></i>
              销售量
            </h4>
          </div>
          <div class="widget-body">
            <div class="widget-main padding-4">
              <div id="sales-charts"></div>
            </div><!-- /.widget-main -->
          </div><!-- /.widget-body -->
        </div><!-- /.widget-box -->
        <div class="space-12"></div>
        <div class="hr hr2 hr-double"></div>
        <div class="space-12"></div>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div>
</template>

<script>
  export default {
    name: "welcome",
    mounted: function () {
      let _this = this;
      _this.drawSalesCharts();
    },
    methods: {
      drawSalesCharts() {
        // 假数据: 随机生成数组
        let d1 = [];
        for (let i = 0; i < 30; i++) {
          d1.push([i + 1, 2000 + Math.floor((Math.random() * 100) + 1)]);
        }
        let d2 = [];
        for (let i = 0; i < 30; i++) {
          d2.push([i + 1, 1900 + Math.floor((Math.random() * 100) + 1)]);
        }
        let sales_charts = $('#sales-charts').css({'width': '100%', 'height': '220px'});
        $.plot("#sales-charts", [{
          label: "最近30天",
          data: d1
        }, {
          label: "上一周期",
          data: d2
        }], {
          hoverable: true,
          shadowSize: 0,
          series: {
            lines: {
              show: true
            },
            points: {
              show: true
            }
          },
          xaxis: {
            tickLength: 0
          },
          /* 分成多少个点: 最小值: 最大值: 小数点个数*/
          yaxis: {
            ticks: 10,
            min: 1850,
            max: 2150,
            tickDecimals: 0
          },
          grid: {
            backgroundColor: {colors: ["#fff", "#fff"]},
            borderWidth: 1,
            borderColor: '#555'
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>