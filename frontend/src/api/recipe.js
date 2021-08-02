// @/api/recipe.js

export default {
  // url: '/recipe/',
  regist () {
    return '/recipe/regist'
  },
  contentList () {
    return '/recipe/regist/contentList'
  },
  modify () {
    return '/recipe/modify'
  },
  delete (recipe_id) {
    return `/recipe/delete/${recipe_id}`
  } ,
  select_all () {
    return '/recipe/select_all'
  },
  select_detail (recipe_id) {
    return `/recipe/select_detail/${recipe_id}`
  },
  search (search_title) {
    return `/recipe/search/${search_title}`
  },
  like_list (user_id, recipe_id) {
    return `/recipe/like_list/${user_id}/${recipe_id}`
  },
  like (user_id, recipe_id) {
    return `/recipe/like/${user_id}/${recipe_id}`
  }
}