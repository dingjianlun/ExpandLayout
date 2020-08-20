package com.dingjianlun.expandlayout.demo

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.view.*

class ExpandLayout(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var minHeight = -1
    private var maxHeight = -1

    init {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (minHeight >= 0 && maxHeight >= 0) return

        tv_text.text = (0..100).joinToString()
        btn_click.visibility = View.VISIBLE
        tv_text.visibility = View.GONE

        btn_click.setOnClickListener {
            expandAnim(tv_text, !tv_text.isVisible)
        }

        doOnLayout { view ->
            minHeight = view.height
            tv_text.visibility = View.VISIBLE
            doOnPreDraw {
                maxHeight = view.height
                tv_text.visibility = View.GONE
            }
        }

    }

    private fun expandAnim(view: View, expand: Boolean) {
        val animator = if (expand) {
            ValueAnimator.ofFloat(0f, 1f)
        } else {
            ValueAnimator.ofFloat(1f, 0f)
        }
        animator.duration = 250
        animator.addUpdateListener {
            val progress = it.animatedValue as Float
            layoutParams.height = (minHeight + (maxHeight - minHeight) * progress).toInt()
            requestLayout()
        }

        if (expand) {
            animator.doOnStart { view.isVisible = true }
        } else {
            animator.doOnEnd { view.isVisible = false }
        }

        animator.start()
    }

}