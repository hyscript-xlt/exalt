package api.demo;

import api.demo.intermediate.DbClient;
import api.demo.intermediate.User;
import com.aerospike.client.AerospikeClient;

import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        User user = User.builder()
                .username("fantastic")
                .tweetCount(1)
                .lastTweet(LocalDate.now().toEpochDay())
                .build();
        DbClient client = new DbClient(new AerospikeClient("localhost", 3000));
        client.insert(user);
    }
}
