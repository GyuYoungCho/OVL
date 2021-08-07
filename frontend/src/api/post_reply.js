// @/api/post_reply.js

export default {
  // url: '/post/reply/',
  regist () {
    return '/post/reply/regist'
  },
  modify () {
    return '/post/reply/modify'
  },
  delete (reply_id) {
    return `/post/reply/delete/${reply_id}`
  },
  select_all (post_id) {
    return `/post/reply/select_all/${post_id}`
  },
  like_list (user_id) {
    return `/post/reply/like_list/${user_id}`
  },
  like (user_id, post_reply_id) {
    return `/post/reply/like/${user_id}/${post_reply_id}`
  }
}