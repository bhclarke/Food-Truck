import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FoodTruckApplication extends Application {

  Stage window;

  // Initial Scene
  BorderPane startLayout;
  Scene startScene;

  // Edit Food
  BorderPane editFoodLayout;
  Scene editFoodScene;

  // Edit Meal Scene
  BorderPane editMealLayout;
  Scene editMealScene;

  // Edit Rules Scene
  BorderPane editRuleLayout;
  Scene editRuleScene;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Set Application Title
    window = primaryStage;
    window.setTitle("Food Truck");

    // Create main layout
    startLayout = new BorderPane();

    // Set initial views in mainLayout: TODO

    // set scene
    startScene = new Scene(startLayout, 600, 400);
    window.setScene(startScene);

    // show Application
    window.show();

  }

}
