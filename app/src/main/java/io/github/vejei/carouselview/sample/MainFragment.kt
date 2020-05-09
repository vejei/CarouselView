package io.github.vejei.carouselview.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hostActivity = activity as MainActivity

        val layoutSamplesButton = view.findViewById<Button>(R.id.button_layout_samples)
        val programmaticallySampleButton = view.findViewById<Button>(R.id.button_programmatically_sample)
        val indicatorSampleButton = view.findViewById<Button>(R.id.button_indicator_sample)

        layoutSamplesButton.setOnClickListener {
            hostActivity.addFragment(LayoutSamplesFragment())
        }
        programmaticallySampleButton.setOnClickListener {
            hostActivity.addFragment(ProgrammaticallySampleFragment())
        }
        indicatorSampleButton.setOnClickListener {
            hostActivity.addFragment(IndicatorSampleFragment())
        }
    }
}
