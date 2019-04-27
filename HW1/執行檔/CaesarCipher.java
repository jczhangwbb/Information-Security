import java.util.Scanner;

public class CaesarCipher {
	static char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public static String encrypt(String ptString, int key) {
		char[] ciphertext = new char[ptString.length()];
		for (int i = 0; i <= ptString.length() - 1; i++) {
			int j = (int) ptString.charAt(i) - 'a'; 	// 十进制中a = 97
			if (j + key >= 26) { 						// 如果超出邊界
				int loopNum = j + key - 26;
				ciphertext[i] = alphabet[loopNum];
			} else {
				ciphertext[i] = alphabet[j + key];
			}
		}
		return new String(ciphertext);
	}
	public static void main(String[] args) {
		System.out.println("< Caesar Cipher >");
		Scanner getPlaintext = new Scanner(System.in);
		System.out.println("1.Please input the Plaintext [Must be English letters]");
		String plaintext = getPlaintext.next();
		Scanner getKey = new Scanner(System.in);
		System.out.println("2.Please input the Key️       [Must be an Integer]");
		int key = getKey.nextInt();
		
		System.out.println("The Ciphertext Is:");
		String encrypted = encrypt(plaintext, key);
		System.out.println(encrypted);
	}
}