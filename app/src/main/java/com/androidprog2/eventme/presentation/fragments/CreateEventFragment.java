package com.androidprog2.eventme.presentation.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment implements Callback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_GALLERY_IMAGE = 1;
    private String DEFAULT_IMG;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialButton createBtn;
    private MaterialButton uploadBtn;
    private ProgressBar progressBar;
    private ImageView eventImage;
    private File mImageFile;
    private ScrollView scrollView;

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
    private String[] categories;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_event, container, false);
        uploadBtn = (MaterialButton) view.findViewById(R.id.upload_btn);
        createBtn = (MaterialButton) view.findViewById(R.id.create_btn);

        categories = new String[] {getString(R.string.home_chip_art), getString(R.string.home_chip_cultural),
                getString(R.string.home_chip_education), getString(R.string.home_chip_games), getString(R.string.home_chip_music),
                getString(R.string.home_chip_politics), getString(R.string.home_chip_science), getString(R.string.home_chip_sport),
                getString(R.string.home_chip_technology), getString(R.string.home_chip_others)};

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
        eventImage = view.findViewById(R.id.eventImage);
        scrollView = view.findViewById(R.id.scroll_create_event);
        progressBar = view.findViewById(R.id.progress_bar);

        startDateInput.getEditText().setInputType(InputType.TYPE_NULL);
        endDateInput.getEditText().setInputType(InputType.TYPE_NULL);

        loadDropDownMenuCategory();
        validationListeners();

        uploadBtn.setOnClickListener(v -> { selectImage(); });
        createBtn.setOnClickListener(v -> { createEvent(); });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createEvent(){
        if(validateData()){
            String name = nameInput.getEditText().getText().toString();
            String location = locationInput.getEditText().getText().toString();
            String description = descriptionInput.getEditText().getText().toString();
            //String startDate = unionDateAndTime(startDateInput.getEditText().getText().toString(), startTimeInput.getEditText().getText().toString());
            //String endDate = unionDateAndTime(endDateInput.getEditText().getText().toString(), endTimeInput.getEditText().getText().toString());
            String startDate = startDateInput.getEditText().getText().toString();
            String endDate = endDateInput.getEditText().getText().toString();
            String category = categroyInput.getEditText().getText().toString();
            String capacity = capacityInput.getEditText().getText().toString();

            loading(true);
            if (mImageFile != null) {
                CallSingelton
                        .getInstance()
                        .insertEvent(name, mImageFile, location, description, startDate, endDate, category, capacity, this);

            }else {
                setDefaultImage(category);
                CallSingelton
                        .getInstance()
                        .insertEvent(name, DEFAULT_IMG, location, description, startDate, endDate, category, capacity, this);
            }
        }
    }

    @Override
    public void onResponse(Call call, Response response) {
        loading(false);
        if (response.isSuccessful()) {
            System.out.println(response.code() + "codi al crear un evento");
            if (response.code() == 201) {
                //Decidir que fer
                System.out.println("event creat");
            }
            else if (response.code() == 400){
                Toast.makeText(getContext(), getString(R.string.incorrect_body_error), Toast.LENGTH_LONG).show();
            }else if (response.code() == 409){
                Toast.makeText(getContext(), getString(R.string.incorrect_to_insert), Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                System.out.println(response.errorBody().toString());
                Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        System.out.println("aqui he entrat");
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        loading(false);
    }

    public void loadDropDownMenuCategory(){
        selectedCategory = null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.list_item_dropdown_menu,
                categories);
        autoCompleteCategory.setAdapter(adapter);
    }

    public void validationListeners(){
        autoCompleteCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validateCategory(categroyInput.getEditText().getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
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
            if(!hasFocus) validateCategory(categroyInput.getEditText().getText().toString());
        });

        startDateInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                hideKeyboard(getView());
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
                hideKeyboard(getView());
                isStartDateOrEnd = false;
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }else{
                validateEndDate(endDateInput.getEditText().getText().toString());
            }
        });

        startTimeInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                hideKeyboard(getView());
                TimePick time = new TimePick(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                time.show(ft, "TimePicker");
            }else{
                validateStartTime(startTimeInput.getEditText().getText().toString());
            }
        });

        endTimeInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                hideKeyboard(getView());
                TimePick time = new TimePick(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                time.show(ft, "TimePicker");
            }else{
                validateEndTime(endTimeInput.getEditText().getText().toString());
            }
        });
        
        /*startDateInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
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
        });*/

        capacityInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                hideKeyboard(getView());
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                },1000);

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
        if(!startTime.isEmpty()){
            startTimeInput.setErrorEnabled(false);
            return true;
        }
        startTimeInput.setError(getString(R.string.createEvent_startTime_error));
        return false;
    }

    public boolean validateEndTime(String endTime){
        if(!endTime.isEmpty()){
            endTimeInput.setErrorEnabled(false);
            return true;
        }
        endTimeInput.setError(getString(R.string.createEvent_endTime_error));
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

    public boolean validateCategory(String category){
        if(!category.isEmpty()){
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
        if(!validateCategory(categroyInput.getEditText().getText().toString())) error = false;
        if(!validateCapacity(capacityInput.getEditText().getText().toString())) error = false;
        if(!validateStartDate(startDateInput.getEditText().getText().toString())) error = false;
        if(!validateEndDate(endDateInput.getEditText().getText().toString())) error = false;
        if(!validateStartTime(startTimeInput.getEditText().getText().toString())) error = false;
        if(!validateEndTime(endTimeInput.getEditText().getText().toString())) error = false;
        return error;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        if(isStartDateOrEnd){
            startDateInput.getEditText().setText(sdf.format(myCalendar.getTime()));
        }else{
            endDateInput.getEditText().setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(INPUT_METHOD_SERVICE);
        if (view == null) {
            view = new View(getContext());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK) {
            Uri uriData = data.getData();
            try {
                // Creating file
                mImageFile = null;
                try {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    ContentResolver cr = getContext().getContentResolver();
                    mImageFile = createImageFile(mime.getExtensionFromMimeType(cr.getType(uriData)));
                } catch (IOException ex) {
                    Log.d("ERR", "Error occurred while creating the file");
                }
                eventImage.setImageURI(uriData);
                InputStream inputStream = getContext().getContentResolver().openInputStream(uriData);
                FileOutputStream fileOutputStream = new FileOutputStream(mImageFile);
                // Copying
                copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();

            } catch (Exception e) {
                Log.d("ERR", "onActivityResult: " + e.toString());
            }
        }
    }

    private File createImageFile(String extension) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,
                "." + extension,
                storageDir
        );
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public void loading(boolean state) {
        boolean enable = !state;

        createBtn.setEnabled(enable);
        uploadBtn.setEnabled(enable);
        nameInput.setEnabled(enable);
        locationInput.setEnabled(enable);
        descriptionInput.setEnabled(enable);
        startDateInput.setEnabled(enable);
        startTimeInput.setEnabled(enable);
        endDateInput.setEnabled(enable);
        endTimeInput.setEnabled(enable);
        categroyInput.setEnabled(enable);
        capacityInput.setEnabled(enable);

        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            createBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createBtn.setVisibility(View.VISIBLE);
        }
    }

    public String unionDateAndTime(String date, String time){
        String ts = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(date + " " + time + ":00.000");
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            ts = timestamp.toString();
        } catch(Exception e) {
            System.out.println(e);
        }
        return ts;
    }

    public void setDefaultImage(String category){
        String[] urls = new String[] {"shorturl.at/cnFT1", "shorturl.at/eiDX6", "shorturl.at/cgmI5", "shorturl.at/hrCL5", "shorturl.at/fnvV3",
                    "shorturl.at/cwB36", "shorturl.at/mtwWX", "shorturl.at/tHPS8", "shorturl.at/ksKO0"};
        int position = -1;
        for (int i = 0; i < categories.length; i++) {
            if(category.equals(categories[i])){
                position = i;
            }
        }

        if(position == -1){
            DEFAULT_IMG = "shorturl.at/pxCX0";
        }else{
            DEFAULT_IMG = urls[position];
        }
    }

    public static class TimePick extends DialogFragment implements TimePickerDialog.OnTimeSetListener
    {
        private TextView time;

        public TimePick(View view)
        {
            time=(EditText)view;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            final Calendar c= Calendar.getInstance();
            int hour=c.get(Calendar.HOUR_OF_DAY);
            int minute=c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet(TimePicker view, int hourofDay, int minute)
        {
            time.setText(Integer.toString(hourofDay) + ":" + Integer.toString(minute));
        }
    }
}