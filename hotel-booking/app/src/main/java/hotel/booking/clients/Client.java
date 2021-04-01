package hotel.booking.clients;

import com.aerospike.client.*;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.*;
import com.aerospike.client.task.IndexTask;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static hotel.booking.config.ApplicationConfig.getDbName;

@RequiredArgsConstructor
public class Client {
    private static final Client singleInstance = AerospikeClientFactory.ofSingleInstance()
            .map(Client::new)
            .orElseThrow(() -> new RuntimeException("Couldn't connect to Aerospike database."));

    private final WritePolicy cPolicy = new WritePolicy();
    private final AerospikeClient client;

    public static Client instanceOf() {
        return singleInstance;
    }

    public void insertWithoutUpdate(Key key, Bin... bins) {
        cPolicy.recordExistsAction = RecordExistsAction.CREATE_ONLY;
        client.put(cPolicy, key, bins);
    }

    public void insert(Key key, Bin... bins) {
        cPolicy.recordExistsAction = RecordExistsAction.UPDATE;
        client.put(cPolicy, key, bins);
    }

    public void update(Key key, Bin... bins) {
        cPolicy.recordExistsAction = RecordExistsAction.UPDATE_ONLY;
        client.put(cPolicy, key, bins);
    }

    public Optional<Record> query(Key key) {
        return Optional.ofNullable(client.get(null, key));
    }

    private void createIndex(String setName, String key, IndexType indexType) {
        IndexTask task = client.createIndex(null, getDbName(), setName,
                "idx_".concat(key), key, indexType);
        task.waitTillComplete();
    }

    private void removeIndex(String setName, String key) {
        //Don't know whether it is good idea every time drop the newly created indexes
        client.dropIndex(null, "test", setName, "idx_".concat(key));
    }

    private List<KeyRecord> findBy(String setName, Filter filter) {
        Statement stmt = new Statement();
        stmt.setNamespace("test");
        stmt.setSetName(setName);
        stmt.setFilter(filter);

        return Optional.ofNullable(client.query(null, stmt))
                .map(RecordSet::iterator)
                .map(iter -> (Iterable<KeyRecord>) () -> iter)
                .map(Iterable::spliterator)
                .map(iter -> StreamSupport.stream(iter, false)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }



    public List<KeyRecord> findByStringValue(String setName, String key, String value) {
        createIndex(setName, key, IndexType.STRING);
        List<KeyRecord> rec = findBy(setName, Filter.equal(key, value));
        removeIndex(setName, key);

        return rec;
    }

    public List<KeyRecord> findByLongValue(String setName, String key, long value) {
        createIndex(setName, key, IndexType.NUMERIC);
        List<KeyRecord> rec = findBy(setName, Filter.equal(key, value));
        removeIndex(setName, key);

        return rec;
    }

    public void delete(Key key) {
        client.delete(null, key);
    }

    public Key makeKeyDefaultHost(String table, String key) {
        return makeKey(getDbName(), table, key);
    }

    public Key makeKey(String db, String table, String key) {
        return new Key(db, table, key);
    }
}

