# Binary Series Sum

## Problem Statement - 1

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

