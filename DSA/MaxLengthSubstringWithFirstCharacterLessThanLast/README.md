# Max Length Substring with First Character < Last

## Problem Summary
Given a lowercase string `s`, find the **maximum length substring** `s[l..r]` such that: s[l] < s[r]

---

## Key Insight
For a fixed ending index `r`, the **earliest index `l`** with a character **strictly smaller** than `s[r]` yields the **longest valid substring** ending at `r`.

Since the alphabet size is fixed (26), this can be solved in **linear time**.

---

## Optimal Strategy
- Scan string left → right
- Track the **earliest occurrence of each character**
- For each position `r`, check all characters `< s[r]`
- Use the earliest such index to update the maximum length

---

## Complexity
- **Time:** `O(n)`
- **Space:** `O(1)` (26 characters)

---

## Common Pitfalls
- Using `<=` instead of `<`
- Assuming the full string is valid
- Treating this as a sliding window problem
- Ignoring repeated characters

---

## Interview Notes
- This is a **Google-style phone screen** problem
- Focus is on **problem interpretation + greedy reasoning**
- Proof of correctness matters more than clever code
