package de.fhandshit.maidmaid;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import de.fhandshit.maidmaid.data.database.AppDatabase;
import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.databinding.FragmentSecondBinding;
import de.fhandshit.maidmaid.ui.add_product_item.AddProductItemFragment;

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
        bundle.putInt("id", product.getId());
        bundle.putBoolean("fromProduct", true);
        NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_ThirdFragment, bundle);
    }

    public void populateRecyclerView(){
        this.productList = (ArrayList<Product>) App.getRepo().getProductDao().getAll();
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