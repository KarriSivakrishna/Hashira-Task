// ...existing code...
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;
public class ShamirSecretSharing {
	public static class Point {
		public BigInteger x;
		public BigInteger y;
		public Point(BigInteger x, BigInteger y) {
			this.x = x;
			this.y = y;
		}
	}
	public static class TestCase {
		public int n;
		public int k;
		public List<Point> points;
		public TestCase(){
			this.points = new ArrayList<>();
		}
	}
	public static TestCase parseTestCase(String filename) throws IOException {
		TestCase testCase = new TestCase();
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		}
		Pattern nPattern = Pattern.compile("\"n\":\s*(\\d+)");
		Pattern kPattern = Pattern.compile("\"k\":\s*(\\d+)");
		String contentStr = content.toString();
		Matcher nMatcher = nPattern.matcher(contentStr);
		Matcher kMatcher = kPattern.matcher(contentStr);
		if (nMatcher.find()) {
			testCase.n = Integer.parseInt(nMatcher.group(1));
		}
		if (kMatcher.find()) {
			testCase.k = Integer.parseInt(kMatcher.group(1));
		}
		// Regex for points: "<x>": {"base": "<base>", "value": "<value>"}
		Pattern pointPattern = Pattern.compile("\"(\\d+)\":\\s*\\{\\s*\"base\":\\s*\"(\\d+)\",\\s*\"value\":\\s*\"([^\"]+)\"\\s*}[,}]", Pattern.MULTILINE);
		Matcher pointMatcher = pointPattern.matcher(contentStr);
		while (pointMatcher.find()) {
			BigInteger x = new BigInteger(pointMatcher.group(1));
			int base = Integer.parseInt(pointMatcher.group(2));
			String value = pointMatcher.group(3);
			BigInteger y = decodeFromBase(value, base);
			testCase.points.add(new Point(x, y));
		}
		return testCase;
	}
	public static BigInteger decodeFromBase(String value, int base) {
		BigInteger result = BigInteger.ZERO;
		BigInteger baseBI = BigInteger.valueOf(base);
		for (int i = 0; i < value.length(); i++) {
			char digit = value.charAt(i);
			int digitValue;
			if (digit >= '0' && digit <= '9') {
				digitValue = digit - '0';
			} else if (digit >= 'a' && digit <= 'z') {
				digitValue = digit - 'a' + 10;
			} else if (digit >= 'A' && digit <= 'Z') {
				digitValue = digit - 'A' + 10;
			} else {
				throw new IllegalArgumentException("Invalid digit: " + digit);
			}
			if (digitValue >= base) {
				throw new IllegalArgumentException("Digit" + digit + " is invalid for base " + base);
			}
			result = result.multiply(baseBI).add(BigInteger.valueOf(digitValue));
		}
		return result;
	}
	public static BigInteger findSecret(List<Point> points, int k) {
		List<Point> selectedPoints = points.subList(0, k);
		BigInteger secret = BigInteger.ZERO;
		for (int i = 0; i < selectedPoints.size(); i++) {
			Point pi = selectedPoints.get(i);
			BigInteger numerator = BigInteger.ONE;
			BigInteger denominator = BigInteger.ONE;
			for (int j = 0; j < selectedPoints.size(); j++) {
				if (i != j) {
					Point pj = selectedPoints.get(j);
					numerator = numerator.multiply(pj.x.negate());
					denominator = denominator.multiply(pi.x.subtract(pj.x));
				}
			}
			BigInteger term = pi.y.multiply(numerator).divide(denominator);
			secret = secret.add(term);
		}
		return secret;
	}
	public static void main(String[] args) throws IOException {
		TestCase testCase1 = parseTestCase("testcase1.json");
		BigInteger secret1 = findSecret(testCase1.points, testCase1.k);
		System.out.println("Secret for testcase1.json: " + secret1);
		TestCase testCase2 = parseTestCase("testcase2.json");
		BigInteger secret2 = findSecret(testCase2.points, testCase2.k);
		System.out.println("Secret for testcase2.json: " + secret2);
	}
}