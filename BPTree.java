import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to many different indexes of a large data
 * set. BPTree objects are created for each type of index needed by the program. BPTrees provide an
 * efficient range search as compared to other types of data structures due to the ability to
 * perform log_m N lookups and linear in-order traversals of the data items.
 * 
 * @author Brett, sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

  // Root of the tree
  private Node root;

  // Branching factor is the number of children nodes
  // for internal nodes of the tree
  private int branchingFactor;


  /**
   * Public constructor
   * 
   * @param branchingFactor
   */
  public BPTree(int branchingFactor) {
    if (branchingFactor <= 2) {
      throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
    }
    this.branchingFactor = branchingFactor;
  }


  /*
   * (non-Javadoc)
   * 
   * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
   */
  @Override
  public void insert(K key, V value) {
    // handle empty tree
    if (root == null) {
      root = new LeafNode();
    }
    root.insert(key, value);
    // handle if root needs to split
    if (root.isOverflow()) {
      Node sibling = root.split();
      InternalNode tempRoot = new InternalNode();
      // get last key of root
      int indexOfKey = root.keys.size() - 1;
      K newKey;
      if (root.getClass() == InternalNode.class) {
        // remove key from root if root is an internal node
        newKey = root.keys.remove(indexOfKey);
      } else {
        newKey = root.keys.get(indexOfKey);
      }
      tempRoot.keys.add(newKey);
      tempRoot.children.add(root);
      tempRoot.children.add(sibling);
      root = tempRoot;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
   */
  @Override
  public List<V> rangeSearch(K key, String comparator) {
    if (!comparator.contentEquals(">=") && !comparator.contentEquals("==")
        && !comparator.contentEquals("<=")) {
      return new ArrayList<V>();
    }
    if (root == null) {
      return new ArrayList<V>();
    }
    return root.rangeSearch(key, comparator);
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    Queue<List<Node>> queue = new LinkedList<List<Node>>();
    queue.add(Arrays.asList(root));
    StringBuilder sb = new StringBuilder();
    while (!queue.isEmpty()) {
      Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
      while (!queue.isEmpty()) {
        List<Node> nodes = queue.remove();
        sb.append('{');
        Iterator<Node> it = nodes.iterator();
        while (it.hasNext()) {
          Node node = it.next();
          sb.append(node.toString());
          if (it.hasNext())
            sb.append(", ");
          if (node instanceof BPTree.InternalNode)
            nextQueue.add(((InternalNode) node).children);
        }
        sb.append('}');
        if (!queue.isEmpty())
          sb.append(", ");
        else {
          sb.append('\n');
        }
      }
      queue = nextQueue;
    }
    return sb.toString();
  }


  /**
   * This abstract class represents any type of node in the tree This class is a super class of the
   * LeafNode and InternalNode types.
   * 
   * @author sapan
   */
  private abstract class Node {

    // List of keys
    List<K> keys;

    /**
     * Package constructor
     */
    Node() {
      keys = new ArrayList<K>();
    }

    /**
     * Inserts key and value in the appropriate leaf node and balances the tree if required by
     * splitting
     * 
     * @param key
     * @param value
     */
    abstract void insert(K key, V value);

    /**
     * Gets the first leaf key of the tree
     * 
     * @return key
     */
    abstract K getFirstLeafKey();

    /**
     * Gets the new sibling created after splitting the node
     * 
     * @return Node
     */
    abstract Node split();

    /*
     * (non-Javadoc)
     * 
     * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
     */
    abstract List<V> rangeSearch(K key, String comparator);

    /**
     * 
     * @return boolean
     */
    abstract boolean isOverflow();

    public String toString() {
      return keys.toString();
    }

  } // End of abstract class Node

  /**
   * This class represents an internal node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations required for internal
   * (non-leaf) nodes.
   * 
   * @author sapan
   */
  private class InternalNode extends Node {

    // List of children nodes
    List<Node> children;

    /**
     * Package constructor
     */
    InternalNode() {
      super();
      children = new ArrayList<Node>();
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#getFirstLeafKey()
     */
    K getFirstLeafKey() {
      return children.get(0).getFirstLeafKey();
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
      int maxChildren = branchingFactor;
      if (children.size() > maxChildren) {
        return true;
      }
      return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
     */
    void insert(K key, V value) {
      // find leaf for insertion
      Node childForInsert = null;
      int indexToInsert = 0;
      for (int i = 0; i < keys.size(); i++) {
        if (key.compareTo(keys.get(i)) <= 0) {
          indexToInsert = i;
          childForInsert = children.get(indexToInsert);
          break;
        }
      }
      if (childForInsert == null) {
        indexToInsert = children.size() - 1;
        childForInsert = children.get(indexToInsert);
      }

      // insert to child
      childForInsert.insert(key, value);

      // split child if necessary
      if (childForInsert.isOverflow()) {
        Node sibling = childForInsert.split();
        // get last key of original node
        int indexOfKey = childForInsert.keys.size() - 1;
        K newKey;
        if (childForInsert.getClass() == InternalNode.class) {
          // remove key from child if child is an internal node
          newKey = childForInsert.keys.remove(indexOfKey);
        } else {
          newKey = childForInsert.keys.get(indexOfKey);
        }
        // add new key to parent and add sibling to the children
        keys.add(indexToInsert, newKey);
        children.add(indexToInsert + 1, sibling);
      }
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#split()
     */
    Node split() {
      // get index we split at
      int index = (int) Math.ceil(children.size() / 2.0);

      // build sibling
      InternalNode sibling = new InternalNode();
      sibling.keys = new ArrayList<K>(keys.subList(index, keys.size()));
      sibling.children = new ArrayList<Node>(children.subList(index, children.size()));

      // update original
      keys = new ArrayList<K>(keys.subList(0, index)); // the last key needs to be used by the
                                                       // parent. The parent is responsible for
                                                       // removing it.
      children = new ArrayList<Node>(children.subList(0, index));

      return (Node) sibling;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
     */
    List<V> rangeSearch(K key, String comparator) {
      if (comparator.equals("<=")) {
        // start at beginning of all leafs and traverse forward until key exceeded
        return children.get(0).rangeSearch(key, comparator);
      } else if (comparator.equals(">=")) {
        // start at end of all leafs and traverse backwards until key exceeded
        return children.get(children.size() - 1).rangeSearch(key, comparator);
      } else {
        // look for child to search from
        for (int i = 0; i < keys.size(); i++) {
          if (key.compareTo(keys.get(i)) <= 0) {
            return children.get(i).rangeSearch(key, comparator);
          }
        }
        return children.get(children.size() - 1).rangeSearch(key, comparator);
      }
    }

  } // End of class InternalNode


  /**
   * This class represents a leaf node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations that required for leaf nodes.
   * 
   * @author sapan
   */
  private class LeafNode extends Node {

    // List of values
    List<V> values;

    // Reference to the next leaf node
    LeafNode next;

    // Reference to the previous leaf node
    LeafNode previous;

    /**
     * Package constructor
     */
    LeafNode() {
      super();
      values = new ArrayList<V>();
      next = null;
      previous = null;
    }


    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#getFirstLeafKey()
     */
    K getFirstLeafKey() {
      return keys.get(0);
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
      // Compute the maximum number of values. Depends on if the leaf node is the root.
      int maxValues = 0;
      if (this == root) {
        maxValues = branchingFactor - 1;
      } else {
        maxValues = branchingFactor;
      }
      // Check if in overflow
      if (values.size() > maxValues) {
        return true;
      }
      return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#insert(Comparable, Object)
     */
    void insert(K key, V value) {
      // check if we first need to split
      int keySize = keys.size();
      Boolean inserted = false;
      // loop over all keys to find where to insert
      for (int i = 0; i < keySize; i++) {
        if (key.compareTo(keys.get(i)) <= 0) {
          keys.add(i, key);
          values.add(i, value);
          inserted = true;
          break;
        }
      }
      if (!inserted) {
        keys.add(key);
        values.add(value);
      }
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#split()
     */
    Node split() {
      // get index we split at
      int index = (int) Math.ceil(values.size() / 2.0);

      // build sibling
      LeafNode sibling = new LeafNode();
      sibling.keys = new ArrayList<K>(keys.subList(index, keys.size()));
      sibling.values = new ArrayList<V>(values.subList(index, values.size()));
      sibling.next = next;
      if (next!=null) {
        sibling.next.previous = sibling;
      }
      sibling.previous = this;

      // update original
      keys = new ArrayList<K>(keys.subList(0, index));
      values = new ArrayList<V>(values.subList(0, index));
      next = sibling;

      return (Node) sibling;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#rangeSearch(Comparable, String)
     */
    List<V> rangeSearch(K key, String comparator) {
      List<V> result = new ArrayList<V>();
      if (comparator.equals("<=")) {
        for (int i = 0; i < keys.size(); i++) {
          if (key.compareTo(keys.get(i)) < 0) {
            return result;
          } else {
            result.add(values.get(i));
          }
        }
        // all keys in leaf are valid, use next leafs to look for more
        LeafNode nextNode = this;
        while (nextNode.next != null) {
          nextNode = nextNode.next;
          for (int i = 0; i < nextNode.keys.size(); i++) {
            if (key.compareTo(nextNode.keys.get(i)) < 0) {
              return result;
            } else {
              result.add(nextNode.values.get(i));
            }
          }
        }
      } else if (comparator.equals(">=")) {
        for (int i = keys.size() - 1; i >= 0; i--) {
          if (key.compareTo(keys.get(i)) > 0) {
            return result;
          } else {
            result.add(values.get(i));
          }
        }
        // all keys in leaf are valid, use previous leafs to look for more
        LeafNode previousNode = this;
        while (previousNode.previous != null) {
          previousNode = previousNode.previous;
          for (int i = previousNode.keys.size() - 1; i >= 0; i--) {
            if (key.compareTo(previousNode.keys.get(i)) > 0) {
              return result;
            } else {
              result.add(previousNode.values.get(i));
            }
          }
        }
      } else {
        for (int i = 0; i < keys.size(); i++) {
          if (key.compareTo(keys.get(i)) < 0) {
            return result;
          } else if (key.compareTo(keys.get(i)) == 0) {
            result.add(values.get(i));
          }
        }
        // have not exceeded search key, possible the key is in next leaf
        LeafNode nextNode = this;
        while (nextNode.next != null) {
          nextNode = nextNode.next;
          for (int i = 0; i < nextNode.keys.size(); i++) {
            if (key.compareTo(nextNode.keys.get(i)) < 0) {
              return result;
            } else if (key.compareTo(nextNode.keys.get(i)) == 0) {
              result.add(nextNode.values.get(i));
            }
          }
        }
      }
      return result;
    }

  } // End of class LeafNode


  /**
   * Contains a basic test scenario for a BPTree instance. It shows a simple example of the use of
   * this class and its related types.
   * 
   * @param args
   */
  public static void main(String[] args) {
    // create empty BPTree with branching factor of 3
    BPTree<Double, Double> bpTree = new BPTree<>(3);

    // create a pseudo random number generator
    Random rnd1 = new Random();

    // some value to add to the BPTree
    Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

    // build an ArrayList of those value and add to BPTree also
    // allows for comparing the contents of the ArrayList
    // against the contents and functionality of the BPTree
    // does not ensure BPTree is implemented correctly
    // just that it functions as a data structure with
    // insert, rangeSearch, and toString() working.
    List<Double> list = new ArrayList<>();
    for (int i = 0; i < 400; i++) {
      Double j = dd[rnd1.nextInt(4)];
      list.add(j);
      bpTree.insert(j, j);
      System.out.println("\n\nTree structure:\n" + bpTree.toString());
    }
    List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
    System.out.println("Filtered values: " + filteredValues.toString());
  }

} // End of class BPTree
