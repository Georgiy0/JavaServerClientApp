package Police.Techno.Server;


import Police.Techno.DB.ConnectionsPool;
import Police.Techno.DB.DBManagement;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;

/**
 * Created by kubri on 1/26/2017.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        int portNum = 8080;
        boolean listening = true;
        ConnectionsPool connectionsPool = new ConnectionsPool();
        try(ServerSocket serverSocket = new ServerSocket(portNum)){
            while (listening) {
                new ServerThread(serverSocket.accept(), connectionsPool).start();
            }
        }
        catch (IOException e){
            System.out.println("Could not listen on port "+portNum);
            System.exit(-1);
        }
    }
}
