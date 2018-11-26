
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
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
  private VBox getTopMenu() {
    // TODO
	  //create menu and menu bar
	  Menu menu = new Menu("File");
	  MenuBar menuBar = new MenuBar();
	  menuBar.getMenus().add(menu);
	  
	  //create menu items
	  MenuItem loadFoodList = new MenuItem("Load Food List");
	  MenuItem saveFoodList = new MenuItem("Save Food List");
	  MenuItem addFoodItem = new MenuItem("Add Food Item");
	  MenuItem loadMeal = new MenuItem("Load Meal List"); 
	  MenuItem saveMeal = new MenuItem("Save Meal List");
	  
	  //add menu items to the menu
	  menu.getItems().add(loadFoodList);
	  menu.getItems().add(saveFoodList);
	  menu.getItems().add(addFoodItem);
	  menu.getItems().add(loadMeal);
	  menu.getItems().add(saveMeal);
	  
	  //menu button actions
	  addFoodItem.setOnAction(e -> getAddFoodItem());
	  
	  VBox menuBarVBox = new VBox(menuBar);
	  
    return menuBarVBox;
  }
  
  /**
   * Alert box for adding new food item
   */
  private void getAddFoodItem() {
	  //TODO
	  //set up window
	  Stage alertWindow = new Stage();
	  alertWindow.initModality(Modality.APPLICATION_MODAL);
	  alertWindow.setTitle("Add Food Item");
	  alertWindow.setMinWidth(500);
	  alertWindow.setMaxWidth(500);
	  alertWindow.setMinHeight(500);
	  
	  //set up grid
	  GridPane alertGrid = new GridPane();
	  alertGrid.setHgap(10);
	  alertGrid.setVgap(10);
	  Scene alertBoxScene = new Scene(alertGrid);
	  
	  //grid elements
	  //f08a,Similac_Formula,calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
	  Label nameLabel = new Label();
	  nameLabel.setText("Name: ");
	  nameLabel.setMinHeight(25);
	  
	  TextField nameInput = new TextField();
	  nameInput.setMinWidth(400);
	  nameInput.setMinHeight(25);
	  
	  Label idLabel = new Label();
	  idLabel.setText("ID: ");
	  idLabel.setMinHeight(25);
	  
	  TextField idInput = new TextField();
	  idInput.setMinWidth(400);
	  idInput.setMinHeight(25);
	  
	  Label spacer = new Label();
	  
	  Label calLabel = new Label();
	  calLabel.setText("Calories: ");
	  idLabel.setMinHeight(25);
	  
	  TextField calInput = new TextField();
	  calInput.setMinWidth(200);
	  calInput.setMinWidth(25);
	  
	  Label fatLabel = new Label();
	  fatLabel.setText("Fat: ");
	  
	  TextField fatInput = new TextField();
	  
	  Label carbLabel = new Label();
	  carbLabel.setText("Fiber: ");
	  
	  TextField carbInput = new TextField();
	  
	  Label fiberLabel = new Label();
	  fiberLabel.setText("Fiber: ");
	  fiberLabel.setMinHeight(25);
	  
	  TextField fiberInput = new TextField();
	  fiberInput.setMinWidth(200);
	  fiberInput.setMinHeight(25);
	  
	  Label proLabel = new Label();
	  proLabel.setText("Protien: ");
	  
	  TextField proInput = new TextField();
	  
	  Button acceptButton = new Button("Accept");
	  Button closeButton = new Button("Close");
	  closeButton.setOnAction(e -> alertWindow.close());
	  
	  //build grid
	  alertGrid.add(nameLabel, 0, 0);
	  alertGrid.add(nameInput, 1, 0, 3, 1);
	  alertGrid.add(idLabel, 0, 1);
	  alertGrid.add(idInput, 1, 1, 3, 1);
	  alertGrid.add(spacer, 0, 2);
	  alertGrid.add(calLabel, 0, 3);
	  alertGrid.add(calInput, 1, 3);
	  alertGrid.add(fatLabel, 0, 4);
	  alertGrid.add(fatInput, 1, 4);
	  alertGrid.add(carbLabel, 0, 5);
	  alertGrid.add(carbInput, 1, 5);
	  alertGrid.add(fiberLabel, 2, 3);
	  alertGrid.add(fiberInput, 3, 3);
	  alertGrid.add(proLabel, 2, 4);
	  alertGrid.add(proInput, 3, 4);
	  alertGrid.add(acceptButton, 2, 5);
	  alertGrid.add(closeButton, 3, 5);
	  
	  
	  //VBox alertBox = new VBox();
	  //alertBox.getChildren().addAll(testLabel, acceptButton, closeButton);
	  //Scene alertBoxScene = new Scene(alertBox);
	  
	  alertWindow.setScene(alertBoxScene);
	  alertWindow.showAndWait();
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
