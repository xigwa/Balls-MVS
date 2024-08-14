package org.example.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Barrier {
    private int x, y, width, height;
    private Color color;
    private int number;
    private Random random = new Random();

    public Barrier(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.number = random.nextInt(14) + 2;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(number), x + width / 2 - 10, y + height / 2 + 5);
    }

    public void reduceNumber() {
        number--;
        if (number <= 0) {
            moveToRandomPosition();
            number = random.nextInt(14) + 2;
        }
    }
    private void moveToRandomPosition() {
        x = random.nextInt(800 - width);
        y = random.nextInt(600 - height);
    }
}