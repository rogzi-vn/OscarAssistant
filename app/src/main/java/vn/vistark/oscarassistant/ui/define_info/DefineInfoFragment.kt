package vn.vistark.oscarassistant.ui.define_info

import WikipediaDefineResponse
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import vn.vistark.oscarassistant.R
import vn.vistark.oscarassistant.core.models.wikipedia.NumberResponse
import vn.vistark.oscarassistant.utils.GlideLoadImage

class DefineInfoFragment : Fragment() {

    companion object {
        const val FULL_CONTENT = "FULL_CONTENT"
        const val OBJECT_NAME = "OBJECT_NAME"
    }

    lateinit var ivObjectImage: ImageView
    lateinit var tvContent: TextView

    var fullContent = ""
    var objectName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fullContent = arguments?.getString(FULL_CONTENT) ?: ""
        objectName = arguments?.getString(OBJECT_NAME) ?: ""
        val v = inflater.inflate(R.layout.fragment_define_info, container, false)
        initViews(v)
        tvContent.text = fullContent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvContent.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        loadImage(v)
        return v
    }

    private fun loadImage(v: View) {
        APIUtils.mAPIServices?.getWikiDefineThumbnail(objectName)
            ?.enqueue(object : Callback<WikipediaDefineResponse> {
                override fun onFailure(call: Call<WikipediaDefineResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<WikipediaDefineResponse>,
                    response: Response<WikipediaDefineResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            val wdr = response.body()!!
                            val nr =
                                NumberResponse.extract(wdr.query.pages.entrySet().first().value)
                            if (nr.thumbnail != null && nr.thumbnail!!.source.isNotEmpty()) {
                                GlideLoadImage.load(
                                    ivObjectImage,
                                    R.drawable.facebook_cover_photo_2,
                                    nr.thumbnail!!.source
                                )
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun initViews(v: View) {
        tvContent = v.findViewById(R.id.tvContent)
        ivObjectImage = v.findViewById(R.id.ivObjectImage)
    }

}
