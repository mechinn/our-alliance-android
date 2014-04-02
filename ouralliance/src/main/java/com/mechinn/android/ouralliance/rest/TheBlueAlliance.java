package com.mechinn.android.ouralliance.rest;

import android.app.IntentService;
import android.content.Intent;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

import java.util.List;
import java.util.Map;

/**
 * Created by mechinn on 3/7/14.
 */
public class TheBlueAlliance {
    public static final String TAG = "TheBlueAlliance";
    public interface TheBlueAllianceAPIv2 {
        public static final String TAG = "TheBlueAllianceAPIv2";
        public static final String URL = "http://www.thebluealliance.com/api/v2";
        public static final String HEADER = "X-TBA-App-Id: ouralliance:scouting:17";

        @Headers(HEADER)
        @GET("/team/{key}")
        public Team getTeam(@Path("teamKey") String key);

        @Headers(HEADER)
        @GET("/team/{key}")
        public void getTeam(@Path("teamKey") String key, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/team/{key}/{year}")
        public Team getTeam(@Path("teamKey") String key, @Path("year") int year);

        @Headers(HEADER)
        @GET("/team/{key}/{year}")
        public void getTeam(@Path("teamKey") String key, @Path("year") int year, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/events/{year}")
        public List<String> getEventList(@Path("year") int year);

        @Headers(HEADER)
        @GET("/events/{year}")
        public void getEventList(@Path("year") int year, Callback<List<String>> callback);

        @Headers(HEADER)
        @GET("/event/{key}")
        public Competition getEvent(@Path("key") String key);

        @Headers(HEADER)
        @GET("/event/{key}")
        public void getEvent(@Path("key") String key, Callback<Competition> callback);

        @Headers(HEADER)
        @GET("/event/{key}/teams")
        public List<Team> getEventTeams(@Path("key") String key);

        @Headers(HEADER)
        @GET("/event/{key}/teams")
        public void getEventTeams(@Path("key") String key, Callback<List<Team>> callback);

        @Headers(HEADER)
        @GET("/event/{key}/matches")
        public List<Match> getEventMatches(@Path("key") String key);

        @Headers(HEADER)
        @GET("/event/{key}/matches")
        public void getEventMatches(@Path("key") String key, Callback<List<Match>> callback);
    }
    private static final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(TheBlueAllianceAPIv2.URL).build();
    private static final TheBlueAllianceAPIv2 service = restAdapter.create(TheBlueAllianceAPIv2.class);
    public static TheBlueAllianceAPIv2 getService() {
        return service;
    }
}
