package com.sh.prolearn.app.lesson

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sh.prolearn.R
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.ScoreBoard
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.DEFAULT_USER_IMAGE
import com.sh.prolearn.core.utils.GeneralUtils
import com.sh.prolearn.core.utils.GeneralUtils.getTimeFromInt
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.databinding.FragmentResultMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultMainFragment : Fragment() {
    private var _binding: FragmentResultMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModuleViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultMainBinding.inflate(inflater, container, false)
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
        val lessonCode = arguments?.getString(ARG_LESSON_CODE)

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
                                error = false
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
            val thirdData = ArrayList<ScoreBoard>()
            var currentScore = ScoreBoard()
            var thirdDataStatus = false
            var currentScoreStatus = false
            var position = 0
            if(scoreboards.isNotEmpty()) {
                for (i in scoreboards.indices) {
                    if(i <= 2) {
                        thirdData.add(scoreboards[i])
                    } else {
                        thirdDataStatus = true
                    }
                    if(authData?.email == scoreboards[i].email) {
                        currentScore = scoreboards[i]
                        position = i+1
                        currentScoreStatus = true
                    }
                    if(currentScoreStatus && thirdDataStatus) {
                        currentScoreStatus = false
                        thirdDataStatus = false
                        break
                    }
                }
            }


            with(binding) {
                civWinner.loadImageFromServer(thirdData[0].avatar)
                tv1stWinner.text = thirdData[0].name
                if(thirdData.size > 1) {
                    civ2ndWinner.loadImageFromServer(thirdData[1].avatar)
                    tv2ndWinner.text = thirdData[1].name
                    if(thirdData.size > 2) {
                        civ3rdWinner.loadImageFromServer(thirdData[2].avatar)
                        tv3rdWinner.text = thirdData[2].name
                    } else {
                        civ3rdWinner.loadImageFromServer(DEFAULT_USER_IMAGE)
                        tv3rdWinner.text = "?"
                    }
                } else {
                    civ2ndWinner.loadImageFromServer(DEFAULT_USER_IMAGE)
                    tv2ndWinner.text = "?"
                    civ3rdWinner.loadImageFromServer(DEFAULT_USER_IMAGE)
                    tv3rdWinner.text = "?"
                }

                tvScoreTotal.text = String.format("%.1f", currentScore.score)
                tvTimeSpent.text = getTimeFromInt(currentScore.time!!)
                tvPosition.text = "$position dari ${scoreboards.size}"
            }
        }
    }
}