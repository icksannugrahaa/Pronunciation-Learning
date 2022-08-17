package com.sh.prolearn.app.authentication

import android.content.Intent
import android.os.Build.*
import android.os.Build.VERSION.RELEASE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import com.sh.prolearn.R
import com.sh.prolearn.app.modules.ModuleActivity
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.utils.Consts.FIELD_EMAIL_NOT_VALID
import com.sh.prolearn.core.utils.Consts.FIELD_REQUIRED
import com.sh.prolearn.core.utils.Consts.LOGIN_ERR_ID
import com.sh.prolearn.core.utils.Consts.LOGIN_ERR_PASS
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.GeneralUtils.getIPAddress
import com.sh.prolearn.core.utils.GeneralUtils.getTodayDateString
import com.sh.prolearn.core.utils.ToastUtils.showToast
import com.sh.prolearn.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthenticationViewModel by viewModel()
    private lateinit var myDialog: DialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        myDialog = DialogUtils()

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        if (savedInstanceState == null) {
            supportActionBar?.title = ""
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                processLogin()
            }
            R.id.btn_register -> {
                Intent(this, RegisterActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun processLogin() {
        with(binding) {
            when {
                loginEmailEdit.text.toString().isEmpty() -> {
                    loginNpmLayout.error = FIELD_REQUIRED
                }
                !Patterns.EMAIL_ADDRESS.matcher(loginEmailEdit.text.toString()).matches() -> {
                    loginNpmLayout.error = FIELD_EMAIL_NOT_VALID
                }
                loginPasswordEdit.text.toString().isEmpty() -> {
                    loginPasswordLayout.error = FIELD_REQUIRED
                }
                else -> {
                    loginNpmLayout.error = ""
                    loginPasswordLayout.error = ""

                    val livedata = viewModel.authLogin(
                        loginEmailEdit.text.toString(),
                        loginPasswordEdit.text.toString()
                    )
                    livedata.observe(this@LoginActivity) { data ->
                        if (data != null) {
                            when (data) {
                                is Resource.Loading<*> -> {
                                    myDialog.setCustomDialog(this@LoginActivity, R.layout.wait_dialog, false)
                                    myDialog.showCustomDialog(true)
                                }
                                is Resource.Success<*> -> {
                                    livedata.removeObservers(this@LoginActivity)
                                    AuthPreferences(this@LoginActivity).saveAuthData(data.data)
                                    AuthPreferences(this@LoginActivity).saveAuthDetail(getIPAddress(), getTodayDateString(), DEVICE, RELEASE, MODEL, PRODUCT)
                                    showToast(data.message.toString(), this@LoginActivity)
                                    myDialog.showCustomDialog(false)
                                    finish()
                                }
                                is Resource.Error<*> -> {
                                    myDialog.showCustomDialog(false)
                                    when(data.message) {
                                        LOGIN_ERR_PASS -> {
                                            loginPasswordLayout.error = data.message
                                        }
                                        LOGIN_ERR_ID -> {
                                            loginNpmLayout.error = data.message
                                        }
                                    }
                                    showToast(data.message.toString(), this@LoginActivity)
                                    livedata.removeObservers(this@LoginActivity)
                                }
                                else -> {
                                    myDialog.showCustomDialog(false)
                                    when(data.message) {
                                        LOGIN_ERR_PASS -> {
                                            loginPasswordLayout.error = data.message
                                        }
                                        LOGIN_ERR_ID -> {
                                            loginNpmLayout.error = data.message
                                        }
                                    }
                                    showToast(data.message.toString(), this@LoginActivity)
                                    livedata.removeObservers(this@LoginActivity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}