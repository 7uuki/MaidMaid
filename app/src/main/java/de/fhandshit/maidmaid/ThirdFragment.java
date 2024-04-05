package de.fhandshit.maidmaid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.data.repository.Repo;
import de.fhandshit.maidmaid.databinding.FragmentThirdBinding;

public class ThirdFragment extends Fragment {
    final Calendar myCalendar = Calendar.getInstance();
    private FragmentThirdBinding binding;

    private Repo repo;
    private LiveData<Product> product;
    private LiveData<List<String>> categories;
    private ArrayAdapter<String> adapter;



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) getActivity().getApplication()).getRepo();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Makes save button inable to save and makes it grey, until all the inputs were made
        setupSaveButton();

        setupAdapter();

        // forces the keyboard to open immediately afer the fragment is created
        EditText editText = binding.productNameInput;
        editText.requestFocus();
        // Show the soft keyboard programmatically
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                binding.datePickerField.setStartIconDrawable(R.drawable.ic_calendar);
                binding.datePicker.setText(myCalendar.get(Calendar.DAY_OF_MONTH) + "." + myCalendar.get(Calendar.MONTH) + "." + myCalendar.get(Calendar.YEAR));
            }
        };
        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (getArguments().getBoolean("fromProduct")) {
            UUID uuid= UUID.fromString(getArguments().getString("id"));

            product = repo.getProduct(uuid);
            product.observe(getViewLifecycleOwner(), product -> {
                if(product != null) {
                    binding.productNameInput.setText(product.getName());
                    binding.categoryDropdownInput.setText(product.getCategory());
                }
            });
        }


        TextInputEditText productNameInput = binding.productNameInput;
        TextInputEditText expiryDateInput = binding.datePicker;



        binding.buttonSave.setOnClickListener(v -> {
            String productName = productNameInput.getText().toString();
            String dateString = binding.datePicker.getText().toString();

            SimpleDateFormat  dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
            Date parsedDate;
            try {
                parsedDate = dateFormatter.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                // Handle parsing error
                return;
            }
            // Convert the Date object to LocalDate if necessary (optional)
            LocalDate expiryDate = parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


            //Create Category Object from User Input
            String selectedCategory = binding.categoryDropdownInput.getText().toString();


            saveDataToDatabase(productName, expiryDate, selectedCategory);
        });


    }

    private void setupAdapter(){
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        binding.categoryDropdownInput.setAdapter(adapter);
        categories = repo.getCategories();
        categories.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapter.clear();
                adapter.addAll(strings);
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void saveDataToDatabase(String productName, LocalDate expiryDate, String selectedCategory) {
        LiveData<Product> liveData = repo.findByName(productName);
        liveData.observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                if(product == null) {
                    product = new Product(productName, selectedCategory, LocalDateTime.now());
                    repo.insertProduct(product);
                }else{
                    product.updateLastAdd();
                    repo.updateProduct(product);
                }
                ProductItem productItem = new ProductItem(expiryDate, product);
                repo.insertProductItem(productItem);
                NavHostFragment.findNavController(ThirdFragment.this)
                        .popBackStack();
                liveData.removeObserver(this);
            }
        });
    }

    private void setupSaveButton() {
        TextInputEditText productNameInput = binding.productNameInput;
        TextInputEditText expiryDateInput = binding.datePicker;
        AutoCompleteTextView categoryDropdownInput = binding.categoryDropdownInput;
        Button saveButton = binding.buttonSave;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean productNameFilled = !productNameInput.getText().toString().trim().isEmpty();
                boolean expiryDateFilled = !expiryDateInput.getText().toString().trim().isEmpty();
                boolean categoryFilled = !categoryDropdownInput.getText().toString().trim().isEmpty();

                saveButton.setEnabled(productNameFilled && expiryDateFilled && categoryFilled);
            }
        };

        productNameInput.addTextChangedListener(textWatcher);
        expiryDateInput.addTextChangedListener(textWatcher);
        categoryDropdownInput.addTextChangedListener(textWatcher);

        // Call afterTextChanged() immediately to update the save button's enabled state
        textWatcher.afterTextChanged(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
