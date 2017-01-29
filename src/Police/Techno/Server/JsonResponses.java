package Police.Techno.Server;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by kubri on 1/29/2017.
 */
public class JsonResponses {
    final JsonObject exit = Json.createObjectBuilder().add("cmd", "exit").build();
}
