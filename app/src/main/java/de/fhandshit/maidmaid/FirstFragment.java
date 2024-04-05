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
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.data.repository.Repo;
import de.fhandshit.maidmaid.databinding.FragmentFirstBinding;
import de.fhandshit.maidmaid.ui.CustomDividerItemDecoration;
import de.fhandshit.maidmaid.ui.add_product_item.ProductItemAdapter;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private LiveData<List<String>> categories;
    private Repo repo;
    private ProductItemAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) getActivity().getApplication()).getRepo();
        categories = repo.getCategories();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        categories.observe(getViewLifecycleOwner(), this::onNewCategories);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.floatingActionButton.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );
        binding.fABtnQR.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_firstFragment_to_qrFragment)
        );

        ChipGroup chipGroup = binding.firstFragmentChipGroup;


        binding.firstFragmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.firstFragmentRecycler.addItemDecoration(new CustomDividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        adapter = new ProductItemAdapter();

        binding.firstFragmentRecycler.setAdapter(adapter);
    }

    private void onNewCategories(List<String> categories){
        binding.firstFragmentChipGroup.removeAllViews();
        categories.forEach(s -> {
            Chip chip = new Chip(getContext(), null, com.google.android.material.R.style.Widget_Material3_Chip_Filter);
            chip.setText(s);
            chip.setId(View.generateViewId());
            chip.setCheckable(true); // Ensure chips are checkable
            chip.setOnClickListener(new Chip.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Chip clickedChip = (Chip) v;
                    String selectedChipText = clickedChip.getText().toString();
                    Log.d("TAg", "onClick: "+selectedChipText);
                    onNewCategorySelected(selectedChipText);
                }
            });
            binding.firstFragmentChipGroup.addView(chip);
        });
    }

    private void onNewCategorySelected(String category){
        LiveData<List<ProductItem>> productItems;
        if(category == null) productItems = repo.getProductItems();
        else productItems = repo.getProductItems(category);

        productItems.observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> list) {
                Log.d("TAG", "onChanged: " + Arrays.toString(list.toArray()));
                updateProductItems(list);
                productItems.removeObserver(this);
            }
        });
    }

    private void updateProductItems(List<ProductItem> productItems){
        adapter.setProductItems(productItems);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}