// /@/api/user.js

export default  { 
  // url: '/user/',
    
  nickname_check (nickname) {
    return `/user/nickname_check/${nickname}`
  },
  join () {
    return '/user/join'
  },
  email_auth (type, email) {
    return `/user/email_auth/${type}/${email}`
  },
  email_auth_check (email, authNumber) {
    return `/user/email_auth_check/${email}/${authNumber}`
  },
  login () {
    return '/user/login'
  },
  info () {
    return '/user/info'
  },
  tokenValid () {
    return '/user/tokenValid'
  },
  search_id (name, phone) {
    return `/user/search_id/${name}/${phone}`
  },
  modify_pw (email, password) {
    return `/user/modify_pw/${email}/${password}`
  },
  // PathVariable, RequestPart, RequestParam
  modify_user (nickname, phone) {
    return `/user/modify_user/${nickname}/${phone}`
  },
  select (user_id) {
    return `/user/select/${user_id}`
  },
  delete (user_id) {
    return `/user/delete/${user_id}`
  }
}