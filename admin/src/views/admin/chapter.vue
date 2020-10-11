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
        <th>ID</th>
        <th>名称</th>
        <th>课程ID</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="chapter in chapters">
        <td>{{chapter.id}}</td>
        <td>{{chapter.name}}</td>
        <td>{{chapter.courseId}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="edit(chapter)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(chapter.id)" class="btn btn-xs btn-danger">
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
                <label for="name" class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="chapter.name" class="form-control" id="name" placeholder="名称">
                </div>
              </div>
              <div class="form-group">
                <label for="courseId" class="col-sm-2 control-label">课程ID</label>
                <div class="col-sm-10">
                  <input v-model="chapter.courseId" class="form-control" id="courseId" placeholder="课程ID">
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
        _this.$ajax.post('http://127.0.0.1:9000/business/admin/chapter/list', {
          page: page,
          size: _this.$refs.pagination.size,
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
        Loading.show();
        _this.$ajax.post('http://127.0.0.1:9000/business/admin/chapter/save',
          _this.chapter).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            toast.success("保存成功");
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
        Swal.fire({
          title: '确认删除',
          text: "该操作不可逆转!",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: '删除'
        }).then((result) => {
          if (result.isConfirmed) {
            Loading.show();
            _this.$ajax.delete('http://127.0.0.1:9000/business/admin/chapter/delete/' + id).then(response => {
              Loading.hide();
              let resp = response.data;
              if (resp.success) {
                _this.getAll(1);
                toast.success("删除成功");
              }
            });
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>