
import { useCookies } from '@vueuse/integrations/useCookies'
const TokenKey = "admin-token"
const cookie = useCookies()

// 获取token
export function getCookies(){
  return cookie.get(TokenKey)
}

// 设置token
export function setCookies(token){
  return cookie.set(TokenKey,token)
}

// 清除token
export function removeCookies(){
  return cookie.remove(TokenKey)
}
