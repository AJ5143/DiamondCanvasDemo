package application;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class StageResizeThrottling extends Application {

    private Random rng = new Random();

    @Override
    public void start(Stage primaryStage) {

        BlockingQueue<Point2D> dimensionChangeQueue = new ArrayBlockingQueue<>(1);
        ChangeListener<Number> dimensionChangeListener = (obs, oldValue, newValue) -> {
            dimensionChangeQueue.clear();
            dimensionChangeQueue.add(new Point2D(primaryStage.getWidth(), primaryStage.getHeight()));
        };
        primaryStage.widthProperty().addListener(dimensionChangeListener);
        primaryStage.heightProperty().addListener(dimensionChangeListener);

        Thread processDimensionChangeThread = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Waiting for change in size");
                    Point2D size = dimensionChangeQueue.take();
                    System.out.printf("Detected change in size to [%.1f, %.1f]: processing%n", size.getX(), size.getY());
                    process(size, primaryStage);
                    System.out.println("Done processing");
                }
            } catch (InterruptedException letThreadExit) { }
        });
        processDimensionChangeThread.setDaemon(true);
        processDimensionChangeThread.start();

        Scene scene = new Scene(new StackPane(), 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void process(Point2D stageDimension, Stage stage) throws InterruptedException {
        // simulate slow process:
        Thread.sleep(500 + rng.nextInt(1000));
        final String title = String.format("Width: %.0f Height: %.0f", stageDimension.getX(), stageDimension.getY());
        Platform.runLater(() -> stage.setTitle(title));
    }

    public static void main(String[] args) {
        launch(args);
    }
}