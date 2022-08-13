package com.sh.prolearn.app.authentication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.sh.prolearn.R
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.FIELD_PASSOWRD_MUST_SAME
import com.sh.prolearn.core.utils.Consts.FIELD_REQUIRED
import com.sh.prolearn.core.utils.Consts.LOGIN_ERR_ID
import com.sh.prolearn.core.utils.Consts.LOGIN_ERR_PASS
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.GeneralUtils
import com.sh.prolearn.core.utils.ToastUtils
import com.sh.prolearn.databinding.ActivityLoginBinding
import com.sh.prolearn.databinding.ActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthenticationViewModel by viewModel()
    private lateinit var myDialog: DialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        myDialog = DialogUtils()

        if (savedInstanceState == null) {
            supportActionBar?.title = ""
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.registerBtn.setOnClickListener {
            with(binding) {
                when {
                    registerEmailEdit.text.toString().isEmpty() -> {
                        registerEmailLayout.error = FIELD_REQUIRED
                    }
//                Patterns.EMAIL_ADDRESS.matcher(loginEmailEdit.text.toString()).matches() -> {
//                    loginNpmLayout.error = FIELD_EMAIL
//                }
                    registerNameEdit.text.toString().isEmpty() -> {
                        registerNameLayout.error = FIELD_REQUIRED
                    }
                    registerPasswordEdit.text.toString().isEmpty() -> {
                        registerPasswordLayout.error = FIELD_REQUIRED
                    }
                    registerCpasswordEdit.text.toString().isEmpty() -> {
                        registerCpasswordLayout.error = FIELD_REQUIRED
                    }
                    registerCpasswordEdit.text.toString() != registerPasswordEdit.text.toString() -> {
                        registerCpasswordLayout.error = FIELD_PASSOWRD_MUST_SAME
                    }
                    else -> {
                        registerEmailLayout.error = ""
                        registerNameLayout.error = ""
                        registerPasswordLayout.error = ""
                        registerCpasswordLayout.error = ""

                        val livedata = viewModel.authRegister(
                            registerEmailEdit.text.toString(),
                            registerNameEdit.text.toString(),
                            registerPasswordEdit.text.toString(),
                            registerCpasswordEdit.text.toString(),
                        )
                        livedata.observe(this@RegisterActivity) { data ->
                            if (data != null) {
                                when (data) {
                                    is Resource.Loading<*> -> {
                                        myDialog.setCustomDialog(
                                            this@RegisterActivity,
                                            R.layout.wait_dialog,
                                            false
                                        )
                                        myDialog.showCustomDialog(true)
                                    }
                                    is Resource.Success<*> -> {
                                        livedata.removeObservers(this@RegisterActivity)
                                        ToastUtils.showToast(
                                            data.message.toString(),
                                            this@RegisterActivity
                                        )
                                        myDialog.showCustomDialog(false)
                                        finish()
                                    }
                                    is Resource.Error<*> -> {
                                        myDialog.showCustomDialog(false)
                                        ToastUtils.showToast(
                                            data.message.toString(),
                                            this@RegisterActivity
                                        )
                                        livedata.removeObservers(this@RegisterActivity)
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
}