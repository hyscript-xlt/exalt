package api.demo.intermediate;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class DbClient {
    private final WritePolicy cPolicy = new WritePolicy();
    private final AerospikeClient client;

    public boolean insert(User user) {
        Key key = new Key("test", "users", user.getUsername());
        boolean rec = client.exists(null, key);
        if(rec) {
            return false;
        }

        cPolicy.recordExistsAction = RecordExistsAction.CREATE_ONLY;
        Bin [] arr = Stream.of(new Bin("username", user.getUsername()),
                new Bin("last_tweet", user.getLastTweet()),
                new Bin("tweet_count", user.getTweetCount()),
                new Bin("interests", user.getInterests()))
                .toArray(Bin[]::new);

        client.put(cPolicy, key, arr);
        cPolicy.recordExistsAction = RecordExistsAction.UPDATE;
        return true;
    }



    protected void finalize() throws Throwable {
        if (this.client != null) {
            this.client.close();
        }
    }

    ;
}
