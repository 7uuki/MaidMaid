package de.fhandshit.maidmaid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private boolean ascendingSort;
    private ArrayList<Product> productList;

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
        productList = new ArrayList<>();
        populateRecyclerView();
        //sortRecyclerView(true);



        // set up the RecyclerView
        RecyclerView recyclerView = binding.rvAnimals;//findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductRecyclerViewAdapter(getContext(), productList);
        adapter.setClickListener(new ProductRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                productSelectedTransaction(productList.get(position));
                Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);


        binding.buttonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromProduct", false);
                bundle.putInt("id", 0);
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


    public void onItemClick(View view, int position) {
        Log.d("RecyclerView", "onViewCreated: "+adapter.getItem(position) + " on row number " + position);
    }

    public void productSelectedTransaction(Product product){
        Bundle bundle = new Bundle();
        bundle.putString("id", product.getProductId().toString());
        bundle.putBoolean("fromProduct", true);
        NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_ThirdFragment, bundle);
    }

    public void populateRecyclerView(){
        LiveData<List<Product>> products = ((App)getActivity().getApplication()).getRepo().getProducts();
        products.observe(getViewLifecycleOwner(), products1 -> {
            productList = new ArrayList<>();
            productList.addAll(products1);
        });
    }

    public void sortRecyclerView(boolean leastRecent){
        if(productList!=null) {
            if (leastRecent) {
                binding.textSorted.setText("ascending");
                binding.btnChangeSort.setImageResource(R.drawable.ic_arrow_up);
                productList.sort(Comparator.comparing(Product::getLastAdd).reversed());
                ascendingSort = true;
            } else {
                binding.textSorted.setText("descending");
                binding.btnChangeSort.setImageResource(R.drawable.ic_arrow_down);
                productList.sort(Comparator.comparing(Product::getLastAdd));
                ascendingSort = false;
            }
            Log.d("list in fragment", Arrays.toString(productList.toArray()));
            adapter.updateList(productList);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}