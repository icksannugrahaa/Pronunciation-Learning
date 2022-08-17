package com.sh.prolearn.app.lesson

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sh.prolearn.R
import com.sh.prolearn.app.modules.ModuleDetailActivity
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.data.preferences.ProgressPreferences
import com.sh.prolearn.core.domain.model.*
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_PROGRESS
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_TYPE
import com.sh.prolearn.core.utils.Consts.ARG_LEVEL_UP
import com.sh.prolearn.core.utils.Consts.ARG_NEW_ACHIEVEMENT
import com.sh.prolearn.core.utils.Consts.ARG_NEW_ACHIEVEMENT_MSG
import com.sh.prolearn.core.utils.Consts.ARG_QUIZ_DATA
import com.sh.prolearn.core.utils.Consts.ARG_SUMMARY_DATA
import com.sh.prolearn.core.utils.Consts.ARG_THEORY_DATA
import com.sh.prolearn.core.utils.Consts.EXTRA_QUESTION_DATA
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.ToastUtils.showToast
import com.sh.prolearn.databinding.ActivityLessonBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class LessonActivity : AppCompatActivity(), QuestTextImageFragment.LessonCallback, SummaryTextImageSongFragment.LessonCallback {
    companion object {
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
    }

    private lateinit var binding: ActivityLessonBinding
    private var currentSelectItemId: Int? = null
    private lateinit var myDialog: DialogUtils
    private val mViewModel: ModuleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showToast(getString(R.string.require_permission), this)
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 111
            )
            finish()
        } else {
            val moduleData = intent.getParcelableExtra<Lesson>(EXTRA_QUESTION_DATA)
            val progressData = intent.getParcelableExtra<Progress>(ARG_LESSON_PROGRESS)
            val lessonCode = intent.getStringExtra(ARG_LESSON_CODE)
            binding.appBarMain.ibNext.visibility = View.GONE
            myDialog = DialogUtils()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setFragmentView(savedInstanceState, moduleData!!, progressData!!, lessonCode!!)
        }
    }

    private fun setFragmentView(
        savedInstanceState: Bundle?,
        lesson: Lesson,
        progress: Progress,
        lessonCode: String,
        isLevelUp: Boolean = false,
        newBadge: Boolean = false,
        newBadgeMsg: String = ""
    ) {
        val questTextFragment = QuestTextFragment() // 1
        val questTextImageFragment = QuestTextImageFragment() //2
        val summaryTextImageSongFragment = SummaryTextImageSongFragment() //2
        val option = lesson.option!!
        var currentLesson = "t"
        var progressTheory = lesson.theory?.filter { item ->
            item?.order == 1
        }
        var code = lessonCode
        var progressQuiz: List<Quiz?>? = lesson.quiz?.filter { item ->
            item?.order == 1
        }

        if (progress.status != null) {
            var viewType = currentLesson
            var order = 1
            if (progress.status != "done") {
                val progressSplit = progress.progress!!.split("-")
                viewType = progressSplit[0]
                order = progressSplit[1].toInt()
            } else {
                val progressSplit = progress.progress!!.split("-")
                viewType = progressSplit[0]
                order = progressSplit[1].toInt()
                when (viewType) {
                    "t" -> {
                        if (lesson.theory?.size!! > (order + 1)) order += 1
                        else {
                            viewType = "q"
                            order = 1
                        }
                    }
                    "q" -> {
                        if (lesson.quiz?.size!! > (order + 1)) order += 1
                        else {
                            viewType = "s"
                            order = 1
                        }
                    }
                    "s" -> {
                        Intent(this, ResultActivity::class.java).apply {
                            startActivity(this)
                        }
                    }
                }
            }

            when (viewType) {
                "t" -> {
                    supportActionBar?.title = getString(R.string.theory)
                    currentLesson = "t"
                    progressTheory = lesson.theory?.filter { item ->
                        item?.order == order.toInt()
                    }
                    code = "$lessonCode-$viewType-$order"
                    val theorySplit = option.theoryType!!.split(",")
                    currentSelectItemId = savedInstanceState?.getInt(SAVED_STATE_CURRENT_TAB_KEY)
                        ?: if (theorySplit.contains("text") && theorySplit.contains("image") && theorySplit.contains(
                                "song"
                            )
                        ) {
                            1
                        } else if (theorySplit.contains("text") && theorySplit.contains("image")) {
                            2
                        } else if (theorySplit.contains("text")) {
                            1
                        } else {
                            1
                        }
                }
                "q" -> {
                    supportActionBar?.title = getString(R.string.quiz)
                    currentLesson = "q"
                    progressQuiz = lesson.quiz?.filter { item ->
                        item?.order == order.toInt()
                    }
                    code = "$lessonCode-$viewType-$order"
                    val quizSplit = option.quizType!!.split(",")
                    currentSelectItemId = savedInstanceState?.getInt(SAVED_STATE_CURRENT_TAB_KEY)
                        ?: if (quizSplit.contains("text") && quizSplit.contains("image") && quizSplit.contains(
                                "song"
                            )
                        ) {
                            1
                        } else if (quizSplit.contains("text") && quizSplit.contains("image")) {
                            2
                        } else if (quizSplit.contains("text")) {
                            1
                        } else {
                            1
                        }
                }
                "s" -> {
                    supportActionBar?.title = getString(R.string.summary)
                    currentLesson = "s"
                    code = "$lessonCode-$viewType-$order"
                    currentSelectItemId = 1

                    val summarySplit = option.summaryType!!.split(",")
                    currentSelectItemId = savedInstanceState?.getInt(SAVED_STATE_CURRENT_TAB_KEY)
                        ?: if (summarySplit.contains("text") && summarySplit.contains("image") && summarySplit.contains(
                                "song"
                            )
                        ) {
                            3
                        } else if (summarySplit.contains("text") && summarySplit.contains("image")) {
                            3
                        } else if (summarySplit.contains("text")) {
                            3
                        } else {
                            1
                        }
                }
            }
        } else {
            val theorySplit = option.theoryType!!.split(",")
            code = "$lessonCode-$currentLesson-1"
            currentSelectItemId = savedInstanceState?.getInt(SAVED_STATE_CURRENT_TAB_KEY)
                ?: if (theorySplit.contains("text") && theorySplit.contains("image") && theorySplit.contains(
                        "song"
                    )
                ) {
                    1
                } else if (theorySplit.contains("text") && theorySplit.contains("image")) {
                    2
                } else if (theorySplit.contains("text")) {
                    1
                } else {
                    1
                }
        }

        when (currentSelectItemId) {
            1 -> setCurrentFragment(
                questTextFragment,
                currentSelectItemId!!,
                currentLesson,
                progressTheory,
                progressQuiz,
                lesson.summary,
                code,
                isLevelUp,
                newBadge,
                newBadgeMsg
            )
            2 -> setCurrentFragment(
                questTextImageFragment,
                currentSelectItemId!!,
                currentLesson,
                progressTheory,
                progressQuiz,
                lesson.summary,
                code,
                isLevelUp,
                newBadge,
                newBadgeMsg
            )
            3 -> setCurrentFragment(
                summaryTextImageSongFragment,
                currentSelectItemId!!,
                currentLesson,
                progressTheory,
                progressQuiz,
                lesson.summary,
                code,
                isLevelUp,
                newBadge,
                newBadgeMsg
            )
        }
    }

    private fun setCurrentFragment(
        fragment: Fragment,
        itemId: Int,
        currentLesson: String,
        theory: List<Theory?>?,
        quiz: List<Quiz?>?,
        summary: Summary?,
        lessonCode: String,
        isLevelUp: Boolean = false,
        newBadge: Boolean = false,
        newBadgeMsg: String = ""
    ) {
        currentSelectItemId = itemId
        val bundle = Bundle()
        bundle.putString(ARG_LESSON_TYPE, currentLesson)
        when (currentLesson) {
            "t" -> {
                bundle.putSerializable(ARG_THEORY_DATA, theory as Serializable)
                bundle.putSerializable(ARG_QUIZ_DATA, ArrayList<Quiz>() as Serializable)
                bundle.putString(ARG_LESSON_CODE, lessonCode)
                bundle.putBoolean(ARG_LEVEL_UP, isLevelUp)
                bundle.putBoolean(ARG_NEW_ACHIEVEMENT, newBadge)
                bundle.putString(ARG_NEW_ACHIEVEMENT_MSG, newBadgeMsg)
            }
            "q" -> {
                bundle.putSerializable(ARG_THEORY_DATA, ArrayList<Theory>() as Serializable)
                bundle.putSerializable(ARG_QUIZ_DATA, quiz as Serializable)
                bundle.putString(ARG_LESSON_CODE, lessonCode)
                bundle.putBoolean(ARG_LEVEL_UP, isLevelUp)
                bundle.putBoolean(ARG_NEW_ACHIEVEMENT, newBadge)
                bundle.putString(ARG_NEW_ACHIEVEMENT_MSG, newBadgeMsg)
            }
            "s" -> {
                bundle.putParcelable(ARG_SUMMARY_DATA, summary)
                bundle.putString(ARG_LESSON_CODE, lessonCode)
                bundle.putBoolean(ARG_LEVEL_UP, isLevelUp)
                bundle.putBoolean(ARG_NEW_ACHIEVEMENT, newBadge)
                bundle.putString(ARG_NEW_ACHIEVEMENT_MSG, newBadgeMsg)
            }
        }
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
//        disallowAddToBackStack().commit()
    }

    private fun setViewCondition(
        shimer: Boolean,
        error: Boolean,
        main: Boolean
    ) {
        with(binding) {
            shimerProgress.root.visibility = if (shimer) View.VISIBLE else View.GONE
            viewError.root.visibility = if (error) View.VISIBLE else View.GONE
            navHostFragment.visibility = if (main) View.VISIBLE else View.GONE
        }
    }

    private fun progressStore() {
        val authData = AuthPreferences(this@LessonActivity).authData
        val authToken = AuthPreferences(this@LessonActivity).authToken
        val currentProgress = ProgressPreferences(this@LessonActivity).progressData
        val currentPredict = ProgressPreferences(this@LessonActivity).predictData

        if (authData != null) {
            val store = mViewModel.progressStore(
                authToken,
                time = currentProgress?.scores?.time!!,
                score = currentProgress.scores?.score!!,
                lesson = currentProgress.lesson!!,
                progress = currentProgress.progress!!,
                status = currentProgress.status!!,
                quest = currentPredict?.textQuest!!,
                answer = currentPredict.finalText!!
            )
            store.observe(this) { progress ->
                if (progress != null) {
                    when (progress) {
                        is Resource.Loading<*> -> {
                            setViewCondition(
                                shimer = true,
                                error = false,
                                main = false
                            )
                        }
                        is Resource.Success<*> -> {
                            setViewCondition(
                                shimer = false,
                                error = false,
                                main = true
                            )
                            val lessonType = currentProgress.progress!!.split('-')[0]
                            if(lessonType == "s") {
                                Log.d("TAG_HASIL_SUMMARY", progress.data.toString())
                                Intent(this, ResultActivity::class.java).apply {
                                    putExtra(ARG_LEVEL_UP, progress.data?.levelUp)
                                    putExtra(ARG_NEW_ACHIEVEMENT, progress.data?.newAchievement)
                                    putExtra(ARG_NEW_ACHIEVEMENT_MSG, progress.data?.newAchievementMsg)
                                    this.putExtra(
                                        ARG_LESSON_CODE,
                                        currentProgress.lesson!!
                                    )
                                    startActivity(this)
                                }
                                finish()
                            } else {
                                setUpProgress(progress.data?.levelUp ?: false, progress.data?.newAchievement ?: false, progress.data?.newAchievementMsg ?: "")
                            }
                        }
                        is Resource.Error<*> -> {
                            val message = progress.message.toString()
                            if (message.contains("401", ignoreCase = true)) {
                                AuthPreferences(this@LessonActivity).saveAuthData(
                                    null
                                )
                                finish()
                            } else {
                                setViewCondition(
                                    shimer = false,
                                    error = true,
                                    main = false
                                )
                                binding.viewError.tvError.text = message
                            }
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setUpProgress(isLevelUp: Boolean, newBadge: Boolean, newBadgeMsg: String) {
        val authData = AuthPreferences(this@LessonActivity).authData
        val authToken = AuthPreferences(this@LessonActivity).authToken
        val currentProgress = ProgressPreferences(this@LessonActivity).progressData
        var moduleOrder = 1
        if (currentProgress?.progress != null) {
            moduleOrder = currentProgress.lesson?.split("-")?.get(0)?.toInt() ?: 1
        }
        if (authData != null) {
            mViewModel.progressIndex(authToken).observe(this) { progress ->
                if (progress != null) {
                    when (progress) {
                        is Resource.Loading<*> -> {
                            setViewCondition(
                                shimer = true,
                                error = false,
                                main = false
                            )
                        }
                        is Resource.Success<*> -> {
                            setViewCondition(
                                shimer = false,
                                error = false,
                                main = true
                            )
                            val progressData =
                                progress.data?.allProgress!!.filter { item ->
                                    item?.order == moduleOrder
                                }
                            Log.d("TAG_PROGRESS_DATA", progressData.toString())
                            val moduleData = intent.getParcelableExtra<Lesson>(EXTRA_QUESTION_DATA)
                            if (progressData.isNotEmpty()) {
                                progressData[0]?.progress!!.forEach { myProgress ->
                                    if (myProgress?.status == "new" || myProgress?.status == "in_progress") {
                                        binding.appBarMain.ibNext.visibility = View.GONE
                                        setFragmentView(
                                            null,
                                            moduleData!!,
                                            myProgress,
                                            currentProgress?.lesson!!,
                                            isLevelUp,
                                            newBadge,
                                            newBadgeMsg
                                        )
                                    }
                                }
                            }
                        }
                        is Resource.Error<*> -> {
                            val message = progress.message.toString()
                            if (message.contains("401", ignoreCase = true)) {
                                AuthPreferences(this@LessonActivity).saveAuthData(
                                    null
                                )
                                finish()
                            } else {
                                setViewCondition(
                                    shimer = false,
                                    error = true,
                                    main = false
                                )
                                binding.viewError.tvError.text = message
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myDialog.showAlertExitDialog(this@LessonActivity, this@LessonActivity)
            return true // return
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                myDialog.showAlertExitDialog(this@LessonActivity, this@LessonActivity)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun recognitionCallback(status: Boolean) {
        binding.appBarMain.ibNext.visibility = if (status) View.VISIBLE else View.GONE
        binding.appBarMain.ibNext.setOnClickListener { progressStore() }
    }
}