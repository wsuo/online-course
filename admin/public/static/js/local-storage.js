/*
将数据的传输转为 JSON 格式
 */
LocalStorage = {
  get: function (key) {
    let v = localStorage.getItem(key);
    if (v && typeof (v) !== "undefined" && v !== "undefined") {
      return JSON.parse(v);
    }
  },
  set: function (key, data) {
    localStorage.setItem(key, JSON.stringify(data));
  },
  remove: function (key) {
    localStorage.removeItem(key);
  },
  clearAll: function () {
    localStorage.clear();
  }
};