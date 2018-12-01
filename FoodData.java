
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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

    	foodItemList = new ArrayList<FoodItem>();
    	indexes = new HashMap<String, BPTree<Double, FoodItem>>();

    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        File inputFile = null;
        Scanner input = null;
        String oneLineOfData = null;
        
    	try {
            inputFile = new File(filePath);
            input = new Scanner(inputFile);
            while (input.hasNextLine()) {
                oneLineOfData = input.nextLine();
                
                // handle empty rows
                if (oneLineOfData.length()==0) {
                	continue;
                }
                
                String[] commaSplit = oneLineOfData.split(",");
                // handle null ids
                if (commaSplit[0].length() == 0) {
                	continue;
                }
                
                // add new food item with nutrients
                FoodItem food = new FoodItem(commaSplit[0],commaSplit[1]);
                for (int i = 3; i < commaSplit.length; i = i+2) {
                	double nut = Double.parseDouble(commaSplit[i]);
                	food.addNutrient(commaSplit[i-1].toLowerCase(), nut);
                }
                
                foodItemList.add(food);    
                
            }    
    	} catch (Exception e) {
    		//System.out.println(e.getMessage());
    	}
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


	@Override
	public void saveFoodItems(String filename) {
        File outputFile = null;
        PrintStream writer = null;


        try {
                outputFile = new File(filename); 
                writer = new PrintStream(outputFile);
                
                for (int i = 0; i < foodItemList.size(); i++) {
                	FoodItem f = foodItemList.get(i);
                	String id = f.getID();
                	String name = f.getName();
                	writer.println(id + "," + name + 
                			",calories," + f.getNutrientValue("calories") 
                			+ ",fat," + f.getNutrientValue("fat") 
                			+ ",carbohydrates," + f.getNutrientValue("carbohydrates") 
                			+ "fiber," + f.getNutrientValue("fiber")
                			+ ",protein," + f.getNutrientValue("protein"));
                }

        } catch (IOException e) {  
                //System.out.println("WARNING: Some kind of IO error occurred");
        } finally {
                if (writer != null) // if statement checks for null pointer
                        writer.close();  // close the file
        } 
		
	}
	
	public static void main(String[] args) {
		FoodData d = new FoodData();
		d.loadFoodItems("foodItems.txt");
		d.saveFoodItems("newFile.txt");
	}

}
