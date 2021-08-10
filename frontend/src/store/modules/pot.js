import axios from "axios";
import API from "@/api/index.js";
import potAPI from "@/api/pot.js";
import restaurantAPI from "@/api/restaurant.js";

export default {
  namespaced: true,
  state: {
    restitems: [],
    potitems: [],
    userpots: [],
    rest: Object,
    selectpot: Object,
  },
  getters: {
    restitems(state) {
      return state.restitems;
    },
    potitems(state) {
      return state.potitems;
    },
    userpots(state) {
      return state.userpots;
    },
    rest(state) {
      return state.rest;
    },
    selectpot(state) {
      return state.selectpot;
    },
  },
  mutations: {
    set_Rest_Items(state, payload) {
      state.restitems = payload;
    },
    set_Pot_Items(state, payload) {
      let datas = [];
      let user = [];
      if (state.consoleuserpots) {
        state.userpots.forEach((item) => {
          user.push(item.potid);
        });
      }
      if (payload) {
        payload.forEach((item) => {
          if (!user.includes(item.potid)) {
            datas.push(item);
          }
        });
      }
      if (datas) {
        state.potitems = datas.sort(function(pot1, pot2) {
          let x = pot1.time;
          let y = pot2.time;
          if (x < y) {
            return -1;
          }
          if (x > y) {
            return 1;
          }
          return 0;
        });
      }
    },
    set_User_Pots(state, payload) {
      if (payload) {
        state.userpots = payload.sort(function(pot1, pot2) {
          let x = pot1.time;
          let y = pot2.time;
          if (x < y) {
            return -1;
          }
          if (x > y) {
            return 1;
          }
          return 0;
        });
      }
    },
    SELECT_REST(state, payload) {
      state.rest = payload;
    },
    SELECT_POT(state, payload) {
      state.selectpot = payload;
    },
  },
  actions: {
    setRestItems(store) {
      axios
        .get(API.url + restaurantAPI.select_all())
        .then((res) => {
          store.commit("set_Rest_Items", res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    setPotItems(store) {
      axios
        .get(API.url + potAPI.select_all())
        .then((res) => {
          store.commit("set_Pot_Items", res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    setUsersPots({ commit }, user_id) {
      axios
        .get(API.url + potAPI.select(user_id))
        .then((res) => {
          commit("set_User_Pots", res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },

    selectRestaurant({ commit }, rest) {
      commit("SELECT_REST", rest);
    },

    selectPot({ commit }, pot) {
      commit("SELECT_POT", pot);
    },
  },
};
