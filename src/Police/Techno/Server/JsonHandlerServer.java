package Police.Techno.Server;

import Police.Techno.DB.ConnectionsPool;
import Police.Techno.DB.DBManagement;
import Police.Techno.Post.Post;
import javax.json.Json;
import javax.json.JsonObject;
import java.sql.Connection;


/**
 * Created by kubri on 1/26/2017.
 */
public class JsonHandlerServer {
    JsonResponses responses = new JsonResponses();
    public JsonObject processInput(JsonObject obj, ConnectionsPool connectionsPool){
        Connection c = null;
        switch (obj.getString("cmd")){

            case "exit":
                return responses.exit;

            case "addPost":
                c = connectionsPool.getConnection();
                DBManagement.addPost(c,
                        obj.getString("title"),
                        obj.getString("post"),
                        obj.getString("author"));
                connectionsPool.returnConnection(c);
                c = null;
                return Json.createObjectBuilder()
                        .add("cmd", "postNum")
                        .add("num", DBManagement.getMaxId(c))
                        .build();

            case "getPost":
                c = connectionsPool.getConnection();
                Post temp = DBManagement.getPost(c, obj.getInt("ID"));
                connectionsPool.returnConnection(c);
                c = null;
                return Json.createObjectBuilder()
                            .add("cmd", "sentPost")
                            .add("num", obj.getInt("ID"))
                            .add("title", temp.title)
                            .add("post", temp.post)
                            .add("author", temp.author)
                            .build();

            case "postNum":
                c = connectionsPool.getConnection();
                int tempID = DBManagement.getMaxId(c);
                connectionsPool.returnConnection(c);
                c = null;
                return Json.createObjectBuilder()
                        .add("cmd", "postNum")
                        .add("num", tempID)
                        .build();

            default:
                return responses.exit;
        }
    }
}
