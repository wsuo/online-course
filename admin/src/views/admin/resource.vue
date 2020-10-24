<template>
  <div>
    <p>
      <button @click="getAll(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh red2"></i>刷新
      </button>
    </p>
    <div class="row">
      <div class="col-md-6">
        <label for="resource-textarea"></label>
        <textarea id="resource-textarea" class="form-control" v-model="resourceStr" name="resource" rows="10"></textarea>
        <br>
        <button id="save-btn" type="button" class="btn btn-primary" v-on:click="save()">
          保存
        </button>
      </div>
      <!--<div class="col-md-6">
        <ul id="tree" class="ztree"></ul>
      </div>-->
    </div>
    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>id</th>
        <th>名称</th>
        <th>页面</th>
        <th>请求</th>
        <th>父id</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="resource in resources">
        <td>{{resource.id}}</td>
        <td>{{resource.name}}</td>
        <td>{{resource.page}}</td>
        <td>{{resource.request}}</td>
        <td>{{resource.parent}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="edit(resource)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(resource.id)" class="btn btn-xs btn-danger">
              <i class="ace-icon fa fa-trash-o bigger-120"></i>
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <pagination ref="pagination" :list="getAll" :itemCount="8"/>
  </div>
</template>

<script>
  import Pagination from '../../components/pagination'

  export default {
    name: "system-resource",
    components: {
      Pagination,
    },
    data() {
      return {
        resources: [],
        resource: {
          id: '',
          name: '',
          page: '',
          request: '',
          parent: '',
        },
        resourceStr: '',
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/resource/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.resources = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      save() {
        let _this = this;

        if (Tool.isEmpty(_this.resourceStr)) {
          Toast.warning("资源不能为空");
          return;
        }
        let json = JSON.parse(_this.resourceStr);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/resource/save',
          json).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          }
        })
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/system/admin/resource/delete/' + id).then(response => {
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