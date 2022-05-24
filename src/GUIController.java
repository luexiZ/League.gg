import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

// DAILY NOTICE: The following code is only to test JPanel format in GUI, no improve.
public class GUIController implements ActionListener
{
    private JTextField userEntryField;
    private JLabel Top1_5;
    private JLabel Top6_10;
    private JPanel bottomPanel;
    private Player player;
    private LeagueOfLegendsClient client;
    private CardLayout card;
    private JLabel pictureLabel1;
    private JLabel pictureLabel2;
    private JLabel pictureLabel3;


    public GUIController()
    {
        userEntryField = new JTextField(9);
        Top1_5 = new JLabel();
        Top6_10 = new JLabel();
        bottomPanel = new JPanel();
        card = new CardLayout();
        player = null;
        client = new LeagueOfLegendsClient();
        pictureLabel1 = new JLabel();
        pictureLabel2 = new JLabel();
        pictureLabel3 = new JLabel();
        screenGUI();

    }

    public void screenGUI()
    {
        JFrame frame = new JFrame("League.GG");
        frame.setPreferredSize( new Dimension(480, 220));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);


        JPanel searchPanel = new JPanel();


        JLabel message = new JLabel("<html><i>Enter the UserName</i><html>");
        JButton submit = new JButton("Submit");
        JButton clear = new JButton("Clear");

        searchPanel.add(message);
        searchPanel.add(userEntryField);
        searchPanel.add(submit);
        searchPanel.add(clear);

        JPanel title = new JPanel();
        JLabel titleLabel = new JLabel("Top Player");
        titleLabel.setFont(new Font("Serif",Font.BOLD, 20 ));
        titleLabel.setForeground(Color.green);
        title.add(titleLabel);

        JPanel topPlayerPanel = new JPanel();
        GridLayout layout = new GridLayout(1,2);
        topPlayerPanel.setLayout(layout);
        bottomPanel.setLayout(card);
        layout.setHgap(10);
        Top1_5 = new JLabel("Top 5 ");
        Top1_5.setHorizontalAlignment(SwingConstants.CENTER);
        Top6_10 = new JLabel("<html>6. <br> player6 <br> 7. player7 <br> 8. player8 <br> 9. player9 <br> 10. player10 <br> <html>");
        Top6_10.setHorizontalAlignment(SwingConstants.CENTER);


        topPlayerPanel.add(Top1_5);
        topPlayerPanel.add(Top6_10);
        bottomPanel.add(topPlayerPanel);


        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(title, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);


        submit.addActionListener(this);
        clear.addActionListener(this);

        frame.pack();
        frame.setVisible(true);
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) (e.getSource());
        String text = button.getText();

        if (text.equals("Submit"))
        {
            String UserName = userEntryField.getText();
            displayInfo(UserName);
        }
        else if (text.equals("Clear"))
        {
            userEntryField.setText("");
            card.next(bottomPanel);


        }
    }

    public void displayInfo(String Username) // call this method when submit is click ; add PARAMETERS !
    {
        JPanel infoPanel = new JPanel();
        player = client.getPlayer(Username);
        ArrayList<Champion> championList = player.getMostPlayed();

        JLabel rankLabel = new JLabel("<html> Solo Rank: " + player.getSoloRank() + "      " +"                                   <br> Solo Win Rate: " +  winRate(player.getSoloWinRate()) + "<br>Flex Rank: " + player.getFlexRank()+ "<br>Flex WinRate: "+ winRate(player.getFlexWinRate()) + "<br> <html>");
        Champion champ1 = player.getMostPlayed().get(0);
        Champion champ2 = player.getMostPlayed().get(1);
        Champion champ3 = player.getMostPlayed().get(2);
        JLabel championsLabel = new JLabel("<html>Most Played: <br>" + champ1.getName() + " " + champ1.getPoints() + "<br>" + champ2.getName() + " " + champ2.getPoints() + "<br>" + champ3.getName() + " " + champ3.getPoints() +"<br> <html>");
        postImage(player.getMostPlayed());
        infoPanel.add(rankLabel, BorderLayout.WEST);
        infoPanel.add(championsLabel, BorderLayout.EAST);
        infoPanel.add(pictureLabel1);
        infoPanel.add(pictureLabel2);
        infoPanel.add(pictureLabel3);
        bottomPanel.add(infoPanel);

        card.next(bottomPanel);


    }

    public String winRate(double winRate){
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(winRate * 100)+"%";
    }

    public void postImage(ArrayList<Champion> champions)
    {
        for(int i = 0; i < champions.size(); i++)
        {
            BufferedImage bufImg = null;
            Image tmp = null;
            try {
                URL url = new URL(champions.get(i).getPictureURL());
                bufImg = ImageIO.read(url);
                tmp = bufImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert false;
            if(i == 0){
                pictureLabel1.setIcon(new ImageIcon(tmp));
            }
            else if(i == 1){
                pictureLabel2.setIcon(new ImageIcon(tmp));
            }
            else{
                pictureLabel3.setIcon(new ImageIcon(tmp));
            }
        }
    }

//    public void topPlayer(){
//        ArrayList<String> topPlayer = client.parseTopPlayers();
//        for(int i = 0; i  topPlayer.size())
//    }



}
