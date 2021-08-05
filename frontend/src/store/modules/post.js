// @/store/modules/user.js
import axios from "axios";
import API from '@/api/index.js'
import postAPI from '@/api/post.js'

export default {
namespaced: true,
	state: {
        postList:[],

	},
	mutations: {
        GET_POST_LIST(state, payload) {
            state.postList = payload;
            console.log("postList : "+state.postList);
        }
	},
	actions: {
        getPostList(store, payload) {
            console.log("payload : "+payload);
            console.log("url path : "+postAPI.select_all(payload));
            axios({
                method:"get",
                url: API.url + postAPI.select_all(payload),
            }).then((res)=>{
                store.commit("GET_POST_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        }
    }
}