import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Scanner;

public class DSA {

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

	//the function that random Generating prime.
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
	public static BigInteger pickPrime(int n) {
		while (true) {
			String strPrime = genNbitsBinaryStr(n);
			BigInteger result = new BigInteger(strPrime, 2);
			if (fermatTest(result, 10)) { // test 10 times
				return result;
			}
		}
	}

	public static BigInteger[] get_p_q() {
		while (true) {
			//random Generate a 160bits length prime q
			BigInteger q = pickPrime(160);
			//converting to binary string
			String qStr = q.toString(2);
			BigInteger p_1 = BigInteger.ZERO;
			// 160bits + 864 "0" = 1024 bits means multiply 864 times 2
			String p_1Str = qStr
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
					+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			p_1 = new BigInteger(p_1Str, 2);
			//we have a 1024bits length number p
			BigInteger p = p_1.add(BigInteger.ONE);
			//check it is prime or not
			if (fermatTest(p, 10)) { // test 10 times
				return new BigInteger[] { p, q };
			}
		}
	}

	public static BigInteger pick_d_k(BigInteger q) {
		Random random = new Random();
		while (true) {
			BigInteger d = pickPrime(random.nextInt(160) + 1);
			if (d.compareTo(q) < 0) {
				return d;
			}
		}
	}

	public static String SHA_1(String str) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
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

	// Using extend Euclidean algorithm to get d.
	public static BigInteger modularInverse(BigInteger e, BigInteger n) {
		// TODO: min d-secure bits, adding k*fn
		BigInteger[] ret = ext_gcd(e, n);

		BigInteger d = ret[0];
		while (d.compareTo(BigInteger.ZERO) != 1) {
			d = d.add(n);
		}
		return d;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("<DSA>");
		System.out.println("Generating big prime number [p](1024bits) [q](160bits)...");
		// signature generation
		BigInteger[] pq = get_p_q();
		BigInteger p = pq[0];
		System.out.println("p = " + p);
		System.out.println("bitLength = " + p.bitLength() + "\n");
		BigInteger q = pq[1];
		System.out.println("q = " + q);
		System.out.println("bitLength = " + q.bitLength() + "\n");

		System.out.println("Picking [d] [k]... and Calculating [a] [y] [r]...");
		BigInteger p_1 = pq[0].subtract(BigInteger.ONE);
		BigInteger p_1_div_q = p_1.divide(q);
		BigInteger a = square_multiply(BigInteger.TWO, p_1_div_q, p);// g
		System.out.println("a = 2^((p − 1)/q) mod p = " + a);
		System.out.println("bitLength = " + a.bitLength() + "\n");
		BigInteger d = pick_d_k(q);// private key(x)
		System.out.println("d is Private key (0<d<q) = " + d);
		System.out.println("bitLength = " + d.bitLength() + "\n");
		// private key ={p,q,a,d}
		BigInteger y = square_multiply(a, d, p);
		System.out.println("y is Public key = (a^d mod p) = " + y);
		System.out.println("bitLength = " + y.bitLength() + "\n");
		// public key={p,q,a,y}

		BigInteger k = pick_d_k(q);
		System.out.println("k is an random key (1<k<q) = " + k);
		System.out.println("bitLength = " + k.bitLength() + "\n");
		BigInteger r = square_multiply(a, k, p).mod(q);
		System.out.println("r = ( (a^k mod p) mod q ) = " + r);
		System.out.println("bitLength = " + r.bitLength() + "\n");

		// System.out.println("Please input the message:(eg.myDSAbooo)");
		// Scanner getinput = new Scanner(System.in);
		// String m = getinput.next();// myDSAbooo;
		// BigInteger s = x+d.multiply(r);
		System.out.println("Message is \"myDSAbooo\"\n");
		String m = "myDSAbooo";

		System.out.println("Calculating SHA1(m) and converting to Decimal...");
		String hString = SHA_1(m);
		System.out.println("SHA1(m)[Hex] = " + hString);
		BigInteger h = new BigInteger(hString, 16);
		System.out.println("h (SHA1(m)[Dec]) = " + h);
		System.out.println("bitLength = " + h.bitLength() + "\n");

		System.out.println("Calculating [s]...");
		BigInteger k_inverse = modularInverse(k, q);
		BigInteger s = (k_inverse.multiply((h.add((d.multiply(r)))))).mod(q);
		System.out.println("s = ( (SHA1(m)+d*r)*k^(-1) mod q ) = " + s);
		System.out.println("bitLength = " + s.bitLength() + "\n");
		// System.out.println("check r & s < q-1 : ");
		// System.out.println("r<q-1? = "+r.compareTo(q.subtract(BigInteger.ONE)));
		// System.out.println("s<q-1? = "+s.compareTo(q.subtract(BigInteger.ONE)));

		// System.out.println();
		// System.out.println("{p,q,a,y,r,s,m}");
		// System.out.println("p = "+p);
		// System.out.println("q = "+q);
		// System.out.println("a = "+a);
		// System.out.println("y = "+y);
		// System.out.println("r = "+r);
		// System.out.println("s = "+s);
		// System.out.println("m = "+m);
		//
		System.out.println("Signature Generation Finished");

		// signature verification
//		BigInteger w = modularInverse(s, q);
//		System.out.println("w = " + w);
//		BigInteger i = (w.multiply(h)).mod(q);
//		System.out.println("i = " + i);
//		BigInteger j = (w.multiply(r)).mod(q);
//		System.out.println("j = " + j);
//		BigInteger step1_To_v = square_multiply(a, i, p);
//		// System.out.println("step1_To_v = "+step1_To_v);
//		BigInteger step2_To_v = square_multiply(y, j, p);
//		// System.out.println("step2_To_v = "+step2_To_v);
//		BigInteger step3_To_v = (step1_To_v.multiply(step2_To_v)).mod(p);
//		// System.out.println("step3_To_v = "+step3_To_v);
//		BigInteger v = step3_To_v.mod(q);
//		System.out.println("v = " + v);
//		System.out.println("bitLength = " + v.bitLength() + "\n");
//		if (v.compareTo(r) == 0) {
//			System.out.println("Verification passed");
//		} else {
//			System.out.println("Verification failed");
//		}
//		System.out.println("Finish");
	}

}
