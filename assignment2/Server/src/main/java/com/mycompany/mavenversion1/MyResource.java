package com.mycompany.mavenversion1;

import bsdsass2testdata.RFIDLiftData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.crypto.Data;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoTimeoutException;
import db.DatabaseManager;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Retrieves representation of an instance of server.ServerResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("myvert/{id}/{dayNum}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus(@PathParam("id")int id, @PathParam("dayNum") int dayNum) {
        try {
            DatabaseManager db = DatabaseManager.createDBClient(dayNum);
            UserStat stat = db.getOneUserStats(id, dayNum);
            db.close();
            Gson gson = new Gson();
            return gson.toJson(stat);
        } catch (MongoTimeoutException exception) {
            exception.printStackTrace();

        }
        return null;
    }

    @GET
    @Path("collate/{day}")
    public int cacheUserStats(@PathParam("day") int dayId) throws InterruptedException {
        DatabaseManager dbman = DatabaseManager.createDBClient(dayId);
        dbman.cacheUsers(dayId);
        dbman.close();
        return 200;
    }

    /**
     * PUT method for updating or creating an instance of ServerResource
     *
     * @param json representation for the resource
     * @return Length of content
     */
    @POST
    @Path("loadAll")
    @Consumes(MediaType.TEXT_PLAIN)

    public int postText(String json) throws ExecutionException, InterruptedException {
        Type type = new TypeToken<List<RFIDLiftData>>() {
        }.getType();
        List<RFIDLiftData> input = new Gson().fromJson(json, type);
        DatabaseManager dbMan = DatabaseManager.createDBClient(input.get(0).getDayNum());
        dbMan.postBatchToDB(input);
        dbMan.close();
        return 200;
    }

    @POST
    @Path("dropDB")
    public int dropDB(String json) throws InterruptedException {
        Type type = new TypeToken<Integer>() {
        }.getType();
        int day = new Gson().fromJson(json, type);
        DatabaseManager dbMan = DatabaseManager.createDBClient(day);
        return dbMan.dropDB(day);
    }

    @POST
    @Path("load")
    @Consumes(MediaType.TEXT_PLAIN)
    public int postOne(String json) {
        Type type = new TypeToken<RFIDLiftData>() {
        }.getType();
        RFIDLiftData input = new Gson().fromJson(json, type);
        DatabaseManager dbMan = DatabaseManager.createDBClient(input.getDayNum());
        dbMan.postOneToDB(input);
        dbMan.close();
        return 200;
    }
}
