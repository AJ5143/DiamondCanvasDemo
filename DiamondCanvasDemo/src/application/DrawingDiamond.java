package application;


import com.sun.javafx.geom.Line2D;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DrawingDiamond  extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			double midPointsCrownTop[] = new double[4];
			double midPointsGirdle1[] = new double[4];
			double midPointsGirdle2[] = new double[4];
			Label noteForMove = new Label("Note : Moving co-ordinates are relative to center");
			Label lblXCo_Ordinate = new Label("X Co-ordinate:");
			Label lblYCo_Ordinate = new Label("Y Co-ordinate:");
			TextField txtX_Co_Ordinate = new TextField();
			TextField txtY_Co_Ordinate = new TextField();
			
			txtX_Co_Ordinate.setPromptText("Default : 0");
			txtY_Co_Ordinate.setPromptText("Default : 0");
			Button btnMovePos = new Button("Move");
			Button btnResetPos = new Button("Reset to Center");
			HBox hbXCo = new HBox();
			HBox hbYCo = new HBox();
			HBox hbButtons = new HBox();
			hbXCo.getChildren().addAll(lblXCo_Ordinate, txtX_Co_Ordinate );
			hbYCo.getChildren().addAll(lblYCo_Ordinate, txtY_Co_Ordinate);
			hbButtons.getChildren().addAll(btnMovePos, btnResetPos);
			hbXCo.setAlignment(Pos.CENTER);
			hbYCo.setAlignment(Pos.CENTER);
			hbButtons.setAlignment(Pos.CENTER);
			hbXCo.setSpacing(5);
			hbYCo.setSpacing(5);
			hbButtons.setSpacing(5);
			
			//double newPosX = Double.valueOf(txtX_Co_Ordinate.getText());
			//double newPosY = Double.valueOf(txtY_Co_Ordinate.getText());
			
			
			
			gc = diamond.getGraphicsContext2D();
			gc.setStroke(Color.CADETBLUE);
			gc.setLineWidth(2);
			Point2D topLinePoint1 = new Point2D(203,165);
			Point2D topLinePoint2 = new Point2D(397,165);
			Point2D firstMiddleLinePoint1 = new Point2D(140,245);
			Point2D firstMiddleLinePoint2 = new Point2D(460,245);
			Point2D secondMiddleLinePoint1 = new Point2D(140,260);
			Point2D secondMiddleLinePoint2 = new Point2D(460,260);
			Point2D lastPointPavilionFacet = new Point2D(300,435);
			Point2D topLineMidPoint = topLinePoint1.midpoint(topLinePoint2);
			Point2D firstMiddleLineMidPoint = firstMiddleLinePoint1.midpoint(firstMiddleLinePoint2);
			Point2D secondMiddleLineMidPoint = secondMiddleLinePoint1.midpoint(secondMiddleLinePoint2);
			Line crownHeight = new Line();
			//Line2D crownLine = new Line2D();
			crownHeight.setStartX(topLineMidPoint.getX());
			crownHeight.setStartY(topLineMidPoint.getY());
			crownHeight.setEndX(firstMiddleLineMidPoint.getX());
			crownHeight.setEndY(firstMiddleLineMidPoint.getY());
			//gc.strokeLine(crownHeight.getStartX(), crownHeight.getStartY(), crownHeight.getEndX(), crownHeight.getEndY()); // crown height
			
			Line girdleHeight = new Line();
			girdleHeight.setStartX(firstMiddleLineMidPoint.getX());
			girdleHeight.setStartY(firstMiddleLineMidPoint.getY());
			girdleHeight.setEndX(secondMiddleLineMidPoint.getX());
			girdleHeight.setEndY(secondMiddleLineMidPoint.getY());
			
			double diameter = firstMiddleLinePoint2.getX() - firstMiddleLinePoint1.getX();
			double crownAngle = firstMiddleLinePoint1.angle(topLinePoint1);
			double pavilionAngle = secondMiddleLinePoint1.angle(lastPointPavilionFacet);
			//gc.strokeLine(girdleHeight.getStartX(), girdleHeight.getStartY(), girdleHeight.getEndX(), girdleHeight.getEndY() ); // girdle height
			
			gc.strokeLine(203,165,397,165);		// Top part (Line at top of crown) 
			midPointsCrownTop = getTwoMidPointsForLine(203,165,397,165);
			//gc.strokeLine(midPointsTop[0], midPointsTop[1], 1, 1); // for testing only
			//gc.strokeLine(midPointsTop[2], midPointsTop[3], 1, 1); // for testing only
			gc.strokeLine(140,245,460,245);		// Middle part (Horizontal Line which joins crown with pavilion)
			midPointsGirdle1 = getTwoMidPointsForLine(140,245,460,245);
			//gc.strokeLine(midPointsGirdle1[0],midPointsGirdle1[1] , 5, 5); // for testing only
			//gc.strokeLine(midPointsGirdle1[2],midPointsGirdle1[3] , 5, 5); // for testing only
			gc.strokeLine(140,260,460,260);		// Middle part (Second Horizontal Line which joins crown with pavilion)
			midPointsGirdle2 = getTwoMidPointsForLine(140,260,460,260);
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
			
			
			//gc.strokeLine(300, 0, 300, 720);	// Vertical base line
			//gc.strokeLine(0, 252.5, 1280, 252.5);// Horizontal base line
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
			
			
			
			vbCenter.setPrefSize(700,700);
			vbCenter.setSpacing(5);
			vbCenter.getChildren().addAll(diamond, noteForMove , hbXCo, hbYCo, hbButtons);
			
			//vbCenter.setAlignment(Pos.BASELINE_CENTER);
			vbCenter.setAlignment(Pos.CENTER);
			try {
				btnMovePos.setOnMouseClicked(e ->{
					
					if(txtX_Co_Ordinate.getText().isEmpty() & txtY_Co_Ordinate.getText().isEmpty()) {
						diamond.setTranslateX(0);
						diamond.setTranslateY(0);
					}
					else if(txtX_Co_Ordinate.getText().isEmpty()) {
							diamond.setTranslateX(0);
							diamond.setTranslateY( Double.valueOf(txtY_Co_Ordinate.getText()));
					}
					else if(txtY_Co_Ordinate.getText().isEmpty()){
							diamond.setTranslateY(0);
							diamond.setTranslateX( Double.valueOf(txtX_Co_Ordinate.getText()));
					}
					
					else {
						diamond.setTranslateX( Double.valueOf(txtX_Co_Ordinate.getText()));
						diamond.setTranslateY( Double.valueOf(txtY_Co_Ordinate.getText()));
					}
					
//					Translate t = (Translate) canvas.getTransforms();
//					t.setX(Double.valueOf(txtX_Co_Ordinate.getText()));
//					t.setY(Double.valueOf(txtY_Co_Ordinate.getText()));
					//gc.translate(Double.valueOf(txtX_Co_Ordinate.getText()), Double.valueOf(txtY_Co_Ordinate.getText()));
					txtX_Co_Ordinate.clear();
					txtY_Co_Ordinate.clear();
				});
			}
			catch(NumberFormatException e) {
				System.out.println("Empty field not allowed");
				
				e.printStackTrace();
			}
			
			btnResetPos.setOnMouseClicked(e ->{
				diamond.setTranslateX(0);
				diamond.setTranslateY(0);
				txtX_Co_Ordinate.clear();
				txtY_Co_Ordinate.clear();
			});
//			scene.setOnMouseClicked(e->{
//				vbCenter.setLayoutX(e.getSceneX());
//				vbCenter.setLayoutY(e.getSceneY());
//			});
//			vbCenter.setOnMouseDragged(e->{
//				vbCenter.setLayoutX(e.getSceneX() - 300);
//				vbCenter.setLayoutY(e.getSceneY() - 240);
//				refreshLine();
//			});
			//root.getChildren().add(canvas);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.setOnMouseClicked(e->{
				diamond.setTranslateX(e.getSceneX() - 727);
				diamond.setTranslateY(e.getSceneY() - 330);
			});
			
//			System.out.println(Screen.getPrimary().getDpi());
//			System.out.println(Screen.getPrimary().getVisualBounds());
//			System.out.println(Screen.getPrimary().getBounds());
//			System.out.println(Screen.getPrimary().getOutputScaleX());
//			System.out.println(Screen.getPrimary().getOutputScaleY());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	


//	private void refreshLine() {
//		Point2D horizontalFirst;
//		Point2D horizontalLast;
//		Point2D verticalFirst;
//		Point2D verticalLast;
//		
//	}



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



	Canvas diamond = new Canvas(500,500);
	GraphicsContext gc; 
	VBox vbCenter = new VBox();
	
	//BorderPane root = new BorderPane();

	Scene scene = new Scene(vbCenter,1366, 768);

public static void main(String[] args) {
	launch(args);
	//System.out.println(Screen.getPrimary());
}
}
