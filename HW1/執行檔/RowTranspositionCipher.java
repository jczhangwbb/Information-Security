import java.util.Scanner;

public class RowTranspositionCipher {
	
	public static String encrypt(String ptString , int key) {
		char[] ptChars = ptString.toCharArray();
		String keyString = String.valueOf(key);
		char[] keyChars = keyString.toCharArray();
		
		int howManyRow;
		int restSpace;
		int mod = ptChars.length % keyChars.length;
		if (mod < keyChars.length) {
			howManyRow = ptChars.length / keyChars.length + 2;
			restSpace = keyChars.length - mod;
		} else {
			howManyRow = ptChars.length / keyChars.length + 1;
		}

		// for (int i = 0; i < ptChars.length; i++) {
		// System.out.print(ptChars[i]);
		// }

		char[][] table = new char[howManyRow][keyChars.length];
		// System.out.println(howManyRow);
		// System.out.println(key.length);
		for (int i = 0; i < keyChars.length; i++) {
			table[0][i] = keyChars[i];
		}
		for (int x = 0; x < ptChars.length;) {
			for (int i = 1; i < howManyRow; i++) {
				for (int j = 0; j < keyChars.length; j++) {
					if (x == ptChars.length) {
						table[i][j] = '-'; //use '-' to fill the rest space OR Can I use ' ' to fill the rest?
					} else {
						table[i][j] = ptChars[x];
						x++;
					}
				}
			}
		}
		System.out.print("\n");
		 for (int i = 0; i < howManyRow; i++) { // loop to print out
		 for (int j = 0; j < keyChars.length; j++)
		 System.out.print(table[i][j] + "\t");
		 System.out.println();
		 }
		 
		char[] ciphertext = new char[(howManyRow - 1) * keyChars.length];
		for (int i = 0; i < ciphertext.length;) {
				for (int j = 0 , y=1; j < keyChars.length && y<= keyChars.length ;j++ ) {
					if (Integer.parseInt(String.valueOf(table[0][j])) == y ) {
						for (int k = 1; k < howManyRow; k++) {
							ciphertext[i] = table[k][j];
							i++;
							if (k == howManyRow-1) {
								y++;
							}
						}
						j=-1;
					}
				}
		}
		
		//real ciphertext means drop the extra letters
		char[] realCiphertext = new char[ptString.length()];
		for (int i = 0 , j = 0 ; i < realCiphertext.length && j<ciphertext.length ; i++, j++) {			
				if (ciphertext[j] == '-') {
					j++;
					realCiphertext[i] = ciphertext[j];
				} else {
					realCiphertext[i] = ciphertext[j];
				}
		}
		
		System.out.print("\n");
		return new String(realCiphertext);
	}
	
	public static void main(String[] args) {
		System.out.println("< Row Transposition Cipher >");
		Scanner getPlaintext = new Scanner(System.in);
		System.out.println("1.Please input the Plaintext  [Must be English letters]");
		String plaintext = getPlaintext.next();
		Scanner getKey = new Scanner(System.in);
		System.out.println("2.Please input the Key        [Must be an Integer]");
		int key = getKey.nextInt();
//		key = 31562487
		
		String encrypted = encrypt(plaintext, key);
		System.out.println("The Ciphertext Is:");
		System.out.println(encrypted);
	}
}
