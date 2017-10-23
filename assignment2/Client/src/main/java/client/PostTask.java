/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import bsdsass2testdata.RFIDLiftData;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author jmich
 */
public class PostTask extends MainClient implements Callable<List<PostResult>>{
    private final WebClient client;
    private final int blockNumber;
    private final int numThreads;
    private final List<RFIDLiftData> liftData;

    PostTask(int blockNumber, int numThreads, List<RFIDLiftData> liftData, WebClient client) {
        this.blockNumber = blockNumber;
        this.numThreads = numThreads;
        this.client = client;
        this.liftData = liftData;
    }

    @Override
    public List<PostResult> call() throws Exception {
        List<PostResult> results = new ArrayList<>();
        int startIndex = (liftData.size() / numThreads) * blockNumber;
        int endIndex = (liftData.size() / numThreads) * (blockNumber + 1);
        for(int request = startIndex; (request < liftData.size() && request < endIndex); request++) {
            RFIDLiftData singleRun = liftData.get(request);
            Gson gson = new Gson();
            String input = gson.toJson(singleRun);
            long start = System.currentTimeMillis();
            Integer response = client.postData(input, Integer.class);
            long stop = System.currentTimeMillis();
            PostResult postResult = new PostResult(start, stop - start, blockNumber, request, response == 200);
            results.add(postResult);
        }
        return results;
    }
}
