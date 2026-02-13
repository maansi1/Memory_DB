package memory_DB;

public class Entry {
    private final String value;
    private final long expiresAtMillis;

    public Entry(String value, long ttlSeconds) {
        this.value = value;

        if (ttlSeconds <= 0) {
            this.expiresAtMillis = Long.MAX_VALUE;
        } else {
            this.expiresAtMillis = System.currentTimeMillis() + ttlSeconds * 1000;
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isExpired() {
        
        return System.currentTimeMillis() > expiresAtMillis;
    }

    public long getExpiresAtMillis() {
        return expiresAtMillis;
    }
}
