package db;

import bsdsass2testdata.RFIDLiftData;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mycompany.assignment2server.UserStat;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;

public class MongoDBManager implements DatabaseManager {
    private MongoDatabase resortDB;

    private static final String IP_ADDRESS = "http://35.166.14.106";
    private static final Integer DB_PORT = 27017;
    private static final String DB_NAME = "ResortDB";

    MongoDBManager() {
        MongoClient dbClient = new MongoClient(IP_ADDRESS, DB_PORT);
        resortDB = dbClient.getDatabase(DB_NAME);
    }

    @Override
    public void postToDB(RFIDLiftData liftData) {
        MongoCollection<Document> collection = resortDB.getCollection("Day " + liftData.getDayNum() + " Master");
        Document document = new Document();
        document.put("skierID", liftData.getSkierID());
        document.put("time", liftData.getTime());
        document.put("liftID", liftData.getLiftID());
        document.put("resortID", liftData.getResortID());
        document.put("day", liftData.getDayNum());
        collection.insertOne(document);
    }

    private void postToUserStats(Document document) {
        int numLifts;
        long totalVert;
        int skierID = document.getInteger("skierID");
        int liftId = document.getInteger("liftID");
        long liftVert = (long)(((liftId - 1) / 10) * 100 + 200);
        MongoCollection<Document> collection = resortDB.getCollection("Day " + document.getInteger("day") + "User Stats");
        Document storedDocument  = collection.find(eq("skierId", skierID)).first();
        if(storedDocument != null) {
            numLifts = storedDocument.getInteger("numLifts") + 1;
            totalVert = storedDocument.getLong("totalVert") + liftVert;
            collection.deleteOne(eq("skierId", skierID));
        } else {
            numLifts = 1;
            totalVert = liftVert;
        }

        document = new Document()
                .append("skierId", skierID)
                .append("numLifts", numLifts)
                .append("totalVert", totalVert);
        collection.insertOne(document);
    }

    @Override
    public UserStat getFromDB(Integer skierId, Integer dayId) {
        int numLifts;
        long totalVert;
        UserStat stat = null;
        MongoCollection<Document> collection = resortDB.getCollection("Day " + dayId + "User Stats");
        Document document = collection.find(eq("skierId", skierId)).first();
        if(document != null) {
            numLifts = document.getInteger("numLifts");
            totalVert = document.getLong("totalVert");
            stat = new UserStat(skierId, numLifts, totalVert);
        }
        return stat;
    }

    @Override
    public void delete(Integer dayId) {
        deleteDayCollection(dayId);
        deleteUserDayStats(dayId);
    }

    @Override
    public void process(Integer dayId) {
        MongoCollection<Document> collection = resortDB.getCollection("Day " + dayId + " Master");
        FindIterable<Document> documents = collection.find();
        for(Document document : documents) {
            postToUserStats(document);
        }
    }

    private void deleteDayCollection(Integer dayId) {
        MongoCollection<Document> collection = resortDB.getCollection("Day " + dayId + " Master");
        collection.deleteMany(new Document());
    }

    private void deleteUserDayStats(Integer dayId) {
        MongoCollection<Document> collection = resortDB.getCollection("Day " + dayId + "User Stats");
        collection.deleteMany(new Document());
    }
}
