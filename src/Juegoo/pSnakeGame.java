package Juegoo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JPanel;

public class pSnakeGame extends JPanel implements KeyListener, ActionListener {

    //Pantalla
    private final int anchoP = 800;
    private final int altoP = 600;
    private final int cuadradoSize = 25;
    private final int totalCuadritosSizeA = (int) altoP / cuadradoSize;
    private final int totalCuadritosSizeL = (int) anchoP / cuadradoSize;
    private Timer timer;
    MAME mame;

    //Snake
    private final int tamañoTotalSnake = totalCuadritosSizeA * totalCuadritosSizeL;
    List<Snake> snake = new ArrayList<>();
    private char direc = ' ';
    private boolean jugando = true;

    //Manzana
    Manzana manzana = new Manzana(totalCuadritosSizeL, totalCuadritosSizeA, cuadradoSize);

    public pSnakeGame(JFrame frame) {
        mame = (MAME) frame;
        
        //initJuego();
    }

    public void setJugando(boolean jugando) {
        this.jugando = jugando;
    }
    
    public void initJuego() {
        this.setPreferredSize(new Dimension(anchoP, altoP));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setVisible(true);
        inicSnake(snake);
        manzana.doradaORoja(totalCuadritosSizeL, totalCuadritosSizeA, cuadradoSize, this.snake);
        this.addKeyListener(this);
        timer = new Timer(100, this);
        timer.start();
    }

    private void inicSnake(List<Snake> S) {
        Snake cabeza = new Snake((totalCuadritosSizeL / 2) * cuadradoSize, (totalCuadritosSizeA / 2) * cuadradoSize,
                new ImageIcon(getClass().getResource("/snakeImag/cabezaW.png")));
        Snake cuerpo = new Snake((totalCuadritosSizeL / 2) * cuadradoSize, cabeza.getY() + cuadradoSize,
                new ImageIcon(getClass().getResource("/snakeImag/cuerpo.png")));
        Snake cola = new Snake((totalCuadritosSizeL / 2) * cuadradoSize, cuerpo.getY() + cuadradoSize,
                new ImageIcon(getClass().getResource("/snakeImag/colaW.png")));
        S.add(cabeza);
        S.add(cuerpo);
        S.add(cola);
    }

    private void serpienteCreciendo(List<Snake> snake, Manzana manzanita) {
        if (snake.get(0).getX() == manzanita.getX() && snake.get(0).getY() == manzanita.getY()) {
            if (!manzanita.isDorada()) {
                snake.add(snake.size() - 1, new Snake(snake.get(snake.size() - 2).getX(), snake.get(snake.size() - 2).getY(), new ImageIcon(getClass().getResource("/snakeImag/cuerpo.png"))));
            } else {
                for (int i = 0; i < 3; i++) {
                    snake.add(snake.size() - 1, new Snake(snake.get(snake.size() - 2).getX(), snake.get(snake.size() - 2).getY(), new ImageIcon(getClass().getResource("/snakeImag/cuerpo.png"))));
                }
            }
            manzanita.doradaORoja(totalCuadritosSizeL, totalCuadritosSizeA, cuadradoSize, this.snake);
        }
    }

    private void colisionPersonal(List<Snake> snake) {
        int i = 1;
        while (i < snake.size() - 1) {
            if (snake.get(0).getX() == snake.get(i).getX() && snake.get(0).getY() == snake.get(i).getY()) {
                this.jugando = false;
            }
            i++;
        }
    }

    private void reaparicion(List<Snake> s) {

        if (s.get(0).getX() >= (totalCuadritosSizeL * cuadradoSize)) {
            s.get(0).setX(0);
        } else if (s.get(0).getX() < 0) {
            s.get(0).setX(totalCuadritosSizeL * cuadradoSize);
        }

        if (s.get(0).getY() >= (totalCuadritosSizeA * cuadradoSize)) {
            s.get(0).setY(0);
        } else if (s.get(0).getY() < 0) {
            s.get(0).setY(totalCuadritosSizeA * cuadradoSize);
        }
    }

    private void moverSerpiente() {

        switch (this.direc) {
            case 'w':
                this.snake.get(0).setImagen(new ImageIcon(getClass().getResource("/snakeImag/cabezaW.png")));
                movimientoCabeza(0, -cuadradoSize, this.snake);
                break;
            case 's':
                this.snake.get(0).setImagen(new ImageIcon(getClass().getResource("/snakeImag/cabezaS.png")));
                movimientoCabeza(0, +cuadradoSize, this.snake);
                break;
            case 'a':
                this.snake.get(0).setImagen(new ImageIcon(getClass().getResource("/snakeImag/cabezaA.png")));
                movimientoCabeza(-cuadradoSize, 0, this.snake);
                break;
            case 'd':
                this.snake.get(0).setImagen(new ImageIcon(getClass().getResource("/snakeImag/cabezaD.png")));
                movimientoCabeza(+cuadradoSize, 0, this.snake);
                break;
        }
    }

    private void movimientoCabeza(int x, int y, List<Snake> s) {
        int i = 0;
        int posActualX, posActualY, posProxX, posProxY;
        posProxX = s.get(0).getX() + x;
        posProxY = s.get(0).getY() + y;

        while (i < this.snake.size()) {
            posActualX = s.get(i).getX();
            posActualY = s.get(i).getY();
            s.get(i).setX(posProxX);
            s.get(i).setY(posProxY);
            posProxX = posActualX;
            posProxY = posActualY;
            i++;
        }
    }

    private void movimientoCola(List<Snake> s) {
        if (s.get(s.size() - 2).getX() == s.get(s.size() - 1).getX() && s.get(s.size() - 2).getY() < s.get(s.size() - 1).getY()) {
            s.get(s.size() - 1).setImagen(new ImageIcon(getClass().getResource("/snakeImag/colaW.png")));
        } else if (s.get(s.size() - 2).getX() == s.get(s.size() - 1).getX() && s.get(s.size() - 2).getY() > s.get(s.size() - 1).getY()) {
            s.get(s.size() - 1).setImagen(new ImageIcon(getClass().getResource("/snakeImag/colaS.png")));
        } else if (s.get(s.size() - 2).getY() == s.get(s.size() - 1).getY() && s.get(s.size() - 2).getX() > s.get(s.size() - 1).getX()) {
            s.get(s.size() - 1).setImagen(new ImageIcon(getClass().getResource("/snakeImag/colaD.png")));
        } else if (s.get(s.size() - 2).getY() == s.get(s.size() - 1).getY() && s.get(s.size() - 2).getX() < s.get(s.size() - 1).getX()) {
            s.get(s.size() - 1).setImagen(new ImageIcon(getClass().getResource("/snakeImag/colaA.png")));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        //TABLERO
        /*for (int i = 0; i < totalCuadritosSizeA; i++) {
            g.drawLine(0, cuadradoSize * i, anchoP, cuadradoSize * i);
        }
        for (int i = 0; i < totalCuadritosSizeL; i++) {
            g.drawLine(cuadradoSize * i, 0, cuadradoSize * i, altoP);
        }*/

        movimientoCola(this.snake);
        //SERPIENTE
        for (int i = 0; i < this.snake.size(); i++) {
            g.drawImage(this.snake.get(i).getImagen().getImage(), this.snake.get(i).getX(), this.snake.get(i).getY(), cuadradoSize, cuadradoSize, null);
        }
        g.drawImage(manzana.getImagen().getImage(), manzana.getX(), manzana.getY(), cuadradoSize, cuadradoSize + 5, null);
    }

    //BUCLE DE ACCION
    @Override
    public void actionPerformed(ActionEvent e) {
        if (jugando) {
            moverSerpiente();
            reaparicion(this.snake);
            serpienteCreciendo(this.snake, manzana);
            colisionPersonal(this.snake);
        } else {
            this.setVisible(jugando);
            timer.stop();
            this.snake.clear();
            this.direc = ' ';
            mame.cambiarpanelGameOver();
        }
        this.repaint();
    }

    //TECLAS
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                if (direc != 's') {
                    direc = 'w';
                }
            }
            case KeyEvent.VK_S -> {
                if (direc != 'w') {
                    direc = 's';
                }
            }
            case KeyEvent.VK_A -> {
                if (direc != 'd') {
                    direc = 'a';
                }
            }
            case KeyEvent.VK_D -> {
                if (direc != 'a') {
                    direc = 'd';
                }
            }
            case KeyEvent.VK_UP -> {
                if (direc != 's') {
                    direc = 'w';
                }
            }
            case KeyEvent.VK_DOWN -> {
                if (direc != 'w') {
                    direc = 's';
                }
            }
            case KeyEvent.VK_LEFT -> {
                if (direc != 'd') {
                    direc = 'a';
                }
            }
            case KeyEvent.VK_RIGHT -> {
                if (direc != 'a') {
                    direc = 'd';
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
