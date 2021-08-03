// @/api/userlog.js

export default {
  // url: '/userlog/',
  select (user_id) {
    return `/userlog/select/${user_id}`
  },
}