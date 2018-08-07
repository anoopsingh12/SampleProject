package com.sampleapp.samplearticleapp.controller.parser;

import android.util.Log;

import com.sampleapp.samplearticleapp.model.MediaMetadata;
import com.sampleapp.samplearticleapp.model.MediaModel;
import com.sampleapp.samplearticleapp.model.ResponseModel;
import com.sampleapp.samplearticleapp.network.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains all the parsing methods
 */
public class JsonParser {
    private static final String LOG_TAG = "JsonParser";

    /**
     * Method to parse the complete JSON response
     * @param response
     * @return
     */
    public static List<ResponseModel> parseJsonData(Response response){
       List<ResponseModel> responseModelList = new ArrayList<>();
        try{
            String result = new String(response.getResponseData());
            JSONObject jsonObject = new JSONObject(result);
            JSONArray resultsArray = jsonObject.getJSONArray(JsonKeys.JSON_KEY_RESULT);
            for(int i = 0; i < resultsArray.length(); i++){
                //Create an object of response
                ResponseModel responseModel = new ResponseModel();
                responseModelList.add(responseModel);
                JSONObject resultsObject = resultsArray.getJSONObject(i);
                responseModel.setTitle(resultsObject.optString(JsonKeys.JSON_KEY_TITLE));
                responseModel.setByLine(resultsObject.optString(JsonKeys.JSON_KEY_BYLINE));
                responseModel.setPublished_date(resultsObject.optString(JsonKeys.JSON_KEY_PUBLISHED_DATE));

                //Retrieve Media from resultObject
                JSONArray mediaArray = resultsObject.getJSONArray(JsonKeys.JSON_KEY_MEDIA);
                for(int j =0; j < mediaArray.length(); j++){
                    List<MediaModel> mediaModelList = new ArrayList<>();
                    MediaModel mediaModel = new MediaModel();
                    mediaModelList.add(mediaModel);
                    //Get the media object from media array
                    JSONObject mediaObject = mediaArray.getJSONObject(j);
                    mediaModel.setCaption(mediaObject.optString(JsonKeys.JSON_KEY_CAPTION));
                    mediaModel.setCopyright(mediaObject.optString(JsonKeys.JSON_KEY_COPYRIGHT));
                    mediaModel.setType(mediaObject.optString(JsonKeys.JSON_KEY_TYPE));

                    //Get media-metadata from mediaObject
                    JSONArray mediaMetadataArray = mediaObject.getJSONArray(JsonKeys.JSON_KEY_MEDIA_METADATA);
                    List<MediaMetadata> mediaMetadataList = new ArrayList<>();
                    for(int k =0; k < mediaMetadataArray.length(); k++){
                        MediaMetadata mediaMetadata = new MediaMetadata();
                        mediaMetadataList.add(mediaMetadata);

                        //Get metadata object from mediaMetadataArray
                        JSONObject mediaMetadataObject = mediaMetadataArray.getJSONObject(k);
                        mediaMetadata.setUrl(mediaMetadataObject.optString(JsonKeys.JSON_KEY_URL));
                        mediaMetadata.setFormat(mediaMetadataObject.optString(JsonKeys.JSON_KEY_FORMAT));
                        mediaMetadata.setHeight(mediaMetadataObject.optInt(JsonKeys.JSON_KEY_HEIGHT));
                        mediaMetadata.setWidth(mediaMetadataObject.optInt(JsonKeys.JSON_KEY_WIDTH));
                    }
                    mediaModel.setMediaMetadataList(mediaMetadataList);
                    responseModel.setMediaModelList(mediaModelList);
                }
            }
        } catch (JSONException je){
            Log.i(LOG_TAG, "Exception in (" + "getResultsList()" + ")");
        }

        return responseModelList;
    }
}
