import Vue from 'vue'
import { USER_INFO, ENHANCE_PRE } from "@/store/mutation-types"
const getters = {
  device: state => state.app.device,
  theme: state => state.app.theme,
  color: state => state.app.color,
  token: state => state.user.token,
  avatar: state => {state.user.avatar = Vue.ls.get(USER_INFO).avatar; return state.user.avatar},
  username: state => state.user.username,
  nickname: state => {state.user.realname = Vue.ls.get(USER_INFO).realname; return state.user.realname},
  welcome: state => state.user.welcome,
  permissionList: state => state.user.permissionList,
  userInfo: state => {state.user.info = Vue.ls.get(USER_INFO); return state.user.info},
  addRouters: state => state.permission.addRouters,
  onlAuthFields: state => {return state.online.authFields },
  enhanceJs:(state) => (code) => {
    state.enhance.enhanceJs[code] = Vue.ls.get(ENHANCE_PRE+code);
    return state.enhance.enhanceJs[code]
  },
  sysSafeMode: state => state.user.sysSafeMode,
  // 1普通用回顾 2管理员
  userIdentity: (state) => {state.user.userIdentity = Vue.ls.get(USER_INFO).userIdentity;return state.user.userIdentity},
  // 法律素养学时
  law: (state) => {state.user.law = Vue.ls.get(USER_INFO).law;return state.user.law},
  // 身心素质学时
  bodyMind: (state) => {state.user.bodyMind = Vue.ls.get(USER_INFO).bodyMind;return state.user.bodyMind},
  // 创新创业素质学时
  innovation: (state) => {state.user.innovation = Vue.ls.get(USER_INFO).innovation;return state.user.innovation},
  // 思想品德素质学时
  thought: (state) => {state.user.thought = Vue.ls.get(USER_INFO).thought;return state.user.thought},
  // 文体素质学时
  cultureSports: (state) => {state.user.cultureSports = Vue.ls.get(USER_INFO).cultureSports;return state.user.cultureSports},

}

export default getters
