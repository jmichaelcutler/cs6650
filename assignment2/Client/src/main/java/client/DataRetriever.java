package client;

import bsdsass2testdata.RFIDLiftData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataRetriever {
    private int numThreads;
    private int numIter;
    private String fileName;
    private WebClient client;
    private List<RFIDLiftData> liftDataIn;

    private static final int DEFAULT_THREAD_NUM = 10;
    private static final int DEFAULT_ITER_NUM = 100;
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");
    private static final String DEFAULT_SOURCE_FILE = "BSDSAssignment2Day1.ser";
    private static final String DEFAULT_URI = "http://52.40.166.203:8080/webapi/";
    private static final String PATH = "/webapi";
    private static final String ENDPOINT = "assignment2server";
    private static final int NUM_ATTEMPTS_FLAG = 1;
    private static final int NUM_SUCCESS_FLAG = 2;

    DataRetriever(String[] args) {
        String uri;
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
            uri = DEFAULT_URI;
        }
        client = new WebClient(uri, ENDPOINT);
        if (args.length == 5) {
            fileName = args[4];
        } else {
            fileName = WORKING_DIRECTORY + SEPARATOR + DEFAULT_SOURCE_FILE;
        }
    }

    void getAll() {
        File file = new File(fileName);
        if(!file.isAbsolute()) {
            file = file.getAbsoluteFile();
        }
        liftDataIn = getLiftData(file);
        if(liftDataIn != null) {
            liftDataIn = Collections.synchronizedList(liftDataIn);
        }
        System.out.println("Number of threads: " + numThreads);
        System.out.println("Number of iterations per thread: " + numIter);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads + 1);

        List<UserStat> getThreadList = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {

        }
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
}
