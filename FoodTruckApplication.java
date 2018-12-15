// Icon credits
// open: https://www.iconfinder.com/icons/1608888/folder_open_icon
// save: https://www.iconfinder.com/icons/352084/guardar_save_icon
// add: https://www.iconfinder.com/icons/126583/add_plus_icon
// foodTruck: https://www.thebusinessplanshop.com/fr/blog/logo-food-truck

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
  VBox nutData;
  // Edit Meal Scene
  BorderPane editMealLayout;
  Scene editMealScene;
  TableView<Meal> mealTable;
  ObservableList<FoodItem> foodListView;

  // Edit Rules Scene
  BorderPane editRuleLayout;
  Scene editRuleScene;

  // Backend Data
  FoodData foodData = new FoodData();
  List<Meal> mealData = new ArrayList<Meal>();
  List<String> rulesData = new ArrayList<String>();
  String searchText = "";
  FileChooser fileChooser = new FileChooser();
  
  // Make the TextArea that displays nutrients 
  // accessible from any scene
  TextArea nutrientField = new TextArea();
  
  // Make foodTable accessible from any scene
  TableView<FoodItem> foodTable; // = new TableView<>();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

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
    grid.setAlignment(Pos.CENTER);

    // Define Food and Meal Data
    List<FoodItem> mealList = meal.getAllFoodItems();

    foodListView = FXCollections.observableArrayList();
    ObservableList<FoodItem> mealListView = FXCollections.observableArrayList();
    
    // filter foodListView
    foodListView = foodData.filterByNutrients(rulesData).stream()
        .filter(s -> s.getName().toLowerCase().contains(searchText.toLowerCase()))
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
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
    allFoodNames.setSortType(TableColumn.SortType.ASCENDING);
    allFoodTable.setItems(foodListView);
    allFoodTable.getColumns().add(allFoodNames);
    allFoodTable.getSortOrder().add(allFoodNames);
    allFoodTable.getSelectionModel().selectionModeProperty().set(SelectionMode.MULTIPLE);

    TableView<FoodItem> mealFoodTable = new TableView<>();
    TableColumn<FoodItem, String> mealFoodNames = new TableColumn<FoodItem, String>("Food in Meal");
    mealFoodNames.setMinWidth(200);
    mealFoodNames.setCellValueFactory(new PropertyValueFactory<>("name"));
    mealFoodNames.setSortType(TableColumn.SortType.ASCENDING);
    mealFoodTable.setItems(mealListView);
    mealFoodTable.getColumns().add(mealFoodNames);
    mealFoodTable.getSortOrder().add(mealFoodNames);
    mealFoodTable.getSelectionModel().selectionModeProperty().set(SelectionMode.MULTIPLE);

    // Define Buttons
    Button addButton = new Button(">");
    addButton.setMinWidth(30);

    addButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        Object[] foods = allFoodTable.getSelectionModel().getSelectedItems().toArray();
        for (Object foodItem : foods) {
          foodListView.remove((FoodItem) foodItem);
          mealListView.add((FoodItem) foodItem);
          meal.addFoodItem((FoodItem) foodItem);
          mealFoodTable.sort();
        } ;

      }
    });

    Button removeButton = new Button("<");
    removeButton.setMinWidth(30);

    removeButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        Object[] foods = mealFoodTable.getSelectionModel().getSelectedItems().toArray();
        for (Object foodItem : foods) {
          foodListView.add((FoodItem) foodItem);
          mealListView.remove((FoodItem) foodItem);
          meal.removeFood((FoodItem) foodItem);
          allFoodTable.sort();
        } ;

      }
    });

    VBox toggleButtonBox = new VBox();
    toggleButtonBox.setPadding(new Insets(10, 10, 10, 10));
    toggleButtonBox.setSpacing(30);
    toggleButtonBox.alignmentProperty().set(Pos.CENTER);
    toggleButtonBox.getChildren().addAll(addButton, removeButton);

    // create a text field for displaying nutrient data for each meal
    nutrientField.setPrefRowCount(12);

    Button analyzeMealButton = new Button("Analyze Meal");
    
    //build nutrition information box
        
    if(meal.getCal() != null) {
    	nutData = getNutritionForm("Meal data for " + meal.getMealName(), meal.getCal(), meal.getFat(),
        		meal.getProtein(),meal.getCarb(),meal.getFiber());
      }else {
    	nutData = getNutritionForm("Meal data for " + meal.getMealName(), 0.0, 0.0, 0.0, 0.0, 0.0);
      }
    
    // whenever the Analyze Selected Meal button is clicked, analyze the currently selected meal's
    // nutrients
    analyzeMealButton.setOnAction((event) -> {
      meal.analyzeMealData();
      nutrientField.setText("Meal data for " + meal.getMealName() + "\n");  // show meal name in nutrient display
      nutrientField.appendText(meal.getNutrientString());
      if(meal.getCal() != null) {
      	nutData = getNutritionForm("Meal data for " + meal.getMealName(), meal.getCal(), meal.getFat(),
          		meal.getProtein(),meal.getCarb(),meal.getFiber());
        }else {
      	nutData = getNutritionForm("Meal Data: " + meal.getMealName(), 0.0, 0.0, 0.0, 0.0, 0.0);
        }
      GridPane.setConstraints(nutData, 0, 0, 3, 1);
      grid.getChildren().set(4, nutData);
    });

    // Add all to grid
    GridPane.setConstraints(nutData, 0, 0, 3, 1);
    GridPane.setConstraints(allFoodTable, 0, 1, 1, 1);
    GridPane.setConstraints(toggleButtonBox, 1, 1, 1, 1);
    GridPane.setConstraints(mealFoodTable, 2, 1, 1, 1);
    GridPane.setConstraints(analyzeMealButton, 2, 2, 1, 1, HPos.RIGHT, VPos.CENTER);    
    GridPane.setConstraints(nutData, 0, 0, 3, 1);
    
    grid.getChildren().addAll(allFoodTable, mealFoodTable, toggleButtonBox, analyzeMealButton,
            nutData);
	
    return grid;
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
    foodTable = new TableView<>();
    Label foodListLabel = new Label("Food List");
    foodListLabel.getStyleClass().add("label-tableHeader");

    // Define Food Table
    TableColumn<FoodItem, String> foodNames = new TableColumn<FoodItem, String>("Name");
    foodNames.setCellValueFactory(new PropertyValueFactory<>("name"));
    foodNames.setSortType(TableColumn.SortType.ASCENDING);
    
    // apply filters if set
    ObservableList<FoodItem> foodList = foodData.filterByNutrients(rulesData).stream()
        .filter(s -> s.getName().toLowerCase().contains(searchText.toLowerCase()))
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
    foodTable.setItems(foodList);
    
    // count number of food items
    int counted = foodList.size();
    Label countL = new Label();
    countL.setText("Total: " + counted);
    
    //foodTable.setItems(foodList);
    foodTable.getColumns().add(foodNames);
    foodTable.getSortOrder().add(foodNames);
    foodTable.getSelectionModel().selectionModeProperty().set(SelectionMode.SINGLE);
    foodTable.setColumnResizePolicy(foodTable.CONSTRAINED_RESIZE_POLICY);
    foodTable.setMinWidth(400);

    
    
        
    
    // Define Search Field
    TextField input = new TextField();
    input.setMaxHeight(20);
    input.setMinWidth(200);
    if (searchText.equals("")) {
      input.setPromptText("Search Food Items (Press Enter)");
    }
    else {
      input.setText(searchText);
    }
    input.setFocusTraversable(false);
    input.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        searchText = input.getText();
        ObservableList<FoodItem> temp = foodData.filterByNutrients(rulesData).stream()
            .filter(s -> s.getName().toLowerCase().contains(searchText.toLowerCase()))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        foodTable.setItems(temp); 
        countL.setText("Total: " + temp.size());       
      };
    });
    
    GridPane.setConstraints(countL, 2, 3, 1, 1, HPos.RIGHT, VPos.BOTTOM);

    // Define Buttons
    Button add = new Button("Add Food Item");
    Button rule = new Button("Set Filter Rules");
    add.setOnAction(e -> getAddFoodItem());
    rule.setOnAction(e -> getRulePopup());


    // Add all to grid
    GridPane.setConstraints(foodListLabel, 0, 0, 3, 1);

    GridPane.setConstraints(input, 0, 1, 1, 1, HPos.LEFT, VPos.BOTTOM);
    GridPane.setConstraints(add, 1, 1, 1, 1, HPos.RIGHT, VPos.BOTTOM);
    GridPane.setConstraints(rule, 2, 1, 1, 1, HPos.LEFT, VPos.BOTTOM);

    GridPane.setConstraints(foodTable, 0, 2, 3, 1);

    grid.getChildren().addAll(foodListLabel, foodTable, input, add, rule, countL);
    
    foodTable.getSelectionModel().selectedItemProperty()
	.addListener((obs, oldV, newV) -> {
		if (newV != null) {
			newV.getItemNutrition();
			mealTable.getSelectionModel().clearSelection();
			layout.setCenter(showFoodItemData(newV));
		} else {
			layout.setCenter(getStartCredits());
		}
	});
        
    return grid;
  }
  
  /**
   * Center pane when a food item is selected from the overall food item list
   * @return
   */
  private GridPane showFoodItemData(FoodItem fi) {
	  GridPane foodItemNutritionForm = new GridPane();
	  foodItemNutritionForm.setPadding(new Insets(10, 10, 10, 10));
	  
	  VBox nutData = getNutritionForm("Nutrition Data: " + fi.getName(),fi.getCal(),fi.getFat(),
	    		fi.getCarb(),fi.getFiber(),fi.getProtein());
	  foodItemNutritionForm.add(nutData, 1, 1);
	  return foodItemNutritionForm;
  }

  /**
   * Use a GridPane to display the meal list, similar to how we're displaying the food list.
   * 
   * @return
   */
  private GridPane getMealList() {

    ObservableList<Meal> mealList = FXCollections.observableArrayList();

    // Define grid and settings
    GridPane mealGrid = new GridPane();
    mealGrid.setPadding(new Insets(10, 10, 0, 10));
    mealGrid.setVgap(5);
    mealGrid.setHgap(5);

    // Define Labels
    Label mealGridLabel = new Label("Meal List");
    mealGridLabel.getStyleClass().add("label-tableHeader");

    // Define Meal Table
    mealTable = new TableView<>();
    TableColumn<Meal, String> mealNames = new TableColumn<Meal, String>("Name");
    mealNames.setMinWidth(200);
    mealNames.setCellValueFactory(new PropertyValueFactory<>("mealName"));    
    mealNames.setSortType(TableColumn.SortType.ASCENDING);

    mealTable.setItems(mealList);
    mealTable.getColumns().add(mealNames);
    mealTable.getSortOrder().add(mealNames);
    mealTable.getSelectionModel().selectionModeProperty().set(SelectionMode.SINGLE);
    mealTable.setColumnResizePolicy(mealTable.CONSTRAINED_RESIZE_POLICY);
    mealTable.setMinWidth(400);
    mealTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null) {
        	foodTable.getSelectionModel().clearSelection();
            layout.setCenter(createEditMeal(newSelection));
            // show meal name in nutrient display
            nutrientField.setText("Meal data for " + newSelection.getMealName() + "\n" + newSelection.getNutrientString());
            // update the nutrition data for the old selection so that selecting
            // a new meal and going back to the old meal essentially files whatever changes were made
            if (oldSelection != null) {oldSelection.analyzeMealData();}
          } else {
            layout.setCenter(getStartCredits());
          }

        });

    Button createMealButton = new Button("Create Meal");
    createMealButton.setOnAction(e -> {

      layout.setCenter(getStartCredits());
      // Create a grid pane to store the text field and buttons
      
      GridPane mealCreationGrid = new GridPane(); mealCreationGrid.setHgap(10);
      mealCreationGrid.setVgap(10);
      
      Stage mealCreationStage = new Stage();
      mealCreationStage.initModality(Modality.APPLICATION_MODAL);
      mealCreationStage.setTitle("Create new meal"); mealCreationStage.setMinWidth(400);
      mealCreationStage.setMaxWidth(400); mealCreationStage.setMinHeight(200);
      mealCreationStage.setMaxHeight(200);
      
      TextField mealNameInput = new TextField(); mealNameInput.setMinWidth(200);
      Label mealNameInputLabel = new Label("Meal name: ");
      HBox mealNameAndLabel = new HBox();
      mealNameAndLabel.getChildren().addAll(mealNameInputLabel, mealNameInput);
      mealNameAndLabel.setPadding(new Insets(0, 10, 10, 10));
      mealNameInput.setMinHeight(25);
      
      Button closeMealButton = new Button("Close"); 
      Button acceptMealButton = new Button("Accept"); 
      acceptMealButton.setDisable(true);  // initially disabled -- only enable if there is a meal name
      acceptMealButton.getStyleClass().add("button-affirmative");
      acceptMealButton.setDefaultButton(true);  // binds Enter key to Accept buttton
      closeMealButton.getStyleClass().add("button-negative");
      closeMealButton.setOnAction(closeMealWindowEvent -> mealCreationStage.close());
      
      // add a listener to the TextField so that Accept button
      // is active only when there is Meal name entered in the TextField
      mealNameInput.textProperty().addListener((obs, oldV, newV) -> {
    	  if (newV.isEmpty() == false) {
    		  acceptMealButton.setDisable(false);
    	  } else {
    		  acceptMealButton.setDisable(true);
    	  }
      });
      
      // when the user hits the Accept button, create the meal and update the Meal List
      acceptMealButton.setOnAction(closeMealWindowEvent -> {
    	  mealCreationStage.close();  // close the pop up window
    	  Meal newMeal = new Meal(mealNameInput.getText());
    	  // add newly created meal to the Meal List and
    	  // update the Edit Meal section to allow for editing that new Meal
    	  layout.setCenter(createEditMeal(newMeal));
    	  mealTable.getItems().add(newMeal);
    	  // in Meal List, set selection to the newly created meal
    	  mealTable.sort();
    	  mealTable.getSelectionModel().select(newMeal);
    	  
      });
      
      HBox mealButtonsBox = new HBox();
      mealButtonsBox.getChildren().addAll(acceptMealButton, closeMealButton);
      mealButtonsBox.setPadding(new Insets(10, 10, 10, 10));
      
      // grid has two elements:
      // row 2 = TextField for the meal name
      // row 3 = HBox for the Accept and Cancel buttons
      mealCreationGrid.add(mealNameAndLabel, 2, 2); mealCreationGrid.add(mealButtonsBox, 2, 3);
            
      Scene mealScene = new Scene(mealCreationGrid);
      mealScene.getStylesheets().add("FoodTruckMain.css");
      
      mealCreationStage.setScene(mealScene); mealCreationStage.showAndWait();
      //layout.setCenter(createEditMeal(new Meal())); // when we have the necessary info, create meal
    });

    GridPane.setConstraints(mealGridLabel, 0, 0, 1, 1);
    GridPane.setConstraints(mealTable, 0, 2, 2, 1);
    GridPane.setConstraints(createMealButton, 0, 1, 1, 1);
    mealGrid.getChildren().addAll(mealGridLabel, mealTable, createMealButton);

    return mealGrid;
  }

  /**
   * Creates menus along top of application.
   * 
   * @return VBox containing the menu bar
   */
  private VBox getTopMenu() {
    // create menu and menu bar
    Menu foodMenu = new Menu("File");
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().add(foodMenu);

    // create menu items
    MenuItem loadFoodList = new MenuItem("Open Food List");
    MenuItem saveFoodList = new MenuItem("Save Food List");
    MenuItem addFoodItem = new MenuItem("Add Food Item");
    MenuItem addMeal = new MenuItem("Add Meal");

    // Add icons menu.setGraphic(new ImageView("file:volleyball.png"));
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

    // add menu items to the menu
    foodMenu.getItems().add(loadFoodList);
    foodMenu.getItems().add(saveFoodList);
    foodMenu.getItems().add(addFoodItem);
    foodMenu.getItems().add(addMeal);


    // menu button actions
    addFoodItem.setOnAction(e -> getAddFoodItem());
    addMeal.setOnAction(e -> layout.setCenter(createEditMeal(new Meal())));
    loadFoodList.setOnAction(e -> {
      fileChooser.setTitle("Open Food List");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
      File selectedFile = fileChooser.showOpenDialog(window);
      if (selectedFile != null) {
        foodData.loadFoodItems(selectedFile.getAbsolutePath());
        layout.setLeft(getFoodList());
      }
    });
    saveFoodList.setOnAction(e -> {
      fileChooser.setTitle("Save Food List");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
      File selectedFile = fileChooser.showSaveDialog(window);
      if (selectedFile != null) {
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
      Double fiber, Double protein) {

    GridPane alertGrid = new GridPane();
    alertGrid.setHgap(10);
    alertGrid.setVgap(10);

    Label header = new Label();
    header.setText(name);
    header.setMinHeight(25);
    header.getStyleClass().add("label-tableHeader");
    
    Label spacer = new Label();

    Label calLabel = new Label();
    calLabel.setText("Calories: ");
    calLabel.setMinHeight(25);

    TextField calInput = new TextField();
    calInput.setMinWidth(175);
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
    fiberInput.setMinWidth(175);
    fiberInput.setMinHeight(25);
    fiberInput.setText(Double.toString(fiber));
    fiberInput.setDisable(true);

    Label proLabel = new Label();
    proLabel.setText("Protein: ");

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
    vbox.getChildren().addAll(header,alertGrid,spacer);
    
    return vbox;

  }


  /**
   * Pop-up for adding new food item
   */
  private void getAddFoodItem() {
    layout.setCenter(getStartCredits());
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
    proLabel.setText("Protein: ");

    TextField proInput = new TextField();
    proInput.setText("0");

    Button acceptButton = new Button("Accept");
    acceptButton.setDefaultButton(true);
    Button closeButton = new Button("Close");

    // Button style classes
    acceptButton.getStyleClass().add("button-affirmative");
    closeButton.getStyleClass().add("button-negative");

    // button actions
    closeButton.setOnAction(e -> alertWindow.close());
    acceptButton.setOnAction(e -> {
      // declare local variables
      boolean failedParse = false;
      double calories = 0.0;
      double fat = 0.0;
      double carbohydrates = 0.0;
      double fiber = 0.0;
      double protein = 0.0;

      
      // validate name
      if (nameInput.getText().compareTo("") == 0) {
        nameLabel.setTextFill(Color.RED);
        nameLabel.setStyle("-fx-font-weight: bold");
        failedParse = true;
  		getErrorMessage("Add Food Item","Error: Name must be specified.");
      } 
      else {
        nameLabel.setTextFill(Color.BLACK);
        nameLabel.setStyle("-fx-font-weight: normal");
      }

      // validate ids
      boolean invalidId = false;
      if (idInput.getText().compareTo("") == 0) {
        idLabel.setTextFill(Color.RED);
        idLabel.setStyle("-fx-font-weight: bold");
        failedParse = true;
        invalidId = true;
  		getErrorMessage("Add Food Item","Error: ID must be specified.");
      } else {
        idLabel.setTextFill(Color.BLACK);
        idLabel.setStyle("-fx-font-weight: normal");
      }
      
      for (FoodItem f : foodData.getAllFoodItems()) {
    	  if (f.getID().equals(idInput.getText())) {
    	      idLabel.setTextFill(Color.RED);
    	      idLabel.setStyle("-fx-font-weight: bold");
    		  failedParse = true;
    		  invalidId = true;
      		getErrorMessage("Add Food Item","Error: ID must be unique.");
    	  }
      }

      // validate calories
      try {
        calories = Double.parseDouble(calInput.getText());
        if (calories < 0) {
        	failedParse = true;
        	calLabel.setTextFill(Color.RED);
            calLabel.setStyle("-fx-font-weight: bold");
        }
        
        else {
        	calLabel.setTextFill(Color.BLACK);
            calLabel.setStyle("-fx-font-weight: normal");
        }
      
      } catch (NumberFormatException f) {
        calLabel.setTextFill(Color.RED);
        calLabel.setStyle("-fx-font-weight: bold");
        failedParse = true;
      }

      // validate fat
      try {
        fat = Double.parseDouble(fatInput.getText());
        if (fat < 0) {
        	failedParse = true;
        	fatLabel.setTextFill(Color.RED);
            fatLabel.setStyle("-fx-font-weight: bold");
        }
        
        else {
        	fatLabel.setTextFill(Color.BLACK);
            fatLabel.setStyle("-fx-font-weight: normal");
        }
      
      } catch (NumberFormatException f) {
        fatLabel.setTextFill(Color.RED);
        fatLabel.setStyle("-fx-font-weight: bold");
        failedParse = true;
      }

      // validate carbohydrates
      try {
        carbohydrates = Double.parseDouble(carbInput.getText());
        if (carbohydrates < 0) {
        	failedParse = true;
        	carbLabel.setTextFill(Color.RED);
            carbLabel.setStyle("-fx-font-weight: bold");
        }
        
        else {
        	carbLabel.setTextFill(Color.BLACK);
            carbLabel.setStyle("-fx-font-weight: normal");
        }
        
      } catch (NumberFormatException f) {
        carbLabel.setTextFill(Color.RED);
        carbLabel.setStyle("-fx-font-weight: bold");
        failedParse = true;
      }

      // validate fiber
      try {
        fiber = Double.parseDouble(fiberInput.getText());
        if (fiber < 0) {
        	failedParse = true;
            fiberLabel.setTextFill(Color.RED);
            fiberLabel.setStyle("-fx-font-weight: bold");
        }
        
        else {
            fiberLabel.setTextFill(Color.BLACK);
            fiberLabel.setStyle("-fx-font-weight: normal");
        }
        
      } catch (NumberFormatException f) {
        fiberLabel.setTextFill(Color.RED);
        fiberLabel.setStyle("-fx-font-weight: bold");
        failedParse = true;
      }

      // validate protein
      try {
        protein = Double.parseDouble(proInput.getText());
        if (protein < 0) {
        	failedParse = true;
            proLabel.setTextFill(Color.RED);
            proLabel.setStyle("-fx-font-weight: bold");
        }
        
        else {
            proLabel.setTextFill(Color.BLACK);
            proLabel.setStyle("-fx-font-weight: normal");
        }

      } catch (NumberFormatException f) {
        proLabel.setTextFill(Color.RED);
        proLabel.setStyle("-fx-font-weight: bold");
        failedParse = true;
      }

      // check if validation fails
      if (failedParse == false) {
        FoodItem food = new FoodItem(idInput.getText(), nameInput.getText());
        food.addNutrient("calories", calories);
        food.addNutrient("fat", fat);
        food.addNutrient("carbohydrate", carbohydrates);
        food.addNutrient("fiber", fiber);
        food.addNutrient("protein", protein);
        foodData.addFoodItem(food);
        layout.setLeft(getFoodList());
        alertWindow.close();
      }
      else if (!invalidId) {
    		getErrorMessage("Add Food Item","Error: Nutrition values "
    				+ "must be numeric and greater than zero.");
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
    layout.setCenter(getStartCredits());
    Stage alertWindow = new Stage();
    alertWindow.initModality(Modality.APPLICATION_MODAL);
    alertWindow.setTitle("Set Filter Rule");

    Label prompt = new Label("Add a new rule.");

    ObservableList<String> nutOptions =
        FXCollections.observableArrayList("Calories", "Fat", "Carbohydrate", "Fiber", "Protein");
    ComboBox<String> nutCombo = new ComboBox<String>(nutOptions);
    nutCombo.setPromptText("Nutrient");

    ObservableList<String> logicOptions = FXCollections.observableArrayList(
    	"\u2265", // unicode for >=
        "\u2264", // unicode for <=
        "=");
    ComboBox<String> logicCombo = new ComboBox<String>(logicOptions);
    logicCombo.setPromptText("Logic");

    TextField valueField = new TextField();
    valueField.setPromptText("Value");

    Button addRule = new Button("Add Rule");
    Button removeRule = new Button("Remove Rule");

    ObservableList<String> ruleListObs = 
    		FXCollections.observableArrayList(new ArrayList<String>());
    String current;
	String nut;
	String logic;
	String value;
    
    for (int i = 0; i < rulesData.size();i++) {
    	current = rulesData.get(i);
		nut = current.substring(0, current.indexOf(" "));
		current = current.substring(current.indexOf(" ") + 1);
		logic = current.substring(0, current.indexOf(" "));
		current = current.substring(current.indexOf(" ") + 1);
		value = current;
		if (logic.compareTo(">=") == 0) {		// Requires comparators to be java syntax
			logic = "\u2265"; // unicode for >=
		}else if (logic.compareTo("<=") == 0) {
			logic = "\u2264"; // unicode for <=
		}else  if (logic.compareTo("==") == 0){
			logic = "=";
		}
		
		ruleListObs.add(nut + " " + logic + " " + value);
    }
    
    ListView<String> listView = new ListView<String>(ruleListObs);
    listView.getSelectionModel().clearSelection();	// No default selection
    listView.setMaxHeight(100);

    Button accept = new Button("Accept");
    Button cancel = new Button("Cancel");
    accept.getStyleClass().add("button-affirmative");
    cancel.getStyleClass().add("button-negative");

    HBox ruleHbox = new HBox(8);
    ruleHbox.getChildren().addAll(nutCombo, logicCombo, valueField);

    HBox ruleButtonHbox = new HBox(8);
    ruleButtonHbox.getChildren().addAll(addRule, removeRule);
    ruleButtonHbox.setAlignment(Pos.CENTER);

    HBox buttonHbox = new HBox(8);
    buttonHbox.getChildren().addAll(accept, cancel);
    buttonHbox.setAlignment(Pos.BOTTOM_RIGHT);

    VBox vbox = new VBox(8);
    vbox.getChildren().addAll(prompt, ruleHbox, ruleButtonHbox, listView, buttonHbox);
    vbox.setPadding(new Insets(0,8,8,8));
    
    //button actions
    addRule.setOnAction(e -> {
    	boolean failedParse = false;
    	if (nutCombo.getSelectionModel().isEmpty()) {
    		getErrorMessage("Set Filter Rule","Error: A nutrient must be specified.");
    		failedParse = true;
    	}
    	if(logicCombo.getSelectionModel().isEmpty()) {
    		getErrorMessage("Set Filter Rule","Error: A logic must be specified.");
    		failedParse = true;
    	}
    	try {
    		Double.parseDouble(valueField.getText());
    	} catch(NumberFormatException f) {
    		getErrorMessage("Set Filter Rule", "Error: Value must be a double.");
    		failedParse = true;
    	}
    	if (failedParse == false) {
    		String rule = nutCombo.getValue() + " " + logicCombo.getValue() + " " + 
					valueField.getText();
    		if(ruleListObs.contains(rule) != true) {
    			ruleListObs.add(rule);
    		}
    	}
    });
    removeRule.setOnAction(e -> {
    	int selectedRow = listView.getSelectionModel().getSelectedIndex();
    	if ((ruleListObs.isEmpty() != true) && (selectedRow >= 0)) {
    		ruleListObs.remove(selectedRow);
    	}
    });
    accept.setOnAction(e -> {
    	rulesData.clear();
    	String current2;
    	String nut2;
    	String logic2;
    	String value2;
    	for(int i = 0; i < ruleListObs.size(); i++) {
    		current2 = ruleListObs.get(i);
    		nut2 = current2.substring(0, current2.indexOf(" "));
    		current2 = current2.substring(current2.indexOf(" ") + 1);
    		logic2 = current2.substring(0, current2.indexOf(" "));
    		current2 = current2.substring(current2.indexOf(" ") + 1);
    		value2 = current2;
    		if (logic2.compareTo("\u2265") == 0) {
    			logic2 = ">=";
    		}else if (logic2.compareTo("\u2264") == 0) {
    			logic2 = "<=";
    		}else  if (logic2.compareTo("=") == 0){
    			logic2 = "==";
    		}
    		rulesData.add(nut2 + " " + logic2 + " " + value2);
    	}
    	layout.setLeft(getFoodList());
    	alertWindow.close();
    });
    cancel.setOnAction(e -> alertWindow.close());
    
    Scene alertBoxScene = new Scene(vbox);
    alertBoxScene.getStylesheets().add("FoodTruckMain.css");
    
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
    alertBoxScene.getStylesheets().add("FoodTruckMain.css");

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
    grid.setAlignment(Pos.CENTER);

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
