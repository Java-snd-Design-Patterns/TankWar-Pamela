import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class TankClient extends Frame {

	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	int x = 50, y = 50;
	Blood bl = new Blood();
	Image offScreenImage = null;
	Tank myTank = new Tank(50, 50, true, this);
	Tank enemyTank = new Tank(100, 100, false, this);
	// Missile m ;
	ArrayList<Missile> missiles = new ArrayList<Missile>();
	ArrayList<Explode> explodes = new ArrayList<Explode>();
	ArrayList<Tank> tanks = new ArrayList<Tank>();
	Wall w = new Wall(200, 400, 200, 10, this);

	public void paint(Graphics g) {

		g.drawString("missiles count: " + missiles.size(), 10, 50);
		g.drawString("explodes count: " + explodes.size(), 10, 70);
		g.drawString("tanks count: " + tanks.size(), 10, 90);
		g.drawString("tank life:" + myTank.getLife(), 10, 110);

		if(bl.isLive()) {
			myTank.eat(bl);
			bl.draw(g);
		}
		w.draw(g);
		myTank.collidesWithTanks(tanks);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTank(tanks);
			m.hitTank(myTank);
			m.hitWall(w);
			if (!m.isLive()) {
				missiles.remove(m);
			} else {
				m.draw(g);
			}
		}

		for (int i = 0; i < explodes.size(); i++) {
			// System.out.println(explodes.size());
			Explode e = explodes.get(i);
			e.draw(g);
			// explodes.remove(e);
		}

		if (myTank.isLive()) {
			myTank.draw(g);
		}
		myTank.collidesWithWall(w);
		for (int i = 0; i < tanks.size(); i++) {
			Tank tk = tanks.get(i);
			if (tanks.get(i).isLive()) {
				tk.collidesWithWall(w);
				tk.collidesWithTanks(tanks);
				tanks.get(i).draw(g);
			} else {
				tanks.remove(tk);
			}
		}
//		if(m != null) 
//			m.draw(g); 
//		for (int i = 0; i < missiles.size(); i++) {
//			Missile m = missiles.get(i);
//			m.draw(g);
//		}

	}

	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		print(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void launchFrame() {
		this.setLocation(300, 50);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("Tank War");
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);

		this.addKeyListener(new KeyMonitor());
		setVisible(true);

		for (int i = 0; i < 10; i++) {
			tanks.add(new Tank(50 + 40 * (i + 1), 50, false, this));
		}
		new Thread(new PaintThread()).start();

	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();

	}

	private class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

	}

}
