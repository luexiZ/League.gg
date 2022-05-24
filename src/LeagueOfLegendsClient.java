import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class LeagueOfLegendsClient {
    private String APIkey;
    private String baseUrl;

    public LeagueOfLegendsClient(){
        APIkey = "RGAPI-76a91ed0-ba79-4675-bc95-640a30c8cec1";
        baseUrl = "https://na1.api.riotgames.com/lol";
    }

    public String getID(String name){
        name = fixName(name);
        String endPoint = "/summoner/v4/summoners/by-name/" + name;
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        JSONObject jsonObject = new JSONObject(makeAPICall(url));
        return jsonObject.getString("id");
    }


    public ArrayList<Champion> parseJSONChampion(String summonerID){
        String endPoint = "/champion-mastery/v4/champion-masteries/by-summoner/" + summonerID;
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(url);
        ArrayList<Champion> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for(int i = 0; i < 3; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("championId");
            int points = jsonObject.getInt("championPointsSinceLastLevel");
            String name = "";
            String pictureURL = "";

            JSONObject jsonChamp = new JSONObject(makeAPICall("http://ddragon.leagueoflegends.com/cdn/12.9.1/data/en_US/champion.json"));
            JSONObject data = jsonChamp.getJSONObject("data");
            Iterator x = data.keys();
            JSONArray jsonArray1 = new JSONArray();
            while (x.hasNext()){
                String key = (String)x.next();
                jsonArray.put(data.get(key));
            }
            for(int j = 0; j < jsonArray1.length(); j++){
                if(jsonArray1.getJSONObject(j).getInt("key") == id){
                    name = jsonArray1.getJSONObject(j).getString("id");
                    pictureURL = "http://ddragon.leagueoflegends.com/cdn/12.9.1/img/champion/" + name + ".png";
                }
            }
            Champion champion = new Champion(name, pictureURL, points);
            list.add(champion);
        }
        return list;
    }

    public Player getPlayer(String name){
        String summonerID = getID(name);
        String endPoint = "/league/v4/entries/by-summoner/" + summonerID;
        String url = baseUrl + endPoint + "?api_key=" + APIkey;

        String soloRank;
        String flexRank;
        double soloWinRate;
        double flexWinRate;
        JSONArray jsonArray = new JSONArray(makeAPICall(url));
        JSONObject solo = jsonArray.getJSONObject(1);
        JSONObject flex = jsonArray.getJSONObject(2);
        soloRank = solo.getString("tier") + " " + solo.getString("rank") + " " + solo.getInt("leaguePoints");
        flexRank = flex.getString("tier") + " " + flex.getString("rank") + " " + flex.getInt("leaguePoints");
        soloWinRate = (double)solo.getInt("wins") / (solo.getInt("wins") + solo.getInt("losses"));
        flexWinRate = (double)flex.getInt("wins") / (flex.getInt("wins") + flex.getInt("losses"));
        Player player = new Player(soloRank,flexRank, "pictureFile",soloWinRate,flexWinRate,parseJSONChampion(summonerID));
        return player;

    }

    private static String fixName(String name){
        String[] nameList = name.split("");
        for(int i = 0; i < nameList.length; i++){
            if(nameList[i].equals(" ")){
                nameList[i] = "%20";
            }
        }
        String result = "";
        for(String str : nameList){
            result += str;
        }
        return result;
    }

    public static String makeAPICall(String url)
    {
        try {
            URI myUri = URI.create(url); // creates a URI object from the url string
            HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


}
