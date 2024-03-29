package application;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
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
	Stage primaryStage = new Stage();
	volatile boolean exitFlagForThread = false;
	volatile boolean resetToCenterFlag = false;
	class ResizableCanvas extends Canvas{
		 
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
			//btnGenerateDiamond.setDisable(true);
						
			BooleanBinding bb = new BooleanBinding() {
				{
					super.bind(txtDiameter.textProperty(), txtCrownHeight.textProperty(), txtGirdleHeight.textProperty(), txtCrownAngle.textProperty(), txtPavilionAngle.textProperty());
				}
				@Override
				protected boolean computeValue() {
					
					return (txtDiameter.getText().isEmpty() || Double.valueOf(txtDiameter.getText()).isNaN() ||
							txtCrownHeight.getText().isEmpty() || Double.valueOf(txtCrownHeight.getText()).isNaN() ||
							txtGirdleHeight.getText().isEmpty() || Double.valueOf(txtGirdleHeight.getText()).isNaN() ||
							txtCrownAngle.getText().isEmpty() || Double.valueOf(txtCrownAngle.getText()).isNaN() ||
							txtPavilionAngle.getText().isEmpty() || Double.valueOf(txtPavilionAngle.getText()).isNaN());
				}
				
			};
			btnGenerateDiamond.disableProperty().bind(bb);
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
				
				
				final ChangeListener<Number> listener = new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
						gc.clearRect(0, 0, 5000, 5000);
						generateLines();
						//double diff = arg2.doubleValue() - arg1.doubleValue();
						
						
						Point2D Center = new Point2D(scene.getWidth()/2, scene.getHeight()/2);
						Point2D CenterofDiameter = new Point2D(Center.getX(), Center.getY());
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
						
						double pavilionHeightInPercentage = 100 - crownHeightInPercentage - girdleHeightInPercentage;
						double totalHeightInPixels = (pavilionHeightInPixels * 100)/pavilionHeightInPercentage;
						double perPercentPixel = totalHeightInPixels/100;
						
						double crownHeightInPixels = crownHeightInPercentage * perPercentPixel;
						double girdleHeightInPixels = girdleHeightInPercentage * perPercentPixel;
						//double pavilionHeightInPixels2 = pavilionHeightInPercentage * perPercentPixel;
						
						//System.out.println(pavilionHeightInPixels2);
						
						Point2D tableEndPoint = new Point2D(CenterofDiameter.getX(),pavilionEndPoint.getY() - totalHeightInPixels);
						Point2D tableLeft = new Point2D(tableEndPoint.getX() - (radius) + ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
						Point2D tableRight = new Point2D(tableEndPoint.getX() + (radius) - ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
						//gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), tableLeft.getX(), tableLeft.getY()); // Testing
						
						// leftmost girdleHeight
						gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
						
						// girdle start
						gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
						
						// girdle end
						gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY());
						
						// crown end
						gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, tableRight.getX(), tableRight.getY());
						
						// table
						gc.strokeLine(tableRight.getX(), tableRight.getY(), tableLeft.getX(), tableLeft.getY());
						
						// crown start
						gc.strokeLine(tableLeft.getX(), tableLeft.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
						
						// for crown facets
						topLineMidPoints = getTwoMidPointsForLine(tableLeft.getX(), tableLeft.getY(), tableRight.getX(), tableRight.getY());
						
						
						
						// for girdle facets
						firstMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
						secondMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() + radius, CenterofDiameter.getY());
						
						gc.strokeLine(topLineMidPoints[0], topLineMidPoints[1],firstMiddleLineMidPoints[0] , firstMiddleLineMidPoints[1]); //crown facets
						gc.strokeLine(topLineMidPoints[2], topLineMidPoints[3],firstMiddleLineMidPoints[2] , firstMiddleLineMidPoints[3]); //crown facets
						gc.strokeLine(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1], secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);   //girdle facets
						gc.strokeLine(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3], secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);	 //girdle facets
						
						gc.strokeLine(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
						gc.strokeLine(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
				
						
					}};
				scene.widthProperty().addListener(listener);
				scene.heightProperty().addListener(listener);
				
				
				
				
				Point2D CenterofDiameter = new Point2D(Center.getX(), Center.getY());
				Point2D pavilionEndPoint = new Point2D(Center.getX(), CenterofDiameter.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
				gc.setStroke(Color.CADETBLUE);
				//gc.setLineWidth(2);
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
				
				double pavilionHeightInPercentage = 100 - crownHeightInPercentage - girdleHeightInPercentage;
				double totalHeightInPixels = (pavilionHeightInPixels * 100)/pavilionHeightInPercentage;
				double perPercentPixel = totalHeightInPixels/100;
				
				double crownHeightInPixels = crownHeightInPercentage * perPercentPixel;
				double girdleHeightInPixels = girdleHeightInPercentage * perPercentPixel;
				System.out.println("Pavilion Height: " + pavilionHeightInPixels);
				System.out.println("Per Percent Pixel: " + perPercentPixel);
				System.out.println("Crown Height: " + crownHeightInPixels);
				System.out.println("Girdle Height: " + girdleHeightInPixels);
				System.out.println("Total Height: " + totalHeightInPixels);
				Point2D tableEndPoint = new Point2D(CenterofDiameter.getX(),pavilionEndPoint.getY() - totalHeightInPixels);
				Point2D tableLeft = new Point2D(tableEndPoint.getX() - (radius) + ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
				Point2D tableRight = new Point2D(tableEndPoint.getX() + (radius) - ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
				//gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), tableLeft.getX(), tableLeft.getY()); // Testing
				
				// leftmost girdleHeight
				gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				// girdle start
				gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				// girdle end
				gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY());
				
				// crown end
				gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, tableRight.getX(), tableRight.getY());
				
				// table
				gc.strokeLine(tableRight.getX(), tableRight.getY(), tableLeft.getX(), tableLeft.getY());
				
				// crown start
				gc.strokeLine(tableLeft.getX(), tableLeft.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				// for crown facets
				topLineMidPoints = getTwoMidPointsForLine(tableLeft.getX(), tableLeft.getY(), tableRight.getX(), tableRight.getY());
				
				
				
				// for girdle facets
				firstMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
				secondMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() + radius, CenterofDiameter.getY());
				
				gc.strokeLine(topLineMidPoints[0], topLineMidPoints[1],firstMiddleLineMidPoints[0] , firstMiddleLineMidPoints[1]); //crown facets
				gc.strokeLine(topLineMidPoints[2], topLineMidPoints[3],firstMiddleLineMidPoints[2] , firstMiddleLineMidPoints[3]); //crown facets
				gc.strokeLine(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1], secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);   //girdle facets
				gc.strokeLine(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3], secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);	 //girdle facets
				
				gc.strokeLine(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
				gc.strokeLine(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
				
				
				
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
			if(!btnGenerateDiamond.isDisabled()) {
				scene.setOnMouseClicked(e->{
					gc.clearRect(0, 0, 5000, 5000);
					generateLines();
					hbDiameter.setSpacing(27);
					hbCrownHeight.setSpacing(3);
					hbGirdleHeight.setSpacing(5);
					hbCrownAngle.setSpacing(7);
					hbPavilionAngle.setSpacing(5);
					hbButtons.setSpacing(5);
					vbDiamondProperties.setSpacing(3);
					
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
						
						Point2D Center = new Point2D(e.getSceneX(), e.getSceneY());
						Point2D oldSize = new Point2D(primaryStage.getWidth(), primaryStage.getHeight());
						

						BlockingQueue<Point2D> dimensionChangeQueue = new ArrayBlockingQueue<>(1);
				        ChangeListener<Number> dimensionChangeListener = (obs, oldValue, newValue) -> {
				            dimensionChangeQueue.clear();
				            dimensionChangeQueue.add(new Point2D(primaryStage.getWidth(), primaryStage.getHeight()));
				        };
				        
				        
				        primaryStage.widthProperty().addListener(dimensionChangeListener);
				        primaryStage.heightProperty().addListener(dimensionChangeListener);
				        
				        Thread processDimensionChangeThread = new Thread(() -> {
				        	resetToCenterFlag = false;
				            try {
				            	exitFlagForThread = true;
				            
				                while (exitFlagForThread == true & resetToCenterFlag == false) {
				                	
				                    System.out.println("Waiting for change in size");
				                    Point2D newSize = dimensionChangeQueue.take();
				                    
				                    System.out.printf("Detected change in size to [%.1f, %.1f]: processing%n", newSize.getX(), newSize.getY());
				                    process(Center, oldSize , newSize, primaryStage);
				                    System.out.println("Done processing");
				                    //exitFlagForThread = false;
				                }
				            } catch (InterruptedException letThreadExit) { }
				          
				        });
				        processDimensionChangeThread.setDaemon(true);
				        processDimensionChangeThread.start();
				        
				        
						Point2D CenterofDiameter = new Point2D(Center.getX(), Center.getY());
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
						//System.out.println(pavilionHeightInPixels);
						double pavilionHeightInPercentage = 100 - crownHeightInPercentage - girdleHeightInPercentage;
						double totalHeightInPixels = (pavilionHeightInPixels * 100)/pavilionHeightInPercentage;
						double perPercentPixel = totalHeightInPixels/100;
						//System.out.println(perPercentPixel);
						double crownHeightInPixels = crownHeightInPercentage * perPercentPixel;
						double girdleHeightInPixels = girdleHeightInPercentage * perPercentPixel;
						//double pavilionHeightInPixels2 = pavilionHeightInPercentage * perPercentPixel;
						//System.out.println(crownHeightInPixels);
						//System.out.println(totalHeightInPixels);
						//System.out.println(pavilionHeightInPixels2);
						//System.out.println(pavilionHeightInPixels);
						//System.out.println(girdleHeightInPixels);
						Point2D tableEndPoint = new Point2D(CenterofDiameter.getX(),pavilionEndPoint.getY() - totalHeightInPixels);
						Point2D tableLeft = new Point2D(tableEndPoint.getX() - (radius) + ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
						Point2D tableRight = new Point2D(tableEndPoint.getX() + (radius) - ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
						//gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), tableLeft.getX(), tableLeft.getY()); // Testing
						
						// leftmost girdleHeight
						gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
						
						// girdle start
						gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
						
						// girdle end
						gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY());
						
						// crown end
						gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, tableRight.getX(), tableRight.getY());
						
						// table
						gc.strokeLine(tableRight.getX(), tableRight.getY(), tableLeft.getX(), tableLeft.getY());
						
						// crown start
						gc.strokeLine(tableLeft.getX(), tableLeft.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
						
						// for crown facets
						topLineMidPoints = getTwoMidPointsForLine(tableLeft.getX(), tableLeft.getY(), tableRight.getX(), tableRight.getY());
						
						
						
						// for girdle facets
						firstMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
						secondMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() + radius, CenterofDiameter.getY());
						
						gc.strokeLine(topLineMidPoints[0], topLineMidPoints[1],firstMiddleLineMidPoints[0] , firstMiddleLineMidPoints[1]); //crown facets
						gc.strokeLine(topLineMidPoints[2], topLineMidPoints[3],firstMiddleLineMidPoints[2] , firstMiddleLineMidPoints[3]); //crown facets
						gc.strokeLine(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1], secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);   //girdle facets
						gc.strokeLine(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3], secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);	 //girdle facets
						
						gc.strokeLine(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
						gc.strokeLine(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
				});
				
			}

			}
			
		@SuppressWarnings("deprecation")
		public void process(Point2D center,Point2D oldSize ,Point2D newSize, Stage primaryStage) throws InterruptedException {
			gc.clearRect(0, 0, 5000, 5000);
			Thread.sleep(8);
	        //final String title = String.format("Width: %.0f Height: %.0f", center.getX(), center.getY());
	        Platform.runLater(() -> {
	        	gc.clearRect(0, 0, 5000, 5000);
				generateLines();
	        	double diameter = Double.valueOf(txtDiameter.getText());
				double radius = diameter/2;
				double crownHeightInPercentage = Double.valueOf(txtCrownHeight.getText());
				double girdleHeightInPercentage = Double.valueOf(txtGirdleHeight.getText());
				double crownAngle = Double.valueOf(txtCrownAngle.getText());
				double pavilionAngle = Double.valueOf(txtPavilionAngle.getText());
				double crownAngleInRadions = Math.toRadians(crownAngle);
				double pavilionAngleInRadions = Math.toRadians(pavilionAngle);
				
	        	
	        	
	        	Point2D CenterOfNewStage = new Point2D(newSize.getX()/2, newSize.getY()/2);
                Point2D CenterofDiameter = new Point2D( (CenterOfNewStage.getX() + (center.getX() - oldSize.getX()/2)), (CenterOfNewStage.getY() + (center.getY() - oldSize.getY()/2)));            
              
				Point2D pavilionEndPoint = new Point2D((newSize.getX()/2 + (center.getX() - oldSize.getX()/2)), CenterofDiameter.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
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
				//System.out.println(pavilionHeightInPixels);
				double pavilionHeightInPercentage = 100 - crownHeightInPercentage - girdleHeightInPercentage;
				double totalHeightInPixels = (pavilionHeightInPixels * 100)/pavilionHeightInPercentage;
				double perPercentPixel = totalHeightInPixels/100;
				//System.out.println(perPercentPixel);
				double crownHeightInPixels = crownHeightInPercentage * perPercentPixel;
				double girdleHeightInPixels = girdleHeightInPercentage * perPercentPixel;
				//double pavilionHeightInPixels2 = pavilionHeightInPercentage * perPercentPixel;
				//System.out.println(crownHeightInPixels);
				//System.out.println(totalHeightInPixels);
				//System.out.println(pavilionHeightInPixels2);
				//System.out.println(pavilionHeightInPixels);
				//System.out.println(girdleHeightInPixels);
				Point2D tableEndPoint = new Point2D(CenterofDiameter.getX(),pavilionEndPoint.getY() - totalHeightInPixels);
				Point2D tableLeft = new Point2D(tableEndPoint.getX() - (radius) + ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
				Point2D tableRight = new Point2D(tableEndPoint.getX() + (radius) - ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
				//gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), tableLeft.getX(), tableLeft.getY()); // Testing
				
				// leftmost girdleHeight
				gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				// girdle start
				gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				// girdle end
				gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY());
				
				// crown end
				gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, tableRight.getX(), tableRight.getY());
				
				// table
				gc.strokeLine(tableRight.getX(), tableRight.getY(), tableLeft.getX(), tableLeft.getY());
				
				// crown start
				gc.strokeLine(tableLeft.getX(), tableLeft.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
				
				// for crown facets
				topLineMidPoints = getTwoMidPointsForLine(tableLeft.getX(), tableLeft.getY(), tableRight.getX(), tableRight.getY());
				
				
				
				// for girdle facets
				firstMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
				secondMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() + radius, CenterofDiameter.getY());
				
				gc.strokeLine(topLineMidPoints[0], topLineMidPoints[1],firstMiddleLineMidPoints[0] , firstMiddleLineMidPoints[1]); //crown facets
				gc.strokeLine(topLineMidPoints[2], topLineMidPoints[3],firstMiddleLineMidPoints[2] , firstMiddleLineMidPoints[3]); //crown facets
				gc.strokeLine(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1], secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);   //girdle facets
				gc.strokeLine(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3], secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);	 //girdle facets
				
				gc.strokeLine(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
				gc.strokeLine(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets

	        	
	        	btnResetToCenter.setOnMouseClicked(m->{
	        		Platform.runLater(()-> {
	        			redrawAtCenter();
	        			resetToCenterFlag = true;
	        		});
	        	});
	        	//exitFlagForThread = false;
	           // primaryStage.setTitle(title);
	        });
	        //exitFlagForThread = false;
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

		public void redrawAtCenter() {
			gc.clearRect(0, 0, 5000, 5000);
			generateLines();
			hbDiameter.setSpacing(27);
			hbCrownHeight.setSpacing(3);
			hbGirdleHeight.setSpacing(5);
			hbCrownAngle.setSpacing(7);
			hbPavilionAngle.setSpacing(5);
			hbButtons.setSpacing(5);
			vbDiamondProperties.setSpacing(3);
			
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
			
			
			final ChangeListener<Number> listener = new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					gc.clearRect(0, 0, 5000, 5000);
					generateLines();
					//double diff = arg2.doubleValue() - arg1.doubleValue();
					
					
					Point2D Center = new Point2D(scene.getWidth()/2, scene.getHeight()/2);
					Point2D CenterofDiameter = new Point2D(Center.getX(), Center.getY());
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
					
					double pavilionHeightInPercentage = 100 - crownHeightInPercentage - girdleHeightInPercentage;
					double totalHeightInPixels = (pavilionHeightInPixels * 100)/pavilionHeightInPercentage;
					double perPercentPixel = totalHeightInPixels/100;
					
					double crownHeightInPixels = crownHeightInPercentage * perPercentPixel;
					double girdleHeightInPixels = girdleHeightInPercentage * perPercentPixel;
					//double pavilionHeightInPixels2 = pavilionHeightInPercentage * perPercentPixel;
					
					//System.out.println(pavilionHeightInPixels2);
					
					Point2D tableEndPoint = new Point2D(CenterofDiameter.getX(),pavilionEndPoint.getY() - totalHeightInPixels);
					Point2D tableLeft = new Point2D(tableEndPoint.getX() - (radius) + ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
					Point2D tableRight = new Point2D(tableEndPoint.getX() + (radius) - ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
					//gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), tableLeft.getX(), tableLeft.getY()); // Testing
					
					// leftmost girdleHeight
					gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
					
					// girdle start
					gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
					
					// girdle end
					gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY());
					
					// crown end
					gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, tableRight.getX(), tableRight.getY());
					
					// table
					gc.strokeLine(tableRight.getX(), tableRight.getY(), tableLeft.getX(), tableLeft.getY());
					
					// crown start
					gc.strokeLine(tableLeft.getX(), tableLeft.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
					
					// for crown facets
					topLineMidPoints = getTwoMidPointsForLine(tableLeft.getX(), tableLeft.getY(), tableRight.getX(), tableRight.getY());
					
					
					
					// for girdle facets
					firstMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
					secondMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() + radius, CenterofDiameter.getY());
					
					gc.strokeLine(topLineMidPoints[0], topLineMidPoints[1],firstMiddleLineMidPoints[0] , firstMiddleLineMidPoints[1]); //crown facets
					gc.strokeLine(topLineMidPoints[2], topLineMidPoints[3],firstMiddleLineMidPoints[2] , firstMiddleLineMidPoints[3]); //crown facets
					gc.strokeLine(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1], secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);   //girdle facets
					gc.strokeLine(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3], secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);	 //girdle facets
					
					gc.strokeLine(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
					gc.strokeLine(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
			
					
				}};
			scene.widthProperty().addListener(listener);
			scene.heightProperty().addListener(listener);

			
			
			
			
			Point2D CenterofDiameter = new Point2D(Center.getX(), Center.getY());
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
			
			double pavilionHeightInPercentage = 100 - crownHeightInPercentage - girdleHeightInPercentage;
			double totalHeightInPixels = (pavilionHeightInPixels * 100)/pavilionHeightInPercentage;
			double perPercentPixel = totalHeightInPixels/100;
			
			double crownHeightInPixels = crownHeightInPercentage * perPercentPixel;
			double girdleHeightInPixels = girdleHeightInPercentage * perPercentPixel;
			//double pavilionHeightInPixels2 = pavilionHeightInPercentage * perPercentPixel;
			
			//System.out.println(pavilionHeightInPixels2);
			
			Point2D tableEndPoint = new Point2D(CenterofDiameter.getX(),pavilionEndPoint.getY() - totalHeightInPixels);
			Point2D tableLeft = new Point2D(tableEndPoint.getX() - (radius) + ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
			Point2D tableRight = new Point2D(tableEndPoint.getX() + (radius) - ((crownHeightInPercentage) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), tableEndPoint.getY());
			//gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), tableLeft.getX(), tableLeft.getY()); // Testing
			
			// leftmost girdleHeight
			gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
			
			// girdle start
			gc.strokeLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
			
			// girdle end
			gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY());
			
			// crown end
			gc.strokeLine(CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels, tableRight.getX(), tableRight.getY());
			
			// table
			gc.strokeLine(tableRight.getX(), tableRight.getY(), tableLeft.getX(), tableLeft.getY());
			
			// crown start
			gc.strokeLine(tableLeft.getX(), tableLeft.getY(), CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels);
			
			// for crown facets
			topLineMidPoints = getTwoMidPointsForLine(tableLeft.getX(), tableLeft.getY(), tableRight.getX(), tableRight.getY());
			
			
			
			// for girdle facets
			firstMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY() - girdleHeightInPixels, CenterofDiameter.getX() + radius, CenterofDiameter.getY() - girdleHeightInPixels);
			secondMiddleLineMidPoints = getTwoMidPointsForLine(CenterofDiameter.getX() - radius, CenterofDiameter.getY(), CenterofDiameter.getX() + radius, CenterofDiameter.getY());
			
			gc.strokeLine(topLineMidPoints[0], topLineMidPoints[1],firstMiddleLineMidPoints[0] , firstMiddleLineMidPoints[1]); //crown facets
			gc.strokeLine(topLineMidPoints[2], topLineMidPoints[3],firstMiddleLineMidPoints[2] , firstMiddleLineMidPoints[3]); //crown facets
			gc.strokeLine(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1], secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);   //girdle facets
			gc.strokeLine(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3], secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);	 //girdle facets
			
			gc.strokeLine(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets
			gc.strokeLine(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3], pavilionEndPoint.getX(), pavilionEndPoint.getY()); // pavilion facets

			
		}

		
        
    }
 
	

	@Override
	public void start(Stage stage) throws Exception {
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
		//canvas.widthProperty().bind(primaryStage.widthProperty());
		//canvas.heightProperty().bind(primaryStage.heightProperty());
		
		
		
		scene.setOnMouseClicked(e->{
			canvas.generatePositioning();
		});
		BooleanBinding bb = new BooleanBinding() {
			{
				super.bind(txtDiameter.textProperty(), txtCrownHeight.textProperty(), txtGirdleHeight.textProperty(), txtCrownAngle.textProperty(), txtPavilionAngle.textProperty());
			}
			@Override
			protected boolean computeValue() {
				
				return (txtDiameter.getText().isEmpty() || Double.valueOf(txtDiameter.getText()).isNaN() ||
						txtCrownHeight.getText().isEmpty() || Double.valueOf(txtCrownHeight.getText()).isNaN() ||
						txtGirdleHeight.getText().isEmpty() || Double.valueOf(txtGirdleHeight.getText()).isNaN() ||
						txtCrownAngle.getText().isEmpty() || Double.valueOf(txtCrownAngle.getText()).isNaN() ||
						txtPavilionAngle.getText().isEmpty() || Double.valueOf(txtPavilionAngle.getText()).isNaN());
			}
			
		};
		btnResetToCenter.disableProperty().bind(bb);
		btnResetToCenter.setOnMouseClicked(e->{
			canvas.redrawAtCenter();
		});
		
//		scene.widthProperty().addListener(e->{
//			//canvas.draw();
//			canvas.generatePositioning();
//			//canvas.generateDiamondChanged();
//		});
//		scene.heightProperty().addListener(e->{
//			//canvas.draw();
//			canvas.generatePositioning();
//			//generateDiamondChanged();
//		});
		
//		primaryStage.resizableProperty().addListener(e->{
//			canvas.generatePositioning();
//		});
//		
		
        
		
		//primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	} catch(Exception e) {
		e.printStackTrace();
	}
		
}



	public static void main(String[] args) {
		launch(args);
	}
}	

