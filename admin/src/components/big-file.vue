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
    name: "big-file",
    // props 中的数据都是父组件给的
    props: {
      afterUpload: {
        type: Function,
        default: null
      },
      text: {
        default: '上传大文件'
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

        // 生成文件标识: 标识上传的是同一个文件
        let key = hex_md5(file);
        let key10 = parseInt(key, 16);
        let key62 = Tool._10to62(key10);

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
        // 文件分片: 以 20M 为一个分片
        let shardSize = 20 * 1024 * 1024;
        // 分片索引: 1表示第一个分片
        let shardIndex = 1;
        let start = (shardIndex - 1) * shardSize;
        let end = Math.min(file.size, start + shardSize);
        // 从文件中截取当前的分片数据
        let fileShard = file.slice(start, end);

        let size = file.size;
        let shardTotal = Math.ceil(size / shardSize);

        // key: "file" 必须和后端 controller 参数名一致
        formData.append('shard', fileShard);
        formData.append('shardIndex', String(shardIndex));
        formData.append('shardSize', String(shardSize));
        formData.append('shardTotal', String(shardTotal));
        formData.append('use', _this.use);
        formData.append('name', file.name);
        formData.append('suffix', suffix);
        formData.append('size', size);
        formData.append('key', key62);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', formData).then(response => {
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