import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Meal extends FoodData {
	
	private FoodItem foodItem = null;
	private HashMap<String, Double> nutrients = null;  // map of all nutrient values for a given food item
	private String nutrientString = new String();  // the string containing all nutrient labels and values
	private Set<String> nutrientLabels = null;  // all keys from our nutrient map
	private String mealName = new String();
	
	/**
	 * Default constructor -- foodItemList is defined in FoodData
	 */
	public Meal() {
	}
	
	/**
	 * Given a list of foodItems in a Meal object, search through individual nutrition BPTrees
	 * for corresponding nutrition value
	 * @return
	 */
	public String analyzeMealData() {		
		for (int i = 0; i < super.getAllFoodItems().size(); i++) {
			foodItem = super.getAllFoodItems().get(i);  // look at each food item
			nutrients = foodItem.getNutrients();  // get the nutrient map
			nutrientLabels = nutrients.keySet();  // retrieve each nutrient and append its value to nutrientString
			for (String label:nutrientLabels) {
				nutrientString += label.toUpperCase() + ": " + nutrients.get(label) + "\n";
			}
			System.out.println("This is nutrient string: " + nutrientString);
		}
		return nutrientString;
	}
	
	/**
	 * Removes food item from a Meal object
	 * @param food
	 */
	public void removeFood(FoodItem food) {
		//mealItems.remove(food);
		super.getAllFoodItems().remove(food);
	}
	
	/**
	 * Retrieves a display name for the meal
	 * TODO: Figure out a good naming convention for meals -- right now, it just returns the first 4 chars of each FoodItem in foodItemList
	 */
	public String getMealName() {
		for (FoodItem fi : super.getAllFoodItems()) {
			mealName = mealName.concat(fi.getName().substring(0, 4));
		}
		return mealName;
	}

}
