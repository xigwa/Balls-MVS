package org.example.view;

import org.example.model.Ball;
import org.example.model.Barrier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameView extends JPanel {
    private List<Ball> balls;
    private List<Barrier> barriers;
    private BufferedImage image;
    private int imageX, imageY;

    public GameView(List<Ball> balls, List<Barrier> barriers) {
        this.balls = balls;
        this.barriers = barriers;

        try {
            image = ImageIO.read(new File("src/main/resources/klipartz.com.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.imageX = 400;
        this.imageY = 300;
    }

    public void setImageX(int imageX) {
        this.imageX = imageX;
    }

    public void setImageY(int imageY) {
        this.imageY = imageY;
    }

    public void moveImageTo(int x, int y) {
        this.imageX = x;
        this.imageY = y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());


        if (image != null)
            g.drawImage(image, imageX, imageY, null);

        for (Barrier barrier : barriers)
            barrier.draw(g);

        for (Ball ball : balls)
            ball.draw(g);
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getImageX() {
        return imageX;
    }

    public int getImageY() {
        return imageY;
    }
    public int getImageWidth() {
        return image.getWidth();
    }
    public int getImageHeight() {
        return image.getHeight();
    }
}
