package cc.lixiaoyu.wanandroid.core.account.ui

import androidx.lifecycle.ViewModel
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity.Companion.SIGN_IN_FRAGMENT_TAG

class LoginViewModel: ViewModel() {
    var currentFragmentTag: String = SIGN_IN_FRAGMENT_TAG
}