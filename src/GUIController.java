import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

// DAILY NOTICE: The following code is only to test JPanel format in GUI, no improve.
public class GUIController implements ActionListener
{
    private JTextField userEntryField;
    private JLabel Top1_5;
    private JLabel Top6_10;
    private Player results;
    private LeagueOfLegendsClient client;

    public GUIController()
    {
        userEntryField = new JTextField(9);
        Top1_5 = new JLabel();
        Top6_10 = new JLabel();
        results = null;
        client = new LeagueOfLegendsClient();
        screenGUI();

    }

    public void screenGUI()
    {
        JFrame frame = new JFrame("League.GG");
        frame.setPreferredSize( new Dimension(500, 220));
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


        JPanel topPlayers = new JPanel();
        GridLayout layout = new GridLayout(1,2);
        topPlayers.setLayout(layout);
        layout.setHgap(10);
        Top1_5 = new JLabel("<html>1.  <br> 2. Quntao Zheng <br> 3. Qihong , <br> 4. player4 <br> 5. player5 <html>");
        Top1_5.setHorizontalAlignment(SwingConstants.CENTER);
        Top6_10 = new JLabel("<html>6. <br> player6 <br> 7. player7 <br> 8. player8 <br> 9. player9 <br> 10. player10 <br> <html>");
        Top6_10.setHorizontalAlignment(SwingConstants.CENTER);


        topPlayers.add(Top1_5);
        topPlayers.add(Top6_10);


        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(title, BorderLayout.CENTER);
        frame.add(topPlayers, BorderLayout.SOUTH);


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


        }
    }

    public void displayInfo(String Username) // call this method when submit is click ; add PARAMETERS !
    {
        results = client.getPlayer(Username);
        String Champ = "";

        for(int i = 0; i < 3; i++){
            String name = results.getMostPlayed().get(i).getName();
            Champ += name + " ";
        }
    Top1_5.setText("<html> Rank" +  results.getSoloRank() + " <br> Solo Win Rate: " +  results.getSoloWinRate() + "<br>FLEX: " + results.getFlexRank()+ "<br>FlexWinRate :"+ results.getFlexWinRate() + "<br>MostPlayed:" + Champ + "<html>");



    }
}
