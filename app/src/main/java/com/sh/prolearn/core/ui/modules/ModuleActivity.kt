package com.sh.prolearn.core.ui.modules

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sh.prolearn.R
import com.sh.prolearn.databinding.ActivityModuleBinding

class ModuleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportActionBar?.title = getString(R.string.app_name)
        }
    }
}