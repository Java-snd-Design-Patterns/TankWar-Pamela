import java.awt.*;
import java.util.ArrayList;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	private int x, y;
	Tank.Direction dir;
	TankClient tc;
	boolean good;
	private boolean live = true;

	public static int getXspeed() {
		return XSPEED;
	}

	public static int getYspeed() {
		return YSPEED;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Tank.Direction getDir() {
		return dir;
	}

	public TankClient getTc() {
		return tc;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, Tank.Direction dir, TankClient tc) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.tc = tc;
	}

	public Missile(int x, int y, Tank.Direction dir, TankClient tc, boolean good) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.tc = tc;
		this.good = good;
	}

	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}

		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
	}

	private void move() {
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
		}
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y >

				TankClient.GAME_HEIGHT) {
			live = false;
			tc.missiles.remove(this);
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t) {
		if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			t.setLive(false);
			this.live = false;

			Explode e = new Explode(t.getX(), t.getY(), tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean hitTank(ArrayList<Tank> tanks) {
		// TODO Auto-generated method stub
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;

	}

	public boolean hitWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
}
