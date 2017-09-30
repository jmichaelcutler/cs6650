/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 *
 * @author jmich
 */
public class MyTask extends ThreadClient implements Callable<Map<Long, Long>>{
    private final WebClient client;
    private final int numIter;
    private static final long NUM_REQUESTS_INDEX = -1;
    private static final long NUM_SUCCESSES_INDEX = -2;

    public MyTask(int numIter, WebClient client) {
        this.numIter = numIter;
        this.client = client;
    }

    @Override
    public Map<Long, Long> call() throws Exception {
        Map<Long, Long> results = Collections.synchronizedMap(new HashMap<Long, Long>());
        long attempts = 0;
        long successes =  0;
        for(int i = 0; i < numIter; i++) {
            attempts += 2;
            Long time1 = System.currentTimeMillis();
            String status = client.getStatus();
            Long time2 = System.currentTimeMillis();
            int response = client.postText("blahb", Integer.class);
            Long time3 = System.currentTimeMillis();
            results.put(time1, time2);
            results.put(time2, time3);
            if(status.equals("alive")) {
                successes++;
            }
            if(response == 5) {
                successes++;
            }
        }
        results.put(NUM_REQUESTS_INDEX, attempts);
        results.put(NUM_SUCCESSES_INDEX, successes);
        return results;
    }
}
