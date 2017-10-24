package db;

import bsdsass2testdata.RFIDLiftData;
import com.mycompany.assignment2server.UserStat;

public interface DatabaseManager {
    static MongoDBManager createDBClient(){
        return new MongoDBManager();
    }

    void postToDB(RFIDLiftData liftData);

    UserStat getFromDB(Integer skierId, Integer dayId);

    void delete(Integer dayId);

    void process(Integer dayId);
}
