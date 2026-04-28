package problem.solving;

import java.util.*;

/**
 * Problem:
 * Implement a Max Stack supporting the following operations, all in O(log N) time:
 *   push(x)    — push x onto the stack
 *   pop()      — remove and return the top element
 *   peek()     — return the top element without removing it
 *   peekMax()  — return the maximum element without removing it
 *   removeMax()— remove and return the maximum element
 *
 * Constraint: No operation may run in O(N) time.
 *
 * Approach: Two data structures kept in sync via a shared node identity.
 *
 *   1. A doubly-linked list (DLL) acting as the stack.
 *      - push  → add node to the tail  — O(1)
 *      - pop   → remove from the tail  — O(1)
 *      - peek  → read tail value       — O(1)
 *
 *   2. A TreeMap<Integer, List<Node>> mapping value → list of DLL nodes with that value.
 *      - peekMax   → treeMap.lastKey()                           — O(log N)
 *      - removeMax → get last node from the list at lastKey(),
 *                    remove it from the DLL and from the map     — O(log N)
 *
 *   Both structures share the same Node objects, so a removeMax unlinks
 *   the node from the DLL in O(1) once the TreeMap lookup gives us its reference.
 *
 * Time Complexity  : O(log N) for all five operations
 * Space Complexity : O(N)
 */
public class MaxStack {

    // -----------------------------------------------------------------------
    // Doubly-linked list node
    // -----------------------------------------------------------------------
    private static class Node {
        int val;
        Node prev, next;
        Node(int val) { this.val = val; }
    }

    // Sentinel head and tail make insert/remove edge-case-free
    private final Node head, tail;

    // TreeMap: value → all DLL nodes carrying that value (in insertion order)
    private final TreeMap<Integer, List<Node>> map;

    public MaxStack() {
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
        map = new TreeMap<>();
    }

    // Appends a new node after 'prev'
    private Node insertAfter(Node prev, int val) {
        Node node = new Node(val);
        node.next = prev.next;
        node.prev = prev;
        prev.next.prev = node;
        prev.next = node;
        return node;
    }

    // Unlinks a node from the DLL
    private void unlink(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /** Push x onto the stack. O(log N) */
    public void push(int x) {
        Node node = insertAfter(tail.prev, x);           // add before sentinel tail
        map.computeIfAbsent(x, k -> new ArrayList<>()).add(node);
    }

    /** Remove and return the top element. O(log N) */
    public int pop() {
        Node top = tail.prev;
        unlink(top);
        List<Node> nodes = map.get(top.val);
        nodes.remove(nodes.size() - 1);                  // O(1) — remove last
        if (nodes.isEmpty()) map.remove(top.val);
        return top.val;
    }

    /** Return the top element without removing it. O(1) */
    public int peek() {
        return tail.prev.val;
    }

    /** Return the maximum element without removing it. O(log N) */
    public int peekMax() {
        return map.lastKey();
    }

    /** Remove and return the maximum element. O(log N) */
    public int removeMax() {
        int maxVal = map.lastKey();
        List<Node> nodes = map.get(maxVal);
        Node node = nodes.remove(nodes.size() - 1);      // O(1) — remove last
        if (nodes.isEmpty()) map.remove(maxVal);
        unlink(node);
        return maxVal;
    }

    public boolean isEmpty() {
        return head.next == tail;
    }

    // -----------------------------------------------------------------------
    // Test suite
    // Each test case is a sequence of operations encoded as:
    //   String[]  ops    — operation names
    //   int[]     args   — argument (0 if operation takes no argument)
    //   Integer[] result — expected return value (null if void / not checked)
    // -----------------------------------------------------------------------
    public static class TestCase {
        public final String label;
        public final String[] ops;
        public final int[]    args;
        public final Integer[] expected; // null = void operation (push)

        public TestCase(String label, String[] ops, int[] args, Integer[] expected) {
            this.label    = label;
            this.ops      = ops;
            this.args     = args;
            this.expected = expected;
        }
    }

    public static TestCase[] getTestSuite() {
        return new TestCase[]{

                new TestCase(
                        "Basic push / peek / pop",
                        new String[]{"push","push","push","peek","pop","peek"},
                        new int[]   {  1,     2,     3,     0,    0,    0  },
                        new Integer[]{null,  null,  null,   3,    3,    2  }
                ),

                new TestCase(
                        "peekMax does not modify stack",
                        new String[]{"push","push","push","peekMax","peek"},
                        new int[]   {  5,     1,     3,     0,        0  },
                        new Integer[]{null,  null,  null,   5,        3  }
                ),

                new TestCase(
                        "removeMax removes correct element",
                        new String[]{"push","push","push","removeMax","peek"},
                        new int[]   {  1,     5,     3,     0,          0  },
                        new Integer[]{null,  null,  null,   5,          3  }
                ),

                new TestCase(
                        "removeMax when max is on top",
                        new String[]{"push","push","push","removeMax","peek"},
                        new int[]   {  1,     2,     5,     0,          0  },
                        new Integer[]{null,  null,  null,   5,          2  }
                ),

                new TestCase(
                        "removeMax when max is at bottom",
                        new String[]{"push","push","push","removeMax","pop","peek"},
                        new int[]   {  5,     2,     3,     0,          0,   0  },
                        new Integer[]{null,  null,  null,   5,          3,   2  }
                ),

                new TestCase(
                        "Duplicate values — removeMax removes only one copy",
                        new String[]{"push","push","push","removeMax","peekMax","peek"},
                        new int[]   {  3,     3,     1,     0,           0,       0  },
                        new Integer[]{null,  null,  null,   3,           3,       1  }
                ),

                new TestCase(
                        "Duplicate values — most recently pushed copy removed first",
                        new String[]{"push","push","removeMax","pop"},
                        new int[]   {  5,     5,     0,           0 },
                        new Integer[]{null,  null,   5,           5 }
                ),

                new TestCase(
                        "Interleaved push and removeMax",
                        new String[]{"push","push","removeMax","push","peekMax","removeMax","peek"},
                        new int[]   {  2,     8,     0,          6,     0,        0,          0  },
                        new Integer[]{null,  null,   8,         null,   6,        6,          2  }
                ),

                new TestCase(
                        "All same values",
                        new String[]{"push","push","push","peekMax","removeMax","peekMax","pop"},
                        new int[]   {  7,     7,     7,     0,         0,          0,       0 },
                        new Integer[]{null,  null,  null,   7,         7,          7,       7 }
                ),

                new TestCase(
                        "Single element: push → peekMax → removeMax",
                        new String[]{"push","peekMax","removeMax"},
                        new int[]   {  42,    0,         0        },
                        new Integer[]{null,  42,        42        }
                ),
        };
    }
}
