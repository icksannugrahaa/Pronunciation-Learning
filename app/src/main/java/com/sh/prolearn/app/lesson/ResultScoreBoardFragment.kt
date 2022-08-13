package com.sh.prolearn.app.lesson

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh.prolearn.R
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.ScoreBoard
import com.sh.prolearn.core.ui.ReviewListAdapter
import com.sh.prolearn.core.ui.ScoreBoardListAdapter
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.databinding.FragmentResultMainBinding
import com.sh.prolearn.databinding.FragmentResultScoreBoardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultScoreBoardFragment : Fragment() {
    private var _binding: FragmentResultScoreBoardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModuleViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultScoreBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            getScoreBoard()

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
            val scoreboardData = viewModel.scoreboardIndex(authToken,lessonCode ?: "1-1","null")
            scoreboardData.observe(viewLifecycleOwner) { scoreboard ->
                if (scoreboard != null) {
                    when (scoreboard) {
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
                            setView(scoreboard.data)
                            Log.d("TAG_RESULT_HASILL", scoreboard.data.toString())

                        }
                        is Resource.Error<*> -> {
                            val message = scoreboard.message.toString()
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

    private fun setViewCondition(
        shimer: Boolean,
        error: Boolean
    ) {
        with(binding) {
            shimerProgress.root.visibility = if (shimer) View.VISIBLE else View.GONE
            viewError.root.visibility = if (error) View.VISIBLE else View.GONE
        }
    }

    private fun setView(scoreboards: List<ScoreBoard>?) {
        val authData = AuthPreferences(requireContext()).authData

        if(scoreboards.isNullOrEmpty()) {
            Log.d("TAG_DATA_NULL", "NULLDATA")
        } else {
            with(binding) {
                var myPosition = 0
                for (i in scoreboards.indices) {
                    if(scoreboards[i].email == authData?.email) {
                        myPosition = i
                        break
                    }
                }
                val scoreboardAdapter = ScoreBoardListAdapter()
                scoreboardAdapter.setData(scoreboards)
                scoreboardAdapter.setAuth(authData!!)
                with(rvScoreboard) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = scoreboardAdapter
                }
                fabFindAccount.setOnClickListener {
                    rvScoreboard.smoothScrollToPosition(myPosition)
                }
            }
        }
    }
}