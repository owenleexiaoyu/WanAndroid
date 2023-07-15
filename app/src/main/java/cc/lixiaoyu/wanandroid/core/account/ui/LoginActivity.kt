package cc.lixiaoyu.wanandroid.core.account.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cc.lixiaoyu.wanandroid.R

class LoginActivity : AppCompatActivity() {

    private var signInFragment: Fragment? = null
    private var signUpFragment: Fragment? = null

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        initFragment(savedInstanceState)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            signInFragment = supportFragmentManager.findFragmentByTag(SIGN_IN_FRAGMENT_TAG)
            signUpFragment = supportFragmentManager.findFragmentByTag(SIGN_UP_FRAGMENT_TAG)
        }
        if (signInFragment == null) {
            signInFragment = SignInFragment.newInstance()
        }
        if (signUpFragment == null) {
            signUpFragment = SignUpFragment.newInstance()
        }
        val transaction = supportFragmentManager.beginTransaction()
        if (!signInFragment!!.isAdded) {
            transaction.add(R.id.login_container, signInFragment!!, SIGN_IN_FRAGMENT_TAG)
        }
        if (!signUpFragment!!.isAdded) {
            transaction.add(R.id.login_container, signUpFragment!!, SIGN_UP_FRAGMENT_TAG)
        }
        val currentFragment = if (viewModel.currentFragmentTag == SIGN_IN_FRAGMENT_TAG) signInFragment!! else signUpFragment!!
        val hiddenFragment = if (viewModel.currentFragmentTag == SIGN_IN_FRAGMENT_TAG) signUpFragment!! else signInFragment!!
        transaction
            .hide(hiddenFragment)
            .show(currentFragment)
            .commitAllowingStateLoss()
    }

    /**
     * 在Activity中控制Fragment的切换
     * @param toTag     目标fragment的tag
     */
    fun switchFragment(toTag: String) {
        if (toTag == viewModel.currentFragmentTag) {
            return
        }
        val toFragment = supportFragmentManager.findFragmentByTag(toTag) ?: return
        val currentFragment = if (viewModel.currentFragmentTag == SIGN_IN_FRAGMENT_TAG) signInFragment else signUpFragment ?: return
        supportFragmentManager.beginTransaction()
            .hide(currentFragment!!)
            .show(toFragment)
            .commitAllowingStateLoss()
        viewModel.currentFragmentTag = toTag
    }

    companion object {
        const val SIGN_IN_FRAGMENT_TAG = "sign_in_fragment"
        const val SIGN_UP_FRAGMENT_TAG = "sign_up_fragment"
    }
}