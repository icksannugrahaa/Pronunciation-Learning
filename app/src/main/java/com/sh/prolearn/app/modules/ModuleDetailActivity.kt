package com.sh.prolearn.app.modules

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sh.prolearn.R
import com.sh.prolearn.app.lesson.LessonActivity
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.*
import com.sh.prolearn.core.ui.LessonListAdapter
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_REVIEW_STATUS
import com.sh.prolearn.core.utils.Consts.EXTRA_LESSON_DATA
import com.sh.prolearn.core.utils.Consts.EXTRA_MODULE_DATA
import com.sh.prolearn.core.utils.Consts.EXTRA_MODULE_PROGRESS_DATA
import com.sh.prolearn.core.utils.Consts.EXTRA_QUESTION_DATA
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.databinding.ActivityModuleDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ModuleDetailActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_material,
            R.string.tab_text_comment
        )
    }

    private lateinit var binding: ActivityModuleDetailBinding
    private val viewModel: ModuleViewModel by viewModel()
    private var moduleId: String? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val lessonData = intent.getSerializableExtra(EXTRA_LESSON_DATA) as List<Lesson>
        val moduleData = intent.getParcelableExtra<Module>(EXTRA_MODULE_DATA)
        val progressData = intent.getSerializableExtra(EXTRA_MODULE_PROGRESS_DATA) as List<Progress>
        val lessonCode = intent.getStringExtra(ARG_LESSON_CODE)
        Log.d("TAG_LESSON_CODE_MD", lessonCode.toString())
        moduleId = moduleData?.id

        if (savedInstanceState == null) {
            supportActionBar?.title = getString(R.string.list_lesson)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setData(moduleData!!)
        setPagerAdapter(lessonData, progressData, moduleData.comments as List<Comment>, lessonCode!!)

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

    private fun setData(module: Module) {
        with(binding) {
            setViewCondition(
                shimer = true,
                error = false,
                tvModule = false,
                imglesson = false,
                tab = false,
                viewpager = false
            )
            supportActionBar?.title = module.name
            tvModuleDesc.text = module.description
            imgLesson.loadImageFromServer(module.image)
            setViewCondition(
                shimer = false,
                error = false,
                tvModule = true,
                imglesson = true,
                tab = true,
                viewpager = true
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun moduleGet() {
        val authData = AuthPreferences(this@ModuleDetailActivity).authData
        val authToken = AuthPreferences(this@ModuleDetailActivity).authToken
        viewModel.moduleIndex("null", moduleId ?: "null").observe(this) { data ->
            if (data != null) {
                when (data) {
                    is Resource.Loading<*> -> {
                        setViewCondition(
                            shimer = true,
                            error = false,
                            tvModule = false,
                            imglesson = false,
                            tab = false,
                            viewpager = false
                        )
                    }
                    is Resource.Success<*> -> {
                        if (authData != null) {
                            viewModel.progressIndex(authToken).observe(this) { progress ->
                                if (progress != null) {
                                    when (progress) {
                                        is Resource.Loading<*> -> {
                                            setViewCondition(
                                                shimer = true,
                                                error = false,
                                                tvModule = false,
                                                imglesson = false,
                                                tab = false,
                                                viewpager = false
                                            )
                                        }
                                        is Resource.Success<*> -> {
                                            setViewCondition(
                                                shimer = false,
                                                error = false,
                                                tvModule = true,
                                                imglesson = true,
                                                tab = true,
                                                viewpager = true
                                            )
                                            val progressData =
                                                progress.data?.allProgress!!.filter { item ->
                                                    item?.order == data.data?.get(0)?.order
                                                }
                                            setPagerAdapter(
                                                data.data?.get(0)?.lessons as List<Lesson>,
                                                if (progressData.isNotEmpty()) progressData[0]?.progress!! as List<Progress> else ArrayList(),
                                                data.data?.get(0)?.comments as List<Comment>,
                                                data.data?.get(0)?.order.toString()
                                            )
                                        }
                                        is Resource.Error<*> -> {
                                            val message = progress.message.toString()
                                            if (message.contains("401", ignoreCase = true)) {
                                                AuthPreferences(this@ModuleDetailActivity).saveAuthData(
                                                    null
                                                )
                                                finish()
                                            } else {
                                                setViewCondition(
                                                    shimer = false,
                                                    error = true,
                                                    tvModule = false,
                                                    imglesson = false,
                                                    tab = false,
                                                    viewpager = false
                                                )
                                                binding.viewError.tvError.text = message
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            setPagerAdapter(
                                data.data?.get(0)?.lessons as List<Lesson>,
                                ArrayList<Progress>() as List<Progress>,
                                data.data?.get(0)?.comments as List<Comment>,
                                data.data?.get(0)?.order.toString()
                            )
                        }
                    }
                    is Resource.Error<*> -> {
                        setViewCondition(
                            shimer = false,
                            error = true,
                            tvModule = false,
                            imglesson = false,
                            tab = false,
                            viewpager = false
                        )
                        binding.viewError.tvError.text =
                            data.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        }
    }

    private fun setViewCondition(
        shimer: Boolean,
        error: Boolean,
        tvModule: Boolean,
        imglesson: Boolean,
        tab: Boolean,
        viewpager: Boolean
    ) {
        with(binding) {
            shimerProgress.root.visibility = if (shimer) View.VISIBLE else View.GONE
            viewError.root.visibility = if (error) View.VISIBLE else View.GONE
            tvModuleDesc.visibility = if (tvModule) View.VISIBLE else View.GONE
            imgLesson.visibility = if (imglesson) View.VISIBLE else View.GONE
            tabs.visibility = if (tab) View.VISIBLE else View.GONE
            viewPager.visibility = if (viewpager) View.VISIBLE else View.GONE
        }
    }

    private fun setPagerAdapter(
        lessonData: List<Lesson>,
        progressData: List<Progress>,
        reviewData: List<Comment>,
        lessonCode: String
    ) {
        val canReview = intent.getBooleanExtra(ARG_REVIEW_STATUS, false)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.setData(lessonData, progressData, lessonCode, reviewData, canReview)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    override fun onResume() {
        super.onResume()
        moduleGet()
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