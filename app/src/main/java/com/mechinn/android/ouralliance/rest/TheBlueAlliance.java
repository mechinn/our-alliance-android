package com.mechinn.android.ouralliance.rest;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by mechinn on 3/7/14.
 */
public class TheBlueAlliance {
    public static final String TAG = "TheBlueAlliance";
    private static final RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("X-TBA-App-Id", "com.mechinn.android.ouralliance:FRC_mobile_scouting_application:2015");
        }
    };
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new JsonDateAdapter())
            .create();
    private static final RestAdapter restAdapter = new RestAdapter
            .Builder()
            .setEndpoint("http://www.thebluealliance.com/api/v2")
            .setRequestInterceptor(requestInterceptor)
            .setClient(new InterceptingOkClient())
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setConverter(new GsonConverter(gson))
            .build();
    private static final TheBlueAllianceAPIv2 service = restAdapter.create(TheBlueAllianceAPIv2.class);
    public static TheBlueAllianceAPIv2 getService() {
        return service;
    }
    public static class InterceptingOkClient extends OkClient {
        public InterceptingOkClient() {
        }

        public InterceptingOkClient(OkHttpClient client) {
            super(client);
        }

        @Override
        public Response execute(Request request) throws IOException {
            Response response = super.execute(request);
            for (Header header : response.getHeaders()) {
                Log.d(TAG, header.toString());
            }
            return response;
        }
    }
    public interface TheBlueAllianceAPIv2 {
        public static final String TAG = "TheBlueAllianceAPIv2";

        @GET("/teams/{page}")
        public List<Team> getTeamPage(@Path("page") String page);

        @GET("/teams/{page}")
        public void getTeamPage(@Path("page") String page, Callback<List<Team>> callback);

        @GET("/team/{team}")
        public Team getTeam(@Path("team") String team);

        @GET("/team/{team}")
        public void getTeam(@Path("team") String team, Callback<Team> callback);

        @GET("/team/{team}/{year}/events")
        public List<Event> getTeamEvents(@Path("team") String team, @Path("year") int year);

        @GET("/team/{team}/{year}/events")
        public void getTeamEvents(@Path("team") String team, @Path("year") int year, Callback<List<Event>> callback);

//        @GET("/team/{team}/event/{event}/awards")
//        public Team getTeamEventAwards(@Path("team") String team, @Path("event") int event);
//
//        @GET("/team/{team}/event/{event}/awards")
//        public void getTeamEventAwards(@Path("team") String team, @Path("event") int event, Callback<Team> callback);

        @GET("/team/{team}/event/{event}/matches")
        public List<Match> getTeamEventMatches(@Path("team") String team, @Path("event") int event);

        @GET("/team/{team}/event/{event}/matches")
        public void getTeamEventMatches(@Path("team") String team, @Path("event") int event, Callback<List<Match>> callback);

        @GET("/team/{team}/years_participated")
        public int[] getTeamYearsParticipated(@Path("team") String team);

        @GET("/team/{team}/years_participated")
        public void getTeamYearsParticipated(@Path("team") String team, Callback<int[]> callback);

//        @GET("/team/{team}/{year}/media")
//        public List<Multimedia> getTeamMedia(@Path("team") String team, @Path("year") int year);
//
//        @GET("/team/{team}/{year}/media")
//        public void getTeamMedia(@Path("team") String team, @Path("year") int year, Callback<List<Multimedia>> callback);

        @GET("/events/{year}")
        public List<Event> getEventList(@Path("year") int year);

        @GET("/events/{year}")
        public void getEventList(@Path("year") int year, Callback<List<Event>> callback);

        @GET("/event/{event}")
        public Event getEvent(@Path("event") String event);

        @GET("/event/{event}")
        public void getEvent(@Path("event") String event, Callback<Event> callback);

        @GET("/event/{event}/teams")
        public List<Team> getEventTeams(@Path("event") String event);

        @GET("/event/{event}/teams")
        public void getEventTeams(@Path("event") String event, Callback<List<Team>> callback);

        @GET("/event/{event}/matches")
        public List<Match> getEventMatches(@Path("event") String event);

        @GET("/event/{event}/matches")
        public void getEventMatches(@Path("event") String event, Callback<List<Match>> callback);

//        @GET("/event/{event}/stats")
//        public List<Match> getEventStats(@Path("event") String event);
//
//        @GET("/event/{event}/stats")
//        public void getEventStats(@Path("event") String event, Callback<List<Match>> callback);

//        @GET("/event/{event}/rankings")
//        public List<Match> getEventRankings(@Path("event") String event);
//
//        @GET("/event/{event}/rankings")
//        public void getEventRankings(@Path("event") String event, Callback<List<Match>> callback);

//        @GET("/event/{event}/awards")
//        public List<Match> getEventAwards(@Path("event") String event);
//
//        @GET("/event/{event}/awards")
//        public void getEventAwards(@Path("event") String event, Callback<List<Match>> callback);

//        @GET("/event/{event}/district_points")
//        public List<Match> getEventDistrictPoints(@Path("event") String event);
//
//        @GET("/event/{event}/district_points")
//        public void getEventDistrictPoints(@Path("event") String event, Callback<List<Match>> callback);

        @GET("/match/{match}")
        public Match getMatch(@Path("match") String match);

        @GET("/match/{match}")
        public void getMatch(@Path("match") String match, Callback<Match> callback);

//        @GET("/districts/{year}")
//        public List<Match> getDistrictList(@Path("year") int year);
//
//        @GET("/districts/{year}")
//        public void getDistrictList(@Path("year") int year, Callback<List<Match>> callback);

        @GET("/district/{district}/{year}/events")
        public List<Event> getDistrictEvents(@Path("district") District district,@Path("year") int year);

        @GET("/district/{district}/{year}/events")
        public void getDistrictEvents(@Path("district") District district,@Path("year") int year, Callback<List<Event>> callback);

//        @GET("/district/{district}/{year}/rankings")
//        public List<Match> getDistrictRankings(@Path("district") District district, @Path("year") int year);
//
//        @GET("/district/{district}/{year}/rankings")
//        public void getDistrictRankings(@Path("district") District district, @Path("year") int year, Callback<List<Match>> callback);
    }
    public enum District {
        NO_DISTRICT(""),
        MICHIGAN("fim"),
        MID_ATLANTIC("mar"),
        NEW_ENGLAND("ne"),
        PACIFIC_NORTHWEST("pnw"),
        INDIANA("in");
        private String value;
        private District(String value) {
            this.value = value;
        }
        public int getValue() {
            if(value.equals(MICHIGAN)) {
                return 1;
            } else if(value.equals(MID_ATLANTIC)) {
                return 2;
            } else if(value.equals(NEW_ENGLAND)) {
                return 3;
            } else if(value.equals(PACIFIC_NORTHWEST)) {
                return 4;
            } else if(value.equals(INDIANA)) {
                return 5;
            } else {
                return 0;
            }
        }
        public String toString() {
            if(value.equals(MICHIGAN)) {
                return "Michigan";
            } else if(value.equals(MID_ATLANTIC)) {
                return "Mid Atlantic";
            } else if(value.equals(NEW_ENGLAND)) {
                return "New England";
            } else if(value.equals(PACIFIC_NORTHWEST)) {
                return "Pacific Northwest";
            } else if(value.equals(INDIANA)) {
                return "Indiana";
            } else {
                return "";
            }
        }
    }
    public static String getEvent(int value) {
        switch(value) {
            case 0:
                return "Regional";
            case 1:
                return "District";
            case 2:
                return "District Championship";
            case 3:
                return "Championship Division";
            case 4:
                return "Championship Finals";
            case 99:
                return "Offseason";
            case 100:
                return "Preseason";
            default:
                return "--";
        }
    }
    public static District getDistrict(int value) {
        switch(value) {
            case 1:
                return District.MICHIGAN;
            case 2:
                return District.MID_ATLANTIC;
            case 3:
                return District.NEW_ENGLAND;
            case 4:
                return District.PACIFIC_NORTHWEST;
            case 5:
                return District.INDIANA;
            default:
                return District.NO_DISTRICT;
        }
    }
}
