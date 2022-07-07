package com.sh.prolearn.app.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import com.sh.prolearn.R
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.utils.Consts.FIELD_EMAIL
import com.sh.prolearn.core.utils.Consts.FIELD_REQUIRED
import com.sh.prolearn.core.utils.Consts.LOGIN_ERR_ID
import com.sh.prolearn.core.utils.Consts.LOGIN_ERR_PASS
import com.sh.prolearn.core.utils.DialogUtils.setCustomDialog
import com.sh.prolearn.core.utils.DialogUtils.showCustomDialog
import com.sh.prolearn.core.utils.ToastUtils.showToast
import com.sh.prolearn.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.loginBtn.setOnClickListener(this)
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
            R.id.login_btn -> {
                processLogin()
            }
        }
    }

    private fun processLogin() {
        with(binding) {
            when {
                loginEmailEdit.text.toString().isEmpty() -> {
                    loginNpmLayout.error = FIELD_REQUIRED
                }
//                Patterns.EMAIL_ADDRESS.matcher(loginEmailEdit.text.toString()).matches() -> {
//                    loginNpmLayout.error = FIELD_EMAIL
//                }
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
                                    setCustomDialog(this@LoginActivity, R.layout.wait_dialog, false)
                                    showCustomDialog(true)
                                }
                                is Resource.Success<*> -> {
                                    livedata.removeObservers(this@LoginActivity)
                                    showToast(data.message.toString(), this@LoginActivity)
                                    showCustomDialog(false)
                                }
                                is Resource.Error<*> -> {
                                    showCustomDialog(false)
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