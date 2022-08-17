package com.sh.prolearn.app.lesson

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sh.prolearn.R
import com.sh.prolearn.app.home.HomeFragment
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.data.preferences.ProgressPreferences
import com.sh.prolearn.core.domain.model.*
import com.sh.prolearn.core.utils.*
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_TYPE
import com.sh.prolearn.core.utils.Consts.ARG_LEVEL_UP
import com.sh.prolearn.core.utils.Consts.ARG_NEW_ACHIEVEMENT
import com.sh.prolearn.core.utils.Consts.ARG_QUIZ_DATA
import com.sh.prolearn.core.utils.Consts.ARG_THEORY_DATA
import com.sh.prolearn.core.utils.Consts.BASE_URL
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.core.utils.ToastUtils.showToast
import com.sh.prolearn.databinding.DialogCountdownBinding
import com.sh.prolearn.databinding.FragmentQuestTextImageBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


@Suppress("DEPRECATION")
class QuestTextImageFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentQuestTextImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var recorder: RecordUtils
    private lateinit var timer: TimerUtils
    private lateinit var myDialog: DialogUtils
    private val viewModel: PredictViewModel by viewModel()
    private var mCallback: LessonCallback? = null

    private val scope = MainScope()
    var job: Job? = null
    var path = ""
    var timeSpent = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestTextImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                StringBuilder().append(
                    "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/Android"
                ).toString()
            } else {
                StringBuilder().append("${Environment.getExternalStorageDirectory()}/Android")
                    .toString()
            }

            // init service
            mediaRecorder = MediaRecorder() //deprecation
            mediaPlayer = MediaPlayer()
            recorder = RecordUtils()
            timer = TimerUtils()
            myDialog = DialogUtils()
            myDialog.showLevelUpDialog(requireContext(), arguments!!.getBoolean(ARG_LEVEL_UP, false))
            myDialog.showNewAchievementDialog(requireContext(), arguments!!.getBoolean(ARG_NEW_ACHIEVEMENT, false))

            // get data
            val lessonType = arguments?.getString(ARG_LESSON_TYPE)!!
            val lessonCode = arguments?.getString(ARG_LESSON_CODE)!!
            val theoryData = arguments?.getSerializable(ARG_THEORY_DATA) as List<Theory>
            val quizData = arguments?.getSerializable(ARG_QUIZ_DATA) as List<Quiz>

            setData(lessonType, theoryData, quizData, lessonCode)

        }
    }

    private fun setData(type: String, theory: List<Theory>, quiz: List<Quiz>, lessonCode: String) {
        // progress preff
        val progressPref = ProgressPreferences(requireContext())
        val authData = AuthPreferences(requireContext()).authData

        // set record status
        progressPref.saveRecordStatus("stop")
        progressPref.savePlayerQuest("stop")
        progressPref.savePlayerAnswer("stop")

        // set record filename
        val filename = "${authData?.id}-${lessonCode}.wav"
        Log.d("TAG_FILENAME", filename)
        if (progressPref.currentRecordFilename == "null" || progressPref.currentRecordFilename != filename) {
            progressPref.saveRecordFilename(filename)
        }
        recorder.WavRecorder(path, progressPref.currentRecordFilename)

        if (type == "t") {
            if (theory.isNotEmpty()) {
                with(binding) {
                    tvQuestType.text = getString(R.string.theory)
                    tvQuestInfo.text = getString(R.string.quest_info_text_image)
                    tvQuest.text = "\"${theory[0].question}\""
                    ivQuestImage.loadImageFromServer(theory[0].image)
                    lavAnswerRecord.setAnimation("mic.json")
                    lavAnswerRecord.playAnimation()
                    lavAnswerRecord.setOnClickListener(this@QuestTextImageFragment)
                    ibAnswerReplay.setOnClickListener(this@QuestTextImageFragment)
                    ibQuestReplay.setOnClickListener(this@QuestTextImageFragment)
                }
            } else {
                requireActivity().finish()
            }
        } else if (type == "q") {
            if (quiz.isNotEmpty()) {
                with(binding) {
                    tvQuestType.text = getString(R.string.quiz)
                    tvQuestInfo.text = getString(R.string.quiz_info_text_image)
                    tvTapInfo.text = getString(R.string.tap_to_record)
                    tvQuest.text = "${quiz[0].question}"
                    ivQuestImage.loadImageFromServer(quiz[0].image)
                    lavAnswerRecord.setAnimation("mic.json")
                    lavAnswerRecord.playAnimation()
                    lavAnswerRecord.setOnClickListener(this@QuestTextImageFragment)
                    ibAnswerReplay.setOnClickListener(this@QuestTextImageFragment)
                    ibQuestReplay.setOnClickListener(this@QuestTextImageFragment)
                }
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun onPlayerClick(text: String, player: String) {
        val progressPref = ProgressPreferences(requireContext())
        val authToken = AuthPreferences(requireContext()).authToken
        try {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer = MediaPlayer()

            if (player == "quest") {
                progressPref.savePlayerQuest("playing")
                progressPref.savePlayerAnswer("stop")
                myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
                val filenameSplit = progressPref.currentRecordFilename.split('.')
                val filename = "${filenameSplit[0]}-p.wav"
                Log.d("TAG_FILENAME_REPLAY", filename)
                val livedata =
                    viewModel.predictTTS(authToken, filename, text)
                livedata.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        when (data) {
                            is Resource.Loading<*> -> {
                                myDialog.showCustomDialog(true)
                            }
                            is Resource.Success<*> -> {
                                livedata.removeObservers(viewLifecycleOwner)
                                ToastUtils.showToast(
                                    data.message.toString(),
                                    requireContext()
                                )
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                                mediaPlayer.setDataSource("$BASE_URL/${data.data?.view}")
                                mediaPlayer.prepare()
                                mediaPlayer.start()
                                myDialog.showCustomDialog(false)
                            }
                            is Resource.Error<*> -> {
                                myDialog.showCustomDialog(false)
                                var message = data.message.toString()
                                if (message.contains("401", ignoreCase = true)) {
                                    AuthPreferences(requireContext()).saveAuthData(null)
                                    message = getString(R.string.logout_success)
                                }
                                ToastUtils.showToast(
                                    message,
                                    requireContext()
                                )
                                livedata.removeObservers(viewLifecycleOwner)
                            }
                        }
                    }
                }
            } else if (player == "answer") {
                progressPref.savePlayerQuest("stop")
                progressPref.savePlayerAnswer("playing")
                try {
                    mediaPlayer.setDataSource("$path/${progressPref.currentRecordFilename}")
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch(e: Exception) {
                    showToast(getString(R.string.record_first), requireContext())
                }
            }
        } catch (e: IOException) {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            Log.e("LOG_TAG", "prepare() failed");
        }
    }

    override fun onClick(v: View?) {
        // progress preff
        val progressPref = ProgressPreferences(requireContext())

        with(binding) {
            when (v?.id) {
                R.id.lav_answer_record -> {
                    if (mCallback != null) {
                        mCallback!!.recognitionCallback(false)
                    }
                    when (progressPref.currentRecordStatus) {
                        "stop" -> {
                            lavAnswerRecord.setAnimation("listen-lesson.json")
                            updateRecord()
                        }
                        "prepare" -> {
                            lavAnswerRecord.setAnimation("replay-lesson.json")
                            lavAnswerRecord.playAnimation()
                            binding.tvTapInfo.text = getString(R.string.tap_to_stop)
                            progressPref.saveRecordStatus("start")
                            recorder.stopRecording()
                            updateRecord()
                        }
                    }
                }
                R.id.ib_quest_replay -> {
                    onPlayerClick(tvQuest.text.toString(), "quest")
                }
                R.id.ib_answer_replay -> {
                    onPlayerClick(tvQuest.text.toString(), "answer")
                }
            }
        }
    }

    private fun updateRecord() {
        stopUpdates()
        // progress preff
        val progressPref = ProgressPreferences(requireContext())
        when (progressPref.currentRecordStatus) {
            "stop" -> {
                myDialog.setCustomDialog(
                    requireContext(), R.layout.dialog_countdown,
                    isCancelAble = false,
                    isCountDown = true,
                    countDownState = true
                )
                progressPref.saveRecordStatus("prepare")
                job = scope.launch {
                    Log.d("TAG_STOP", "S")
                    myDialog.showCustomDialog(true)
                    delay(5000)
                    myDialog.showCustomDialog(false)
                    updateRecord()
                    Log.d("TAG_STOP", "B")
                }
                binding.lavAnswerRecord.playAnimation()
            }
            "prepare" -> {
                binding.tvTapInfo.text = getString(R.string.tap_to_stop)
                myDialog.setCustomDialog(
                    requireContext(), R.layout.dialog_countdown,
                    isCancelAble = false,
                    isCountDown = true,
                    countDownState = false
                )
                recorder.startRecording()
                var time = 10

                job = scope.launch {
                    Log.d("TAG_PREPARE", "S")
                    while (time > 0) {
                        delay(1000)
                        time -= 1
                        timeSpent += 1
                    }
                    recorder.stopRecording()
                    myDialog.showCustomDialog(true)
                    binding.lavAnswerRecord.setAnimation("replay-lesson.json")
                    binding.lavAnswerRecord.playAnimation()
                    binding.tvTapInfo.text = getString(R.string.tap_to_replay)
                    binding.lavAnswerRecord.loop(true)
                    delay(5000)
                    myDialog.showCustomDialog(false)
                    progressPref.saveRecordStatus("start")
                    updateRecord()
                    Log.d("TAG_PREPARE", "B")
                }
            }
            "start" -> {
                Log.d("TAG_START", "S")
                val authToken = AuthPreferences(requireContext()).authToken
                progressPref.saveRecordStatus("stop")
                myDialog.setCustomDialog(
                    requireContext(), R.layout.dialog_countdown,
                    isCancelAble = false,
                    isPredict = true
                )
                val livedata =
                    viewModel.predictFile(
                        "$path/${progressPref.currentRecordFilename}",
                        "others",
                        authToken
                    )
                livedata.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        when (data) {
                            is Resource.Loading<*> -> {
                                myDialog.showCustomDialog(true)
                                binding.tvAnswer.visibility = View.GONE
                            }
                            is Resource.Success<*> -> {
                                livedata.removeObservers(viewLifecycleOwner)
                                ToastUtils.showToast(
                                    data.message.toString(),
                                    requireContext()
                                )
                                myDialog.showCustomDialog(false)
                                binding.tvAnswer.visibility = View.VISIBLE
                                binding.tvTapInfo.text = getString(R.string.tap_to_replay)
                                setResult(data.data!!)
                                if (mCallback != null) {
                                    mCallback!!.recognitionCallback(true)
                                }
                                stopUpdates()
                            }
                            is Resource.Error<*> -> {
                                myDialog.showCustomDialog(false)
                                binding.tvAnswer.visibility = View.GONE
                                binding.tvTapInfo.text = getString(R.string.tap_to_replay)
                                var message = data.message.toString()
                                if (message.contains("401", ignoreCase = true)) {
                                    AuthPreferences(requireContext()).saveAuthData(null)
                                    message = getString(R.string.logout_success)
                                }
                                ToastUtils.showToast(
                                    message,
                                    requireContext()
                                )
                                livedata.removeObservers(viewLifecycleOwner)
                                stopUpdates()
                            }
                        }
                    }
                }
                Log.d("TAG_START", "B")
            }
        }
    }

    private fun setResult(predict: Predict) {
        with(binding) {
            val progressPreferences = ProgressPreferences(requireContext())
            val codeSplit = arguments?.getString(ARG_LESSON_CODE)!!.split('-')

            // processing text
            val predictResultSplit = predict.finalText?.split(" ")
            var finalPredictText = ""
            val re = Regex("[^A-Za-z0-9 ]")
            predictResultSplit?.forEachIndexed { i, it ->
                Log.d("TAG_LOOP_TEXT", i.toString())
                if (i == 0) it.toUpperCase()
                finalPredictText += if (it.contains("<")) {
                    "<font color=#FF0000>${re.replace(it, "")}</font> "
                } else {
                    "<font color=#228B22>$it</font> "
                }
            }
            predict.finalText = finalPredictText

            // save data
            progressPreferences.saveCurrentPredict(predict)

            val score = Scores(
                answer = finalPredictText,
                quest = if (codeSplit[2] == "q") "What it is ? (${predict.textQuest})" else predict.textQuest,
                score = predict.totalScore,
                time = timeSpent
            )
            val progress = Progress(
                scores = score,
                lesson = "${codeSplit[0]}-${codeSplit[1]}",
                progress = "${codeSplit[2]}-${codeSplit[3]}",
                status = "done"
            )
            progressPreferences.saveCurrentProgress(progress, score)

            tvAnswer.text = Html.fromHtml(finalPredictText, 1)
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

    private fun stopUpdates() {
        job?.cancel()
        job = null
    }

    public override fun onDestroy() {
        recorder.stopRecording()
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        stopUpdates()
        super.onDestroy()
    }
}