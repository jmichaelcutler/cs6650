package db;

import bsdsass2testdata.RFIDLiftData;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mycompany.mavenversion1.UserStat;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;


public class MongoDBManager implements DatabaseManager {
    private MongoClient client;
    private Integer numUsers;
    private static final String IP_ADDRESS = "34.211.15.186";
    private static final Integer DB_PORT = 27017;
    private String dbName;
    private static final Integer MAX_POOL_SIZE = 20000;
    private static final Integer NUM_THREADS = 100;
    private static final Integer NUM_USERS = 40000;

    MongoDBManager(Integer day) {
        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        options.connectionsPerHost(MAX_POOL_SIZE);
        options.socketKeepAlive(true);
        client = new MongoClient(new ServerAddress(IP_ADDRESS, DB_PORT), options.build());
        numUsers = NUM_USERS;
        dbName = "day" + day + "master";
    }


    @Override
    public void postOneToDB(RFIDLiftData liftData) {
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> coll = db.getCollection("day" + liftData.getDayNum() + "AllRides");
        Document doc = createDocument(liftData);
        coll.insertOne(doc);
//        postOnetoDB(liftData, coll);
//        coll = db.getCollection("user" + liftData.getSkierID() + "day" + liftData.getDayNum() + "stats");
//        postOnetoDB(liftData, coll);
    }


    @Override
    public Integer postBatchToDB(List<RFIDLiftData> liftData) throws InterruptedException, ExecutionException {
        for (RFIDLiftData data : liftData) {
            postOneToDB(data);
        }
        return 200;
    }

    @Override
    public UserStat getOneUserStats(Integer skierId, Integer dayId) {

        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> userStats = db.getCollection("day" + dayId + "userStats");
        userStats.createIndex(Indexes.ascending("_id"));
        BasicDBObject query = new BasicDBObject("_id", skierId);
        Document myDoc = userStats.find(query).first();
        if (myDoc != null) {
            Document doc = userStats.find(eq("_id", skierId)).first();
            int numRides = doc.getInteger("numRides");
            long totalVert = doc.getLong("totalVert");
            return new UserStat(skierId, numRides, totalVert);
        }
        return compileUserStats(skierId, dayId, db);
    }

    private UserStat compileUserStats(Integer skierId, Integer dayId, MongoDatabase db) {
        int numRides = 0;
        long totalVert = 0;
        MongoCollection<Document> stats = db.getCollection("day" + dayId + "AllRides");
        stats.createIndex(Indexes.ascending("skierID"));
        FindIterable<Document> results = stats.find(eq("skierID", skierId));
        try {
            long count = stats.count(eq("skierID", skierId));
            if (count == 0) {
                return new UserStat(skierId, 0, 0);
            }
            for (Document doc : results) {
                int liftID = doc.getInteger("liftID");
                long thisVert = (long) (((liftID - 1) / 10) * 100 + 200);
                numRides += 1;
                totalVert += thisVert;
            }
            Document userStat = new Document()
                    .append("_id", skierId)
                    .append("numRides", numRides)
                    .append("totalVert", totalVert);
            db.getCollection("day" + dayId + "userStats").insertOne(userStat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new UserStat(skierId, numRides, totalVert);
    }

    public void close() {
        client.close();
    }

    @Override
    public int dropDB(Integer day) throws InterruptedException {
        MongoDatabase db = client.getDatabase(dbName);
        db.drop();
        return 200;
    }

    private Document createDocument(RFIDLiftData liftData) {
        Document document = new Document();
        document.append("skierID", liftData.getSkierID());
        document.append("time", liftData.getTime());
        document.append("liftID", liftData.getLiftID());
        document.append("resortID", liftData.getResortID());
        document.append("day", liftData.getDayNum());
        return document;
    }

    Integer getNumUsers() {
        return this.numUsers;
    }

    public void cacheUsers(Integer dayId) throws InterruptedException {
        List<CacheTask> tasks = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS + 1);
        for (int i = 0; i < NUM_THREADS; i++) {
            tasks.add(new CacheTask(i, NUM_THREADS, this, dayId));
        }
        executor.invokeAll(tasks);
        executor.shutdown();
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> docs = db.getCollection("day" + dayId + "userStats");
        docs.createIndex(Indexes.ascending("_id"));
    }
}