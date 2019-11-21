package com.example.attendancemonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemonitoring.Accounts.Employee.EmployeeRegisterActivity;
import com.example.attendancemonitoring.Accounts.Student.StudentRegisterActivity;
import com.example.attendancemonitoring.Helpers.UserHelper;
import com.example.attendancemonitoring.Repositories.UserRepository;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializeRegister();

        LinearLayout studentLayout = findViewById(R.id.studentLayout);
        LinearLayout employeeLayout = findViewById(R.id.employeeLayout);
        Button btnStudent = findViewById(R.id.btnStudent);
        Button btnEmployee = findViewById(R.id.btnEmployee);

        studentLayout.setOnClickListener(view -> this.registerForStudent());

        btnStudent.setOnClickListener(view -> this.registerForStudent());

        employeeLayout.setOnClickListener(view -> this.registerForEmployee());

        btnEmployee.setOnClickListener(view -> this.registerForEmployee());
    }


    private void initializeRegister() {
        if  (UserHelper.isUserAlreadyRegister(this) || UserRepository.isUserAlreadyRegister(this)) {
            Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }
    }

    private void registerForEmployee() {
        Intent intent = new Intent(this, EmployeeRegisterActivity.class);
        startActivity(intent);
    }

    private void registerForStudent() {
        Intent intent = new Intent(this, StudentRegisterActivity.class);
        startActivity(intent);
    }


}
