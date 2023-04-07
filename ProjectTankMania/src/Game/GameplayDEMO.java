package Game;


	import java.awt.*;
	import java.awt.event.ActionEvent;
	import java.awt.event.KeyAdapter;
	import java.awt.event.KeyEvent;
	import java.awt.geom.AffineTransform;
	import java.awt.geom.Line2D;
	import java.awt.geom.Line2D.Double;
	import java.awt.image.BufferedImage;
	import java.util.Arrays;
	import java.util.LinkedList;
	import java.util.Random;

	import javax.swing.*;

	import projectAccess.ResultMenu;

	public class GameplayDEMO extends JPanel implements Runnable {
	    private Tank[] tanks= new Tank[2];
	    private final double tankRatioAngle; //angle between height and width
	    private final double tankDistance; //distance between edges and center
	    private boolean ingame = true;
	    private Thread animator;
	    private final BufferedImage base;
	    private final ImageIcon map;
	    private final ImageIcon obstacleVip;
	    private Point[] obstacle = new Point[30];
	    static Action moveForwardT, moveBackwardT, rotateLeftT, rotateRightT, shootT;
	    static Action moveForwardT1, moveBackwardT1, rotateLeftT1, rotateRightT1, shootT1;
	    private String message1 = " won ";
	    public String[] userNames;
	    private static String winner;
	    private static int winnerScore;
	    private final LinkedList<Shot> shotList = new LinkedList<>();
	    Main obj;

	    public GameplayDEMO(Main main) { //sets up all tanks, map, images, new tread and Key actions

	        addKeyListener(new TAdapter());
	        setFocusable(true);

	        obj = main;
	        
	        ImageIcon baseIcon = new ImageIcon("images/NEW.png");
	        tankRatioAngle = Math.atan((double)baseIcon.getIconHeight()/baseIcon.getIconWidth());
	        tankDistance = Math.sqrt(Math.pow(baseIcon.getIconHeight(), 2)+Math.pow(baseIcon.getIconWidth(), 2))/2 - 1; //Pythagoras
	        
	        map = new ImageIcon("images/Map.png");
	        obstacleVip = new ImageIcon("images/obstackle.png");
	        base = new BufferedImage(baseIcon.getIconWidth(), baseIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
	        Graphics g = base.createGraphics();
	        // paint the Icon to the BufferedImage.
	        baseIcon.paintIcon(null, g, 0, 0);
	        g.dispose();

	        tanks[0]= new Tank(920, 920, 5, Math.PI, 4, 100);
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
	            
	            Graphics2D g2d = (Graphics2D) obj.create();
	            obstacleVip.paintIcon(this, g2d, (int)obstacle[i].getX(), (int)obstacle[i].getY());
	        }
	    }

	    public void MapCreate() { //makes a random map at the start
	        Random r = new Random();
	        for (int i = 0; i < obstacle.length; i++) {
	            obstacle[i] = new Point((r.nextInt(15) + 2) * 50, (r.nextInt(15) + 2) * 50);
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
	        obj.setColor(java.awt.Color.red);

	    }

	    private void drawShot(Graphics obj) { //draws all the shots
	        for (Shot shot : shotList) {
	            shot.tickChange();
	            obj.fillRect((int) shot.getX(), (int) shot.getY(), 3, 3);
	        }
	    }

	    public Line2D[] tankSides(Tank tank) { //returns the 4 sides of a specified thank 
	    	Point[] edges= {
	    			new Point((int)(Math.cos(tank.getRotation()+tankRatioAngle)*tankDistance + tank.getX()+ 15), 
	    					(int)(Math.sin(tank.getRotation()+tankRatioAngle)*tankDistance + tank.getY()+ 7.5)),
	    			
	    			new Point((int)(Math.cos(tank.getRotation()-tankRatioAngle)*tankDistance + tank.getX()+ 15), 
	    					(int)(Math.sin(tank.getRotation()-tankRatioAngle)*tankDistance + tank.getY()+ 7.5)),
	    			
	    			new Point((int)(Math.cos(tank.getRotation()+tankRatioAngle+Math.PI)*tankDistance + tank.getX()+ 15), 
	    					(int)(Math.sin(tank.getRotation()+tankRatioAngle+Math.PI)*tankDistance + tank.getY()+ 7.5)),
	    			
	    			new Point((int)(Math.cos(tank.getRotation()-tankRatioAngle+Math.PI)*tankDistance + tank.getX()+ 15), 
	    	        		(int)(Math.sin(tank.getRotation()-tankRatioAngle+Math.PI)*tankDistance + tank.getY()+ 7.5))};
	    	Line2D[] tSides = new Line2D[edges.length];
	        for(int i=0; i<edges.length; i++) {
	        	tSides[i] = new Line2D.Double(edges[i].getX(), edges[i].getY(), edges[Math.floorMod(i+1, 4)].getX(), edges[Math.floorMod(i+1, 4)].getY());
	        }
	        
	    	return tSides;
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
	            shotTankCollision(tanks[i]);
	            tanks[i].tickChange();
	            TankMapCollision(tanks[i]);
	            
	            TankTankCollision(tanks[i], tanks[(i==0)?(1):(0)]);
	            if (tanks[i].getHealth() == 0) {
	                ingame = false;
	                if(i==1)i=0;else i=1;
	                gameWon(i,g);
	                break;
	            }
	        }
	        g.dispose();
	    }

	    private void shotMapCollisions() { //destroys the bullets when they collide
	        boolean deleted = false;
	        largeLoop: 
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
	                    if(shotList.size()<=i)break largeLoop;
	                    deleted = true;
	                }
	            }
	            if (i > shotList.size() - 1)  break largeLoop;
	            if ((shotList.get(i).getX() > 960 || shotList.get(i).getY() > 955 || shotList.get(i).getY() < 5 || shotList.get(i).getX() < 5) && shotList.size()!=0) {
	                shotList.remove(i);
	                if(shotList.size()<=i) break largeLoop;
	                deleted = true;
	            }
	        }
	    }
	    
	    private void shotTankCollision(Tank tank) { //Collision between thank and shot
	        for (int i = 0; i < shotList.size(); i++) {
	            
	            Line2D[] shotLine = {
	            		new Line2D.Double(shotList.get(i).getPreviousX(), shotList.get(i).getPreviousY(), 
	            				shotList.get(i).getX(),shotList.get(i).getY()),
	            		new Line2D.Double(shotList.get(i).getPreviousX()+3, shotList.get(i).getPreviousY(), 
	            				shotList.get(i).getX()+3,shotList.get(i).getY()),
	            		new Line2D.Double(shotList.get(i).getPreviousX(), shotList.get(i).getPreviousY()+3, 
	                    		shotList.get(i).getX(),shotList.get(i).getY()+3),
	            		new Line2D.Double(shotList.get(i).getPreviousX()+3, shotList.get(i).getPreviousY()+3, 
	            				shotList.get(i).getX()+3,shotList.get(i).getY()+3)};
	            Line2D[] tSides = tankSides(tank);
	            Loop:
	            for (int j = 0; j < shotLine.length; j++) {
	                for (int k = 0; k < tSides.length; k++) {
	                	   if (tSides[k].intersectsLine(shotLine[j])) {
	                		   shotList.remove(i);
	                           tank.removeHealth(20);
	                           break Loop;
	                	   }
	                }
	            }
	        }
	    }
	    
	    private void TankMapCollision(Tank tank) { //stops the tank when colliding into an obstacle or the edge
	        Rectangle tankRec = new Rectangle((int) tank.getX(), (int) tank.getY(), 15, 15);
	        Line2D[] tSides= tankSides(tank);
	        
	        for (int i = 0; i < obstacle.length; i++) {
	            Rectangle component = new Rectangle((int)obstacle[i].getX(), (int)obstacle[i].getY(), 50, 50);
	            for (int j = 0; j < tSides.length; j++) {
	            	   if (tSides[j].intersects(component)) {
	            		   tank.returnPrevious();
	            	   }
	            }
	         
	        }
	        if (tank.getX() > 955||tank.getY() > 955||tank.getY() < 5||tank.getX() < 5) {
	            tank.returnPrevious();
	        }
	        
	      
	        

	    }

	    private void TankTankCollision(Tank t1, Tank t2) { //stops the tank when colliding into a tank
	    	Rectangle tank1 = new Rectangle((int) t1.getX(), (int) t1.getY(), 15, 15);
	    	Rectangle tank2 = new Rectangle((int) t2.getX(), (int) t2.getY(), 15, 15);
	    	
	    	Line2D[] t1Sides = tankSides(t1);
	    	Line2D[] t2Sides = tankSides(t2);
	    	
	        largeLoop: 
	        for(int i = 0; i<t1Sides.length; i++) {
	        	for(int j = 0; j<t2Sides.length; j++) {
	        		if(t1Sides[i].intersectsLine(t2Sides[j])) {
	        			t1.returnPrevious();
	        			break largeLoop;
	        		}
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
	        message1 =  userNames[userWonPlace] + message1 +  " health left:" + tanks[userWonPlace].getHealth();

	        g.drawString(message1, (this.getWidth() - fontMetrics.stringWidth(message1)) / 2, this.getWidth() / 2);
	        winner = userNames[userWonPlace];
	        winnerScore = tanks[userWonPlace].getHealth()/10;
	    }

	    private class TAdapter extends KeyAdapter { //mainly for key released
	        @Override

	        public void keyReleased(KeyEvent e) {
	            int key = e.getKeyCode();
	           //System.out.println(e.getKeyText(key));
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
	            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !ingame) {
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
	                shotList.add(new Shot(Math.cos(tank.getRotation())*22 + tank.getX()+ 15, Math.sin(tank.getRotation())*22 + tank.getY() + 7.5, 20, tank.getRotation()));
	            }
	        }
	    }

	    public void run() { // tread run
	        int animationDelay = 45;
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

