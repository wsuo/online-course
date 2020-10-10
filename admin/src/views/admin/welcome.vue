<template>
  <div>你好,欢迎进入控制台</div>
</template>

<script>
  export default {
    name: "welcome",
    mounted() {
      this.$parent.activeSidebar("welcome-sidebar");
    }
  }
</script>

<style scoped>

</style>