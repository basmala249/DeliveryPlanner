# DeliveryPlanner

# 🚚 Delivery Route Planner

A Java command-line application that reads delivery requests, validates inputs, and optimizes vehicle trip assignments based on weight capacity limits, geographic area grouping, and delivery urgency.

---

## 🛠️ How to Compile & Run

### Prerequisites
* **Java Development Kit (JDK):** Version 11 or higher
* **Terminal / Command Prompt**

### Execution Steps

1. **Compile the program:**
   ```bash
   javac Main.java

2. **Run the program:**
   ```bash
   java Main <csv_file> <capacity>
   
## Command Arguments Explained

<csv_file>: Path to your input dataset (e.g., deliveries.csv). <capacity>: Maximum vehicle weight capacity limit in kilograms ($T$), such as 10.


## 📄 Example Output
![Example Output](https://github.com/basmala249/DeliveryPlanner/blob/main/Output.png)

📝 Reasoning & Decision Framework

Q1. Explain your solution approach in your own words.My solution follows a structured pipeline focused on validation, area-based grouping, and greedy priority sorting:Input Validation & Rejection: After reading the input files, every delivery record undergoes strict validation checks. A delivery is rejected and excluded from processing if it has missing fields (such as missing Priority, Area, ID, or Weight) or if its weight exceeds the maximum vehicle capacity limit ($T = 10\text{ kg}$).Area Grouping & In-Area Greedy Sorting: Valid deliveries are grouped by their destination area. Within each area, deliveries are sorted using a greedy strategy:Primary Factor (Priority): Deliveries with the highest priority (urgency) are placed first.Secondary Factor (Balancing via Smaller Weights): Among items of equal priority, smaller items are prioritized to efficiently combine multiple small items together and balance vehicle load utilization quickly.Tie-Breaker (Delivery ID Order): If two items have identical priorities and weights, the insertion order is preserved by comparing their Delivery IDs (earlier IDs come first).Trip Assembly: For each area, items are sequentially packed into trips in their sorted order. As soon as adding an item would exceed the vehicle capacity limit, the current trip is finalized, and a new trip is created.Final Trip Sorting & Reporting: Once all trips are created across all areas, the trips themselves are sorted globally:Primary: Priority of the trip's first delivery (most urgent trips dispatched first).Secondary: Delivery ID of the trip's first package.Finally, the system generates comprehensive reports detailing trip schedules, remaining space, and capacity utilization metrics.


Q2. What was the most difficult part of the assignment?
The most difficult challenge was designing an optimal item combination algorithm without using Dynamic Programming (DP). My initial goal was to compute the theoretical best package combinations per trip, but because DP for multi-constraint knapsack problems is complex, I opted for a customized greedy heuristic. Finding a greedy strategy that effectively balances high-priority items while simultaneously pairing lightweight packages to maximize vehicle capacity without complex recursive states was the trickiest part of the project.


Q3. Are there situations where your algorithm may not produce the best possible grouping? Explain.Yes. Because the algorithm relies on a Greedy Approach rather than an exact Dynamic Programming solution, it can produce sub-optimal trip counts:Sub-Optimal Bin Combinations: Consider packages with weights $[9\text{ kg}, 8\text{ kg}, 2\text{ kg}, 1\text{ kg}]$ going to the same area with a 10 kg vehicle limit.My Greedy Outcome: The algorithm might pair $1\text{ kg}$ with $2\text{ kg}$ into Trip 1 ($3\text{ kg}$ total), then place $9\text{ kg}$ into Trip 2, and $8\text{ kg}$ into Trip 3 ($3$ total trips).Optimal Combinatorial Outcome: Pairing $[9\text{ kg} + 1\text{ kg}]$ into Trip 1 ($10\text{ kg}$) and $[8\text{ kg} + 2\text{ kg}]$ into Trip 2 ($10\text{ kg}$) achieves full capacity with only $2$ total trips.

Q4. If the input contained 1,000,000 delivery requests, what part of your solution might become slow or memory-intensive?Time Complexity ($\mathcal{O}(N \log N)$): The implementation avoids heavy $\mathcal{O}(N^2)$ or $\mathcal{O}(N^3)$ operations. Reading data runs in $\mathcal{O}(N)$ time, and sorting per area runs in $\mathcal{O}(N \log N)$ time. At $1,000,000$ rows, processing takes around $\sim 10^6 \log_2(10^6) \approx 2 \times 10^7$ operations, which is efficient and finishes in a few seconds on modern CPUs.Memory Bottleneck ($\mathcal{O}(N)$ Space): Holding 1,000,000 Delivery objects in RAM inside Java collections (ArrayList, HashMap) consumes significant heap memory ($\sim 300\text{MB} - 500\text{MB}$).

Q5. What would you improve if you had another day to work on the solution?
If I had an extra day, I would improve the item packing algorithm by advancing from a primitive DP understanding to a full Dynamic Programming Knapsack Solver. This would allow the system to evaluate exact package weight combinations for small-to-medium batch sizes, guaranteeing the absolute minimum number of total trips while respecting priority constraints.


✨ Extension Features

1. 🧹 Area String Normalization & CleaningWhat it does: Automatically cleans and normalizes user-entered area names before grouping (e.g., handling variations like "Nasr City", "nasr city", or trailing spaces).Why it was added: In real-world CSV datasets, typos and inconsistent spacing prevent items from being grouped together. Normalizing strings ensures deliveries for the same location are correctly recognized and merged into shared trips.

2. 📊 Trip Capacity Utilization & Space BreakdownWhat it does: Calculates and logs the exact remaining payload capacity ($T - \text{used weight}$) and percentage utilization for every vehicle run.Why it was added: Gives logistics dispatchers clear insight into vehicle efficiency, making it easy to identify underutilized trips and evaluate how well packages were packed.
