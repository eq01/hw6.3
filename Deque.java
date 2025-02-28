import tester.*;
import java.util.function.*;

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  ANode(ANode<T> next, ANode<T> prev) {
    this.prev = prev;
    this.next = next;
  }

  int nodeLength() {
    return 1 + this.next.nodeLength();
  }

  abstract void addNode(T value, int index);

  abstract void removeNode(int index);

  abstract ANode<T> findNode(Predicate<T> pred);

  abstract T getData();
}

class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> value) {
    this.header = value;
  }

  int size() {
    return this.header.next.nodeLength();
  }

  T addAtHead(T value) {
    return this.header.addHead(value);
  }

  T addAtTail(T value) {
    return this.header.addTail(value);
  }

  T removeFromHead() {
    if (this.header == this.header.next) {
      throw new RuntimeException("Can't remove from an empty list!");
    }
    return this.header.removeHead();

  }

  T removeFromTail() {
    if (this.header == this.header.prev) {
      throw new RuntimeException("Can't remove from an empty list!");
    }
    return this.header.removeTail();
  }

  // ABSTRACTION METHODS: add and remove
  void add(T value, int index) {
    // check if index is out of bounds!
    if (index >= 0 && index <= this.size() - 1) {
      this.header.addNode(value, index);
    }
    else {
      throw new IllegalArgumentException(
          "Cannot add value " + "because index " + index + " is out of bounds!");
    }
  }

  void remove(int index) {
    // check if index is out of bounds!
    if (index >= 0 && index <= this.size() - 1) {
      this.header.removeNode(index);
    }
    else {
      throw new IllegalArgumentException(
          "Cannot remove value " + "because index " + index + " is out of bounds!");
    }
  }

  ANode<T> find(Predicate<T> pred) {
    return this.header.next.findNode(pred);
  }
  
  void removeNode(ANode<T> nodeToRemove) {
    if (this.header != nodeToRemove) {
      this.remove(this.indexOf(nodeToRemove));
    }
  }

}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    super(null, null);
    this.data = data;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    if (prev == null || next == null) {
      throw new IllegalArgumentException("Given nodes cannot be null!");
    }
    this.data = data;
    super.next.prev = this;
    super.prev.next = this;
  }

  public void update(boolean updateHead, T value) {
    if (value != null) {

    }
  }

  ANode<T> findNode(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    }
    return this.next.findNode(pred);
  }

  public void addNode(T value, int index) {
    if (index == 0) {
      this.prev = new Node<T>(value, this, this.prev);
    }
    else {
      this.next.addNode(value, index - 1);
    }
  }

  public void removeNode(int index) {
    if (index == 0) {
      this.prev.next = this.next;
      this.next.prev = this.prev;
    }
    else {
      this.next.removeNode(index - 1);
    }

  }
  
  public T getData() {
    return this.data;
  }

}

class Sentinel<T> extends ANode<T> {

  Sentinel() {
    super(null, null);
    super.prev = this;
    super.next = this;
  }

  int nodeLength() {
    return 0;
  }

  T addHead(T value) {
    ANode<T> currentFirst = this.next;
    this.next = new Node<T>(value, currentFirst, this);
    return value;
  }

  T addTail(T value) {
    ANode<T> currentLast = super.prev;
    super.prev = new Node<T>(value, this, currentLast);
    return value;
  }

  T removeHead() {
    ANode<T> oldFirst = super.next;
    ANode<T> newFirst = super.next.next;
    super.next = newFirst;
    newFirst.prev = this;
    return oldFirst.getData();
  }

  T removeTail() {
    ANode<T> oldLast = super.prev;
    ANode<T> newLast = super.prev.prev;
    super.prev = newLast;
    newLast.next = this;
    return oldLast.getData();
  }

  ANode<T> findNode(Predicate<T> pred) {
    return this;
  }

  public void addNode(T value, int index) {
    if (index == 0) {
      ANode<T> currentFirst = this.next;
      this.next = new Node<T>(value, currentFirst, this);
    }
    else {
      this.next.addNode(value, index);
    }

  }

  public void removeNode(int index) {
    this.next.removeNode(index);

  }
  
  public T getData() {
    return null;
  }
}

// Predicate Example
class StrContains implements Predicate<String> {
  String strToFind;

  StrContains(String strToFind) {
    this.strToFind = strToFind;
  }

  public boolean test(String str) {
    return str.indexOf(strToFind) != -1;
  }
}

// need to define more pred classes maybe 2 more to test!

class ExamplesDeque {
  Node<String> abc, cde, bcd, def, zla, iml, rel, tcy;
  Sentinel<String> sentinel1, sentinel2, sentinel3;
  Deque<String> deque1, deque2, deque3;

  void initData() {
    // NOTE: fix order of list!!

    // Example 1 ------
    this.sentinel1 = new Sentinel<String>();
    this.def = new Node<String>("def", sentinel1, sentinel1);
    this.cde = new Node<String>("cde", def, sentinel1);
    this.bcd = new Node<String>("bcd", cde, sentinel1);
    this.abc = new Node<String>("abc", bcd, sentinel1);
    this.deque1 = new Deque<String>(sentinel1);


    // Example 2 ------
    this.sentinel2 = new Sentinel<String>();
    this.zla = new Node<String>("zla", sentinel2, sentinel2);
    this.iml = new Node<String>("iml", zla, sentinel2);
    this.rel = new Node<String>("rel", iml, sentinel2);
    this.tcy = new Node<String>("tcy", rel, sentinel2);
    this.deque2 = new Deque<String>(sentinel2);

    // Example 3 ------
    this.sentinel3 = new Sentinel<String>();
    this.deque3 = new Deque<String>(sentinel3);
  }
  
  void test1 (Tester t){
    this.initData();
    t.checkExpect(this.deque1.header, null);
  }

  /*
   * void testSize(Tester t) { this.initData(); t.checkExpect(deque1.header,
   * null); t.checkExpect(deque1.size(), 4); t.checkExpect(deque2.size(), 4);
   * t.checkExpect(deque3.size(), 0); }
   * 
   * void testAddAtHead(Tester t) { this.initData(); this.deque1.addAtHead("yup");
   * t.checkExpect(this.deque1.header, null); }
   * 
   * void testAddAtTail(Tester t) { this.initData(); this.deque1.addAtTail("yup");
   * t.checkExpect(this.deque1.header, null); }
   * 
   */

  /*
   * void testRemoveHead(Tester t) { this.initData();
   * this.deque1.removeFromHead(); this.deque2.removeFromHead(); //
   * deque3.removeFromHead(); // t.checkExpect(deque3.header, null); -> change to
   * check exception t.checkExpect(this.deque1.header, null);
   * t.checkExpect(this.deque2.header, null);
   * 
   * }
   */

  /*
  void testFind(Tester t) {
    this.initData();
    StrContains containsE = new StrContains("e");
    StrContains containsZ = new StrContains("z");

    t.checkExpect(this.deque1.find(containsE), this.cde);
    t.checkExpect(this.deque1.find(containsZ), this.deque1.header);
    t.checkExpect(this.deque2.find(containsZ), this.zla);
    t.checkExpect(this.deque3.find(containsZ), this.deque3.header);
    ;
  }

  void testAddAbstraction1(Tester t) {
    this.initData();
    this.deque1.add("new value", 0);
    t.checkExpect(this.deque1.header, null);
  }
  
  void testAddAbstraction2(Tester t) {
    this.initData();
    this.deque1.add("new value", 3);
    t.checkExpect(this.deque1.header, null);
  }
  
  // insert add test with out of bounds -> exception!
  
  void testRemoveAbstraction3(Tester t) {
    this.initData();
    this.deque2.remove(4); // check exception out of bounds!
    t.checkExpect(this.deque2.header, null);
  }
  
  void testRemoveAbstraction1(Tester t) {
    this.initData();
    this.deque2.remove(0);
    t.checkExpect(this.deque2.header, null);
  } */
  /*
  void testRemoveAbstraction2(Tester t) {
    this.initData();
    this.deque2.remove(1);
    t.checkExpect(this.deque2.header, null);
  }*/
  
 

}
