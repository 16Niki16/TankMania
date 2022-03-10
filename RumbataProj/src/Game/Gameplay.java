package Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;

import projectAccess.ResultMenu;

public class Gameplay extends JPanel implements Runnable {
    private Tank[] tanks= new Tank[2];
    private boolean ingame = true;
    private Thread animator;
    private final BufferedImage base;
    private final ImageIcon map;
    private final ImageIcon obstacleVip;
    private Character[] obstacle = new Character[30];
    static Action moveForwardT, moveBackwardT, rotateLeftT, rotateRightT, shootT;
    static Action moveForwardT1, moveBackwardT1, rotateLeftT1, rotateRightT1, shootT1;
    private String message1 = " won ";
    public String[] userNames;
    private static String winner;
    private static int winnerScore;
    private final LinkedList<Shot> shotList = new LinkedList<>();
    Main obj;

    public Gameplay(Main main) { //sets up all thanks, map, images, new tread and Key actions

        addKeyListener(new TAdapter());
        setFocusable(true);

        obj = main;
        ImageIcon baseIcon = new ImageIcon("images/tank.png");
        map = new ImageIcon("images/Map.png");
        obstacleVip = new ImageIcon("images/obstackle.png");
        base = new BufferedImage(baseIcon.getIconWidth(), baseIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = base.createGraphics();
        // paint the Icon to the BufferedImage.
        baseIcon.paintIcon(null, g, 0, 0);
        g.dispose();

        tanks[0]= new Tank(920, 920, 5, Math.toRadians(180d), 4, 100);
        tanks[1]= new Tank(20, 20, 6, 0.0, 4, 100);

        Gameplay.moveForwardT = new moveForward(tanks[0]);
        Gameplay.moveBackwardT = new moveBackward(tanks[0]);
        Gameplay.rotateLeftT = new rotateLeft(tanks[0]);
        Gameplay.rotateRightT = new rotateRight(tanks[0]);
        Gameplay.shootT = new shot(tanks[0]);
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "moveForwardT");
        this.getActionMap().put("moveForwardT", moveForwardT);
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "moveBackwardT");
        this.getActionMap().put("moveBackwardT", moveBackwardT);
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "rotateLeftT");
        this.getActionMap().put("rotateLeftT", rotateLeftT);
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rotateRightT");
        this.getActionMap().put("rotateRightT", rotateRightT);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "shootT");
        this.getActionMap().put("shootT", shootT);

        Gameplay.moveForwardT1 = new moveForward(tanks[1]);
        Gameplay.moveBackwardT1 = new moveBackward(tanks[1]);
        Gameplay.rotateLeftT1 = new rotateLeft(tanks[1]);
        Gameplay.rotateRightT1 = new rotateRight(tanks[1]);
        Gameplay.shootT1 = new shot(tanks[1]);
        this.getInputMap().put(KeyStroke.getKeyStroke('w'), "moveForwardT1");
        this.getActionMap().put("moveForwardT1", moveForwardT1);
        this.getInputMap().put(KeyStroke.getKeyStroke('s'), "moveBackwardT1");
        this.getActionMap().put("moveBackwardT1", moveBackwardT1);
        this.getInputMap().put(KeyStroke.getKeyStroke('a'), "rotateLeftT1");
        this.getActionMap().put("rotateLeftT1", rotateLeftT1);
        this.getInputMap().put(KeyStroke.getKeyStroke('d'), "rotateRightT1");
        this.getActionMap().put("rotateRightT1", rotateRightT1);
        this.getInputMap().put(KeyStroke.getKeyStroke(' '), "shootT1");
        this.getActionMap().put("shootT1", shootT1);

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
        setDoubleBuffered(true);
    }

    private void mapDraw(Graphics obj) { //draws the map every thick
        for (int i = 0; i < obstacle.length; i++) {
            //obj.fillRect((int)obstacle[i].x, (int)obstacle[i].y, 50, 50);
            Graphics2D g2d = (Graphics2D) obj.create();
            obstacleVip.paintIcon(this, g2d, (int)obstacle[i].getX(), (int)obstacle[i].getY());
        }
    }

    public void MapCreate() { //makes a random map at the start
        Random r = new Random();
        for (int i = 0; i < obstacle.length; i++) {
            obstacle[i]= new Character((r.nextInt(15) + 2) * 50, (r.nextInt(15) + 2) * 50, 0);
        }
    }

    private void Border(Graphics obj) { //draws the border every thick
        obj.setColor(Color.blue);
        obj.fillRect(0, 0, 3, 992);
        obj.fillRect(0, 0, 992, 3);
        obj.fillRect(982, 0, 3, 965);
        obj.fillRect(0, 958, 992, 3);
    }

    private void Tank(Graphics obj, Tank tank) { //draws given tank
        Graphics2D g2d = (Graphics2D) obj.create();
        AffineTransform at = AffineTransform.getTranslateInstance(tank.getX(), tank.getY());
        at.rotate(tank.getRotation(), base.getWidth() / 2d, base.getHeight() / 2d);
        g2d.setTransform(at);
        g2d.drawImage(base, 0, 0, this);
        g2d.dispose();
        // obj.setColor(java.awt.Color.red);
        // obj.fillRect((int)t.x, (int)t.y, 3,3);
    }

    private void drawShot(Graphics obj) { //draws all the shots
        for (Shot shot : shotList) {
            shot.tickChange();
            obj.fillRect((int) shot.getX(), (int) shot.getY(), 3, 3);
        }
    }

    public void paint(Graphics g) { //main paint method
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        map.paintIcon(this, g2d, 0, 0);
        mapDraw(g);
        Border(g);
        drawShot(g);
        shotMapCollisions();
        for(int i=0; i<tanks.length;i++){
            Tank(g, tanks[i]);
            shotTank(tanks[i]);
            tanks[i].tickChange();
            TankCollision(tanks[i]);
            if (tanks[i].getHealth() == 0) {
                ingame = false;
                if(i==1)i=0;else i=1;
                gameWon(i,g);
            }
        }
        g.dispose();
    }

    private void shotMapCollisions() { //destroys the bullets when they collide
        boolean deleted = false;
        for (int i = 0; i < shotList.size(); i++) {
            if (deleted) {
                i--;
                deleted = false;
            }
            Rectangle shot = new Rectangle((int) shotList.get(i).getX(), (int) shotList.get(i).getY(), 3, 3);
            for (int j = 0; j < obstacle.length; j++) {
                Rectangle component = new Rectangle((int)obstacle[j].getX(), (int)obstacle[j].getY(), 50, 50);
                if (shot.intersects(component) && shotList.size()!=0) {
                    shotList.remove(i);
                    deleted = true;
                }
            }
            if (i > shotList.size() - 1)  break;
            if ((shotList.get(i).getX() > 960 || shotList.get(i).getY() > 955 || shotList.get(i).getY() < 5 || shotList.get(i).getX() < 5) && shotList.size()!=0) {
                shotList.remove(i);
                deleted = true;
            }
        }
    }

    private void TankCollision(Tank tank) { //stops the tank when coliding into an obstacle or the edge
        Rectangle tankkk = new Rectangle((int) tank.getX(), (int) tank.getY(), 15, 15);
        for (int i = 0; i < obstacle.length; i++) {
            Rectangle component = new Rectangle((int)obstacle[i].getX(), (int)obstacle[i].getY(), 50, 50);
            if (tankkk.intersects(component)) {
                tank.stop();
                tank.returnPrevious();
            }
        }
        if (tank.getX() > 960||tank.getY() > 955||tank.getY() < 5||tank.getX() < 5) {
            tank.returnPrevious();
        }
    }

    private void shotTank(Tank tank) { //colission between thank and shot
        for (int i = 0; i < shotList.size(); i++) {
            Rectangle shot = new Rectangle((int) shotList.get(i).getX(), (int) shotList.get(i).getY(), 3, 3);
            Rectangle component = new Rectangle((int) tank.getX(), (int) tank.getY(), 15, 15);
            if (shot.intersects(component)) {
                shotList.remove(i);
                tank.removeHealth(20);
            }
        }
    }

    private void gameWon(int userWonPlace, Graphics g) { //draws blue screen with the score

        g.setColor(Color.blue);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, this.getWidth() / 2 - 30, this.getHeight() - 100, 50);
        g.setColor(Color.green);
        g.drawRect(50, this.getWidth() / 2 - 30, this.getHeight() - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        int health;
        message1 =  userNames[userWonPlace] + message1 +  " health left:" + tanks[userWonPlace].getHealth();

        g.drawString(message1, (this.getWidth() - fontMetrics.stringWidth(message1)) / 2, this.getWidth() / 2);
        winner = userNames[userWonPlace];
        winnerScore = tanks[userWonPlace].getHealth()/10;
    }

    private class TAdapter extends KeyAdapter { //mainly for key released
        @Override

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
           System.out.println(e.getKeyText(key));
            switch (key) {
                case 37 : tanks[0].rotateLeft = false; break;
                case 38 : tanks[0].moveForward = false; break;
                case 39 : tanks[0].rotateRight = false; break;
                case 40 : tanks[0].moveBackward = false; break;

                case 65 : tanks[1].rotateLeft = false; break;
                case 68 : tanks[1].rotateRight = false; break;
                case 83 : tanks[1].moveBackward = false; break;
                case 87 : tanks[1].moveForward = false; break;
            }


        }
        @Override
        public void keyTyped(KeyEvent e) {

        }
        @Override public void keyPressed(KeyEvent e) {//exits the game with enter when it finishes
            if (e.getKeyCode() == KeyEvent.VK_ENTER && !ingame) {
            new Thread() {
                public void run() {
                    String obj;
                    if(userNames[1].equals(winner)) {
                        userNames[1] = userNames[0];
                        userNames[0] = winner;
                    }
                    ResultMenu.main(new String[] {userNames[0], userNames[1]}, winnerScore);
                }
            }.start();
            obj.dispose();
        }}
    }

    private class moveTank extends AbstractAction { //class to make the constructor need a "Tank" class in keyStroke classes
        Tank tank;
        public moveTank(Tank t) {
            tank = t;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    private class moveForward extends moveTank {
        public moveForward(Tank t) {
            super(t);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!tank.moveBackward)
                tank.moveForward = true;
        }
    }

    private class moveBackward extends moveTank {
        public moveBackward(Tank t) {
            super(t);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!tank.moveForward)
                tank.moveBackward = true;
        }
    }

    private class rotateLeft extends moveTank {
        public rotateLeft(Tank t) {
            super(t);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!tank.rotateRight)
                tank.rotateLeft = true;
        }
    }

    private class rotateRight extends moveTank {
        public rotateRight(Tank t) {
            super(t);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!tank.rotateLeft)
                tank.rotateRight = true;
        }
    }

    private class shot extends moveTank {
        public shot(Tank t) {
            super(t);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tank.shoot()) {
                shotList.add(new Shot(tank.getX() + 10, tank.getY() + 10, 20, tank.getRotation()));
            }
        }
    }

    public void run() { // tread run
        int animationDelay = 50;
        long time = System.currentTimeMillis();
        MapCreate();
        while (true) {
            if (!ingame) break;
            repaint();
            try {
                time += animationDelay;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
