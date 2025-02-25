
//import tester.*;
import java.util.function.*;
import java.util.ArrayList;

// represents Huffman
class Huffman {
    ArrayList<ITree> forest;
    // constructor for Huffmaan
    Huffman(ArrayList<String> strs, ArrayList<Integer> frequency) {
        if (strs.size() != frequency.size()) {
            throw new IllegalArgumentException("List of strings and list of frequencies are "
                    + "not of the same length!");
        }

        if (strs.size() < 2) {
            throw new IllegalArgumentException("List of strings must have at least"
                    + "2 elements!");
        }

        for (int i = 0; i < strs.size(); i+=1) {
            

        }

    }

    void sort() {

    }
}

// represents a tree
interface ITree {
    // IForest sort();
    void insertIntoSortedForest(ArrayList<ITree> forest);
    
    boolean greaterVal(ITree otherTree);

    boolean lesserValNode(Node n);

    boolean lesserValLeaf(Leaf l);
}

abstract class ATree implements ITree {
    int value;

    ATree(int value) {
        this.value = value;
    }
    
    public void insertIntoSortedForest(ArrayList<ITree> forest) {
        for (int i = 0; i < forest.size(); i += 1) {
            if (!this.greaterVal(forest.get(i))) {
                forest.insert(i, this);
            }
        }
    }
    
    public boolean greaterVal(ITree otherTree) {
        this.value > otherTree.value;
    }

    public abstract boolean lesserValNode(Node n);
    
    public abstract boolean lesserValLeaf(Leaf l);

}

// represents a tree node
class Node extends ATree {
    ITree left;
    ITree right;

    // constructor for Node
    Node(int value, ITree left, ITree right) {
        super(value);
        this.left = left;
        this.right = right;
    }

    public boolean greaterVal(ITree otherTree) {
        return otherTree.lesserValNode(this);
    }

    public boolean lesserValNode(Node n) {
        return this.combinedValue < n.combinedValue;
    }

    public boolean lesserValLeaf(Leaf l) {
        return this.combinedValue < l.value;
    }

    // 2 3 4 5 want to insert 1
    // forest.get(i=0) = 2
    // 1 < 2 ?
    // true
    // insert where 2 is
    // 1 2 3 4 5
    // 2 2 3 4 5 want to insert 2
    //
    public void insertIntoSortedForest(ArrayList<ITree> forest) {
        for (int i = 0; i < forest.size(); i += 1) {
            if (!this.greaterVal(forest.get(i))) {
                forest.insert(i, this);
            }
        }
    }
}

// represents a tree leaf
class Leaf extends ATree {
    String letter;
    // constructor for leaf
    Leaf(int value, String letter) {
        super(value);
        this.letter = letter;
    }

    public boolean greaterVal(ITree otherTree) {
        return otherTree.lesserValLeaf(this);
    }
    
    public boolean lesserValNode(Node n) {
        return this.value < n.combinedValue;
    }

    public boolean lesserValLeaf(Leaf l) {
        return this.value < l.value;
    }

    public void insertIntoSortedForest(ArrayList<ITree> forest) {
        for (int i = 0; i < forest.size(); i += 1) {
            if (!this.greaterVal(forest.get(i))) {
                forest.insert(i, this);
            }
        }
    }
}
