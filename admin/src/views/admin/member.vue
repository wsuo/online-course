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
        <th>手机号</th>
        <th>密码</th>
        <th>昵称</th>
        <th>头像url</th>
        <th>注册时间</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="member in members">
        <td>{{member.id}}</td>
        <td>{{member.mobile}}</td>
        <td>{{member.password}}</td>
        <td>{{member.name}}</td>
        <td>{{member.photo}}</td>
        <td>{{member.registerTime}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="edit(member)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(member.id)" class="btn btn-xs btn-danger">
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
                  <label for="mobile" class="col-sm-2 control-label">手机号</label>
                  <div class="col-sm-10">
                    <input v-model="member.mobile" id="mobile" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="password" class="col-sm-2 control-label">密码</label>
                  <div class="col-sm-10">
                    <input v-model="member.password" id="password" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="name" class="col-sm-2 control-label">昵称</label>
                  <div class="col-sm-10">
                    <input v-model="member.name" id="name" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="photo" class="col-sm-2 control-label">头像url</label>
                  <div class="col-sm-10">
                    <input v-model="member.photo" id="photo" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="registerTime" class="col-sm-2 control-label">注册时间</label>
                  <div class="col-sm-10">
                    <input v-model="member.registerTime" id="registerTime" class="form-control">
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
    name: "business-member",
    components: {
      Pagination,
    },
    data() {
      return {
        members: [],
        member: {
          id: '',
          mobile: '',
          password: '',
          name: '',
          photo: '',
          registerTime: '',
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/member/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.members = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      add() {
        let _this = this;
        _this.member = {};
        $("#myModal").modal("show");
      },
      save() {
        let _this = this;

        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.member.mobile, "手机号")
          || !Validator.length(_this.member.mobile, "手机号", 1, 11)
          || !Validator.require(_this.member.password, "密码")
          || !Validator.length(_this.member.name, "昵称", 1, 50)
          || !Validator.length(_this.member.photo, "头像url", 1, 200)
        ) {return;}

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/member/save',
          _this.member).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          }
        })
      },
      edit(member) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.member = $.extend({}, member);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/member/delete/' + id).then(response => {
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