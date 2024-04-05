package de.fhandshit.maidmaid.ui.add_product_item;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.databinding.ProductItemBinding;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder> {
    private List<ProductItem> productItems = new ArrayList<>();
    private MyInterface run;

    public void setRun(MyInterface run) {
        this.run = run;
    }

    public void setProductItems(List<ProductItem> productItems){
        final ProductItemDiffCallback diffCallback = new ProductItemDiffCallback(this.productItems,productItems);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
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
         private ProductItemBinding binding;

        public ProductItemViewHolder(ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductItem productItem){
            binding.productItemName.setText(productItem.getProduct().getName());
            if(productItem.getExpiryDate() == null) binding.productItemDate.setVisibility(View.GONE);
            else{
                LocalDate date = productItem.getExpiryDate();
                String text = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                binding.productItemDate.setText(text);
                binding.productItemDate.setOnClickListener(v -> {
                    new DatePickerDialog(binding.getRoot().getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            productItem.setExpiryDate(date.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth));
                            binding.productItemDate.setText(productItem.getExpiryDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                            run.myFun(productItem);
                        }
                    }, date.getYear(), date.getMonthValue(), date.getDayOfMonth()).show();

                });
            }
           // binding.productItemDate.setText();
            /*int color = category.getColor();
            int a = (color >> 24) & 0xff;
            int r = (color >> 16) & 0xff;
            int g = (color >>  8) & 0xff;
            int b = (color      ) & 0xff;
            binding.productItemCategory.setChipBackgroundColor(ColorStateList.valueOf(Color.argb(a,r,g,b)));*/

        }
    }

    public interface MyInterface{
        void myFun(ProductItem productItem);
    }
}

class ProductItemDiffCallback extends DiffUtil.Callback {

    private final List<ProductItem> mOldProductItemList;
    private final List<ProductItem> mNewProductItemList;

    public ProductItemDiffCallback(List<ProductItem> oldProductItemList, List<ProductItem> newProductItemList) {
        this.mOldProductItemList = oldProductItemList;
        this.mNewProductItemList = newProductItemList;
    }

    @Override
    public int getOldListSize() {
        return mOldProductItemList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewProductItemList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldProductItemList.get(oldItemPosition).getProductItemId() == (mNewProductItemList.get(
                newItemPosition).getProductItemId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final ProductItem oldProductItem = mOldProductItemList.get(oldItemPosition);
        final ProductItem newProductItem = mNewProductItemList.get(newItemPosition);

        return oldProductItem.equals(newProductItem);
    }

    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}