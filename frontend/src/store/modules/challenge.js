import axios from "axios";
import API from "@/api/index.js";
import challengeAPI from "@/api/challenge.js";

export default {
  namespaced: true,
  state: {
    // 모든 챌린지 리스트가 들어 옵니다.
    challengeList: [],
    userchallengeList: [],
    certList: [],
  },
  getters: {
    challengeList(state) {
      return state.challengeList;
    },
    userchallengeList(state) {
      return state.userchallengeList;
    },
    userfoodChallengeList(state) {
      return state.userfoodChallengeList;
    },
    userclothChallengeList(state) {
      return state.userclothChallengeList;
    },
    usercosmeticChallengeList(state) {
      return state.usercosmeticChallengeList;
    },
    certList(state) {
      return state.certList;
    },
  },
  mutations: {
    // 일단 모든 챌린지 리스트를 받습니다.
    SET_CHALLENGE_LISTS(state, challengeList) {
      state.challengeList = challengeList;
    },
    SET_USER_CHALLENGE_LISTS(state, userchallengeList) {
      state.userchallengeList = userchallengeList;
    },
    SET_CERT_LIST(state, payload) {
      state.certList = payload;
    },
  },
  actions: {
    // ChallengeList.vue 가 created -> 렌더링 되는 순간 백에서 리스트들을 불러와 줍니다.
    fetchChallengeList({ commit }) {
      const URL = API.url + challengeAPI.search_all();
      axios({
        method: "GET",
        url: URL,
      })
        .then((res) => commit("SET_CHALLENGE_LISTS", res.data))
        .catch((err) => {
          console.error(err);
        });
    },

    // 해당 유저가 챌린지에 참여 의사를 밝혔을 때, 유저 정보에 챌린지 정보를 넣어 줍니다.
    challengeAttend({ dispatch }, { user_id, challenge_id }) {
      const URL = API.url + challengeAPI.attend();
      axios({
        method: "GET",
        url: URL,
        params: {
          user_id,
          challenge_id,
        },
      })
        .then((res) => {
          console.log(res, "유저의 챌린지 세팅 완료!");
          // 다른 스토어의 actions dispatch 호출 방식
          // 이거 두번째 인자로 null 안해주면 조짐, root true를 3번째에 꼭 넣어줘야함
          dispatch("user/getUpdateUserInfo", user_id, { root: true });
        })
        .catch((err) => console.log(err));
    },
    fetchUserChallengeList(store, { challenge_id, user_id }) {
      const URL = API.url + challengeAPI.search_mychallenge(challenge_id, user_id);
      axios({
        method: "GET",
        url: URL,
        params: {
          user_id,
          challenge_id,
        },
      })
        .then((res) => store.commit("SET_USER_CHALLENGE_LISTS", res.data))
        .catch((err) => {
          console.error(err);
        });
    },

    getCertList({ commit }, payload) {
      //console.log(payload);
      axios
        .get(API.url + challengeAPI.search_cert_list(payload))
        .then((res) => {
          commit("SET_CERT_LIST", res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },

  },
};
