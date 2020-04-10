package vn.vistark.oscarassistant.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import vn.vistark.oscarassistant.R

class GlideLoadImage {
    companion object {
        fun load(v: ImageView, placeholderResource: Int, url: String) {
            if ("(.+)\\.gif$".toRegex().matches(url)) {
                Glide.with(v).asGif()
                    .placeholder(placeholderResource)
                    .load(url)
                    .into(v)
            } else if ("(.+)\\.webp$".toRegex().matches(url)) {
                val circleCrop: Transformation<Bitmap> = CircleCrop()
                Glide.with(v)
                    .load(url)
                    .placeholder(placeholderResource)
                    .optionalTransform(circleCrop)
                    .optionalTransform(
                        WebpDrawable::class.java,
                        WebpDrawableTransformation(circleCrop)
                    )
                    .into(v)
            } else {
                Glide.with(v)
                    .load(url)
                    .placeholder(placeholderResource)
                    .into(v)
            }
        }
    }
}