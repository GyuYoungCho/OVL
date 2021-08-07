import Vue from "vue";
import VueRouter from "vue-router";
import Main from "../views/Main.vue";
import Signup from "../views/Signup.vue";
import Login from "../views/Login.vue";
import NotFound from "../views/errorpages/404.vue";
import ArticleCreate from "../views/ArticleCreate.vue";
import ArticleDetail from "../views/ArticleDetail.vue";
import ChallengeDetail from "../views/ChallengeDetail.vue";
import ChallengeList from "../views/ChallengeList.vue";
import FindEmail from "../views/FindEmail.vue";
import FindPassword from "../views/FindPassword.vue";
import Profile from "../views/Profile.vue";
import RecipeCreate from "../views/RecipeCreate.vue";
import RecipeDetail from "../views/RecipeDetail.vue";
import RecipeSearch from "../views/RecipeSearch.vue";
import VetPartyCreate from "../views/VetPartyCreate.vue";
import VetPartyList from "../views/VetPartyList.vue";
import ModifyUser from "../views/ModifyUser.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Main",
    component: Main,
  },
  {
    path: "/signup",
    name: "Signup",
    component: Signup,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "*",
    name: "NotFound",
    component: NotFound,
  },
  {
    path: "/article_create",
    name: "ArticleCreate",
    component: ArticleCreate,
  },
  {
    path: "/article_detail/:postId",
    name: "ArticleDetail",
    component: ArticleDetail,
  },
  {
    path: "/challenge_detail",
    name: "ChallengeDetail",
    component: ChallengeDetail,
  },
  {
    path: "/challenge_list",
    name: "ChallengeList",
    component: ChallengeList,
  },
  {
    path: "/find_email",
    name: "FindEmail",
    component: FindEmail,
  },
  {
    path: "/find_password",
    name: "FindPassword",
    component: FindPassword,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/recipe_create",
    name: "RecipeCreate",
    component: RecipeCreate,
  },
  {
    path: "/recipe_detail",
    name: "RecipeDetail",
    component: RecipeDetail,
  },
  {
    path: "/recipe_search",
    name: "RecipeSearch",
    component: RecipeSearch,
  },
  {
    path: "/vetparty_create",
    name: "VetPartyCreate",
    component: VetPartyCreate,
  },
  {
    path: "/vetparty_list",
    name: "VetPartyList",
    component: VetPartyList,
  },
  {
    path: "/modify_user",
    name: "ModifyUser",
    component: ModifyUser,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
