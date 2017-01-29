package Police.Techno.DB;

import java.sql.Connection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by kubri on 1/29/2017.
 */
public class ConnectionsPool {
    private final int MAX = 5;
    private final Semaphore sem = new Semaphore(MAX, true);
    private final Queue<Connection> pool = new ConcurrentLinkedQueue<>();

    public Connection getConnection() {
        sem.tryAcquire();
        Connection c = pool.poll();
        if(c != null)
            return c;

        try {
            c = DBManagement.connect("posts");
        }
        catch (Exception e) {
            System.out.println("Could not open connection!");
            sem.release();
            System.exit(-1);
        }
        return c;
    }

    public void returnConnection(Connection c) {
        pool.add(c);
        sem.release();
    }
}
