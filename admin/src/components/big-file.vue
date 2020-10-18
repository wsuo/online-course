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
      return {}
    },
    methods: {
      uploadFile() {
        let _this = this;
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

        let size = file.size;
        let shardTotal = Math.ceil(size / shardSize);

        let param = {
          'shard': '',
          'shardIndex': shardIndex,
          'shardSize': shardSize,
          'shardTotal': shardTotal,
          'use': _this.use,
          'name': file.name,
          'suffix': suffix,
          'size': size,
          'key': key62
        };

        _this.upload(param);
      },
      selectFile() {
        let _this = this;
        $("#" + _this.inputId + "-input").trigger("click");
      },
      /**
       * 分片上传文件
       * @param param 对象
       */
      upload(param) {
        let _this = this;
        let shardIndex = param.shardIndex;
        let shardSize = param.shardSize;
        let shardTotal = param.shardTotal;
        let fileShard = _this.getFileShard(shardIndex, shardSize);
        // 将图片转为 base64 进行传输
        let fileReader = new FileReader();
        fileReader.onload = function (e) {
          param.shard = e.target.result;
          Loading.show();
          _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', param).then(response => {
            Loading.hide();
            let resp = response.data;
            if (shardIndex < shardTotal) {
              // 上传下一个分片
              param.shardIndex += 1;
              // 递归调用
              _this.upload(param);
            }
            _this.afterUpload(resp);
            // 清空原来控件中的值
            $("#" + _this.inputId + "-input").val("");
          });
        };
        fileReader.readAsDataURL(fileShard);
      },
      getFileShard: function (shardIndex, shardSize) {
        let _this = this;
        let file = _this.$refs.file.files[0];
        let start = (shardIndex - 1) * shardSize;
        let end = Math.min(file.size, start + shardSize);
        // 从文件中截取当前的分片数据
        return file.slice(start, end);
      },
    }
  }
</script>

<style scoped>

</style>