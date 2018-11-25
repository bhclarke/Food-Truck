import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    
    /**
     * Public constructor
     */
    public FoodData() {
        // TODO : Complete
    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        // TODO : Complete
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        // TODO : Complete
        return null;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
        // TODO : Complete
        return null;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        foodItemList.add(foodItem);
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }
    
    /**
     * Save the list of food items in ascending order by name
     * The format of foodItems.txt is a comma-delimited list like this:
     * hashCode (?), foodItemName, caloriesLabel, caloriesValue, fatLabel, fatValue, carbLabel, carbValue, fiberLabel, fiberValue, proteinLabel, proteinValue
     * 
     * @param filename name of the file where the data needs to be saved 
     */
    public void saveFoodItems(String filename) {
		File inputFile = null;
		Scanner sc = null;
		
		try {
			inputFile = new File(filename);
			sc = new Scanner(inputFile);
			while(sc.hasNextLine()) {
				String name = sc.nextLine();
				//TODO
			}
			sc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		
    }

}
