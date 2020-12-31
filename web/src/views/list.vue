<template>
  <div>
    <main role="main">
      <div class="header-nav">
        <div class="clearfix">
          <div class="container">
            <div class="row">
              <div class="col-12">
                <a v-on:click="onClickLevel1('00000000')"
                   id="category-00000000"
                   href="javascript:"
                   class="cur">全部
                </a>
                <!-- 所有的一级分类 -->
                <a v-for="o in level1"
                   v-on:click="onClickLevel1(o.id)"
                   v-bind:id="'category-' + o.id" href="javascript:">
                  {{o.name}}
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="skill clearfix">
        <div class="container">
          <div class="row">
            <div class="col-12">
              <a v-on:click="onClickLevel2('11111111')"
                 id="category-11111111"
                 href="javascript:"
                 class="on">不限
              </a>
              <!-- 所有的二级分类 -->
              <a v-for="o in level2"
                 v-on:click="onClickLevel2(o.id)"
                 v-bind:id="'category-' + o.id"
                 href="javascript:">
                {{o.name}}
              </a>
              <div style="clear:both"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="album py-5 bg-light">
        <div class="container">
          <div class="row">
            <div class="col-md-12">
              <pagination ref="pagination" :list="listCourse"/>
            </div>
          </div>
          <div class="row">
            <div v-for="o in courses" class="col-md-4">
              <!--一个课程-->
              <the-course :course="o"/>
            </div>
            <h3 v-show="courses.length === 0">课程还未上架</h3>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
  import TheCourse from "../components/the-course";
  import Pagination from "../components/pagination";

  export default {
    name: "list",
    components: {Pagination, TheCourse},
    mounted() {
      let _this = this;
      // 初始化每页的大小为 1
      _this.$refs.pagination.size = 1;
      _this.listCourse();
      _this.allCategory();
    },
    data() {
      return {
        courses: [{
          id: '',
          name: '',
          summary: '',
          time: '',
          price: '',
          image: '',
          level: '',
          charge: '',
          status: '',
          enroll: '',
          sort: '',
          createdAt: '',
          updatedAt: '',
          categories: [],
          teacherId: '',
        }],
        level1: [],
        level2: [],
        categorys: [],
        level1Id: '',
        level2Id: '',
      }
    },
    methods: {
      /**
       * 展示所有的课程
       * @param page 分页
       */
      listCourse(page) {
        let _this = this;
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/course/list', {
          page: page,
          size: _this.$refs.pagination.size,
          categoryId: _this.level2Id || _this.level1Id || ""
        }).then(response => {
          let resp = response.data;
          _this.courses = resp.content.list;
          _this.$refs.pagination.render(page, resp.content.total);
        }).catch(resp => {
          console.log("error: ", resp);
        })
      },

      /**
       * 所有的分类
       */
      allCategory() {
        let _this = this;
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/category/all').then(response => {
          let resp = response.data;
          _this.categorys = resp.content;

          // 将所有的记录格式化为树形结构
          _this.level1 = [];
          for (let i = 0; i < _this.categorys.length; i++) {
            let c = _this.categorys[i];

            // 如果分类的父 id 是 00000000;表示他是一级分类
            if (c.parent === '00000000') {
              _this.level1.push(c);

              // 否则是二级分类
            } else {
              _this.level2.push(c);
            }
          }
        });
      },

      /**
       * 点击一级分类时
       * @param level1Id 一级分类ID
       */
      onClickLevel1(level1Id) {
        let _this = this;

        // 点击一级分类: 设置变量: 用于课程筛选
        _this.level2Id = null;
        _this.level1Id = level1Id;

        // 如果点击的是[全部],则一级分类为空
        if (level1Id === '00000000') {
          _this.level1Id = null;
        }

        // 点击一级分类时,显示激活状态
        let cate_l1 = $("#category-" + level1Id);
        let cate_1 = $("#category-11111111");
        // 移除所有的兄弟节点的样式
        cate_l1.siblings("a").removeClass("cur");
        cate_l1.addClass("cur");

        // 点击一级分类时,二级分类[无限]按钮要设置成激活状态
        cate_1.siblings("a").removeClass("on");
        cate_1.addClass("on");

        // 注意: 要先把 level2 中的所有的值清空,再往里放;
        _this.level2 = [];
        let categories = _this.categorys;

        // 如果点击的是全部,则显示所有的二级分类
        if (level1Id === '00000000') {
          for (let i = 0; i < categories.length; i++) {
            let c = categories[i];
            if (c.parent !== '00000000') {
              _this.level2.push(c);
            }
          }

        // 如果点击的是某个二级分类-则显示该一级分类下的二级分类
        } else {
          for (let i = 0; i < categories.length; i++) {
            let c = categories[i];
            if (c.parent === level1Id) {
              _this.level2.push(c);
            }
          }
        }

        // 重新加载课程列表
        _this.listCourse(1);
      },

      /**
       * 点击二级分类时
       * @param level2Id 二级分类ID
       */
      onClickLevel2(level2Id) {
        let _this = this;
        let cate_l2 = $("#category-" + level2Id);
        cate_l2.siblings("a").removeClass("on");
        cate_l2.addClass("on");

        // 点击二级分类时: 设置变量: 用于课程查询
        if (level2Id === '11111111') {
          _this.level2Id = null;
        } else {
          _this.level2Id = level2Id;
        }

        _this.listCourse(1);
      }
    }
  }
</script>
<style>
  /* 头部 一级分类 */
  .header-nav {
    height: auto;
    background: #fff;
    box-shadow: 0 8px 16px 0 rgba(28, 31, 33, .1);
    padding: 16px 0;
    box-sizing: border-box;
    position: relative;
    z-index: 1;
    /*background-color: #d6e9c6;*/
  }

  .header-nav > div {
    width: 100%;
    padding-left: 12px;
    box-sizing: border-box;
    margin-left: auto;
    margin-right: auto;
    /*background-color: #B4D5AC;*/
  }

  .header-nav a {
    float: left;
    font-size: 16px;
    color: #07111b;
    line-height: 50px;
    height: 45px;
    position: relative;
    margin-right: 46px;
    font-weight: 700;
  }

  .header-nav a:hover {
    color: #c80;
  }

  .header-nav a.cur {
    color: #c80;
  }

  .header-nav a.cur:before {
    display: block;
  }

  .header-nav a:before {
    display: none;
    content: ' ';
    position: absolute;
    bottom: 0;
    background: #c80;
    width: 16px;
    height: 3px;
    left: 50%;
    margin-left: -8px;
  }

  /* 二级分类 */
  .skill {
    width: 100%;
    padding: 24px 0 0;
    position: relative;
    margin: 0 auto;
  }

  .skill a.on {
    color: #c80;
    background: rgba(204, 136, 0, .1);
  }

  .skill a {
    float: left;
    margin-right: 20px;
    padding: 0 12px;
    font-size: 14px;
    color: #4d555d;
    line-height: 32px;
    border-radius: 6px;
    margin-bottom: 12px;
  }

  .skill a:hover {
    background: #d9dde1;
  }
</style>