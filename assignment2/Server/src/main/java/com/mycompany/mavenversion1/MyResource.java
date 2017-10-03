package com.mycompany.mavenversion1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Retrieves representation of an instance of server.ServerResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        return "alive";
    }

    /**
     * PUT method for updating or creating an instance of ServerResource
     * @param content representation for the resource
     * @return Length of content
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    
    public int postText(String content) {
        return content.length();
    }
}
