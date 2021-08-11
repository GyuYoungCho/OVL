// @/api/challenge.js

export default {
  // url: '/challenge/',
  attend () {
    return '/challenge/attend'
  },
  attend_list (challenge_id) {
    return `/challenge/attend_list/${challenge_id}`
  },
  complete (user_id) {
    return `/challenge/complete/${user_id}`
  },

  delete (user_id) {
    return `/challenge/delete/${user_id}`
  },
  search_all () {
    return '/challenge/search_all'
  },
  search_detail (challenge_id) {
    return `/challenge/search_detail/${challenge_id}`
  },
  search_history (user_id) {
    return `/challenge/search_history/${user_id}`
  },
  search_mychallenge (challenge_id, user_id) {
    return `/challenge/search_mychallenge/${challenge_id}/${user_id}`;
  },
}