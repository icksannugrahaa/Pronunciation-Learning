package com.sh.prolearn.app.lesson

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh.prolearn.R
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.data.preferences.ProgressPreferences
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.domain.model.ScoreBoard
import com.sh.prolearn.core.ui.ResultReviewListAdapter
import com.sh.prolearn.core.ui.ScoreBoardListAdapter
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.ToastUtils
import com.sh.prolearn.databinding.FragmentResultReviewMaterialBinding
import com.sh.prolearn.databinding.FragmentResultScoreBoardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class ResultReviewMaterialFragment : Fragment() {
    private var _binding: FragmentResultReviewMaterialBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModuleViewModel by viewModel()
    private val pViewModel: PredictViewModel by viewModel()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var myDialog: DialogUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultReviewMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            getScoreBoard()
            mediaPlayer = MediaPlayer()
            myDialog = DialogUtils()

            val refreshLayout = binding.swipeToRefreshLayout
            refreshLayout.setColorSchemeColors(
                R.color.green_200,
                R.color.purple_200,
                R.color.teal_200,
                R.color.red_200
            )
            refreshLayout.setOnRefreshListener {
                getScoreBoard()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun getScoreBoard() {
        val authData = AuthPreferences(requireContext()).authData
        val authToken = AuthPreferences(requireContext()).authToken
        val lessonCode = arguments?.getString(Consts.ARG_LESSON_CODE)

        if (authData != null) {
            val scoreboardData = viewModel.progressIndex(authToken)
            scoreboardData.observe(viewLifecycleOwner) { progress ->
                if (progress != null) {
                    when (progress) {
                        is Resource.Loading<*> -> {
                            setViewCondition(
                                shimer = true,
                                error = false
                            )
                            Log.d("TAG_RESULT_LOADING", "LOADING...")
                        }
                        is Resource.Success<*> -> {
                            setViewCondition(
                                shimer = false,
                                error = false,
                            )
                            val myProgress = progress.data?.allProgress?.get(0)?.progress?.filter {
                                it?.lesson == lessonCode
                            }
                            setView(myProgress as List<Progress>)
                            Log.d("TAG_RESULT_HASILL", progress.data.toString())

                        }
                        is Resource.Error<*> -> {
                            val message = progress.message.toString()
                            if (message.contains("401", ignoreCase = true)) {
                                AuthPreferences(requireContext()).saveAuthData(
                                    null
                                )
                                requireActivity().finish()
                            } else {
                                setViewCondition(
                                    shimer = false,
                                    error = true
                                )
                                Log.d("TAG_RESULT_ERRR", message)
                            }
                        }
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun onPlayerClick(text: String, player: String, filename: String) {
        val progressPref = ProgressPreferences(requireContext())
        val authToken = AuthPreferences(requireContext()).authToken
        progressPref.savePlayerQuest("stop")
        progressPref.savePlayerAnswer("stop")

        try {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer = MediaPlayer()

            if (player == "quest") {
                progressPref.savePlayerQuest("playing")
                progressPref.savePlayerAnswer("stop")
                myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
                Log.d("TAG_FILENAME_REPLAY", filename)
                val livedata =
                    pViewModel.predictTTS(authToken, filename, text)
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
                                mediaPlayer.setDataSource("${Consts.BASE_URL}/${data.data?.view}")
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
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer.setDataSource("${Consts.BASE_URL}/api/file/show?filename=$filename&path=/others")
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        } catch (e: IOException) {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            Log.e("LOG_TAG", "prepare() failed");
        }
    }

    private fun setViewCondition(
        shimer: Boolean,
        error: Boolean
    ) {
        with(binding) {
            shimerProgress.root.visibility = if (shimer) View.VISIBLE else View.GONE
            viewError.root.visibility = if (error) View.VISIBLE else View.GONE
        }
    }

    private fun setView(progress: List<Progress>) {
        val authData = AuthPreferences(requireContext()).authData
        with(binding) {
            val reviewAdapter = ResultReviewListAdapter()
            reviewAdapter.setData(progress)
            reviewAdapter.onIbPlayerQuestClick = { it, text ->
                val filename = "${authData?.name?.split(' ')?.joinToString(separator = "_")}-${it.lesson}-${it.progress}-p.wav"
                onPlayerClick(text, "quest", filename)
                Log.d("TAG_REPLAY_QUEST", filename)
            }
            reviewAdapter.onIbPlayerAnswerClick = { it, text ->
                val filename = "${authData?.name?.split(' ')?.joinToString(separator = "_")}-${it.lesson}-${it.progress}.wav"
                onPlayerClick(text, "answer", filename)
                Log.d("TAG_REPLAY_ANSWER", filename)
            }
            with(rvScoreboard) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = reviewAdapter
            }
        }
    }
}