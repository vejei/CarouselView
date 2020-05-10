package io.github.vejei.carouselview.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.vejei.carouselview.CarouselAdapter

class PageAdapter : CarouselAdapter<PageAdapter.ViewHolder>() {
    private var data = mutableListOf<Page>()

    fun setData(data: MutableList<Page>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    data class Page(val imageRes: Int, val content: String)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val backgroundImageView = itemView.findViewById<ImageView>(R.id.view_background)
        private val contentTextView = itemView.findViewById<TextView>(R.id.text_view_content)

        fun bind(page: Page) {
            backgroundImageView.clipToOutline = true
            backgroundImageView.setImageResource(page.imageRes)
            contentTextView.text = page.content
        }
    }

    override fun onCreatePageViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        )
    }

    override fun onBindPageViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getPageCount(): Int {
        return data.size
    }
}