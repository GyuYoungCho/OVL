// @/api/post_comment.js

export default {
  url : '/post/comment/',
  regist () {
    return 'regist'
  },
  modify (post_comment_id) {
    return `modify/${post_comment_id}`
  },
  delete (post_comment_id) {
    return `delete/${post_comment_id}`
  },
  select_all (post_id) {
    return `select_all/${post_id}`
  },
  like_list (user_id) {
    return `like_list/${user_id}`
  },
  like (user_id, post_comment_id) {
    return `like/${user_id}/${post_comment_id}`
  }
}