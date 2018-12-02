// Icon credits
// open: https://www.iconfinder.com/icons/1608888/folder_open_icon
// save: https://www.iconfinder.com/icons/352084/guardar_save_icon
// add: https://www.iconfinder.com/icons/126583/add_plus_icon

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
  BorderPane layout;
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
    // get starting data
    foodData.loadFoodItems("foodItems.csv");
    
    // Set Application Title
    window = primaryStage;
    window.setTitle("Food Truck");

    // Create initial layout
    createStart();

    // set scene
    startScene = new Scene(layout, 1600, 900);
    startScene.getStylesheets().add("FoodTruckMain.css");
    window.setScene(startScene);

    // show Application
    window.show();

  }

  /**
   * Create layout for the initial start of the application
   * 
   * @return BorderPane. You can change the return type.
   */
  private void createStart() {
    layout = new BorderPane();
    // Set Menu
    layout.setTop(getTopMenu());
    // Set up application content
    layout.setLeft(getFoodList());
    layout.setRight(getMealList());
    layout.setCenter(getStartCredits());
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

    // Define Food and Meal Data 
    List<FoodItem> mealList = meal.getAllFoodItems();
    List<FoodItem> foodList = foodData.getAllFoodItems();

    ObservableList<FoodItem> foodListView = FXCollections.observableArrayList();
    ObservableList<FoodItem> mealListView = FXCollections.observableArrayList();
    for (FoodItem foodItem : foodList) {
      foodListView.add(foodItem);
    }
    for (FoodItem foodItem : mealList) {
      foodListView.remove(foodItem);
      mealListView.add(foodItem);
    }

    // Put data into tables
    TableView<FoodItem> allFoodTable = new TableView<>();
    TableColumn<FoodItem, String> allFoodNames =
        new TableColumn<FoodItem, String>("Available Food");
    allFoodNames.setMinWidth(200);
    allFoodNames.setCellValueFactory(new PropertyValueFactory<>("name"));
    allFoodTable.setItems(foodListView);
    allFoodTable.getColumns().add(allFoodNames);
    allFoodTable.getSelectionModel().selectionModeProperty().set(SelectionMode.MULTIPLE);

    TableView<FoodItem> mealFoodTable = new TableView<>();
    TableColumn<FoodItem, String> mealFoodNames = new TableColumn<FoodItem, String>("Food in Meal");
    mealFoodNames.setMinWidth(200);
    mealFoodNames.setCellValueFactory(new PropertyValueFactory<>("name"));
    mealFoodTable.setItems(mealListView);
    mealFoodTable.getColumns().add(mealFoodNames);
    mealFoodTable.getSelectionModel().selectionModeProperty().set(SelectionMode.MULTIPLE);

    // Define Buttons
    Button addButton = new Button(">");
    addButton.setMinWidth(25);

    addButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        Object[] foods = allFoodTable.getSelectionModel().getSelectedItems().toArray();
        for (Object foodItem : foods) {
          foodListView.remove((FoodItem) foodItem);
          mealListView.add((FoodItem) foodItem);
          meal.addFoodItem((FoodItem) foodItem);
        } ;

      }
    });

    Button removeButton = new Button("<");
    removeButton.setMinWidth(25);

    removeButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        Object[] foods = mealFoodTable.getSelectionModel().getSelectedItems().toArray();
        for (Object foodItem : foods) {
          foodListView.add((FoodItem) foodItem);
          mealListView.remove((FoodItem) foodItem);
          meal.removeFood((FoodItem) foodItem);
        } ;

      }
    });

    VBox toggleButtonBox = new VBox();
    toggleButtonBox.setPadding(new Insets(10, 10, 10, 10));
    toggleButtonBox.setSpacing(30);
    toggleButtonBox.alignmentProperty().set(Pos.CENTER);
    toggleButtonBox.getChildren().addAll(addButton, removeButton);
    
    // create a text field for displaying nutrient data for each meal
    TextArea nutrientField = new TextArea();
    nutrientField.setPrefRowCount(12);
    
    // an alternative to TextArea is discrete fields to show each nutrient value
    // TODO: set this up
    
    Button analyzeMealButton = new Button("Analyze Meal");
    // whenever the Analyze Selected Meal button is clicked, analyze the currently selected meal's nutrients
    analyzeMealButton.setOnAction((event) -> {
        meal.analyzeMealData();
        nutrientField.setText(meal.getNutrientString());
    });     

    // Add all to grid
    GridPane.setConstraints(allFoodTable, 0, 0, 1, 1);
    GridPane.setConstraints(toggleButtonBox, 1, 0, 1, 1);
    GridPane.setConstraints(mealFoodTable, 2, 0, 1, 1);
    GridPane.setConstraints(analyzeMealButton, 2, 1, 1, 1, HPos.RIGHT, VPos.CENTER);
    GridPane.setConstraints(nutrientField, 0, 2, 3, 1);

    grid.getChildren().addAll(allFoodTable, mealFoodTable, toggleButtonBox, analyzeMealButton, nutrientField);

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
    grid.setVgap(5);
    grid.setHgap(5);


    // Define Labels
    Label foodListLabel = new Label("Food List");
    foodListLabel.getStyleClass().add("label-tableHeader");

    // Define Food Table
    ObservableList<FoodItem> foodList = FXCollections.observableArrayList();
    for (FoodItem fi : foodData.getAllFoodItems()) {
      foodList.add(fi);
    }

    TableView<FoodItem> foodTable = new TableView<>();
    TableColumn<FoodItem, String> foodNames = new TableColumn<FoodItem, String>("Name");
    foodNames.setCellValueFactory(new PropertyValueFactory<>("name"));
    
    foodTable.setItems(foodList);
    foodTable.getColumns().add(foodNames);
    foodTable.getSelectionModel().selectionModeProperty().set(SelectionMode.SINGLE);
    foodTable.setColumnResizePolicy(foodTable.CONSTRAINED_RESIZE_POLICY);
    foodTable.setMinWidth(400);

    // Define Search Field
    TextField input = new TextField();
    input.setMaxHeight(20);
    input.setMinWidth(200);
    input.setPromptText("Search Food Items (Press Enter)");
    input.setFocusTraversable(false);
    input.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
          ObservableList<FoodItem> temp = foodData.getAllFoodItems().stream()
              .filter(s -> s.getName().toLowerCase().contains(input.getText().toLowerCase()))
              .collect(Collectors.toCollection(FXCollections::observableArrayList));
          
          foodTable.setItems(temp); // TODO does this replace or append?
        };
    });
    
    // Define Buttons
    Button add = new Button("Add Food Item");
    Button rule = new Button("Set Filter Rules");
    add.setOnAction(e -> getAddFoodItem());
    rule.setOnAction(e -> getRulePopup());
    
	  int counted = foodData.getAllFoodItems().size();
    String count = "Total: " + counted;
    Label countL = new Label(count);
    GridPane.setConstraints(countL, 2, 3, 1, 1, HPos.RIGHT, VPos.BOTTOM);

    // Add all to grid
    GridPane.setConstraints(foodListLabel, 0, 0, 3, 1);
    
    GridPane.setConstraints(input, 0, 1, 1, 1, HPos.LEFT, VPos.BOTTOM);
    GridPane.setConstraints(add, 1, 1, 1, 1, HPos.RIGHT, VPos.BOTTOM);
    GridPane.setConstraints(rule, 2, 1, 1, 1, HPos.LEFT, VPos.BOTTOM);
    
    GridPane.setConstraints(foodTable, 0, 2, 3, 1);
    
    grid.getChildren().addAll(foodListLabel, foodTable, input, add, rule, countL);

    return grid;
  }
    
  /**
   * Use a GridPane to display the meal list, similar to how we're displaying the food list.
   * @return
   */
  private GridPane getMealList() {
    
    // TODO: remove mock data // begin mock data
    ObservableList<Meal> mealList = FXCollections.observableArrayList();
    foodData.loadFoodItems("foodItems.csv");
    Meal meal1 = new Meal();
    meal1.addFoodItem(foodData.getAllFoodItems().get(0));
    meal1.addFoodItem(foodData.getAllFoodItems().get(2));
    Meal meal2 = new Meal();
    meal2.addFoodItem(foodData.getAllFoodItems().get(5));
    meal2.addFoodItem(foodData.getAllFoodItems().get(6));

    // need the following two lines in order to avoid duplicate meal names
    meal1.createMealName();
    meal2.createMealName();
    mealList.addAll(meal1,meal2);

    // Define grid and settings
    GridPane mealGrid = new GridPane();
    mealGrid.setPadding(new Insets(10, 10, 0, 10));
    mealGrid.setVgap(5);
    mealGrid.setHgap(5);

    // Define Labels
    Label mealGridLabel = new Label("Meal list");
    mealGridLabel.getStyleClass().add("label-tableHeader");
    
    // Define Meal Table
    TableView<Meal> mealTable = new TableView<>();
    TableColumn<Meal, String> mealNames = new TableColumn<Meal, String>("Name");
    mealNames.setMinWidth(200);
    mealNames.setCellValueFactory(new PropertyValueFactory<>("mealName"));

    mealTable.setItems(mealList);
    mealTable.getColumns().add(mealNames);
    mealTable.getSelectionModel().selectionModeProperty().set(SelectionMode.SINGLE);
    mealTable.setColumnResizePolicy(mealTable.CONSTRAINED_RESIZE_POLICY);
    mealTable.setMinWidth(400);
    mealTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection!= null) {
        layout.setCenter(createEditMeal(newSelection));
      } else {
        layout.setCenter(getStartCredits());
      }
      
  });

    GridPane.setConstraints(mealGridLabel, 0, 0, 1, 1);
    GridPane.setConstraints(mealTable, 0, 2, 2, 1);
    mealGrid.getChildren().addAll(mealGridLabel, mealTable);

    return mealGrid;
  }

  /**
   * Creates menus along top of application.
   * 
   * @return VBox containing the menu bar
   */
  private VBox getTopMenu() {
	  //create menu and menu bar
	  Menu foodMenu = new Menu("File");
	  MenuBar menuBar = new MenuBar();
	  menuBar.getMenus().add(foodMenu);
	  
	  //create menu items
	  MenuItem loadFoodList = new MenuItem("Open Food List");
	  MenuItem saveFoodList = new MenuItem("Save Food List");
	  MenuItem addFoodItem = new MenuItem("Add Food Item");
	  MenuItem addMeal = new MenuItem("Add Meal"); 
	  
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
	  
	  ImageView addMealImg = new ImageView("file:add.png");
	  addMealImg.setFitHeight(15);
	  addMealImg.setFitWidth(15);
	  
	  loadFoodList.setGraphic(loadImg);
	  saveFoodList.setGraphic(saveImg);
	  addFoodItem.setGraphic(addImg);
	  addMeal.setGraphic(addMealImg);
	  
	  //add menu items to the menu
	  foodMenu.getItems().add(loadFoodList);
	  foodMenu.getItems().add(saveFoodList);
	  foodMenu.getItems().add(addFoodItem);
	  foodMenu.getItems().add(addMeal);
	  
	  
	  //menu button actions
	  addFoodItem.setOnAction(e -> getAddFoodItem());
	  loadFoodList.setOnAction(e -> {
		  fileChooser.setTitle("Open Food List");
		  fileChooser.getExtensionFilters().add(
				  new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		  fileChooser.getExtensionFilters().add(
				  new FileChooser.ExtensionFilter("Text files", "*.txt"));
		  File selectedFile = fileChooser.showOpenDialog(window);
		  if (selectedFile != null) {
			  foodData.loadFoodItems(selectedFile.getAbsolutePath());
		  }
		  });
	  saveFoodList.setOnAction(e -> {
		  fileChooser.setTitle("Save Food List");
		  fileChooser.getExtensionFilters().add(
				  new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		  fileChooser.getExtensionFilters().add(
				  new FileChooser.ExtensionFilter("Text files", "*.txt"));
		  File selectedFile = fileChooser.showSaveDialog(window);
		  if(selectedFile != null) {
			  foodData.saveFoodItems(selectedFile.getAbsolutePath());
		  }
		  });
	  
	  
	  VBox menuBarVBox = new VBox(menuBar);
	  
      return menuBarVBox;
  }
  /**
   * Generate nutrition grid for food item or a meal
   * 
   * @param name
   * @param calories
   * @param fat
   * @param carbohydrates
   * @param protein
   */
  private VBox getNutritionForm(String name, Double calories, Double fat, Double carbohydrate, 
		  Double fiber, Double protein){

	  GridPane alertGrid = new GridPane();
	  alertGrid.setHgap(10);
	  alertGrid.setVgap(10);
	    
	  Label header = new Label();
	  header.setText(name);
	  header.setMinHeight(25);

	  Label calLabel = new Label();
	  calLabel.setText("Calories: ");
	  calLabel.setMinHeight(25);

	  TextField calInput = new TextField();
	  calInput.setMinWidth(200);
	  calInput.setMinHeight(25);
	  calInput.setText(Double.toString(calories));
	  calInput.setDisable(true);
	  
	  Label fatLabel = new Label();
	  fatLabel.setText("Fat: ");
	  
	  TextField fatInput = new TextField();
	  fatInput.setText(Double.toString(fat));
	  fatInput.setDisable(true);
	  
	  Label carbLabel = new Label();
	  carbLabel.setText("Carbs: ");

	  TextField carbInput = new TextField();
	  carbInput.setText(Double.toString(carbohydrate));
	  carbInput.setDisable(true);
	  
	  Label fiberLabel = new Label();
	  fiberLabel.setText("Fiber: ");
	  fiberLabel.setMinHeight(25);
	  
	  TextField fiberInput = new TextField();
	  fiberInput.setMinWidth(200);
	  fiberInput.setMinHeight(25);
	  fiberInput.setText(Double.toString(fiber));
	  fiberInput.setDisable(true);
	  
	  Label proLabel = new Label();
	  proLabel.setText("Protien: ");

	  TextField proInput = new TextField();
	  proInput.setText(Double.toString(protein));
	  proInput.setDisable(true);
	  
	  alertGrid.add(calLabel, 0, 0);
	  alertGrid.add(calInput, 1, 0);
	  alertGrid.add(fatLabel, 0, 1);
	  alertGrid.add(fatInput, 1, 1);
	  alertGrid.add(carbLabel, 0, 2);
	  alertGrid.add(carbInput, 1, 2);
	  alertGrid.add(fiberLabel, 2, 0);
	  alertGrid.add(fiberInput, 3, 0);
	  alertGrid.add(proLabel, 2, 1);
	  alertGrid.add(proInput, 3, 1);
	  
	  VBox vbox = new VBox();
	  return vbox;
	  
  }
  
  
  /**
   * Pop-up for adding new food item
   */
  private void getAddFoodItem() {
    // TODO
    // set up window
    Stage alertWindow = new Stage();
    alertWindow.initModality(Modality.APPLICATION_MODAL);
    alertWindow.setTitle("Add Food Item");
    alertWindow.setMinWidth(570);
    alertWindow.setMaxWidth(570);
    alertWindow.setMinHeight(290);
    alertWindow.setMaxHeight(290);

    // set up grid
    GridPane alertGrid = new GridPane();
    alertGrid.setHgap(10);
    alertGrid.setVgap(10);
    // Scene alertBoxScene = new Scene(alertGrid);

    // grid elements
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
    calLabel.setMinHeight(25);

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
    
    // Button style classes
    acceptButton.getStyleClass().add("button-affirmative");
    closeButton.getStyleClass().add("button-negative");

  //button actions
	  closeButton.setOnAction(e -> alertWindow.close());
	  acceptButton.setOnAction(e -> {
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

    // build grid
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

    HBox buttonMenu = new HBox(8);
    buttonMenu.getChildren().addAll(acceptButton, closeButton);
    buttonMenu.setAlignment(Pos.BOTTOM_RIGHT);

    VBox alertBox = new VBox();
    alertBox.getChildren().addAll(header, alertGrid, buttonMenu);
    alertBox.setPadding(new Insets(0, 10, 10, 10));

    Scene alertBoxScene = new Scene(alertBox);
    alertBoxScene.getStylesheets().add("FoodTruckMain.css");

    alertWindow.setScene(alertBoxScene);
    alertWindow.showAndWait();
  }
  
  
  /**
   * Popup for rule editor
   */
  private void getRulePopup() {
	  Stage alertWindow = new Stage();
	  alertWindow.initModality(Modality.APPLICATION_MODAL);
	  alertWindow.setTitle("Set Filter Rule");
	  
	  List<String> ruleList = new ArrayList<String>();
	  
	  Label prompt = new Label("Add a new rule.");
	  
	  ObservableList<String> nutOptions = 
			    FXCollections.observableArrayList(
			        "Calories",
			        "Fat",
			        "Carbohydrates",
			        "Fiber",
			        "Protein"
			    );
	  ComboBox<String> nutCombo = new ComboBox(nutOptions);
	  nutCombo.setPromptText("Nutrient");
	  
	  ObservableList<String> logicOptions = 
			    FXCollections.observableArrayList(
			    	"\u2265", // unicode for >=
			    	"\u2264", // unicode for <=
			    	"="
			    );
	  ComboBox<String> logicCombo = new ComboBox(logicOptions);
	  logicCombo.setPromptText("Logic");
	  
	  TextField valueField = new TextField();
	  valueField.setPromptText("Value");
	  
	  Button addRule = new Button("Add Rule");
	  Button removeRule = new Button("Remove Rule");
	  
	  ObservableList<String> ruleListObs = FXCollections.observableArrayList(ruleList);
	  ListView<String> listView = new ListView<String>(ruleListObs);
	  listView.setMaxHeight(100);
	  
	  Button accept = new Button("Accept");
	  Button cancel = new Button("Cancel");
	  cancel.setOnAction(e -> alertWindow.close());
	  
	  HBox ruleHbox = new HBox(8);
	  ruleHbox.getChildren().addAll(nutCombo,logicCombo,valueField);
	  
	  HBox ruleButtonHbox = new HBox(8);
	  ruleButtonHbox.getChildren().addAll(addRule,removeRule);
	  ruleButtonHbox.setAlignment(Pos.CENTER);
	  
	  HBox buttonHbox = new HBox(8);
	  buttonHbox.getChildren().addAll(accept,cancel);
	  buttonHbox.setAlignment(Pos.BOTTOM_RIGHT);
	  
	  VBox vbox= new VBox();
	  vbox.getChildren().addAll(prompt, ruleHbox, ruleButtonHbox, listView, buttonHbox);
	  
	  Scene alertBoxScene = new Scene(vbox);
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
	  Button dontSaveButton = new Button("Don't Save");
	  	  
	  VBox alertBox = new VBox(10);
	  HBox hBox = new HBox(10);
	  
	  Label spacer = new Label();
	  errorMessage.setAlignment(Pos.CENTER);
	  hBox.getChildren().addAll(saveButton, dontSaveButton);
	  alertBox.getChildren().addAll(errorMessage, spacer, hBox);
	  alertBox.setAlignment(Pos.CENTER);
	  hBox.setAlignment(Pos.BOTTOM_RIGHT);
	  
	  //TODO: add save and don't save actions
	  dontSaveButton.setOnAction(e -> alertWindow.close());
	  
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
    grid.setPadding(new Insets(50, 50, 200, 50));
    grid.setVgap(8);
    grid.setHgap(10);

    // import logo and add to grid
    String imagePath = "file:food-truck-logo.png";
    Image image = new Image(imagePath);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(350);
    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);
    GridPane.setConstraints(imageView, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    
    // create text and add to grid
    Label appCredits =
        new Label("By: Brett Clarke, Ryan Keil, Jerald Kuan, Riley Olson, and Jamison Wickman");
    appCredits.setWrapText(true);
    GridPane.setConstraints(appCredits, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);

    grid.getChildren().addAll(imageView, appCredits);

    return grid;
  }
}
