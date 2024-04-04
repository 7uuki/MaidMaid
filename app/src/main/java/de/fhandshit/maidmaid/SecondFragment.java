package de.fhandshit.maidmaid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.repository.Repo;
import de.fhandshit.maidmaid.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private boolean ascendingSort;
    private LiveData<List<Product>> productList;
    private Repo repo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) getActivity().getApplication()).getRepo();
        productList = repo.getProducts();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    ProductRecyclerViewAdapter adapter;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productList.observe(getViewLifecycleOwner(), products1 -> {
            adapter.updateList(products1);
        });

        RecyclerView recyclerView = binding.rvAnimals;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new ProductRecyclerViewAdapter(getContext());
        adapter.setItemClickListener(new ProductRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, Product product) {
                productSelectedTransaction(product);
                //Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);


        binding.buttonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromProduct", false);
                NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_ThirdFragment, bundle);
            }
        });

        binding.btnChangeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortRecyclerView(!ascendingSort);
                Log.d("Sorting", "Reversed: ");
            }
        });

    }

    public void productSelectedTransaction(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("id", product.getProductId().toString());
        bundle.putBoolean("fromProduct", true);
        NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_ThirdFragment, bundle);
    }

    public void sortRecyclerView(boolean leastRecent) {
        adapter.sortRecyclerView(leastRecent);
        if (leastRecent) {
            binding.btnChangeSort.setText("ascending");
            binding.btnChangeSort.setIcon( AppCompatResources.getDrawable(getContext(),R.drawable.ic_arrow_up));

            ascendingSort = true;
        } else {
            binding.btnChangeSort.setText("descending");
            binding.btnChangeSort.setIcon( AppCompatResources.getDrawable(getContext(),R.drawable.ic_arrow_down));

            ascendingSort = false;
        }
        //Log.d("list in fragment", Arrays.toString(productList.toArray()));
        adapter.sortRecyclerView(leastRecent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable divider;
        private int orientation;

        public CustomDividerItemDecoration(Context context, int orientation) {
            divider = ContextCompat.getDrawable(context, R.drawable.devider_drawable);
            this.orientation = orientation;
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int dividerLeft, dividerRight, dividerTop, dividerBottom;
            int childCount = parent.getChildCount();
            if (orientation == DividerItemDecoration.VERTICAL) {
                dividerLeft = parent.getPaddingLeft();
                dividerRight = parent.getWidth() - parent.getPaddingRight();
                for (int i = 0; i < childCount - 1; i++) {
                    if (isLastItem(i, parent)) {
                        continue; // Skip drawing divider for last item
                    }
                    View child = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    dividerTop = child.getBottom() + params.bottomMargin;
                    dividerBottom = dividerTop + divider.getIntrinsicHeight();
                    divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                    divider.draw(c);
                }
            } else {
                // Add logic for horizontal orientation if needed
            }
        }

        private boolean isLastItem(int position, RecyclerView parent) {
            return position == parent.getAdapter().getItemCount() - 1;
        }
    }

}