// @/api/pot.js

export default {
  // url: '/pot/',
  regist () {
    return '/pot/regist'
  },
  modify () {
    return '/pot/modify'
  },
  delete (pot_id) {
    return `/pot/delete/${pot_id}`
  },
  attend (user_id, pot_id) {
    return `/pot/attend/${user_id}/${pot_id}`
  },
  select_all () {
    return '/pot/select_all'
  },
  select (user_id) {
    return `/pot/select/${user_id}`
  },
  attendcount (pot_id) {
    return `/pot/attendcount/${pot_id}`
  },
}