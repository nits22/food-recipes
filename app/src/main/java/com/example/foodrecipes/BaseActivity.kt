package com.example.foodrecipes

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import com.example.foodrecipes.Repository.NetworkState
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mProgressBar: ProgressBar

    lateinit var customProgressBar : HorizontalDottedProgress

    override fun setContentView(layoutResID: Int) {

        var constraintLayout =
            layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout
        var frameLayout = constraintLayout.findViewById<FrameLayout>(R.id.activity_content)

        mProgressBar = constraintLayout.findViewById(R.id.progress_bar)
        customProgressBar = constraintLayout.findViewById(R.id.custom_progress)

        layoutInflater.inflate(layoutResID, frameLayout, true)

        super.setContentView(constraintLayout)
    }

    fun showProgressBar(visibility: Boolean) {
        //mProgressBar.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
        customProgressBar.visibility = if(visibility) View.VISIBLE else View.GONE

    }

    fun showErrorMessage(visibility : Boolean, msg : String?){
        if(msg != null){
            txt_error.text = msg
        }
        txt_error.visibility = if (visibility) View.VISIBLE else View.GONE

    }
}