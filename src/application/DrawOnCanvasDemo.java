package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class DrawOnCanvasDemo extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			gc = canvas.getGraphicsContext2D();
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
				scene.setOnMousePressed(e -> {
				gc.beginPath();
				gc.lineTo(e.getSceneX(), e.getSceneY());
				gc.stroke();
			});
			
			scene.setOnMouseDragged(e->{
				gc.lineTo(e.getSceneX(), e.getSceneY());
				gc.stroke();
			});
			
			root.getChildren().add(canvas);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	Canvas canvas = new Canvas(600,600);
	GraphicsContext gc; 
	
	BorderPane root = new BorderPane();
	
	Scene scene = new Scene(root,600,600);
	
	public static void main(String[] args) {
		launch(args);
	}
}
