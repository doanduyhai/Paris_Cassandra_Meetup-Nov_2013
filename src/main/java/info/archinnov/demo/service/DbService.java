package info.archinnov.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;
import info.archinnov.demo.model.RateLimit;
import info.archinnov.demo.model.ValueWithTimestamp;

@Service
public class DbService {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private int threshold = 5;

    @Inject
    private CQLPersistenceManager manager;

    private Session session;

    @PostConstruct
    public void retrieveNativeSession() {
        this.session = manager.getNativeSession();
    }

    public void insertForCountDown(int ttl) {
        manager.nativeQuery("INSERT INTO countdown(id,value) VALUES('countdown','value') USING TTL " + ttl).first();
    }

    public int getCountDown() {
        final Map<String, Object> map = manager.nativeQuery("SELECT ttl(value) FROM countdown WHERE id='countdown'")
                                               .first();
        if (map != null) {
            return (Integer) map.get("ttl(value)");
        } else {
            throw new IllegalStateException("No value found for countdown");
        }
    }

    public List<RateLimit> setThresholdForRateLimit(int threshold) {
        this.threshold = threshold;
        return fetchRateLimitedValues();
    }

    public List<RateLimit> insertForRateLimit(String value, int ttl) {
        final Map<String, Object> countMap = manager
                .nativeQuery("SELECT count(*) FROM ratelimit WHERE id='ratelimit' LIMIT 100")
                .first();
        if (countMap != null) {
            final Long count = (Long) countMap.get("count");
            if (count >= threshold) {
                throw new IllegalStateException("You cannot have more than " + threshold + " values per " + ttl + " " +
                                                        "seconds");
            }
        }

        manager.nativeQuery("INSERT INTO ratelimit(id,column,value) VALUES('ratelimit','" + value + "'," +
                                "'" + value + "') USING TTL " + ttl).first();

        return fetchRateLimitedValues();
    }

    private List<RateLimit> fetchRateLimitedValues() {
        List<RateLimit> result = new ArrayList<RateLimit>();
        final List<Map<String, Object>> maps = manager
                .nativeQuery("SELECT value,ttl(value) FROM ratelimit WHERE id='ratelimit' LIMIT 100").get();
        for (Map<String, Object> map : maps) {
            result.add(new RateLimit((String) map.get("value"), (Integer) map.get("ttl(value)")));
        }
        return result;
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(new Date());
    }

    public ValueWithTimestamp insertWithCurrentTimePlus(String value, int shiftInSecs) {
        long timeStampInMicros = getTimestampInMicros(shiftInSecs);

        manager.nativeQuery("INSERT INTO time_stamp(id,value) VALUES('time_stamp'," +
                                    "'" + value + "') USING TIMESTAMP " + timeStampInMicros).first();

        final Map<String, Object> map = manager
                .nativeQuery("SELECT value,writetime(value) FROM time_stamp WHERE id='time_stamp'").first();

        String readValue = (String) map.get("value");
        long timestamp = (Long) map.get("writetime(value)");
        long shortTimestamp = timestamp / 1000;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        return new ValueWithTimestamp(readValue, format.format(new Date(shortTimestamp)));
    }

    public String insertForWriteBarrier(String value) {
        manager.nativeQuery("INSERT INTO writebarrier(id,value) VALUES('writebarrier','" + value + "')").first();
        final Map<String, Object> map = manager.nativeQuery("SELECT value FROM writebarrier WHERE id='writebarrier'")
                                               .first();
        if (map != null) {
            return (String) map.get("value");
        } else {
            return null;
        }
    }

    public void deleteWithWriteBarrier(int shiftInSecs) {
        long timestampInMicros = getTimestampInMicros(shiftInSecs);
        manager.nativeQuery("DELETE FROM writebarrier USING TIMESTAMP " + timestampInMicros + " WHERE id='writebarrier'").first();
    }

    private long getTimestampInMicros(int shiftInSecs) {
        final long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis + 1000 * shiftInSecs) * 1000;
    }

    public void resetDb() {
        manager.nativeQuery("TRUNCATE countdown").first();
        manager.nativeQuery("TRUNCATE ratelimit").first();
        manager.nativeQuery("TRUNCATE time_stamp").first();
        manager.nativeQuery("TRUNCATE writebarrier").first();
    }
}
