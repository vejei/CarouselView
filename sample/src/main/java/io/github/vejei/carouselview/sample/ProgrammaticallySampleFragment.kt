package io.github.vejei.carouselview.sample

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import io.github.vejei.carouselview.CarouselView

class ProgrammaticallySampleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_programmatically_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton = view.findViewById<Button>(R.id.button_add_carousel)

        val offset = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 30f, context?.resources?.displayMetrics
        ).toInt()
        val pageMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 10f, context?.resources?.displayMetrics
        ).toInt()

        addButton.setOnClickListener {
            val carouselView = CarouselView(context).apply {
                mode = CarouselView.Mode.PREVIEW
                previewSide = CarouselView.PreviewSide.SIDE_BY_SIDE
                sideBySideStyle = CarouselView.PreviewSideBySideStyle.SCALE
                previewOffset = offset
                itemMargin = pageMargin
            }
            carouselView.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


            carouselView.adapter = PageAdapter().apply {
                setData((activity as MainActivity).adapterData)
            }

            view.findViewById<LinearLayout>(R.id.container).addView(carouselView)
        }
    }
}