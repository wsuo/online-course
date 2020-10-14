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