import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSA_1024 {
	// square & multiply
	public static BigInteger square_multiply(BigInteger base, BigInteger exp, BigInteger mode) {
		String bits = exp.toString(2);
		BigInteger val = new BigInteger(base + "").mod(mode);
		for (int i = 1; i < bits.length(); i++) {
			val = val.multiply(val).mod(mode);
			if (bits.charAt(i) == '1') {
				val = val.multiply(base).mod(mode);
			}
		}
		return val;
	}

	public static BigInteger gcd(BigInteger a, BigInteger b) {
		return b.equals(BigInteger.ZERO) ? a : gcd(b, a.mod(b));
	}

	// extend Euclidean
	public static BigInteger[] ext_gcd(BigInteger a, BigInteger b) {
		if (b.equals(BigInteger.ZERO)) {
			return new BigInteger[] { BigInteger.ONE, BigInteger.ZERO, a };
		}

		Object[] ret = ext_gcd(b, a.mod(b));
		BigInteger x = (BigInteger) ret[0];
		BigInteger y = (BigInteger) ret[1];
		BigInteger r = (BigInteger) ret[2];

		BigInteger tmp = x;
		x = y;
		y = tmp.subtract((a.divide(b)).multiply(y));
		return new BigInteger[] { x, y, r };

	}
//	Using extend Euclidean algorithm to get d.
	public static BigInteger find_d(BigInteger e, BigInteger fn) {
		// TODO: min d-secure bits, adding k*fn
		BigInteger[] ret = ext_gcd(e, fn);

		BigInteger d = ret[0];
		while (d.compareTo(BigInteger.ZERO) != 1) {
			d = d.add(fn);
		}
		return d;
	}

	public static BigInteger find_e(BigInteger fn) {
		BigInteger i = new BigInteger("2");
		while (true) {
			if (gcd(fn, i).equals(BigInteger.ONE)) {
				return i;
			}
			i = i.add(BigInteger.ONE);
		}
	}

	// random generate a big number
	public static String genNbitsBinaryStr(int n) {
		String result = "";
		String prime = "";
		Random random = new Random();
		for (int i = 1; i <= n - 2; i++) {
			prime += random.nextInt(100) % 2;
		}
		result = "1" + prime + "1";
		return new String(result);
	}

	// fermat test -> isPrime?
	public static boolean fermatTest(BigInteger n, int checkHowManyTimes) {
		Random random = new Random();
		for (int i = 0; i < checkHowManyTimes; i++) {
			String randStr = String.valueOf(1 + random.nextInt(99999999));
			if (!square_multiply(new BigInteger(randStr), n.subtract(BigInteger.ONE), n).equals(BigInteger.ONE)) {
				return false;
			}
		}
		return true;
	}

	// Random choice a Big Number and Text it is prime or not.
	public static BigInteger pickPrime() {
		while (true) {
			String strPrime = genNbitsBinaryStr(512);
			BigInteger result = new BigInteger(strPrime, 2);
			if (fermatTest(result, 10)) { // test 10 times
				return result;
			}
		}
	}

	// pick p and q
	public static BigInteger[] get_p_q() {
		BigInteger p = pickPrime();
		while (true) {
			BigInteger q = pickPrime();
			if (!p.equals(q)) {
				return new BigInteger[] { p, q };
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("<RSA Encryption & Decryption>");
		System.out.println("Please input the plaintext:(eg.1123)");
		Scanner getinput = new Scanner(System.in);
		BigInteger plaintext = getinput.nextBigInteger();// 1123;

		BigInteger[] pq = get_p_q();
		BigInteger pBigInteger = pq[0];
		BigInteger qBigInteger = pq[1];

		System.out.println("decimal p = " + pBigInteger);
		System.out.println("decimal q = " + qBigInteger);

		BigInteger nBigInteger = pBigInteger.multiply(qBigInteger);
		System.out.println("n = " + nBigInteger);
		BigInteger p_1 = pBigInteger.subtract(new BigInteger("1"));
		BigInteger q_1 = qBigInteger.subtract(new BigInteger("1"));
		BigInteger fi_n = p_1.multiply(q_1);
		System.out.println("φ(n) = " + fi_n);

		BigInteger e = find_e(fi_n);

		System.out.println("---------------------Encryption--------------------");
		System.out.println("e = " + e);
		System.out.println("c = m^e mod n");
		// BigInteger ciphertext = plaintext.modPow(e, nBigInteger);
		BigInteger ciphertext = square_multiply(plaintext, e, nBigInteger);
		System.out.println("Ciphertext：" + ciphertext);

		System.out.println("--------------------Dencryption--------------------");
		BigInteger dBigInteger = find_d(e, fi_n);
		System.out.println("d = " + dBigInteger);
		// System.out.println("[d*e mod fin] = "+(dBigInteger.multiply(e).mod(fi_n)));
		// 驗證d是否滿足條件
		System.out.println("m = c^d mod n");
		// BigInteger pt = ciphertext.modPow(dBigInteger, nBigInteger);
		BigInteger pt = square_multiply(ciphertext, dBigInteger, nBigInteger);
		System.out.println("Plaintext：" + pt);
	}
}
