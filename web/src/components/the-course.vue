<template>
  <div>
    <div class="card mb-4 shadow-sm course">
      <img v-bind:src="course.image" class="img-fluid" alt="">
      <div class="card-body">
        <h4 class="">{{course.name}}</h4>
        <p class="card-text">{{course.summary}}</p>
        <div class="d-flex justify-content-between align-items-center">
          <div class="btn-group">
            <router-link v-bind:to="'/detail?id=' + course.id" class="btn btn-sm btn-outline-secondary">课程详情</router-link>
          </div>
          <div class="text-muted">
            <span class="badge badge-info">&nbsp;
              <i class="fa fa-yen" aria-hidden="true">&nbsp;{{course.price}}&nbsp;</i>&nbsp;
            </span>&nbsp;
            <span class="badge badge-info">&nbsp;
              <i class="fa fa-user" aria-hidden="true">&nbsp;{{course.enroll}}&nbsp;</i>
            </span>&nbsp;
            <span class="badge badge-info">&nbsp;
              {{COURSE_LEVEL | optionKV(course.level)}}&nbsp;
            </span>&nbsp;
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "the-course",
    props: {
      course: {}
    },
    data: function () {
      return {
        COURSE_LEVEL: COURSE_LEVEL,
      }
    },
  }
</script>

<style scoped>
  .course h4 {
    font-size: 1.25rem;
    margin: 15px 0;
  }

  .course .text-muted .badge {
    font-size: 1rem;
  }
</style>