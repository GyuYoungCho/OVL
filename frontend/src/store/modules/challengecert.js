import axios from "axios";
import API from "@/api/index.js";
import challengeAPI from "@/api/challenge.js";

export default {
  namespaced: true,
  state: {
    certList: [],
  },
  getters: {
    certList(state) {
      return state.certList;
    },
  },
  mutations: {
    setCertList(state, payload) {
      state.certList = payload;
    },
  },
  actions: {
    getCertList({ commit }, payload) {
      axios
        .get(API.url + challengeAPI.search_cert_list(payload))
        .then((res) => {
          commit("setCertList", res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
