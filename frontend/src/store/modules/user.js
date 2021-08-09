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
		stored_file_path: "",
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
        stored_file_path: "",
    };
    localStorage.removeItem("access-token");
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
    console.log("jwt 정보" + token);
    if (!token) {
        return;
    }
    axios({
        method: "post",
        url: API.url + userAPI.info(),
        headers: { "access-token": token },
    })
        .then((res) => {
        console.log(res);

        if (res.data) {
            console.log(res.data.UserDto);
            console.log("성공!!!!!!!!!!!!");
            store.commit("setUserInfo", res.data.UserDto);
            this.state.isLogin = true;
            console.log(this.state);
        } else console.log("실패.");
        })
        .catch((err) => {
        console.log();
        console.log(err);
        });
    },
    login(store, loginObj) {
    console.log("로그인 확인");
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
            //console.log("확인한다 로그인3");
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

    getTokenUserInfo(store) {
    let token = localStorage.getItem("access-token");
    if (!token) {
        return;
    }
    axios({
        method: "get",
        url: API.url + userAPI.tokenValid(),
        headers: { "access-token": token },
    })
        .then((res) => {
        if (res.data) {
            console.log(res.data.UserDto);
            store.commit("setUserInfo", res.data.UserDto);
            this.state.isLogin = true;
        } else console.log("실패.");
        })
        .catch((err) => {
        console.log(err);
        });
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
        axios({
            method: "get",
            url: API.url + userAPI.rank(payload),
        }).then((res) => {
            if (res) {
                console.log(res.data.rank);
                store.commit("setUserRank", res.data.rank);
                this.state.rank = res.rank;
            }
            else
                console.log("랭크 가져오기 실패.");
        }).catch((err) => {
            console.log(err);
        })
    },    

},
};
