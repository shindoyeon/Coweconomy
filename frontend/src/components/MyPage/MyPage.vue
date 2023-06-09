<template>
  <v-sheet color="transparent" height="100%">
    <!-- 로그인 상태 -->
    <div v-if="isLoggedIn">
      <MyPageLoading v-if="loading"></MyPageLoading>
      <div v-else>
        <!-- level profile -->
        <MyPageProfile :user="user" class="justify-center"></MyPageProfile>
        <!-- username & logout btn -->
        <v-sheet class="pa-6" rounded="t-xl" elevation="5">
          <!-- hello & logout -->
          <v-sheet class="pa-1 d-flex flex-row justify-space-between pb-5">
            <div class="xl-font">
              <div>안녕하세요</div>
              <div class="d-flex align-center">
                <span class="b-font">{{ user.userNickname }}</span
                >님
                <img
                  height="25"
                  class="pl-1"
                  src="@/assets/images/emoji/waving_hand.png"
                />
              </div>
            </div>
            <MyPageLogoutBtn></MyPageLogoutBtn>
          </v-sheet>
          <!-- info & memo -->
          <div>
            <v-row class="px-2 mt-1 mb-3">
              <v-col class="pa-1"
                ><v-btn
                  class="swiper-menu my-info"
                  slot="button-prev"
                  elevation="0"
                  block
                  >나의 정보</v-btn
                ></v-col
              ><v-col class="pa-1">
                <v-btn
                  class="swiper-menu my-memo"
                  slot="button-next"
                  elevation="0"
                  block
                  >나의 메모</v-btn
                >
              </v-col>
            </v-row>

            <swiper class="swiper" :options="swiperOptionMain" ref="swiperMain">
              <swiper-slide><MyPageInfo></MyPageInfo></swiper-slide>
              <swiper-slide
                ><MyPageMemo :memoDtoList="memoDtoList"></MyPageMemo
              ></swiper-slide>
            </swiper>
          </div>
        </v-sheet>
      </div>
    </div>
    <!-- 로그아웃 상태 -->
    <v-sheet color="transparent" height="100%" v-else>
      <kakaoLogin></kakaoLogin>
    </v-sheet>
  </v-sheet>
</template>

<script>
import MyPageProfile from "./MyPageProfile.vue";
import MyPageLogoutBtn from "./MyPageLogoutBtn.vue";
import MyPageInfo from "./MyPageInfo/MyPageInfo.vue";
import MyPageMemo from "./MyPageMemo/MyPageMemo.vue";
import MyPageLoading from "./MyPageLoading.vue";
import KakaoLogin from "./KakaoLogin.vue";

import { mapGetters, mapActions } from "vuex";
import { getUserInfo } from "@/api/modules/mypage.js";

export default {
  name: "MyPage",
  data() {
    return {
      kakaoCode: null,
      swiperOptionMain: {
        spaceBetween: 10,
        navigation: {
          nextEl: ".my-memo",
          prevEl: ".my-info",
        },
      },
      selectedBtn: "my-memo",
      memoDtoList: [],
      user: {},
      loading: false,
    };
  },
  async created() {
    // Kakao 인가 코드 추출
    this.kakaoCode = this.$route.query.code;
    if (this.kakaoCode != null) {
      this.kakao();
    }
    // this.loaded = true;

    if (this.isLoggedIn) {
      this.loading = true;

      // [@Method] user info 가져오기
      await getUserInfo().then(async (res) => {
        // [@Method] 가져온 user info 중 graph 관련 data 저장
        await this.setUserGraphData(res);

        this.memoDtoList = await res.memoDtoList;
        this.user = await res.user;
        this.loading = false;
      });
    }
  },
  components: {
    MyPageProfile,
    MyPageLogoutBtn,
    MyPageMemo,
    MyPageInfo,
    MyPageLoading,
    KakaoLogin,
  },
  computed: {
    ...mapGetters("userStore", ["isLoggedIn"]),
  },
  methods: {
    ...mapActions("userStore", ["executeToken", "setUserGraphData"]),
    // 받은 인가 코드를 사용하여 Kakao Token 발급 요청
    async kakao() {
      await this.executeToken();
      // 받은 인가 코드 초기화
      this.kakaoCode = null;
    },
  },
};
</script>

<style></style>
