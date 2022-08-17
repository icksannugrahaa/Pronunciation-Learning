package com.sh.prolearn.app.modules

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh.prolearn.R
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.ui.ModuleListAdapter
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_REVIEW_STATUS
import com.sh.prolearn.core.utils.Consts.EXTRA_LESSON_DATA
import com.sh.prolearn.core.utils.Consts.EXTRA_MODULE_DATA
import com.sh.prolearn.core.utils.Consts.EXTRA_MODULE_PROGRESS_DATA
import com.sh.prolearn.core.utils.ToastUtils.showToast
import com.sh.prolearn.databinding.ActivityModuleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class ModuleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModuleBinding
    private val viewModel: ModuleViewModel by viewModel()
    private lateinit var moduleAdapter: ModuleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        moduleGet()
        if (savedInstanceState == null) {
            supportActionBar?.title = getString(R.string.list_module)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val refreshLayout = binding.swipeToRefreshLayout
        refreshLayout.setColorSchemeColors(
            R.color.green_200,
            R.color.purple_200,
            R.color.teal_200,
            R.color.red_200
        )
        refreshLayout.setOnRefreshListener {
            moduleGet()
            refreshLayout.isRefreshing = false
        }
    }

    private fun moduleGet() {
        var authData = AuthPreferences(this@ModuleActivity).authData
        val authToken = AuthPreferences(this@ModuleActivity).authToken
        moduleAdapter = ModuleListAdapter()

        viewModel.moduleIndex("null", "null").observe(this) { data ->
            if (data != null) {
                when (data) {
                    is Resource.Loading<*> -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.viewError.root.visibility = View.GONE
                    }
                    is Resource.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.GONE

                        if (authData != null) {
                            viewModel.progressIndex(authToken).observe(this) { progress ->
                                if (progress != null) {
                                    when (progress) {
                                        is Resource.Loading<*> -> {
                                            binding.progressBar.visibility = View.VISIBLE
                                            binding.viewError.root.visibility = View.GONE
                                        }
                                        is Resource.Success<*> -> {
                                            binding.progressBar.visibility = View.GONE
                                            binding.viewError.root.visibility = View.GONE
                                            progress.data?.let { moduleAdapter.setProgress(it) }
                                            authData.let {
                                                if (it != null) {
                                                    moduleAdapter.setAuth(it)
                                                }
                                            }
                                            moduleAdapter.setData(data.data)
                                        }
                                        is Resource.Error<*> -> {
                                            val message = progress.message.toString()
                                            if(message.contains("401", ignoreCase = true)) {
                                                AuthPreferences(this@ModuleActivity).saveAuthData(null)
                                                authData = null
                                                showToast(getString(R.string.login_expired), this@ModuleActivity)
                                                moduleAdapter.setData(data.data)
                                                binding.progressBar.visibility = View.GONE
                                                binding.viewError.root.visibility = View.GONE
                                            } else {
                                                binding.progressBar.visibility = View.GONE
                                                binding.viewError.root.visibility = View.VISIBLE
                                                binding.viewError.tvError.text = message
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            moduleAdapter.setData(data.data)
                        }
                    }
                    is Resource.Error<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text =
                            data.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        }

        if (authData != null) {
            moduleAdapter.onItemClick = {
                val moduleOrder = it.order
                if(moduleAdapter.progressData != null) {
                    val progressData = moduleAdapter.progressData?.allProgress!!.filter { progress ->
                        progress?.order == moduleOrder
                    }
                    Log.d("TAG_PROGRESS", progressData.toString())
                    if(progressData.isNotEmpty()) {
                        progressData.forEach { progress ->
                            if(progress?.status == "done" || progress?.status == "in_progress") {
                                Intent(this, ModuleDetailActivity::class.java).apply {
                                    this.putExtra(EXTRA_LESSON_DATA, it.lessons as Serializable)
                                    this.putExtra(EXTRA_MODULE_DATA, it)
                                    this.putExtra(ARG_LESSON_CODE, it.order.toString())
                                    this.putExtra(EXTRA_MODULE_PROGRESS_DATA, progress.progress as Serializable)
                                    this.putExtra(ARG_REVIEW_STATUS, progress.status == "done")
                                    startActivity(this)
                                }
                            } else if(authData!!.level!! >= it.level!!) {
                                Intent(this, ModuleDetailActivity::class.java).apply {
                                    this.putExtra(EXTRA_LESSON_DATA, it.lessons as Serializable)
                                    this.putExtra(EXTRA_MODULE_DATA, it)
                                    this.putExtra(ARG_LESSON_CODE, it.order.toString())
                                    this.putExtra(EXTRA_MODULE_PROGRESS_DATA, ArrayList<Progress>() as Serializable)
                                    startActivity(this)
                                }
                            } else {
                                showToast(getString(R.string.level_not_enaugh), this@ModuleActivity)
                            }
                        }
                    } else if(authData!!.level!! >= it.level!!) {
                        Intent(this, ModuleDetailActivity::class.java).apply {
                            this.putExtra(EXTRA_LESSON_DATA, it.lessons as Serializable)
                            this.putExtra(EXTRA_MODULE_DATA, it)
                            this.putExtra(ARG_LESSON_CODE, it.order.toString())
                            this.putExtra(EXTRA_MODULE_PROGRESS_DATA, ArrayList<Progress>() as Serializable)
                            startActivity(this)
                        }
                    } else {
                        showToast(getString(R.string.level_not_enaugh), this@ModuleActivity)
                    }
                }
            }
        } else {
            moduleAdapter.onItemClick = {
                showToast(getString(R.string.login_first), this@ModuleActivity)
            }
        }

        with(binding.rvModule) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = moduleAdapter
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