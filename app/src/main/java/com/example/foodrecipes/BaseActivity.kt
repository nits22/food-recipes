package com.example.foodrecipes

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mProgressBar: ProgressBar

    override fun setContentView(layoutResID: Int) {

        var constraintLayout =
            layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout
        var frameLayout = constraintLayout.findViewById<FrameLayout>(R.id.activity_content)

        mProgressBar = constraintLayout.findViewById(R.id.progress_bar)

        layoutInflater.inflate(layoutResID, frameLayout, true)

        super.setContentView(constraintLayout)
    }

    fun showProgressBar(visibility: Boolean) {
        mProgressBar.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }
}