<template>
  <div class="main-container">
    <div class="main-content">
      <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
          <div class="login-container">
            <div class="center">
              <h1>
                <i class="ace-icon fa fa-leaf green"></i>
                <span class="black"> 控制台登陆</span>
              </h1>
            </div>
            <div class="space-6"></div>
            <div class="position-relative">
              <div id="login-box" class="login-box visible widget-box no-border">
                <div class="widget-body">
                  <div class="widget-main">
                    <h4 class="header blue lighter bigger">
                      <i class="ace-icon fa fa-coffee green"></i>
                      请输入用户名密码
                    </h4>
                    <div class="space-6"></div>
                    <form>
                      <fieldset>
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="用户名" v-model="user.loginName"/>
															<i class="ace-icon fa fa-user"></i>
														</span>
                        </label>
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="密码" v-model="user.password"/>
															<i class="ace-icon fa fa-lock"></i>
														</span>
                        </label>
                        <div class="space"></div>
                        <div class="clearfix">
                          <label class="inline">
                            <input type="checkbox" class="ace"/>
                            <span class="lbl"> 记住我</span>
                          </label>
                          <button type="button" class="width-35 pull-right btn btn-sm btn-primary" @click="login()">
                            <i class="ace-icon fa fa-key"></i>
                            <span class="bigger-110">登陆</span>
                          </button>
                        </div>
                        <div class="space-4"></div>
                      </fieldset>
                    </form>
                  </div><!-- /.widget-main -->
                </div><!-- /.widget-body -->
              </div><!-- /.login-box -->

              <div id="forgot-box" class="forgot-box widget-box no-border">
                <div class="widget-body">
                  <div class="widget-main">
                    <h4 class="header red lighter bigger">
                      <i class="ace-icon fa fa-key"></i>
                      Retrieve Password
                    </h4>
                    <div class="space-6"></div>
                    <p>
                      Enter your email and to receive instructions
                    </p>
                    <form>
                      <fieldset>
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control"
                                     placeholder="Email"/>
															<i class="ace-icon fa fa-envelope"></i>
														</span>
                        </label>
                        <div class="clearfix">
                          <button type="button"
                                  class="width-35 pull-right btn btn-sm btn-danger">
                            <i class="ace-icon fa fa-lightbulb-o"></i>
                            <span class="bigger-110">Send Me!</span>
                          </button>
                        </div>
                      </fieldset>
                    </form>
                  </div><!-- /.widget-main -->
                  <div class="toolbar center">
                    <a href="#" data-target="#login-box" class="back-to-login-link">
                      Back to login
                      <i class="ace-icon fa fa-arrow-right"></i>
                    </a>
                  </div>
                </div><!-- /.widget-body -->
              </div><!-- /.forgot-box -->
              <div id="signup-box" class="signup-box widget-box no-border">
                <div class="widget-body">
                  <div class="widget-main">
                    <h4 class="header green lighter bigger">
                      <i class="ace-icon fa fa-users blue"></i>
                      New User Registration
                    </h4>
                    <div class="space-6"></div>
                    <p> Enter your details to begin: </p>
                    <form>
                      <fieldset>
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control"
                                     placeholder="Email"/>
															<i class="ace-icon fa fa-envelope"></i>
														</span>
                        </label>
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control"
                                     placeholder="Username"/>
															<i class="ace-icon fa fa-user"></i>
														</span>
                        </label>
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control"
                                     placeholder="Password"/>
															<i class="ace-icon fa fa-lock"></i>
														</span>
                        </label>
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control"
                                     placeholder="Repeat password"/>
															<i class="ace-icon fa fa-retweet"></i>
														</span>
                        </label>
                        <label class="block">
                          <input type="checkbox" class="ace"/>
                          <span class="lbl">
															I accept the
															<a href="#">User Agreement</a>
														</span>
                        </label>
                        <div class="space-24"></div>
                        <div class="clearfix">
                          <button type="reset" class="width-30 pull-left btn btn-sm">
                            <i class="ace-icon fa fa-refresh"></i>
                            <span class="bigger-110">Reset</span>
                          </button>
                          <button type="button" class="width-65 pull-right btn btn-sm btn-success">
                            <span class="bigger-110">Register</span>
                            <i class="ace-icon fa fa-arrow-right icon-on-right"></i>
                          </button>
                        </div>
                      </fieldset>
                    </form>
                  </div>
                  <div class="toolbar center">
                    <a href="#" data-target="#login-box" class="back-to-login-link">
                      <i class="ace-icon fa fa-arrow-left"></i>
                      Back to login
                    </a>
                  </div>
                </div><!-- /.widget-body -->
              </div><!-- /.signup-box -->
            </div><!-- /.position-relative -->
          </div>
        </div><!-- /.col -->
      </div><!-- /.row -->
    </div><!-- /.main-content -->
  </div>
</template>

<script>
  export default {
    name: "login",
    data() {
      return {
        user: {
          id: '',
          loginName: '',
          name: '',
          password: '',
        },
      }
    },
    mounted: function() {
      let body = $('body');
      body.attr('class', 'login-layout light-login');
      body.removeClass('no-skin');
    },
    methods: {
      login() {
        let _this = this;
        // 密码 MD5 加密
        _this.user.password = hex_md5(_this.user.password + KEY);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/login', _this.user).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            console.log(resp.content);
            this.$router.push("/welcome");
          } else {
            Toast.warning(resp.message);
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>