
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;


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

  // Backend Data
  FoodData foodData = new FoodData();
  List<Meal> mealData = new ArrayList<Meal>();
  List<String> rulesData = new ArrayList<String>();

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
    startScene = new Scene(startLayout, 1600, 900);
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

    // uncomment the setCenter for the content you are testing. Comment out the rest.
    //layout.setCenter(getStartCredits());
    layout.setCenter(createEditMeal(null));
    return layout;
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
  private GridPane createEditMeal(Meal meal) {
    // Define grid and settings
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(8);
    grid.setHgap(10);

    // TODO remove mocked data
    meal = mockData();

    // Define Labels
    Label foodListLabel = new Label("Available Food:");
    Label mealListLabel = new Label("Food in Meal:");

    // Define Food and Meal ListViews
    List<FoodItem> mealList = meal.getAllFoodItems();
    List<FoodItem> foodList = foodData.getAllFoodItems();
    ListView<FoodItem> foodListView = new ListView<FoodItem>();
    ListView<FoodItem> mealListView = new ListView<FoodItem>();
    for (FoodItem foodItem : foodList) {
      foodListView.getItems().add(foodItem);
    }
    for (FoodItem foodItem : mealList) {
      foodListView.getItems().remove(foodItem);
      mealListView.getItems().add(foodItem);
    }
    foodListView.getSelectionModel().selectionModeProperty().set(SelectionMode.MULTIPLE);
    mealListView.getSelectionModel().selectionModeProperty().set(SelectionMode.MULTIPLE);
    foodListView.setCellFactory(new Callback<ListView<FoodItem>, ListCell<FoodItem>>() {
      @Override
      public ListCell<FoodItem> call(ListView<FoodItem> param) {
        ListCell<FoodItem> cell = new ListCell<FoodItem>() {

          @Override
          protected void updateItem(FoodItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
              setText(item.getName());
            } else {
              setText("");
            }
          }
        };
        return cell;
      }

    });
    mealListView.setCellFactory(new Callback<ListView<FoodItem>, ListCell<FoodItem>>() {
      @Override
      public ListCell<FoodItem> call(ListView<FoodItem> param) {
        ListCell<FoodItem> cell = new ListCell<FoodItem>() {

          @Override
          protected void updateItem(FoodItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
              setText(item.getName());
            } else {
              setText("");
            }
          }
        };
        return cell;
      }
    });

    // Define Buttons
    Button addButton = new Button("->");
    addButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        Object[] foods = foodListView.getSelectionModel().getSelectedItems().toArray();
        for (Object foodItem : foods) {
          foodListView.getItems().remove((FoodItem) foodItem);
          mealListView.getItems().add((FoodItem) foodItem);
        } ;

      }
    });
    Button removeButton = new Button("<-");
    removeButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Object[] foods = mealListView.getSelectionModel().getSelectedItems().toArray();
        for (Object foodItem : foods) {
          foodListView.getItems().add((FoodItem) foodItem);
          mealListView.getItems().remove((FoodItem) foodItem);
        } ;

      }
    });
    VBox toggleButtonBox = new VBox();
    toggleButtonBox.setPadding(new Insets(10, 10, 10, 10));
    toggleButtonBox.setSpacing(30);
    toggleButtonBox.alignmentProperty().set(Pos.CENTER);
    toggleButtonBox.getChildren().addAll(addButton, removeButton);
    Button saveButton = new Button("Save");

    // Add all to grid
    GridPane.setConstraints(foodListLabel, 0, 0, 1, 1);
    GridPane.setConstraints(mealListLabel, 2, 0, 1, 1);
    GridPane.setConstraints(foodListView, 0, 1, 1, 1);
    GridPane.setConstraints(toggleButtonBox, 1, 1, 1, 1);
    GridPane.setConstraints(mealListView, 2, 1, 1, 1);
    GridPane.setConstraints(saveButton, 2, 2, 1, 1, HPos.RIGHT, VPos.CENTER);

    grid.getChildren().addAll(foodListLabel, mealListLabel, foodListView, mealListView,
        toggleButtonBox, saveButton);

    return grid;
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
    // create grid
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(200, 50, 200, 50));
    grid.setVgap(8);
    grid.setHgap(10);

    // create text and add to grid
    Label appName = new Label("Food Truck");
    Label appCredits =
        new Label("By: Brett Clarke, Ryan Keil, Jerald Kuan, Riley Olson, and Jamison Wickman");
    GridPane.setConstraints(appName, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    GridPane.setConstraints(appCredits, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);

    // import logo and add to grid
    String imagePath = "file:food-truck-logo.png";
    Image image = new Image(imagePath);
    ImageView imageView = new ImageView(image);
    GridPane.setConstraints(imageView, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);

    grid.getChildren().addAll(appName, appCredits, imageView);

    return grid;
  }

  private Meal mockData() {
    Meal meal = new Meal();
    foodData.loadFoodItems("foodItems.csv");
    meal.addFoodItem(foodData.getAllFoodItems().get(0));
    return meal;
  }
}
