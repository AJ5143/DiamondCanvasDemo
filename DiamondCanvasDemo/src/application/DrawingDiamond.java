package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DrawingDiamond  extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			double midPointsCrownTop[] = new double[4];
			double midPointsGirdle1[] = new double[4];
			double midPointsGirdle2[] = new double[4];
			gc = canvas.getGraphicsContext2D();
			gc.setStroke(Color.CADETBLUE);
			gc.setLineWidth(2);
			gc.strokeLine(203,165,397,165);		// Top part (Line at top of crown) 
			midPointsCrownTop = getMidPointsForTopCrown(203,165,397,165);
			//gc.strokeLine(midPointsTop[0], midPointsTop[1], 1, 1); // for testing only
			//gc.strokeLine(midPointsTop[2], midPointsTop[3], 1, 1); // for testing only
			gc.strokeLine(140,245,460,245);		// Middle part (Horizontal Line which joins crown with pavilion)
			midPointsGirdle1 = getMidPointsForTopCrown(140,245,460,245);
			//gc.strokeLine(midPointsGirdle1[0],midPointsGirdle1[1] , 5, 5); // for testing only
			//gc.strokeLine(midPointsGirdle1[2],midPointsGirdle1[3] , 5, 5); // for testing only
			gc.strokeLine(140,260,460,260);		// Middle part (Second Horizontal Line which joins crown with pavilion)
			midPointsGirdle2 = getMidPointsForTopCrown(140,260,460,260);
			//gc.strokeLine(midPointsGirdle2[0],midPointsGirdle2[1] , 5, 5); // for testing only
			//gc.strokeLine(midPointsGirdle2[2],midPointsGirdle2[3] , 5, 5); // for testing only
			gc.strokeLine(midPointsCrownTop[0], midPointsCrownTop[1],midPointsGirdle1[0] , midPointsGirdle1[1]); //crown facets
			gc.strokeLine(midPointsCrownTop[2], midPointsCrownTop[3],midPointsGirdle1[2] , midPointsGirdle1[3]); //crown facets
			gc.strokeLine(midPointsGirdle1[0], midPointsGirdle1[1], midPointsGirdle2[0], midPointsGirdle2[1]);   //girdle facets
			gc.strokeLine(midPointsGirdle1[2], midPointsGirdle1[3], midPointsGirdle2[2], midPointsGirdle2[3]);	 //girdle facets
			
			gc.strokeLine(midPointsGirdle2[0], midPointsGirdle2[1], 300, 435); // pavilion facets
			gc.strokeLine(midPointsGirdle2[2], midPointsGirdle2[3], 300, 435); // pavilion facets
			//gc.beginPath();
			//gc.appendSVGPath("M299,166 L300,245,300,435"); // Middle part (Vertical Line which joins crown with pavilion)
			//gc.stroke();
			//gc.beginPath();
			//gc.appendSVGPath("M202,165.5 L140,244.5,300,435"); // Left part of whole Diamond
			//gc.stroke();
			gc.strokeLine(203, 165, 140, 245);	// Crown start line
			gc.strokeLine(397, 165, 460, 245);	// Crown last line
			gc.strokeLine(140, 245, 140, 260);	// Girdle start line
			gc.strokeLine(460, 245, 460, 260);	// Girdle last line
			
			gc.strokeLine(140, 260, 300, 435);	// Pavilion start line
			gc.strokeLine(460, 260, 300, 435);	// Pavilion last line
			//gc.beginPath();
			//gc.appendSVGPath("M300,435 L460,244.5,398,165.5"); // Right part of whole Diamond
			//gc.stroke();
			
			//gc.strokeLine(227.25, 165, 180, 245);	// Crown facet 1
			//gc.strokeLine(275.25, 165, 260, 245);	// Crown facet 2 and 3
			//gc.strokeLine(324.25, 165, 340, 245);	// Crown facet 4 and 5 
			//gc.strokeLine(372.25, 165, 420, 245);	// Crown facet 6
			
			
			//gc.strokeLine(180, 245, 300, 435);		// Pavilion facet 1 
			//gc.strokeLine(260, 245, 300, 435);		// Pavilion facet 2 and 3
			//gc.strokeLine(340, 245, 300, 435);		// Pavilion facet 4 and 5
			//gc.strokeLine(420, 245, 300, 435);		// Pavilion facet 6
			
			
		
			vbCenter.setPrefSize(480,480);
			vbCenter.getChildren().add(canvas);
			vbCenter.setAlignment(Pos.BASELINE_CENTER);
			
//			scene.setOnMouseClicked(e->{
//				vbCenter.setLayoutX(e.getSceneX());
//				vbCenter.setLayoutY(e.getSceneY());
//			});
			vbCenter.setOnMouseDragged(e->{
				vbCenter.setLayoutX(e.getSceneX() - 700);
				vbCenter.setLayoutY(e.getSceneY() - 240);
			});
			//root.getChildren().add(canvas);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	


	private double[] getMidPointsForTopCrown(double x1, double y1, double x2, double y2) {
		
		double topX1;
		double topY1;
		double topX2;
		double topY2;
		
		topX1 = (x1 + (0.33)*(x2-x1)); 
		topY1 = (y1 + (0.33)*(y2-y1));
		topX2 = (x1 + (0.67)*(x2-x1));
		topY2 = (y1 + (0.67)*(y2-y1));
		double points[] = {topX1,topY1,topX2,topY2};
		return points;
		
	}



	Canvas canvas = new Canvas(480,480);
	GraphicsContext gc; 
	VBox vbCenter = new VBox();
	//BorderPane root = new BorderPane();

	Scene scene = new Scene(vbCenter,1280,720);

public static void main(String[] args) {
	launch(args);
}
}
