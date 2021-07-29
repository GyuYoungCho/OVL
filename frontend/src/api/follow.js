// @/api/follow.js

export default {
  url : '/follow/',

  select_following (from_id) {
    return `select_following/${from_id}`
  },
  select_follower (to_id) {
    return `select_follower/${to_id}`
  },
  select_detail () {
    return 'select_detail'
  },
  follow (from_id, to_id) {
    return `follow/${from_id}/${to_id}`
  },
}