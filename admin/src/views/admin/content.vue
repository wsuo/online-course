<template>
  <div>
    <h4 class="lighter">
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/course" class="pink"> {{course.name}}</router-link>
    </h4>
    <hr>

    <file v-bind:input-id="'content-file-upload'"
          v-bind:text="'上传文件'"
          v-bind:suffixs="['jpg', 'jpeg', 'png', 'mp4']"
          v-bind:use="FILE_USE.COURSE.key"
          v-bind:after-upload="afterUploadContentFile"></file>
    <br>
    <table id="file-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>名称</th>
        <th>地址</th>
        <th>大小</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="(f, i) in files" v-bind:key="f.id">
        <td>{{f.name}}</td>
        <td>{{f.url}}</td>
        <td>{{f.size | formatFileSize}}</td>
        <td>
          <button v-on:click="delFile(f)" class="btn btn-white btn-xs btn-warning btn-round">
            <i class="ace-icon fa fa-times red2"></i>
            删除
          </button>
        </td>
      </tr>
      </tbody>
    </table>
    <form class="form-horizontal">
      <div class="form-group">
        <div class="col-lg-12">
          <div id="content"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-lg-12">
          {{saveContentLabel}}
        </div>
      </div>
    </form>
    <p>
      <button type="button" class="btn btn-white btn-info btn-round" v-on:click="saveContent()">
        <i class="ace-icon fa fa-plus blue"></i>
        保存
      </button>&nbsp;
      <router-link to="/business/course" type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
        <i class="ace-icon fa fa-times"></i>
        返回课程
      </router-link>
    </p>
  </div>
</template>

<script>
  import File from "../../components/file";

  export default {
    components: {File},
    name: "business-course-content",
    data: function () {
      return {
        course: {},
        FILE_USE: FILE_USE,
        saveContentLabel: "",
        files: [],
        saveContentInterval: {},
      }
    },
    mounted: function () {
      let _this = this;

      let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
      if (Tool.isEmpty(course)) {
        _this.$router.push("/welcome");
      }
      _this.course = course;

      _this.init();

      // sidebar激活样式方法一
      this.$parent.activeSidebar("business-course-sidebar");
    },
    destroyed: function() {
      let _this = this;
      clearInterval(_this.saveContentInterval);
    },
    methods: {
      /**
       * 保存文件内容
       */
      afterUploadContentFile(response) {
        let _this = this;
        let file = response.content;
        file.courseId = _this.course.id;
        file.url = file.path;
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/save', file).then(response => {
          let resp = response.data;
          if (resp.success) {
            Toast.success("上传文件成功");
            _this.files.push(resp.content);
          }
        });
      },
      delFile(file) {
        let _this = this;
        Confirm.show("删除文件后不可恢复,确认删除?", function () {
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/delete/' + file.id).then(response => {
            let resp = response.data;
            if (resp.success) {
              Toast.success("删除文件成功");
              Tool.removeObj(_this.files, file);
            }
          });
        })
      },
      /**
       * 加载文件内容列表
       */
      listContentFiles() {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/list/' + _this.course.id).then(response => {
          let resp = response.data;
          if (resp.success) {
            _this.files = resp.content;
          }
        });
      },
      /**
       * 打开内容编辑框
       */
      init() {
        let _this = this;
        let course = _this.course;
        let id = course.id;
        let cont = $("#content");
        let course_cont = $("#course-content-modal");
        _this.course = course;
        cont.summernote({
          focus: true,
          height: 300
        });

        // 先清空历史版本
        cont.summernote('code', '');
        _this.saveContentLabel = "";

        // 加载文件内容列表
        _this.listContentFiles();
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/course/find-content/' + id).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            /*
            调用 modal 方法时: 增加 backdrop static 则点击空白位置 模态框不会自动关闭;
             */
            course_cont.modal({backdrop: 'static', keyboard: false});
            if (resp.content) {
              cont.summernote('code', resp.content.content);
            }

            // 定时自动保存
            let saveContentInterval = setInterval(function () {
              _this.saveContent();
            }, 5000);

            // 关闭内容框时 清空自动保存任务
            course_cont.on('hidden.bs.modal', function () {
              clearInterval(saveContentInterval);
            })
          } else {
            Toast.warning(resp.message);
          }
        });
      },

      /*
      保存内容
       */
      saveContent() {
        let _this = this;
        let content = $("#content").summernote("code");
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save-content', {
          id: _this.course.id,
          content: content
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            let now = Tool.dateFormat("hh:mm:ss");
            _this.saveContentLabel = "最后保存时间：" + now;
          } else {
            Toast.warning(resp.message);
          }
        });
      },
    }
  }
</script>

<style scoped>

</style>