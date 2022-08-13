package com.sh.prolearn.app.modules

import android.os.Bundle
import android.text.method.KeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sh.prolearn.R
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Comment
import com.sh.prolearn.core.ui.ReviewListAdapter
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.DialogUtils
import com.sh.prolearn.core.utils.ToastUtils
import com.sh.prolearn.databinding.FragmentCommentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommentFragment : Fragment() {
    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModuleViewModel by viewModel()
    private lateinit var myDialog: DialogUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            myDialog = DialogUtils()
            var authData = AuthPreferences(requireContext()).authData
            val reviewData = arguments?.getSerializable(Consts.ARG_REVIEW_DATA) as List<Comment>
            val reviewAdapter = ReviewListAdapter()
            reviewAdapter.setData(reviewData)
            with(binding.rvReview) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = reviewAdapter
            }
            binding.fabAddReview.setOnClickListener {
                val myReview = reviewData.filter {
                    it.name == authData?.name
                }
                showReviewDialog(myReview)
            }
        }
    }

    private fun showReviewDialog(review: List<Comment>) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.dialog_review)
        if(review.isNotEmpty()) {
            bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.setIsIndicator(true)
            bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.isFocusable = false
            bottomSheetDialog.findViewById<EditText>(R.id.et_review)?.isEnabled = false
            bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.rating = review[0].rating?.toFloat()!!
            bottomSheetDialog.findViewById<EditText>(R.id.et_review)?.setText(review[0].comment!!)
            bottomSheetDialog.findViewById<Button>(R.id.btn_submit)?.setText(R.string.change_review)
        } else {
            val editTextReview = bottomSheetDialog.findViewById<EditText>(R.id.et_review)
            bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.setIsIndicator(false)
            bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.isFocusable = true
            bottomSheetDialog.findViewById<EditText>(R.id.et_review)?.isEnabled = true
            bottomSheetDialog.findViewById<Button>(R.id.btn_submit)?.setText(R.string.submit)
        }

        bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.setOnRatingBarChangeListener { ratingBar, fl, b ->
            ratingBar.rating = fl
        }
        bottomSheetDialog.findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.findViewById<Button>(R.id.btn_submit)?.setOnClickListener {
            val review = bottomSheetDialog.findViewById<EditText>(R.id.et_review)?.text
            val rate = bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.rating
            val lessonCode = arguments?.getString(ARG_LESSON_CODE)
            var authToken = AuthPreferences(requireContext()).authToken

            if (bottomSheetDialog.findViewById<Button>(R.id.btn_submit)?.text == "Ubah") {
                val editTextReview = bottomSheetDialog.findViewById<EditText>(R.id.et_review)
                bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.setIsIndicator(false)
                bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.isFocusable = true
                bottomSheetDialog.findViewById<EditText>(R.id.et_review)?.isEnabled = true
                bottomSheetDialog.findViewById<Button>(R.id.btn_submit)?.setText(R.string.submit)
            } else {
                val livedata = viewModel.moduleReview(
                    authToken,"$lessonCode-1", review.toString(), rate.toString()
                )
                livedata.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        when (data) {
                            is Resource.Loading<*> -> {
                                myDialog.setCustomDialog(requireContext(), R.layout.wait_dialog, false)
                                myDialog.showCustomDialog(true)
                            }
                            is Resource.Success<*> -> {
                                livedata.removeObservers(viewLifecycleOwner)
                                ToastUtils.showToast(data.message.toString(), requireContext())
                                myDialog.showCustomDialog(false)
                                bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.setIsIndicator(true)
                                bottomSheetDialog.findViewById<RatingBar>(R.id.rb_review)?.isFocusable = false
                                bottomSheetDialog.findViewById<EditText>(R.id.et_review)?.isEnabled = false
                                bottomSheetDialog.findViewById<Button>(R.id.btn_submit)?.text = getString(R.string.change_review)
                            }
                            is Resource.Error<*> -> {
                                myDialog.showCustomDialog(false)
                                ToastUtils.showToast(data.message.toString(), requireContext())
                                livedata.removeObservers(viewLifecycleOwner)
                            }
                        }
                    }
                }
            }
        }
        bottomSheetDialog.show()
    }
}