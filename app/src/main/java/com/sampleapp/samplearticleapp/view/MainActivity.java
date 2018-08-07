package com.sampleapp.samplearticleapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.sampleapp.samplearticleapp.R;
import com.sampleapp.samplearticleapp.controller.RequestController;
import com.sampleapp.samplearticleapp.model.ResponseModel;
import com.sampleapp.samplearticleapp.network.ConnectivityUtils;
import com.sampleapp.samplearticleapp.network.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IScreen {
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.article_list);

        if(ConnectivityUtils.isNetworkEnabled(this)){
            mProgressBar.setVisibility(View.VISIBLE);
            new RequestController(this,this).getData();
        }
    }

    @Override
    public void handleUiUpdate(Response response) {
        if (isFinishing()) {
            return;
        }
        try {
            updateUi(response);
        } catch (Exception e) {
            Log.i(getClass().getSimpleName(), "updateUi()", e);
        }
    }

    private void updateUi(Response response) {
        if(response.isSuccess()){
            mProgressBar.setVisibility(View.GONE);
            List<ResponseModel> responseModelList = (ArrayList) response.getResponseObject();
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(responseModelList, this);
           // mRecyclerView.setAdapter(recyclerAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.setAdapter( recyclerAdapter );
        }
    }
}

