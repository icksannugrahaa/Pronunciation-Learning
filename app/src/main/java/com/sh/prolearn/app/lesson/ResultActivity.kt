package com.sh.prolearn.app.lesson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.sh.prolearn.R
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_LEVEL_UP
import com.sh.prolearn.core.utils.Consts.SAVED_STATE_CURRENT_TAB_KEY
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.databinding.ActivityResultBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class ResultActivity : AppCompatActivity() {
    private var currentSelectItemId = R.id.btn_nav_score
    private lateinit var binding: ActivityResultBinding
    private lateinit var myDialog: DialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        myDialog = DialogUtils()
        showLevelUpDialog()
        setFragmentView(savedInstanceState)
        supportActionBar?.title = getString(R.string.score_board)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLevelUpDialog() {
        val isLevelUp = intent.getBooleanExtra(ARG_LEVEL_UP, false)
        if(isLevelUp) {
            myDialog.setCustomDialog(
                this, R.layout.dialog_level_up,
                isCancelAble = true
            )
            myDialog.showCustomDialog(true)
        }
    }

    private fun setFragmentView(savedInstanceState: Bundle?) {
        val scoreFragment = ResultMainFragment()
        val scoreboardFragment = ResultScoreBoardFragment()
        val reviewFragment = ResultReviewMaterialFragment()

        if (savedInstanceState != null) {
            currentSelectItemId = savedInstanceState.getInt(SAVED_STATE_CURRENT_TAB_KEY)
        }

        when(currentSelectItemId) {
            R.id.btn_nav_score -> setCurrentFragment(scoreFragment, currentSelectItemId)
            R.id.btn_nav_review -> setCurrentFragment(reviewFragment, currentSelectItemId)
            R.id.btn_nav_scoreboard -> setCurrentFragment(scoreboardFragment, currentSelectItemId)
        }

        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.btn_nav_score -> setCurrentFragment(scoreFragment, it.itemId)
                R.id.btn_nav_review -> setCurrentFragment(reviewFragment, currentSelectItemId)
                R.id.btn_nav_scoreboard -> setCurrentFragment(scoreboardFragment, it.itemId)
                else -> throw AssertionError()
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment, itemId: Int) {
        currentSelectItemId = itemId
        val lessonCode = intent.getStringExtra(ARG_LESSON_CODE)
        fragment.arguments = Bundle().apply {
            putString(ARG_LESSON_CODE, lessonCode)
        }
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).disallowAddToBackStack().commit()
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