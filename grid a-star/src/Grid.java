import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Grid {
	
	private Scene scene;
	private Pane pane;
	
	private Spot[][] grid;
	private int spotWidth;
	private int spotHeight;
	
	
	public Grid(int width, int height, int gridSize) {
		super();
		this.grid = new Spot[gridSize][gridSize];
		this.spotWidth = width / gridSize;
		this.spotHeight = height / gridSize;
	}



	public void setNeighbors() {
		for (int row = 0; row < grid.length; row++) {
			for (int column = 0; column < grid[row].length; column++) {
				putSpotNeighbors(grid[row][column], row, column);
			}
		}
		
	}

	
	private void putSpotNeighbors(Spot spot, int row, int column) {
		int gridSize = grid.length;
		spot.getNeighbors().clear();
		if(row + 1 < gridSize && !grid[row + 1][column].isObstacleSpot()) spot.getNeighbors().add(grid[row + 1][column]);
		if(row - 1 >= 0 && !grid[row - 1][column].isObstacleSpot()) spot.getNeighbors().add(grid[row - 1][column]);
		if(column - 1 >= 0 && !grid[row][column - 1].isObstacleSpot()) spot.getNeighbors().add(grid[row][column - 1]);
		if(column + 1 < gridSize && !grid[row][column + 1].isObstacleSpot()) spot.getNeighbors().add(grid[row][column + 1]);

	}



	public Spot[][] getGrid() {
		return grid;
	}


	public void setGrid(Spot[][] grid) {
		this.grid = grid;
	}



	public Spot aStar(Spot startSpot, Spot targetSpot) {
		PriorityQueue<Spot> queue = new PriorityQueue<Spot>();
		List<Spot> visitedSpot = new LinkedList<Spot>();
		
		startSpot.calculateHeuristics(targetSpot);
		startSpot.setG(0);
		startSpot.setF(startSpot.getG() + startSpot.getH());
		queue.add(startSpot);
		
		targetSpot.setH(0);
		targetSpot.setF(0);
		
		while(!queue.isEmpty()) {
			Spot currentSpot = queue.poll();
			
			if(currentSpot.equals(targetSpot)) return currentSpot;
			
						
			for(Spot neighbor: currentSpot.getNeighbors()) {
				
				int currentDistanceFromStartSpot = neighbor.getG() + 1;
				neighbor.calculateHeuristics(targetSpot);
				neighbor.visited();
				
				if(!queue.contains(neighbor) && !visitedSpot.contains(neighbor)) {
					neighbor.setParent(currentSpot);
					neighbor.setG(currentDistanceFromStartSpot);
					neighbor.setF(currentDistanceFromStartSpot + neighbor.getH());
					queue.add(neighbor);
				}else {
					if(neighbor.getG() > currentDistanceFromStartSpot) {
						neighbor.setParent(currentSpot);
						neighbor.calculateHeuristics(targetSpot);
						neighbor.setG(currentDistanceFromStartSpot);
						neighbor.setF(currentDistanceFromStartSpot + neighbor.getH());
						if(visitedSpot.contains(neighbor)) {
							visitedSpot.remove(neighbor);
							queue.add(neighbor);
							neighbor.Free();
						}
					}
				}
				
			}
			visitedSpot.add(currentSpot);
		}
		
		return null;
	}



	public void showPath(Spot path) {
		while(path != null) {
			System.out.println(path);
			path.setColor(Colors.PATH_NODE_COLOR);
			path = path.getParent();
		}
	}



	public void noNeighbors(Spot spot, int row, int column) {
		int gridSize = grid.length;
		
		if(row + 1 < gridSize && !grid[row + 1][column].isObstacleSpot()) {
			grid[row + 1][column].getNeighbors().remove(spot);
		}
		if(row - 1 >= 0 && !grid[row - 1][column].isObstacleSpot()) {
			grid[row - 1][column].getNeighbors().remove(spot);
		}
		if(column - 1 >= 0 && !grid[row][column - 1].isObstacleSpot()) {
			grid[row][column - 1].getNeighbors().remove(spot);
		}
		if(column + 1 < gridSize && !grid[row][column + 1].isObstacleSpot()) {
			grid[row][column + 1].getNeighbors().remove(spot);
		}
//		for(Spot neighbor: spot.getNeighbors()) {
//			neighbor.getNeighbors().remove(spot);
//		}
//		spot.getNeighbors().clear();
	}



	public void backToOriginalGrid() {
		
		for(int row = 0 ; row < grid.length ; row++) {
			for(int column = 0 ; column < grid.length ; column++) {
				if(!grid[row][column].isStartSpot() && !grid[row][column].isTargetSpot() && !grid[row][column].isObstacleSpot()) {
					grid[row][column].Free();
					grid[row][column].defaultValues();
				}
				
			}
		}
		setNeighbors();
	}
	

}
