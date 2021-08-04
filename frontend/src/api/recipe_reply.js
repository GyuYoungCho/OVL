// @/api/recipe_reply.js

export default {
  // url: '/recipe/reply/',
  regist () {
    return '/recipe/reply/regist'
  },
  modify () {
    return '/recipe/reply/modify'
  },
  delete (reply_id) {
    return `/recipe/reply/delete/${reply_id}`
  },
  select_all (comment_id) {
    return `/recipe/reply/select_all/${comment_id}`
  },
  like_list (user_id) {
    return `/recipe/reply/like_list/${user_id}`
  },
  like (user_id, recipe_reply_id) {
    return `/recipe/reply/like/${user_id}/${recipe_reply_id}`
  }
}