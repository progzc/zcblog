// 根级别的mutations
import { RESET_STORE } from 'store/constant/mutation-types'
import cloneDeep from 'lodash/cloneDeep'

export default {
  [RESET_STORE] (state) {
    Object.keys(state).forEach(key => {
      state[key] = cloneDeep(window.SITE_CONFIG.storeState[key])
    })
  }
}
