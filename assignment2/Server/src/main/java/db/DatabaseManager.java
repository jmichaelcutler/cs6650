package db;

import bsdsass2testdata.RFIDLiftData;
import com.mycompany.mavenversion1.UserStat;


import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DatabaseManager {
    static MongoDBManager createDBClient(Integer day){
        return new MongoDBManager(day);
    }

    Integer postBatchToDB(List<RFIDLiftData> liftData) throws InterruptedException, ExecutionException;

    UserStat getOneUserStats(Integer skierId, Integer dayId);

    void postOneToDB(RFIDLiftData liftData);

    void close();

    int dropDB(Integer day) throws InterruptedException;

    void cacheUsers(Integer dayId) throws InterruptedException;
}
