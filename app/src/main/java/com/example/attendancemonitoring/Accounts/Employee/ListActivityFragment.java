package com.example.attendancemonitoring.Accounts.Employee;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.Adapters.ServerActivityAdapter;
import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.R;

import java.util.ArrayList;
import java.util.List;


public class ListActivityFragment extends Fragment {

    List<Activity> activityList;
    ServerActivityAdapter serverActivityAdapter;


    public ListActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText searchField = view.findViewById(R.id.searchField);
        LinearLayout noDataLayout = view.findViewById(R.id.noDataAvailable);
        LinearLayout recyclerViewLayout = view.findViewById(R.id.recyclerViewLayout);
        activityList = DB.getInstance(getContext()).activityDao().getActivity();
        if(activityList.size() != 0) {
            this.buildRecyclerView(view);
            searchField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filter(s.toString());
                }
            });
        } else {
            noDataLayout.setVisibility(View.VISIBLE);
            recyclerViewLayout.setVisibility(View.GONE);
        }





    }

    private void filter(String text) {
        ArrayList<Activity> filteredList = new ArrayList<>();

        for (Activity item : activityList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        serverActivityAdapter.filterList(filteredList);
    }


    private void buildRecyclerView(View view) {


        serverActivityAdapter = new ServerActivityAdapter(activityList, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.list_activity_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),1));

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(serverActivityAdapter);

    }

}
