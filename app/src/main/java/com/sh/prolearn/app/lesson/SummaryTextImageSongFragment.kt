package com.sh.prolearn.app.lesson

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sh.prolearn.R
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.data.preferences.ProgressPreferences
import com.sh.prolearn.core.domain.model.*
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_TYPE
import com.sh.prolearn.core.utils.Consts.ARG_LEVEL_UP
import com.sh.prolearn.core.utils.Consts.ARG_NEW_ACHIEVEMENT
import com.sh.prolearn.core.utils.Consts.ARG_QUIZ_DATA
import com.sh.prolearn.core.utils.Consts.ARG_SUMMARY_DATA
import com.sh.prolearn.core.utils.Consts.ARG_THEORY_DATA
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.core.utils.RecordUtils
import com.sh.prolearn.core.utils.TimerUtils
import com.sh.prolearn.databinding.FragmentQuestTextImageBinding
import com.sh.prolearn.databinding.FragmentSummaryTextImageSongBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class SummaryTextImageSongFragment : Fragment(){
    private var _binding: FragmentSummaryTextImageSongBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaPlayer: MediaPlayer
    private val mViewModel: ModuleViewModel by viewModel()
    private lateinit var myDialog: DialogUtils

    private val scope = MainScope()
    var job: Job? = null
    var path = ""
    var currentLength = 0
    private var mCallback: LessonCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryTextImageSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                StringBuilder().append(
                    "${
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    }"
                ).toString()
            } else {
                StringBuilder().append("${Environment.getExternalStorageDirectory()}")
                    .toString()
            }

            // get data
            val lessonType = arguments?.getString(ARG_LESSON_TYPE)!!
            val lessonCode = arguments?.getString(ARG_LESSON_CODE)!!
            val summaryData = arguments?.getParcelable<Summary>(ARG_SUMMARY_DATA)
            myDialog = DialogUtils()
            myDialog.showLevelUpDialog(requireContext(), arguments!!.getBoolean(ARG_LEVEL_UP, false))
            myDialog.showNewAchievementDialog(requireContext(), arguments!!.getBoolean(ARG_NEW_ACHIEVEMENT, false))

            setData(summaryData!!)

            Log.d("TAG_SUMMARY_LESSON_TYPE", lessonType.toString())
            Log.d("TAG_SUMMARY_LESSON_CODE", lessonCode.toString())
            Log.d("TAG_SUMMARY_LESSON_DATA", summaryData.toString())
        }
    }

    @Suppress("DEPRECATION")
    private fun setData(summary: Summary) {
        // progress preff
        val progressPref = ProgressPreferences(requireContext())
        val authData = AuthPreferences(requireContext()).authData

        // set record status
        progressPref.savePlayerAnswer("stop")
        mediaPlayer = MediaPlayer()

        if (mCallback != null) {
            mCallback!!.recognitionCallback(false)
        }

        with(binding) {
            tvQuestType.text = getString(R.string.theory)
            tvQuest.text = "\"${summary.text}\""
            ivQuestImage.loadImageFromServer(summary.image)
            lavAnswerRecord.setOnClickListener {
                when (progressPref.playerAnswerState) {
                    "stop" -> {
                        lavAnswerRecord.setAnimation("listen-lesson.json")
                        progressPref.savePlayerAnswer("start")
                        binding.tvTapInfo.text = getString(R.string.tap_to_start_listening)

                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                        mediaPlayer.setDataSource("${Consts.BASE_URL}/${summary.song}")
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }
                    "start" -> {
                        lavAnswerRecord.setAnimation("pause-lesson.json")
                        progressPref.savePlayerAnswer("pause")
                        binding.tvTapInfo.text = getString(R.string.tap_to_pause_listening)
                        mediaPlayer.pause()
                        currentLength = mediaPlayer.currentPosition
                    }
                    "pause" -> {
                        lavAnswerRecord.setAnimation("listen-lesson.json")
                        progressPref.savePlayerAnswer("start")
                        binding.tvTapInfo.text = getString(R.string.tap_to_start_listening)
                        mediaPlayer.seekTo(currentLength)
                        mediaPlayer.start()
                    }
                }
                mediaPlayer.setOnCompletionListener {
                    saveCalculateResult()
                    lavAnswerRecord.setAnimation("play-lesson.json")
                    lavAnswerRecord.playAnimation()
                    mediaPlayer.stop()
                    mediaPlayer = MediaPlayer()
                    progressPref.savePlayerAnswer("stop")
                    if (mCallback != null) {
                        mCallback!!.recognitionCallback(true)
                    }
                }
                lavAnswerRecord.playAnimation()
            }
        }
    }

    private fun saveCalculateResult() {
        with(binding) {
            val authData = AuthPreferences(requireContext()).authData
            val authToken = AuthPreferences(requireContext()).authToken
            val progressPreferences = ProgressPreferences(requireContext())
            val codeSplit = arguments?.getString(ARG_LESSON_CODE)!!.split('-')
            val currentScore = progressPreferences.scoreData
            val currentProgress = progressPreferences.progressData
            var moduleOrder = 1
            if (currentProgress?.progress != null) {
                moduleOrder = currentProgress.lesson?.split("-")?.get(0)?.toInt() ?: 1
            }

            if (authData != null) {
                mViewModel.progressIndex(authToken).observe(viewLifecycleOwner) { progress ->
                    if (progress != null) {
                        when (progress) {
                            is Resource.Loading<*> -> {
                                Log.d("TAG_SUMMARY_ALL_PROGRES", "LOADING")
                            }
                            is Resource.Success<*> -> {
                                val data =
                                    progress.data?.allProgress!!.filter { item ->
                                        item?.order == moduleOrder
                                    }
                                var timeFinal = 0
                                var scoreFinal = 0.0
                                var lenTotal = 0
                                data[0]?.progress?.forEach {
                                    Log.d("TAG_SUMMARY_PROGRES", it.toString())
                                    if(it?.lesson == currentProgress?.lesson && it?.scores?.score != null) {
                                        timeFinal += it.scores?.time!!.toInt()
                                        scoreFinal += it.scores?.score!!.toDouble()
                                        lenTotal += 1
                                    }
                                }
                                Log.d("TAG_SUMMARY_PROGRES_SCORE_FINAL", scoreFinal.toString())
                                Log.d("TAG_SUMMARY_PROGRES_SCORE_TOTAL", (lenTotal*100).toString())
                                val finalScore = ((scoreFinal/(lenTotal*100).toDouble())*100)
                                Log.d("TAG_SUMMARY_PROGRES_SCORE", finalScore.toString())

                                // save data
                                val score = Scores(
                                    answer = "null",
                                    quest = "null",
                                    score = finalScore,
                                    time = timeFinal
                                )
                                val progressData = Progress(
                                    scores = score,
                                    lesson = "${codeSplit[0]}-${codeSplit[1]}",
                                    progress = "${codeSplit[2]}-${codeSplit[3]}",
                                    status = "done"
                                )
                                progressPreferences.saveCurrentProgress(progressData, score)
                            }
                            is Resource.Error<*> -> {
                                val message = progress.message.toString()
                                if (message.contains("401", ignoreCase = true)) {
                                    AuthPreferences(requireContext()).saveAuthData(
                                        null
                                    )
                                } else {

                                    Log.d("TAG_SUMMARY_ALL_PROGRES", "DATA kosong")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LessonCallback) {
            mCallback = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    interface LessonCallback {
        fun recognitionCallback(status: Boolean)
    }
}