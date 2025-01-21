package cc.lixiaoyu.wanandroid.core.account.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.account.LoginCallback
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity.Companion.SIGN_IN_FRAGMENT_TAG
import cc.lixiaoyu.wanandroid.databinding.FragmentSignUpBinding
import cc.lixiaoyu.wanandroid.entity.User
import cc.lixiaoyu.wanandroid.util.ToastUtil

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            val userName = binding.etUsername.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }
            val passwordConfirm = binding.etPasswordConfirm.text.toString().trim { it <= ' ' }
            AccountManager.signUp(userName, password, passwordConfirm, object : LoginCallback {
                override fun onSuccess(user: User) {
                    ToastUtil.showToast(getString(R.string.signup_success))
                    activity?.finish()
                }

                override fun onFail(t: Throwable) {
                    ToastUtil.showToast(getString(R.string.signup_failed))
                }

            })
        }
        binding.tvToSignIn.setOnClickListener {
            (activity as? LoginActivity)?.switchFragment(SIGN_IN_FRAGMENT_TAG)
        }
    }

    companion object {
        fun newInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }
}