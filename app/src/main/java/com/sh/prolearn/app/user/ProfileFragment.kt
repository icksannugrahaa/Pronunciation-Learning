@file:Suppress("DEPRECATION")

package com.sh.prolearn.app.user

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.sh.prolearn.R
import com.sh.prolearn.app.authentication.AuthenticationViewModel
import com.sh.prolearn.app.home.HomeFragment
import com.sh.prolearn.app.lesson.UploadViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.utils.Consts.CAMERA_PERMISSION_CODE
import com.sh.prolearn.core.utils.Consts.FIELD_EMAIL_NOT_VALID
import com.sh.prolearn.core.utils.Consts.FIELD_REQUIRED
import com.sh.prolearn.core.utils.Consts.STORAGE_PERMISSION_CODE
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.core.utils.ToastUtils.showToast
import com.sh.prolearn.databinding.FragmentProfileBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by viewModel()
    private val uViewModel: UploadViewModel by viewModel()
    private lateinit var myDialog: DialogUtils
    private var codeIsSend = false
    private val scope = MainScope()
    private var job: Job? = null
    private var timeLeft = 0
    private var imgFromStorage: Uri? = null
    private var imgUrl: String? = null
    private var mCallback: AuthenticationCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            setView()
            setFormStatus(false)
            checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE
            )
            checkPermission(
                Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE
            )
            myDialog = DialogUtils()
        }
    }

    private fun update() {
        val token = AuthPreferences(requireContext()).authToken
        val currentData = AuthPreferences(requireContext()).authData
        with(binding) {
            var error = 0
            if(tietPhone.text.toString() == "") {
                error = 1
                textLayoutPhone.error = FIELD_REQUIRED
            }
            if(tietName.text.toString() == "") {
                error = 1
                textLayoutName.error = FIELD_REQUIRED
            }
            if(tietEmail.text.toString() == "") {
                error = 1
                textLayoutEmail.error = FIELD_REQUIRED
            }
            if(actvGender.text.toString() == "") {
                error = 1
                textLayoutGender.error = FIELD_REQUIRED
            }
            if (error != 1) {
                val account = Account(
                    phoneNumber = tietPhone.text.toString() ?: currentData?.phoneNumber,
                    gender = actvGender.text.toString() ?: currentData?.gender,
                    biodata = tietBiodata.text.toString() ?: currentData?.biodata,
                    name = tietName.text.toString() ?: currentData?.name,
                    email = tietEmail.text.toString() ?: currentData?.email,
                    avatar = if (imgUrl != "null" && imgUrl != "" && imgUrl != null) imgUrl else currentData?.avatar
                )
                val livedata = viewModel.accountUpdate(token, account, tietCurrentPassword.text.toString())
                myDialog = DialogUtils()
                myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
                livedata.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        when (data) {
                            is Resource.Loading<*> -> {
                                myDialog.showCustomDialog(true)
                            }
                            is Resource.Success<*> -> {
                                showToast(data.message!!, requireContext())
                                setFormStatus(false)
                                binding.cardView9.visibility = View.GONE
                                if (mCallback != null) {
                                    mCallback!!.authCallback(data.data)
                                }
                                btnEditProfile.setText(R.string.edit_profile)
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
                                showToast(message, requireContext())
                                myDialog.showCustomDialog(false)
                                livedata.removeObservers(viewLifecycleOwner)
                            }
                        }
                    }
                }
            } else {
                showToast(getString(R.string.please_fix_all_error), requireContext())
            }
        }
    }

    private fun setEmailVerify(state: Boolean) {
        with(binding) {
            btnRefreshCode.visibility = if (state) View.VISIBLE else View.GONE
            btnVerifyCode.visibility = if (state) View.VISIBLE else View.GONE
            textLayoutEmailCode.visibility = if (state) View.VISIBLE else View.GONE
            binding.cardView9.visibility = if (!state) View.VISIBLE else View.GONE
        }
    }

    private fun setFormStatus(state: Boolean) {
        with(binding) {
            tietName.isEnabled = state
            tietBiodata.isEnabled = state
            tietEmail.isEnabled = state
            tietPhone.isEnabled = state
            actvGender.isEnabled = state
            ibAvatar.isEnabled = state
        }
    }

    private fun setView() {
        val authData: Account? = AuthPreferences(requireContext()).authData
        val genders = resources.getStringArray(R.array.genders)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_select_item, genders)
        with(binding) {
            binding.cardView9.visibility = View.GONE
            tietName.setText(authData?.name)
            tietBiodata.setText(authData?.biodata)
            tietEmail.setText(authData?.email)
            tietPhone.setText(authData?.phoneNumber)
            actvGender.setText(authData?.gender)
            actvGender.setAdapter(arrayAdapter)
            civAvatar.loadImageFromServer(authData?.avatar)
            civAvatar.setOnClickListener {
                myDialog.setCustomDialog(
                    requireContext(),
                    com.sh.prolearn.core.R.layout.dialog_image,
                    true
                )
                if (imgUrl != "null" && imgUrl != "" && imgUrl != null) myDialog.showImageDialog(true, imgUrl.toString())
                else myDialog.showImageDialog(true, authData?.avatar.toString())
            }
            ibAvatar.setOnClickListener {
                pickImageFromGallery()
            }
            btnEditProfile.setOnClickListener {
                when (btnEditProfile.text) {
                    "Kirim" -> {
                        update()
                    }
                    else -> {
                        btnEditProfile.setText(R.string.submit)
                        setFormStatus(true)
                    }
                }
            }
            btnRefreshCode.setOnClickListener {
                if (codeIsSend) {
                    showToast("Tolong kirim ulang setelah $timeLeft detik", requireContext())
                } else {
                    sendCode()
                    btnRefreshCode.text = getString(R.string.wait)
                    codeIsSend = true
                    timeLeft = 5
                    job = scope.launch {
                        Log.d("TAG_STOP", "S")
                        while (timeLeft > 0) {
                            delay(1000)
                            timeLeft -= 1
                        }
                        codeIsSend = false
                        btnRefreshCode.text = getString(R.string.send_code)
                        Log.d("TAG_STOP", "B")
                    }
                }
            }
            btnVerifyCode.setOnClickListener {
                if (tietCode.text.toString() != "") {
                    verifyCode()
                } else {
                    showToast("Tolong isi kode verifikasi terlebih dahulu!", requireContext())
                }
            }
            tietName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    textLayoutName.error = null
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() == "") {
                        textLayoutName.error = FIELD_REQUIRED
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() == "") {
                        textLayoutName.error = FIELD_REQUIRED
                    }
                }
            })
            actvGender.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    textLayoutGender.error = null
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() == "") {
                        textLayoutGender.error = FIELD_REQUIRED
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() == "") {
                        textLayoutGender.error = FIELD_REQUIRED
                    }
                }
            })
            tietEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    setEmailVerify(false)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setEmailVerify(true)
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() != "") {
                        if (Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                            if (s.toString() == authData?.email) {
                                setEmailVerify(false)
                            } else {
                                setEmailVerify(true)
                            }
                            textLayoutEmail.error = null
                        } else {
                            textLayoutEmail.error = FIELD_EMAIL_NOT_VALID
                        }
                    } else {
                        textLayoutEmail.error = FIELD_REQUIRED
                    }
                }
            })
            tietPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    textLayoutPhone.error = null
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() == "") {
                        textLayoutPhone.error = FIELD_REQUIRED
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() == "") {
                        textLayoutPhone.error = FIELD_REQUIRED
                    }
                }
            })
            if (tietEmail.text.toString() != authData?.email) {
                tietCode.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        textLayoutEmailCode.error = null
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (s.toString() == "") {
                            textLayoutEmailCode.error = FIELD_REQUIRED
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString() == "") {
                            textLayoutEmailCode.error = FIELD_REQUIRED
                        }
                    }
                })
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
            }
        }
    }

    private fun sendCode() {
        val token = AuthPreferences(requireContext()).authToken
        val livedata = viewModel.authSendCode(token, binding.tietEmail.text.toString())
        myDialog = DialogUtils()
        myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
        livedata.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                when (data) {
                    is Resource.Loading<*> -> {
                        myDialog.showCustomDialog(true)
                    }
                    is Resource.Success<*> -> {
                        showToast(data.message!!, requireContext())
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
                        showToast(message, requireContext())
                        myDialog.showCustomDialog(false)
                        livedata.removeObservers(viewLifecycleOwner)
                    }
                }
            }
        }
    }

    private fun verifyCode() {
        val authData = AuthPreferences(requireContext()).authData
        val key = "${binding.tietCode.text.toString()}-${authData?.createdAt}".encodeToByteArray()

        val livedata = viewModel.verifyAccount(binding.tietCode.text.toString())
        myDialog = DialogUtils()
        myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
        livedata.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                when (data) {
                    is Resource.Loading<*> -> {
                        binding.cardView9.visibility = View.GONE
                        myDialog.showCustomDialog(true)
                    }
                    is Resource.Success<*> -> {
                        setEmailVerify(false)
                        binding.cardView9.visibility = View.VISIBLE
                        showToast(data.message!!, requireContext())
                        myDialog.showCustomDialog(false)
                        livedata.removeObservers(viewLifecycleOwner)
                    }
                    is Resource.Error<*> -> {
                        binding.cardView9.visibility = View.GONE
                        val message = data.message.toString()
                        if (message.contains("401", ignoreCase = true)) {
                            AuthPreferences(requireContext()).saveAuthData(
                                null
                            )
                        }
                        showToast(message, requireContext())
                        setEmailVerify(true)
                        myDialog.showCustomDialog(false)
                        livedata.removeObservers(viewLifecycleOwner)
                    }
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            STORAGE_PERMISSION_CODE
        )
    }

    //handle result of picked image
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == STORAGE_PERMISSION_CODE) {
            imgFromStorage = data?.data
            val livedata =
                uViewModel.uploadFile(fileFromContentUri(requireContext(), data?.data!!), "images")
            myDialog = DialogUtils()
            myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
            livedata.observe(this) { upload ->
                if (upload != null) {
                    when (upload) {
                        is Resource.Loading<*> -> {
                            myDialog.showCustomDialog(true)
                        }
                        is Resource.Success<*> -> {
                            val urlOnly = upload.data?.view?.split("/")
                            imgUrl = "/${
                                urlOnly?.slice(3..(urlOnly.size - 1))?.joinToString(separator = "/")
                            }"
                            binding.civAvatar.loadImageFromServer(imgUrl)
                            myDialog.showCustomDialog(false)
                            livedata.removeObservers(this)
                        }
                        is Resource.Error<*> -> {
                            val message = upload.message.toString()
                            if (message.contains("401", ignoreCase = true)) {
                                AuthPreferences(requireContext()).saveAuthData(
                                    null
                                )
                            }
                            Log.d("TAG_MASUK_SINI", upload.message.toString())
                            myDialog.showCustomDialog(false)
                            livedata.removeObservers(this)
                        }
                    }
                }
            }
        }
    }

    private fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val name = contentUri.path?.split("/")
        val fileName = "${name!![name.size - 1]}.$fileExtension"

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(
                    resources.getString(R.string.permission_granted, "Camera"),
                    requireContext()
                )
            } else {
                showToast(
                    resources.getString(R.string.permission_denied, "Camera"),
                    requireContext()
                )
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(
                    resources.getString(R.string.permission_granted, "Storage"),
                    requireContext()
                )
            } else {
                showToast(
                    resources.getString(R.string.permission_denied, "Storage"),
                    requireContext()
                )
            }
        }
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
//            val extraText = if (requestCode == 100) "Camera" else "Storage"
//            showToast(resources.getString(R.string.permission_granted, extraText), this)
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