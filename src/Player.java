import java.util.ArrayList;

public class Player {
    private String soloRank;
    private String flexRank;
    private String rankURL;
    private double soloWinRate;
    private double flexWinRate;
    private ArrayList<Champion> mostPlayed;

    public Player(String soloRank, String flexRank, String rankURL, double soloWinRate, double flexWinRate, ArrayList<Champion> mostPlayed) {
        this.soloRank = soloRank;
        this.flexRank = flexRank;
        this.rankURL = rankURL;
        this.soloWinRate = soloWinRate;
        this.flexWinRate = flexWinRate;
        this.mostPlayed = mostPlayed;
    }

    public String getSoloRank() {
        return soloRank;
    }

    public String getFlexRank(){return flexRank;}

    public String getRankURL() {
        return rankURL;
    }

    public double getSoloWinRate() {
        return soloWinRate;
    }

    public double getFlexWinRate() {
        return flexWinRate;
    }

    public ArrayList<Champion> getMostPlayed() {
        return mostPlayed;
    }


}
