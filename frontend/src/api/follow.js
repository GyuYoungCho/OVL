// @/api/follow.js

export default {
  // url : '/follow/',

  select_following (from_id) {
    return `/follow/select_following/${from_id}`
  },
  select_follower (to_id) {
    return `/follow/select_follower/${to_id}`
  },
  select_detail () {
    return '/follow/select_detail'
  },
  follow (from_id, to_id) {
    return `/follow/follow/${from_id}/${to_id}`
  },
}