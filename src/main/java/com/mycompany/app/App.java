/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

package com.mycompany.app;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridGame extends JPanel implements KeyListener {
    
    private final int CELL_SIZE = 10; // size of each cell in pixels
    private final int GRID_WIDTH = 60; // number of cells in width
    private final int GRID_HEIGHT = 40; // number of cells in height
    private final List<Point> snake = new ArrayList<Point>(); // list of points representing the snake
    private Point food = null; // point representing the food
    private int dx = 0; // direction of movement
    private int dy = 0;
    private boolean gameOver = false; // flag to check if game is over

    public GridGame() {
        setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
    }
    
    private void initGame() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_WIDTH - 10) + 5; // starting position of snake
        int y = rand.nextInt(GRID_HEIGHT - 10) + 5;
        snake.clear();
        snake.add(new Point(x, y));
        dx = 1; // move to the right
        dy = 0;
        spawnFood();
        gameOver = false;
    }
    
    private void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_WIDTH);
        int y = rand.nextInt(GRID_HEIGHT);
        for (Point p : snake) {
            if (p.x == x && p.y == y) {
                // food spawned on snake, try again
                spawnFood();
                return;
            }
        }
        food = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!gameOver) {
            // draw snake
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
            // draw food
            g.setColor(Color.RED);
            g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        } else {
            // game over, display message
            g.setColor(Color.WHITE);
            g.drawString("Game Over!", GRID_WIDTH * CELL_SIZE / 2 - 40, GRID_HEIGHT * CELL_SIZE / 2);
        }
    }

    private void move() {
        // move snake according to current direction
        Point head = snake.get(0);
        Point newHead = new Point(head.x + dx, head.y + dy);
        if (newHead.x < 0 || newHead.x >= GRID_WIDTH || newHead.y < 0 || newHead.y >= GRID_HEIGHT) {
            // collision with border, game over
            gameOver = true;
            return;
        }
        for (Point p : snake) {
            if (p.x == newHead.x && p.y == newHead.y) {
                // collision with snake, game over
                gameOver = true;
                return;
            }
        }
        snake.add(0, newHead);
        if (food != null && newHead.x == food.x && newHead.y == food.y) {
            // snake eats food, spawn new food and increase size of snake
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (dy == 0) {
                    dx = 0;
                    dy = -1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (dy == 0) {
                    dx = 0;
                    dy = 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (dx == 0) {
                    dx = -1;
                    dy = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (dx == 0) {
                    dx = 1;
                    dy = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
                if (gameOver) {
                    initGame();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Grid Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new GridGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}



