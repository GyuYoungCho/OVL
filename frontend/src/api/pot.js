// @/api/pot.js

export default {
  // url: '/pot/',
  regist(userid) {
    return `/pot/regist/${userid}`;
  },
  modify() {
    return "/pot/modify";
  },
  delete(pot_id) {
    return `/pot/delete/${pot_id}`;
  },
  attend(pot_id, user_id) {
    return `/pot/attend/${pot_id}/${user_id}`;
  },
  attend_cancel(pot_id, user_id) {
    return `/pot/attend_cancel/${pot_id}/${user_id}`;
  },
  select_all() {
    return "/pot/select_all";
  },
  select(user_id) {
    return `/pot/select/${user_id}`;
  },
  attendcount(pot_id) {
    return `/pot/attendCount/${pot_id}`;
  },
};
