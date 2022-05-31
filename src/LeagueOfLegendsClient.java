import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class LeagueOfLegendsClient {
    private String APIkey;
    private String baseUrl;

    public LeagueOfLegendsClient(){

        APIkey = "RGAPI-c76cbc75-b524-436a-8482-9d4e3042beca";
        baseUrl = "https://na1.api.riotgames.com";
    }

    public ArrayList<Champion> parseJSONChampion(String summonerID){
        String endPoint = "/lol/champion-mastery/v4/champion-masteries/by-summoner/" + summonerID;
        String champUrl = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(champUrl);

        ArrayList<Champion> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for(int i = 0; i < 3; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("championId");
            int points = jsonObject.getInt("championPoints");
            String championName = "";
            String pictureURL = "";

            JSONObject jsonChamp = new JSONObject(makeAPICall("http://ddragon.leagueoflegends.com/cdn/12.9.1/data/en_US/champion.json"));
            JSONObject data = jsonChamp.getJSONObject("data");
            Iterator<String> keys = data.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                if (data.get(key) instanceof JSONObject) {
                    JSONObject character = data.getJSONObject(key);
                    int idInList = character.getInt("key");
                    if(idInList == id){
                        championName = character.getString("id");
                        pictureURL = "http://ddragon.leagueoflegends.com/cdn/12.9.1/img/champion/" + championName + ".png";
                    }

                }
            }
            Champion champion = new Champion(championName, pictureURL, points);
            list.add(champion);
        }
        return list;
    }


    public Player getPlayer(String name){
        String summonerID = getID(name);
        String endPoint = "/lol/league/v4/entries/by-summoner/" + summonerID;
        String playerUrl = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(playerUrl);

        JSONArray jsonArray = new JSONArray(response);
        JSONObject solo = null;
        JSONObject flex = null;
        for(int i = 0; i < jsonArray.length(); i++)
        {
            if(jsonArray.getJSONObject(i).getString("queueType").equals("RANKED_SOLO_5x5"))
            {
                solo = jsonArray.getJSONObject(i);
            }
            if(jsonArray.getJSONObject(i).getString("queueType").equals("RANKED_FLEX_SR"))
            {
                flex = jsonArray.getJSONObject(i);
            }
        }
        String soloRank = "Unranked";
        String soloTier = "Unranked";
        String flexRank = "Unranked";
        String flexTier = "Unranked";
        String tftRank = getTFTRank(summonerID);
        String tftTier = "Unranked";
        if(!tftRank.equals("Unranked")){
            tftTier = tftRank.substring(0, tftRank.indexOf(" "));
        }
        String soloWinLose = "";
        String soloWinRate = "";
        String flexWinLose = "";
        String flexWinRate = "";
        if(solo != null) {
            soloRank = solo.getString("tier") + " " + solo.getString("rank") + " " + solo.getInt("leaguePoints");
            soloTier = solo.getString("tier");
            soloWinLose = solo.getInt("wins") + " W  " + solo.getInt("losses") + " L";
            soloWinRate = round((double)solo.getInt("wins") / (solo.getInt("wins") + solo.getInt("losses")) * 100) + "%";
        }
        if(flex != null) {
            flexRank = flex.getString("tier") + " " + flex.getString("rank") + " " + flex.getInt("leaguePoints");
            flexTier = flex.getString("tier");
            flexWinLose = flex.getInt("wins") + " W  " + flex.getInt("losses") + " L";
            flexWinRate = round((double)flex.getInt("wins") / (flex.getInt("wins") + flex.getInt("losses")) * 100) + "%";
        }

        Player player = new Player(soloRank,soloTier, flexRank, flexTier, tftRank, tftTier, soloWinLose,soloWinRate,flexWinLose,flexWinRate,parseJSONChampion(summonerID), getIconUrl(name), getGameStatus(summonerID));

        return player;

    }


    public ArrayList<String> parseTopPlayers() {
        String endPoint = "/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(url);

        ArrayList<String> topPlayers = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray players = jsonObject. getJSONArray("entries");
        JSONObject player = null;
        int highestIdx = 0;
        for (int i = 0; i < 10; i++) {
            JSONObject highest = players.getJSONObject(0);
            for (int j = 1; j < players.length(); j++) {
                player = players.getJSONObject(j);
                if (player.getInt("leaguePoints") > highest.getInt("leaguePoints")) {
                    highest = player;
                    highestIdx = j;
                }
            }
            topPlayers.add(highest.getString("summonerName"));
            players.remove(highestIdx);
        }
        return topPlayers;
    }

    private String getID(String name){
        name = fixName(name);
        String endPoint = "/lol/summoner/v4/summoners/by-name/" + name;
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        JSONObject jsonObject = new JSONObject(makeAPICall(url));
        return jsonObject.getString("id");
    }


    private String getIconUrl(String name){
        name = fixName(name);
        String endPoint = "/lol/summoner/v4/summoners/by-name/" + name;
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        JSONObject jsonObject = new JSONObject(makeAPICall(url));
        int iconId = jsonObject.getInt("profileIconId");
        String iconUrl = "http://ddragon.leagueoflegends.com/cdn/12.10.1/img/profileicon/" + iconId + ".png";
        return iconUrl;
    }


 
    private String getTFTRank(String summonerID){
        String endPoint = "/tft/league/v1/entries/by-summoner/";
        String url = baseUrl + endPoint + summonerID + "?api_key=" + APIkey;
        String response = makeAPICall(url);

        String tftRank = "Unranked";
        JSONArray arr = new JSONArray(response);
        if(arr.length() != 0){
            JSONObject info = arr.getJSONObject(0);
 
            tftRank = info.getString("tier") + " " + info.getString("rank") + " " + info.getInt("leaguePoints");

 
        }
        return tftRank;
    }

 
    public String getGameStatus(String summonerID){
        String endPoint = "/lol/spectator/v4/active-games/by-summoner/";
        String url = baseUrl + endPoint + summonerID + "?api_key=" + APIkey;
        String response = makeAPICall(url);

        String status = "IN GAME ";
        try{
            JSONObject jsonObject = new JSONObject(response);
            String gameType = jsonObject.getString("gameMode");
            if(gameType.equals("CLASSIC")){
                status += "SUMMONER'S RIFT";
            }
            else{
                status += jsonObject.getString("gameMode");
            }
        }
        catch (Exception e){
            if(e.getMessage().contains("not found")){
                status = "NOT IN GAME";
            }
        }
        return status;
    }

 
 
    private String fixName(String name){
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

    private double round(double num){
        return Math.round(num * 100.0) / 100.0;
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