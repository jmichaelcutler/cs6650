package com.mycompany.mavenversion1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Assignment 2 root resource(exposed at "ski_server" path)
 */
@Path("ski_server")
public class SkiServer {
    @GET
    @Path("/myvert/")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStats() {
        return "received";
    }

    @POST
    @Path("/load/")
    @Consumes(MediaType.TEXT_PLAIN)

    public int loadStats(String content) {
        return content.length();
    }
}
