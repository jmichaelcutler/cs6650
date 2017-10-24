package client;

import java.util.List;
import java.util.concurrent.Callable;

public class GetTask extends MainClient implements Callable<List<UserStat>> {

    @Override
    public List<UserStat> call() throws Exception {
        return null;
    }

    //TODO
}
