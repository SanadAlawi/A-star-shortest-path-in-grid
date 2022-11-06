import java.util.LinkedList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Spot implements Comparable<Spot>{
	private int x;
	private int y;
	private int width;
	private int height;
	private Rectangle spot;
	private Color color;
	
	
	private int H = 0;
	private int G = 0;
	private int F = 0;
	private Spot parent;
	private LinkedList<Spot> neighbors;
	
	public Spot(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = Colors.NORMAL_NODE_COLOR;
		
		this.spot = new Rectangle(x, y, width, height);
		this.spot.setStroke(Color.BLACK);
		this.spot.setFill(this.color);
		this.neighbors = new LinkedList<Spot>();
		
		defaultValues();
	}

	
	public void defaultValues() {
		this.G = Integer.MAX_VALUE;
		this.H = Integer.MAX_VALUE;
		this.F = Integer.MAX_VALUE;
		
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Rectangle getSpot() {
		return spot;
	}

	public void setSpot(Rectangle spot) {
		this.spot = spot;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		this.spot.setFill(color);
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getF() {
		return F;
	}

	public void setF(int f) {
		F = f;
	}

	public Spot getParent() {
		return parent;
	}

	public void setParent(Spot parent) {
		this.parent = parent;
	}
	
	public boolean isStartSpot() {
		return color == Colors.START_NODE_COLOR;
	}
	
	public boolean isTargetSpot() {
		return color == Colors.TARGET_NODE_COLOR;
	}
	
	public boolean isNormalSpot() {
		return color == Colors.NORMAL_NODE_COLOR;
	}
	
	public boolean isObstacleSpot() {
		return color == Colors.OBSTACLE_NODE_COLOR;
	}
	
	public LinkedList<Spot> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(LinkedList<Spot> neighbors) {
		this.neighbors = neighbors;
	}
	

	public void calculateHeuristics(Spot target) {
		H = Math.abs((x - target.getX())) + Math.abs((y - target.getY()));
	}
	
	public void Free() {
		this.color = Colors.NORMAL_NODE_COLOR;
		spot.setFill(color);
	}
	

	@Override
	public String toString() {
		return "Spot [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", G=" + G + "]";
	}

	@Override
	public int compareTo(Spot o) {
		return this.F - o.getF(); 
	}

	public void visited() {
		this.color = Colors.VISITED_NODE_COLOR;
		spot.setFill(color);
	}

}
