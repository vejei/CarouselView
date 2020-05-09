package io.github.vejei.carouselview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public final class MarginDecoration extends RecyclerView.ItemDecoration {
    private int marginLeft;
    private int marginRight;

    MarginDecoration(int marginLeft, int marginRight) {
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = marginLeft;
        outRect.right = marginRight;
    }
}
