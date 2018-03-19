package fi.abloy.mapapplication;

import android.app.Application;

import fi.abloy.mapapplication.interfaces.INominatimLocationService;

import static fi.abloy.mapapplication.common.Extra.NOMINATIM_API_BASE_URL;


public class MapApplication extends Application {
    private static MapApplication instance;

    private INominatimLocationService iNominatimLocationService;

    public static MapApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        iNominatimLocationService = RetrofitClient.getClient(NOMINATIM_API_BASE_URL).create(INominatimLocationService.class);
    }

    public INominatimLocationService getApiLocationService() {
        return iNominatimLocationService;
    }

}
