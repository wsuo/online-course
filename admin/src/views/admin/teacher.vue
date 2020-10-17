<template>
  <div>
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
            <th>姓名</th>
            <th>昵称</th>
            <th>头像</th>
            <th>职位</th>
            <th>座右铭</th>
            <th>简介</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="teacher in teachers">
        <td>{{teacher.id}}</td>
        <td>{{teacher.name}}</td>
        <td>{{teacher.nickname}}</td>
        <td>{{teacher.image}}</td>
        <td>{{teacher.position}}</td>
        <td>{{teacher.motto}}</td>
        <td>{{teacher.intro}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="edit(teacher)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(teacher.id)" class="btn btn-xs btn-danger">
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
                  <label for="name" class="col-sm-2 control-label">姓名</label>
                  <div class="col-sm-10">
                    <input v-model="teacher.name" id="name" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="nickname" class="col-sm-2 control-label">昵称</label>
                  <div class="col-sm-10">
                    <input v-model="teacher.nickname" id="nickname" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="image" class="col-sm-2 control-label">头像</label>
                  <div class="col-sm-10">
                    <input v-model="teacher.image" id="image" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="position" class="col-sm-2 control-label">职位</label>
                  <div class="col-sm-10">
                    <input v-model="teacher.position" id="position" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="motto" class="col-sm-2 control-label">座右铭</label>
                  <div class="col-sm-10">
                    <input v-model="teacher.motto" id="motto" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="intro" class="col-sm-2 control-label">简介</label>
                  <div class="col-sm-10">
                    <input v-model="teacher.intro" id="intro" class="form-control">
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
    name: "business-teacher",
    components: {
      Pagination,
    },
    data() {
      return {
        teachers: [],
        teacher: {
          id: '',
          name: '',
          nickname: '',
          image: '',
          position: '',
          motto: '',
          intro: '',
        },
    }
    },
    created() {
    },
    mounted() {
      let _this = this;
      _this.$refs.pagination.size = 10;
      _this.getAll(1);
    },
    methods: {
      getAll(page) {
        let _this = this;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/teacher/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.teachers = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      add() {
        let _this = this;
        _this.teacher = {};
        $("#myModal").modal("show");
      },
      save() {
        let _this = this;

        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.teacher.name, "姓名")
          || !Validator.length(_this.teacher.name, "姓名", 1, 50)
          || !Validator.length(_this.teacher.nickname, "昵称", 1, 50)
          || !Validator.length(_this.teacher.image, "头像", 1, 100)
          || !Validator.length(_this.teacher.position, "职位", 1, 50)
          || !Validator.length(_this.teacher.motto, "座右铭", 1, 50)
          || !Validator.length(_this.teacher.intro, "简介", 1, 500)
        ) {return;}

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/teacher/save',
          _this.teacher).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          }
        })
      },
      edit(teacher) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.teacher = $.extend({}, teacher);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/teacher/delete/' + id).then(response => {
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.getAll(1);
              Toast.success("删除成功");
            }
          });
        });
      }
    }
  }
</script>

<style scoped>

</style>