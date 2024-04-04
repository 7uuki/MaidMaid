package de.fhandshit.maidmaid.ui.categoryView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.fhandshit.maidmaid.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView categoryNameTextView;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryNameTextView = itemView.findViewById(R.id.category_name_textview);
    }
}
