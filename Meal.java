import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Meal extends FoodData {

  private FoodItem foodItem = null;
  private HashMap<String, Double> nutrients = null; // map of all nutrient values for a given food
                                                    // item
  private String nutrientString = new String(); // the string containing all nutrient labels and
                                                // values
  private String mealName = new String();
  // private String mealName;
  private HashMap<String, Double> mealNutrients = new HashMap<>();; // separate map for building the
                                                                    // nutrientString

  /**
   * Default constructor -- foodItemList is defined in FoodData
   */
  public Meal() {}

  public Meal(String mealName) {
    this.mealName = mealName;
  }

  /**
   * Given a list of foodItems in a Meal object, adds nutrition values in a long string. The form of
   * the string is Nutrition Label: Nutrition Value TODO: may want to change this to void return
   * type and to clear out nutrientString on each run
   * 
   * @return
   */
  public void analyzeMealData() {
    // start with a new nutrientString mealNutrients to avoid over-appending or over-incrementing
    // this is a temp workaround until conditional is set up in button event handler
    nutrientString = new String();
    mealNutrients = new HashMap<>();
    for (int i = 0; i < super.getAllFoodItems().size(); i++) {
      foodItem = super.getAllFoodItems().get(i); // look at each food item
      nutrients = foodItem.getNutrients(); // get the nutrient map - a new one is created for each
                                           // food item in meal
      // TODO: remove the following line -- this just includes the name of all food items in the
      // meal (for troubleshooting)
      //nutrientString += super.getAllFoodItems().get(i).getName() + "\n";
      for (String label : nutrients.keySet()) {
        if (mealNutrients.containsKey(label)) {
          // if we previously added a nutrient of this (label), retrieve its
          // previous value and increment with the value for the next food item
          mealNutrients.put(label, mealNutrients.get(label) + nutrients.get(label));
        } else {
          // otherwise, just add the value to mealNutrients table
          mealNutrients.put(label, nutrients.get(label));
        }
      }
    }
    // build the sting outside of the above loop so that we display the total of each nutrient value
    // (e.g. total fat instead of the fat content for each food item in meal)
    for (String label : mealNutrients.keySet()) {
      nutrientString += label.toUpperCase() + ": " + mealNutrients.get(label) + "\n";
    }
    // return nutrientString;
  }

  /**
   * Removes food item from a Meal object
   * 
   * @param food
   */
  public void removeFood(FoodItem food) {
    // mealItems.remove(food);
    super.getAllFoodItems().remove(food);
  }

  /**
   * Retrieves the display name for the meal (for ListView when it displays each meal object).
   */
  public String toString() {
    return mealName;
  }

  /**
   * Creates the meal name string. To be called right before adding the meal to the meal list. TODO:
   * Figure out a good naming convention for meals -- right now, it just returns the first 4 chars
   * of each FoodItem in foodItemList
   */
  public void createMealName() {
    for (FoodItem fi : super.getAllFoodItems()) {
      mealName = mealName.concat(fi.getName().substring(0, 4));
    }
  }

  public String getMealName() {
    return this.mealName;
  }

  public String getNutrientString() {
    return nutrientString;
  }
	  
  public Double getCal() {
	  return mealNutrients.get("calories");
  }
	  
  public Double getFat() {
	  return mealNutrients.get("fat");
  }
	  
  public Double getCarb() {
	  return mealNutrients.get("carbohydrate");
  }
	  
  public Double getFiber() {
	  return mealNutrients.get("fiber");
  }
	  
  public Double getProtein() {
	  return mealNutrients.get("protein");
  }

}
