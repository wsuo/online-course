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
                <a v-show="!memberCourse.id" @click="enroll" href="javascript:"
                   class="btn btn-lg btn-primary btn-shadow">立即报名</a>
                <a v-show="memberCourse.id" href="#" class="btn btn-lg btn-success btn-shadow disabled">您已报名</a>
              </p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-9">
              <!-- Tab -->
              <ul class="nav nav-tabs">
                <li class="nav-item">
                  <!-- 跳转到对应的ID: #info -->
                  <a href="#info" data-toggle="tab" class="nav-link active">课程介绍</a>
                </li>
                <li class="nav-item">
                  <a href="#chapter" data-toggle="tab" class="nav-link">章节目录</a>
                </li>
              </ul>

              <br>

              <!-- 两个 Tab 对应的内容 -->
              <div class="tab-content">
                <!-- 因为文本存的是 HTML 格式的 所以需要解析 -->
                <div class="tab-pane active" id="info" v-html="course.content"></div>
                <!-- 大章的展示 -->
                <div class="tab-pane" id="chapter">
                  <div v-for="(chapter, i) in chapters" class="chapter">
                    <div class="chapter-chapter" v-on:click="doFolded(chapter, i)">
                      <span>{{chapter.name}}</span>
                      <span class="pull-right">
                        <i v-show="chapter.folded" class="fa fa-plus-square" aria-hidden="true"></i>
                        <i v-show="!chapter.folded" class="fa fa-minus-square" aria-hidden="true"></i>
                      </span>
                    </div>
                    <div v-show="!chapter.folded">
                      <!-- 大章中的所有小节: 用表格展示 -->
                      <table class="table table-striped">
                        <tr v-for="(s,j) in chapter.sections" class="chapter-section-tr">
                          <td class="col-sm-8 col-xs-12">
                            <div class="section-title" v-on:click="play(s)">
                              <!-- d-none 就是 display: none; 即不显示-->
                              <i class="fa fa-video-camera d-none d-sm-inline"></i>&nbsp;&nbsp;
                              <span class="d-none d-sm-inline">第{{j+1}}节&nbsp;&nbsp;</span>
                              {{s.title}}
                              <span v-show="s.charge !== SECTION_CHARGE.CHARGE.key"
                                    class="badge badge-primary hidden-xs">免费</span>
                            </div>
                          </td>
                          <td class="col-sm-1 text-right">
                            <span class="badge badge-primary">{{s.time | formatSecond}}</span>
                          </td>
                        </tr>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- 讲师信息 -->
            <div class="col-md-3">
              <div class="card" style="width: 18rem;">
                <img :src="teacher.image" alt="" class="card-img-top">
                <div class="card-body">
                  <h5 class="card-title">{{teacher.name}}</h5>
                  <p class="card-text">{{teacher.motto}}</p>
                  <p class="card-text">{{teacher.intro}}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <modal-player ref="modalPlayer"/>
  </div>
</template>

<script>
  import ModalPlayer from "../components/modal-player";

  export default {
    name: "detail",
    components: {ModalPlayer},
    data() {
      return {
        id: '',
        course: {},
        teacher: {},
        chapters: [],
        sections: [],
        memberCourse: {},
        COURSE_LEVEL: COURSE_LEVEL,
        SECTION_CHARGE: SECTION_CHARGE,
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

          // 这里的小节是全部的小节: 需要将他放入对应的大章里面
          for (let i = 0; i < _this.chapters.length; i++) {
            let c = _this.chapters[i];
            c.sections = [];
            for (let j = 0; j < _this.sections.length; j++) {
              let s = _this.sections[j];
              if (c.id === s.chapterId) {
                c.sections.push(s);
              }
            }
            Tool.sortAsc(c.sections, "sort");
          }
        });
      },

      /**
       * 展开或收缩一个章节
       * @param chapter 大章
       * @param i 序号
       */
      doFolded(chapter, i) {
        let _this = this;
        // 初始时默认是 false;就是展开的状态
        chapter.folded = !chapter.folded;
        // 在 v-for 里写 v-show,只修改属性不起作用,需要 $set;
        // 将 chapter 放到 chapters 数组中的 i 位置;
        _this.$set(_this.chapters, i, chapter);
      },

      /**
       * 播放视频
       * @param section 小节
       */
      play(section) {
        let _this = this;
        // 如果视频是免费的可以直接播放; 收费的需要登录
        if (section.charge === _this.SECTION_CHARGE.CHARGE.key) {
          Toast.warning("请先登录");
        } else {
          _this.$refs.modalPlayer.playVod(section.vod);
        }
      },

      /**
       * 报名
       */
      enroll() {
        let _this = this;
        let loginMember = Tool.getLoginMember();
        if (Tool.isEmpty(loginMember)) {
          Toast.warning("请先登录");
          return;
        }
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member-course/enroll', {
            courseId: _this.course.id,
            memberId: loginMember.id,
          }
        ).then(response => {
          let resp = response.data;
          if (resp.success) {
            _this.memberCourse = resp.content;
            Toast.success("报名成功!");
          } else {
            Toast.warning(resp.message);
          }
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
