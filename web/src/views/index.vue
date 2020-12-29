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
          <div class="row">
            <div v-for="o in news" class="col-md-4">
              <div class="card mb-4 shadow-sm">
                <img v-bind:src="o.image" class="img-fluid" alt="">
                <div class="card-body">
                  <h4 class="">{{o.name}}</h4>
                  <p class="card-text">{{o.summary}}</p>
                  <div class="d-flex justify-content-between align-items-center">
                    <div class="btn-group">
                      <button type="button" class="btn btn-sm btn-outline-secondary">View</button>
                      <button type="button" class="btn btn-sm btn-outline-secondary">Edit</button>
                    </div>
                    <div class="text-muted">
                      <span class="badge badge-info"><i class="fa fa-yen" aria-hidden="true">{{o.price}}</i></span>
                      <span class="badge badge-info"><i class="fa fa-user" aria-hidden="true">{{o.enroll}}</i></span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
  export default {
    name: "index",
    mounted() {
      let _this = this;
      _this.listNew();
    },
    data() {
      return {
        news: [{
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
        }]
      }
    },
    methods: {
      /**
       * 查询新上好课
       */
      listNew() {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/course/list-new').then(response => {
          let resp = response.data;
          if (resp.success) {
            _this.news = resp.content;
          }
        }).catch(resp => {
          console.log("error: ", resp);
        });
      }
    }
  }
</script>