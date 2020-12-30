<template>
  <div>
    <main role="main">
      <section class="jumbotron text-center">
        <div class="container">
          <h1>在线视频课程平台</h1>
          <p class="lead text-muted">知识付费时代刚刚起步，在这个领域有很多的发展机会。硕子鸽手把手教你学IT，一步一步搭建一个完整的企业级项目。不讲废话，只讲干货。</p>
          <p>
            <!-- p-3 就是 padding: 1rem-->
            <a href="#" class="btn btn-primary my-2 p-3 font-weight-bold">点击进入所有课程</a>
          </p>
        </div>
      </section>

      <div class="album py-5 bg-light">
        <div class="container">
          <div class="title1">最新上线</div>
          <div class="row">
            <div v-for="o in news" class="col-md-4">
              <!--一个课程-->
              <the-course :course="o"/>
            </div>
          </div>
          <hr>
          <div class="title1">好课推荐</div>
          <div class="row">
            <div v-for="o in courses" class="col-md-4">
              <!--一个课程-->
              <the-course :course="o"/>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
  import TheCourse from "../components/the-course";
  export default {
    name: "list",
    components: {TheCourse},
    mounted() {
      let _this = this;
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
      listCourse() {

      }
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