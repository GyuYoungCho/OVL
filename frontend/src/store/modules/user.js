// @/store/modules/user.js
import axios from "axios";
import API from '@/api/index.js'
import userAPI from '@/api/user.js'

export default {
namespaced: true,
	state: {
		userinfo: {
			email: "",
			password: "",
			name: "",
			nickname: "",
			phone: "",
		},
		isLogin: false,

	},
	mutations: {
		setUserInfo(state, payload) {
			state.isLogin = true;
			state.userinfo = payload;
		},
		setLogout(state) {
			state.isLogin = true;
			state.userinfo = {
				email: "",
				password: "",
				name:"",
				nickname: "",
				phone: "",
			};
			localStorage.removeItem("access-token");
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
			if (!token) {
				return;
			}
			axios({
				method: "post",
				url: "http://localhost:3000/user/modify_user",
				headers: { "access-token" : token}
			}).then((res) => {
				console.log(res.data.User.nickname);
				if (res.data) {
					store.commit("setUserInfo", res.data.User);
				}
				else
					console.log("실패.");
			}).catch((err) => {
				console.log(err);
			})
	},
		login(store, loginObj) {
			console.log("확인한다 로그인");
			return new Promise(function (resolve) {
			axios({
				method: "post",
				url: API.url + userAPI.login(),
				data: loginObj,
			}).then((res) => {
				localStorage.setItem("access-token", res.header["access-token"]);
				resolve();
			}).catch((err) => {
				alert("이메일과 비밀번호를 확인하세요.");
				console.log(err);
			});
		})
	},
		logout(store) {
			store.commit('setLogout');
	},

}
}