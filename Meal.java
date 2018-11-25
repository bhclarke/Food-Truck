import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Meal extends FoodData {
	
	// a smaller list of foodItemList (from FoodData) that contains the food items making up
	// the meal
	private List<FoodItem> mealItems;
	private FoodItem foodItem = null;
	private HashMap<String, Double> nutrients = null;  // map of all nutrient values for a given food item
	private String nutrientString = null;  // the string containing all nutrient labels and values
	private Set<String> nutrientLabels = null;  // all keys from our nutrient map
	
	public Meal() {
		mealItems = null;
		
	}
	
	/**
	 * Given a list of foodItems in a Meal object, search through individual nutrition BPTrees
	 * for corresponding nutrition value
	 * @return
	 */
	public String analyzeMealData() {
		for (int i = 0; i < mealItems.size(); i++) {
			foodItem = mealItems.get(i);  // look at each food item
			nutrients = foodItem.getNutrients();  // get the nutrient map
			nutrientLabels = nutrients.keySet();  // retrieve each nutrient and append its value to nutrientString
			for (String label:nutrientLabels) {
				nutrientString.concat(label + nutrients.get(label) + "\n");
			}
		}
		return nutrientString;
	}
	
	/**
	 * Removes food item from a Meal object
	 * @param food
	 */
	public void removeFood(FoodItem food) {
		mealItems.remove(food);
	}

}
