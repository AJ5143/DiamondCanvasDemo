package application;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StageResizeDemo extends Application {
	

@Override
public void start(Stage primaryStage) throws Exception {
		Label lbl = new Label("Hiii");
		TextField txt = new TextField();
		txt.setPromptText("Hellooooo");
		
		HBox hb = new HBox(lbl,txt);
		hb.setSpacing(5);
		hb.setAlignment(Pos.CENTER);
			final ChangeListener<Number> listener = new ChangeListener<Number>()
			{
				final Timer timer = new Timer(); // uses a timer to call your resize method
				TimerTask task = null; // task to execute after defined delay
				final long delayTime = 200; // delay that has to pass in order to consider an operation done

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue)
				{
					if (task != null)
					{ // there was already a task scheduled from the previous operation ...
						task.cancel(); // cancel it, we have a new size to consider
					}
				
					task = new TimerTask() // create new task that calls your resize operation
							{
						@Override
						public void run()
							{ 
							// here you can place your resize code
							//System.out.println("resize to " + primaryStage.getWidth() + " " + primaryStage.getHeight());
								primaryStage.setOnCloseRequest(e->{
								System.exit(0);
								task.cancel();
							});
						
							
					}
							};	
							// schedule new task
							timer.schedule(task, delayTime);
				}

				
			};

		//finally we have to register the listener
		primaryStage.widthProperty().addListener(listener);
		primaryStage.heightProperty().addListener(listener);
		//BorderPane root = new BorderPane();
		VBox root = new VBox();
		
		root.getChildren().add(hb);
		root.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	
}
	public static void main(String[] args) {
		launch(args);
	}
}


	