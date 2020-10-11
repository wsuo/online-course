Loading = {
  show: function () {
    $.blockUI({
      message: '<img src="/static/image/loading.gif">',
      css: {
        padding: "10px",
        left: "50%",
        width: "80px",
        marginLeft: "-40px",
      }
    });
  },
  hide: function () {
    // 本地查询速度过快,所以故意做一个延迟
    setTimeout(function () {
      $.unblockUI();
    }, 500)
  }
};