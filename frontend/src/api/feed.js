// @/api/feed.js

export default {
  // url: '/challenge/',
  feedmain() {
    return "/feed/postfeed";
  },
  postsearch() {
    return "/feed/postfeed/search";
  },
  postpopular() {
    return "/feed/popularfeed";
  },
  select_alluser() {
    return `/feed/user/select_all`;
  },
};
