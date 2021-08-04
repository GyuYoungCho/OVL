// @/store/modules/user.js
import axios from "axios";
import API from '@/api/index.js'
import userAPI from '@/api/user.js'

export default {
namespaced: true,
	state: {
		userinfo: {
			userid:"",
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
			state.isLogin = false;
			state.userinfo = {
				userid:"",
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
			console.log("jwt 정보" + token);
			if (!token) {
				return;
			}
			axios({
				method: "post",
				url: API.url + userAPI.info(),
				headers: { "access-token" : token}
			}).then((res) => {
				console.log(res.data.UserDto.nickname);

				if (res.data) {
					console.log(res.data.UserDto);
					console.log("성공!!!!!!!!!!!!");
					store.commit("setUserInfo", res.data.UserDto);
					this.state.isLogin = true;
					console.log(this.state)
				}
				else
					console.log("실패.");
			}).catch((err) => {
				console.log();
				console.log(err);
			})

	},
		login(store, loginObj) {
			console.log("로그인 확인");
			return new Promise(function (resolve) {
			axios({
				method: "post",
				url: API.url + userAPI.login(),
				data: loginObj,
			}).then((res) => {
				localStorage.setItem("access-token", res.headers["access-token"])
				resolve();
				//console.log("확인한다 로그인3");
			}).catch((err) => {
				alert("이메일과 비밀번호를 확인하세요.");
				console.log(err);
			});
			
		})
	},
		logout(store) {
			store.commit('setLogout');
		},
		
		confirmuserinfo(store, payload) {
			return new Promise(function (resolve) {
				let token = localStorage.getItem("access-token");
				axios({
					method: "post",
					url: API.url + userAPI.info(),
					data: payload,
					headers: { "access-token" : token}
				}).then((res) => {
					console.log(res.data.UserDto + "확인");
					localStorage.setItem("access-token", res.headers["access-token"]);
					store.commit("setUserInfo", payload);
					resolve();
				}).catch((err) => {
					console.log(err)
				})
			})
		}

}
}