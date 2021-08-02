// /@/api/user.js

export default  {
  url: '/user/',
    
  nickname_check (nickname) {
    return `nickname_check/${nickname}`
  },
  join () {
    return 'join'
  },
  email_auth (email) {
    return `email_auth/${email}`
  },
  email_auth_check (authNumber) {
    return `email_auth_check/${authNumber}`
  },
  login () {
    return 'login'
  },
  search_id (name, phone) {
    return `search_id/${name}/${phone}`
  },
  modify_pw () {
    return 'modify_pw'
  },
  // PathVariable, RequestPart, RequestParam
  modify_user (nickname, phone) {
    return `modify_user/${nickname}/${phone}`
  },
  select (user_id) {
    return `select/${user_id}`
  },
  delete (user_id) {
    return `delete/${user_id}`
  }
}