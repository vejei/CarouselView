package io.github.vejei.carouselview.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    lateinit var adapterData: MutableList<PageAdapter.Page>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, MainFragment())
                .commit()
        }
        adapterData = mutableListOf(
            PageAdapter.Page(R.drawable.illustration_00, "First"),
            PageAdapter.Page(R.drawable.illustration_01, "Second"),
            PageAdapter.Page(R.drawable.illustration_02, "Third"),
            PageAdapter.Page(R.drawable.illustration_03, "Fourth"),
            PageAdapter.Page(R.drawable.illustration_04, "Fifth")
        )
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
