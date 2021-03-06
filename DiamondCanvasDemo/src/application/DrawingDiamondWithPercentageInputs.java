package application;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

// This time I will draw diamond with CH,GH in percentage (To Total Height) 
// This time I will use strokeLine method so NO PATHs, NO MOVETOs, NO LINETOs and NO WRITABLE IMAGEs!
// In order to change positioning, I will redraw with strokeLines at specific position
public class DrawingDiamondWithPercentageInputs extends Application {
	
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
	Button btnResetToCenter = new Button("Reset to Center");
	HBox hbButtons = new HBox(btnGenerateDiamond, btnResetToCenter);
	VBox vbDiamondProperties = new VBox(hbDiameter, hbCrownHeight, hbGirdleHeight, hbCrownAngle, hbPavilionAngle, hbButtons);
	
	
	
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
    		generateDiamond();	// done	
    		generatePositioning();	// done
        }
      
        private void generateLines() {
        	//Horizontal Line
    		gc.setStroke(Color.BLACK);
    		gc.strokeLine(0, scene.getHeight()/2, scene.getWidth(), scene.getHeight()/2);
    		//Vertical Line
    		gc.strokeLine(scene.getWidth()/2, 0, scene.getWidth()/2, scene.getHeight());
    	
		}

		private void generateDiamond() {
			gc.clearRect(0, 0, 5000, 5000);
			generateLines();
			hbDiameter.setSpacing(27);
			hbCrownHeight.setSpacing(3);
			hbGirdleHeight.setSpacing(5);
			hbCrownAngle.setSpacing(7);
			hbPavilionAngle.setSpacing(5);
			hbButtons.setSpacing(5);
			vbDiamondProperties.setSpacing(3);
			btnGenerateDiamond.setOnMouseClicked(e->{
				double diameter = Double.valueOf(txtDiameter.getText());
				double radius = diameter/2;
				double crownHeightInPercentage = Double.valueOf(txtCrownHeight.getText());
				double girdleHeightInPercentage = Double.valueOf(txtGirdleHeight.getText());
				double crownAngle = Double.valueOf(txtCrownAngle.getText());
				double pavilionAngle = Double.valueOf(txtPavilionAngle.getText());
				double crownAngleInRadions = Math.toRadians(crownAngle);
				double pavilionAngleInRadions = Math.toRadians(pavilionAngle);
				
				gc.clearRect(0, 0, 5000, 5000);
				generateLines();
				
				
				
				
				Point2D Center = new Point2D(scene.getWidth()/2, scene.getHeight()/2);
				Point2D CenterofDiameter = new Point2D(Center.getX(), Center.getY() + 8);
				Point2D pavilionEndPoint = new Point2D(Center.getX(), CenterofDiameter.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
				gc.setStroke(Color.CADETBLUE);
				double[] topLineMidPoints = new double[4];
				double[] firstMiddleLineMidPoints = new double[4];
				double[] secondMiddleLineMidPoints = new double[4];
				
				// Left radius
				gc.strokeLine(CenterofDiameter.getX(), CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY()); 
				// Right radius
				gc.strokeLine(CenterofDiameter.getX(), CenterofDiameter.getY(), CenterofDiameter.getX() + radius, CenterofDiameter.getY());
				// leftmost pavilion line
				gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX(), CenterofDiameter.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
				// rightmost pavilion line
				gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY(), CenterofDiameter.getX(), CenterofDiameter.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
				double pavilionHeightInPixels = pavilionEndPoint.getY() - CenterofDiameter.getY();
				System.out.println(pavilionHeightInPixels);
				double pavilionHeightInPercentage = 100 - crownHeightInPercentage - girdleHeightInPercentage;
				double totalHeightInPixels = (pavilionHeightInPixels * 100)/pavilionHeightInPercentage;
				double perPercentPixel = totalHeightInPixels/100;
				System.out.println(perPercentPixel);
				double crownHeightInPixels = crownHeightInPercentage * perPercentPixel;
				double girdleHeightInPixels = girdleHeightInPercentage * perPercentPixel;
				//double pavilionHeightInPixels2 = pavilionHeightInPercentage * perPercentPixel;
				System.out.println(crownHeightInPixels);
				System.out.println(totalHeightInPixels);
				//System.out.println(pavilionHeightInPixels2);
				System.out.println(pavilionHeightInPixels);
				System.out.println(girdleHeightInPixels);
				Point2D tableEndPoint = new Point2D(CenterofDiameter.getX(),pavilionEndPoint.getY() - totalHeightInPixels);
				Point2D tableLeft = new Point2D(tableEndPoint.getX() - (radius) + ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
				Point2D tableRight = new Point2D(tableEndPoint.getX() + (radius) - ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
				//gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), tableLeft.getX(), tableLeft.getY()); // Testing
				
				// leftmost girdleHeight
				gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				// girdle start
				gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY());
				
//			    System.out.println("Total Height: " + totalHeight);
//			    System.out.println("Crown Height: " + crownHeight);
//			    System.out.println("Girdle Height: " + girdleHeight);
//			    System.out.println("Pavilion Height: " + pavilionHeight);
//			    System.out.println("Crown Height in percentage: " + crownHeightPercentage);
//			    System.out.println("Girdle Height in percentage: " + girdleHeightPercentage);
//			    System.out.println("Pavilion Height in percentage: " + pavilionHeightPercentage);
//			    System.out.println(crownHeightPercentage + girdleHeightPercentage+ pavilionHeightPercentage);
//			    
			});
			
		}
		
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

		private void generatePositioning() {
			
			
		}

		@Override
        public boolean isResizable() {
            return true;
        }
 
        @Override
        public double prefWidth(double width) {
            return getWidth();
        }
 
        @Override
        public double prefHeight(double height) {
            return getHeight();
        }
        
    }
 
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
		hbDiameter.setSpacing(27);
		hbCrownHeight.setSpacing(3);
		hbGirdleHeight.setSpacing(5);
		hbCrownAngle.setSpacing(7);
		hbPavilionAngle.setSpacing(5);
		hbButtons.setSpacing(5);
		vbDiamondProperties.setSpacing(3);
		
		root.getChildren().add(canvas);
		root.setLeft(vbDiamondProperties);
		//root.setCenter(canvas);
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());
		scene.widthProperty().addListener(e->{
			canvas.draw();
			generateDiamondChanged();
		});
		scene.heightProperty().addListener(e->{
			canvas.draw();
			generateDiamondChanged();
		});
		
		btnResetToCenter.setOnMouseClicked(e->{
			generateDiamondChanged();
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	} catch(Exception e) {
		e.printStackTrace();
	}
		
}



	private void generateDiamondChanged() {
		
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}	

