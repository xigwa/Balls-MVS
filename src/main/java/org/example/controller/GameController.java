package org.example.controller;

import org.example.model.Ball;
import org.example.model.Barrier;
import org.example.view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController implements MouseListener {
    private GameView gameView;
    private List<Ball> balls = new ArrayList<>();
    private List<Barrier> barriers = new ArrayList<>();
    private int score = 0;
    private JLabel scoreLabel = new JLabel("Score: 0");
    private final Random random = new Random();

    public GameController() {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);

        barriers.add(new Barrier(100, 100, 100, 20, Color.BLUE));
        barriers.add(new Barrier(300, 200, 20, 100, Color.GREEN));
        barriers.add(new Barrier(500, 400, 150, 20, Color.YELLOW));

        gameView = new GameView(balls, barriers);
        gameView.setBounds(0, 0, 800, 600);
        frame.add(gameView);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(650, 10, 150, 30);
        frame.add(scoreLabel);

        frame.setVisible(true);
        frame.addMouseListener(this);

        new GameThread().start();
    }

    private void moveImageToRandomPosition() {
        int maxAttempts = 100;
        boolean validPositionFound = false;
        int attempts = 0;

        while (!validPositionFound && attempts < maxAttempts) {
            int newX = random.nextInt(gameView.getWidth() - gameView.getImageWidth());
            int newY = random.nextInt(gameView.getHeight() - gameView.getImageHeight());

            Rectangle newImageBounds = new Rectangle(newX, newY, gameView.getImageWidth(), gameView.getImageHeight());
            boolean intersects = false;

            for (Barrier barrier : barriers) {
                if (newImageBounds.intersects(barrier.getBounds())) {
                    intersects = true;
                    break;
                }
            }

            if (!intersects) {
                gameView.moveImageTo(newX, newY);
                validPositionFound = true;
            }

            attempts++;
        }

        if (!validPositionFound) {
            System.out.println("Не вдалося знайти вільне місце для зображення після " + maxAttempts + " спроб.");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int xStep = random.nextInt(20) - 10;
        int yStep = random.nextInt(20) - 10;
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Ball ball = new Ball(x, y, xStep, yStep, color);
        balls.add(ball);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    private class GameThread extends Thread {
        @Override
        public void run() {
            while (true) {
                for (Ball ball : balls) {
                    ball.move();

                    if (ball.getX() <= 0 || ball.getX() + ball.getRad() >= gameView.getWidth())
                        ball.setxStep(-ball.getxStep());
                    if (ball.getY() <= 0 || ball.getY() + ball.getRad() >= gameView.getHeight())
                        ball.setyStep(-ball.getyStep());


                    for (Barrier barrier : barriers) {
                        Rectangle ballBounds = new Rectangle(ball.getX(), ball.getY(), ball.getRad(), ball.getRad());

                        if (ballBounds.intersects(barrier.getBounds())) {
                            barrier.reduceNumber();
                            score++;
                            updateScore();
                            moveImageToRandomPosition();

                            if (ballBounds.intersectsLine(barrier.getBounds().getMinX(), barrier.getBounds().getMinY(), barrier.getBounds().getMinX(), barrier.getBounds().getMaxY()) ||
                                    ballBounds.intersectsLine(barrier.getBounds().getMaxX(), barrier.getBounds().getMinY(), barrier.getBounds().getMaxX(), barrier.getBounds().getMaxY())) {
                                ball.setxStep(-ball.getxStep());
                            }

                            if (ballBounds.intersectsLine(barrier.getBounds().getMinX(), barrier.getBounds().getMinY(), barrier.getBounds().getMaxX(), barrier.getBounds().getMinY()) ||
                                    ballBounds.intersectsLine(barrier.getBounds().getMinX(), barrier.getBounds().getMaxY(), barrier.getBounds().getMaxX(), barrier.getBounds().getMaxY())) {
                                ball.setyStep(-ball.getyStep());
                            }
                        }
                    }
                }

                gameView.repaint();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
