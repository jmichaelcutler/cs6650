package client;

import bsdsass2testdata.RFIDLiftData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class DataPoster {
    private String uri;
    private int numThreads;
    private int numIter;
    private String fileName;
    private List<RFIDLiftData> liftDataIn;

    private static final int DEFAULT_THREAD_NUM = 10;
    private static final int DEFAULT_ITER_NUM = 100;
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");
    private static final String DEFAULT_SOURCE_FILE = "BSDSAssignment2Day1.ser";
    private static final String DEFAULT_URI = "http://52.40.166.203:8080";
    private static final String PATH = "/mavenVersion1/webapi";
    private static final String ENDPOINT = "assignment2server/load";
    private static final int NUM_ATTEMPTS_FLAG = 1;
    private static final int NUM_SUCCESS_FLAG = 2;

    DataPoster(String[] args) {
        if (args.length >= 2) {
            numThreads = Integer.valueOf(args[0]);
            numIter = Integer.valueOf(args[1]);
        } else {
            numThreads = DEFAULT_THREAD_NUM;
            numIter = DEFAULT_ITER_NUM;
        }
        if (args.length >= 4) {
            uri = "http://" + args[2] + ":" + args[3] + PATH;
        } else {
            uri = DEFAULT_URI + PATH;
        }
//        client = new WebClient(uri, ENDPOINT);
        if (args.length == 5) {
            fileName = args[4];
        } else {
            fileName = WORKING_DIRECTORY + SEPARATOR + DEFAULT_SOURCE_FILE;
        }
    }

    void postAll() throws InterruptedException, ExecutionException{
        File file = new File(fileName);
        if(!file.isAbsolute()) {
            file = file.getAbsoluteFile();
        }
        liftDataIn = getLiftData(file);
        if(liftDataIn != null) {
            liftDataIn = Collections.synchronizedList(liftDataIn);
        }
        System.out.println("Number of threads: " + numThreads);
        System.out.println("Number of iterations per thread: " + (liftDataIn.size() / numThreads));

        ExecutorService executor = Executors.newFixedThreadPool(numThreads + 1);

        List<PostTask> postThreadList = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            postThreadList.add(new PostTask(i, numThreads, liftDataIn, uri, ENDPOINT));
        }

        long start = System.currentTimeMillis();
        System.out.println("Client starting.....Time: " + start);
        System.out.println("Posting lift data...");
        List<Future<List<PostResult>>> futurePostList = executor.invokeAll(postThreadList);
        long stop = System.currentTimeMillis();
        System.out.println("Posting lift data complete. Time: " + stop);
        executor.shutdown();
        outputResults(futurePostList, start, stop);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<RFIDLiftData> getLiftData(File file) {
        ArrayList<RFIDLiftData> result = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            result = (ArrayList<RFIDLiftData>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Class not found");
            cnfe.printStackTrace();
        }
        return result;
    }

    private void outputResults(List<Future<List<PostResult>>> futurePostList, long start, long stop) throws ExecutionException, InterruptedException {
        StatsCalculator statCalc = new StatsCalculator();
        long numAttempt = statCalc.calculateCount(futurePostList, NUM_ATTEMPTS_FLAG);
        long numSuccess = statCalc.calculateCount(futurePostList, NUM_SUCCESS_FLAG);
        String numerator = String.valueOf(stop - start);
        double wallTime = (Double.valueOf(numerator)) / 1000;
        System.out.println("Wall time: " + wallTime + " seconds");
        List<Long> latencies = statCalc.calculateLatencies(futurePostList);
        System.out.println("Number of post requests sent to server: " + numAttempt);
        System.out.println("Number of successful post requests: " + numSuccess);
        System.out.println("Mean latency: " + statCalc.calculateMeanLatency(latencies) + "ms");
        System.out.println("Median latency: " + statCalc.calculateNthPercentile(latencies, 0.5) + " ms");
        System.out.println("95th percentile: " + statCalc.calculateNthPercentile(latencies, .95) + " ms");
        System.out.println("99th percentile: " + statCalc.calculateNthPercentile(latencies, .99) + " ms");

    }
}
