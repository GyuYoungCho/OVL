// @/store/modules/user.js
import axios from "axios";
import API from "@/api/index.js";
import userAPI from "@/api/user.js";
import router from "@/router";

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
      account_open: "",
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
  mutations: {
    SET_USER_INFO(state, payload) {
      if (!state.isLogin) {
        // 로그인이 되어있지 않다면 지금 로그인했다는 뜻! 로그인 끝내고 메인페이지로 보내기
        state.isLogin = true;
        router.push({ name: "Main" });
      }
      state.userinfo = payload;
      console.log("유저 : ", state.userinfo.nickname);
    },
    SET_LOGOUT(state) {
      state.isLogin = false;
      state.userinfo = {
        userid: "",
        email: "",
        password: "",
        name: "",
        nickname: "",
        phone: "",
        account_open: "",
        filepath: "",
      };
      (state.rank = ""), (state.percent = ""), localStorage.removeItem("access-token");
      localStorage.removeItem("vuex");
    },
    SET_RESET(state) {
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

    SET_USER_RANK(state, payload) {
      state.rank = payload;
    },

    SET_USER_PERCENT(state, payload) {
      state.percent = payload;
    },

    SET_MODAL(state, { modalOpen, modalContent }) {
      state.modalOpen = modalOpen;
      state.modalContent = modalContent;
    },
    SET_USER_LIST(state, payload) {
      if (payload) {
        state.userlist = payload.sort(function(user1, user2) {
          const x = user1.experience;
          const y = user2.experience;
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
  actions: {
    // 토큰으로 유저 확인
    async getUserInfo({ commit }) {
      const token = localStorage.getItem("access-token");
      // 토큰 없으면 함수 실행 끝냄
      if (!token) {
        return;
      }
      // 토큰 있으면 POST 요청으로 유저 정보 가져오기
      await axios({
        method: "post",
        url: API.url + userAPI.info(),
        headers: { "access-token": token },
      })
        .then((res) => {
          if (res.data) {
            commit("SET_USER_INFO", res.data.UserDto);
          } else console.log("실패.");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async login({ commit, dispatch }, loginObj) {
      await axios({
        method: "post",
        url: API.url + userAPI.login(),
        data: loginObj,
      })
        .then((res) => {
          localStorage.setItem("access-token", res.headers["access-token"]);
          dispatch("getUserInfo");
        })
        .catch((err) => {
          // 로그인 실패 시 모달 띄우기
          commit("SET_MODAL", { modalOpen: true, modalContent: "이메일과 비밀번호를 확인하세요." });
          setTimeout(() => {
            commit("SET_MODAL", { modalOpen: false, modalContent: "" });
          }, 1000);
          console.error(err);
        });
    },
    logout({ commit }) {
      commit("SET_LOGOUT");
    },
    deleteUser({ commit, state }) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "delete",
        url: API.url + userAPI.delete(state.userinfo.userid),
        headers: { "access-token": token },
      })
        .then(() => {
          alert("탈퇴가 정상적으로 처리 되었습니다.");
          commit("SET_LOGOUT");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getUserRank({ commit }, payload) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "get",
        url: API.url + userAPI.rank(payload),
        headers: { "access-token": token },
      })
        .then((res) => {
          if (res) {
            console.log(res.data.rank);
            console.log("percent", res.data.percent);
            commit("SET_USER_RANK", res.data.rank);
            commit("SET_USER_PERCENT", res.data.percent);
          } else console.log("랭크 가져오기 실패.");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getUpdateUserInfo({ commit }, payload) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "get",
        url: API.url + userAPI.tokenUpdate(payload),
        headers: { "access-token": token },
      })
        .then((res) => {
          commit("SET_RESET");
          localStorage.setItem("access-token", res.headers["access-token"]);
          commit("SET_USER_INFO", res.data.UserDto);
          this.state.isLogin = true;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getUserList({ commit }) {
      let token = localStorage.getItem("access-token");
      axios({
        method: "get",
        url: API.url + userAPI.select_all(),
        headers: { "access-token": token },
      })
        .then((res) => {
          commit("SET_USER_LIST", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
