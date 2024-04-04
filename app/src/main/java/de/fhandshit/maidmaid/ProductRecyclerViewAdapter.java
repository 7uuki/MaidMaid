package de.fhandshit.maidmaid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.fhandshit.maidmaid.data.model.Product;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private List<Product> productList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    ProductRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        productList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_recylcer_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> productList) {
        final ProductDiffCallback diffCallback = new ProductDiffCallback(this.productList, productList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.productList.clear();
        this.productList.addAll(productList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void sortRecyclerView(boolean leastRecent) {
        List<Product> products = new ArrayList<>(productList);
        if (leastRecent) products.sort(Comparator.comparing(Product::getLastAdd).reversed());
        else products.sort(Comparator.comparing(Product::getLastAdd));
        updateList(products);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myTextView;
        private TextView categoryTextview;
        private Product product;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvProductName);
            categoryTextview = itemView.findViewById(R.id.vt_product_item_category);
            itemView.setOnClickListener(this);
        }

        void bind(Product product) {
            this.product = product;
            myTextView.setText(product.getName());
            categoryTextview.setText(product.getCategory());
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, product);
        }
    }

    void setItemClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void onItemClick(View view, Product product);
    }
}



class ProductDiffCallback extends DiffUtil.Callback {

    private final List<Product> mOldProductList;
    private final List<Product> mNewProductList;

    public ProductDiffCallback(List<Product> oldProductList, List<Product> newProductList) {
        this.mOldProductList = oldProductList;
        this.mNewProductList = newProductList;
    }

    @Override
    public int getOldListSize() {
        return mOldProductList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewProductList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldProductList.get(oldItemPosition).getProductId().equals(mNewProductList.get(
                newItemPosition).getProductId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Product oldProduct = mOldProductList.get(oldItemPosition);
        final Product newProduct = mNewProductList.get(newItemPosition);

        return oldProduct.getName().equals(newProduct.getName());
    }

    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}