/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;


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
public class WebClient extends MainClient {
    private final WebTarget webTarget;
    private final Client client;

    WebClient(String uri, String endpoint) {
        client = ClientBuilder.newClient();
        webTarget = client.target(uri).path(endpoint);
    }

    <T> T load(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestEntity,MediaType.APPLICATION_JSON), responseType);
    }

    String getStatus() throws ClientErrorException {
        return webTarget.request(MediaType.TEXT_PLAIN).get(String.class);
    }

    public void close() {
        client.close();
    }

}
