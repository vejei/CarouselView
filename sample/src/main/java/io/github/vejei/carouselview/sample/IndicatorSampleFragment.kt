package io.github.vejei.carouselview.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.vejei.carouselview.CarouselView
import io.github.vejei.viewpagerindicator.indicator.RectIndicator

class IndicatorSampleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_indicator_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carouselView = view.findViewById<CarouselView>(R.id.carousel_view)
        val indicator = view.findViewById<RectIndicator>(R.id.indicator)

        val pageAdapter = PageAdapter()

        carouselView.adapter = pageAdapter

        indicator.setWithViewPager2(carouselView.viewPager2, false)

        pageAdapter.setData((activity as MainActivity).adapterData)
        indicator.itemCount = pageAdapter.pageCount
    }
}