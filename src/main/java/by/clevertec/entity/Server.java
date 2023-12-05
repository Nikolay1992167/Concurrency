package by.clevertec.entity;

import by.clevertec.exception.ServerException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Getter
public class Server {

    private final List<Integer> resourceList;

    public Server() {
        resourceList = new ArrayList<>();
    }

    public Response processRequest(Future<Client.Request> future) {
        int delayTime = new Random().ints(100, 1000)
                .findFirst()
                .orElse(100);
        try {
            TimeUnit.MILLISECONDS.sleep(delayTime);
            int dataResponse = future.get().getDataRequest();
            resourceList.add(dataResponse);
        } catch (InterruptedException | ExecutionException ex) {
            throw new ServerException(ex);
        }
        return new Response(resourceList.size());
    }

    public record Response(int message) {
    }
}
