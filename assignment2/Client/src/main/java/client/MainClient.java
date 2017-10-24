package client;

import java.util.concurrent.ExecutionException;

public class MainClient {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        DataPoster dataPoster = new DataPoster(args);
        dataPoster.postAll();
    }
}
