// @/store/modules/user.js
import axios from "axios";
import API from "@/api/index.js";
import userAPI from "@/api/user.js";

export default {
  namespaced: true,
  state: {
    userinfo: {
      userid: "",
      email: "",
      password: "",
      name: "",
      nickname: "",
      phone: "",
      filepath: "",
      challengeId: {
        start_date: null,
      },
    },
    rank: null,
    percent: null,
    isLogin: false,

    modalOpen: false,
    modalContent: "",
    userlist: [],
  },
  mutations: {
    setUserInfo(state, payload) {
      state.isLogin = true;
      state.userinfo = payload;
      console.log("유저 : ", state.userinfo.nickname);
    },
    setLogout(state) {
      state.isLogin = false;
      state.userinfo = {
        userid: "",
        email: "",
        password: "",
        name: "",
        nickname: "",
        phone: "",
        filepath: "",
      };
      state.rank = '',
        state.percent = '',
      localStorage.removeItem("access-token");
      localStorage.removeItem("vuex");
    },
    setReset(state) {
      state.userinfo = {
        userid: "",
        email: "",
        password: "",
        name: "",
        nickname: "",
        phone: "",
        filepath: "",
        challengeId: {
          start_date: null,
        },
        rank: null,
        percent: null,
      };
    },

    setUserRank(state, payload) {
      state.rank = payload;
    },

    setUserPercent(state, payload) {
      state.percent = payload;
    },

    setModal(state, { modalOpen, modalContent }) {
      state.modalOpen = modalOpen;
      state.modalContent = modalContent;
    },
    setUserList(state, payload) {
      if (payload) {
        state.userlist = payload.sort(function(user1, user2) {
          let x = user1.experience;
          let y = user2.experience;
          if (x > y) {
            return -1;
          }
          if (x < y) {
            return 1;
          }
          return 0;
        });
      } else state.userlist = payload;
    },
  },
  getters: {
    userinfo(state) {
      return state.userinfo;
    },
    userlist(state) {
      return state.userlist;
    },
    isLogin(state) {
      return state.isLogin;
    },
    modalOpen(state) {
      return state.modalOpen;
    },
    modalContent(state) {
      return state.modalContent;
    },
  },
  actions: {
    getUserInfo(store) {
      let token = localStorage.getItem("access-token");
      //console.log("jwt 정보" + token);
      if (!token) {
        return;
      }
      axios({
        method: "post",
        url: API.url + userAPI.info(),
        headers: { "access-token": token },
      })
        .then((res) => {
          if (res.data) {
            store.commit("setUserInfo", res.data.UserDto);
            this.state.isLogin = true;
          } else console.log("실패.");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    login(store, loginObj) {
      return new Promise(function(resolve) {
        axios({
          method: "post",
          url: API.url + userAPI.login(),
          data: loginObj,
        })
          .then((res) => {
            localStorage.setItem("access-token", res.headers["access-token"]);
            resolve();
            store.commit("setUserInfo", res.data.data);
          })
          .catch((err) => {
            store.commit("setModal", { modalOpen: true, modalContent: "이메일과 비밀번호를 확인하세요." });
            setTimeout(() => {
              store.commit("setModal", { modalOpen: false, modalContent: "" });
            }, 1000);
            console.log(err);
          });
      });
    },
    logout(store) {
      store.commit("setLogout");
    },
    deleteUser(store) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "delete",
        url: API.url + userAPI.delete(store.state.userinfo.userid),
        headers: { "access-token": token },
      })
        .then(() => {
          alert("탈퇴가 정상적으로 처리 되었습니다.");
          store.commit("setLogout");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getUserRank(store, payload) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "get",
        url: API.url + userAPI.rank(payload),
        headers: { "access-token": token },
      })
        .then((res) => {
          if (res) {
<<<<<<< HEAD
=======
            console.log(res.data.rank);
            console.log("percent", res.data.percent);
>>>>>>> f14b89c0e3c612534c675510a16bf34e2f41d553
            store.commit("setUserRank", res.data.rank);
            store.commit("setUserPercent", res.data.percent);
            this.state.rank = res.rank;
            this.state.percent = res.percent;
          } else console.log("랭크 가져오기 실패.");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getUpdateUserInfo(store, payload) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "get",
        url: API.url + userAPI.tokenUpdate(payload),
        headers: { "access-token": token },
      })
        .then((res) => {
          store.commit("setReset");
          localStorage.setItem("access-token", res.headers["access-token"]);
          store.commit("setUserInfo", res.data.UserDto);
          this.state.isLogin = true;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getUserList(store) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "get",
        url: API.url + userAPI.select_all(),
        headers: { "access-token": token },
      })
        .then((res) => {
          store.commit("setUserList", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
