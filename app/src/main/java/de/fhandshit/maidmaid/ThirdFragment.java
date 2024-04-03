package de.fhandshit.maidmaid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

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

        dateText = binding.datePicker;
        dateText_Layout = binding.datePickerField;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                dateText_Layout.setStartIconDrawable(R.drawable.ic_calendar);
                dateText.setText(myCalendar.get(Calendar.DAY_OF_MONTH) + "." + myCalendar.get(Calendar.MONTH)+ "." + myCalendar.get(Calendar.YEAR));
            }
        };
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        nameText = binding.productNameInput;
        categoryList = binding.autoCompleteTextView;

        if (getArguments().getBoolean("fromProduct")) {
            int id = getArguments().getInt("id") ;
            Product product = App.getRepo().getProductDao().getAll().get(id);
            nameText.setText(product.getProductName());
            categoryList.setText(product.getCategoryName());
        }

/*
        TextInputEditText productNameInput = view.findViewById(R.id.product_name_input);
        TextInputEditText expiryDateInput = view.findViewById(R.id.date_picker);
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the text entered by the user
                String productName = productNameInput.getText().toString();
                String expiryDate = expiryDateInput.getText().toString();

                // Perform your database operations or save the data in variables as needed
                // For example, you can pass this data to a method for saving to the database
                saveDataToDatabase(productName, expiryDate);
            }
        });*/
    }

    /*private void saveDataToDatabase(String productName, String expiryDate){
        //Dummy
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
