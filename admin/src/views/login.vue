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
                        <label class="block clearfix">
                          <span class="block input-icon input-icon-right">
                            <div class="input-group">
                              <input type="text" class="form-control" placeholder="验证码" v-model="user.imageCode"/>
                              <span class="input-group-addon" id="basic-addon2">
                                <img @click="loadImageCode()" id="image-code" alt="验证码">
                              </span>
                            </div>
                          </span>
                        </label>
                        <div class="space"></div>
                        <div class="clearfix">
                          <label class="inline">
                            <input type="checkbox" class="ace" v-model="remember"/>
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
          imageCode: '',
          imageCodeToken: ''
        },
        remember: true,
      }
    },
    mounted: function() {
      let _this = this;
      let body = $('body');
      body.attr('class', 'login-layout light-login');
      body.removeClass('no-skin');
      let rememberUser = LocalStorage.get(LOCAL_KEY_REMEMBER_USER);

      if (rememberUser) {
        _this.user = rememberUser;
      }
      // 初始时加载一次验证码图片
      _this.loadImageCode();
    },
    methods: {
      login() {
        let _this = this;

        // 如果密码是从缓存带出来的: 则不需要重新加密
        let md5 = hex_md5(_this.user.password);
        let rememberUser = LocalStorage.get(LOCAL_KEY_REMEMBER_USER) || {};

        if (md5 !== rememberUser.md5) {
          _this.user.password = hex_md5(_this.user.password + KEY);
        }
        _this.user.imageCodeToken = _this.imageCodeToken;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/login', _this.user).then(response => {
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            // resp.content: {id: "wAtUSitS", loginName: "test", name: "test"}
            let cont = resp.content;
            Toast.success('登录成功');

            // 如果用户点击了记住我: 保存到本地
            if (_this.remember) {
              let md5 = hex_md5(_this.user.password);
              LocalStorage.set(LOCAL_KEY_REMEMBER_USER, {
                loginName: cont.loginName,
                password: _this.user.password,
                md5: md5
              });
            } else {
              // 如果用户没有点击记住我: 清空值
              LocalStorage.set(LOCAL_KEY_REMEMBER_USER, null);
            }
            Tool.setLoginUser(cont);
            this.$router.push("/welcome");
          } else {
            Toast.warning(resp.message);
            _this.user.password = "";
            _this.loadImageCode();
          }
        });
      },
      /**
       * 加载验证码
       */
      loadImageCode() {
        let _this = this;
        _this.imageCodeToken = Tool.uuid(8);
        $('#image-code').attr('src', process.env.VUE_APP_SERVER + '/system/admin/kaptcha/image-code/' + _this.imageCodeToken);
      },
    }
  }
</script>

<style scoped>
  .input-group-addon {
    padding: 0;
  }
</style>