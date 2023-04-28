import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tank {

	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;

	TankClient tc = null;
	int step = 0;
	int x, y;
	int oldX, oldY;

	private static Random r = new Random();
	Direction[] dirs = Direction.values();
	int rn = r.nextInt(dirs.length);

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	boolean good = true;
	boolean live = true;

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	private boolean bL = false, bU = false, bR = false, bD = false;

	enum Direction {
		L, LU, U, RU, R, RD, D, LD, STOP
	};

	private Direction dir = Direction.STOP;

	private Direction ptDir = Direction.D;

	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Tank(int x, int y, TankClient tc) {
		this(x, y);
		this.tc = tc;
	}

	public Tank(int x, int y, boolean good, TankClient tc) {
		this(x, y);
		this.good = good;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLUE);
		}

		// g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);

		switch (ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y);
			break;
		case R:

			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT / 2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT);
			break;
		case STOP:
			break;
		}

		move();
	}

	void move() {
		oldX = x;
		oldY = y;
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		if (this.dir != Direction.STOP) {

			this.ptDir = this.dir;

			if (x < 0)
				x = 0;
			if (y < 25)
				y = 25;
			if (x + Tank.WIDTH > TankClient.GAME_WIDTH)
				x = TankClient.GAME_WIDTH - Tank.WIDTH;
			if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT)
				y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		}
		if (!good) {
			// Direction. values () converts this enumeration type to an array Direction []
			// dirs=Direction. values ();
			if (step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;

			if (r.nextInt(40) > 38)
				this.fire();
		}

	}

	public void KeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			bL = true;
			break;
		case KeyEvent.VK_W:
			bU = true;
			break;
		case KeyEvent.VK_D:
			bR = true;
			break;
		case KeyEvent.VK_S:
			bD = true;
			break;
		case KeyEvent.VK_J:
			fire();
			break;

		}
		locateDirection();

	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			bL = false;
			break;
		case KeyEvent.VK_W:
			bU = false;
			break;
		case KeyEvent.VK_D:
			bR = false;
			break;
		case KeyEvent.VK_S:
			bD = false;
			break;
		}
		locateDirection();
	}

	void locateDirection() {
		if (bL && !bU && !bR && !bD)
			dir = Direction.L;
		else if (bL && bU && !bR && !bD)
			dir = Direction.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Direction.U;
		else if (!bL && bU && bR && !bD)
			dir = Direction.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Direction.R;
		else if (!bL && !bU && bR && bD)
			dir = Direction.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Direction.D;
		else if (bL && !bU && !bR && bD)
			dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;

	}

	public Missile fire() {

		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.WIDTH / 2;
		Missile m = new Missile(x, y, ptDir, tc, this.good);
		System.out.println(ptDir);
		tc.missiles.add(m);
		return m;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean collidesWithWall(Wall w) {
		if (this.getRect().intersects(w.getRect()) && this.live) {
			this.stay();
			return true;
		}
		return false;
	}

	private void stay() {
		x = oldX;
		y = oldY;
	}

}
