package com.sh.prolearn.app.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sh.prolearn.R
import com.sh.prolearn.app.authentication.AuthenticationViewModel
import com.sh.prolearn.app.authentication.LoginActivity
import com.sh.prolearn.app.modules.ModuleActivity
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.GeneralUtils
import com.sh.prolearn.core.utils.ToastUtils
import com.sh.prolearn.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by viewModel()
    private var mCallback: AuthenticationCallback? = null
    private lateinit var myDialog: DialogUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            myDialog = DialogUtils()
            changeView()
            binding.btnTry.setOnClickListener {
                Intent(requireContext(), ModuleActivity::class.java).apply {
                    startActivity(this)
                }
            }
            binding.btnLogin.setOnClickListener {
                val authData: Account? = AuthPreferences(requireContext()).authData
                val authToken = AuthPreferences(requireContext()).authToken
                if (authData != null) {
                    val livedata = viewModel.authLogout(authToken)
                    livedata.observe(viewLifecycleOwner) { data ->
                        if (data != null) {
                            when (data) {
                                is Resource.Loading<*> -> {
                                    myDialog.setCustomDialog(
                                        requireContext(),
                                        R.layout.wait_dialog,
                                        false
                                    )
                                    myDialog.showCustomDialog(true)
                                }
                                is Resource.Success<*> -> {
                                    livedata.removeObservers(viewLifecycleOwner)
                                    if (mCallback != null) {
                                        mCallback!!.authCallback(null)
                                    }
                                    ToastUtils.showToast(
                                        data.message.toString(),
                                        requireContext()
                                    )
                                    myDialog.showCustomDialog(false)
                                    changeView()
                                }
                                is Resource.Error<*> -> {
                                    myDialog.showCustomDialog(false)
                                    var message = data.message.toString()
                                    if(message.contains("401", ignoreCase = true)) {
                                        AuthPreferences(requireContext()).saveAuthData(null)
                                        message = getString(R.string.logout_success)
                                    }
                                    ToastUtils.showToast(
                                        message,
                                        requireContext()
                                    )
                                    livedata.removeObservers(viewLifecycleOwner)
                                    changeView()
                                }
                                else -> {
                                    myDialog.showCustomDialog(false)
                                    var message = data.message.toString()
                                    if(message.contains("401", ignoreCase = true)) {
                                        AuthPreferences(requireContext()).saveAuthData(null)
                                        message = getString(R.string.logout_success)
                                    }
                                    ToastUtils.showToast(
                                        message,
                                        requireContext()
                                    )
                                    livedata.removeObservers(viewLifecycleOwner)
                                    changeView()
                                }
                            }
                        }
                    }
                } else {
                    Intent(requireContext(), LoginActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        changeView()
    }

    private fun changeView() {
        val data = AuthPreferences(requireContext()).authData
        if (data != null) {
            binding.btnLogin.text = getString(R.string.logout)
            binding.btnLogin.setBackgroundColor(resources.getColor(R.color.red_700, requireContext().theme))
            binding.btnLogin.setTextColor(resources.getColor(R.color.white, requireContext().theme))
        } else {
            binding.btnLogin.text = getString(R.string.login)
            binding.btnLogin.setBackgroundColor(resources.getColor(R.color.white, requireContext().theme))
            binding.btnLogin.setTextColor(resources.getColor(R.color.green_700, requireContext().theme))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AuthenticationCallback) {
            mCallback = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    interface AuthenticationCallback {
        fun authCallback(account: Account?)
    }
}