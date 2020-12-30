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
        }).then(response => {
          let resp = response.data;
          _this.courses = resp.content.list;
          _this.$refs.pagination.render(page, resp.content.total);
        }).catch(resp => {
          console.log("error: ", resp);
        })
      },
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
              for (let j = 0; j < _this.categorys.length; j++) {
                let child = _this.categorys[j];
                if (child.parent === c.id) {
                  if (Tool.isEmpty(c.children)) {
                    c.children = [];
                  }
                  c.children.push(child)
                }
              }

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
      },

      /**
       * 点击二级分类时
       * @param level2Id 二级分类ID
       */
      onClickLevel2(level2Id) {
        let _this = this;
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