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
            <th>标题</th>
            <th>课程</th>
            <th>大章</th>
            <th>视频</th>
            <th>时长</th>
            <th>收费</th>
            <th>顺序</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="section in sections">
            <td>{{section.id}}</td>
            <td>{{section.title}}</td>
            <td>{{section.courseId}}</td>
            <td>{{section.chapterId}}</td>
            <td>{{section.video}}</td>
            <td>{{section.time}}</td>
            <td>{{CHARGE | optionKV(section.charge)}}</td>
            <td>{{section.sort}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="edit(section)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(section.id)" class="btn btn-xs btn-danger">
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
                  <label for="title" class="col-sm-2 control-label">标题</label>
                  <div class="col-sm-10">
                    <input v-model="section.title" id="title" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="courseId" class="col-sm-2 control-label">课程</label>
                  <div class="col-sm-10">
                    <input v-model="section.courseId" id="courseId" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="chapterId" class="col-sm-2 control-label">大章</label>
                  <div class="col-sm-10">
                    <input v-model="section.chapterId" id="chapterId" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="video" class="col-sm-2 control-label">视频</label>
                  <div class="col-sm-10">
                    <input v-model="section.video" id="video" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="time" class="col-sm-2 control-label">时长</label>
                  <div class="col-sm-10">
                    <input v-model="section.time" id="time" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">收费</label>
                  <div class="col-sm-10">
                      <select v-model="section.charge" class="form-control">
                        <option v-for="o in CHARGE" :value="o.key">{{o.value}}</option>
                      </select>
                  </div>
                </div>
                <div class="form-group">
                  <label for="sort" class="col-sm-2 control-label">顺序</label>
                  <div class="col-sm-10">
                    <input v-model="section.sort" id="sort" class="form-control">
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
    name: "section",
    components: {
      Pagination,
    },
    data() {
      return {
        sections: [],
        section: {
            id: '',
            title: '',
            courseId: '',
            chapterId: '',
            video: '',
            time: '',
            charge: '',
            sort: '',
            createdAt: '',
            updatedAt: '',
        },
        CHARGE: [
          {key: "C",value: "收费"},
          {key: "F",value: "免费"}
        ],
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.sections = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      add() {
        let _this = this;
        _this.section = {};
        $("#myModal").modal("show");
      },
      save() {
        let _this = this;

        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.section.title, "标题")
          || !Validator.length(_this.section.title, "标题", 1, 50)
          || !Validator.length(_this.section.video, "视频", 1, 200)
        ) {return;}

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/save',
          _this.section).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          }
        })
      },
      edit(section) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.section = $.extend({}, section);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/section/delete/' + id).then(response => {
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