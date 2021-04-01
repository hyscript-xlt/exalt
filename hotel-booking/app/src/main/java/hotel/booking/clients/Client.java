package hotel.booking.clients;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.*;
import com.aerospike.client.task.IndexTask;
import hotel.booking.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.aerospike.client.policy.RecordExistsAction.*;
import static hotel.booking.config.ApplicationConfig.getDbName;

/**
 * Instantiate an AerospikeClient by {@link AerospikeClientFactory} to access
 * an Aerospike database to perform database operations.
 * This client is not thread safe.
 * For thread safe client please refer to {@link AerospikeClient}
 */
@RequiredArgsConstructor
public class Client {
    /**
     * Throws a RuntimeException if can't connect to the Aerospike database by given host and port.
     */
    private static final Client singleInstance = AerospikeClientFactory.ofSingleInstance()
            .map(Client::new)
            .orElseThrow(() -> new RuntimeException("Couldn't connect to Aerospike database."));

    private final AerospikeClient client;

    /**
     * Standard getter for {@link #singleInstance}.
     *
     * @return {@link #singleInstance}
     */
    public static Client instanceOf() {
        return singleInstance;
    }

    /**
     * Doesn't insert if the record exists.
     *
     * @param key  of the record
     * @param bins to be inserted
     * @see <a href="https://www.aerospike.com/docs/client/java/usage/best_practices.html">Java Client Best Practices</a>
     */
    public void insertWithoutUpdate(Key key, Bin... bins) {
        WritePolicy cPolicy = setPolicyTo(client, CREATE_ONLY);
        client.put(cPolicy, key, bins);
    }

    /**
     * Inserts if the record doesn't exist, otherwise updates the existing record.
     *
     * @param key  of the record
     * @param bins to be inserted
     * @see <a href="https://www.aerospike.com/docs/client/java/usage/best_practices.html">Java Client Best Practices</a>
     */
    public void insert(Key key, Bin... bins) {
        WritePolicy cPolicy = setPolicyTo(client, UPDATE);
        client.put(cPolicy, key, bins);
    }

    /**
     * Updates the record if it exists; fails otherwise.
     *
     * @param key  of the record
     * @param bins to be inserted
     * @see <a href="https://www.aerospike.com/docs/client/java/usage/best_practices.html">Java Client Best Practices</a>
     */
    public void update(Key key, Bin... bins) {
        WritePolicy cPolicy = setPolicyTo(client, UPDATE_ONLY);
        client.put(cPolicy, key, bins);
    }

    /**
     * <pre>
     * {@code
     * client.get(null, key)
     * }
     * </pre>
     * Returns null if key doesn't exists in the DB {@link AerospikeClient#get(Policy, Key)}. <br/>
     * The method wraps the value into {@link Optional#ofNullable(Object)}
     *
     * @param key of the record
     * @return Optional value of {@link Record}
     */
    public Optional<Record> query(Key key) {
        return Optional.ofNullable(client.get(null, key));
    }

    /**
     * Preforms the query by the key/value pair {@link #findBy(String, Filter)}.
     *
     * @param setName equivalent to the table name in RDBMS.
     * @param key     the String value of key in the record
     * @param value   corresponding String value for the key
     * @return List of {@link KeyRecord}
     */
    public List<KeyRecord> findByStringValue(String setName, String key, String value) {
        createIndex(setName, key, IndexType.STRING);
        List<KeyRecord> rec = findBy(setName, Filter.equal(key, value));
        removeIndex(setName, key);

        return rec;
    }

    /**
     * Preforms the query by the key/value pair {@link #findBy(String, Filter)}.
     *
     * @param setName equivalent to the table name in RDBMS.
     * @param key     the String value of key in the record
     * @param value   corresponding long value for the key
     * @return List of {@link KeyRecord}
     */
    public List<KeyRecord> findByLongValue(String setName, String key, long value) {
        createIndex(setName, key, IndexType.NUMERIC);
        List<KeyRecord> rec = findBy(setName, Filter.equal(key, value));
        removeIndex(setName, key);

        return rec;
    }

    /**
     * Delete record by key.
     */
    public void delete(Key key) {
        client.delete(null, key);
    }

    /**
     * Creates new Key {@link Key} setting {@link ApplicationConfig#getDbName()} as default namespace.
     *
     * @param setName equivalent to the table name in RDBMS.
     * @param key     equivalent to the primary key in RDBMS.
     */
    public Key makeKeyDefaultDb(String setName, String key) {
        return makeKey(getDbName(), setName, key);
    }

    /**
     * Creates new Key {@link Key}.
     *
     * @param db      equivalent to the database name in RDBMS.
     * @param setName equivalent to the table name in RDBMS.
     * @param key     equivalent to the primary key in RDBMS.
     */
    public Key makeKey(String db, String setName, String key) {
        return new Key(db, setName, key);
    }

    /**
     * Create scalar secondary index and waits for the command completion.
     *
     * @param setName   equivalent to the table name in RDBMS.
     * @param key       equivalent to the primary key in RDBMS.
     * @param indexType can be NUMERIC, STRING, etc. {@link IndexType}
     */
    private void createIndex(String setName, String key, IndexType indexType) {
        IndexTask task = client.createIndex(null, getDbName(), setName,
                "idx_".concat(key), key, indexType);
        task.waitTillComplete();
    }

    /**
     * According to the official documentation  If the policy is identical for a group of commands,
     * reuse them instead of instantiating policies for each command.
     *
     * @param client the client which preforms current queries
     * @param action handles the records that already exists.
     * @return changed write policy {@link WritePolicy}
     * @see <a href="https://www.aerospike.com/docs/client/java/usage/best_practices.html">Java Client Best Practices</a>
     */
    private WritePolicy setPolicyTo(AerospikeClient client, RecordExistsAction action) {
        WritePolicy cPolicy = client.getWritePolicyDefault();
        cPolicy.recordExistsAction = action;
        return cPolicy;
    }

    private void removeIndex(String setName, String key) {
        //Don't know whether it is good idea every time drop the newly created indexes
        client.dropIndex(null, "test", setName, "idx_".concat(key));
    }

    /**
     * Internal helper method used to in
     * {@link #findByLongValue(String, String, long)},
     * {@link #findByStringValue(String, String, String)} methods
     * The namespace which corresponds to the DB name on RDBMS set {@link ApplicationConfig#getDbName()}
     *
     * @param setName equivalent to the table name in RDBMS.
     * @param filter  query filter definition {@link Filter}
     * @return List of records {@link KeyRecord}
     */
    private List<KeyRecord> findBy(String setName, Filter filter) {
        Statement stmt = new Statement();
        stmt.setNamespace(getDbName());
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

}

