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
import RecipeUpdate from "../views/RecipeUpdate.vue";
import RecipeSearch from "../views/RecipeSearch.vue";
import VetPartyCreate from "../views/VetPartyCreate.vue";
import VetPartyList from "../views/VetPartyList.vue";
import ModifyUser from "../views/ModifyUser.vue";
import ModifyPic from "../views/ModifyPic.vue";
import OtherProfile from "../views/OtherProfile.vue";
import NewsFeedPractice from "../views/NewsFeedPractice.vue";
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
    path: "/404",
    name: "NotFound",
    component: NotFound,
  },
  {
    path: "*",
    redirect: "/404",
  },
  {
    path: "/article_create/:type",
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
    path: "/profile/:userid",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/modify_pic/:userid",
    name: "ModifyPic",
    component: ModifyPic,
  },
  {
    path: "/recipe_create",
    name: "RecipeCreate",
    component: RecipeCreate,
  },
  {
    path: "/recipe_detail/:recipeId",
    name: "RecipeDetail",
    component: RecipeDetail,
  },
  {
    path: "/recipe_update",
    name: "RecipeUpdate",
    component: RecipeUpdate,
  },
  {
    path: "/recipe_search",
    name: "RecipeSearch",
    component: RecipeSearch,
  },
  {
    path: "/vetparty_create/:type",
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
  {
    path: "/other_profile/:userid",
    name: "OtherProfile",
    component: OtherProfile,
  },
  {
    path: "/feed",
    name: "NewsFeedPractice",
    component: NewsFeedPractice,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

import store from "@/store";
// 로그인한 사람만 갈 수 있는 페이지와 아닌 사람만 갈 수 있는 페이지 구분
router.beforeEach((to, from, next) => {
  // 로그인 안 해야 갈 수 있는 페이지
  const outerPages = ["Signup", "Login", "FindEmail", "FindPassword", "NewsFeedPractice"];
  // 로그인 해야 갈 수 있는 페이지
  const privatePages = [
    "NotFound",
    "ArticleCreate",
    "ArticleDetail",
    "ChallengeDetail",
    "ChallengeList",
    "Profile",
    "RecipeCreate",
    "RecipeDetail",
    "RecipeUpdate",
    "RecipeSearch",
    "VetPartyCreate",
    "VetPartyList",
    "ModifyUser",
    "ModifyPic",
    "OtherProfile",
    "NewsFeedPractice",
  ];

  const authRequired = privatePages.includes(to.name);
  const guestRequired = outerPages.includes(to.name);
  const isLoggedIn = store.getters["user/isLogin"];

  // 존재하지 않는 페이지로 이동하려 한다면
  if (!to.name) {
    next({ name: "NotFound" });
  }

  // 로그인한 사용자가 로그인하면 못 가는 페이지로 이동하려 할 때
  if (isLoggedIn && guestRequired) {
    next({ name: "Main" });
  }
  // 로그인 안 한 사용자가 로그인해야 갈 수 있는 페이지로 이동하려 할 때
  if (!isLoggedIn && authRequired) {
    next({ name: "Login" });
  } else {
    next();
  }
});

export default router;
