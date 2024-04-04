package de.fhandshit.maidmaid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        Log.d("list in adapter", Arrays.toString(productList.toArray()));
        this.productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    public void sortRecyclerView(boolean leastRecent) {
        if (leastRecent) productList.sort(Comparator.comparing(Product::getLastAdd).reversed());
        else productList.sort(Comparator.comparing(Product::getLastAdd));
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myTextView;
        private Product product;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvProductName);
            itemView.setOnClickListener(this);
        }

        void bind(Product product) {
            this.product = product;
            myTextView.setText(product.getName());
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
