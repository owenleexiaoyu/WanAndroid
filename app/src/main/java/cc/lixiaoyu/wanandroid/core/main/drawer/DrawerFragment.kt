package cc.lixiaoyu.wanandroid.core.main.drawer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.about.AboutActivity
import cc.lixiaoyu.wanandroid.core.collection.CollectionActivity
import cc.lixiaoyu.wanandroid.core.login.LoginActivity
import cc.lixiaoyu.wanandroid.core.login.event.LoginEvent
import cc.lixiaoyu.wanandroid.core.todo.TodoActivity
import cc.lixiaoyu.wanandroid.databinding.MainDrawerContainerBinding
import cc.lixiaoyu.wanandroid.util.RxBus
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import cc.lixiaoyu.wanandroid.util.storage.DataManager

class DrawerFragment: Fragment() {

    private lateinit var binding: MainDrawerContainerBinding

    private var isLogin = true

    private val dataManager by lazy { DataManager() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainDrawerContainerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llCollections.setOnClickListener {
            if (isLogin) {
                //登录后进入收藏界面
                startActivity(Intent(activity, CollectionActivity::class.java))
            } else {
                //未登录则进入登录界面
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        binding.llTodos.setOnClickListener {
            if (isLogin) {
                //登录后进入TODO界面
                startActivity(Intent(activity, TodoActivity::class.java))
            } else {
                //未登录则进入登录界面
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        binding.llTheme.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        binding.llAboutUs.setOnClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
        }
        if (isLogin) {
            binding.llLogout.visibility = View.VISIBLE
            binding.llLogout.setOnClickListener {
                logout()
            }
        } else {
            binding.llLogout.visibility = View.GONE
        }

    }

    private fun logout() {
        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.setTitle(getString(R.string.logout_confirm_title))
            .setMessage(getString(R.string.logout_confirm_body))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.logout_confirm_confirm)) { _, _ -> realLogout() }
            .setNegativeButton(getString(R.string.logout_confirm_cancel)) { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }

    private fun realLogout() {
        val service = RetrofitManager.getInstance().wanAndroidService
        service.logout()
        dataManager.loginAccount = ""
        dataManager.loginPassword = ""
        dataManager.loginState = false
        RxBus.getInstance().post(LoginEvent(false))
        ToastUtil.showToast(getString(R.string.toast_logout_success))
    }
}