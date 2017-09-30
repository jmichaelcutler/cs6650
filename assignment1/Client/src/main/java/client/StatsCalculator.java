package client;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class StatsCalculator extends ThreadClient {
    private static final long ATTEMPT_KEY = -1;
    private static final long SUCCESS_KEY = -2;

    public StatsCalculator() {
    }

    public long calculateCount(List<Future<Map<Long, Long>>> futures, long key) throws ExecutionException, InterruptedException {
        long result = 0;
        for(Future<Map<Long, Long>> future : futures) {
            result += future.get().get(key);
        }
        return result;
    }

    public List<Long> calculateLatencies(List<Future<Map<Long, Long>>> futures) throws ExecutionException, InterruptedException {
        List<Long> latencyList = new ArrayList<>();
        for(Future<Map<Long, Long>> future : futures) {
            Map<Long, Long> currentMap = future.get();
            for(long key : currentMap.keySet()) {
                if(key != ATTEMPT_KEY && key != SUCCESS_KEY) {
                    Long latency = currentMap.get(key) - key;
                    latencyList.add(latency);
                }
            }
        }
        Collections.sort(latencyList);
        return latencyList;
    }

    public double calculateMeanLatency(List<Long> latencies) {
        long latencySum = 0;
        for(Long latency : latencies) {
            latencySum += latency;
        }
        return ((double) latencySum) / latencies.size();
    }

    public long calculateNthPercentile(List<Long> latencies, double percentile) {

        int index = (int) Math.ceil(percentile * latencies.size());
        return latencies.get(index - 1);
    }
}
