import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This class represents the backend for managing all the operations associated with FoodItems
 * 
 * @authors Riley, Brett, Ryan, Jerald, Jamison
 */
public class FoodData implements FoodDataADT<FoodItem> {

  // List of all the food items.
  private List<FoodItem> foodItemList;

  // Map of nutrients and their corresponding index
  private HashMap<String, BPTree<Double, String>> indexes;


  /**
   * Constructor to create food data list
   */
  public FoodData() {

    foodItemList = new ArrayList<FoodItem>();
    indexes = new HashMap<String, BPTree<Double, String>>();

    // Set up indices for nutrients
    String[] allNutrients = {"calories", "fat", "carbohydrate", "fiber", "protein"};
    for (String currentNutrient : allNutrients) {
      indexes.put(currentNutrient, new BPTree<Double, String>(3)); // For now try branching factor
                                                                   // of 3
    }

  }

  /**
   * Loads data from file to the food list
   */
  @Override
  public void loadFoodItems(String filePath) {
    File inputFile = null;
    Scanner input = null;
    String oneLineOfData = null;

    try {
      // Read data from each file line
      inputFile = new File(filePath);
      input = new Scanner(inputFile);
      while (input.hasNextLine()) {
        oneLineOfData = input.nextLine();

        // Handle empty rows
        if (oneLineOfData.length() == 0) {
          continue;
        }

        String[] commaSplit = oneLineOfData.split(",");

        // Handle null ids
        if (commaSplit[0].length() == 0) {
          continue;
        }

        // Add new food item with nutrients
        FoodItem food = new FoodItem(commaSplit[0], commaSplit[1]);
        for (int i = 3; i < commaSplit.length; i = i + 2) {
          double nut = Double.parseDouble(commaSplit[i]);

          // Handle negative nutrition values
          if (nut < 0)
            nut = 0;
          food.addNutrient(commaSplit[i - 1].toLowerCase(), nut);
        }

        // add food item to list
        foodItemList.add(food);
        indexFoodItem(food);

      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Filters food list by substring
   */
  @Override
  public List<FoodItem> filterByName(String substring) {
    List<FoodItem> newList = new ArrayList<FoodItem>();
    for (int i = 0; i < foodItemList.size(); i++) {
      if (foodItemList.get(i).getName().contains(substring))
        newList.add(foodItemList.get(i));
    }
    return newList;
  }

  /**
   * Filters food list by nutrients
   */
  @Override
  public List<FoodItem> filterByNutrients(List<String> inputRules) {
    // Protect the passed rule list
    List<String> rules = new ArrayList<String>();
    rules.addAll(inputRules);

    // Handle cases with no rules passed
    if (rules == null || rules.isEmpty()) {
      return foodItemList;
    }

    // Use the first rule passed to create the base set
    String baseRule = rules.get(0);
    rules.remove(0);
    Set<String> baseIdSet = filterOneNutrient(baseRule);
    if (baseIdSet.isEmpty()) {
      return new ArrayList<FoodItem>();
    }

    // Use a stream to intersect the sets resulting from any remaining rules
    Set<String> resultSet =
        rules.stream().map(rule -> filterOneNutrient(rule)).reduce(baseIdSet, (base, test) -> {
          base.retainAll(test);
          return base;
        });

    // Transform stream back to a list of foodItems
    List<FoodItem> resultList = foodItemList.stream()
        .filter(item -> resultSet.contains(item.getID())).collect(Collectors.toList());

    return resultList;
  }

  /**
   * Add food item to the data list
   */
  @Override
  public void addFoodItem(FoodItem foodItem) {
    foodItemList.add(foodItem);
    indexFoodItem(foodItem);
  }

  /**
   * Returns food items from the list
   */
  @Override
  public List<FoodItem> getAllFoodItems() {
    return foodItemList;
  }


  /**
   * Save food data list to a file
   */
  @Override
  public void saveFoodItems(String filename) {
    File outputFile = null;
    PrintStream writer = null;


    try {
      outputFile = new File(filename);
      writer = new PrintStream(outputFile);

      for (int i = 0; i < foodItemList.size(); i++) {
        // Retrieve food item from list
        FoodItem f = foodItemList.get(i);

        // Write food information to file
        String id = f.getID();
        String name = f.getName();
        writer.println(id + "," + name + ",calories," + f.getNutrientValue("calories") + ",fat,"
            + f.getNutrientValue("fat") + ",carbohydrate," + f.getNutrientValue("carbohydrate")
            + ",fiber," + f.getNutrientValue("fiber") + ",protein,"
            + f.getNutrientValue("protein"));
      }

    } catch (IOException e) {
      System.out.println("WARNING: Some kind of IO error occurred");
    } finally {
      if (writer != null) // if statement checks for null pointer
        writer.close(); // close the file
    }

  }

  /**
   * Add food to nutrient index
   */
  private void indexFoodItem(FoodItem foodItem) {
    String[] allNutrients = {"calories", "fat", "carbohydrate", "fiber", "protein"};

    for (String currentNutrient : allNutrients) {
      if (!indexes.containsKey(currentNutrient)) { // This nutrient doesn't have an index
        continue;
      }
      Double nutrientValue = foodItem.getNutrientValue(currentNutrient);
      if (nutrientValue >= 0) {
        indexes.get(currentNutrient).insert(nutrientValue, foodItem.getID());
      }
    }
    return;
  } // indexFoodItem

  /**
   * Filters food data list by nutrition
   * 
   * @return filter result
   */
  private Set<String> filterOneNutrient(String rule) {
    String current = rule;

    // Specify nutrient
    String nutrient = current.substring(0, current.indexOf(" ")).toLowerCase();
    current = current.substring(current.indexOf(" ") + 1);

    // Specify logic
    String logic = current.substring(0, current.indexOf(" "));
    current = current.substring(current.indexOf(" ") + 1);
    String valueString = current;

    // Convert value string to double
    Double value = null;
    try {
      value = Double.parseDouble(valueString);
    } catch (NumberFormatException f) {
      return new HashSet<String>(); // Invalid rule, return empty set
    }

    BPTree<Double, String> nutrientIndex = indexes.get(nutrient);
    if (nutrientIndex == null || value == null) {
      return new HashSet<String>();
    }

    List<String> indexResult = nutrientIndex.rangeSearch(value, logic);
    if (indexResult == null) {
      return new HashSet<String>();
    }

    // Make resulting list into a set
    Set<String> resultSet = new HashSet<String>();
    resultSet.addAll(indexResult);

    return resultSet;

  } // FilterOneNutrient

}
