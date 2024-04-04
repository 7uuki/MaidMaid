package de.fhandshit.maidmaid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.databinding.FragmentThirdBinding;

public class ThirdFragment extends Fragment {
    final Calendar myCalendar = Calendar.getInstance();
    TextInputEditText dateText;
    TextInputEditText nameText;
    TextInputLayout dateText_Layout;
    AutoCompleteTextView categoryList;

    private FragmentThirdBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);

        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Makes save button inable to save and makes it grey, until all the inputs were made
        setupSaveButton();

        // forces the keyboard to open immediately afer the fragment is created
        EditText editText = binding.productNameInput;
        editText.requestFocus();
        // Show the soft keyboard programmatically
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);


        dateText = binding.datePicker;
        dateText_Layout = binding.datePickerField;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                dateText_Layout.setStartIconDrawable(R.drawable.ic_calendar);
                dateText.setText(myCalendar.get(Calendar.DAY_OF_MONTH) + "." + myCalendar.get(Calendar.MONTH) + "." + myCalendar.get(Calendar.YEAR));
            }
        };
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        nameText = binding.productNameInput;
        categoryList = binding.categoryDropdownInput;

        if (getArguments().getBoolean("fromProduct")) {
            int id = getArguments().getInt("id");
            Product product = App.getRepo().getProductDao().getAll().get(id);
            Log.d("TAG", product.getCategoryName());
            Log.d("TAG", product.getCategory().getName());
            Log.d("TAG", product.getProductName());
            Log.d("TAG", String.valueOf(product.getId()));
            nameText.setText(product.getProductName());
            categoryList.setText(product.getCategoryName());
        }


        TextInputEditText productNameInput = binding.productNameInput;
        TextInputEditText expiryDateInput = binding.datePicker;
        // Find the AutoCompleteTextView
        AutoCompleteTextView categoryDropdownInput = binding.categoryDropdownInput;

        // Fetch categories from your database
        List<String> categories = getCategoriesFromDatabase();

        // Create an ArrayAdapter using the list of categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, categories);

        // Set the adapter to the AutoCompleteTextView
        categoryDropdownInput.setAdapter(adapter);

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the text entered by the user
                String productName = productNameInput.getText().toString();

                // Retrieve the text entered by the user in the date picker
                String dateString = dateText.getText().toString();

                // Define the date format expected from the user input
                SimpleDateFormat  dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
                // Parse the string into a Date object
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
                String selectedCategory = categoryDropdownInput.getText().toString();

                if (categories.contains(selectedCategory)) {
                    saveDataToDatabase(productName, expiryDate, selectedCategory);
                } else {
                    App.getRepo().getCategoryDao().insertAll(new Category(selectedCategory));
                    saveDataToDatabase(productName, expiryDate, selectedCategory);
                }

                 NavHostFragment.findNavController(ThirdFragment.this)
                         .popBackStack();
            }
        });


    }

    public List<String> getCategoriesFromDatabase() {
            List<Category> categories = App.getRepo().getCategories();
            List<String> categoriesString = new ArrayList<>();
            for (Category c : categories) {
                categoriesString.add(c.getName());
            }
            return categoriesString;
    }

    private void saveDataToDatabase(String productName, LocalDate expiryDate, String selectedCategory) {
        Product product = new Product(33, productName, App.getRepo().getCategoryDao().loadByName(selectedCategory));
        List<Product> products = App.getRepo().getProductDao().getAll();
        if (products.contains(product)){
            App.getRepo().getProductItemDao().insertAll(new ProductItem(33, expiryDate, product));
        }else{
            App.getRepo().getProductDao().insertAll(product);
            App.getRepo().getProductItemDao().insertAll(new ProductItem(33, expiryDate, product));
        }

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
