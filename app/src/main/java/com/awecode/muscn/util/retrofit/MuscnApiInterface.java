package com.awecode.muscn.util.retrofit;

import com.awecode.muscn.model.http.fixtures.FixturesResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by munnadroid on 1/27/16.
 */
public interface MuscnApiInterface {

    @GET("api/v1/fixtures")
    Observable<FixturesResponse> getFixtures();



}
