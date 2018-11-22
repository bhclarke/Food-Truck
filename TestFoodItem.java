import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import org.junit.Before; // setUp method
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.After; // tearDown method
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.rules.Timeout;

public class TestFoodItem {
  boolean DEBUG = false;
  private static int testsPassed;
  private static int tests;

  static FoodItem fi;

  @Rule
  public Timeout globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    testsPassed = 0;
    tests = 0;
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    System.out.println("You passed " + testsPassed + " out of " + tests + " tests.");
  }

  @Before
  public void setUp() throws Exception {
    fi = new FoodItem("1234", "apple");
    tests++;
  }

  @After
  public void tearDown() throws Exception {
    fi = null;
  }


  /**
   *
   */
  @Test
  public void test_getName() {
    try {
      String expected = "apple";
      String result = fi.getName();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Testing getName");
    }
  }

  /**
   *
   */
  @Test
  public void test_getID() {
    try {
      String expected = "1234";
      String result = fi.getID();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Testing getID");
    }
  }

  /**
   *
   */
  @Test
  public void test_getNutrients_noNutrients() {
    try {
      HashMap<String, Double> nutrientMap = fi.getNutrients();
      int expected = 0;
      int result = nutrientMap.size();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Testing getNutrients");
    }
  }

  /**
  *
  */
  @Test
  public void test_addAndGetNutrients_oneNutrient() {
    try {
      fi.addNutrient("calories", 50);
      HashMap<String, Double> nutrientMap = fi.getNutrients();
      
      // check size of nutrientMap
      int expected = 1;
      int result = nutrientMap.size();
      assertEquals(expected, result);
      
      // check correctness of map
      assertTrue(nutrientMap.containsKey("calories"));
      assertEquals(50, nutrientMap.get("calories"), .000001);
      
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Testing getNutrients");
    }
  }
  
  /**
  *
  */
  @Test
  public void test_addAndGetNutrients_manyNutrients() {
    try {
      fi.addNutrient("calories", 50);
      fi.addNutrient("fat", 10.5);
      fi.addNutrient("carbs", 12.1);
      fi.addNutrient("protein", 0.95);
      HashMap<String, Double> nutrientMap = fi.getNutrients();
      
      // check size of nutrientMap
      int expected = 4;
      int result = nutrientMap.size();
      assertEquals(expected, result);
      
      // check correctness of map
      assertTrue(nutrientMap.containsKey("calories"));
      assertEquals(50, nutrientMap.get("calories"), .000001);
      assertTrue(nutrientMap.containsKey("fat"));
      assertEquals(10.5, nutrientMap.get("fat"), .000001);
      assertTrue(nutrientMap.containsKey("carbs"));
      assertEquals(12.1, nutrientMap.get("carbs"), .000001);
      assertTrue(nutrientMap.containsKey("protein"));
      assertEquals(0.95, nutrientMap.get("protein"), .000001);
      
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Testing getNutrients");
    }
  }
  
  /**
  *
  */
  @Test
  public void test_addAndGetNutrients_updateNutrient() {
    try {
      fi.addNutrient("calories", 50);
      fi.addNutrient("fat", 10.5);
      fi.addNutrient("carbs", 12.1);
      fi.addNutrient("protein", 0.95);
      fi.addNutrient("fat", 13.2);
      HashMap<String, Double> nutrientMap = fi.getNutrients();
      
      // check size of nutrientMap
      int expected = 4;
      int result = nutrientMap.size();
      assertEquals(expected, result);
      
      // check correctness of map
      assertTrue(nutrientMap.containsKey("calories"));
      assertEquals(50, nutrientMap.get("calories"), .000001);
      assertTrue(nutrientMap.containsKey("fat"));
      assertEquals(13.2, nutrientMap.get("fat"), .000001);
      assertTrue(nutrientMap.containsKey("carbs"));
      assertEquals(12.1, nutrientMap.get("carbs"), .000001);
      assertTrue(nutrientMap.containsKey("protein"));
      assertEquals(0.95, nutrientMap.get("protein"), .000001);
      
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Testing getNutrients");
    }
  }

  /**
   *
   */
  @Test
  public void test_getNutrientValues_addedNutrients() {
    try {
      // add nutrients
      fi.addNutrient("calories", 50);
      fi.addNutrient("fat", 10.5);
      fi.addNutrient("carbs", 12.1);
      fi.addNutrient("protein", 0.95);
      // check added nutrients
      double expected = 50.0;
      double result = fi.getNutrientValue("calories");
      assertEquals(expected, result, .000001);
      expected = 10.5;
      result = fi.getNutrientValue("fat");
      assertEquals(expected, result, .000001);
      expected = 12.1;
      result = fi.getNutrientValue("fat");
      assertEquals(expected, result, .000001);
      expected = 0.95;
      result = fi.getNutrientValue("fat");
      assertEquals(expected, result, .000001);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Testing getNutrientValue");
    }
  }
  
  /**
  *
  */
 @Test
 public void test_getNutrientValues_notAddedNutrients() {
   try {
     // add nutrients
     fi.addNutrient("calories", 50);
     fi.addNutrient("fat", 10.5);
     fi.addNutrient("carbs", 12.1);
     fi.addNutrient("protein", 0.95);
     // check added nutrient not added
     double expected = 0.0;
     double result = fi.getNutrientValue("iron");
     assertEquals(expected, result, .000001);
     testsPassed++;
   } catch (Exception e) {
     printUnexpectedException(e, "Testing getNutrientValue");
   }
 }

  private void printUnexpectedException(Exception e, String message) {
    printExceptionStack(e);
    fail("Unexpectedly threw " + e.getClass().getName() + ". " + message);
  }

  private void printExceptionStack(Exception e) {
    if (DEBUG)
      e.printStackTrace();
  }
}
