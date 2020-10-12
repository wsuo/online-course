Tool = {
  /**
   * 空校验: null 或 "" 都返回 true
   * @param obj 判断的参数
   * @returns {boolean} 返回值
   */
  isEmpty: function (obj) {
    if ((typeof obj == "string")) {
      return !obj || obj.replace(/\s+/g, "") === "";
    } else {
      return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
    }
  },

  /**
   * 非空校验
   * @param obj 判断的参数
   * @returns {boolean} 返回值
   */
  isNotEmpty: function (obj) {
    return !this.isEmpty(obj);
  },

  /**
   * 长度校验
   * @param str 参数
   * @param min 最小值
   * @param max 最大值
   * @returns {boolean} 返回值
   */
  isLength: function (str, min, max) {
    return $.trim(str).length >= min && $.trim(str).length <= max;
  }
};