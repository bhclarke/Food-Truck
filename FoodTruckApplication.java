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

    // Create initial layout
    startLayout = createStart();

    // Set initial views in startLayout: TODO

    // set scene
    startScene = new Scene(startLayout, 1200, 800);
    window.setScene(startScene);

    // show Application
    window.show();

  }
  
  /**
   *  Create layout for the initial start of the application
   * @return BorderPane. You can change the return type. 
   */
  private BorderPane createStart() {
    //TODO
    return new BorderPane();
  }
  
  /**
   *  Create layout for the edit food area of the application
   * @return BorderPane. You can change the return type. 
   */
  private BorderPane createEditFood() {
    //TODO
    return new BorderPane();
  }
  
  /**
   *  Create layout for the edit meal area of the application
   * @return BorderPane. You can change the return type. 
   */
  private BorderPane createEditMeal() {
    //TODO
    return new BorderPane();
  }

  /**
   *  Create layout for the edit rule area of the application
   * @return BorderPane. You can change the return type. 
   */
  private BorderPane createEditRule() {
    //TODO
    return new BorderPane();
  }
}
