import axios from "axios";
import API from "@/api/index.js";
import potAPI from "@/api/pot.js";
import restaurantAPI from "@/api/restaurant.js";

export default {
  namespaced: true,
  state: {
    count: 0,
    restitems: [],
    potitems: [],
    passpotitems: [],
    userpots: [],
    rest: Object,
    selectpot: Object,
    potattendusers: [],
  },
  getters: {
    count(state) {
      return state.count;
    },
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
    // 레스토랑 리스트
    set_Rest_Items(state, payload) {
      state.restitems = payload;
    },
    // 팟 item
    set_Pot_Items(state, payload) {
      let potdatas = [];
      let notdatas = [];
      let user = [];
      let today = new Date().getTime();

      if (state.userpots) {
        state.userpots.forEach((item) => {
          user.push(item.potid);
        });
      }
      // 기간 지나고 내가 참여한 팟 제외
      if (payload) {
        payload.forEach((item) => {
          if (today < new Date(item.time).getTime()) {
            if (user.length == 0 || !user.includes(item.potid)) {
              // 사람이 다 찬 경우 따로 마감 표시를 하기 위함\
              if (item.pot_count < item.total_people) {
                potdatas.push(item);
              } else {
                notdatas.push(item);
              }
            }
          }
        });
      } else potdatas = payload;

      // 시간 순으로 sorting
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
      } else state.potitems = potdatas;
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
      } else state.passpotitems = notdatas;
    },
    // 내가 참여한 팟 가져오기
    set_User_Pots(state, payload) {
      let today = new Date().getTime();
      let datas = [];
      if (payload) {
        payload.forEach((item) => {
          if (today < new Date(item.time).getTime()) {
            datas.push(item);
          }
        });
      }

      // 시간 순 sorting
      if (datas) {
        state.userpots = datas.sort(function(pot1, pot2) {
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
      } else state.userpots = datas;
    },
    // 하나의 식당 선택
    SELECT_REST(state, payload) {
      state.rest = payload;
    },
    // 상세보기 때 하나의 팟 저장
    SELECT_POT(state, payload) {
      state.selectpot = payload;
    },
    // 팟에 참여한 유저
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
          console.log(store.state.userpots);
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
          console.log("userpot");
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
    // 팟에 참여한 유저
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
