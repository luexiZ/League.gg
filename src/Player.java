import java.util.ArrayList;

public class Player {
    private String soloRank;
    private String soloTier;
    private String flexRank;
    private String flexTier;
    private String tftRank;
    private String tftTier;
    private String soloWinLose;
    private String soloWinRate;
    private String flexWinLose;
    private String flexWinRate;
    private ArrayList<Champion> mostPlayed;

    public Player(String soloRank, String soloTier, String flexRank, String flexTier, String tftRank, String tftTier, String soloWinLose, String soloWinRate, String flexWinlose, String flexWinRate, ArrayList<Champion> mostPlayed) {
        this.soloRank = soloRank;
        this.soloTier = soloTier;
        this.flexRank = flexRank;
        this.flexTier = flexTier;
        this.tftRank = tftRank;
        this.tftTier = tftTier;
        this.soloWinLose = soloWinLose;
        this.soloWinRate = soloWinRate;
        this.flexWinLose = flexWinlose;
        this.flexWinRate = flexWinRate;
        this.mostPlayed = mostPlayed;
    }

    public String getSoloRank() {
        return soloRank;
    }

    public String getFlexRank(){return flexRank;}

    public String getTftRank() {
        return tftRank;
    }

    public String getTftTier(){return tftTier;}

    public String getSoloWinLose(){
        return soloWinLose;
    }

    public String getFlexWinLose(){
        return flexWinLose;
    }

    public String getSoloWinRate() {
        return soloWinRate;
    }

    public String getFlexWinRate() {
        return flexWinRate;
    }

    public String getSoloTier(){
        return soloTier;
    }

    public String getFlexTier(){
        return flexTier;
    }

    public ArrayList<Champion> getMostPlayed() {
        return mostPlayed;
    }


}
