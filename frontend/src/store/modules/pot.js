import axios from "axios";
import API from "@/api/index.js";
import potAPI from "@/api/pot.js";
import restaurantAPI from "@/api/restaurant.js";
import bcode from "@/assets/image/defaultImg.png";

export default {
  namespaced: true,
  state: {
    restitems: [],
    potitems: [],
    potattendusers: [],
    userpots: [],
    rest: Object,
  },
  getters: {
    restitems(state) {
      return state.restitems;
    },
    potitems(state) {
      return state.potitems;
    },
    potattendusers(state) {
      return state.potattendusers;
    },
    userpots(state) {
      return state.userpots;
    },
    rest(state) {
      return state.rest;
    },
  },
  mutations: {
    set_Rest_Items(state, payload) {
      state.restitems = payload;
    },
    set_Pot_Items(state, payload) {
      state.potitems = payload;
    },
    set_Pot_Attend_Users(state, payload) {
      state.potattendusers = payload;
    },
    set_User_Pots(state, payload) {
      state.userpots = payload;
    },
    SELECT_REST(state, payload) {
      state.rest = payload;
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
          console.log(typeof bcode);

          store.commit("set_Pot_Items", res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },

    setPotAttendUsers({ commit }, pot_id) {
      axios
        .get(API.url + potAPI.attendcount(pot_id))
        .then((res) => {
          commit("set_Pot_Attend_Users", res.data);
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
  },
};
