package com.sampleapp.samplearticleapp.controller;

import android.app.Activity;

import com.sampleapp.samplearticleapp.network.Response;
import com.sampleapp.samplearticleapp.network.ServiceRequest;
import com.sampleapp.samplearticleapp.view.IScreen;

public interface IController {
    IScreen getScreen();

    Activity getActivity();

    ServiceRequest getData(int dataType, Object requestData);

    ServiceRequest getData();

    void handleResponse(Response response);

    void parseResponse(Response response);

    void sendResponseToScreen(Response response);
}
