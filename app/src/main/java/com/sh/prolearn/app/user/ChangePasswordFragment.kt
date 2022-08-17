package com.sh.prolearn.app.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sh.prolearn.R
import com.sh.prolearn.app.authentication.AuthenticationViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.FIELD_REQUIRED
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.ToastUtils
import com.sh.prolearn.databinding.FragmentChangePasswordBinding
import com.sh.prolearn.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by viewModel()
    private lateinit var myDialog: DialogUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            with(binding) {
                tietCurrentPassword.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        textLayoutCurrentPassword.error = null
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (s.toString() == "") {
                            textLayoutCurrentPassword.error = FIELD_REQUIRED
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString() == "") {
                            textLayoutCurrentPassword.error = FIELD_REQUIRED
                        }
                    }
                })
                tietNewPassword.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        textLayoutNewPassword.error = null
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (s.toString() == "") {
                            textLayoutNewPassword.error = FIELD_REQUIRED
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString() == "") {
                            textLayoutNewPassword.error = FIELD_REQUIRED
                        }
                    }
                })
                tietConfirmPassword.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        textLayoutConfirmPassword.error = null
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (s.toString() == "") {
                            textLayoutConfirmPassword.error = FIELD_REQUIRED
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString() == "") {
                            textLayoutConfirmPassword.error = FIELD_REQUIRED
                        }
                    }
                })
                btnEditPassword.setOnClickListener {
                    changePassword()
                }
            }
        }
    }

    private fun changePassword() {
        val token = AuthPreferences(requireContext()).authToken
        with(binding) {
            var error = 0
            if(tietCurrentPassword.text.toString() == "") {
                error = 1
                textLayoutCurrentPassword.error = FIELD_REQUIRED
            }
            if(tietNewPassword.text.toString() == "") {
                error = 1
                textLayoutNewPassword.error = FIELD_REQUIRED
            }
            if(tietConfirmPassword.text.toString() == "") {
                error = 1
                textLayoutConfirmPassword.error = FIELD_REQUIRED
            }
            if (error != 1) {
                val livedata = viewModel.changePassword(token, tietCurrentPassword.text.toString(), tietNewPassword.text.toString(), tietConfirmPassword.text.toString())
                myDialog = DialogUtils()
                myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
                livedata.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        when (data) {
                            is Resource.Loading<*> -> {
                                myDialog.showCustomDialog(true)
                            }
                            is Resource.Success<*> -> {
                                ToastUtils.showToast(data.message!!, requireContext())
                                myDialog.showCustomDialog(false)
                                livedata.removeObservers(viewLifecycleOwner)
                            }
                            is Resource.Error<*> -> {
                                val message = data.message.toString()
                                if (message.contains("401", ignoreCase = true)) {
                                    AuthPreferences(requireContext()).saveAuthData(
                                        null
                                    )
                                }
                                ToastUtils.showToast(message, requireContext())
                                myDialog.showCustomDialog(false)
                                livedata.removeObservers(viewLifecycleOwner)
                            }
                        }
                    }
                }
            } else {
                ToastUtils.showToast(getString(R.string.please_fix_all_error), requireContext())
            }
        }
    }
}