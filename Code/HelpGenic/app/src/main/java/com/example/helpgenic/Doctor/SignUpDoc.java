package com.example.helpgenic.Doctor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.GuestUser;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpDoc extends AppCompatActivity {

    //--------------------Declare All Views--------//
    String[] categories = {"Dermatologist", "Alergist", "Neurologist", "Pathologist", "Urologist", "Anesthesiologist", "Ophthalmologist"};
    Dialog dialog;
    TextView selectOption,dob;
    Button submitBtn;
    EditText name, mail, password, contact;
    RadioGroup rg;
    RadioButton male,female,yes,no;
    Button uploaddocs;
    java.sql.Date dateSelected = null;
    private String encodedImage=null;
    final Calendar myCalendar= Calendar.getInstance();
    // Initialize variable
    ActivityResultLauncher<Intent> resultLauncher;
    //---------------------------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doc);
        //--------------------- Register all views ----------------------//
        submitBtn = findViewById(R.id.submitButtonDoc);
        selectOption = findViewById(R.id.selectCategory); // select category option
        name = findViewById(R.id.editTextTextPersonName7);
        mail = findViewById(R.id.editTextTextPersonName8);
        password = findViewById(R.id.editTextTextPassword2);
        dob = findViewById(R.id.editTextDate);
        contact = findViewById(R.id.editTextNumber);
        rg = findViewById(R.id.radioGroup);
        male=findViewById(R.id.Male);
        female=findViewById(R.id.Female);
        yes=findViewById(R.id.Yes);
        no=findViewById(R.id.No);
        uploaddocs = findViewById(R.id.uploaddocs);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpDoc.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //----------------------------------------------------------------//
        // When user presses the select category option
        selectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDialogBox();
                handleDialogBoxFunctionality();
            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyCredentials()) { // if all credentials are filled and validated
                    //check either male or female
                    char gen = maleORfemale();
                    // is Surgeon;
                    boolean Surgeon = isSurgeon();
                    //User and Doctor created for inserting in user table
                    GuestUser guestUser = new GuestUser(name.getText().toString(),mail.getText().toString(),
                            password.getText().toString(),gen,dateSelected);

                    Doctor doctor = new Doctor(selectOption.getText().toString(),Surgeon,null,
                            null);

                    DbHandler dbHandler = new DbHandler();
                    dbHandler.connectToDb(getApplicationContext());

                    //verify if a user already exists with the current email
                    if(dbHandler.verifyUser(mail.getText().toString(),getApplicationContext())){
                        //insert user to db if the email is unique
                        int id =dbHandler.insertUser(guestUser,"D",getApplicationContext());
                        if(id>0) {
                            dbHandler.insertDoctor(doctor,id,encodedImage, getApplicationContext());
                            Toast.makeText(getApplicationContext(), "User inserted!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"id<0",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(),"User already exists with this email",Toast.LENGTH_LONG).show();

                    try {
                        dbHandler.closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        // Initialize result launcher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result)
            {
                // Initialize result data
                Intent data = result.getData();
                // check condition
                if (data != null) {

                    Uri selectedImageUri = data.getData();
                    // get bytes data from image and save it to database along with other doctor credentials

                    ReportsHandler rh = new ReportsHandler();
                    encodedImage = rh.loadImage(selectedImageUri , SignUpDoc.this);
                    Toast.makeText(SignUpDoc.this, "Upload Successful !", Toast.LENGTH_SHORT).show();


                }
            }

        } );


        uploaddocs.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View view) {

                // check condition
                if (ActivityCompat.checkSelfPermission(SignUpDoc.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    // When permission is not granted
                    // Result permission
                    ActivityCompat.requestPermissions(SignUpDoc.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    // When permission is granted
                    // Create method
                    selectImage();
                }
            }
        });


    }

    public boolean verifyCredentials() {
        boolean errorfound= false;

        if (name.length() <= 4) {
            name.setError("Minimum length should 5!");
            errorfound=true;
        }

        if (mail.length() == 0) {
            mail.setError("Please fill this!");
            errorfound=true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
            mail.setError("Invalid Email!");
            errorfound=true;
        }

        if(!validateDatePattern()){
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            errorfound=true;
        }

        if(contact.length()<11){
            contact.setError("Invalid Contact!");
            errorfound=true;
        }
        if(dateSelected == null){
            Toast.makeText(this, "Select your data", Toast.LENGTH_SHORT).show();
        }

        if(password.length()< 9){
            password.setError("Must be at least 9 characters!");
            errorfound=true;
        }

        if(selectOption.getText().toString().equals("")){
            selectOption.setError("Select Category!");
            errorfound=true;
        }

        if(!(male.isChecked() || female.isChecked())) {
            female.setError("Select one option!");
            errorfound=true;
        }

        if(!(no.isChecked() || yes.isChecked())){
            no.setError("Select one option!");
            errorfound=true;
        }
        if(encodedImage == null){
            Toast.makeText(this, "Please Upload Your Degree img", Toast.LENGTH_SHORT).show();
            errorfound=true;
        }

        return !errorfound;
    }


    @SuppressLint("SimpleDateFormat")
    private void updateLabel(){

        Date t = myCalendar.getTime();
        dateSelected = new java.sql.Date(t.getTime());
        dob.setText(dateSelected.toString());


    }

    private void setUpDialogBox() {
        // initialize dialog
        dialog = new Dialog(this);
        // set custom design of dialog
        dialog.setContentView(R.layout.searchable_spinner_custom_design);
        // set custom height
        dialog.getWindow().setLayout(1000, 1500);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show
        dialog.show();
    }

    private void handleDialogBoxFunctionality() {

        EditText editText = dialog.findViewById(R.id.editText);
        ListView dialogList = dialog.findViewById(R.id.categoryList);
        ArrayAdapter<String> dialogAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        dialogList.setAdapter(dialogAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // leave it
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filter list
                dialogAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // leave it
            }
        });
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // set text view as the selected item
                selectOption.setText(dialogAdapter.getItem(i));
                // close the dialog box
                dialog.dismiss();
            }
        });
    }

    public boolean validateDatePattern() {

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            Calendar calendar = Calendar.getInstance();
            String date = sdf.format(calendar.getTime());
            java.util.Date currdate1 = null;
            try {
                currdate1 =new SimpleDateFormat("yyyy/MM/dd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if(dateSelected != null){
                assert currdate1 != null;
                if(currdate1.compareTo(dateSelected) < 0){

                    return false;
                }
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return true;


    }

    public char maleORfemale()
    {
        if(male.isChecked())
            return 'm';
        else
            return 'f';
    }

    public boolean isSurgeon()
    {
        return yes.isChecked();
    }

    public void onRadioButtonClicked2(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.Yes:
                if (checked)
                    Toast.makeText(getApplicationContext(),"Yes",Toast.LENGTH_LONG).show();
                break;

            case R.id.No:
                if (checked)
                    Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_LONG).show();
                break;

        }
    }


    private void selectImage()
    {
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("image/*");
        // Launch intent
        resultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        // check condition
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            selectImage();
        }
        else {
            // When permission is denied
            // Display toast
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

}