import axios from "axios";
import API from '@/api/index.js';
import reportAPI from '@/api/report.js'

export default {
    namespaced: true,
    state: {
        reportList:[],
    },
    getters: {
        reportList(state) {
            return state.reportList;
        }
    },
    mutations: {
        SELECT_REPORT(state, payload) {
            state.reportList = payload;
        }
    },
    actions: {
        reportRegist({dispatch}, payload) {
            const URL = API.url + reportAPI.report(payload.fromId, payload.toId);
            axios.post(URL, payload.reason)
            .then((res)=>{
                if (res.data=="success") dispatch("selectReport", payload.fromId);
            }).catch((err)=>console.error(err));
        },
        selectReport({commit}, payload) {
            const URL = API.url + reportAPI.select(payload);
            axios.get(URL)
            .then((res)=>{
                commit("SELECT_REPORT", res.data);
            }).catch((err)=> console.error(err));
        }, 
        reportCancel({dispatch}, payload) {
            const URL = API.url + reportAPI.unreport(payload.fromId, payload.toId);
            axios.get(URL)
            .then((res)=> {
                if (res.data=="success") dispatch("selectReport", payload.fromId);
            }).catch((err)=> console.error(err));
        }
    }
}