Confirm = {
  // 将变化的代码作为回调函数传递进来
  show: function (message, callback) {
    Swal.fire({
      title: '确认?',
      text: message,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '删除'
    }).then((result) => {
      if (result.isConfirmed && callback) {
        callback();
      }
    });
  }
};