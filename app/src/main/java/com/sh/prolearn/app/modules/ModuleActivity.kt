package com.sh.prolearn.app.modules

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh.prolearn.R
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.ui.ModuleListAdapter
import com.sh.prolearn.databinding.ActivityModuleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

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
    }

    private fun moduleGet() {
        moduleAdapter = ModuleListAdapter()
        viewModel.moduleIndex("null").observe(this) { data ->
            if (data != null) {
                when (data) {
                    is Resource.Loading<*> -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.GONE
                        moduleAdapter.setData(data.data)
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
        moduleAdapter.onItemClick = {
//            Intent(this, PresenceDetailActivity::class.java).apply {
//                startActivity(this)
//            }
        }
        with(binding.rvAbsent) {
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