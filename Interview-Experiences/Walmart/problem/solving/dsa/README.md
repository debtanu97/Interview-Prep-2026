# Walmart DSA Round - Problem Solutions

This repository contains solutions to two classic problems asked in Walmart's Problem Solving / DSA Round.

---

## 🧠 Problem 1: Rotten Oranges

### 📌 Problem Statement
You are given a grid where:
- `0` → Empty cell
- `1` → Fresh orange
- `2` → Rotten orange

Every minute, any fresh orange adjacent (4-directionally) to a rotten orange becomes rotten.

### 🎯 Goal
Return the **minimum time** required to rot all oranges.
If impossible → return `-1`.

---

### 💡 Approach (BFS - Multi Source)

1. Add all rotten oranges to queue
2. Count fresh oranges
3. Perform BFS level-wise:
    - Each level = 1 minute
4. Convert adjacent fresh → rotten
5. Track time

---

### ⏱ Complexity

| Metric | Value |
|------|------|
| Time | O(N × M) |
| Space | O(N × M) |

---

### 🚨 Edge Cases

- No fresh oranges → `0`
- Isolated fresh oranges → `-1`
- All already rotten → `0`

---

## 🧠 Problem 2: House Robber

### 📌 Problem Statement

Given an array:
- Each element represents money in a house
- Cannot rob adjacent houses

### 🎯 Goal

Maximize money robbed.

---

### 💡 Approach (Dynamic Programming)

**State:**

`dp[i] = max money till index i`


**Transition:**

```
dp[i] = max(
    dp[i-1], // skip
    nums[i] + dp[i-2] // rob
)
```


Optimized to O(1) space.

---

### ⏱ Complexity

| Metric | Value |
|------|------|
| Time | O(N) |
| Space | O(1) |

---

### 🚨 Edge Cases

- Empty array → `0`
- Single element → return value
- All decreasing → pick max non-adjacent

---