<template>
  <div>
    <main role="main">
      <div class="album py-5 bg-light">
        <div class="container">
          <div class="row course-head">
            <div class="col-sm-6" id="cover-video-div">
              <img :src="course.image" alt="" class="img-fluid">
            </div>
            <div class="col-sm-6">
              <h1>{{course.name}}</h1>
              <p class="course-head-item">
                <span><i class="fa fa-clock-o"></i>&nbsp;{{(course.time) | formatSecond}}</span>
                <span>{{COURSE_LEVEL | optionKV(course.level)}}</span>
                <span><i class="fa fa-user"></i>&nbsp;{{course.enroll}}</span>
              </p>
              <p class="course-head-desc">{{course.summary}}</p>
              <p class="course-head-price">
                <span class="price-now text-danger"><i class="fa fa-yen"></i>&nbsp;{{course.price}}</span>
              </p>
              <p class="course-head-button-links">
                <a href="javascript:" class="btn btn-lg btn-primary btn-shadow">立即报名</a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
  export default {
    name: "detail",
    data() {
      return {
        id: '',
        course: {},
        teacher: {},
        chapters: [],
        sections: [],
        COURSE_LEVEL: COURSE_LEVEL,
      }
    },
    mounted() {
      let _this = this;
      // 去地址栏中查找 id 这个属性
      _this.id = _this.$route.query.id;
      _this.findCourse();
    },
    methods: {
      /**
       * 查找课程详情
       */
      findCourse() {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/course/find/' + _this.id).then((response) => {
          let resp = response.data;
          _this.course = resp.content;
          _this.teacher = _this.course.teacher || {};
          _this.chapters = _this.course.chapters || {};
          _this.sections = _this.course.sections || {};
        })
      }
    }
  }
</script>

<style>
  /* 课程信息 */
  .course-head {
  }

  .course-head h1 {
    font-size: 2rem;
    margin-bottom: 1.5rem;
  }

  .course-head-item span {
    margin-right: 1rem;
  }

  .course-head-desc {
    font-size: 1rem;
    color: #555
  }

  .course-head a {
  }

  .course-head-price {
    font-size: 2rem;
  }

  @media (max-width: 700px) {
    .course-head h1 {
      font-size: 1.5rem;
    }
  }

  /* 章节列表 */
  .chapter {
    padding-bottom: 1.25rem;
  }

  .chapter-chapter {
    font-size: 1.25rem;
    padding: 1.25rem;
    background-color: #23527c;
    color: white;
    cursor: pointer;
  }

  .chapter-section-tr {
    font-size: 1rem;
  }

  .chapter-section-tr td {
    padding: 1rem 1.25rem;
    vertical-align: middle;
  }

  /*鼠标手势*/
  .chapter-section-tr td .section-title {
    color: #555;
  }

  .chapter-section-tr td .section-title:hover {
    color: #23527c;
    font-weight: bolder;
    cursor: pointer;
  }

  /*行头小图标*/
  .chapter-section-tr td .section-title i {
    color: #2a6496;
  }

  @media (max-width: 700px) {
    .chapter-chapter {
      font-size: 1.2rem;
    }

    .chapter-section-tr {
      font-size: 0.9rem;
    }
  }
</style>
