import axios from "axios";
import API from '@/api/index.js'
import followAPI from '@/api/follow.js'

export default {
    namespaced: true,
    state: {
        followerList: [],
        followingList: [],
        detailFollowUser: [],
    },
    mutations: {
        GET_FOLLOWER_LIST(state, payload) {
            state.followerList = payload;
        },
        GET_FOLLOWING_LIST(state, payload) {
            state.followingList = payload;
            //console.log("팔로잉: " + state.followingList);
        },
        GET_DETAIL_FOLLOWLIST(state, payload) {
            state.detailFollowUser = payload;
            //console.log("상세조회: " + state.detailFollowUser);
        }
    },
    actions: {
        getFollowerList(store, payload) {
            axios({
                method: "get",
                url: API.url + followAPI.select_follower(payload),
            }).then((res) => {
                //console.log("팔로워야~~: "+ res.data)
                store.commit("GET_FOLLOWER_LIST", res.data);
            }).catch((err) => {
                console.log("실패");
                console.log(err);
            })
        },
        getFollowingList(store, payload) {
            axios({
                method: "get",
                url: API.url + followAPI.select_following(payload),
            }).then((res) => {
                //console.log("팔로워야~~: "+ res.data)
                store.commit("GET_FOLLOWING_LIST", res.data);
            }).catch((err) => {
                console.log("실패");
                console.log(err);
            })
        },
        getDetailFollowUser(store, payload) {
            axios({
                method: "post",
                url: API.url + followAPI.select_detail(),
                data: payload,
            }).then((res) => {
                console.log("Detail: " + res.data);
                store.commit("GET_DETAIL_FOLLOWLIST", res.data);
            }).catch((err) => {
                console.log("실패");
                console.log(err);
            })
        }

    }
}
