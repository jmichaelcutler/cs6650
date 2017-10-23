package client;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class StatsCalculator {
    private static final int NUM_ATTEMPTS_FLAG = 1;
    private static final int NUM_SUCCESS_FLAG = 2;

    long calculateCount(List<Future<List<PostResult>>> futures, int flag) throws ExecutionException, InterruptedException {
        long result = 0;
        for(Future<List<PostResult>> future : futures) {
            List<PostResult> postResults = future.get();
            if(flag == NUM_ATTEMPTS_FLAG) {
                result += postResults.size();
            } else if (flag == NUM_SUCCESS_FLAG) {
                for(PostResult postResult : postResults) {
                    if(postResult.getSuccess()) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    List<Long> calculateLatencies(List<Future<List<PostResult>>> futures) throws ExecutionException, InterruptedException {
        List<Long> latencyList = new ArrayList<>();
        for(Future<List<PostResult>> future : futures) {
            List<PostResult> results = future.get();
            for(PostResult result : results) {
                latencyList.add(result.getLatency());
            }
        }
//        for(Future<Map<Long, Long>> future : futures) {
//            Map<Long, Long> currentMap = future.get();
//            for(long key : currentMap.keySet()) {
//                if(key != ATTEMPT_KEY && key != SUCCESS_KEY) {
//                    Long latency = currentMap.get(key) - key;
//                    latencyList.add(latency);
//                }
//            }
//        }
        Collections.sort(latencyList);
        return latencyList;
    }

    double calculateMeanLatency(List<Long> latencies) {
        long latencySum = 0;
        for(Long latency : latencies) {
            latencySum += latency;
        }
        return ((double) latencySum) / latencies.size();
    }

    long calculateNthPercentile(List<Long> latencies, double percentile) {
        int index = (int) Math.ceil(percentile * latencies.size());
        return latencies.get(index - 1);
    }
}
