// @/api/report.js

export default {
  // url: '/report/',
  regist (from_id, to_id) {
    return `/report/regist/${from_id}/${to_id}`
  },
  select (user_id) {
    return `/report/select/${user_id}`
  },
  unreport (from_id, to_id) {
    return `/report/unreport/${from_id}/${to_id}`
  },
}