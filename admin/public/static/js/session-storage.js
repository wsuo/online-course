// 定义常量: 课程管理页面点击章管理时: 保存课程信息
SESSION_KEY_COURSE = "SESSION_KEY_COURSE";
SESSION_KEY_CHAPTER = "SESSION_KEY_CHAPTER";
SESSION_KEY_LOGIN_USER = "SESSION_KEY_LOGIN_USER";

/*
将数据的传输转为 JSON 格式
 */
SessionStorage = {
  get: function (key) {
    let v = sessionStorage.getItem(key);
    if (v && typeof (v) !== "undefined" && v !== "undefined") {
      return JSON.parse(v);
    }
  },
  set: function (key, data) {
    sessionStorage.setItem(key, JSON.stringify(data));
  },
  remove: function (key) {
    sessionStorage.removeItem(key);
  },
  clearAll: function () {
    sessionStorage.clear();
  }
};