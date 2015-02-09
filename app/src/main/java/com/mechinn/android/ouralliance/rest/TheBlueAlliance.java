package com.mechinn.android.ouralliance.rest;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

import java.util.List;

/**
 * Created by mechinn on 3/7/14.
 */
public class TheBlueAlliance {
    public static final String TAG = "TheBlueAlliance";
    public interface TheBlueAllianceAPIv2 {
        public static final String TAG = "TheBlueAllianceAPIv2";
        public static final String URL = "http://www.thebluealliance.com/api/v2";
        public static final String HEADER = "X-TBA-App-Id: com.mechinn.android.ouralliance:FRC_mobile_scouting_application:2015";

        @Headers(HEADER)
        @GET("/teams/{page}")
        public Team getTeamPage(@Path("page") String page);

        @Headers(HEADER)
        @GET("/teams/{page}")
        public void getTeamPage(@Path("page") String page, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/team/{team}")
        public Team getTeam(@Path("team") String team);

        @Headers(HEADER)
        @GET("/team/{team}")
        public void getTeam(@Path("team") String team, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/team/{team}/{year}/events")
        public Team getTeamEvents(@Path("team") String team, @Path("year") int year);

        @Headers(HEADER)
        @GET("/team/{team}/{year}/events")
        public void getTeamEvents(@Path("team") String team, @Path("year") int year, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/team/{team}/event/{event}/awards")
        public Team getTeamEventAwards(@Path("team") String team, @Path("event") int event);

        @Headers(HEADER)
        @GET("/team/{team}/event/{event}/awards")
        public void getTeamEventAwards(@Path("team") String team, @Path("event") int event, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/team/{team}/event/{event}/matches")
        public Team getTeamEventMatches(@Path("team") String team, @Path("event") int event);

        @Headers(HEADER)
        @GET("/team/{team}/event/{event}/matches")
        public void getTeamEventMatches(@Path("team") String team, @Path("event") int event, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/team/{team}/years_participated")
        public Team getTeamYearsParticipated(@Path("team") String team);

        @Headers(HEADER)
        @GET("/team/{team}/years_participated")
        public void getTeamYearsParticipated(@Path("team") String team, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/team/{team}/{year}/media")
        public Team getTeamMedia(@Path("team") String team, @Path("year") int year);

        @Headers(HEADER)
        @GET("/team/{team}/{year}/media")
        public void getTeamMedia(@Path("team") String team, @Path("year") int year, Callback<Team> callback);

        @Headers(HEADER)
        @GET("/events/{year}")
        public List<String> getEventList(@Path("year") int year);

        @Headers(HEADER)
        @GET("/events/{year}")
        public void getEventList(@Path("year") int year, Callback<List<String>> callback);

        @Headers(HEADER)
        @GET("/event/{event}")
        public Competition getEvent(@Path("event") String event);

        @Headers(HEADER)
        @GET("/event/{event}")
        public void getEvent(@Path("event") String event, Callback<Competition> callback);

        @Headers(HEADER)
        @GET("/event/{event}/teams")
        public List<Team> getEventTeams(@Path("event") String event);

        @Headers(HEADER)
        @GET("/event/{event}/teams")
        public void getEventTeams(@Path("event") String event, Callback<List<Team>> callback);

        @Headers(HEADER)
        @GET("/event/{event}/matches")
        public List<Match> getEventMatches(@Path("event") String event);

        @Headers(HEADER)
        @GET("/event/{event}/matches")
        public void getEventMatches(@Path("event") String event, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/event/{event}/stats")
        public List<Match> getEventStats(@Path("event") String event);

        @Headers(HEADER)
        @GET("/event/{event}/stats")
        public void getEventStats(@Path("event") String event, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/event/{event}/rankings")
        public List<Match> getEventRankings(@Path("event") String event);

        @Headers(HEADER)
        @GET("/event/{event}/rankings")
        public void getEventRankings(@Path("event") String event, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/event/{event}/awards")
        public List<Match> getEventAwards(@Path("event") String event);

        @Headers(HEADER)
        @GET("/event/{event}/awards")
        public void getEventAwards(@Path("event") String event, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/event/{event}/district_points")
        public List<Match> getEventDistrictPoints(@Path("event") String event);

        @Headers(HEADER)
        @GET("/event/{event}/district_points")
        public void getEventDistrictPoints(@Path("event") String event, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/match/{match}")
        public List<Match> getMatch(@Path("match") String match);

        @Headers(HEADER)
        @GET("/match/{match}")
        public void getMatch(@Path("match") String match, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/districts/{year}")
        public List<Match> getDistrictList(@Path("year") int year);

        @Headers(HEADER)
        @GET("/districts/{year}")
        public void getDistrictList(@Path("year") int year, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/district/{district}/{year}/events")
        public List<Match> getDistrictEvents(@Path("district") District district,@Path("year") int year);

        @Headers(HEADER)
        @GET("/district/{district}/{year}/events")
        public void getDistrictEvents(@Path("district") District district,@Path("year") int year, Callback<List<Match>> callback);

        @Headers(HEADER)
        @GET("/district/{district}/{year}/rankings")
        public List<Match> getDistrictRankings(@Path("district") District district, @Path("year") int year);

        @Headers(HEADER)
        @GET("/district/{district}/{year}/rankings")
        public void getDistrictRankings(@Path("district") District district, @Path("year") int year, Callback<List<Match>> callback);
    }
    private static final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(TheBlueAllianceAPIv2.URL).build();
    private static final TheBlueAllianceAPIv2 service = restAdapter.create(TheBlueAllianceAPIv2.class);
    public static TheBlueAllianceAPIv2 getService() {
        return service;
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
