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
      <div class="col-md-6">
        <ul id="tree" class="ztree"></ul>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    name: "system-resource",
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
        tree: {},
    }
    },
    created() {
    },
    mounted() {
      let _this = this;
      _this.getAll();
    },
    methods: {
      getAll() {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/resource/load-tree').then(response => {
          Loading.hide();
          let resp = response.data;
          _this.resources = resp.content;
          // 初始化树
          _this.initTree();
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
            _this.getAll();
            Toast.success("保存成功");
          } else {
            Toast.warning(resp.message);
          }
        })
      },
      /**
       * 初始化资源树
       */
      initTree() {
        let _this = this;
        let setting = {
          data: {
            simpleData: {
              idKey: "id",
              pIdKey: "parent",
              rootPId: "",
            }
          }
        };

        _this.zTree = $.fn.zTree.init($("#tree"), setting, _this.resources);
        _this.zTree.expandAll(true);
      }
    }
  }
</script>

<style scoped>

</style>