package fi.mapapplication;


import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import fi.mapapplication.common.Extra;
import fi.mapapplication.data.Location;
import fi.mapapplication.interfaces.IResponseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NomantimApiRequest {
    //Getting the response from Nomantim Api and placing the result in callback interface
    public static void getLocation(String searchItem, ArrayList<String> excludedIdsList, final IResponseHelper iResponseHelper) {
        String excludedIdString = TextUtils.join(",", excludedIdsList);
        MapApplication.getInstance().getApiLocationService().getLocation(searchItem, Extra.COORDINATES_TYPE, "json", Extra.FETCH_RECORDS_LIMIT, excludedIdString).enqueue(new Callback<ArrayList<Location>>() {
            @Override
            public void onResponse(Call<ArrayList<Location>> call, Response<ArrayList<Location>> response) {
                if (response.isSuccessful()) {
                    iResponseHelper.getData(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Location>> call, Throwable t) {
                Log.d("ERROR", "Failed Get Request " + t.getMessage());
            }
        });
    }
}
