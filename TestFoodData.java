import java.util.ArrayList;
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

public class TestFoodData {
  boolean DEBUG = true;
  private static int testsPassed;
  private static int tests;

  static FoodData fd;

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
    tests++;
  }

  @After
  public void tearDown() throws Exception {
    fd = null;
  }

  /**
  *
  */
 @Test
 public void test_loadFoodItems() {
   //TODO implement
   testsPassed++;
 }

 /**
  *
  */
 @Test
 public void test_filterByName() {
   //TODO implement
   testsPassed++;
 }

 /**
  *
  */
 @Test
 public void test_filterByNutrients() {
   //TODO implement
   testsPassed++;
 }

 /**
  *
  */
 @Test
 public void test_addFoodItem() {
   //TODO implement
   testsPassed++;
 }

 /**
  *
  */
 @Test
 public void test_getAllFoodItems() {
   //TODO implement
   testsPassed++;
 }

 /**
  *
  */
 @Test
 public void test_saveFoodItems() {
   //TODO implement
   testsPassed++;
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
