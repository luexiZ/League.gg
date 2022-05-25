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

// DAILY NOTICE:
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
        Image icon = Toolkit.getDefaultToolkit().getImage("src/icon.png");
        frame.setIconImage(icon);
        frame.setPreferredSize( new Dimension(1200, 700));
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
        ArrayList<String> topPlayer = client.parseTopPlayers();
        Top1_5 = new JLabel("<html>1. " + topPlayer.get(0) + "<br> 2. " + topPlayer.get(1) + "<br> 3. " + topPlayer.get(2) + "<br> 4. "+ topPlayer.get(3) + "<br> 5. "+ topPlayer.get(4) + "<br> <html>");
        Top1_5.setHorizontalAlignment(SwingConstants.CENTER);
        Top1_5.setFont(new Font("Serif",Font.BOLD, 25 ));
        Top6_10 = new JLabel("<html>6. " + topPlayer.get(5) + "<br> 7. " + topPlayer.get(6) + "<br> 8. " + topPlayer.get(7) + "<br> 9. "+ topPlayer.get(8) + "<br> 10. "+ topPlayer.get(9) + "<br> <html>");
        Top6_10.setHorizontalAlignment(SwingConstants.CENTER);
        Top6_10.setFont(new Font("Serif",Font.BOLD, 25 ));




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
        GridLayout layout = new GridLayout(3, 2);
        layout.setHgap(10);
        infoPanel.setLayout(layout);
        player = client.getPlayer(Username);
        ArrayList<Champion> championList = player.getMostPlayed();


        Champion champ1 = championList.get(0);
        Champion champ2 = championList.get(1);
        Champion champ3 = championList.get(2);
        ImageIcon rankSolo = new ImageIcon("src/" + player.getSoloTier() + ".png");
        Image solo = rankSolo.getImage();
        Image solo1 = solo.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        rankSolo = new ImageIcon(solo1);
        ImageIcon rankFlex = new ImageIcon("src/" + player.getFlexTier() + ".png");
        JLabel rankLabel1 = new JLabel(/*<html>*/ "Ranked Solo/Duo" + /*"<br>"+*/ player.getSoloRank() /*+ "<br>"*/ + winRate(player.getSoloWinRate()) /*+ "<html>"*/, rankSolo, SwingConstants.LEFT);
        JLabel rankLabel2 = new JLabel("<html> Flex SR :" + "<br>" + player.getFlexRank() + "<br>" + winRate(player.getFlexWinRate()) + "<html>");
        JLabel placeholder = new JLabel();
       // JLabel rankLabel3 = new JLabel("Ranked TFT: " + player.getSoloRank());
        JLabel champLabel1 = new JLabel(champ1.getName() + "     Mastery Points: " + champ1.getPoints() , getImage(champ1.getPictureURL()), SwingConstants.LEFT);
        JLabel champLabel2 = new JLabel(champ2.getName() + "     Mastery Points: " + champ2.getPoints(), getImage(champ2.getPictureURL()), SwingConstants.LEFT);
        JLabel champLabel3 = new JLabel(champ3.getName() + "     Mastery Points: " + champ3.getPoints(), getImage(champ3.getPictureURL()), SwingConstants.LEFT);
        rankLabel1.setFont(new Font("Serif",Font.BOLD, 20 ));
        rankLabel2.setFont(new Font("Serif",Font.BOLD, 20 ));
       // rankLabel3.setFont(new Font("Serif",Font.BOLD, 30 ));
        champLabel1.setFont(new Font("Arial",Font.BOLD, 20 ));
        champLabel2.setFont(new Font("Arial",Font.BOLD, 20 ));
        champLabel3.setFont(new Font("Arial",Font.BOLD, 20 ));

        infoPanel.add(rankLabel1);
        infoPanel.add(champLabel1);
        infoPanel.add(placeholder);
        infoPanel.add(champLabel2);
       // infoPanel.add(rankLabel3);
        infoPanel.add(rankLabel2);
        infoPanel.add(champLabel3);
        bottomPanel.add(infoPanel);

        card.next(bottomPanel);


    }

    public ImageIcon getImage(String url){
        ImageIcon imageIcon = null;
        try {
            URL imageURL = new URL(url);
            BufferedImage image = ImageIO.read(imageURL);
            imageIcon = new ImageIcon(image);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return imageIcon;
    }

    public String winRate(double winRate){
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(winRate * 100)+"%";
    }


}
