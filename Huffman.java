import tester.*;
import java.util.ArrayList;
import java.util.*;



// represents Huffman
class Huffman {
    ArrayList<ITree> forest;
    
    // convenience constructor
    Huffman(ArrayList<String> strs, ArrayList<Integer> frequency) {
        if (strs.size() != frequency.size()) {
            throw new IllegalArgumentException("List of strings and list of frequencies are "
                    + "not of the same length!");
        }

        if (strs.size() < 2) {
            throw new IllegalArgumentException("List of strings must have at least"
                    + "2 elements!");
        }
        
        // initiates a forest/ list of trees
        this.forest = new ArrayList<ITree>();
        
        // adds new leaves with the frequency and character from the Huffman constructor
        for (int i = 0; i < strs.size(); i+=1) {
            this.forest.add(new Leaf(frequency.get(i), strs.get(i)));
        }

        // sorts this new list
        this.forest.sort(new CompareITreeValue());
    }

}

// comparator for two trees
class CompareITreeValue implements Comparator<ITree> {
    
  // compares the values of two trees
  public int compare(ITree t1, ITree t2) {
    int difference = t1.calculateVal() - t2.calculateVal();
    if (difference < 0) {
      return -1;
    }
    if (difference > 0) {
      return 1;
    }
    return difference;
  }
}

// represents a tree
interface ITree {
    int calculateVal();
}

// represents a tree node
class Node implements ITree {
    ITree left;
    ITree right;

    // constructor for Node
    Node(ITree left, ITree right) {
        this.left = left;
        this.right = right;
    }
    
    // calculates value of node
    public int calculateVal() {
      return this.left.calculateVal() + this.right.calculateVal();
    }

    
}

// represents a tree leaf
class Leaf implements ITree {
    int value;
    String letter;

    // constructor for leaf
    Leaf(int value, String letter) {
        this.value = value;
        this.letter = letter;
    }
    
    // calculates value of leaf
    public int calculateVal() {
      return this.value;
    }

}
