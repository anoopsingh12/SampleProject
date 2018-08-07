package com.sampleapp.samplearticleapp.network;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Class takes care of HttpConnection and returns response to controller
 */
public class HttpConnection extends Thread {
    private static HttpConnection instance;
    private final String LOG_TAG = "HttpConnection";
    private StatusCodeChecker defaultStatusCodeChecker;
    private boolean isRunning;
    private Vector<ServiceRequest> highPriorityQueue;
    private Vector<ServiceRequest> lowPriorityQueue;
    private ServiceRequest currentRequest;
    public HttpConnection() {
        defaultStatusCodeChecker = new StatusCodeChecker() {
            @Override
            public boolean isSuccess(int statusCode) {
                return statusCode == 200;
            }
        };
    }

    /**
     * Request to execute
     * @param request
     */
    public void execute(ServiceRequest request) {
        highPriorityQueue = new Vector<ServiceRequest>();
        lowPriorityQueue = new Vector<ServiceRequest>();
        isRunning = true;
        addRequest(request);
        start();
    }

    @Override
    public void run() {
        while (isRunning) {
            if (nextRequest()) {
                executeRequest();
            } else {
                try {
                    Thread.sleep(10 * 60);// 10 min sleep
                } catch (InterruptedException e) {
                    Log.i(LOG_TAG, "" + e);
                }
            }
        }
    }

    private boolean nextRequest() {
        if (highPriorityQueue.size() > 0) {
            currentRequest = (ServiceRequest) highPriorityQueue.remove(0);
        } else if (lowPriorityQueue.size() > 0) {
            currentRequest = (ServiceRequest) lowPriorityQueue.remove(0);
        } else {
            currentRequest = null;
        }

        return currentRequest != null;
    }

    public void executeRequest() {
        if (currentRequest.isCancelled()) {
            return;
        }
        /**
         * Check if device is connected.
         */
        Activity activity = currentRequest.getResponseController().getActivity();
        if (activity != null && !ConnectivityUtils.isNetworkEnabled(activity)) {
            notifyError("Device is out of network coverage.", null);
            return;
        }

        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        try {
            Log.i(LOG_TAG, "Request URL: " + currentRequest.getUrl());
            URL url = new URL(currentRequest.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            switch (currentRequest.getHttpMethod()) {
                case HTTP_METHOD.GET: {
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");
                    break;
                }
            }

            int status = connection.getResponseCode();
            InputStream responseContentStream = connection.getContent() == null ? null : (InputStream) connection.getContent();
            Log.i(LOG_TAG, "Status Code " + status);
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            Response response = new Response();
            if (responseContentStream != null) {
                if (currentRequest.getIsCompressed()) {
                    responseContentStream = new GZIPInputStream(responseContentStream);
                }
                if (currentRequest.isCancelled()) {
                    return;
                }
                response.setResponseData(convertStreamToBytes(responseContentStream));
            }

            if (currentRequest.isCancelled()) {
                return;
            }
            currentRequest.getResponseController().handleResponse(response);
        } catch (MalformedURLException me) {
            Log.e(LOG_TAG, "MalformedURLException", me);
            notifyError("MalformedURLException", me);
        } catch (IOException io) {
            Log.e(LOG_TAG, "IOException", io);
            notifyError("IOException", io);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception", e);
            notifyError("Exception", e);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * @param errorMessage
     * @param exception
     */
    private void notifyError(String errorMessage, Exception exception) {
        if (exception == null) {
            Log.e(LOG_TAG, "Error Response: " + errorMessage);
        } else {
            Log.e(LOG_TAG, "Error Response: " + errorMessage, exception);
        }
        Response response = new Response();
        response.setErrorMessage(errorMessage);
        response.setSuccess(false);
        response.setException(exception);
        if (currentRequest.isCancelled()) {
            return;
        }
        currentRequest.getResponseController().handleResponse(response);
    }

    public void addRequest(ServiceRequest request) {
        try {
            if (request.getPriority() == PRIORITY.HIGH) {
                highPriorityQueue.add(0, request);
            } else {
                lowPriorityQueue.addElement(request);
            }
            interrupt();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "addRequest()", ex);
        }
    }

    /**
     * @return the currentRequest
     */
    public ServiceRequest getCurrentRequest() {
        return currentRequest;
    }

    /**
     * @return the nextRequest
     */
    public ServiceRequest getNextRequest() {
        if (highPriorityQueue.size() > 0) {
            return highPriorityQueue.get(0);
        } else if (lowPriorityQueue.size() > 0) {
            return lowPriorityQueue.get(0);
        } else {
            return null;
        }
    }

    /**
     * @return true if pRequest is found and removed from high/low queue.
     */
    public boolean removeRequest(ServiceRequest pRequest, Comparator<ServiceRequest> pComparator) {
        ServiceRequest tempRq = null;
        Vector<ServiceRequest> targetQueue = lowPriorityQueue;
        if (pRequest.getPriority() == PRIORITY.HIGH) {
            targetQueue = highPriorityQueue;
        }
        for (int i = 0; i < targetQueue.size(); i++) {
            try {
                tempRq = targetQueue.get(i);
            } catch (Exception e) {
                return false;
            }
            if (tempRq != null && pComparator.compare(tempRq, pRequest) == 0) {
                return targetQueue.removeElement(tempRq);
            }
        }
        return false;
    }

    /**
     * {@link ServiceRequest} with {@link PRIORITY#HIGH} are executed before {@link ServiceRequest} with {@link PRIORITY#LOW}
     */
    public static interface PRIORITY {
        /**
         * When-ever a new {@link ServiceRequest} with {@link PRIORITY#LOW} is added,
         * it gets lower priority than previous requests with same priority.
         */
        public static byte LOW = 0;
        /**
         * When-ever a new {@link ServiceRequest} with {@link PRIORITY#HIGH} is added,
         * it gets higher priority than previous requests with same priority.
         */
        public static byte HIGH = 1;
    }

    public static interface HTTP_METHOD {
        public static byte GET = 0;
        public static byte POST = 1;
        public static byte PUT = 2;
        public static byte DELETE = 3;
    }

    /**
     * Specific instance of StatusCodeChecker can be set in ServiceRequest
     */
    public static interface StatusCodeChecker {
        boolean isSuccess(int statusCode);
    }

    /**
     * @param pInputStream
     * @return
     */
    private byte[] convertStreamToBytes(InputStream pInputStream) {
        int read = 0;
        byte[] data = new byte[1024];    /** data will be read in chunks */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((read = pInputStream.read(data)) != -1) {
                baos.write(data, 0, read);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
