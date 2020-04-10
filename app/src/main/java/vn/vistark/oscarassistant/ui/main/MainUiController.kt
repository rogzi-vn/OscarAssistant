package vn.vistark.oscarassistant.ui.main

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.activity_main.*
import vn.vistark.oscarassistant.R
import java.util.*


class MainUiController(val context: MainActivity) {
    val defaultAnimationTime = 300L

    init {
        context.userMessage.isSelected = true

        val circleCrop: Transformation<Bitmap> = CircleCrop()
        Glide.with(context)
            .load(R.raw.idle)
            .optionalTransform(circleCrop)
            .optionalTransform(WebpDrawable::class.java, WebpDrawableTransformation(circleCrop))
            .into(context.oscarIdle)

        Glide.with(context)
            .load(R.raw.loading)
            .optionalTransform(circleCrop)
            .optionalTransform(WebpDrawable::class.java, WebpDrawableTransformation(circleCrop))
            .into(context.oscarListening)

        context.oscarBottomsheet.visibility = View.GONE
        context.oscarIdle.visibility = View.GONE

        hideBottomSheet()

        context.rlRootLayout.setOnClickListener {
            if (context.oscarBottomsheet.visibility == View.VISIBLE) {
                hideBottomSheet(true)
            } else {
                hideOscarIdle(true)
            }

            // Exit application
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    context.rlRootLayout.background = ColorDrawable(Color.TRANSPARENT)
                    SystemClock.sleep(100)
                    context.finish()
                }
            }, 500)
        }
    }

    fun updateFrame(frg: Fragment) {
        val transaction = context.supportFragmentManager.beginTransaction()
        transaction.replace(context.contentForShowing.id, frg)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun updateOscarResponse(msg: String) {
        context.oscarResponse.text = msg
    }

    fun showBottomSheet() {
        context.oscarBottomsheet.visibility = View.VISIBLE
        val slide_up = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_up
        )
        slide_up.duration = defaultAnimationTime
        context.oscarBottomsheet.startAnimation(slide_up)
    }

    fun hideBottomSheet(isExit: Boolean = false) {
        val slide_down = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_down
        )
        slide_down.duration = defaultAnimationTime
        slide_down.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                context.oscarBottomsheet.visibility = View.GONE
                if (!isExit) {
                    showOscarIdle()
                }
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        context.oscarBottomsheet.startAnimation(slide_down)
    }

    fun showOscarIdle() {
        context.oscarIdle.visibility = View.VISIBLE
        val slide_up = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_up
        )
        slide_up.duration = defaultAnimationTime
        context.oscarIdle.startAnimation(slide_up)
    }

    fun hideOscarIdle(isExit: Boolean = false) {
        val slide_down = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_down
        )
        slide_down.duration = defaultAnimationTime
        slide_down.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                context.oscarIdle.visibility = View.GONE
                if (!isExit) {
                    showBottomSheet()
                }
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        context.oscarIdle.startAnimation(slide_down)
    }
}