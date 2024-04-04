package de.fhandshit.maidmaid.ui.add_product_item;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.databinding.ProductItemBinding;

public class AddProductItemAdapter extends RecyclerView.Adapter<AddProductItemAdapter.ProductItemViewHolder> {
    private List<ProductItem> productItems;

    public void setProductItems(List<ProductItem> productItems){
        this.productItems.clear();
        this.productItems.addAll(productItems);
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemBinding binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        holder.bind(productItems.get(position));
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ProductItemBinding binding;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ProductItemViewHolder(ProductItemBinding binding) {
            super(binding.getRoot());
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            this.binding = binding;
        }

        public void bind(ProductItem productItem){

            /*int color = category.getColor();
            int a = (color >> 24) & 0xff;
            int r = (color >> 16) & 0xff;
            int g = (color >>  8) & 0xff;
            int b = (color      ) & 0xff;
            binding.productItemCategory.setChipBackgroundColor(ColorStateList.valueOf(Color.argb(a,r,g,b)));*/

        }


    }
}