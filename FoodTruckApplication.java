//Icon credits
// open: https://www.iconfinder.com/icons/1608888/folder_open_icon
// save: https://www.iconfinder.com/icons/352084/guardar_save_icon
// add: https://www.iconfinder.com/icons/126583/add_plus_icon

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
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
  FileChooser fileChooser = new FileChooser();

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
    //layout.setRight(getMealList());
    layout.setRight(getMealGrid());

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
  private GridPane getFoodList() {
	    // Define grid and settings
	    GridPane grid = new GridPane();
	    grid.setPadding(new Insets(10, 10, 0, 10));
    
	    FoodData food = new FoodData();
	    food.loadFoodItems("foodItems.txt");
	    
	    // Define Labels
	    Label foodListLabel = new Label("Food List");
	    
	    Font f = new Font(20);
	    foodListLabel.setFont(f);

	    // Define Food and Meal ListViews
	    List<FoodItem> foodList = food.getAllFoodItems();
	    
	    ListView<String> foodListView = new ListView<String>();
	    foodListView.getSelectionModel().selectionModeProperty().set(SelectionMode.MULTIPLE);
	    for (FoodItem fi : foodList) {
	      foodListView.getItems().add(fi.getName());
	    }
	    	    
	    foodListView.setMinHeight(700);
	    foodListView.setMinWidth(400);
	    
	    // Add all to grid
	    GridPane.setConstraints(foodListLabel, 0, 0, 1, 1);
	    GridPane.setConstraints(foodListView, 0, 2, 2, 1);
	    
	    grid.getChildren().addAll(foodListLabel,foodListView);
	
			TextField input = new TextField();
			input.setMaxHeight(20); input.setMinWidth(200);
			input.setPromptText("Search Food Items");
			input.setFocusTraversable(false);
			
			Button add = new Button("Add Food Item");		
			Button rule = new Button("Set Filter Rules");
			
			add.setOnAction(e -> getAddFoodItem());
			
		    GridPane.setConstraints(add, 1, 1, 1, 1, HPos.RIGHT, VPos.BOTTOM);
		    GridPane.setConstraints(input, 0, 1, 1, 1, HPos.LEFT, VPos.BOTTOM);
		    GridPane.setConstraints(rule, 1, 1, 1, 1, HPos.LEFT, VPos.BOTTOM);

			//grid.setConstraints(child, columnIndex, rowIndex, columnspan, rowspan, halignment, valignment, hgrow, vgrow, margin);
			grid.getChildren().addAll(input, add, rule);
			grid.setVgap(5);
			grid.setHgap(5);
			
		    return grid;
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
   * Use a GridPane to display the meal list, similar to how we're displaying the food list.
   * @return
   */
  private GridPane getMealGrid() {
	  GridPane mealGrid = new GridPane();
	  // TODO: right offset of 100 gets things closer together, but I need to set each layout (2 GridPane, 1 BorderPane) to move with each other
	  mealGrid.setPadding(new Insets(10, 100, 10, 10)); // right offset of 100 gets things closer together
	  // TODO: Need to adjust padding so that both Food List and Meal List grids are aligned
	  mealGrid.setVgap(8);
	  mealGrid.setHgap(10);
	  Label mealGridLabel = new Label("Meal list");
	  
	  // if a ListView of type String, we can display the name of each meal; if a ListView of type Meal, we can dynamically change nutrientField
	  ListView<Meal> mealListView = new ListView<Meal>();
	  mealListView.getSelectionModel().selectionModeProperty().set(SelectionMode.SINGLE);  // not sure if I need to list this -- I think default is single select
	  mealListView.setMinHeight(700);
	  mealListView.setMinWidth(400);
	  
	  // TODO: remove mock data // begin mock data
	  List<Meal> mealList = new ArrayList<Meal>();  // a list of meals that we'll add to the ListView
	  foodData.loadFoodItems("foodItems.csv");
	  Meal meal1 = new Meal();
	  meal1.addFoodItem(foodData.getAllFoodItems().get(0));
	  meal1.addFoodItem(foodData.getAllFoodItems().get(2));
	  Meal meal2 = new Meal();
	  meal2.addFoodItem(foodData.getAllFoodItems().get(5));
	  meal2.addFoodItem(foodData.getAllFoodItems().get(6));

	  mealList.add(meal1);
	  mealList.add(meal2);
	  // end mock data
	  
	  // create a text field for displaying nutrient data for each meal
	  TextArea nutrientField = new TextArea();
	  

	  for (Meal v : mealList) {
		  mealListView.getItems().add(v);
	  }
	  
	  /*
	  // TODO: Is there a better way of implementing this instead of regenerating nutrientString and re-running analyzeMealData with each selection?
	  // Use listeners to update mealListView whenever a new Meal is selected
	  // Build nutrientString for newValue (the newly selected meal), then set this value to nutrientField
	  mealListView.getSelectionModel().selectedItemProperty().addListener(
			  (observable, oldValue, newValue) -> nutrientField.setText(mealListView.getSelectionModel().getSelectedItem().analyzeMealData())
			  );
	  // Clear out nutrientString for newValue so that the next time that same Meal is selected, it is starting with a clean nutrientString
	  // (instead of appending to a nutrientString that already
	  mealListView.getSelectionModel().selectedItemProperty().addListener(
			  (observable, oldValue, newValue) -> newValue.cleanUpSring()
			  );*/
	  
	  // Creates a listener to update the value in the TextArea (nutrientField) whenever a value is selected in the meal list (mealListView).
	  mealListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Meal>() {
		  @Override
		  public void changed(ObservableValue<? extends Meal> observable, Meal oldValue, Meal newValue) {
			  if (newValue.getNutrientString().isEmpty()) {
				  nutrientField.setText(mealListView.getSelectionModel().getSelectedItem().analyzeMealData());
			  }
			  else {
				  nutrientField.setText(newValue.getNutrientString());
			  }
		  }
	  });
	  
	  GridPane.setConstraints(mealGridLabel, 0, 0, 1, 1);
	  GridPane.setConstraints(mealListView, 0, 2, 2, 1);
	  GridPane.setConstraints(nutrientField, 0, 3, 2, 1);
	  mealGrid.getChildren().addAll(mealGridLabel, mealListView, nutrientField);
	  
	  
	  return mealGrid;
  }

  /**
   * Creates menus along top of application.
   * 
   * @return VBox containing the menu bar
   */
  private VBox getTopMenu() {
    // TODO
	  //create menu and menu bar
	  Menu foodMenu = new Menu("Food");
	  Menu mealMenu = new Menu("Meal");
	  MenuBar menuBar = new MenuBar();
	  menuBar.getMenus().add(foodMenu);
	  menuBar.getMenus().add(mealMenu);
	  
	  //create menu items
	  MenuItem loadFoodList = new MenuItem("Open Food List");
	  MenuItem saveFoodList = new MenuItem("Save Food List");
	  MenuItem addFoodItem = new MenuItem("Add Food Item");
	  MenuItem loadMeal = new MenuItem("Open Meal List"); 
	  MenuItem saveMeal = new MenuItem("Save Meal List");
	  
	  //Add icons menu.setGraphic(new ImageView("file:volleyball.png"));
	  ImageView loadImg = new ImageView("file:open.png");
	  loadImg.setFitHeight(15);
	  loadImg.setFitWidth(15);
	  
	  ImageView saveImg = new ImageView("file:save.png");
	  saveImg.setFitHeight(15);
	  saveImg.setFitWidth(15);
	  
	  ImageView addImg = new ImageView("file:add.png");
	  addImg.setFitHeight(15);
	  addImg.setFitWidth(15);
	  
	  ImageView loadImgMeal = new ImageView("file:open.png");
	  loadImgMeal.setFitHeight(15);
	  loadImgMeal.setFitWidth(15);
	  
	  ImageView saveImgMeal = new ImageView("file:save.png");
	  saveImgMeal.setFitHeight(15);
	  saveImgMeal.setFitWidth(15);
	  
	  loadFoodList.setGraphic(loadImg);
	  saveFoodList.setGraphic(saveImg);
	  addFoodItem.setGraphic(addImg);
	  loadMeal.setGraphic(loadImgMeal);
	  saveMeal.setGraphic(saveImgMeal);
	  
	  //add menu items to the menu
	  foodMenu.getItems().add(loadFoodList);
	  foodMenu.getItems().add(saveFoodList);
	  foodMenu.getItems().add(addFoodItem);
	  mealMenu.getItems().add(loadMeal);
	  mealMenu.getItems().add(saveMeal);
	  
	  //TODO remove test button
	  MenuItem testItem = new MenuItem("Test Item");
	  foodMenu.getItems().add(testItem);
	  testItem.setOnAction(e -> getSaveMessage("Test","Sample text"));
	  
	  //menu button actions
	  addFoodItem.setOnAction(e -> getAddFoodItem());
	  loadFoodList.setOnAction(e -> {
		  fileChooser.setTitle("Open Food List");
		  File selectedFile = fileChooser.showOpenDialog(window);
		  foodData.loadFoodItems(selectedFile.getAbsolutePath());
		  });
	  saveFoodList.setOnAction(e -> {
		  fileChooser.setTitle("Save Food List");
		  fileChooser.getExtensionFilters().add(
				  new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		  File selectedFile = fileChooser.showSaveDialog(window);
		  foodData.saveFoodItems(selectedFile.getAbsolutePath());
		  });
	  loadMeal.setOnAction(e -> {
		  fileChooser.setTitle("Open Meal List"); 
		  File selectedFile = fileChooser.showOpenDialog(window);
		  
		  });
	  saveMeal.setOnAction(e -> {
		  fileChooser.setTitle("Save Meal List");
		  File selectedFile = fileChooser.showSaveDialog(window);
	  });
	  VBox menuBarVBox = new VBox(menuBar);
	  
    return menuBarVBox;
  }
  
  /**
   * Pop-up for adding new food item
   */
  private void getAddFoodItem() {
	  //TODO
	  //set up window
	  Stage alertWindow = new Stage();
	  alertWindow.initModality(Modality.APPLICATION_MODAL);
	  alertWindow.setTitle("Add Food Item");
	  alertWindow.setMinWidth(570);
	  alertWindow.setMaxWidth(570);
	  alertWindow.setMinHeight(290);
	  alertWindow.setMaxHeight(290);
	  
	  //set up grid
	  GridPane alertGrid = new GridPane();
	  alertGrid.setHgap(10);
	  alertGrid.setVgap(10);
	  //Scene alertBoxScene = new Scene(alertGrid);
	  
	  //grid elements
	  Label header = new Label();
	  header.setText("Complete form to add a new food item to food list.");
	  header.setMinHeight(25);
	  
	  Label nameLabel = new Label();
	  nameLabel.setText("Name: ");
	  nameLabel.setMinHeight(25);
	  
	  TextField nameInput = new TextField();
	  nameInput.setMinWidth(200);
	  nameInput.setMinHeight(25);
	  
	  Label idLabel = new Label();
	  idLabel.setText("ID: ");
	  idLabel.setMinHeight(25);
	  
	  TextField idInput = new TextField();
	  idInput.setMinWidth(200);
	  idInput.setMinHeight(25);
	  
	  Label spacer = new Label();
	  
	  Label calLabel = new Label();
	  calLabel.setText("Calories: ");
	  idLabel.setMinHeight(25);
	  
	  TextField calInput = new TextField();
	  calInput.setMinWidth(200);
	  calInput.setMinHeight(25);
	  calInput.setText("0");
	  
	  Label fatLabel = new Label();
	  fatLabel.setText("Fat: ");
	  
	  TextField fatInput = new TextField();
	  fatInput.setText("0");
	  
	  Label carbLabel = new Label();
	  carbLabel.setText("Carbs: ");
	  
	  TextField carbInput = new TextField();
	  carbInput.setText("0");
	  
	  Label fiberLabel = new Label();
	  fiberLabel.setText("Fiber: ");
	  fiberLabel.setMinHeight(25);
	  
	  TextField fiberInput = new TextField();
	  fiberInput.setMinWidth(200);
	  fiberInput.setMinHeight(25);
	  fiberInput.setText("0");
	  
	  Label proLabel = new Label();
	  proLabel.setText("Protien: ");
	  
	  TextField proInput = new TextField();
	  proInput.setText("0");
	  
	  Button acceptButton = new Button("Accept");
	  Button closeButton = new Button("Close");
	  
	  //button actions
	  closeButton.setOnAction(e -> alertWindow.close());
	  acceptButton.setOnAction(e -> {
		  /*
		  if ((nameInput.getText().compareTo("") == 0) || (idInput.getText().compareTo("") == 0)){
			  getErrorMessage("Add Food Item", "Error: Name and/or ID is required.");
		  } else if ((calInput.getText().compareTo("") == 0) || 
				  (fatInput.getText().compareTo("") == 0) || 
				  (carbInput.getText().compareTo("") == 0) ||
				  (fiberInput.getText().compareTo("") == 0 ||
				  (proInput.getText().compareTo("") == 0))) {
			  getErrorMessage("Add Food Item", "Error: All Nutrients must have a value.");
		  } else {
			  FoodItem food = new FoodItem(idInput.getText(),nameInput.getText());
			  food.addNutrient("calories", Double.parseDouble(calInput.getText()));
			  food.addNutrient("fat", Double.parseDouble(fatInput.getText()));
			  food.addNutrient("carbohydrates", Double.parseDouble(carbInput.getText()));
			  food.addNutrient("fiber", Double.parseDouble(fiberInput.getText()));
			  food.addNutrient("protein", Double.parseDouble(proInput.getText()));
			  foodData.addFoodItem(food);
			  alertWindow.close();
		  }
		  */
		  //declare local variables
		  boolean failedParse = false;
		  double calories = 0.0;
		  double fat = 0.0;
		  double carbohydrates = 0.0;
		  double fiber = 0.0;
		  double protein = 0.0;
		  
		  //check each field for validation
		  if(nameInput.getText().compareTo("") == 0) {
			  nameLabel.setTextFill(Color.RED);
			  nameLabel.setStyle("-fx-font-weight: bold");
			  failedParse = true;
		  } else {
			  nameLabel.setTextFill(Color.BLACK);
			  nameLabel.setStyle("-fx-font-weight: normal");
		  }
		  
		  if(idInput.getText().compareTo("") == 0) {
			  idLabel.setTextFill(Color.RED);
			  idLabel.setStyle("-fx-font-weight: bold");
			  failedParse = true;
		  } else {
			  idLabel.setTextFill(Color.BLACK);
			  idLabel.setStyle("-fx-font-weight: normal");
		  }
		  
		  try {
			  calories = Double.parseDouble(calInput.getText());
			  calLabel.setTextFill(Color.BLACK);
			  calLabel.setStyle("-fx-font-weight: normal");
		  } catch (NumberFormatException f) {
			  calLabel.setTextFill(Color.RED);
			  calLabel.setStyle("-fx-font-weight: bold");
			  failedParse = true;
		  }
		  
		  try {
			  fat = Double.parseDouble(fatInput.getText());
			  fatLabel.setTextFill(Color.BLACK);
			  fatLabel.setStyle("-fx-font-weight: normal");
		  } catch (NumberFormatException f) {
			  fatLabel.setTextFill(Color.RED);
			  fatLabel.setStyle("-fx-font-weight: bold");
			  failedParse = true;
		  }
		  
		  try {
			  carbohydrates = Double.parseDouble(carbInput.getText());
			  carbLabel.setTextFill(Color.BLACK);
			  carbLabel.setStyle("-fx-font-weight: normal");
		  } catch (NumberFormatException f) {
			  carbLabel.setTextFill(Color.RED);
			  carbLabel.setStyle("-fx-font-weight: bold");
			  failedParse = true;
		  }
		  
		  try {
			  fiber = Double.parseDouble(fiberInput.getText());
			  fiberLabel.setTextFill(Color.BLACK);
			  fiberLabel.setStyle("-fx-font-weight: normal");
		  } catch (NumberFormatException f) {
			  fiberLabel.setTextFill(Color.RED);
			  fiberLabel.setStyle("-fx-font-weight: bold");
			  failedParse = true;
		  }
		  
		  try {
			  protein = Double.parseDouble(proInput.getText());
			  proLabel.setTextFill(Color.BLACK);
			  proLabel.setStyle("-fx-font-weight: normal");
		  } catch (NumberFormatException f) {
			  proLabel.setTextFill(Color.RED);
			  proLabel.setStyle("-fx-font-weight: bold");
			  failedParse = true;
		  }
		  
		  //check if validation fails
		  if(failedParse == false) {
			  FoodItem food = new FoodItem(idInput.getText(),nameInput.getText());
			  food.addNutrient("calories", calories);
			  food.addNutrient("fat", fat);
			  food.addNutrient("carbohydrates", carbohydrates);
			  food.addNutrient("fiber", fiber);
			  food.addNutrient("protein", protein);
			  foodData.addFoodItem(food);
			  alertWindow.close(); 
		  }
		  
	  });
	  
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
	  //alertGrid.add(acceptButton, 2, 5);
	  //alertGrid.add(closeButton, 3, 5);
	  
	  HBox buttonMenu = new HBox(8);
	  buttonMenu.getChildren().addAll(acceptButton, closeButton);
	  buttonMenu.setAlignment(Pos.BOTTOM_RIGHT);
	  
	  VBox alertBox = new VBox();
	  alertBox.getChildren().addAll(header, alertGrid, buttonMenu);
	  alertBox.setPadding(new Insets(0,10,10,10));
	  
	  Scene alertBoxScene = new Scene(alertBox);
	  
	  alertWindow.setScene(alertBoxScene);
	  alertWindow.showAndWait();
  }
  
  /**
   * Standardized error message popup
   * 
   * @param title: the popup title
   * @param message: the text that displays within the message
   */
  private void getErrorMessage(String title, String message) {
	  Stage alertWindow = new Stage();
	  alertWindow.initModality(Modality.APPLICATION_MODAL);
	  alertWindow.setTitle(title);
	  alertWindow.setMinHeight(115);
	  alertWindow.setMinWidth(300);
	  
	  
	  Label errorMessage = new Label();
	  errorMessage.setText(message);
	  
	  Button goBackButton = new Button("Go back");
	  
	  VBox alertBox = new VBox(10);
	  alertBox.getChildren().addAll(errorMessage, goBackButton);
	  alertBox.setAlignment(Pos.CENTER);
	  
	  goBackButton.setOnAction(e -> alertWindow.close());
	  
	  Scene alertBoxScene = new Scene(alertBox);
	  
	  alertWindow.setScene(alertBoxScene);
	  alertWindow.showAndWait();
	  
  }
  
  
  /**
   * Popup error message prompting user to save before exiting
   * 
   * @param title name of the popup
   * @param message the main text in the popup 
   */
  private void getSaveMessage(String title, String message) {
	  Stage alertWindow = new Stage();
	  alertWindow.initModality(Modality.APPLICATION_MODAL);
	  alertWindow.setTitle(title);
	  alertWindow.setMinHeight(115);
	  alertWindow.setMinWidth(300);
	  
	  
	  Label errorMessage = new Label();
	  errorMessage.setText(message);
	  
	  Button saveButton = new Button("Save");
	  Button dontSaveButton = new Button("Dont Save");
	  Button goBackButton = new Button("Go back");
	  	  
	  VBox alertBox = new VBox(10);
	  HBox hBox = new HBox(10);
	  hBox.getChildren().addAll(saveButton, dontSaveButton, goBackButton);
	  alertBox.getChildren().addAll(errorMessage, hBox);
	  alertBox.setAlignment(Pos.BOTTOM_RIGHT);
	  
	  //TODO: add save and don't save actions
	  goBackButton.setOnAction(e -> alertWindow.close());
	  
	  Scene alertBoxScene = new Scene(alertBox);
	  
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
    
//    //Test fileChooser
//    fileChooser.setTitle("Open Food List");
//	File selectedFile = fileChooser.showOpenDialog(window);
//	foodData.loadFoodItems(selectedFile.getAbsolutePath());
//    //test adding new food item
//    getAddFoodItem();
    
    meal.addFoodItem(foodData.getAllFoodItems().get(0));
    return meal;
  }
}
