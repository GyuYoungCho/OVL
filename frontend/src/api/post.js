// @/api/post.js

export default {
  // url: '/post/',
  regist () {
    return '/post/regist'
  },
  modify () {
    return '/post/modify'
  },
  delete (post_id) {
    return `/post/delete/${post_id}`
  },
  select_my (user_id) {
    return `/post/select_my/${user_id}`
  },
  select_all() {
    return '/post/select_all';
  },
  select_user (user_id) {
    return `/post/select_user/${user_id}`
  },
  select_detail (post_id) {
    return `/post/select_detail/${post_id}`
  },
  select_detail_photo (post_id) {
    return `/post/select_detail_photo/${post_id}`
  },
  like_list (user_id) {
    return `/post/like_list/${user_id}`
  },
  like (user_id, post_id) {
    return `/post/like/${user_id}/${post_id}`
  },
  recommendRe() {
    return '/post/recommend_re'
  },
  recommendCh() {
    return '/post/recommend_ch'
  }
}