<template>
  <div>
    <h4>
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/course" class="pink">{{course.name}}</router-link>
    </h4>
    <p>
      <router-link to="/business/course" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-arrow-left"></i>返回课程
      </router-link> &nbsp;
      <button @click="add" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit red2"></i>新增
      </button> &nbsp;
      <button @click="getAll(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh red2"></i>刷新
      </button>&nbsp;
    </p>
    <table id="simple-table" class="table  table-bordered table-hover">

      <thead>
      <tr>
        <th>ID</th>
        <th>名称</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="chapter in chapters">
        <td>{{chapter.id}}</td>
        <td>{{chapter.name}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button v-on:click="toSection(chapter)" class="btn btn-white btn-xs btn-info btn-round">
              小节
            </button>&nbsp;
            <button v-on:click="edit(chapter)" class="btn btn-white btn-xs btn-info btn-round">
              编辑
            </button>&nbsp;
            <button v-on:click="del(chapter.id)" class="btn btn-white btn-xs btn-warning btn-round">
              删除
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
                <label for="name" class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="chapter.name" class="form-control" id="name" placeholder="名称">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{course.name}}</p>
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

  export default {
    name: "chapter",
    components: {
      Pagination,
    },
    data() {
      return {
        chapters: [],
        chapter: {
          name: '',
          courseId: '',
        },
        course: {},
      }
    },
    created() {
    },
    mounted() {
      let _this = this;
      _this.$refs.pagination.size = 10;
      let course = SessionStorage.get("course") || {};
      if (Tool.isEmpty(course)) {
        _this.$router.push("/welcome");
      }
      _this.course = course;
      _this.getAll(1);
    },
    methods: {
      getAll(page) {
        let _this = this;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/chapter/list', {
          page: page,
          size: _this.$refs.pagination.size,
          courseId: _this.course.id,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.chapters = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      add() {
        let _this = this;
        _this.chapter = {};
        $("#myModal").modal("show");
      },
      save() {
        let _this = this;

        // 保存校验
        if (!Validator.require(_this.chapter.name, "名称")
          || !Validator.length(_this.chapter.courseId, "课程ID", 1, 8)) {
          return;
        }
        // ID 不允许修改
        _this.chapter.courseId = _this.course.id;

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/chapter/save',
          _this.chapter).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          }
        })
      },
      edit(chapter) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.chapter = $.extend({}, chapter);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/chapter/delete/' + id).then(response => {
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.getAll(1);
              Toast.success("删除成功");
            }
          });
        });
      },
      toSection(chapter) {
        let _this = this;
        SessionStorage.set("chapter", chapter);
        _this.$router.push("/business/section");
      },
    }
  }
</script>

<style scoped>

</style>