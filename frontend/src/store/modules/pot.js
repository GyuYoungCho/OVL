import axios from "axios";
import API from "@/api/index.js";
import potAPI from "@/api/pot.js";
import restaurantAPI from "@/api/restaurant.js";

export default {
  namespaced: true,
  state: {
    restitems: [],
    potitems: [],
    passpotitems: [],
    userpots: [],
    rest: Object,
    selectpot: Object,
    potattendusers: [],
  },
  getters: {
    restitems(state) {
      return state.restitems;
    },
    potitems(state) {
      return state.potitems;
    },
    passpotitems(state) {
      return state.passpotitems;
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
    potattendusers(state) {
      return state.potattendusers;
    },
  },
  mutations: {
    set_Rest_Items(state, payload) {
      state.restitems = payload;
    },
    set_Pot_Items(state, payload) {
      let potdatas = [];
      let notdatas = [];
      let user = [];
      let today = new Date().getTime();
      console.log(today);
      if (state.userpots) {
        state.userpots.forEach((item) => {
          user.push(item.potid);
        });
      }
      if (payload) {
        payload.forEach((item) => {
          if (!user.includes(item.potid)) {
            if (today < new Date(item.time).getTime()) {
              potdatas.push(item);
            } else {
              notdatas.push(item);
            }
          }
        });
      }
      if (potdatas) {
        state.potitems = potdatas.sort(function(pot1, pot2) {
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
      if (notdatas) {
        state.passpotitems = notdatas.sort(function(pot1, pot2) {
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
      console.log(state.userpots);
    },
    SELECT_REST(state, payload) {
      state.rest = payload;
    },
    SELECT_POT(state, payload) {
      state.selectpot = payload;
    },
    set_Attend_User(state, payload) {
      state.potattendusers = payload;
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
          console.log(res.data);
          console.log(user_id);
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

    potAttendUsers({ commit }, pot_id) {
      axios
        .get(API.url + potAPI.attendcount(pot_id))
        .then((res) => {
          commit("set_Attend_User", res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
