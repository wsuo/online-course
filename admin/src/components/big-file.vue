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
        let shardSize = 10 * 1024 * 1024;
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

        _this.check(param);
      },

      /*
      检查文件状态: 是否已经上传过 传到第几个分片
      */
      check(param) {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/file/admin/check/' + param.key).then(response => {
          let resp = response.data;
          if (resp.success) {
            let obj = resp.content;
            if (!obj) {
              // 没有找到文件记录: 所以从 1 开始上传
              param.shardIndex = 1;
              _this.upload(param);
            } else if (obj.shardIndex === obj.shardTotal) {
              // 已上传分片 == 分片总数: 说明全部上传完毕: 不需要再上传
              Toast.success("文件极速秒传成功!");
              _this.afterUpload(resp);
              $("#" + _this.inputId + "-input").val("");
            } else {
              param.shardIndex = obj.shardIndex + 1;
              console.log("找到文件记录,从分片" + param.shardIndex + "开始上传");
              _this.upload(param);
            }
          } else {
            Toast.warning("文件上传失败!");
            $("#" + _this.inputId + "-input").val("");
          }
        });
      },

      /*
      * 选择文件之后自动出发请求事件
      * */
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
            } else {
              _this.afterUpload(resp);
              // 清空原来控件中的值
              $("#" + _this.inputId + "-input").val("");
            }
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