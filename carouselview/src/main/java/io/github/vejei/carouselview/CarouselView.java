package io.github.vejei.carouselview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.Px;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.concurrent.TimeUnit;

public class CarouselView extends ViewGroup {
    private static final String TAG = CarouselView.class.getSimpleName();
    private static final float MAX_SCALE_FACTOR = 0.4f;
    private static final float MIN_SCALE_FACTOR = 0.1f;
    private static final float DEFAULT_SCALE_FACTOR = 0.15f;

    private Mode mode = Mode.SNAP;
    private PreviewSide previewSide = PreviewSide.SIDE_BY_SIDE;
    private PreviewSideBySideStyle sideBySideStyle = PreviewSideBySideStyle.NORMAL;
    private float previewScaleFactor = DEFAULT_SCALE_FACTOR;
    private int previewOffset;
    private int itemMargin;

    private ViewPager2 viewPager2;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback;
    private CarouselAdapter adapter;
    private RecyclerView.AdapterDataObserver dataSetChangeObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (!initialPositionChanged) {
                int firstItemPosition = getFirstItemPosition();
                viewPager2.setCurrentItem(firstItemPosition, false);
                currentPosition = firstItemPosition;
            }
        }
    };
    private boolean initialPositionChanged = false;
    private int currentPosition;
    private Handler handler;
    private Runnable runnable;

    private final Rect containerRect = new Rect();
    private final Rect childRect = new Rect();

    public CarouselView(Context context) {
        super(context);

        removeAllViews();
        setupViewPager();
        attachViewToParent(viewPager2, 0, viewPager2.getLayoutParams());
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CarouselView, 0, 0);
        try {
            mode = Mode.values()[typedArray.getInt(R.styleable.CarouselView_carouselMode, 0)];
            previewSide = PreviewSide.values()[typedArray.getInt(
                    R.styleable.CarouselView_carouselPreviewSide, 0)];
            sideBySideStyle = PreviewSideBySideStyle.values()[typedArray.getInt(
                    R.styleable.CarouselView_carouselPreviewSideBySideStyle, 0)];
            previewScaleFactor = typedArray.getFloat(R.styleable.CarouselView_carouselPreviewScaleFactor, DEFAULT_SCALE_FACTOR);
            previewOffset = typedArray.getDimensionPixelSize(
                    R.styleable.CarouselView_carouselPreviewOffset, 0);
            itemMargin = typedArray.getDimensionPixelSize(
                    R.styleable.CarouselView_carouselMargin, 0);
        } finally {
            typedArray.recycle();
        }
        if (previewScaleFactor < MIN_SCALE_FACTOR) {
            previewScaleFactor = MIN_SCALE_FACTOR;
        } else if (previewScaleFactor > MAX_SCALE_FACTOR) {
            previewScaleFactor = MAX_SCALE_FACTOR;
        }

        removeAllViews();
        setupViewPager();
        attachViewToParent(viewPager2, 0, viewPager2.getLayoutParams());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = viewPager2.getMeasuredWidth();
        int height = viewPager2.getMeasuredHeight();

        containerRect.left = getPaddingLeft();
        containerRect.right = r - l - getPaddingRight();
        containerRect.top = getPaddingTop();
        containerRect.bottom = b - t - getPaddingBottom();

        Gravity.apply(Gravity.TOP | Gravity.START, width, height, containerRect, childRect);

        viewPager2.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(viewPager2, widthMeasureSpec, heightMeasureSpec);
        int width = viewPager2.getMeasuredWidth();
        int height = viewPager2.getMeasuredHeight();
        int childState = viewPager2.getScrollState();

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    private void setupViewPager() {
        if (viewPager2 == null) {
            viewPager2 = new ViewPager2(getContext());
        }
        viewPager2.setLayoutParams(
                new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        );
        viewPager2.setOffscreenPageLimit(2);
        if (onPageChangeCallback == null) {
            onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        if (adapter == null || adapter.getPageCount() <= 0) {
                            currentPosition = 0;
                        } else {
                            currentPosition = viewPager2.getCurrentItem();
                        }
                    }
                }
            };
        }
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback);

        viewPager2.setPageTransformer(null);
        removeItemDecorations();

        PreviewTransformer previewTransformer = null;
        if (mode == Mode.PREVIEW) {
            if (previewSide == PreviewSide.RIGHT) {
                previewTransformer = new PreviewTransformer.RightSideTransformer(
                        itemMargin, previewOffset, viewPager2
                );
                viewPager2.addItemDecoration(new MarginDecoration(itemMargin,
                        itemMargin + previewOffset));
                viewPager2.setPageTransformer(previewTransformer);
            } else {
                switch (sideBySideStyle) {
                    case NORMAL:
                        // The currently selected item remain at normal size
                        previewTransformer = new PreviewTransformer.SideBySideTransformer(
                                itemMargin, previewOffset, viewPager2
                        );
                        break;
                    case SCALE:
                        // scale currently selected item
                        previewTransformer = new PreviewTransformer.ScaleTransformer(
                                previewScaleFactor, itemMargin, previewOffset, viewPager2
                        );
                        break;
                }
                // Add margin for items
                viewPager2.addItemDecoration(
                        new MarginDecoration(
                                itemMargin + previewOffset,
                                itemMargin + previewOffset
                        ));
                viewPager2.setPageTransformer(previewTransformer);
            }
        } else {
            previewSide = PreviewSide.RIGHT;
            sideBySideStyle = PreviewSideBySideStyle.NORMAL;
        }
    }

    private void removeItemDecorations() {
        int itemDecorationCount = viewPager2.getItemDecorationCount();
        if (itemDecorationCount > 0) {
            for (int i = 0; i < itemDecorationCount; i++) {
                viewPager2.removeItemDecorationAt(i);
            }
        }
    }

    public void setAdapter(CarouselAdapter adapter) {
        if (adapter != null) {
            this.adapter = adapter;
            this.adapter.registerAdapterDataObserver(dataSetChangeObserver);
            this.viewPager2.setAdapter(this.adapter);

            int firstItemPosition = getFirstItemPosition();
            initialPositionChanged = firstItemPosition != 0;
            if (firstItemPosition != 0) {
                this.viewPager2.setCurrentItem(firstItemPosition, false);
                currentPosition = firstItemPosition;
            }
        } else {
            throw new NullPointerException("Adapter must not be null.");
        }
    }

    public CarouselAdapter getAdapter() {
        return this.adapter;
    }

    private int getFirstItemPosition() {
        if (adapter == null || adapter.getPageCount() == 0) return 0;
        int position = Integer.MAX_VALUE / 2;
        while (position % adapter.getPageCount() != 0) {
            position = position + 1;
        }
        return position;
    }

    public void start(long interval, TimeUnit unit) {
        final long actualInterval = getActualInterval(interval, unit);
        if (handler == null) {
            handler = new Handler();
        }
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    viewPager2.setCurrentItem(++currentPosition);
                    handler.postDelayed(this, actualInterval);
                }
            };
        }
        handler.postDelayed(runnable, actualInterval);
    }

    private long getActualInterval(long interval, TimeUnit unit) {
        return TimeUnit.MILLISECONDS.convert(interval, unit);
    }

    public void stop() {
        if (handler == null || runnable == null) return;
        handler.removeCallbacks(runnable);
    }

    public void release() {
        stop();
        if (viewPager2 != null) {
            viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
            viewPager2 = null;
        }
        if (dataSetChangeObserver != null) {
            this.adapter.unregisterAdapterDataObserver(this.dataSetChangeObserver);
            this.dataSetChangeObserver = null;
            this.adapter = null;
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        setupViewPager();
    }

    public PreviewSide getPreviewSide() {
        return previewSide;
    }

    public void setPreviewSide(PreviewSide previewSide) {
        this.previewSide = previewSide;
        setupViewPager();
    }

    public PreviewSideBySideStyle getSideBySideStyle() {
        return sideBySideStyle;
    }

    public void setSideBySideStyle(PreviewSideBySideStyle sideBySideStyle) {
        this.sideBySideStyle = sideBySideStyle;
        setupViewPager();
    }

    public float getPreviewScaleFactor() {
        return previewScaleFactor;
    }

    public void setPreviewScaleFactor(float previewScaleFactor) {
        this.previewScaleFactor = previewScaleFactor;
        if (this.previewScaleFactor < MIN_SCALE_FACTOR) {
            this.previewScaleFactor = MIN_SCALE_FACTOR;
        } else if (previewScaleFactor > MAX_SCALE_FACTOR) {
            this.previewScaleFactor = MAX_SCALE_FACTOR;
        }
        setupViewPager();
    }

    @Px
    public int getPreviewOffset() {
        return previewOffset;
    }

    public void setPreviewOffset(@Px int previewOffset) {
        this.previewOffset = previewOffset;
        setupViewPager();
    }

    public int getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(@Px int itemMargin) {
        this.itemMargin = itemMargin;
        setupViewPager();
    }

    public ViewPager2 getViewPager2() {
        return this.viewPager2;
    }

    public enum Mode {
        SNAP, PREVIEW
    }

    public enum PreviewSide {
        SIDE_BY_SIDE, RIGHT
    }

    public enum PreviewSideBySideStyle {
        NORMAL, SCALE
    }
}
