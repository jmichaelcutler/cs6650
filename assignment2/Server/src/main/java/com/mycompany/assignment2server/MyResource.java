package com.mycompany.assignment2server;

import bsdsass2testdata.RFIDLiftData;
import com.google.gson.Gson;
import db.DatabaseManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "assignment2server" path)
 */
@Path("assignment2server")
public class MyResource {

    /**
     * Retrieves representation of an instance of server.ServerResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String myVert(GetRequest request) {
        DatabaseManager dbmanager = DatabaseManager.createDBClient();
        UserStat result = dbmanager.getFromDB(request.getSkierId(), request.getDayId());
        return new Gson().toJson(result);
    }

    /**
     * PUT method for updating or creating an instance of ServerResource
     * @param liftData representation for the resource
     * @return Length of content
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int postData(RFIDLiftData liftData) {
        if(liftData == null) {
            return 400;
        }
        DatabaseManager dbmanager = DatabaseManager.createDBClient();
        if(liftData.getSkierID() < 0) {
            dbmanager.process(liftData.getDayNum());
        } else {
            dbmanager.postToDB(liftData);
        }
        return 200;
    }
}
