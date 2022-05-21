public class Champion {
    private String name;
    private String pictureURL;
    private int points;

    public Champion(String name, String pictureURL, int points){
        this.name = name;
        this.pictureURL = pictureURL;
        this.points = points;
    }

    public String getName(){
        return name;
    }

    public int getPoints(){
        return points;
    }

    public String getPictureURL(){return pictureURL;}
}
