<template>
  <div>
    <main role="main">
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
    }
  }
</script>
<style>
  .title1 {
    width: 100%;
    display: block;
    line-height: 1.5em;
    overflow: visible;
    font-size: 2rem;
    margin-bottom: 2rem;
    text-shadow: #f3f3f3 1px 1px 0, #b2b2b2 1px 2px 0
  }
</style>