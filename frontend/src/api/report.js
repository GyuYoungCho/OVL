// @/api/report.js

export default {
  // url: '/report/',
  report (user_id, report_id) {
    return `/report/regist/${user_id}/${report_id}`
  },
  select (user_id) {
    return `/report/select/${user_id}`
  },
  unreport (user_id, report_id) {
    return `/report/unreport/${user_id}/${report_id}`
  },
}