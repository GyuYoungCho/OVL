// @/api/post_comment.js

export default {
  // url : '/post/comment/',
  regist () {
    return '/post/comment/regist'
  },
  modify (post_comment_id) {
    return `/post/comment/modify/${post_comment_id}`
  },
  delete (post_comment_id) {
    return `/post/comment/delete/${post_comment_id}`
  },
  select_all (post_id) {
    return `/post/comment/search_all/${post_id}`
  },
  like_list (user_id, post_id) {
    return `/post/comment/like_list/${user_id}/${post_id}`
  },
  like (user_id, post_comment_id) {
    return `/post/comment/like/${user_id}/${post_comment_id}`
  }
}