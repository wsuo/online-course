import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import filter from './filter/filter'

Vue.config.productionTip = false;
// 以 Vue 属性的方式使用 axios
Vue.prototype.$ajax = axios;

// 解决每次 ajax 请求, 对应的 sessionId 不一致的问题
axios.defaults.withCredentials = true;

/*
* axios 拦截器
* */
axios.interceptors.request.use(function (config) {
  console.log("请求: ",config);
  return config;
}, error => {});

axios.interceptors.response.use(function (response) {
  console.log("返回结果: ", response);
  return response;
}, error => {});

// 全局过滤器
Object.keys(filter).forEach(key => {
  Vue.filter(key, filter[key]);
});

// 路由登录拦截
router.beforeEach((to, from, next) => {
  if (to.matched.some(item => {
    return item.meta.loginRequire;
  })) {
    let loginUser = Tool.getLoginUser();
    if (Tool.isEmpty(loginUser)) {
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');

console.log("环境: ", process.env.NODE_ENV);
