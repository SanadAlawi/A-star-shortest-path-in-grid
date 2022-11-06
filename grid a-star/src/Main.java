import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	public final static int WIDTH = 720;
	public final static int HEIGHT = 500;
	public final static int GRID_SIZE = 50;

	Grid grid = new Grid(WIDTH, HEIGHT, GRID_SIZE);

	private Spot startSpot = null;
	private Spot targetSpot = null;
	
	private Label startSpotLabel = new Label("Start (Click)");
	private Label targetSpotLabel = new Label("Target (Click)");
	private Label obstacleSpotLabel = new Label("Obstacle (Drag the mouse)");
	private Label pathSpotLabel = new Label("Path");
	private Label visitedSpotLabel = new Label("Visited spots");
	
	private Label shortestPathSpotLabel = new Label("Space Key(Shortest path)");
	private Label resetStartAndTargetSpotLabel = new Label("Back Space Key(Reset start and target spot)");

	@Override
	public void start(Stage stage) throws Exception {
		Pane pane = new Pane();
		putSpotIntoGrid(pane);
		putInformationLabelsInPane(pane);
		pane.setBackground(new Background(new BackgroundFill(Colors.NORMAL_NODE_COLOR, null, null)));
		
		Scene scene = new Scene(pane, WIDTH, HEIGHT + 50);
		setUpSceneClickEvent(scene);
		
		stage.setScene(scene);
		stage.show();

	}


	private void putInformationLabelsInPane(Pane pane) {
		startSpotLabel.setTextFill(Colors.START_NODE_COLOR);
		targetSpotLabel.setTextFill(Colors.TARGET_NODE_COLOR);
		obstacleSpotLabel.setTextFill(Colors.OBSTACLE_NODE_COLOR);
		pathSpotLabel.setTextFill(Colors.PATH_NODE_COLOR);
		visitedSpotLabel.setTextFill(Colors.VISITED_NODE_COLOR);
		
		HBox hbSpots = new HBox(10);
		hbSpots.getChildren().addAll(startSpotLabel, targetSpotLabel, obstacleSpotLabel, pathSpotLabel, visitedSpotLabel);
		hbSpots.setLayoutY(HEIGHT+30);
		
		HBox hbKey = new HBox(10);
		hbKey.getChildren().addAll(shortestPathSpotLabel, resetStartAndTargetSpotLabel);
		hbKey.setLayoutY(HEIGHT);
		
		pane.getChildren().addAll(hbKey, hbSpots);
	}


	private void putSpotIntoGrid(Pane pane) {
		int spotWidth = WIDTH / GRID_SIZE;
		int spotHeight = HEIGHT / GRID_SIZE;

		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++) {
				grid.getGrid()[row][column] = new Spot(column * spotWidth, row * spotHeight, spotWidth, spotHeight);
				pane.getChildren().add(grid.getGrid()[row][column].getSpot());
			}
		}

	}

	private void setUpSceneClickEvent(Scene scene) {
		int spotWidth = WIDTH / GRID_SIZE;
		int spotHeight = HEIGHT / GRID_SIZE;
		
		// MAKE OBSTACLE SPOT WHEN DRAG THE MOUSE
		scene.setOnMouseDragged(e -> {
			int row = (int) e.getX() / spotWidth;
			int column = (int) e.getY() / spotHeight;
			makeObstacleSpot(GRID_SIZE, row, column);
		});
		
		// CHOOSE THE STARTING AND TARGET SPOT TO KNOW THE SHORTEST PATH BETWEEN THEM
		scene.setOnMouseClicked(e -> {
			int row = (int) e.getX() / spotWidth;
			int column = (int) e.getY() / spotHeight;
			setStartAndTargetSpots(row, column);	
		});
		
		// PRESS SPECIFIC KEY (SPACE OR BACK SPACE) TO FIND SHORTEST PATH OR RESET START AND TARGET SPOTS
		scene.setOnKeyPressed(e -> {
			// ENTER (SPACE KEY) TO CALL A* ALGORITHM TO FIND THE SHORTEST PATH BETWEEN TWO SPOT
			if(e.getCode() == KeyCode.SPACE) findShortestPath();
			
			// ENTER (BACK SOACE KEY) TO RESET START AND TARGET SPOT, TO CHOOSE ANOTHER ONES
			if(e.getCode() == KeyCode.BACK_SPACE) resetStartAndTargetSpot();
		});

	}
	
	
	private void resetStartAndTargetSpot() {
		startSpot.Free();
		targetSpot.Free();
		startSpot = null;
		targetSpot = null;
		grid.backToOriginalGrid();
		
	}


	private void findShortestPath() {
		if(startSpot != null && targetSpot != null) {
			grid.backToOriginalGrid();
			
			Spot path = grid.aStar(startSpot, targetSpot);
			if(path != null) {
				grid.showPath(path);
				startSpot.setColor(Colors.START_NODE_COLOR);
				targetSpot.setColor(Colors.TARGET_NODE_COLOR);
			}
			else System.out.println("No Path!!!");
		}else System.out.println("Chose start and target together!!!");
		
	}


	private void setStartAndTargetSpots(int row, int column) {
		Spot clickedSpot = grid.getGrid()[column][row];

		if (startSpot == null) {
			clickedSpot.setColor(Colors.START_NODE_COLOR);
			startSpot = clickedSpot;
		}else if(targetSpot == null && !clickedSpot.isStartSpot()){
			clickedSpot.setColor(Colors.TARGET_NODE_COLOR);
			targetSpot = clickedSpot;
		}
		
	}


	private void makeObstacleSpot(int gridSize, int row, int column) {
		
		if(row < 0 || row >= gridSize || column < 0 || column >= gridSize) return ;
		if(!grid.getGrid()[column][row].isStartSpot() && !grid.getGrid()[column][row].isTargetSpot()) {
			grid.getGrid()[column][row].setColor(Colors.OBSTACLE_NODE_COLOR);
			grid.noNeighbors(grid.getGrid()[row][column], row, column);
		}
		
	}


	public static void main(String[] args) {
		launch(args);
	}
}
