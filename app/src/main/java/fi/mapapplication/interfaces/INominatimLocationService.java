package fi.mapapplication.interfaces;


import java.util.ArrayList;

import fi.mapapplication.data.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface INominatimLocationService {
    @Headers("accept-language:en-US")
    @GET("/search.php")
    Call<ArrayList<Location>> getLocation(@Query("q") String query,
                                          @Query("polygon_geojson") int coordinatesType,
                                          @Query("format") String responseFormat,
                                          @Query("limit") int limit,
                                          @Query("exclude_place_ids") String excludedIdsList);


}
