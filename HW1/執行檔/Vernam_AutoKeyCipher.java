import java.io.StreamCorruptedException;
import java.util.Scanner;

public class Vernam_AutoKeyCipher {
	static char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public static String encrypt(String ptString, String key) {
		char[] ptChars = ptString.toCharArray();
		char[] keyChars = key.toCharArray();

		char[] autoKey = new char[ptString.length()];
		int fillHowMany = ptChars.length - keyChars.length;

		// fill keyword
		for (int i = 0; i < keyChars.length; i++) {
			autoKey[i] = keyChars[i];
		}

		// fill restSpace
		for (int i = keyChars.length, j = 0; i < autoKey.length && j < fillHowMany; i++, j++) {
			autoKey[i] = ptChars[j];
		}

		// Xor and fill ciphertext
		char[] ctChars = new char[ptChars.length];
		for (int i = 0; i < ptChars.length; i++) {
			int ptDecimal = (int) ptChars[i] - 'a';
			int keyDecimal = (int) autoKey[i] - 'a';
			int xor = ptDecimal ^ keyDecimal;
			if (xor >= 26) {
				xor = xor % 26;
				ctChars[i] = alphabet[xor]; // i = 1000 u=10100 xor = 11100-> 28 > 26
			} else {
				ctChars[i] = alphabet[xor]; // i = 1000 u=10100 xor = 11100-> 28 > 26
			}
		}
		
		System.out.println("The AutoKey Is:");
		for (int i = 0; i < autoKey.length; i++) {
			System.out.print(autoKey[i]);
		}

		System.out.println("\n");
		
		return new String(ctChars);
	}

	public static void main(String[] args) {
		System.out.println("< Vernam & AutoKey Cipher >");
		Scanner getPlaintext = new Scanner(System.in);
		System.out.println("1.Please input the Plaintext  [Must be English letters]");
		String plaintext = getPlaintext.next();
		Scanner getKey = new Scanner(System.in);
		System.out.println("2.Please input the Keyword  [Must be Some English letters]");
		String key = getKey.next();
		key = key.toLowerCase();

		String encrypted = encrypt(plaintext, key);
		System.out.println("The Ciphertext Is:");
		System.out.println(encrypted);
	}
}
