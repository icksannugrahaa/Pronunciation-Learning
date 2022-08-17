package com.sh.prolearn.app.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.sh.prolearn.R
import com.sh.prolearn.app.about.AboutFragment
import com.sh.prolearn.app.authentication.AuthenticationViewModel
import com.sh.prolearn.app.home.HomeFragment
import com.sh.prolearn.app.progress.AchievementFragment
import com.sh.prolearn.app.progress.LearnHistoryFragment
import com.sh.prolearn.app.user.ChangePasswordFragment
import com.sh.prolearn.app.user.ProfileFragment
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.GeneralUtils
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromLocal
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeFragment.AuthenticationCallback, ProfileFragment.AuthenticationCallback  {
    companion object {
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuthenticationViewModel by viewModel()
    private lateinit var myDialog: DialogUtils
    private var currentTab = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment())
                .commit()
            supportActionBar?.title = getString(R.string.app_name)
        }
        myDialog = DialogUtils()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var title = getString(R.string.app_name)
        when (item.itemId) {
            R.id.nav_drawer_home -> {
                currentTab = "home"
                fragment = HomeFragment()
                title = getString(R.string.app_name)
            }
            R.id.nav_drawer_profile -> {
                currentTab = "profile"
                fragment = ProfileFragment()
                title = getString(R.string.edit_profile)
            }
            R.id.nav_drawer_change_password -> {
                currentTab = "password"
                fragment = ChangePasswordFragment()
                title = getString(R.string.change_password)
            }
            R.id.nav_drawer_about -> {
                currentTab = "about"
                fragment = AboutFragment()
                title = getString(R.string.about)
            }
            R.id.nav_drawer_histories -> {
                currentTab = "histories"
                fragment = LearnHistoryFragment()
                title = getString(R.string.learning_histories)
            }
            R.id.nav_drawer_achievement -> {
                currentTab = "achievement"
                fragment = AchievementFragment()
                title = getString(R.string.achievement)
            }
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
        }
        supportActionBar?.title = title
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        if(currentTab != "profile") {
            checkAuthentication()
        }
    }

    override fun authCallback(account: Account?) {
        AuthPreferences(this).saveAuthData(account)
        checkAuthentication()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putInt(SAVED_STATE_CURRENT_TAB_KEY, currentSelectItemId)
    }

    private fun changeView() {
        val data = AuthPreferences(this@MainActivity).authData
        val menu = binding.navView.menu
        val header = binding.navView.getHeaderView(0)

        if (data?.token != null) {
            header.isVisible = true
            header.findViewById<ProgressBar>(R.id.nav_drawer_pb_level).progress = ((data.exp!!.toDouble() / data.expNext!!.toDouble())*100).toInt()
            header.findViewById<TextView>(R.id.nav_drawer_tv_username).text = data.name
            header.findViewById<TextView>(R.id.nav_drawer_tv_user_level).text = data.levelName
            header.findViewById<TextView>(R.id.nav_drawer_tv_level).text = "Level ${data.level}"
            header.findViewById<TextView>(R.id.nav_drawer_tv_level_progress).text = "${data.exp} / ${data.expNext}"
            if(data.avatar != null) {
                header.findViewById<ImageView>(R.id.nav_drawer_img_avatar).loadImageFromServer(data.avatar)
            } else {
                header.findViewById<ImageView>(R.id.nav_drawer_img_avatar).loadImageFromLocal(R.drawable.user_default)
            }
            menu.findItem(R.id.nav_drawer_profile).isVisible = true
            menu.findItem(R.id.nav_drawer_histories).isVisible = true
            menu.findItem(R.id.nav_drawer_change_password).isVisible = true
            menu.findItem(R.id.nav_drawer_achievement).isVisible = true


        } else {
            header.isVisible = false
            menu.findItem(R.id.nav_drawer_profile).isVisible = false
            menu.findItem(R.id.nav_drawer_histories).isVisible = false
            menu.findItem(R.id.nav_drawer_change_password).isVisible = false
            menu.findItem(R.id.nav_drawer_achievement).isVisible = false
        }
    }

    private fun checkAuthentication() {
        val token = AuthPreferences(this@MainActivity).authToken
        val livedata = viewModel.authMe(token)
        myDialog = DialogUtils()
        myDialog.setCustomDialog(this@MainActivity, R.layout.wait_dialog, false)
        livedata.observe(this) { data ->
            if (data != null) {
                when (data) {
                    is Resource.Loading<*> -> {
                        myDialog.showCustomDialog(true)
                    }
                    is Resource.Success<*> -> {
                        AuthPreferences(this@MainActivity).saveAuthData(data.data)
                        AuthPreferences(this@MainActivity).saveAuthDetail(
                            GeneralUtils.getIPAddress(),
                            GeneralUtils.getTodayDateString(),
                            Build.DEVICE,
                            Build.VERSION.RELEASE,
                            Build.MODEL,
                            Build.PRODUCT
                        )
                        changeView()
                        myDialog.showCustomDialog(false)
                        livedata.removeObservers(this)
                    }
                    is Resource.Error<*> -> {
                        val message = data.message.toString()
                        if (message.contains("401", ignoreCase = true)) {
                            AuthPreferences(this@MainActivity).saveAuthData(
                                null
                            )
                        }
                        myDialog.showCustomDialog(false)
                        changeView()
                        livedata.removeObservers(this)
                    }
                }
            }
        }
    }
}