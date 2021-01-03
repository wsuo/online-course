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
        <th>角色</th>
        <th>描述</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="role in roles">
        <td>{{role.id}}</td>
        <td>{{role.name}}</td>
        <td>{{role.desc}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="editUser(role)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-user bigger-120"></i>
            </button>
            <button @click="editResource(role)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-list bigger-120"></i>
            </button>
            <button @click="edit(role)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(role.id)" class="btn btn-xs btn-danger">
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
                <label for="name" class="col-sm-2 control-label">角色</label>
                <div class="col-sm-10">
                  <input v-model="role.name" id="name" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label for="desc" class="col-sm-2 control-label">描述</label>
                <div class="col-sm-10">
                  <input v-model="role.desc" id="desc" class="form-control">
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
    <div class="modal fade" id="resource-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">角色资源关联配置</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <ul id="tree" class="ztree"></ul>
              </div>
            </form>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">
                <i class="ace-icon fa fa-times"></i>关闭
              </button>
              <button @click="saveResource" type="button" class="btn btn-primary">
                <i class="ace-icon fa fa-times"></i>保存
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--角色用户关联配置-->
    <div id="user-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">角色用户关联配置</h4>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="col-md-6">
                <table id="user-table" class="table table-hover">
                  <tbody>
                  <tr v-for="user in users">
                    <td>{{user.loginName}}</td>
                    <td class="text-right">
                      <a v-on:click="addUser(user)" href="javascript:" class="">
                        <i class="ace-icon fa fa-arrow-circle-right blue"></i>
                      </a>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div class="col-md-6">
                <table id="role-user-table" class="table table-hover">
                  <tbody>
                  <tr v-for="user in roleUsers">
                    <td>{{user.loginName}}</td>
                    <td class="text-right">
                      <a v-on:click="deleteUser(user)" href="javascript:" class="">
                        <i class="ace-icon fa fa-trash blue"></i>
                      </a>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
              <i class="ace-icon fa fa-times"></i>
              关闭
            </button>
            <button type="button" class="btn btn-white btn-info btn-round" v-on:click="saveUser()">
              <i class="ace-icon fa fa-plus blue"></i>
              保存
            </button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
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
    name: "system-role",
    components: {
      Pagination,
    },
    data() {
      return {
        roles: [],
        users: [],
        roleUsers: [],
        role: {
          id: '',
          name: '',
          desc: '',
        },
        resources: [],
        zTree: {},
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.roles = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      /**
       * 点击【保存】
       */
      save() {
        let _this = this;

        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.role.name, "角色")
          || !Validator.length(_this.role.name, "角色", 1, 50)
          || !Validator.require(_this.role.desc, "描述")
          || !Validator.length(_this.role.desc, "描述", 1, 100)
        ) {
          return;
        }

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/save', _this.role).then((response) => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#form-modal").modal("hide");
            _this.list(1);
            Toast.success("保存成功！");
          } else {
            Toast.warning(resp.message)
          }
        })
      },

      /**
       * 点击【新增】
       */
      add() {
        let _this = this;
        _this.role = {};
        $("#myModal").modal("show");
      },

      /**
       * 点击【编辑】
       */
      edit(role) {
        let _this = this;
        _this.role = $.extend({}, role);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/system/admin/role/delete/' + id).then(response => {
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.getAll(1);
              Toast.success("删除成功");
            }
          });
        });
      },
      editResource(role) {
        let _this = this;
        _this.role = $.extend({}, role);
        _this.loadResource();
        $('#resource-modal').modal('show');
      },
      /**
       * 加载资源树
       */
      loadResource() {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/resource/load-tree').then(response => {
          Loading.hide();
          let resp = response.data;
          _this.resources = resp.content;
          _this.initTree();
          _this.listRoleResource();
        });
      },
      /**
       * 初始化资源树
       */
      initTree() {
        let _this = this;
        let setting = {
          check: {
            enable: true
          },
          data: {
            simpleData: {
              idKey: "id",
              pIdKey: "parent",
              rootPId: "",
              enable: true,
            }
          }
        };
        _this.zTree = $.fn.zTree.init($("#tree"), setting, _this.resources);
        _this.zTree.expandAll(true);
      },
      /**
       * 资源模态框点击保存
       */
      saveResource() {
        let _this = this;
        let resources = _this.zTree.getCheckedNodes();
        // 保存时只需要保存资源 id
        let resourceIds = [];
        for (let i = 0; i < resources.length; i++) {
          resourceIds.push(resources[i].id);
        }

        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/save-resource', {
          id: _this.role.id,
          resourceIds: resourceIds
        }).then(response => {
          let resp = response.data;
          if (resp.success) {
            Toast.success("保存成功");
          } else {
            Toast.warning(resp.message);
          }
        });
      },
      /**
       * 加载角色资源关联记录
       */
      listRoleResource() {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/role/list-resource/' + _this.role.id).then(response => {
          let resp = response.data;
          let resources = resp.content;

          // 勾选查询到的资源: 先把树的所有点清空勾选: 再勾选查询到的节点
          _this.zTree.checkAllNodes(false);
          for (let i = 0; i < resources.length; i++) {
            let node = _this.zTree.getNodeByParam("id", resources[i]);
            _this.zTree.checkNode(node, true);
          }
        });
      },
      /*
      修改用户权限
       */
      editUser(role) {
        let _this = this;
        _this.role = $.extend({}, role);
        _this.listUser();
        $("#user-modal").modal("show");
      },

      /*
      查找所有的用户
       */
      listUser() {
        let _this = this;
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/list', {
          page: 1,
          size: 9999
        }).then(resp => {
          let res = resp.data;
          if (res.success) {
            _this.users = res.content.list;
            _this.listRoleUser();
          } else {
            Toast.warning(res.message);
          }
        });
      },

      /*
      角色中增加用户
       */
      addUser(user) {
        let _this = this;

        // 如果当前要添加的用户在右边的表中已经有了,则不再添加
        let users = _this.roleUsers;
        for (let i = 0; i < users.length; i++) {
          if (user === users[i]) {
            return;
          }
        }

        // 将用户选择的角色添加到 roleUsers 中
        _this.roleUsers.push(user);
      },

      /*
      角色中删除用户: 依赖 Vue 的双向绑定功能-可以将复杂的页面操作变成简单的数据操作
       */
      deleteUser(user) {
        let _this = this;
        Tool.removeObj(_this.roleUsers, user);
      },

      /**
       * 保存角色用户
       */
      saveUser() {
        let _this = this;
        let users = _this.roleUsers;
        let userIds = [];

        for (let i = 0; i < users.length; i++) {
          userIds.push(users[i].id);
        }

        // 调用后端接口
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/save-user', {
          id: _this.role.id,
          userIds: userIds
        }).then(resp => {
          console.log("保存用户角色结果: ", resp);
          let res = resp.data;
          if (res.success) {
            Toast.success("保存成功!");
          } else {
            Toast.warning(res.message);
          }
        });
      },

      /**
       * 加载用户角色
       */
      listRoleUser() {
        let _this = this;
        _this.roleUsers = [];
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/role/list-user/' + _this.role.id).then(resp => {
          let res = resp.data;
          let userIds = res.content;

          for (let i = 0; i < userIds.length; i++) {
            for (let j = 0; j < _this.users.length; j++) {
              if (userIds[i] === _this.users[j].id) {
                _this.roleUsers.push(_this.users[j]);
              }
            }
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>