package PingPong;

import Juegoo.MAME;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class pPong extends JPanel implements KeyListener, Runnable {

    private static final int ancho = 1000;
    private static final int alto = 500;
    private static final int DiametroPelota = 15;
    private static final int anchoPaleta = 20;
    private static final int altoPaleta = 80;
    private boolean jugando = true;
    private Thread hiloDeJuego;
    private Image imagen;
    private Graphics graficos;
    private Random random;
    private Raqueta raquetaP1;
    private Raqueta raquetaP2;
    private Pelota pelota;
    private MAME mame;

    public pPong(JFrame frame) {
        mame = (MAME) frame;
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(ancho, alto));
        this.setVisible(true);
        //initPong();
    }

    public void initPong() {
        crearRaquetas();
        crearPelota();
        this.addKeyListener(this);
        hiloDeJuego = new Thread(this);
        hiloDeJuego.start();
    }

    public void crearPelota() {
        random = new Random();
        pelota = new Pelota((ancho / 2) - (DiametroPelota / 2), (alto / 2) - (DiametroPelota / 2), DiametroPelota, DiametroPelota);
    }

    public void crearRaquetas() {
        raquetaP1 = new Raqueta(10, (alto / 2) - (anchoPaleta / 2), anchoPaleta, altoPaleta, 1);
        raquetaP2 = new Raqueta((ancho - anchoPaleta - 10), (alto / 2) - (anchoPaleta / 2), anchoPaleta, altoPaleta, 2);
    }

    @Override
    public void paint(Graphics g) {
        imagen = createImage(getWidth(), getHeight());
        graficos = imagen.getGraphics();
        dibujar(graficos);
        g.drawImage(imagen, 0, 0, this);
    }

    public void dibujar(Graphics g) {
        raquetaP1.dibujar(g);
        raquetaP2.dibujar(g);
        pelota.dibujar(g);
    }

    public void Movimiento() {
        raquetaP1.movimiento();
        raquetaP2.movimiento();
        pelota.movimiento();
    }

    public void coliciones() {
        //Limites de raquetas en Y
        if (raquetaP1.y <= 0) {
            raquetaP1.y = 0;
        } else if (raquetaP1.y >= (alto - altoPaleta)) {
            raquetaP1.y = (alto - altoPaleta);
        }

        if (raquetaP2.y <= 0) {
            raquetaP2.y = 0;
        } else if (raquetaP2.y >= (alto - altoPaleta)) {
            raquetaP2.y = (alto - altoPaleta);
        }
        //Limites de Pelota en Y
        if (pelota.y <= 0) {
            pelota.setXeYDireccion(pelota.getxVelocidad(), -pelota.getyVelocidad());
        } else if (pelota.y >= (alto - DiametroPelota)) {
            pelota.setXeYDireccion(pelota.getxVelocidad(), -pelota.getyVelocidad());
        }
        //Raquetazo
        if (pelota.intersects(raquetaP1)) {
            pelota.setxVelocidad(Math.abs(pelota.getxVelocidad()));
            pelota.setxVelocidad(pelota.getxVelocidad() + 1);
            if (pelota.getyVelocidad() > 0) {
                pelota.setyVelocidad(pelota.getyVelocidad() + 1);
            } else {
                pelota.setyVelocidad(pelota.getyVelocidad() - 1);
            }
            pelota.setXeYDireccion(pelota.getxVelocidad(), pelota.getyVelocidad());

        } else if (pelota.intersects(raquetaP2)) {
            pelota.setxVelocidad(Math.abs(pelota.getxVelocidad()));
            pelota.setxVelocidad(pelota.getxVelocidad() + 1);
            if (pelota.getyVelocidad() > 0) {
                pelota.setyVelocidad(pelota.getyVelocidad() + 1);
            } else {
                pelota.setyVelocidad(pelota.getyVelocidad() - 1);
            }
            pelota.setXeYDireccion(-pelota.getxVelocidad(), pelota.getyVelocidad());
            System.out.println(pelota.getxVelocidad());
        }
    }

    @Override
    public void run() {
        //Bucle de juego
        long ultimaVez = System.nanoTime();
        double cantidadDeTicks = 60.0d;
        double ns = 1000000000 / cantidadDeTicks;
        double espera = 0;
        while (jugando) {
            long ahora = System.nanoTime();
            espera += (ahora - ultimaVez) / ns;
            ultimaVez = ahora;
            if (espera >= 1) {
                Movimiento();
                coliciones();
                this.repaint();
                espera--;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        raquetaP1.teclaPrecionada(e);
        raquetaP2.teclaPrecionada(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        raquetaP1.teclaSoltada(e);
        raquetaP2.teclaSoltada(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
