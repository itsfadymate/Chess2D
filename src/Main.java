import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception{

        Board b = new Board();
        b.consoleDisplay();
        JFrame game = new JFrame("CHESS");
        game.setSize(1200,750);

        BufferedImage image = ImageIO.read(new File("backGround.png"));
        JPanel backgroundImage = new JPanel(){
            @Override
            protected  void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(image,0,0,null);
            }
        };
        game.setContentPane(backgroundImage);


        game.setLocationRelativeTo(null);
        game.setResizable(false);
        game.setLayout(null);
        displayPanel gamePanel = new displayPanel(game);
        gamePanel.setBounds(300,75,600,600);
        game.add(gamePanel);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setVisible(true);



    }
}