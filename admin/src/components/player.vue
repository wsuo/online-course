<template>
  <div :id="playerId">

  </div>
</template>

<script>
  export default {
    name: "player",
    props: {
      playerId: {
        default: "player-div"
      }
    },
    data: function () {
      return {
        // 播放器实例
        aliPlayer: {},
      }
    },
    methods: {
      playUrl(url) {
        let _this = this;
        console.log("开始播放:", url);

        // 如果已经有播放器了 就将播放器删除
        if (_this.aliPlayer) {
          _this.aliPlayer = null;
          $("#J_prismPlayer").remove();
        }

        // 初始化播放器
        $("#" + _this.playerId).append("<div  class=\"prism-player\" id=\"J_prismPlayer\"></div>");
        _this.aliPlayer = new Aliplayer({
          id: 'J_prismPlayer',
          width: '100%',
          autoplay: true,
          //支持播放地址播放,此播放优先级最高
          source: url,
          cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png'
        }, function (player) {
          console.log("播放器创建好了")
        })
      }
    },

  }
</script>

<style scoped>

</style>