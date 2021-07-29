// @/api/report.js

export default {
  url: '/report/',
  regist (from_id, to_id) {
    return `regist/${from_id}/${to_id}`
  },
  select (user_id) {
    return `select/${user_id}`
  },
  unreport (from_id, to_id) {
    return `unreport/${from_id}/${to_id}`
  },
}