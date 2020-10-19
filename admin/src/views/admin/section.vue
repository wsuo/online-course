<template>
  <div>
    <h4 class="lighter">
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/course" class="pink"> {{course.name}}</router-link>
      ：
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/chapter" class="pink"> {{chapter.name}}</router-link>
    </h4>
    <hr>
    <p>
      <button @click="add" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit red2"></i>新增
      </button> &nbsp;
      <button @click="getAll(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh red2"></i>刷新
      </button>
    </p>
    <table id="simple-table" class="table  table-bordered table-hover">

      <thead>
      <tr>
        <th>id</th>
        <th>标题</th>
        <th>视频</th>
        <th>时长</th>
        <th>收费</th>
        <th>顺序</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="section in sections">
        <td>{{section.id}}</td>
        <td>{{section.title}}</td>
        <td>{{section.video}}</td>
        <td>{{section.time | formatSecond}}</td>
        <td>{{CHARGE | optionKV(section.charge)}}</td>
        <td>{{section.sort}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="edit(section)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(section.id)" class="btn btn-xs btn-danger">
              <i class="ace-icon fa fa-trash-o bigger-120"></i>
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="title" class="col-sm-2 control-label">标题</label>
                <div class="col-sm-10">
                  <input v-model="section.title" id="title" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{course.name}}</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">大章</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{chapter.name}}</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">视频</label>
                <div class="col-sm-10">
                  <vod :text="'上传VOD'"
                       :suffixs='["mp4"]'
                       :use="FILE_USE.COURSE.key"
                       :after-upload="afterUpload"></vod>
                  <!--想把那一行变成 12 格就在哪里增加一个 row -->
                  <div v-show="section.video" class="row">
                    <!--占这 12 格中的 4 格-->
                    <div class="col-md-9">
                      <player ref="player"></player>
                      <video :src="section.video" id="video" controls="controls" class="hidden"></video>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="time" class="col-sm-2 control-label">时长</label>
                <div class="col-sm-10">
                  <input v-model="section.time" id="time" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">视频</label>
                <div class="col-sm-10">
                  <input v-model="section.video" class="form-control" disabled>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">VOD</label>
                <div class="col-sm-10">
                  <input v-model="section.vod" class="form-control" disabled>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">收费</label>
                <div class="col-sm-10">
                  <label>
                    <select v-model="section.charge" class="form-control">
                      <option v-for="o in CHARGE" :value="o.key">{{o.value}}</option>
                    </select>
                  </label>
                </div>
              </div>
              <div class="form-group">
                <label for="sort" class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="section.sort" id="sort" class="form-control">
                </div>
              </div>
            </form>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button @click="save" type="button" class="btn btn-primary">保存</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--
        :list="getAll"

        list: 是子组件暴露出来的一个回调方法;
        getAll: 是父组件的 getAll 方法;
      -->
    <pagination ref="pagination" :list="getAll" :itemCount="8"/>
  </div>
</template>

<script>
  import Pagination from '../../components/pagination'
  import BigFile from '../../components/big-file'
  import Vod from '../../components/vod'
  import Player from '../../components/player'

  export default {
    name: "business-section",
    components: {
      Pagination,
      BigFile,
      Vod,
      Player,
    },
    data() {
      return {
        sections: [],
        section: {
          id: '',
          title: '',
          courseId: '',
          chapterId: '',
          video: '',
          time: '',
          charge: '',
          sort: '',
          createdAt: '',
          updatedAt: '',
          vod: '',
        },
        CHARGE: SECTION_CHARGE,
        FILE_USE: FILE_USE,
        course: {},
        chapter: {},
      }
    },
    created() {
    },
    mounted() {
      let _this = this;
      _this.$refs.pagination.size = 10;
      let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
      let chapter = SessionStorage.get(SESSION_KEY_CHAPTER) || {};
      if (Tool.isEmpty(course) || Tool.isEmpty(chapter)) {
        _this.$router.push("/welcome");
      }
      _this.course = course;
      _this.chapter = chapter;
      _this.getAll(1);
      // sidebar 激活
      this.$parent.activeSidebar("business-course-sidebar")
    },
    methods: {
      getAll(page) {
        let _this = this;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/list', {
          page: page,
          size: _this.$refs.pagination.size,
          courseId: _this.course.id,
          chapterId: _this.chapter.id,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.sections = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      add() {
        let _this = this;
        _this.section = {};
        $("#myModal").modal("show");
      },
      save() {
        let _this = this;
        _this.section.video = "";

        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.section.title, "标题")
          || !Validator.length(_this.section.title, "标题", 1, 50)
          || !Validator.length(_this.section.video, "视频", 1, 200)
        ) {
          return;
        }

        _this.section.courseId = _this.course.id;
        _this.section.chapterId = _this.chapter.id;

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/save',
          _this.section).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          }
        })
      },
      edit(section) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.section = $.extend({}, section);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/section/delete/' + id).then(response => {
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.getAll(1);
              Toast.success("删除成功");
            }
          });
        });
      },
      afterUpload(resp) {
        let _this = this;
        let cont = resp.content;
        _this.section.video = cont.path;
        _this.section.vod = cont.vod;
        _this.getTime();
        // 调用组件的方法播放视频
        _this.$refs.player.playUrl(cont.path);
      },
      /**
       * 获取时长
       *
       */
      getTime() {
        let _this = this;
        setTimeout(function () {
          let ele = document.getElementById("video");
          console.log(ele);
          // duration 是自带的属性: 换成10进制的整数: 放到 time 中去
          _this.section.time = parseInt(ele.duration, 10);
        }, 500);
      }
    }
  }
</script>

<style scoped>
  video {
    width: 100%;
    height: auto;
    margin-top: 10px;
  }
</style>