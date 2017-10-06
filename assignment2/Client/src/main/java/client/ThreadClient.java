/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This program creates a given number of threads (default 10), each sending a GET and POST request to the server a
 * given number of times (default 100 iterations, default server IP: 52.40.166.203 default port: 8080). It waits until
 * all threads have finished their work, then collects, computes,  and outputs statistics about server latency.
 */
public class ThreadClient {
    private static final int DEFAULT_THREAD_NUM = 10;
    private static final int DEFAULT_ITER_NUM = 100;
    private static final long NUM_ATTEMPTS_KEY = -1;
    private static final long NUM_SUCCESS_KEY = -2;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads;
        int numIter;
        long numAttempt;
        long numSuccess;

        if (args.length >= 2) {
            numThreads = Integer.valueOf(args[0]);
            numIter = Integer.valueOf(args[1]);
        } else {
            numThreads = DEFAULT_THREAD_NUM;
            numIter = DEFAULT_ITER_NUM;
        }
        WebClient client;
        if(args.length == 4) {
            client = new WebClient(args[2], args[3]);
        } else {
            client = new WebClient();
        }
        System.out.println("Number of threads: " + numThreads);
        System.out.println("Number of iterations per thread: " + numIter);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads + 1);

        List<MyTask> threadList = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            threadList.add(new MyTask(numIter, client));
        }

        long start = System.currentTimeMillis();
        System.out.println("Client starting.....Time: " + start);
        System.out.println("All threads running...");
        List<Future<Map<Long, Long>>> futureList = executor.invokeAll(threadList);
        long stop = System.currentTimeMillis();
        System.out.println("All threads complete....Time: " + stop);
        executor.shutdown();
        StatsCalculator statCalc = new StatsCalculator();
        numAttempt = statCalc.calculateCount(futureList, NUM_ATTEMPTS_KEY);
        numSuccess = statCalc.calculateCount(futureList, NUM_SUCCESS_KEY);

        double wallTime = ((double) (stop - start)) / 1000;
        System.out.println("Wall time: " + wallTime + " seconds");
        List<Long> latencies = statCalc.calculateLatencies(futureList);

        System.out.println("Number of requests sent to server: " + numAttempt);
        System.out.println("Number of successful requests: " + numSuccess);

        System.out.println("Mean latency: " + statCalc.calculateMeanLatency(latencies) + " ms");
        System.out.println("Median latency: " + statCalc.calculateNthPercentile(latencies, 0.5) + " ms");
        System.out.println("95th percentile: " + statCalc.calculateNthPercentile(latencies, .95) + " ms");
        System.out.println("99th percentile: " + statCalc.calculateNthPercentile(latencies, .99) + " ms");
    }
}
