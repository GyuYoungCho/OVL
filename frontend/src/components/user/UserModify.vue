<template>
<div id="app">
	<img v-bind:src=filename width="200px" height="200px"><br>
	<input type="text" v-model=nickname>{{nickname}}<br>
	<input type="text" v-model=phone><br>
	<input type="file" id="picture" ref="picture" accept="image/png, image/jpeg"><br>
	<button v-on:click="update(list)">update</button>
</div>    
</template>

<script>
import {mapGetters, mapState} from "vuex"
import axios from "axios"

export default {
    data () {
        return{
            file: "",
		lists: [],
		path:'',
        }

        
    },
    computed:{
    ...mapGetters('user',["userinfo","isLogin"]),
    ...mapState('user', ["isLogin"]),

    filename(){
			return "profile" + this.lists[0].userid + "/" + this.lists[0].stored_file_path.split('/').reverse()[0]
		},

    },
    created() {
        this.$store.dispatch("user/getTokenUserInfo"),
        this.nickname = this.userinfo.nickname;
         this.userid = this.userinfo.userid;
    },

	methods:{
		update:function(list){
			
			console.log(this.filename)
			console.log(this.$refs.picture[0].files[0])
			var formData = new FormData();
			formData.append("picture",this.$refs.picture[0].files[0])
			formData.append("nickname",list.nickname)
			formData.append("phone",list.phone)
			axios.put(
				'http://localhost:8080/user/modify_user/' + list.userid,
				formData,
				{headers:{'Content-Type':'multipart/form-data'}}
			).then(response=>{
				console.log(response.data)
				this.getList();
			})
		},
		
		getList:function(){
			axios.get('http://localhost:8080/user/select/'+4).then(response=>{
				this.lists.push(response.data)
				console.log(this.lists)
			})
			
		}
	},

	mounted:function() {
		this.$nextTick(function(){
			this.getList()
		})
	},


}
</script>