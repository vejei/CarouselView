package io.github.vejei.carouselview.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.vejei.carouselview.CarouselView
import java.util.concurrent.TimeUnit

class LayoutSamplesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layout_samples, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hostActivity = activity as MainActivity

        val defaultCarouselView = view.findViewById<CarouselView>(
            R.id.carousel_view_default)
        val rightSidePreviewCarouselView = view.findViewById<CarouselView>(
            R.id.carousel_view_right_side_preview)
        val sideBySidePreviewCarouselView = view.findViewById<CarouselView>(
            R.id.carousel_view_side_by_side_preview)
        val sideBySidePreviewScaleCarouselView = view.findViewById<CarouselView>(
            R.id.carousel_view_side_by_side_preview_scale)

        val pageAdapter = PageAdapter().apply {
            setData(hostActivity.adapterData)
        }
        defaultCarouselView.adapter = pageAdapter

        rightSidePreviewCarouselView.adapter = pageAdapter
        sideBySidePreviewCarouselView.adapter = pageAdapter
        sideBySidePreviewScaleCarouselView.adapter = pageAdapter

        defaultCarouselView.start(3, TimeUnit.SECONDS)

        pageAdapter.setData(hostActivity.adapterData)
    }
}
