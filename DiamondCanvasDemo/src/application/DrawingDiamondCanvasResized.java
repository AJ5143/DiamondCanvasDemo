package application;


import com.sun.javafx.geom.Line2D;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.stage.Stage;

public class DrawingDiamondCanvasResized extends Application {

	Label lblDiameter = new Label("Enter Diameter: ");
	Label lblCrownHeight = new Label("Enter Crown Height: ");
	Label lblGirdleHeight = new Label("Enter Girdle Height: ");
	Label lblCrownAngle = new Label("Enter Crown Angle: ");
	Label lblPavilionAngle = new Label("Enter Pavilion Angle:");
	TextField txtDiameter = new TextField();
	TextField txtCrownHeight = new TextField();
	TextField txtGirdleHeight = new TextField();
	TextField txtCrownAngle = new TextField();
	TextField txtPavilionAngle = new TextField();
	HBox hbDiameter = new HBox(lblDiameter,txtDiameter);
	HBox hbCrownHeight = new HBox(lblCrownHeight,txtCrownHeight);
	HBox hbGirdleHeight = new HBox(lblGirdleHeight,txtGirdleHeight);
	HBox hbCrownAngle = new HBox(lblCrownAngle,txtCrownAngle);
	HBox hbPavilionAngle = new HBox(lblPavilionAngle,txtPavilionAngle);
	Button btnGenerateDiamond = new Button("Generate");
	VBox vbDiamondProperties = new VBox(hbDiameter, hbCrownHeight, hbGirdleHeight, hbCrownAngle, hbPavilionAngle, btnGenerateDiamond);
	
	
	BorderPane root = new BorderPane();
	Scene scene = new Scene(root,1280,720);
	ResizableCanvas canvas = new ResizableCanvas();
	GraphicsContext gc = canvas.getGraphicsContext2D();

	

	class ResizableCanvas extends Canvas {
		 
        public ResizableCanvas() {
            // Redraw canvas when size changes.
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
            
        }
 
        private void draw() {
            double width = getWidth();
            double height = getHeight();
 
            //GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);
            
			generateLines();	// done
    		generateDiamond();
    		generatePositioning();
        }
 
        @Override
        public boolean isResizable() {
            return true;
        }
 
        @Override
        public double prefWidth(double height) {
            return getWidth();
        }
 
        @Override
        public double prefHeight(double width) {
            return getHeight();
        }
    }
 
 
	
	
	
	public void start(Stage primaryStage) {
		try {
			root.getChildren().add(canvas);
			root.setLeft(vbDiamondProperties);
			//root.setCenter(canvas);
			canvas.widthProperty().bind(root.widthProperty());
			canvas.heightProperty().bind(root.heightProperty());
			primaryStage.setScene(scene);
			//primaryStage.sizeToScene();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void generatePositioning() {
		
		
	}



	private void generateLines() {
		//Point2D Center = new Point2D(scene.getWidth()/2, scene.getHeight()/2);
		//Horizontal Line
		gc.setStroke(Color.BLACK);
		gc.strokeLine(0, scene.getHeight()/2, scene.getWidth(), scene.getHeight()/2);
		//Vertical Line
		gc.strokeLine(scene.getWidth()/2, 0, scene.getWidth()/2, scene.getHeight());
	
	}



	@SuppressWarnings("unused")
	private void generateDiamond() {

		hbDiameter.setSpacing(27);
		hbCrownHeight.setSpacing(3);
		hbGirdleHeight.setSpacing(5);
		hbCrownAngle.setSpacing(7);
		hbPavilionAngle.setSpacing(5);
		//root.setLeft(vbDiamondProperties);
		vbDiamondProperties.setSpacing(3);
		
		btnGenerateDiamond.setOnMouseClicked(e->{
			double diameter = Double.valueOf(txtDiameter.getText());
			double radius = diameter/2;
			Point2D Center = new Point2D(scene.getWidth()/2, scene.getHeight()/2);
			System.out.println(Center.getX());
			System.out.println(Center.getY());
			System.out.println(diameter);
			System.out.println(radius);
			gc.setStroke(Color.CADETBLUE);
//			MoveTo mCenter = new MoveTo();
//			//LineTo lCenter = new LineTo();
//			mCenter.setX(Center.getX());
//			mCenter.setY(Center.getY());
//			LineTo firstMidLineFirstHalf = new LineTo(200,200);
//			
			Point2D firstMiddleLinePoint1 = new Point2D(Center.getX() - radius, Center.getY());
			System.out.println(firstMiddleLinePoint1.getX());
			System.out.println(firstMiddleLinePoint1.getY());
//			Line test = new Line(); 
//			test.setStartX(firstMiddleLinePoint1.getX());
//			test.setEndX(Center.getX());
//			test.setEndX(firstMiddleLinePoint1.getY());
//			test.setEndY(firstMiddleLinePoint1.getY());
			
			gc.moveTo(Center.getX(), Center.getY());
			
			//gc.lineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY());
			gc.lineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY() + 20);
			gc.lineTo(500, 500);
			gc.lineTo(120, 230);
			
		});
			
	}
	@SuppressWarnings("unused")
	private double[] getTwoMidPointsForLine(double x1, double y1, double x2, double y2) {
		
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


	public static void main(String[] args) {
		launch(args);
	}
}

