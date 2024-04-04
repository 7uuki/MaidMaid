package de.fhandshit.maidmaid;

import static java.util.Arrays.*;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.repository.Repo;
import de.fhandshit.maidmaid.databinding.FragmentSecondBinding;
import de.fhandshit.maidmaid.ui.CustomDividerItemDecoration;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private boolean ascendingSort;
    private LiveData<List<Product>> productList;

    private Repo repo;
    //
    //private ListView listView;
    private SearchView searchView;
    private ArrayAdapter searchBarAdapter;
    private List<String> stringList;
    private ProductRecyclerViewAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) getActivity().getApplication()).getRepo();
        productList = repo.getProducts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // when the LiveData<Product> changes updates the List in  ProductRecyclerViewAdapter adapter
        productList.observe(getViewLifecycleOwner(), products1 -> {
            adapter.updateList(products1,true);
        });
        // creates ProductListView with Adapter
        createProductListView();

        // ----- Searchbar:
        searchView = binding.searchView;

        binding.listView.setVisibility(View.INVISIBLE);

        // Sample list of strings
        stringList = getProductNamesFromDatabase();

        // Initialize adapter with the full list
        searchBarAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stringList);
        //listView.setAdapter(searchBarAdapter);


        // Implement search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list based on the search query
                Log.d("TAG", "befor");
                if (TextUtils.isEmpty(newText)) {
                    Log.d("TAG", "isempty");
                    // If the search query is empty, show the full list
                    searchBarAdapter.getFilter().filter("");
                } else {
                    Log.d("TAG", "notEmpty: "+newText);
                    // Show only items that match the search query
                    searchBarAdapter.getFilter().filter(newText);
                    searchBarAdapter.notifyDataSetChanged();
                }
                adapter.filterList(newText);
                return true;
            }
        });

        binding.buttonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productSelectedTransaction(null);
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
    public void sortRecyclerView(boolean leastRecent) {
        adapter.sortRecyclerView(leastRecent);
        if (leastRecent) {
            binding.btnChangeSort.setText("oldest first");
            binding.btnChangeSort.setIcon( AppCompatResources.getDrawable(getContext(),R.drawable.ic_arrow_up));
            ascendingSort = true;
        } else {
            binding.btnChangeSort.setText("newest first");
            binding.btnChangeSort.setIcon( AppCompatResources.getDrawable(getContext(),R.drawable.ic_arrow_down));
            ascendingSort = false;
        }
        adapter.sortRecyclerView(leastRecent);
    }

    private void createProductListView() {
        //View
        RecyclerView recyclerView = binding.productList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        adapter = new ProductRecyclerViewAdapter(getContext());
        adapter.setItemClickListener(new ProductRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, Product product) {
                productSelectedTransaction(product);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void productSelectedTransaction(Product product) {
        Bundle bundle = new Bundle();
        if(product != null){
            bundle.putBoolean("fromProduct", true);
            bundle.putString("id", product.getProductId().toString());
        }else{
            bundle.putBoolean("fromProduct", false);
        }
        NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_ThirdFragment, bundle);
        //cloeses SearchBar again
        searchView.onActionViewCollapsed();
    }

    public List<String> getProductNamesFromDatabase() {
        List<String> categoriesString = new ArrayList<>(asList("Apple", "Banana", "Orange", "Pineapple", "Grapes", "Watermelon"));
        return categoriesString;
    }


}