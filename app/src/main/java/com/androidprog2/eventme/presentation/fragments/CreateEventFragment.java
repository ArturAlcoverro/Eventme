package com.androidprog2.eventme.presentation.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.presentation.activities.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialButton createBtn;
    private MaterialButton uploadBtn;

    private TextInputLayout nameInput;
    private TextInputLayout locationInput;
    private TextInputLayout descriptionInput;
    private TextInputLayout startDateInput;
    private TextInputLayout endDateInput;
    private TextInputLayout startTimeInput;
    private TextInputLayout endTimeInput;
    private TextInputLayout capacityInput;
    private TextInputLayout categroyInput;
    private AutoCompleteTextView autoCompleteCategory;

    private String selectedCategory;
    private boolean isStartDateOrEnd;
    private final Calendar myCalendar = Calendar.getInstance();

    public CreateEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventFragment newInstance(String param1, String param2) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_event, container, false);
        uploadBtn = (MaterialButton) view.findViewById(R.id.upload_btn);
        createBtn = (MaterialButton) view.findViewById(R.id.create_btn);

        nameInput = view.findViewById(R.id.createEvent_name);
        locationInput = view.findViewById(R.id.createEvent_location);
        descriptionInput = view.findViewById(R.id.createEvent_description);
        startDateInput = view.findViewById(R.id.createEvent_startDate);
        endDateInput = view.findViewById(R.id.createEvent_endDate);
        startTimeInput = view.findViewById(R.id.createEvent_startTime);
        endTimeInput = view.findViewById(R.id.createEvent_endTime);
        capacityInput = view.findViewById(R.id.createEvent_capacity);
        categroyInput = view.findViewById(R.id.createEvent_category);
        autoCompleteCategory = view.findViewById(R.id.dropdown_menu_Category);

        startDateInput.getEditText().setInputType(InputType.TYPE_NULL);
        endDateInput.getEditText().setInputType(InputType.TYPE_NULL);

        loadDropDownMenuCategory();
        validationListeners();

        uploadBtn.setOnClickListener(v -> { System.out.println("upload clicked"); });
        createBtn.setOnClickListener(v -> { createEvent(); });

        return view;
    }

    public void createEvent(){
        if(validateData()){
            System.out.println("hola he clicat");
        }
    }

    public void loadDropDownMenuCategory(){
        selectedCategory = null;
        String[] category = new String[] {"Music", "Education", "Games", "Travel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.list_item_dropdown_menu,
                category);
        autoCompleteCategory.setAdapter(adapter);
    }

    public void validationListeners(){
        autoCompleteCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                System.out.println(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        nameInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        locationInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        descriptionInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        capacityInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCapacity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        nameInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) validateName(nameInput.getEditText().getText().toString());
        });

        locationInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
           if(!hasFocus) validateLocation(locationInput.getEditText().getText().toString());
        });

        descriptionInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) validateDescription(descriptionInput.getEditText().getText().toString());
        });

        capacityInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) validateCapacity(capacityInput.getEditText().getText().toString());
        });

        categroyInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) validateCategory();
        });

        startDateInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                isStartDateOrEnd = true;
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }else{
                validateStartDate(startDateInput.getEditText().getText().toString());
            }
        });

        endDateInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                isStartDateOrEnd = false;
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }else{
                validateEndDate(endDateInput.getEditText().getText().toString());
            }
        });

        startDateInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                startTimeInput.requestFocus();
                return true;
            }
            return false;
        });

        startTimeInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                endDateInput.requestFocus();
                return true;
            }
            return false;
        });

        endDateInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                endTimeInput.requestFocus();
                return true;
            }
            return false;
        });

        endTimeInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                categroyInput.requestFocus();
                return true;
            }
            return false;
        });

        capacityInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                //hidekeyboard
                capacityInput.clearFocus();
                return true;
            }
            return false;
        });
    }

    public boolean validateName(String name){
        if(!name.isEmpty()){
            nameInput.setErrorEnabled(false);
            return true;
        }
        nameInput.setError(getString(R.string.createEvent_name_error));
        return false;
    }

    public boolean validateLocation(String location){
        if(!location.isEmpty()){
            locationInput.setErrorEnabled(false);
            return true;
        }
        locationInput.setError(getString(R.string.createEvent_location_error));
        return false;
    }

    public boolean validateDescription(String description){
        if(!description.isEmpty()){
            descriptionInput.setErrorEnabled(false);
            return true;
        }
        descriptionInput.setError(getString(R.string.createEvent_description_error));
        return false;
    }

    public boolean validateStartDate(String startDate){
        if(!startDate.isEmpty()){
            startDateInput.setErrorEnabled(false);
            return true;
        }
        startDateInput.setError(getString(R.string.createEvent_startDate_error));
        return false;
    }

    public boolean validateEndDate(String endDate){
        if(!endDate.isEmpty()){
            endDateInput.setErrorEnabled(false);
            return true;
        }
        endDateInput.setError(getString(R.string.createEvent_endDate_error));
        return false;
    }

    public boolean validateStartTime(String startTime){
        return false;
    }

    public boolean validateEndTime(String endTime){
        return false;
    }

    public boolean validateCapacity(String capacity){
        if(!capacity.isEmpty()){
            if(TextUtils.isDigitsOnly(capacity)){
                capacityInput.setErrorEnabled(false);
                return true;
            }
            capacityInput.setError(getString(R.string.createEvent_capacity_error_syntax));
            return false;
        }
        capacityInput.setError(getString(R.string.createEvent_capacity_error));
        return false;
    }

    public boolean validateCategory(){
        if(selectedCategory != null){
            categroyInput.setErrorEnabled(false);
            return true;
        }
        categroyInput.setError(getString(R.string.createEvent_capacity_error));
        return false;
    }

    public boolean validateData(){
        boolean error = true;
        if(!validateName(nameInput.getEditText().getText().toString())) error = false;
        if(!validateLocation(locationInput.getEditText().getText().toString())) error = false;
        if(!validateDescription(descriptionInput.getEditText().getText().toString())) error = false;
        if(!validateCategory()) error = false;
        if(!validateCapacity(capacityInput.getEditText().getText().toString())) error = false;
        return error;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        if(isStartDateOrEnd){
            startDateInput.getEditText().setText(sdf.format(myCalendar.getTime()));
        }else{
            endDateInput.getEditText().setText(sdf.format(myCalendar.getTime()));
        }
    }
}