/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import bsdsass2testdata.RFIDLiftData;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author jmich
 */
public class PostTask extends MainClient implements Callable<List<PostResult>>{
    private final String uri;
    private final String endpoint;
    private final int blockNumber;
    private final int numThreads;
    private final List<RFIDLiftData> liftData;

    PostTask(int blockNumber, int numThreads, List<RFIDLiftData> liftData, String uri, String endpoint) {
        this.blockNumber = blockNumber;
        this.numThreads = numThreads;
        this.uri = uri;
        this.liftData = liftData;
        this.endpoint = endpoint;
    }

    @Override
    public List<PostResult> call() throws Exception {
        WebClient client = new WebClient(uri, endpoint);
        List<PostResult> results = new ArrayList<>();
        int startIndex = (liftData.size() / numThreads) * blockNumber;
        int endIndex = (liftData.size() / numThreads) * (blockNumber + 1);
        for(int request = startIndex; (request < liftData.size() && request < endIndex); request++) {
            RFIDLiftData singleRun = liftData.get(request);
//            JSONObject json = new JSONObject();
//            json.put("resortID", singleRun.getResortID());
//            json.put("dayNum", singleRun.getDayNum());
//            json.put("skierID", singleRun.getSkierID());
//            json.put("liftID", singleRun.getLiftID());
//            json.put("time", singleRun.getTime());
            long start = System.currentTimeMillis();
            Integer response = client.load("alive", Integer.class);
            long stop = System.currentTimeMillis();
            PostResult postResult = new PostResult(start, stop - start, blockNumber, request, response == 200);
            results.add(postResult);
        }
        if(endIndex == liftData.size() - 1) {
            RFIDLiftData endFlag = new RFIDLiftData(-1, -1, -1, -1, -1);
            client.load(endFlag, Integer.class);
        }
        client.close();
        wait(10);
        return results;
    }
}
