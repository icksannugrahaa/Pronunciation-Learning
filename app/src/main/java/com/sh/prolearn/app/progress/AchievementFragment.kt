package com.sh.prolearn.app.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh.prolearn.R
import com.sh.prolearn.app.authentication.AuthenticationViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Achievement
import com.sh.prolearn.core.ui.AchievementListAdapter
import com.sh.prolearn.databinding.FragmentAchievementBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AchievementFragment : Fragment() {
    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val refreshLayout = binding.swipeToRefreshLayout
            refreshLayout.setColorSchemeColors(
                R.color.green_200,
                R.color.purple_200,
                R.color.teal_200,
                R.color.red_200
            )
            getAchievement()
            refreshLayout.setOnRefreshListener {
                getAchievement()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun getAchievement() {
        val token = AuthPreferences(requireContext()).authToken
        val livedata = viewModel.achievementIndex(token)
        livedata.observe(viewLifecycleOwner) { achievement ->
            if (achievement != null) {
                when (achievement) {
                    is Resource.Loading<*> -> {
                        setViewCondition(shimer = true, error = false)
                    }
                    is Resource.Success<*> -> {
                        setView(achievement.data!!)
                        setViewCondition(shimer = false, error = false)
                        livedata.removeObservers(viewLifecycleOwner)
                    }
                    is Resource.Error<*> -> {
                        val message = achievement.message.toString()
                        if (message.contains("401", ignoreCase = true)) {
                            AuthPreferences(requireContext()).saveAuthData(
                                null
                            )
                        }
                        setViewCondition(shimer = false, error = true)
                        livedata.removeObservers(viewLifecycleOwner)
                    }
                }
            }
        }
    }

    private fun setView(data: List<Achievement>) {
        val authData = AuthPreferences(requireContext()).authData
        with(binding) {
            var totalTrophy = 0
            var totalTrophyArchived = 0
            data.forEach {
                it.level?.forEach { alevel ->
                    totalTrophy += 1
                    authData?.achievement?.forEach { ulevel ->
                        if(it.name == ulevel?.name) {
                            ulevel?.level?.forEach { level ->
                                if (alevel?.level == level?.level) {
                                    totalTrophyArchived += 1
                                }
                            }
                        }
                    }
                }
            }
            tvThropy.text = "$totalTrophyArchived/$totalTrophy"
            val achievementAdapter = AchievementListAdapter()
            achievementAdapter.setUser(authData!!)
            achievementAdapter.setData(data)
            with(rvAchievement) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = achievementAdapter
            }
        }
    }

    private fun setViewCondition(
        shimer: Boolean,
        error: Boolean,
    ) {
        with(binding) {
            shimerProgress.root.visibility = if (shimer) View.VISIBLE else View.GONE
            viewError.root.visibility = if (error) View.VISIBLE else View.GONE
        }
    }
}