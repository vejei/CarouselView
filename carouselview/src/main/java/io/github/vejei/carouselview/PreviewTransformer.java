package io.github.vejei.carouselview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

abstract class PreviewTransformer implements ViewPager2.PageTransformer {
    int pageMargin;
    int previewOffset;
    ViewPager2 viewPager2;

    PreviewTransformer(int pageMargin, int previewOffset, ViewPager2 viewPager2) {
        this.pageMargin = pageMargin;
        this.previewOffset = previewOffset;
        this.viewPager2 = viewPager2;
    }

    static class RightSideTransformer extends PreviewTransformer {
        RightSideTransformer(int pageMargin, int previewOffset, ViewPager2 viewPager2) {
            super(pageMargin, previewOffset, viewPager2);
        }

        @Override
        public void transformPage(@NonNull View page, float position) {
            float offset = position * -(previewOffset + pageMargin);
            if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                page.setTranslationX(offset);
            }
        }
    }

    static class SideBySideTransformer extends PreviewTransformer {
        SideBySideTransformer(int pageMargin, int previewOffset, ViewPager2 viewPager2) {
            super(pageMargin, previewOffset, viewPager2);
        }

        @Override
        public void transformPage(@NonNull View page, float position) {
            float offset = position * -(previewOffset * 2 + pageMargin);
            if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                page.setTranslationX(offset);
            }
        }
    }

    static class ScaleTransformer extends PreviewTransformer {
        private float scaleFactory;

        ScaleTransformer(float scaleFactory, int pageMargin, int previewOffset, ViewPager2 viewPager2) {
            super(pageMargin, previewOffset, viewPager2);
            this.scaleFactory = scaleFactory;
        }

        @Override
        public void transformPage(@NonNull View page, float position) {
            float offset = position * -(previewOffset * 2 + pageMargin);
            if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                page.setTranslationX(offset);
                page.setScaleY(1 - scaleFactory * Math.abs(position));
            }
        }
    }
}
