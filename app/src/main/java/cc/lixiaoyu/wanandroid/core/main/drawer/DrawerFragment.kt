package cc.lixiaoyu.wanandroid.core.main.drawer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.about.AboutActivity
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.account.LogoutCallback
import cc.lixiaoyu.wanandroid.core.collection.CollectionActivity
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity
import cc.lixiaoyu.wanandroid.core.theme.ThemeManager
import cc.lixiaoyu.wanandroid.core.todo.ui.TodoActivity
import cc.lixiaoyu.wanandroid.databinding.MainDrawerContainerBinding
import cc.lixiaoyu.wanandroid.entity.User
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.storage.DataManager

class DrawerFragment: Fragment() {

    private lateinit var binding: MainDrawerContainerBinding

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
        if (!AccountManager.isLogin()) {
            binding.tvUsername.setOnClickListener {
                //未登录则进入登录界面
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        binding.itemCollection.setOnClickListener {
            if (AccountManager.isLogin()) {
                //登录后进入收藏界面
                startActivity(Intent(activity, CollectionActivity::class.java))
            } else {
                //未登录则进入登录界面
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        binding.itemTodos.setOnClickListener {
            if (AccountManager.isLogin()) {
                //登录后进入TODO界面
                startActivity(Intent(activity, TodoActivity::class.java))
            } else {
                //未登录则进入登录界面
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        binding.itemTheme.apply {
            if (ThemeManager.isDarkMode.value) {
                setStartIcon(ContextCompat.getDrawable(context, R.drawable.ic_light_mode))
                setTitleText(getString(R.string.light_mode))
            } else {
                setStartIcon(ContextCompat.getDrawable(context, R.drawable.ic_dark_mode))
                setTitleText(getString(R.string.dark_mode))
            }
            setOnClickListener {
                if (ThemeManager.isDarkMode.value) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    ThemeManager.setDarkMode(false)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    ThemeManager.setDarkMode(true)
                }
            }
        }
        binding.itemAboutUs.setOnClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
        }
        if (AccountManager.isLogin()) {
            binding.itemLogout.setOnClickListener {
                logout()
            }
        }
        // 监听 user 信息的变化
        AccountManager.userLiveData.observe(viewLifecycleOwner) { user: User? ->
            if (user == null) {
                binding.tvUsername.text = getString(R.string.login)
                binding.itemLogout.visibility = View.GONE
            } else {
                binding.tvUsername.text = user.username ?: ""
                binding.itemLogout.visibility = View.VISIBLE
            }
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
        AccountManager.logout(object: LogoutCallback {
            override fun onSuccess() {
                ToastUtil.showToast(getString(R.string.toast_logout_success))
            }

            override fun onFail(t: Throwable) {
                ToastUtil.showToast(getString(R.string.logout_failed))
            }
        })
    }
}