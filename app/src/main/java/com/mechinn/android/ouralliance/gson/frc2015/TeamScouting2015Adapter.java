package com.mechinn.android.ouralliance.gson.frc2015;

import android.database.Cursor;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mechinn.android.ouralliance.data.MatchScouting;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.Wheel;
import com.mechinn.android.ouralliance.data.frc2015.MatchScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;
import com.mechinn.android.ouralliance.data.frc2015.Wheel2015;
import com.mechinn.android.ouralliance.gson.OurAllianceGson;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class TeamScouting2015Adapter implements JsonSerializer<TeamScouting2015>, JsonDeserializer<TeamScouting2015> {
    @Override
    public TeamScouting2015 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TeamScouting2015 scouting = new TeamScouting2015();
        JsonObject object = json.getAsJsonObject();
        scouting.setModified(new Date(object.get(TeamScouting2015.MODIFIED).getAsLong()));
        scouting.setTeam(OurAllianceGson.BUILDER.fromJson(object.get(TeamScouting2015.TEAM), Team.class));
        JsonElement element = object.get(TeamScouting2015.NOTES);
        if(null!=element) {
            scouting.setNotes(element.getAsString());
        }
        element = object.get(TeamScouting2015.ORIENTATION);
        if(null!=element) {
            scouting.setOrientation(element.getAsString());
        }
        element = object.get(TeamScouting2015.DRIVE_TRAIN);
        if(null!=element) {
            scouting.setDriveTrain(element.getAsString());
        }
        element = object.get(TeamScouting2015.WIDTH);
        if(null!=element) {
            scouting.setWidth(element.getAsDouble());
        }
        element = object.get(TeamScouting2015.LENGTH);
        if(null!=element) {
            scouting.setLength(element.getAsDouble());
        }
        element = object.get(TeamScouting2015.HEIGHT);
        if(null!=element) {
            scouting.setHeight(element.getAsDouble());
        }
        element = object.get(TeamScouting2015.COOP);
        if(null!=element) {
            scouting.setCoop(element.getAsBoolean());
        }element = object.get(TeamScouting2015.DRIVER_EXPERIENCE);
        if(null!=element) {
            scouting.setDriverExperience(element.getAsFloat());
        }
        element = object.get(TeamScouting2015.PICKUP_MECHANISM);
        if(null!=element) {
            scouting.setPickupMechanism(element.getAsString());
        }
        element = object.get(TeamScouting2015.MAX_TOTE_STACK);
        if(null!=element) {
            scouting.setMaxToteStack(element.getAsInt());
        }
        element = object.get(TeamScouting2015.MAX_CONTAINER_STACK);
        if(null!=element) {
            scouting.setMaxTotesStackContainer(element.getAsInt());
        }
        element = object.get(TeamScouting2015.MAX_TOTES_AND_CONTAINER_LITTER);
        if(null!=element) {
            scouting.setMaxTotesAndContainerLitter(element.getAsInt());
        }
        element = object.get(TeamScouting2015.HUMAN_PLAYER);
        if(null!=element) {
            scouting.setHumanPlayer(element.getAsFloat());
        }
        element = object.get(TeamScouting2015.NO_AUTO);
        if(null!=element) {
            scouting.setNoAuto(element.getAsBoolean());
        }
        element = object.get(TeamScouting2015.DRIVE_AUTO);
        if(null!=element) {
            scouting.setDriveAuto(element.getAsBoolean());
        }
        element = object.get(TeamScouting2015.TOTE_AUTO);
        if(null!=element) {
            scouting.setContainerAuto(element.getAsBoolean());
        }
        element = object.get(TeamScouting2015.STACKED_AUTO);
        if(null!=element) {
            scouting.setStackedAuto(element.getAsBoolean());
        }
        element = object.get(TeamScouting2015.LANDFILL_AUTO);
        if(null!=element) {
            scouting.setLandfillAuto(element.getAsInt());
        }
        return scouting;
    }
    @Override
    public JsonElement serialize(TeamScouting2015 src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(TeamScouting2015.MODIFIED,src.getModified().getTime());
        object.add(TeamScouting2015.TEAM, OurAllianceGson.BUILDER.toJsonTree(src.getTeam()));
        object.addProperty(TeamScouting2015.NOTES, src.getNotes());
        object.addProperty(TeamScouting2015.ORIENTATION, src.getOrientation());
        object.addProperty(TeamScouting2015.DRIVE_TRAIN, src.getDriveTrain());
        object.addProperty(TeamScouting2015.WIDTH, src.getWidth());
        object.addProperty(TeamScouting2015.LENGTH, src.getLength());
        object.addProperty(TeamScouting2015.HEIGHT, src.getHeight());
        object.addProperty(TeamScouting2015.COOP, src.getCoop());
        object.addProperty(TeamScouting2015.DRIVER_EXPERIENCE, src.getDriverExperience());
        object.addProperty(TeamScouting2015.PICKUP_MECHANISM, src.getPickupMechanism());
        object.addProperty(TeamScouting2015.MAX_TOTE_STACK, src.getMaxToteStack());
        object.addProperty(TeamScouting2015.MAX_CONTAINER_STACK, src.getMaxTotesStackContainer());
        object.addProperty(TeamScouting2015.MAX_TOTES_AND_CONTAINER_LITTER, src.getMaxTotesAndContainerLitter());
        object.addProperty(TeamScouting2015.HUMAN_PLAYER, src.getHumanPlayer());
        object.addProperty(TeamScouting2015.NO_AUTO,src.getNoAuto());
        object.addProperty(TeamScouting2015.DRIVE_AUTO,src.getDriveTrain());
        object.addProperty(TeamScouting2015.TOTE_AUTO,src.getToteAuto());
        object.addProperty(TeamScouting2015.CONTAINER_AUTO,src.getContainerAuto());
        object.addProperty(TeamScouting2015.STACKED_AUTO,src.getStackedAuto());
        object.addProperty(TeamScouting2015.LANDFILL_AUTO,src.getLandfillAuto());
        return object;
    }
}
