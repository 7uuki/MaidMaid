package de.fhandshit.maidmaid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
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
            binding.textSorted.setText("ascending");
            binding.btnChangeSort.setImageResource(R.drawable.ic_arrow_up);
            ascendingSort = true;
        } else {
            binding.textSorted.setText("descending");
            binding.btnChangeSort.setImageResource(R.drawable.ic_arrow_down);
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

}