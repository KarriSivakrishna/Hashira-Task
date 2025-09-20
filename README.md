Shamir’s Secret Sharing – Hashira Placements Assignment
📌 Problem Statement

You are given a set of points in JSON format. Each point represents a root (x, y) of a secret polynomial used in Shamir’s Secret Sharing scheme.

The task is to:

Parse the input JSON.

Decode the values from their given number bases.

Reconstruct the polynomial using Lagrange interpolation.

Find the secret, which is the constant term of the polynomial (f(0)).

📂 Input Format

The input is provided as a JSON file.
Example test case:

{
  "keys": {
    "n": 4,
    "k": 3
  },
  "1": {
    "base": "10",
    "value": "4"
  },
  "2": {
    "base": "2",
    "value": "111"
  },
  "3": {
    "base": "10",
    "value": "12"
  },
  "6": {
    "base": "4",
    "value": "213"
  }
}

Explanation:

n: Total number of points given.

k: Minimum number of points required to reconstruct the secret.

Note: k = m + 1, where m is the degree of the polynomial.

Each numbered entry ("1", "2", "3", "6") is a point (x, y):

The key ("1", "2", etc.) is the x value.

base indicates the number system of the value.

value is the encoded y, which needs to be decoded from the given base.

⚙️ Approach
Step 1: Parse the JSON

Extract n and k from the "keys" object.

Extract each point (x, y):

Convert x from the JSON key.

Decode y from its given base into a BigInteger.

Step 2: Decode from Base

Supports bases 2 to 36 (0-9 and a-z).

Example:

"value": "111", "base": "2" → 7 in decimal.

"value": "213", "base": "4" → 39 in decimal.

Step 3: Lagrange Interpolation

Reconstruct the secret using:

𝑓
(
0
)
=
∑
𝑖
=
0
𝑘
−
1
𝑦
𝑖
⋅
∏
0
≤
𝑗
<
𝑘


𝑗
≠
𝑖
0
−
𝑥
𝑗
𝑥
𝑖
−
𝑥
𝑗
f(0)=
i=0
∑
k−1
	​

y
i
	​

⋅
0≤j<k
j

=i
	​

∏
	​

x
i
	​

−x
j
	​

0−x
j
	​

	​


This gives the constant term of the polynomial.

Step 4: Output

Print the reconstructed secret.

🖥️ Example Run
Input (test1.json)
{
  "keys": {
    "n": 4,
    "k": 3
  },
  "1": {"base": "10", "value": "4"},
  "2": {"base": "2", "value": "111"},
  "3": {"base": "10", "value": "12"},
  "6": {"base": "4", "value": "213"}
}

Output
Secret for test1.json: 3
