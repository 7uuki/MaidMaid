package de.fhandshit.maidmaid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.data.repository.Repo;
import de.fhandshit.maidmaid.databinding.FragmentFirstBinding;
import de.fhandshit.maidmaid.databinding.FragmentSecondBinding;

public class FirstFragment extends Fragment {
    private Repo repo;

    private List<ProductItem> productItems;
    private ArrayAdapter<ProductItem> productItemAdapter;
    private de.fhandshit.maidmaid.databinding.FragmentFirstBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) getActivity().getApplication()).getRepo();
    }

    public void populateChips(List<ProductItem> items){
        productItems.addAll(items);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private List<String> getUniqueCategories(List<ProductItem> productItems) {
        List<String> categories = new ArrayList<>();
        for (ProductItem item : productItems) {
            if (!categories.contains(item.getProduct().getCategory())) {
                categories.add(item.getProduct().getCategory());
            }
        }
        return categories;
    }

    private void filterProductItems(String category) {
        List<ProductItem> filteredList = new ArrayList<>();
        for (ProductItem item : productItems) {
            if (item.getProduct().getCategory().equals(category)) {
                filteredList.add(item);
            }
        }
        productItemAdapter.clear();
        productItemAdapter.addAll(filteredList);
        productItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFab.setOnClickListener(v->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );




        // Initialize the list of product items
        productItems = new ArrayList<>();

        LiveData<List<ProductItem>> items = repo.getProductItems();
        items.observe(getViewLifecycleOwner(), itemList ->{
            populateChips(itemList);
        });
        // Populate the list with sample data, you should retrieve the actual data from your database

        // Initialize the ListView
        ListView listView = view.findViewById(R.id.listView);

        // Create and set the adapter for the ListView
        productItemAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, productItems);
        listView.setAdapter(productItemAdapter);

        // Set item click listener for the ListView items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click here
                // Example: Open details activity or fragment for the selected product
            }
        });

        // Initialize the ChipGroup
        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);

        // Add chips dynamically based on available categories
        List<String> categories = getUniqueCategories(productItems);
        for (String category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chipGroup.addView(chip);
        }

        // Set chip click listener for filtering
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                // Handle chip click to filter product items
                // Example: Filter product items based on selected category
                String selectedCategory = ((Chip) group.findViewById(checkedId)).getText().toString();
                filterProductItems(selectedCategory);
            }
        });

        // Set click listener for FAB using data binding
    }
}