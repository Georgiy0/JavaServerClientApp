package Police.Techno.Client;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Scanner;

/**
 * Created by kubri on 1/26/2017.
 */
public class JsonHandlerClient {
    private int postNum = 0;
    int exit = 0;
    public JsonObject processInput(JsonObject input){
        Scanner sc = new Scanner(System.in);
        switch (input.getString("cmd")){

            case "init":
                return Json.createObjectBuilder().add("cmd", "postNum").build();

            case "postNum":
                if(input.getInt("num") == postNum) {
                    System.out.println("There are no more posts.");
                    System.out.println("post (2), refresh (1), exit (0)");
                    exit = sc.nextInt();
                    switch (exit) {
                        case 0:
                            return Json.createObjectBuilder().add("cmd", "exit").build();
                        case 2:
                            System.out.println("Enter title: ");
                            sc.nextLine();
                            String title = sc.nextLine();
                            String post = "";
                            String temp;
                            System.out.println("Enter post (end)");
                            while(!(temp = sc.nextLine()).equals("end")) {
                                post = post + temp + "\n";
                            }
                            System.out.println("Enter author: ");
                            String author = sc.nextLine();
                            return Json.createObjectBuilder()
                                    .add("cmd", "addPost")
                                    .add("title", title)
                                    .add("post", post)
                                    .add("author", author)
                                    .build();
                        default:
                            return Json.createObjectBuilder().add("cmd", "postNum").build();
                    }
                }
                else {
                    int temp = postNum;
                    postNum = input.getInt("num");
                    return Json.createObjectBuilder().add("cmd", "getPost").add("ID", temp+1).build();
                }

            case "sentPost":
                System.out.println(input.getString("title"));
                System.out.println();
                System.out.println(input.getString("post"));
                System.out.println();
                System.out.println("Author: "+input.getString("author"));
                System.out.println();
                if(input.getInt("num") == postNum) {
                    return Json.createObjectBuilder().add("cmd", "postNum").build();
                }
                else
                    return Json.createObjectBuilder()
                            .add("cmd", "getPost")
                            .add("ID", input.getInt("num")+1)
                            .build();

            default:
                return Json.createObjectBuilder().add("cmd", "exit").build();
        }
    }
}
