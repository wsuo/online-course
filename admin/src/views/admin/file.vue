<template>
  <div>
    <p>
      <button @click="getAll(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh red2"></i>刷新
      </button>
    </p>
    <table id="simple-table" class="table  table-bordered table-hover">

      <thead>
      <tr>
                    <th>id</th>
            <th>相对路径</th>
            <th>文件名</th>
            <th>后缀</th>
            <th>大小</th>
            <th>用途</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="file in files">
        <td>{{file.id}}</td>
        <td>{{file.path}}</td>
        <td>{{file.name}}</td>
        <td>{{file.suffix}}</td>
        <td>{{file.size | formatFileSize}}</td>
        <td>{{FILE_USE | optionKV(file.use)}}</td>
      </tr>
      </tbody>
    </table>
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
    name: "file-file",
    components: {
      Pagination,
    },
    data() {
      return {
        files: [],
        file: {
          id: '',
          path: '',
          name: '',
          suffix: '',
          size: '',
          use: '',
          createdAt: '',
          updatedAt: '',
        },
        FILE_USE: FILE_USE,
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/file/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.files = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
    }
  }
</script>

<style scoped>

</style>