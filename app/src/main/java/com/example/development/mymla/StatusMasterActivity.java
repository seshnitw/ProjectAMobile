package com.example.development.mymla;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import Adapters.Divider;
import Adapters.DividerItemDecoration;
import Adapters.StatusAdapter;
import Models.Status;
import RestService.IMyMLAService;
import RestService.MyMLAServiceApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusMasterActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_master);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reports_recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        IMyMLAService apiService =
                MyMLAServiceApiClient.getClient().create(IMyMLAService.class);

        Call<LinkedHashMap<String,Status>> call = apiService.getAllStatus();
        call.enqueue(new Callback<LinkedHashMap<String,Status>>() {
            @Override
            public void onResponse(Call<LinkedHashMap<String,Status>> call, Response<LinkedHashMap<String,Status>> response) {
                int statusCode = response.code();
                LinkedHashMap<String,Status> status = response.body();
                List<Status> statusvalues = new ArrayList<Status>(status.values());
                List<String> keys = new ArrayList<String>(status.keySet());
                for(int i=0;i<statusvalues.size();i++)
                {
                    statusvalues.get(i).complaintNo = keys.get(i);
                }
                recyclerView.setAdapter(new StatusAdapter(statusvalues, R.layout.list_item, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<LinkedHashMap<String,Status>> call, Throwable t) {

                Log.e(TAG, t.toString());
            }
        });
    }
}