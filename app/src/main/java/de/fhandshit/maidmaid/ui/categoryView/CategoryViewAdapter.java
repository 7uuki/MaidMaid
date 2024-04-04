package de.fhandshit.maidmaid.ui.categoryView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.fhandshit.maidmaid.R;
import de.fhandshit.maidmaid.data.model.Category;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Category> categories;

    public CategoryViewAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_listitem, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryNameTextView.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}