# Resolve String Template with Nested Placeholders

## Problem Description
Resolve a template string containing placeholders of the form `%key%`, where each key maps to a value that may itself contain other placeholders.

Placeholders must be resolved **recursively** until the template is fully expanded.  
If a **cycle** exists in the placeholder dependencies, the resolution must fail gracefully.

---

## Reference Discussion
**Google L4 Phone Screen – String Template Resolution with Dependencies**

---

## Core Patterns
- DFS with Memoization  
- Dependency Graph Resolution  
- Cycle Detection (Recursion Stack)  
- String Parsing  
- HashMap-Based Caching  

---

## Key Insight
Placeholder resolution forms an **implicit directed dependency graph**:
- Each placeholder depends on the placeholders referenced in its value
- Resolving a placeholder requires resolving all its dependencies first

This naturally leads to a **DFS-based solution** with:
- Memoization for efficiency
- Recursion stack tracking for cycle detection

---

## Approach Summary
1. Parse the template string and identify placeholders
2. Resolve placeholders recursively using DFS
3. Cache resolved values to avoid recomputation
4. Detect cycles using a recursion stack
5. Replace missing keys with empty strings
6. Return `"EXCEPTION"` if a cycle is detected

---

## Complexity Analysis
- **Time Complexity:** `O(n)`  
  (each character and placeholder is processed at most once)
- **Space Complexity:** `O(k)`  
  (`k` = number of unique placeholders)

---

## Edge Cases Covered
- Nested placeholders  
- Shared dependencies  
- Repeated placeholders  
- Missing keys  
- Self-referential cycles  
- Indirect cycles  
- Mixed literal and placeholder content  

---

## Solution (Interview-Prep-2026)
**Resolve String Template with Recursive Dependency Resolution**

---

## Interview Notes
This problem tests the ability to:
- Recognize hidden dependency graphs in string problems
- Apply DFS with memoization correctly
- Detect cycles cleanly
- Avoid unnecessary over-engineering (topological sort is optional)

It is a strong indicator of **systematic problem-solving and robustness** expected at Google L4+.

---
