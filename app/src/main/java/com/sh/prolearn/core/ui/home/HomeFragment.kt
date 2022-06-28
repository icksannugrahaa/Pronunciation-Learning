package com.sh.prolearn.core.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sh.prolearn.core.ui.modules.ModuleActivity
import com.sh.prolearn.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
//            val factory = ViewModelFactory.getInstance(requireContext())
//            viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

            binding.btnTry.setOnClickListener {
                Intent(requireContext(), ModuleActivity::class.java).apply {
                    startActivity(this)
                }
            }
            binding.btnLogin.setOnClickListener {
//                Intent(requireContext(), LoginActivity::class.java).apply {
//                    startActivity(this)
//                }
            }

        }
    }
}