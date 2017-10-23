package com.mycompany.assignment2server;

import bsdsass2testdata.RFIDLiftData;

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
    @GET
    @Path("myvert")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        return "alive";
    }

    /**
     * PUT method for updating or creating an instance of ServerResource
     * @param liftData representation for the resource
     * @return Length of content
     */
    @Path("load")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    
    public int postData(RFIDLiftData liftData) {
        return 200;
    }
}
