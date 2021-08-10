import axios from 'axios'
import API from '@/api/index.js'
import challengeAPI from '@/api/challenge.js'
// import userAPI from "@/api/user.js"

export default {
  namespaced: true,
  state: {
    // 모든 챌린지 + 카테고리별 챌린지 더미데이터 리스트가 들어 옵니다.
    challengeList: [],
    foodChallengeList: [],
    clothChallengeList: [],
    cosmeticChallengeList: [],
    
  },
  getters: {
    challengeList (state) {
      return state.challengeList
    },
    foodChallengeList (state) {
      return state.foodChallengeList
    },
    clothChallengeList (state) {
      return state.clothChallengeList
    },
    cosmeticChallengeList (state) {
      return state.cosmeticChallengeList
    }
    
  },
  mutations: {
    // 일단 모든 챌린지 리스트를 받고, filter로 카테고리별로 뽑아낸 리스트를 만들어 줍니다.
    SET_CHALLENGE_LISTS (state, challengeList) {
      state.challengeList = challengeList
      state.foodChallengeList = challengeList.filter((eachList)=> eachList.category===1)
      state.clothChallengeList = challengeList.filter((eachList)=> eachList.category===2)
      state.cosmeticChallengeList = challengeList.filter((eachList)=> eachList.category===3)
    },


  },
  actions: {
    // ChallengeList.vue 가 created -> 렌더링 되는 순간 백에서 리스트들을 불러와 줍니다.
    fetchChallengeList ({commit}) {
      const URL = API.url + challengeAPI.search_all()
      axios({
        method: 'GET',
        url: URL,
      })
        .then((res) => commit('SET_CHALLENGE_LISTS', res.data) )
        .catch((err)=>{console.error(err)})
    },

    // 해당 유저가 챌린지에 참여 의사를 밝혔을 때, 유저 정보에 챌린지 정보를 넣어 줍니다.
    challengeAttend ({dispatch}, {user_id, challenge_id}) {
      const URL = API.url + challengeAPI.attend()
      axios({
        method: 'GET',
        url: URL,
        params: {
          user_id,
          challenge_id
        }
      })
        .then(res => {
          console.log(res,'유저의 챌린지 세팅 완료!')
          // 다른 스토어의 actions dispatch 호출 방식
          // 이거 두번째 인자로 null 안해주면 조짐, root true를 3번째에 꼭 넣어줘야함
          dispatch("user/getUpdateUserInfo", user_id, {root:true})
        })
        .catch((err) => console.log(err))
      },
      
      // 유저의 챌린지 관련 정보를 갱신해 주는 로직
      // fetchUserChallengeInfo ({commit}) {
      //     const URL =  API.url + userAPI.select()
      //   }   


      }
    }
