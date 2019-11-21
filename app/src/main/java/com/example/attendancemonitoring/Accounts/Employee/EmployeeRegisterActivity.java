package com.example.attendancemonitoring.Accounts.Employee;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.attendancemonitoring.DashboardActivity;
import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.User;
import com.example.attendancemonitoring.Helpers.SharedPref;
import com.example.attendancemonitoring.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;

public class EmployeeRegisterActivity extends AppCompatActivity {

    @BindView(R.id.idNumber) EditText idNumber;
    @BindView(R.id.firstName) EditText firstName;
    @BindView(R.id.middleName) EditText middleName;
    @BindView(R.id.lastName) EditText lastName;
    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);
        ButterKnife.bind(this);


        // Initialize Form Validator
        mAwesomeValidation = new AwesomeValidation(COLORATION);
        mAwesomeValidation.setColor(Color.YELLOW);  // optional, default color is RED if not set
        mAwesomeValidation.addValidation(this, R.id.idNumber, "\\d+\\-\\d+", R.string.idNumber);
        mAwesomeValidation.addValidation(this, R.id.firstName, "[a-zA-Z\\s]+", R.string.firstName);
        mAwesomeValidation.addValidation(this, R.id.middleName, "[a-zA-Z\\s]+", R.string.middleName);
        mAwesomeValidation.addValidation(this, R.id.lastName, "[a-zA-Z\\s]+", R.string.lastName);

        /* for development purpose */
        idNumber.setText("15-10755");
        firstName.setText("christopher");
        middleName.setText("platino");
        lastName.setText("vistal");
    }




    @OnClick(R.id.btnRegister) void register()
    {
        if  (mAwesomeValidation.validate()) {



            User user = new User(
                    idNumber.getText().toString(),
                    firstName.getText().toString(),
                    middleName.getText().toString(),
                    lastName.getText().toString(),
                    ""
            );

            DB.getInstance(this).userDao().create(user);
            SharedPref.setSharedPreferenceString(this, "user_role", "employee");

            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }


}
