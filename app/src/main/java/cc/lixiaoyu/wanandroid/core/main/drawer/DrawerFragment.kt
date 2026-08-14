package cc.lixiaoyu.wanandroid.core.main.drawer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.about.AboutActivity
import cc.lixiaoyu.wanandroid.core.about.KMPActivity
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.account.LogoutCallback
import cc.lixiaoyu.wanandroid.core.collection.CollectionActivity
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity
import cc.lixiaoyu.wanandroid.core.todo.ui.TodoActivity
import cc.lixiaoyu.wanandroid.databinding.MainDrawerContainerBinding
import cc.lixiaoyu.wanandroid.util.ToastUtil

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
        initLoginItem()
        initCollectionItem()
        initTodoItem()
        initAboutItem()
        initLogoutItem()
        initKmpItem()
    }

    private fun initLoginItem() {
        binding.tvUsername.setOnClickListener {
            if (!AccountManager.isLogin()) {
                //未登录则进入登录界面
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        AccountManager.isLoginLiveData.observe(viewLifecycleOwner) { login: Boolean ->
            binding.tvUsername.text = if (login) AccountManager.getCurUser()?.username.orEmpty() else getString(R.string.login)
        }
    }

    private fun initCollectionItem() {
        binding.itemCollection.setOnClickListener {
            //登录后进入收藏界面
            startActivity(Intent(activity, CollectionActivity::class.java))
        }
        AccountManager.isLoginLiveData.observe(viewLifecycleOwner) { login: Boolean ->
            binding.itemCollection.visibility = if (login) View.VISIBLE else View.GONE
        }
    }

    private fun initTodoItem() {
        binding.itemTodos.setOnClickListener {
            //登录后进入TODO界面
            startActivity(Intent(activity, TodoActivity::class.java))
        }
        AccountManager.isLoginLiveData.observe(viewLifecycleOwner) { login: Boolean ->
            binding.itemTodos.visibility = if (login) View.VISIBLE else View.GONE
        }
    }

    private fun initAboutItem() {
        binding.itemAboutUs.setOnClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
        }
    }

    private fun initLogoutItem() {
        binding.itemLogout.setOnClickListener {
            logout()
        }
        AccountManager.isLoginLiveData.observe(viewLifecycleOwner) { login: Boolean ->
            binding.itemLogout.visibility = if (login) View.VISIBLE else View.GONE
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

    private fun initKmpItem() {
        binding.itemKmp.setTitleText("KMP & Compose Multiplatform")
        binding.itemKmp.setOnClickListener {
            startActivity(Intent(activity, KMPActivity::class.java))
        }
    }
}
