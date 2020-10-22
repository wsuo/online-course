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
        <th>登陆名</th>
        <th>昵称</th>
        <th>密码</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users">
        <td>{{user.id}}</td>
        <td>{{user.loginName}}</td>
        <td>{{user.name}}</td>
        <td>{{user.password}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="editPwd(user)" class="btn btn-xs btn-danger">
              <i class="ace-icon fa fa-key bigger-120"></i>
            </button>
            <button @click="edit(user)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(user.id)" class="btn btn-xs btn-danger">
              <i class="ace-icon fa fa-trash-o bigger-120"></i>
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <!-- 表单模态框 -->
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
                <label for="loginName" class="col-sm-2 control-label">登陆名</label>
                <div class="col-sm-10">
                  <input v-model="user.loginName" :disabled="user.id" id="loginName" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label for="name" class="col-sm-2 control-label">昵称</label>
                <div class="col-sm-10">
                  <input v-model="user.name" id="name" class="form-control">
                </div>
              </div>
              <div class="form-group" v-show="!user.id">
                <label for="password" class="col-sm-2 control-label" type="password">密码</label>
                <div class="col-sm-10">
                  <input v-model="user.password" id="password" class="form-control">
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
    <!--密码模态框-->
    <div class="modal fade" id="pwdModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="pwdModalLabel">修改密码</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="pwd" class="col-sm-2 control-label">密码</label>
                <div class="col-sm-9">
                  <input v-model="user.password" id="pwd" class="form-control" type="password">
                </div>
              </div>
            </form>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button @click="savePwd" type="button" class="btn btn-primary">保存</button>
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
    name: "system-user",
    components: {
      Pagination,
    },
    data() {
      return {
        users: [],
        user: {
          id: '',
          loginName: '',
          name: '',
          password: '',
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.users = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      add() {
        let _this = this;
        _this.user = {};
        $("#myModal").modal("show");
      },
      save() {
        let _this = this;

        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.user.loginName, "登陆名")
          || !Validator.length(_this.user.loginName, "登陆名", 1, 50)
          || !Validator.length(_this.user.name, "昵称", 1, 50)
          || !Validator.require(_this.user.password, "密码")
        ) {
          return;
        }
        // 密码 MD5 加密
        _this.user.password = hex_md5(_this.user.password + KEY);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/save',
          _this.user).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          } else {
            Toast.warning(resp.message);
          }
        })
      },
      edit(user) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.user = $.extend({}, user);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/system/admin/user/delete/' + id).then(response => {
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.getAll(1);
              Toast.success("删除成功");
            }
          });
        });
      },
      /**
       * 点击重置密码
       * @param user 用户
       */
      editPwd(user) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.user = $.extend({}, user);
        _this.user.password = null;
        $("#pwdModal").modal("show");
      },
      savePwd() {
        let _this = this;
        // 密码 MD5 加密
        _this.user.password = hex_md5(_this.user.password + KEY);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/save-password',
          _this.user).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#pwdModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          } else {
            Toast.warning(resp.message);
          }
        })
      },
    }
  }
</script>

<style scoped>

</style>