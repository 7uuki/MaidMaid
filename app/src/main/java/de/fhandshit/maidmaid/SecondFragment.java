package de.fhandshit.maidmaid;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.fhandshit.maidmaid.data.database.AppDatabase;
import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.databinding.FragmentSecondBinding;
import de.fhandshit.maidmaid.ui.add_product_item.AddProductItemFragment;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private boolean ascendingSort;
    private ArrayList<Product> productList;

    private ListView listView;
    private SearchView searchView;
    private ArrayAdapter searchBarAdapter;
    private List<String> stringList;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);

        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        //Searchbar:
        listView = binding.listView;
        searchView = binding.searchView;

        listView.setVisibility(View.INVISIBLE);

        // Sample list of strings
        stringList = getProductNamesFromDatabase();

        // Initialize adapter with the full list
        searchBarAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(searchBarAdapter);

        // Implement search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list based on the search query
                binding.listView.setVisibility(View.VISIBLE);
                Log.d("TAG", "befor");
                if (TextUtils.isEmpty(newText)) {
                    Log.d("TAG", "isempty");
                    // If the search query is empty, show the full list
                    searchBarAdapter.getFilter().filter("");
                } else {
                    Log.d("TAG", "notEmpty");
                    // Show only items that match the search query
                    searchBarAdapter.getFilter().filter(newText);
                    searchBarAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

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

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.listView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    listView.setVisibility(View.INVISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listView.setVisibility(View.INVISIBLE);
                searchView.clearFocus();
                //productSelectedTransaction(); TODO: Sollte ausgeführt werden damit es auf third übertragen wird
            }
        });

    }


    public List<String> getProductNamesFromDatabase() {
        List<String> categoriesString = new ArrayList<>(Arrays.asList("Apple", "Banana", "Orange", "Pineapple", "Grapes", "Watermelon"));
        return categoriesString;
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