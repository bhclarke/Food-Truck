import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

    // set scene
    startScene = new Scene(startLayout, 1200, 800);
    window.setScene(startScene);

    // show Application
    window.show();

  }

  /**
   * Create layout for the initial start of the application
   * 
   * @return BorderPane. You can change the return type.
   */
  private BorderPane createStart() {
    BorderPane layout = new BorderPane();
    // Set Menu
    layout.setTop(getTopMenu());
    // Set up application content
    layout.setLeft(getFoodList());
    layout.setRight(getMealList());
    layout.setCenter(getStartCredits());
    return new BorderPane();
  }

  /**
   * Create layout for the edit food area of the application
   * 
   * @return BorderPane. You can change the return type.
   */
  private BorderPane createEditFood() {
    // TODO
    return new BorderPane();
  }

  /**
   * Create layout for the edit meal area of the application
   * 
   * @return BorderPane. You can change the return type.
   */
  private BorderPane createEditMeal() {
    // TODO
    return new BorderPane();
  }

  /**
   * Create layout for the edit rule area of the application
   * 
   * @return BorderPane. You can change the return type.
   */
  private BorderPane createEditRule() {
    // TODO
    return new BorderPane();
  }

  /**
   * Create view of food list that includes the filter and search functionality. This will be added
   * to the left-hand area of each scene.
   * 
   * @return VBox. You can change the return type.
   */
  private VBox getFoodList() {
    // TODO
    return new VBox();
  }

  /**
   * Create view of meal list that includes access to meal functionality. This will be added to the
   * right-hand area of each scene.
   * 
   * @return VBox. You can change the return type.
   */
  private VBox getMealList() {
    // TODO
    return new VBox();
  }

  /**
   * Creates menus along top of application.
   * 
   * @return Menu. You can change the return type.
   */
  private MenuBar getTopMenu() {
    // TODO
    return new MenuBar();
  }
  
  /**
   * Create view of application logo and team credits. Displayed only at start of application in the
   * center of the application.
   * 
   * @return GridPane. You can change the return type.
   */
  private GridPane getStartCredits() {
    // TODO
    return new GridPane();
  }
}
