/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;


/**
 * Jersey REST client generated for REST resource:MyResource [myresource]<br>
 * USAGE:
 * <pre>
 *        WebClient client = new WebClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author jmich
 */
public class WebClient extends ThreadClient {
    private final WebTarget webTarget;
    private final Client client;

    private static final String BASE_URI = "http://52.40.166.203:8080/mavenVersion1/webapi";

    public WebClient() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("myresource");
    }

    public WebClient(String host, String port) {
        client = ClientBuilder.newClient();
        String URI = "http://" + host + ":" + port + "/mavenVersion1/webapi/";
        webTarget = client.target(URI).path("myresource");
    }

    public <T> T postText(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(MediaType.TEXT_PLAIN).post(Entity.entity(requestEntity, MediaType.TEXT_PLAIN), responseType);
    }

    public String getStatus() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(MediaType.TEXT_PLAIN).get(String.class);
    }

    public void close() {
        client.close();
    }

}
