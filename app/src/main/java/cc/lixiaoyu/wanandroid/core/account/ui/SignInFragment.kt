package cc.lixiaoyu.wanandroid.core.account.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.account.LoginCallback
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity.Companion.SIGN_UP_FRAGMENT_TAG
import cc.lixiaoyu.wanandroid.databinding.FragmentSignInBinding
import cc.lixiaoyu.wanandroid.entity.User
import cc.lixiaoyu.wanandroid.util.ToastUtil

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val userName = binding.etUsername.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }
            AccountManager.signIn(userName, password, object : LoginCallback {
                override fun onSuccess(user: User) {
                    ToastUtil.showToast(getString(R.string.login_success))
                    activity?.finish()
                }

                override fun onFail(t: Throwable) {
                    ToastUtil.showToast(getString(R.string.login_failed))
                }
            })
        }
        binding.tvToSignUp.setOnClickListener {
            (activity as? LoginActivity)?.switchFragment(SIGN_UP_FRAGMENT_TAG)
        }
    }

    companion object {
        fun newInstance(): SignInFragment {
            return SignInFragment()
        }
    }
}