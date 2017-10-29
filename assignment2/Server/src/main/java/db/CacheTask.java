package db;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.VoidType;

import java.util.concurrent.Callable;

public class CacheTask implements Callable<VoidType> {
    private final Integer multiplier;
    private final MongoDBManager db;
    private final Integer numThreads;
    private final Integer dayId;

    CacheTask(Integer multiplier, Integer numThreads, MongoDBManager db, Integer dayId) {
        this.multiplier = multiplier;
        this.db = db;
        this.numThreads = numThreads;
        this.dayId = dayId;
    }

    @Override
    public VoidType call() throws Exception {
        Integer startUserID = 1 + (db.getNumUsers() / numThreads * multiplier);
        Integer endUserId = 1 + db.getNumUsers() / numThreads * (multiplier + 1);
        if(endUserId > db.getNumUsers()) {
            endUserId = db.getNumUsers() + 1;
        }
        for(int i = startUserID; i < endUserId; i++) {
            db.getOneUserStats(i, dayId);
        }
        return null;
    }
}
