// @/api/post_reply.js

export default {
  url: '/post/reply/',
  regist () {
    return 'regist'
  },
  modify () {
    return 'modify'
  },
  delete (reply_id) {
    return `delete/${reply_id}`
  },
  select_all (comment_id) {
    return `select_all/${comment_id}`
  },
  like_list (user_id) {
    return `like_list/${user_id}`
  },
  like (user_id, post_reply_id) {
    return `like/${user_id}/${post_reply_id}`
  }
}