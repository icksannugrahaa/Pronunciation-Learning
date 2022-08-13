package com.sh.prolearn.app.modules

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sh.prolearn.core.domain.model.Comment
import com.sh.prolearn.core.domain.model.Lesson
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_DATA
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_PROGRESS
import com.sh.prolearn.core.utils.Consts.ARG_REVIEW_DATA
import java.io.Serializable

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var lessonData = ArrayList<Lesson>()
    private var lessonProgress = ArrayList<Progress>()
    private var reviewData = ArrayList<Comment>()
    private var lessonCode = "null"

    fun setData(lesson: List<Lesson>, progress: List<Progress>, lessonCode: String, review: List<Comment>) {
        lessonData.clear()
        lessonData.addAll(lesson)
        lessonProgress.clear()
        lessonProgress.addAll(progress)
        this.lessonCode = lessonCode
        reviewData.clear()
        reviewData.addAll(review)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = MaterialFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable(ARG_LESSON_DATA, lessonData as Serializable)
                    putSerializable(ARG_LESSON_PROGRESS, lessonProgress as Serializable)
                    putString(ARG_LESSON_CODE, lessonCode)
                }

            }
            1 -> {
                fragment = CommentFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable(ARG_REVIEW_DATA, reviewData as Serializable)
                    putString(ARG_LESSON_CODE, lessonCode)
                }
            }
        }
        return fragment as Fragment
    }
}