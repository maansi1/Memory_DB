package memory_DB;

import java.util.*;
import memory_DB.CommandParser.*;

public class DB {

    
    private final Map<Integer, Entry> storage = new HashMap<>();

    private volatile boolean running = false;

    private Thread cleanerThread;
    private final long cleanupIntervalMs;
    private long cleanupRemovedCount = 0; 

    public DB(long cleanupIntervalMs) {
        this.cleanupIntervalMs = cleanupIntervalMs;
        startCleanupThread(); 
    }

    public DB() {
        this(10_000); 
    }


    private void startCleanupThread() {
        cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (running) {
                        cleanupExpiredKeys();
                    }
                    Thread.sleep(cleanupIntervalMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public void manualCleanup() {
        cleanupExpiredKeys();
    }

    private synchronized void cleanupExpiredKeys() {
        long removedNow = 0;

        Iterator<Map.Entry<Integer, Entry>> iterator = storage.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Entry> entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
                removedNow++;
            }
        }

        cleanupRemovedCount += removedNow;
    }

    public synchronized void start() {
        if (running) {
            System.out.println("INFO: Database already running.");
            return;
        }
        running = true;
        System.out.println("INFO: Database started.");
    }

    public synchronized void stop() {
        if (!running) {
            System.out.println("INFO: Database already stopped.");
            return;
        }
        running = false;
        System.out.println("INFO: Database stopped.");
    }

    public boolean isRunning() {
        return running;
    }

    private void checkRunning() {
        if (!running) {
            throw new DatabaseNotRunningException();
        }
    }

    public synchronized void put(int key, String value, long ttlSeconds) {
        checkRunning();

        if (storage.containsKey(key)) {
            System.out.println("WARN: Overwriting existing key " + key);
        }

        storage.put(key, new Entry(value, ttlSeconds));
        System.out.println("OK: Stored key=" + key + " ttl=" + ttlSeconds + "s");
    }

    public synchronized String get(int key) {
        checkRunning();

        Entry entry = storage.get(key);
        if (entry == null) {
            throw new KeyNotFoundException("Key not found: " + key);
        }

        if (entry.isExpired()) {
            storage.remove(key);
            throw new KeyNotFoundException("Key expired: " + key);
        }

        return entry.getValue();
    }

    public synchronized void delete(int key) {
        checkRunning();

        if (!storage.containsKey(key)) {
            throw new KeyNotFoundException("Key not found: " + key);
        }

        storage.remove(key);
        System.out.println("OK: Deleted key=" + key);
    }

    public synchronized int size() {
        return storage.size();
    }

    public synchronized Set<Integer> keys() {
        return new TreeSet<>(storage.keySet());
    }

    public synchronized void clear() {
        storage.clear();
        System.out.println("OK: Cleared all entries.");
    }

    public synchronized String info() {
        return "INFO: running=" + running +
                ", size=" + storage.size() +
                ", cleanupIntervalMs=" + cleanupIntervalMs +
                ", totalCleaned=" + cleanupRemovedCount;
    }
}
