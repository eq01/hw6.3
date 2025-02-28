import tester.*;
import java.util.ArrayList;
import java.util.*;

// NOTE: add effect statements!

class ArrayListUtils {
  <T> ArrayList<T> append(ArrayList<T> l1, ArrayList<T> l2) {
    ArrayList<T> appendedList = new ArrayList<T>();
    
    for (int i = 0; i < l1.size(); i = i + 1) {
      appendedList.add(l1.get(i));
    }
    
    for (int i = 0; i < l2.size(); i = i + 1) {
      appendedList.add(l2.get(i));
    }
    
    return appendedList;
  }
}

// represents Huffman
class Huffman {
    ArrayList<ITree> forest;
    ArrayList<String> alphabet;
    
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
        // keep copy of alphabet
        this.alphabet = strs;
        // initiates a forest/ list of trees
        this.forest = new ArrayList<ITree>();
        
        // adds new leaves with the frequency and character from the Huffman constructor
        for (int i = 0; i < strs.size(); i+=1) {
            this.forest.add(new Leaf(frequency.get(i), strs.get(i)));
        }

        // sorts this new list
        this.forest.sort(new CompareITreeValue()); 

        // create one big tree
        this.combineSmallestTrees();    
    }

    // creates new nodes starting with the smallest frequencies
    // NOTE: add termination statement here!
    void combineSmallestTrees() {
      while (this.forest.size() >= 2) {
        ITree firstTree = this.forest.get(0);
        ITree secondTree = this.forest.get(1);
        ITree combinedNode = new Node(firstTree, secondTree);
        this.forest.set(0, combinedNode);
        this.forest.remove(1);
        this.forest.sort(new CompareITreeValue());
      }
    }

    //
    ArrayList<Boolean> encode(String toEncode) {
      ArrayListUtils utils = new ArrayListUtils();
      ArrayList<Boolean> encodedExpr = new ArrayList<Boolean>();
      ITree rootNode = this.forest.get(0);
      

      for (int i = 0; i < toEncode.length(); i = i + 1) {
        System.out.println("PARAMETERS: " + toEncode.substring(i, i+1) + this.alphabet);
        ArrayList<Boolean> encodedLetter = 
            rootNode.findPath(
                new ArrayList<Boolean>(), toEncode.substring(i, i+1), 
                this.alphabet);
        System.out.println(toEncode.substring(i, i+1)
            + ": " + encodedLetter);
        encodedExpr = utils.append(encodedExpr, encodedLetter);
        
      }
      return encodedExpr;
    }
    
    String decode(ArrayList<Boolean> encodedStr) {
      ITree rootNode = this.forest.get(0);
      if (encodedStr.size() == 0) {
        return "";
      }
      return rootNode.decodeTree(rootNode, encodedStr);
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
    // calculates value of ITree
    int calculateVal();
    ArrayList<Boolean> findPath(ArrayList<Boolean> accumPath, String letter, ArrayList<String> alphabet);
    String decodeTree(ITree rootNode, ArrayList<Boolean> encodedStr);
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

    public ArrayList<Boolean> findPath(ArrayList<Boolean> accumPath, String letter, ArrayList<String> alphabet) {
      // NOTE: check if leaf is in alphabet at all here instead of in huffman!
      System.out.println("Current Node: " + this.calculateVal());
      if (!alphabet.contains(letter)) {
        throw new IllegalArgumentException("Tried to encode " + letter +
            " but that is not part of the language.");
      }
      
      // see if there is potential path from left side to leaf
      ArrayList<Boolean> leftPath = new ArrayList<Boolean>(accumPath);
      leftPath.add(false);
      ArrayList<Boolean> rightPath = new ArrayList<Boolean>(accumPath);
      rightPath.add(true);
      ArrayList<Boolean> leftPathAttempt = 
          this.left.findPath(leftPath, letter, alphabet);
      ArrayList<Boolean> rightPathAttempt = 
          this.right.findPath(rightPath, letter, alphabet);
      
      if (leftPathAttempt.size() == 0) {
        return rightPathAttempt;
      }
      return leftPathAttempt;
    }
    
    public String decodeTree(ITree rootNode, ArrayList<Boolean> encodedStr) {
      if (encodedStr.size() == 0) {
        return "?";
      }
      
      System.out.println(encodedStr);

      
      if (encodedStr.get(0)) {
        System.out.println(encodedStr.get(0));
        encodedStr.remove(0);
        System.out.println(encodedStr);
        return this.right.decodeTree(rootNode, encodedStr);
      }
      System.out.println(encodedStr.get(0));
      System.out.println(encodedStr);


      encodedStr.remove(0);
      return this.left.decodeTree(rootNode, encodedStr);    
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
    
    public ArrayList<Boolean> findPath(ArrayList<Boolean> accumPath, String letter, ArrayList<String> alphabet) {
      // return accumulated path if leaf found
      if (this.letter.equals(letter)) {
        System.out.println("Path found!");
        return accumPath;
      }
      // return empty path if leaf not found
      System.out.println("Path not found!");

      return new ArrayList<Boolean>();
    }
    
    public String decodeTree(ITree rootNode, ArrayList<Boolean> encodedStr) {
      if (encodedStr.size() != 0) {
        return this.letter + rootNode.decodeTree(rootNode, encodedStr);
      }
      return this.letter;
    }


}


class ExamplesHuffman2 {
  ArrayList<String> s1, s2;
  ArrayList<Integer> num1, num2;
  ArrayList<Boolean> b1, b2, b3, b4;
  Huffman h1, h2;

  void initConditions() {
    // EXAMPLE 1 -------------------------------
    this.s1 = new ArrayList<String>();
    s1.add("a");
    s1.add("b");
    s1.add("c");
    s1.add("d");
    s1.add("e");
    s1.add("f");
    
    this.num1 = new ArrayList<Integer>();
    num1.add(12);
    num1.add(45);
    num1.add(5);
    num1.add(13);
    num1.add(9);
    num1.add(16);
    
    h1 = new Huffman(s1, num1);
    
    // encoding for "eba"
    this.b1 = new ArrayList<Boolean>();
    b1.add(true);
    b1.add(true);
    b1.add(false);
    b1.add(true);
    b1.add(false);
    b1.add(true);
    b1.add(false);
    b1.add(false);
    
    // decoding for "df"
    this.b2 = new ArrayList<Boolean>();
    b2.add(true);
    b2.add(false);
    b2.add(true);
    b2.add(true);
    b2.add(true);
    b2.add(true);
    
    // EXAMPLE 2 -------------------------------

    this.s2 = new ArrayList<String>();
    s2.add("a");
    s2.add("b");
    
    this.num2 = new ArrayList<Integer>();
    num2.add(1);
    num2.add(1);
    
    this.h2 = new Huffman(s2, num2);
    
    this.b3 = new ArrayList<Boolean>();
    b3.add(false);
    
    this.b4 = new ArrayList<Boolean>();
    b4.add(false);  
  }
  
  
  void testDecode1(Tester t) {
    this.initConditions();
    t.checkExpect(h1.decode(h1.encode("ab")), "ab");
  }
  
  void testDecode2(Tester t) {
    this.initConditions();
    t.checkExpect(h2.decode(b3), "a");
  }
  /*
  void testDecode3(Tester t) {
    this.initConditions();
    ArrayList<Boolean> encodedExpr3 = new ArrayList<>();
    encodedExpr3.add(false);
    encodedExpr3.add(true);
    encodedExpr3.add(false);
    encodedExpr3.add(true);
    encodedExpr3.add(false);
    encodedExpr3.add(true);
    encodedExpr3.add(false);
    encodedExpr3.add(false);
    t.checkExpect(h2.decode(encodedExpr3), "b");
  }*/
/*
  void testEncode(Tester t) {
    this.initConditions();
    ArrayListUtils utils = new ArrayListUtils();
    ITree leafC = new Leaf(5, "c");
    ITree leafE = new Leaf(9, "e");
    ITree leafF = new Leaf(16, "f");
    ITree leafA = new Leaf(12, "a");
    ITree leafD = new Leaf(13, "d");
    ITree leafB = new Leaf(45, "b");
    ITree nodeCE = new Node(leafC, leafE);
    ITree nodeAD = new Node(leafA, leafD);
    ArrayList<ITree> forestEx = new ArrayList<ITree>();
    forestEx.add(leafF); // 16
    forestEx.add(leafB); // 45
    forestEx.add(nodeAD); // 25
    forestEx.add(nodeCE); // 14
    
    forestEx.sort(new CompareITreeValue());
    
    ArrayList<Boolean> bPath = new ArrayList<Boolean>();
    bPath.add(false);
    ArrayList<Boolean> aPath = new ArrayList<Boolean>();
    aPath.add(true);
    aPath.add(false);
    aPath.add(false);
    t.checkExpect(h1.encode("b"), bPath);
    t.checkExpect(h1.encode("ab"), utils.append(aPath, bPath));

  }*/
  
}

