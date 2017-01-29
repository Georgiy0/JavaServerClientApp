package Police.Techno.Server;

import Police.Techno.DB.ConnectionsPool;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;

/**
 * Created by kubri on 1/26/2017.
 */
public class ServerThread extends Thread {
    private Socket socket = null;
    private ConnectionsPool connectionsPool = null;

    public ServerThread(Socket socket, ConnectionsPool connectionsPool) {
        super("ServerThread");
        this.socket = socket;
        this.connectionsPool = connectionsPool;
    }
    
    private JsonObject parse(String str) {
        JsonObject obj = null;
        try(
                JsonReader reader = Json.createReader(new StringReader(str));
        ){
            obj = reader.readObject();
            reader.close();
        }
        catch (NullPointerException e) {
            System.err.println("Null pointer exception!");
            System.exit(-1);
        }
        catch (JsonParsingException e) {
            System.err.println("Could not parse the string!");
            System.exit(-1);
        }
        return obj;
    }

    public void run(){
        try(
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            String input;
            JsonObject output;
            JsonHandlerServer jsonHandler = new JsonHandlerServer();
            out.println(Json.createObjectBuilder().add("cmd", "init").build().toString());

            while ((input = in.readLine()) != null) {
                JsonObject obj = parse(input);
                output = jsonHandler.processInput(obj, connectionsPool);
                out.println(output.toString());
                try {
                    if (output.getString("cmd").equals("exit")) {
                        break;
                    }
                }
                catch (NullPointerException e){
                    System.err.println("Null pointer exception!");
                    break;
                }
            }
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Cannot close connection!");
                e.printStackTrace();
            }

        }
    }
}
