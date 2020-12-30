<template>
  <div class="pagination" role="group" aria-label="分页">
    <button type="button" class="btn btn-outline-dark"
            v-bind:disabled="page === 1"
            v-on:click="selectPage(1)">
      1
    </button>
    <button type="button" class="btn btn-outline-dark"
            v-bind:disabled="page === 1"
            v-on:click="selectPage(page - 1)">
      上一页
    </button>
    <button v-for="p in pages" v-bind:id="'page-' + p"
            type="button" class="btn btn-outline-dark"
            v-bind:class="{'btn-primary active':page === p}"
            v-on:click="selectPage(p)">
      {{p}}
    </button>
    <button type="button" class="btn btn-outline-dark"
            v-bind:disabled="page === pageTotal"
            v-on:click="selectPage(page + 1)">
      下一页
    </button>
    <button type="button" class="btn btn-outline-dark"
            v-bind:disabled="page === pageTotal"
            v-on:click="selectPage(pageTotal)">
      {{pageTotal||1}}
    </button>
    &nbsp;
  </div>
</template>

<script>
  export default {
    name: "pagination",
    // props 中的数据都是父组件给的
    props: {
      list: {
        type: Function,
        default: null
      },
      //显示的页码数
      itemCount: Number
    },
    data() {
      return {
        // 总行数, 每页条数, 当前页码, 总页码, 显示的页码数组
        total: 0,
        size: 0,
        page: 0,
        pageTotal: 0,
        pages: []
      }
    },
    methods: {
      render(page, total) {
        let _this = this;
        _this.page = page;
        _this.total = total;
        _this.pageTotal = Math.ceil(total / _this.size);
        _this.pages = _this.getPageItems(_this.pageTotal, page, _this.itemCount || 5);
      },

      /**
       * 查询某一页
       * @param page 页码
       */
      selectPage(page) {
        let _this = this;
        if (page < 1) {
          page = 1;
        }
        if (page > _this.pageTotal) {
          page = _this.pageTotal;
        }
        if (_this.page !== page) {
          _this.page = page;
          if (_this.page) {
            _this.list(page)
          }
        }
      },

      /**
       * 当前要显示在页面上的页码
       *
       * @param total 总页数
       * @param current 当前页码
       * @param itemCount 显示的页码数
       * @returns {[]} 返回页码数组
       */
      getPageItems(total, current, itemCount) {
        let items = [];
        if (itemCount >= total) {
          for (let i = 0; i < total; i++) {
            items.push(i + 1);
          }
        } else {
          let base = 0;
          if (current - 0 > Math.floor((itemCount - 1) / 2)) {
            base = Math.min(total, current - 0 + Math.ceil((itemCount - 1) / 2)) - itemCount;
          }
          for (let i = 0; i < itemCount; i++) {
            items.push(base + i + 1);
          }
        }
        return items;
      }
    }
  }
</script>

<style scoped>
  .pagination {
    vertical-align: middle !important;
    font-size: 16px;
    margin-top: 0;
    margin-bottom: 10px;
  }

  .pagination button {
    margin-right: 5px;
  }

  .btn-primary.active {
    background-color: #2f7bba !important;
    border-color: #27689d !important;
    color: white !important;
    font-weight: 600;
  }
</style>