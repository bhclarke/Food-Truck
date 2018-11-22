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

public class TestBPTree {
  boolean DEBUG = true;
  private static int testsPassed;
  private static int tests;

  static BPTree<Integer, String> tree;

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
    tree = null;
  }

  @Test
  public void test_initializeTree_invalidBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(2);
      fail("Did not throw IllegalArgumentException exception for branching factor less than 3.");
    } catch (IllegalArgumentException e) {
      testsPassed++;
    }
  }

  @Test
  public void test_searchEmptyTree_validComparators_oddBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(3);
      List<String> result = tree.rangeSearch(5, "<=");
      List<String> expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">=");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "==");
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Searching in an empty tree.");
    }
  }

  @Test
  public void test_searchEmptyTree_invalidComparator_oddBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(3);
      List<String> result = tree.rangeSearch(5, "<");
      List<String> expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "=");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "?");
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Searching in an empty tree with invalid comparators.");
    }
  }

  @Test
  public void test_searchOnInsertedKey_validComparators_oddBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(3);
      tree.insert(5, "Brett");
      List<String> result = tree.rangeSearch(5, "<=");
      List<String> expected = new ArrayList<String>();
      expected.add("Brett");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">=");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "==");
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Searching in with the only key in the tree.");
    }
  }

  @Test
  public void test_searchOnInsertedKey_invalidComparator_oddBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(3);
      tree.insert(5, "Brett");
      List<String> result = tree.rangeSearch(5, "<");
      List<String> expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">");
      expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "=");
      expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "?");
      expected = new ArrayList<String>();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e,
          "Searching in with the only key in the tree, but invalid comparator.");
    }
  }

  @Test
  public void test_canInsertAndSearch_DuplicateKeys_oddBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(3);
      tree.insert(5, "Brett");
      tree.insert(5, "Ryan");
      tree.insert(5, "Jerald");
      tree.insert(5, "Riley");
      tree.insert(5, "Jamison");
      int expected = 5;
      int result = tree.rangeSearch(5, "==").size();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "<=").size();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">=").size();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Inserting multiple values with the same key.");
    }
  }

  @Test
  public void test_canInsertAndSearch_DuplicateValues_oddBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(3);
      tree.insert(1, "Brett");
      tree.insert(2, "Brett");
      tree.insert(3, "Brett");
      tree.insert(4, "Brett");
      tree.insert(5, "Brett");
      int result = tree.rangeSearch(4, "<=").size();
      assertTrue(result == 4);
      result = tree.rangeSearch(0, "<=").size();
      assertTrue(result == 0);
      result = tree.rangeSearch(7, "<=").size();
      assertTrue(result == 5);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Inserting multiple keys with the same value.");
    }
  }

  @Test
  public void test_canInsertAndSearch_manyKeys_oddBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(3);
      int numToInsert = 100000;
      for (int i = 0; i < numToInsert; i++) {
        tree.insert(i, "Brett" + i);
      }
      int expected = 5;
      int result = tree.rangeSearch(4, "<=").size();
      assertEquals(expected, result);
      expected = numToInsert-4;
      result = tree.rangeSearch(4, ">=").size();
      assertEquals(expected, result);
      expected = 1;
      result = tree.rangeSearch(4, "==").size();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Inserting 10000 keys and searching.");
    }
  }

  @Test
  public void test_searchEmptyTree_validComparators_evenBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(4);
      List<String> result = tree.rangeSearch(5, "<=");
      List<String> expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">=");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "==");
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Searching in an empty tree.");
    }
  }

  @Test
  public void test_searchEmptyTree_invalidComparator_evenBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(4);
      List<String> result = tree.rangeSearch(5, "<");
      List<String> expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "=");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "?");
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Searching in an empty tree with invalid comparators.");
    }
  }

  @Test
  public void test_searchOnInsertedKey_validComparators_evenBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(4);
      tree.insert(5, "Brett");
      List<String> result = tree.rangeSearch(5, "<=");
      List<String> expected = new ArrayList<String>();
      expected.add("Brett");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">=");
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "==");
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Searching in with the only key in the tree.");
    }
  }

  @Test
  public void test_searchOnInsertedKey_invalidComparator_evenBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(4);
      tree.insert(5, "Brett");
      List<String> result = tree.rangeSearch(5, "<");
      List<String> expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">");
      expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "=");
      expected = new ArrayList<String>();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "?");
      expected = new ArrayList<String>();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e,
          "Searching in with the only key in the tree, but invalid comparator.");
    }
  }

  @Test
  public void test_canInsertAndSearch_DuplicateKeys_evenBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(4);
      tree.insert(5, "Brett");
      tree.insert(5, "Ryan");
      tree.insert(5, "Jerald");
      tree.insert(5, "Riley");
      tree.insert(5, "Jamison");
      int expected = 5;
      int result = tree.rangeSearch(5, "==").size();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, "<=").size();
      assertEquals(expected, result);
      result = tree.rangeSearch(5, ">=").size();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Inserting multiple values with the same key.");
    }
  }

  @Test
  public void test_canInsertAndSearch_DuplicateValues_evenBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(4);
      tree.insert(1, "Brett");
      tree.insert(2, "Brett");
      tree.insert(3, "Brett");
      tree.insert(4, "Brett");
      tree.insert(5, "Brett");
      int result = tree.rangeSearch(4, "<=").size();
      assertTrue(result == 4);
      result = tree.rangeSearch(0, "<=").size();
      assertTrue(result == 0);
      result = tree.rangeSearch(7, "<=").size();
      assertTrue(result == 5);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Inserting multiple keys with the same value.");
    }
  }

  @Test
  public void test_canInsertAndSearch_manyKeys_evenBranchingFactor() {
    try {
      tree = new BPTree<Integer, String>(4);
      int numToInsert = 100000;
      for (int i = 0; i < numToInsert; i++) {
        tree.insert(i, "Brett" + i);
      }
      int expected = 5;
      int result = tree.rangeSearch(4, "<=").size();
      assertEquals(expected, result);
      expected = numToInsert-4;
      result = tree.rangeSearch(4, ">=").size();
      assertEquals(expected, result);
      expected = 1;
      result = tree.rangeSearch(4, "==").size();
      assertEquals(expected, result);
      testsPassed++;
    } catch (Exception e) {
      printUnexpectedException(e, "Inserting 10000 keys and searching.");
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
