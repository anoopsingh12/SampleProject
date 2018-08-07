package com.sampleapp.samplearticleapp.controller;

import android.app.Activity;
import android.util.Log;

import com.sampleapp.samplearticleapp.constants.Constants;
import com.sampleapp.samplearticleapp.controller.parser.JsonParser;
import com.sampleapp.samplearticleapp.model.ResponseModel;
import com.sampleapp.samplearticleapp.network.HttpConnection;
import com.sampleapp.samplearticleapp.network.Response;
import com.sampleapp.samplearticleapp.network.ServiceRequest;
import com.sampleapp.samplearticleapp.view.IScreen;

import java.util.List;

public class RequestController implements IController {
    private static final String LOG_TAG = "RequestController";

    private Activity mActivity;
    private IScreen mIScreen;

    public RequestController(Activity activity, IScreen pScreen) {
        this.mActivity = activity;
        this.mIScreen = pScreen;
    }

    @Override
    public IScreen getScreen() {
        return mIScreen;
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public ServiceRequest getData(int dataType, Object requestData) {
        return null;
    }

    @Override
    public ServiceRequest getData() {
        ServiceRequest serviceRq = new ServiceRequest();
        serviceRq.setResponseController(this);
        serviceRq.setPriority(HttpConnection.PRIORITY.LOW);
        serviceRq.setUrl(Constants.TIMES_API);
        serviceRq.setHttpMethod(HttpConnection.HTTP_METHOD.GET);
        HttpConnection connection = new HttpConnection();
        connection.execute(serviceRq);
        return serviceRq;
    }

    @Override
    public void handleResponse(Response response) {
        if (response.getResponseData() instanceof byte[]) {
            try {
                parseResponse(response);
            } catch (Exception e) {
                Log.i(LOG_TAG, "parseJsonData()");
                response.setSuccess(false);
            }
        }
        sendResponseToScreen(response);
    }

    @Override
    public void parseResponse(Response response) {
        if(response.getResponseData() != null) {
            response.setSuccess(true);
            List<ResponseModel> responseModelList = JsonParser.parseJsonData(response);
            response.setResponseObject(responseModelList);
        } else {
            response.setSuccess(false);
            response.setResponseObject(null);
        }

    }

    @Override
    public void sendResponseToScreen(final Response response) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    getScreen().handleUiUpdate(response);
                } catch (Throwable tr) {
                    Log.e(LOG_TAG, "sendResponseToScreen()", tr);
                }
            }
        });
    }
}
