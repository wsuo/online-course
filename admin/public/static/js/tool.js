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
  },

  /**
   * 时间格式化，date为空时取当前时间
   */
  dateFormat: function (format, date) {
    let result;
    if (!date) {
      date = new Date();
    }
    const option = {
      "y+": date.getFullYear().toString(),        // 年
      "M+": (date.getMonth() + 1).toString(),     // 月
      "d+": date.getDate().toString(),            // 日
      "h+": date.getHours().toString(),           // 时
      "m+": date.getMinutes().toString(),         // 分
      "s+": date.getSeconds().toString()          // 秒
    };
    for (let i in option) {
      result = new RegExp("(" + i + ")").exec(format);
      if (result) {
        format = format.replace(result[1], (result[1].length === 1) ? (option[i]) : (option[i].padStart(result[1].length, "0")))
      }
    }
    return format;
  },
};