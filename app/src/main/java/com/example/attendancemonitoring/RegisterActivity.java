package com.example.attendancemonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.User;
import com.example.attendancemonitoring.Repositories.UserRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.idNumber) EditText idNumber;
    @BindView(R.id.firstName) EditText firstName;
    @BindView(R.id.middleName) EditText middleName;
    @BindView(R.id.lastName) EditText lastName;
    @BindView(R.id.course) Spinner course;

    private String[] courses = { "Bachelor of Science in Computer Science" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        this.initializeRegister();
    }

    private void initializeRegister() {
        if  (UserRepository.isUserAlreadyRegister(this)) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        } else {
            // The user need to register so we need to init the value of course spinner.
            this.setCourses();
        }
    }

    private void setCourses() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course.setAdapter(adapter);
    }

    @OnClick(R.id.btnRegister) void register()
    {
        // Add validation here..
        User user = new User(
                idNumber.getText().toString(),
                firstName.getText().toString(),
                middleName.getText().toString(),
                lastName.getText().toString(),
                course.getSelectedItem().toString()
        );

        // Insert new user.
        DB.getInstance(getApplicationContext()).userDao().create(user);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
