package Police.Techno.Client;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by kubri on 1/26/2017.
 */
public class Client {
    public static void main(String[] args) {
        int portNum = 8080;
        String hostName = "none";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            System.out.println("UnknownHost");
            System.exit(-1);
        }

        try(
                Socket socket = new Socket(hostName, portNum);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String fromServer;
            JsonObject fromClient;
            JsonHandlerClient jsonHandler = new JsonHandlerClient();

            while((fromServer = in.readLine()) != null) {
                //System.out.println("Server: " + fromServer); // FOR DEBUGGING
                JsonObject obj = null;
                try(
                        JsonReader reader = Json.createReader(new StringReader(fromServer));
                        ){
                    obj = reader.readObject();
                    reader.close();
                }
                catch (NullPointerException e) {
                    System.out.println("null");
                    break;
                }
                catch (JsonParsingException e) {
                    System.out.println("parse");
                    break;
                }

                if(obj.getString("cmd").equals("exit"))
                    break;

                fromClient = jsonHandler.processInput(obj);
                if(fromClient != null){
                    //System.out.println("Client: " + fromClient); // FOR DEBUGGING
                    out.println(fromClient.toString());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
