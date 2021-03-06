package application;


import java.awt.image.BufferedImage;
import java.time.LocalTime;

import com.sun.javafx.geom.Line2D;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    		//generateDiamondChanged();
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
 
 
	
	
	
	public void start(Stage primaryStage) {
		try {
			root.getChildren().add(canvas);
			root.setLeft(vbDiamondProperties);
			//root.setCenter(canvas);
			canvas.widthProperty().bind(root.widthProperty());
			canvas.heightProperty().bind(root.heightProperty());
			scene.widthProperty().addListener(e->{
				//canvas.draw();
				generatePositioning();
				generateDiamondChanged();
			});
			scene.heightProperty().addListener(e->{
				//canvas.draw();
				generatePositioning();
				generateDiamondChanged();
			});
			
			btnResetToCenter.setOnMouseClicked(e->{
				generateDiamondChanged();
			});
			//canvas.widthProperty().bindBidirectional(root.prefWidthProperty());
			//canvas.heightProperty().bindBidirectional(root.prefHeightProperty());
			primaryStage.setScene(scene);
			//primaryStage.sizeToScene();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	// This method is intended to generate diamond when scene gets resized 
	// Diamond gets generated at center only
	private void generateDiamondChanged() throws NumberFormatException{
		double diameter = Double.valueOf(txtDiameter.getText());
		double radius = diameter/2;
		double crownHeight = Double.valueOf(txtCrownHeight.getText());
		double girdleHeight = Double.valueOf(txtGirdleHeight.getText());
		double crownAngle = Double.valueOf(txtCrownAngle.getText());
		double pavilionAngle = Double.valueOf(txtPavilionAngle.getText());
		double crownAngleInRadions = Math.toRadians(crownAngle);
		double pavilionAngleInRadions = Math.toRadians(pavilionAngle);
		gc.clearRect(0, 0, 5000, 5000);
		generateLines();
		
		Point2D Center = new Point2D(scene.getWidth()/2, scene.getHeight()/2);
	
		gc.setStroke(Color.CADETBLUE);
		double[] topLineMidPoints = new double[4];
		double[] firstMiddleLineMidPoints = new double[4];
		double[] secondMiddleLineMidPoints = new double[4];
		
		
		Path diamondPath = new Path();
		diamondPath.setStroke(Color.CADETBLUE);
		diamondPath.setOpacity(30);
		
		MoveTo start = new MoveTo(Center.getX(), Center.getY() - girdleHeight/2);
		
		Point2D firstMiddleLinePoint1 = new Point2D(Center.getX() - radius, Center.getY() - girdleHeight/2);
		Point2D secondMiddleLinePoint1 = new Point2D(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY() + girdleHeight );
		Point2D secondMiddleLinePoint2 = new Point2D(secondMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint1.getY());
		Point2D firstMiddleLinePoint2 = new Point2D(firstMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint2.getY() - girdleHeight);
		Point2D crownHeightEndPoint = new Point2D(Center.getX(), Center.getY() - crownHeight);
		Point2D topLinePoint1 = new Point2D(crownHeightEndPoint.getX() - (radius) + ((crownHeight) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), crownHeightEndPoint.getY());
		Point2D topLinePoint2 = new Point2D(crownHeightEndPoint.getX() + (radius) - ((crownHeight) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), crownHeightEndPoint.getY());
		Point2D pavilionEndPoint = new Point2D(crownHeightEndPoint.getX(), start.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
		
		topLineMidPoints = getTwoMidPointsForLine(topLinePoint1.getX(), topLinePoint1.getY(), topLinePoint2.getX(), topLinePoint2.getY());
		firstMiddleLineMidPoints = getTwoMidPointsForLine(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY(), firstMiddleLinePoint2.getX(), firstMiddleLinePoint2.getY());
		secondMiddleLineMidPoints = getTwoMidPointsForLine(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY(), secondMiddleLinePoint2.getX(), secondMiddleLinePoint2.getY());
		
		LineTo radius1 = new LineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY());
		LineTo girdleHeightLeftSide = new LineTo(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY());
		LineTo secondMiddleLine = new LineTo(secondMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint1.getY());
		LineTo girdleHeightRightSide = new LineTo(firstMiddleLinePoint2.getX(), firstMiddleLinePoint2.getY());
		LineTo radius2 = new LineTo(Center.getX(), Center.getY() - girdleHeight/2);
		
		MoveTo firstMiddleLinePoint2Again = new MoveTo(firstMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint2.getY() - girdleHeight);
		LineTo crownEndRight = new LineTo(topLinePoint2.getX(), topLinePoint2.getY());
		LineTo topLine = new LineTo(topLinePoint1.getX(), topLinePoint1.getY());
		LineTo crownEndLeft =  new LineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY());
		
		MoveTo secondMiddleLinePoint1Again = new MoveTo(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY());
		LineTo pavilionLeftSide = new LineTo(pavilionEndPoint.getX(), pavilionEndPoint.getY());
		LineTo pavilionRightSide = new LineTo(secondMiddleLinePoint2.getX(), secondMiddleLinePoint2.getY());
		
		MoveTo topLineMidPoint1 = new MoveTo(topLineMidPoints[0], topLineMidPoints[1]);
		LineTo crownFacetLine1 = new LineTo(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1]);
		LineTo girdleFacetLine1 = new LineTo(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);
		
		MoveTo topLineMidPoint2 = new MoveTo(topLineMidPoints[2], topLineMidPoints[3]);
		LineTo crownFacetLine2 = new LineTo(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3]);
		LineTo girdleFacetLine2 = new LineTo(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);
		LineTo pavilionFacetLine2 = new LineTo(pavilionEndPoint.getX(), pavilionEndPoint.getY());
		LineTo pavilionFacetLine1 = new LineTo(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);
		diamondPath.getElements().addAll(start, radius1, girdleHeightLeftSide,
				secondMiddleLine, girdleHeightRightSide, radius2,
				firstMiddleLinePoint2Again, crownEndRight, topLine,
				crownEndLeft, secondMiddleLinePoint1Again, pavilionLeftSide,
				pavilionRightSide, topLineMidPoint1, crownFacetLine1, 
				girdleFacetLine1, topLineMidPoint2, crownFacetLine2,
				girdleFacetLine2, pavilionFacetLine2, pavilionFacetLine1);
		
		Bounds pathBounds = diamondPath.getLayoutBounds();
		
	    WritableImage snapshot = new WritableImage(
	            (int) pathBounds.getWidth()+2, (int) pathBounds.getHeight());
	    SnapshotParameters snapshotParams = new SnapshotParameters();
	    snapshotParams.setFill(Color.TRANSPARENT);
	    snapshot = diamondPath.snapshot(snapshotParams, snapshot);
	    //Image diamondImage = (Image) snapshot.getPixelWriter();
	    //gc.getCanvas().snapshot(snapshotParams, snapshot);
	    gc.drawImage(snapshot, pathBounds.getMinX(), pathBounds.getMinY());
	
		
	}


	// This method is intended to generate diamond at specified position where mouse click occurs
	private void generatePositioning() throws NumberFormatException {
		
		scene.setOnMouseClicked(e->{
			double diameter = Double.valueOf(txtDiameter.getText());
			double radius = diameter/2;
			double crownHeight = Double.valueOf(txtCrownHeight.getText());
			double girdleHeight = Double.valueOf(txtGirdleHeight.getText());
			double crownAngle = Double.valueOf(txtCrownAngle.getText());
			double pavilionAngle = Double.valueOf(txtPavilionAngle.getText());
			double crownAngleInRadions = Math.toRadians(crownAngle);
			double pavilionAngleInRadions = Math.toRadians(pavilionAngle);
			gc.clearRect(0, 0, 5000, 5000);
			generateLines();
			
			Point2D Center = new Point2D(e.getSceneX(), e.getSceneY());
			
			gc.setStroke(Color.CADETBLUE);
			double[] topLineMidPoints = new double[4];
			double[] firstMiddleLineMidPoints = new double[4];
			double[] secondMiddleLineMidPoints = new double[4];
			
			
			Path diamondPath = new Path();
			diamondPath.setStroke(Color.CADETBLUE);
			diamondPath.setOpacity(30);
			
			MoveTo start = new MoveTo(Center.getX(), Center.getY() - girdleHeight/2);
			
			Point2D firstMiddleLinePoint1 = new Point2D(Center.getX() - radius, Center.getY() - girdleHeight/2);
			Point2D secondMiddleLinePoint1 = new Point2D(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY() + girdleHeight );
			Point2D secondMiddleLinePoint2 = new Point2D(secondMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint1.getY());
			Point2D firstMiddleLinePoint2 = new Point2D(firstMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint2.getY() - girdleHeight);
			Point2D crownHeightEndPoint = new Point2D(Center.getX(), Center.getY() - crownHeight);
			Point2D topLinePoint1 = new Point2D(crownHeightEndPoint.getX() - (radius) + ((crownHeight) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), crownHeightEndPoint.getY());
			Point2D topLinePoint2 = new Point2D(crownHeightEndPoint.getX() + (radius) - ((crownHeight) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), crownHeightEndPoint.getY());
			Point2D pavilionEndPoint = new Point2D(crownHeightEndPoint.getX(), start.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
			
			topLineMidPoints = getTwoMidPointsForLine(topLinePoint1.getX(), topLinePoint1.getY(), topLinePoint2.getX(), topLinePoint2.getY());
			firstMiddleLineMidPoints = getTwoMidPointsForLine(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY(), firstMiddleLinePoint2.getX(), firstMiddleLinePoint2.getY());
			secondMiddleLineMidPoints = getTwoMidPointsForLine(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY(), secondMiddleLinePoint2.getX(), secondMiddleLinePoint2.getY());
			
			LineTo radius1 = new LineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY());
			LineTo girdleHeightLeftSide = new LineTo(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY());
			LineTo secondMiddleLine = new LineTo(secondMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint1.getY());
			LineTo girdleHeightRightSide = new LineTo(firstMiddleLinePoint2.getX(), firstMiddleLinePoint2.getY());
			LineTo radius2 = new LineTo(Center.getX(), Center.getY() - girdleHeight/2);
			
			MoveTo firstMiddleLinePoint2Again = new MoveTo(firstMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint2.getY() - girdleHeight);
			LineTo crownEndRight = new LineTo(topLinePoint2.getX(), topLinePoint2.getY());
			LineTo topLine = new LineTo(topLinePoint1.getX(), topLinePoint1.getY());
			LineTo crownEndLeft =  new LineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY());
			
			MoveTo secondMiddleLinePoint1Again = new MoveTo(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY());
			LineTo pavilionLeftSide = new LineTo(pavilionEndPoint.getX(), pavilionEndPoint.getY());
			LineTo pavilionRightSide = new LineTo(secondMiddleLinePoint2.getX(), secondMiddleLinePoint2.getY());
			
			MoveTo topLineMidPoint1 = new MoveTo(topLineMidPoints[0], topLineMidPoints[1]);
			LineTo crownFacetLine1 = new LineTo(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1]);
			LineTo girdleFacetLine1 = new LineTo(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);
			
			MoveTo topLineMidPoint2 = new MoveTo(topLineMidPoints[2], topLineMidPoints[3]);
			LineTo crownFacetLine2 = new LineTo(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3]);
			LineTo girdleFacetLine2 = new LineTo(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);
			LineTo pavilionFacetLine2 = new LineTo(pavilionEndPoint.getX(), pavilionEndPoint.getY());
			LineTo pavilionFacetLine1 = new LineTo(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);
			diamondPath.getElements().addAll(start, radius1, girdleHeightLeftSide,
					secondMiddleLine, girdleHeightRightSide, radius2,
					firstMiddleLinePoint2Again, crownEndRight, topLine,
					crownEndLeft, secondMiddleLinePoint1Again, pavilionLeftSide,
					pavilionRightSide, topLineMidPoint1, crownFacetLine1, 
					girdleFacetLine1, topLineMidPoint2, crownFacetLine2,
					girdleFacetLine2, pavilionFacetLine2, pavilionFacetLine1);
			
			Bounds pathBounds = diamondPath.getLayoutBounds();
			
		    WritableImage snapshot = new WritableImage(
		            (int) pathBounds.getWidth()+2, (int) pathBounds.getHeight());
		    SnapshotParameters snapshotParams = new SnapshotParameters();
		    snapshotParams.setFill(Color.TRANSPARENT);
		    snapshot = diamondPath.snapshot(snapshotParams, snapshot);
		    //Image diamondImage = (Image) snapshot.getPixelWriter();
		    //gc.getCanvas().snapshot(snapshotParams, snapshot);
		    gc.drawImage(snapshot, pathBounds.getMinX(), pathBounds.getMinY());
		

			
		});
	}


	// This method is responsible for the two lines which defines center of scene for judgement
	private void generateLines() {
		
		gc.setStroke(Color.BLACK);
		//Horizontal Line
		gc.strokeLine(0, scene.getHeight()/2, scene.getWidth(), scene.getHeight()/2);
		//Vertical Line
		gc.strokeLine(scene.getWidth()/2, 0, scene.getWidth()/2, scene.getHeight());
	
	}
	

	// This method generates diamond based on input parameters (Currently in pixels)
	@SuppressWarnings("unused")
	private void generateDiamond() throws NumberFormatException{
		
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
			double crownHeight = Double.valueOf(txtCrownHeight.getText());
			double girdleHeight = Double.valueOf(txtGirdleHeight.getText());
			double crownAngle = Double.valueOf(txtCrownAngle.getText());
			double pavilionAngle = Double.valueOf(txtPavilionAngle.getText());
			double crownAngleInRadions = Math.toRadians(crownAngle);
			double pavilionAngleInRadions = Math.toRadians(pavilionAngle);
			
			gc.clearRect(0, 0, 5000, 5000);
			generateLines();
			
			
			
			
			Point2D Center = new Point2D(scene.getWidth()/2, scene.getHeight()/2);
			
			gc.setStroke(Color.CADETBLUE);
			double[] topLineMidPoints = new double[4];
			double[] firstMiddleLineMidPoints = new double[4];
			double[] secondMiddleLineMidPoints = new double[4];
			
			
			Path diamondPath = new Path();
			diamondPath.setStroke(Color.CADETBLUE);
			diamondPath.setOpacity(30);
			
			MoveTo start = new MoveTo(Center.getX(), Center.getY() - girdleHeight/2);
			
			Point2D firstMiddleLinePoint1 = new Point2D(Center.getX() - radius, Center.getY() - girdleHeight/2);
			Point2D secondMiddleLinePoint1 = new Point2D(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY() + girdleHeight );
			Point2D secondMiddleLinePoint2 = new Point2D(secondMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint1.getY());
			Point2D firstMiddleLinePoint2 = new Point2D(firstMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint2.getY() - girdleHeight);
			Point2D crownHeightEndPoint = new Point2D(Center.getX(), Center.getY() - crownHeight);
			Point2D topLinePoint1 = new Point2D(crownHeightEndPoint.getX() - (radius) + ((crownHeight) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), crownHeightEndPoint.getY());
			Point2D topLinePoint2 = new Point2D(crownHeightEndPoint.getX() + (radius) - ((crownHeight) * Math.cos(crownAngleInRadions) / Math.sin(crownAngleInRadions)), crownHeightEndPoint.getY());
			Point2D pavilionEndPoint = new Point2D(crownHeightEndPoint.getX(), start.getY() + (radius/Math.cos(pavilionAngleInRadions)) * Math.sin(pavilionAngleInRadions));
			Point2D pavilionHeightEndPoint = new Point2D(Center.getX(), Center.getY() + girdleHeight);
			//TODO Will come at this(Percentage logic) after done with generatePositioning()
			double totalHeight = pavilionEndPoint.getY() - crownHeightEndPoint.getY();
			double pavilionHeight = pavilionEndPoint.getY() - pavilionHeightEndPoint.getY();
			
			
			topLineMidPoints = getTwoMidPointsForLine(topLinePoint1.getX(), topLinePoint1.getY(), topLinePoint2.getX(), topLinePoint2.getY());
			firstMiddleLineMidPoints = getTwoMidPointsForLine(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY(), firstMiddleLinePoint2.getX(), firstMiddleLinePoint2.getY());
			secondMiddleLineMidPoints = getTwoMidPointsForLine(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY(), secondMiddleLinePoint2.getX(), secondMiddleLinePoint2.getY());
			
			LineTo radius1 = new LineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY());
			LineTo girdleHeightLeftSide = new LineTo(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY());
			LineTo secondMiddleLine = new LineTo(secondMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint1.getY());
			LineTo girdleHeightRightSide = new LineTo(firstMiddleLinePoint2.getX(), firstMiddleLinePoint2.getY());
			LineTo radius2 = new LineTo(Center.getX(), Center.getY() - girdleHeight/2);
			
			MoveTo firstMiddleLinePoint2Again = new MoveTo(firstMiddleLinePoint1.getX() + diameter, secondMiddleLinePoint2.getY() - girdleHeight);
			LineTo crownEndRight = new LineTo(topLinePoint2.getX(), topLinePoint2.getY());
			LineTo topLine = new LineTo(topLinePoint1.getX(), topLinePoint1.getY());
			LineTo crownEndLeft =  new LineTo(firstMiddleLinePoint1.getX(), firstMiddleLinePoint1.getY());
			
			MoveTo secondMiddleLinePoint1Again = new MoveTo(secondMiddleLinePoint1.getX(), secondMiddleLinePoint1.getY());
			LineTo pavilionLeftSide = new LineTo(pavilionEndPoint.getX(), pavilionEndPoint.getY());
			LineTo pavilionRightSide = new LineTo(secondMiddleLinePoint2.getX(), secondMiddleLinePoint2.getY());
			
			MoveTo topLineMidPoint1 = new MoveTo(topLineMidPoints[0], topLineMidPoints[1]);
			LineTo crownFacetLine1 = new LineTo(firstMiddleLineMidPoints[0], firstMiddleLineMidPoints[1]);
			LineTo girdleFacetLine1 = new LineTo(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);
			
			MoveTo topLineMidPoint2 = new MoveTo(topLineMidPoints[2], topLineMidPoints[3]);
			LineTo crownFacetLine2 = new LineTo(firstMiddleLineMidPoints[2], firstMiddleLineMidPoints[3]);
			LineTo girdleFacetLine2 = new LineTo(secondMiddleLineMidPoints[2], secondMiddleLineMidPoints[3]);
			LineTo pavilionFacetLine2 = new LineTo(pavilionEndPoint.getX(), pavilionEndPoint.getY());
			LineTo pavilionFacetLine1 = new LineTo(secondMiddleLineMidPoints[0], secondMiddleLineMidPoints[1]);
			diamondPath.getElements().addAll(start, radius1, girdleHeightLeftSide,
					secondMiddleLine, girdleHeightRightSide, radius2,
					firstMiddleLinePoint2Again, crownEndRight, topLine,
					crownEndLeft, secondMiddleLinePoint1Again, pavilionLeftSide,
					pavilionRightSide, topLineMidPoint1, crownFacetLine1, 
					girdleFacetLine1, topLineMidPoint2, crownFacetLine2,
					girdleFacetLine2, pavilionFacetLine2, pavilionFacetLine1);
			
			Bounds pathBounds = diamondPath.getLayoutBounds();
			
		    WritableImage snapshot = new WritableImage(
		            (int) pathBounds.getWidth()+2, (int) pathBounds.getHeight());
		    SnapshotParameters snapshotParams = new SnapshotParameters();
		    snapshotParams.setFill(Color.TRANSPARENT);
		    snapshot = diamondPath.snapshot(snapshotParams, snapshot);
		    //Image diamondImage = (Image) snapshot.getPixelWriter();
		    //gc.getCanvas().snapshot(snapshotParams, snapshot);
		    gc.drawImage(snapshot, pathBounds.getMinX(), pathBounds.getMinY());
		    double crownHeightPercentage, girdleHeightPercentage, pavilionHeightPercentage;
		    crownHeightPercentage = (crownHeight/totalHeight) * 100;
		    girdleHeightPercentage = (girdleHeight/totalHeight) * 100;
		    pavilionHeightPercentage = (pavilionHeight/totalHeight) * 100;
//		    System.out.println("Total Height: " + totalHeight);
//		    System.out.println("Crown Height: " + crownHeight);
//		    System.out.println("Girdle Height: " + girdleHeight);
//		    System.out.println("Pavilion Height: " + pavilionHeight);
//		    System.out.println("Crown Height in percentage: " + crownHeightPercentage);
//		    System.out.println("Girdle Height in percentage: " + girdleHeightPercentage);
//		    System.out.println("Pavilion Height in percentage: " + pavilionHeightPercentage);
//		    System.out.println(crownHeightPercentage + girdleHeightPercentage+ pavilionHeightPercentage);
//		    
		});
	}
	
	
	// This method finds two mid points for a line(Input: line's two end points' X and Y co-ordinates)
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

