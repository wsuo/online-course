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
        <th>验证码</th>
        <th>用途</th>
        <th>生成时间</th>
        <th>状态</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="sms in smss">
        <td>{{sms.id}}</td>
        <td>{{sms.mobile}}</td>
        <td>{{sms.code}}</td>
        <td>{{SMS_USE | optionKV(sms.use)}}</td>
        <td>{{sms.at}}</td>
        <td>{{SMS_STATUS | optionKV(sms.status)}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button @click="edit(sms)" class="btn btn-xs btn-info">
              <i class="ace-icon fa fa-pencil bigger-120"></i>
            </button>
            <button @click="del(sms.id)" class="btn btn-xs btn-danger">
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
                    <input v-model="sms.mobile" id="mobile" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="code" class="col-sm-2 control-label">验证码</label>
                  <div class="col-sm-10">
                    <input v-model="sms.code" id="code" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="use" class="col-sm-2 control-label">用途</label>
                  <div class="col-sm-10">
                    <select v-model="sms.use" class="form-control" id="use">
                      <option v-for="o in SMS_USE" :value="o.key">{{o.value}}</option>
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <label for="at" class="col-sm-2 control-label">生成时间</label>
                  <div class="col-sm-10">
                    <input v-model="sms.at" id="at" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label for="status" class="col-sm-2 control-label">状态</label>
                  <div class="col-sm-10">
                    <select v-model="sms.status" class="form-control" id="status">
                      <option v-for="o in SMS_STATUS" :value="o.key">{{o.value}}</option>
                    </select>
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
    name: "business-sms",
    components: {
      Pagination,
    },
    data() {
      return {
        smss: [],
        sms: {
          id: '',
          mobile: '',
          code: '',
          use: '',
          at: '',
          status: '',
        },
        SMS_USE: SMS_USE,
        SMS_STATUS: SMS_STATUS,
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/sms/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.smss = resp.content.list;
          // 渲染子组件
          _this.$refs.pagination.render(page, resp.content.total);
        })
      },
      add() {
        let _this = this;
        _this.sms = {};
        $("#myModal").modal("show");
      },
      save() {
        let _this = this;

        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.sms.mobile, "手机号")
          || !Validator.length(_this.sms.mobile, "手机号", 1, 50)
          || !Validator.require(_this.sms.code, "验证码")
          || !Validator.require(_this.sms.use, "用途")
          || !Validator.require(_this.sms.at, "生成时间")
          || !Validator.require(_this.sms.status, "状态")
        ) {return;}

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/sms/save',
          _this.sms).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#myModal").modal("hide");
            _this.getAll(1);
            Toast.success("保存成功");
          }
        })
      },
      edit(sms) {
        let _this = this;
        // 双向绑定问题: 输入的时候表格也会更新数据: 使用 JQuery 的函数解决问题
        _this.sms = $.extend({}, sms);
        $("#myModal").modal("show");
      },
      del(id) {
        let _this = this;
        Confirm.show("该操作不可逆转", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/sms/delete/' + id).then(response => {
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