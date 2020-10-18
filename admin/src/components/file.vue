<template>
  <div>
    <button type="button" @click="selectFile" class="btn btn-white btn-default btn-round">
      <i class="ace-icon fa fa-edit red2"></i>{{text}}
    </button>
    <input class="hidden" type="file" :id="inputId + '-input'" v-on:change="uploadFile" ref="file">
  </div>
</template>

<script>
  export default {
    name: "file",
    // props 中的数据都是父组件给的
    props: {
      afterUpload: {
        type: Function,
        default: null
      },
      text: {
        default: '上传文件'
      },
      inputId: {
        default: 'file-upload'
      },
      suffixs: {
        type: Array,
        default: function () {
          return [];
        }
      },
      use: {
        default: ""
      }
    },
    data() {
      return {

      }
    },
    methods: {
      uploadFile() {
        let _this = this;
        let formData = new window.FormData();
        let file = _this.$refs.file.files[0];
        // 判断文件格式
        let suffixs = _this.suffixs;
        let filename = file.name;
        let suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length).toLowerCase();
        let validateSuffix = false;
        for (let suf of suffixs) {
          if (suf.toLowerCase() === suffix) {
            validateSuffix = true;
            break;
          }
        }
        if (!validateSuffix) {
          Toast.warning("文件格式不正确! 只支持上传：" + suffixs.join("  "));
          $("#" + _this.inputId + "-input").val("");
          return;
        }
        // key: "file" 必须和后端 controller 参数名一致
        formData.append('file', file);
        formData.append('use', _this.use);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/oss-simple', formData).then(response => {
          Loading.hide();
          let resp = response.data;
          _this.afterUpload(resp);
          // 清空原来控件中的值
          $("#" + _this.inputId + "-input").val("");
        })
      },
      selectFile() {
        let _this = this;
        $("#" + _this.inputId + "-input").trigger("click");
      }
    }
  }
</script>

<style scoped>

</style>