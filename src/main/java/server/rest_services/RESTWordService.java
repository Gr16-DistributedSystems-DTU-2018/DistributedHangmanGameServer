package server.rest_services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RESTWordService implements Remote {

    public static List<String> fetchWords() throws JSONException {
        Client client = ClientBuilder.newClient();
        Response response = client.target("https://www.dr.dk/mu-online/api/1.4/list/view/mostviewed?limit=50").request(MediaType.APPLICATION_JSON).get();
        String responseStr = response.readEntity(String.class);

        JSONObject json = new JSONObject(responseStr);
        JSONArray array = json.getJSONArray("Items");

        List<String> splittedArray = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = (JSONObject) array.get(i);
            String slug = o.getString("SeriesSlug");

            String desc;
            try {
                desc = new JSONObject(client.target("https://www.dr.dk/mu-online/api/1.4/programcard/" + slug).request(MediaType.APPLICATION_JSON).get().readEntity(String.class)).getString("Description");
            } catch (Exception e) {
                continue;
            }

            desc = desc.replaceAll("<.+?#>,:/", " ").toLowerCase().replaceAll("[^a-zæøå]", " ");
            desc = desc.trim().replaceAll(" +", " ");
            splittedArray.addAll(Arrays.asList(desc.split("\\s+")));
        }

        return splittedArray;
    }

}