# CarouselView
A carousel widget for Android based on ViewPager2.

## Download
```groovy
dependencies {
    implementation 'io.github.vejei.carouselview:carouselview:1.0.0-alpha'
}
```

## Usage
### Basic
Add `CarouselView` to your layout:
```xml
<io.github.vejei.carouselview.CarouselView
    android:id="@+id/carousel_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:carouselMode="preview"
    app:carouselPreviewSide="sides"
    app:carouselPreviewOffset="30dp"
    app:carouselPreviewSideBySideStyle="scale"
    app:carouselMargin="10dp"
    android:layout_marginTop="8dp"/>
```

Or add it programmatically:
```kotlin
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
```

And setup in your `Fragment` or `Activity`:
```kotlin
val carouselView = view.findViewById<CarouselView>(R.id.carousel_view)

val pageAdapter = PageAdapter()
carouselView.adapter = pageAdapter

// Calling the start method to start carousel by passing in the interval.
carouselView.start(3, TimeUnit.SECONDS)
```

`PageAdapter` example:
```kotlin
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
```

### Setup indicator
If you need to set indicator for `CarouselView`, you can use [ViewPagerIndicator](https://github.com/vejei/ViewPagerIndicator), the example:
```kotlin
val carouselView = view.findViewById<CarouselView>(R.id.carousel_view)
val indicator = view.findViewById<RectIndicator>(R.id.indicator)

val pageAdapter = PageAdapter()

carouselView.adapter = pageAdapter

indicator.setWithViewPager2(carouselView.viewPager2, false)

pageAdapter.setData((activity as MainActivity).adapterData)
indicator.itemCount = pageAdapter.pageCount
```

## Attributes
|Attribute|Description|Type|Example Value|
|---|---|---|---|
|`carouselMode`|Set the carousel view mode. The accepted values include `snap` and `preview`, the default value is `snap`.|`enum`|`snap` or `preview`|
|`carouselPreviewSide`|Set the carousel view preview side.|`enum`|`sides` or `right`|
|`carouselPreviewSideBySideStyle`|Set the side by side preview style.|`enum`|`normal` or `scale`|
|`carouselPreviewScaleFactor`|Set the side by side preview scale factor.|`float`|`0.2`|
|`carouselPreviewOffset`|Set carousel preview offset.|`Dimension`|`30dp`|
|`carouselMargin`|Set carousel item margin.|`Dimension`|`10dp`|

## Screenshots
![screenshot](./screenshots/screenshot.gif)

## Change Log
[Change Log](./CHANGELOG.md)

## License
[MIT](./LICENSE)
