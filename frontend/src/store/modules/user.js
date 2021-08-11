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
    rank:null,
    isLogin: false,
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
    localStorage.removeItem("access-token");
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
        };
    },
        
    setUserRank(state, payload) {
        state.rank = payload;
    },

},
getters: {
    userinfo(state) {
    return state.userinfo;
    },
    isLogin(state) {
    return state.isLogin;
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
            //console.log(this.state);
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
                alert("이메일과 비밀번호를 확인하세요.");
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
            headers: {"access-token" : token}
        }).then(() => {
            alert("탈퇴가 정상적으로 처리 되었습니다.");
            store.commit("setLogout");
        }).catch((err) => {
            console.log(err);
        })
    },
    getUserRank(store, payload) {
        let token = localStorage.getItem("access-token");
        axios({
            method: "get",
            url: API.url + userAPI.rank(payload),
            headers: {"access-token" : token}
        }).then((res) => {
            if (res) {
                // console.log(res.data.rank);
                store.commit("setUserRank", res.data.rank);
                this.state.rank = res.rank;
            }
            else
                console.log("랭크 가져오기 실패.");
        }).catch((err) => {
            console.log(err);
        })
    },
    getUpdateUserInfo(store, payload) {
        let token = localStorage.getItem("access-token");
        console.log("token : ", token);
        axios({
            method: "get",
            url: API.url + userAPI.tokenUpdate(payload),
            headers: {"access-token" : token}
        })
        .then((res) => {
            store.commit("setReset");
            localStorage.setItem("access-token", res.headers["access-token"]);
            store.commit("setUserInfo", res.data.UserDto);
                this.state.isLogin = true;
            })
        .catch((err) => {
            console.log(err);
<<<<<<< HEAD
        });
    },
=======
            });
    },
    // getOtherUserInfo(payload) {
    //     axios({
    //         method: "get",
    //         url: API.url + userAPI.select(payload),
    //     }).then((res) => {
    //             console.log(" 다른 유저 : ", res.data);
    //         })
    //         .catch((err) => {
    //         console.log("ohter User 정보 조회 실패"); 
    //         console.log(err);
    //         });
    //     },
>>>>>>> branch 'master' of https://lab.ssafy.com/s05-webmobile2-sub3/S05P13A606.git
        
},
};
