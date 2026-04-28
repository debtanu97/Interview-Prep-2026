# Binary Series Sum

## Problem Statement

Given a number **N**, find the **minimum number of terms** from the "binary-only decimal" series whose sum equals N.

The series is: `[1, 10, 11, 100, 101, 110, 111, 1000, ...]`

These are decimal numbers that contain **only the digits 0 and 1** — i.e., numbers whose base-10 representation uses no digit other than 0 or 1.

### Example

```
Input:  N = 18
Output: 8
 
Explanation:
  18 = 11 + 1 + 1 + 1 + 1 + 1 + 1 + 1   → 8 terms  ✓ (minimum)
  18 = 10 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 → 9 terms
  18 = 1  * 18                             → 18 terms
```
 
---

## Approaches

### Approach 1 — Dynamic Programming (Coin Change)

**Intuition:**
Treat the binary-decimal numbers as "coins" and the problem as a classic coin-change minimum-count problem. First generate all valid binary-decimal terms ≤ N, then fill a DP table bottom-up.

**Algorithm:**
1. Generate all binary-decimal numbers ≤ N using BFS (start from `1`, expand by appending `0` or `1` to get `10`/`11`, then `100`/`101`/`110`/`111`, etc.)
2. Build `dp[0..N]` where `dp[i]` = minimum terms summing to `i`.
3. Recurrence: `dp[i] = min(dp[i - term] + 1)` for all valid `term ≤ i`.
   **Complexity:**

| | |
|---|---|
| Time | `O(N × K)` where K = count of binary-decimal terms ≤ N (`K ≈ 2^(log₁₀N + 1)`, grows very slowly) |
| Space | `O(N + K)` |
 
---

### Approach 2 — Greedy (Digit Sum) ✓ Optimal

**Intuition:**
Each decimal digit `d` at position `p` (place value `10^p`) needs exactly `d` binary-decimal terms of value `10^p` to cover it. Since `10^p` is itself a valid binary-decimal number, the minimum count for that digit is exactly `d`. Summing across all digits gives the answer.

**Key insight:** The minimum number of binary-decimal terms that sum to N equals the **sum of digits of N in base 10**.

**Example:**
```
N = 123  →  digit sum = 1 + 2 + 3 = 6
 
Decomposition:
  100               → covers the hundreds digit (1 term)
  10 + 10           → covers the tens digit    (2 terms)
  1  + 1 + 1        → covers the units digit   (3 terms)
  ─────────────────────────────────────────────────────
  Total = 6 terms
```

**Complexity:**

| | |
|---|---|
| Time | `O(log N)` — one pass over the digits |
| Space | `O(1)` |
 
---

## Comparison

| | Approach 1 (DP) | Approach 2 (Greedy) |
|---|---|---|
| Time | `O(N × K)` | `O(log N)` |
| Space | `O(N)` | `O(1)` |
| Correctness | Verified by construction | Provably optimal |
| Use case | Good for understanding / exploring | Best for production |
 
---

## Test Cases

| N | Expected | Notes |
|---|---|---|
| 1 | 1 | Single term |
| 10 | 1 | `10` is itself in the series |
| 11 | 2 | `1 + 1` digit sum |
| 18 | 8 | Example from problem statement |
| 20 | 2 | `10 + 10` |
| 99 | 18 | `9 + 9` |
| 100 | 1 | `100` is itself in the series |
| 123 | 6 | `1 + 2 + 3` |
| 999 | 27 | `9 + 9 + 9` |
 
---

# Implement Max Stack

## Problem Statement

Implement a **Max Stack** that supports the following five operations, with **no operation running in O(N) time**:

| Operation | Description |
|---|---|
| `push(x)` | Push element `x` onto the stack |
| `pop()` | Remove and return the top element |
| `peek()` | Return the top element without removing it |
| `peekMax()` | Return the maximum element without removing it |
| `removeMax()` | Remove and return the maximum element |
 
---

## Approach: Doubly-Linked List + TreeMap

The core challenge is `removeMax()`. A plain stack with a "max tracker" array can handle `peekMax` in O(1), but removing the maximum from an arbitrary position in the stack is O(N) with a plain array/linked list because we have to shift elements.

The key insight is to **separate the stack's ordering concern from the max-finding concern**, and use a shared node reference to bridge them.

### Data Structures

**1. Doubly-Linked List (DLL) — acts as the stack**

```
sentinel_head ↔ [node1] ↔ [node2] ↔ ... ↔ [nodeN] ↔ sentinel_tail
                                                 ▲
                                              "top" of stack
```

- `push` → insert a new node before the sentinel tail → O(1)
- `pop` → unlink the node just before the sentinel tail → O(1)
- `peek` → read `tail.prev.val` → O(1)
- Sentinel nodes eliminate all edge cases for insertion/removal.
  **2. `TreeMap<Integer, List<Node>>` — maps values to their DLL nodes**

```
TreeMap:
  1 → [nodeA]
  3 → [nodeB, nodeC]   ← two nodes with value 3
  5 → [nodeD]
        ▲
     lastKey() = max
```

- `peekMax` → `treeMap.lastKey()` → O(log N)
- `removeMax` → get the last node from the list at `lastKey()`, unlink it from the DLL → O(log N)
### Why Share Node References?

When `removeMax` is called, the TreeMap gives us the **exact DLL node** to remove. Because the DLL is doubly-linked, unlinking any arbitrary node is O(1) once we have a pointer to it. No linear scan needed.

### Handling Duplicates

Each TreeMap key maps to a `List<Node>` — all DLL nodes carrying that value. When a duplicate is pushed, its node is appended to the list. `removeMax` always removes the **last** entry from the list (the most recently pushed copy), which is O(1).
 
---

## Complexity

| Operation | Time | Notes |
|---|---|---|
| `push(x)` | O(log N) | DLL insert O(1) + TreeMap insert O(log N) |
| `pop()` | O(log N) | DLL unlink O(1) + TreeMap update O(log N) |
| `peek()` | O(1) | Read DLL tail |
| `peekMax()` | O(log N) | TreeMap `lastKey()` |
| `removeMax()` | O(log N) | TreeMap lookup + DLL unlink |
| **Space** | **O(N)** | N nodes in DLL + N entries across TreeMap lists |

> `push` and `pop` are technically O(log N) due to the TreeMap maintenance. They could be O(1) if we only needed `peekMax`, but `removeMax` requires the map to be kept in sync.
 
---

## Why Not Other Approaches?

| Approach | Problem |
|---|---|
| Stack + max-tracking aux stack | `removeMax` is O(N) — must scan for the max position |
| Stack + `PriorityQueue` (heap) | `removeMax` from heap is O(log N), but removing the corresponding stack element to preserve order is O(N) |
| Stack + sorted set (no node sharing) | Same issue — can't remove from middle of stack in O(1) |
| **DLL + TreeMap (this approach)** | ✓ O(log N) for all operations |
 
---

## Test Cases

| # | Scenario | Key Assertion |
|---|---|---|
| 1 | Basic push / peek / pop | Stack LIFO order preserved |
| 2 | `peekMax` does not modify stack | `peek()` unchanged after `peekMax()` |
| 3 | `removeMax` when max is in the middle | Correct element removed, stack order preserved |
| 4 | `removeMax` when max is on top | Behaves like `pop()` in this case |
| 5 | `removeMax` when max is at the bottom | Middle-of-list removal works via DLL pointer |
| 6 | Duplicate values — only one copy removed | TreeMap list shrinks by one entry |
| 7 | Duplicate values — most recent copy removed first | Last-in-list removal is correct |
| 8 | Interleaved `push` and `removeMax` | Map and DLL stay in sync across mixed ops |
| 9 | All same values | `peekMax` always returns the same value until empty |
| 10 | Single element | Degenerate case: push → peekMax → removeMax |
 
---
