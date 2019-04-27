import java.util.Scanner;

public class ProductCipher {

	public static String encrypt(String ptString, int[] key) {
		char[] ptChars = ptString.toCharArray();
		char[] ctChars = new char[ptChars.length];
		// int [] key = {15,11,19,18,16,3,7,14,2,20,4,12,9,6,1,5,17,13,10,8};
		//put every letters in their own position
		for (int i = 0; i < ptChars.length; i++) {
			ctChars[i] = ptChars[key[i] - 1];
		}
		System.out.println("\n");
		return new String(ctChars);
	}

	public static void main(String[] args) {
		System.out.println("< Product Cipher >");
		Scanner getPlaintext = new Scanner(System.in);
		System.out.println("1.Please input the Plaintext  [Must be English letters]");
		String plaintext = getPlaintext.next();
		Scanner getKey = new Scanner(System.in);

		System.out.println("2.Please input "+plaintext.length()+" Numbers as a Key [Must be Integers and have a same length with plaintext]");
		int[] keyChars = new int[plaintext.length()];
		System.out.println("No.1:");
		for (int i = 0; i < plaintext.length(); i++) {
			int inPutKey = getKey.nextInt();
			keyChars[i] = inPutKey;
			if (i == plaintext.length()-1) {
				System.out.println(" ");
			} else {
				System.out.println("No."+(i+2)+":");				
			}
		}

		System.out.println("The key is:");
		for (int i = 0; i < keyChars.length; i++) {
			System.out.print(keyChars[i]);
			if (i == keyChars.length -1) {
				System.out.print(" ");
			} else {
				System.out.print(",");
			}
		}
		
		String encrypted = encrypt(plaintext, keyChars);
		System.out.println("The Ciphertext Is:");
		System.out.println(encrypted);
	}
}