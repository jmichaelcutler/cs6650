import bsdsass2testdata.RFIDLiftData;
import com.google.gson.Gson;
import com.mycompany.mavenversion1.MyResource;
import db.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RFIDLiftData test1 = new RFIDLiftData(1,1,1,30,1);
        RFIDLiftData test3 = new RFIDLiftData(1, 1, 3, 30, 2);
        RFIDLiftData test2 = new RFIDLiftData(1,2,2,2,2);
        RFIDLiftData test4 = new RFIDLiftData(1, 1, 1, 20, 5);
        List<RFIDLiftData> testList = new ArrayList<>();
        testList.add(test1);
        testList.add(test2);
        testList.add(test3);
        testList.add(test4);
        Gson gson = new Gson();
        String json = gson.toJson(testList);
        MyResource resource = new MyResource();
        resource.postText(json);

        //int response = resource.dropDB("1");
        DatabaseManager dbman = DatabaseManager.createDBClient(1);
        dbman.getOneUserStats(3, 1);
        dbman.getOneUserStats(1, 1);
        dbman.close();
    }
}
