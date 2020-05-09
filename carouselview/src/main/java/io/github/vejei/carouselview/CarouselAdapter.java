package io.github.vejei.carouselview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CarouselAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreatePageViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        onBindPageViewHolder(holder, position % getPageCount());
    }

    @Override
    public final int getItemCount() {
        return getPageCount() > 1 ? Integer.MAX_VALUE : getPageCount();
    }

    public abstract VH onCreatePageViewHolder(@NonNull ViewGroup parent, int viewType);
    public abstract void onBindPageViewHolder(@NonNull VH holder, int position);
    public abstract int getPageCount();
}
