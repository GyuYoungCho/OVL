// @/api/recipe_comment.js

export default {
  // url: '/recipe/comment/',
  regist () {
    return '/recipe/comment/regist'
  },
  modify () {
    return '/recipe/comment/modify'
  },
  delete (post_comment_id) {
    return `/recipe/comment/delete/${post_comment_id}`
  },
  search_all (recipe_id) {
    return `/recipe/comment/search_all/${recipe_id}`
  },
  like_list (user_id) {
    return `/recipe/comment/like_list/${user_id}`
  },
  like (user_id, recipe_comment_id) {
    return `/recipe/comment/like/${user_id}/${recipe_comment_id}`
  },
}