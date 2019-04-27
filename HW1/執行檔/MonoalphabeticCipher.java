import java.util.Scanner;

public class MonoalphabeticCipher {
	
	public static String encrypt(String ptString ,char [] plain, char[] cipher) {
		char [] ciphertext = new char[ptString.length()];
        char [] map = new char[26];
        for (int i = 0; i < plain.length; i++) {
        	int letter = (int)plain[i] - 'a' ;
        	map[letter] = cipher[i];
		}
        for (int i = 0; i < ptString.length()-1; i++) {
			int letter = (int)ptString.charAt(i) - 'a';
			ciphertext[i]=map[letter];
		}
        return new String(ciphertext);
	}
	
	public static void main(String[] args) {
		System.out.println("< Monoalphabetic Cipher >");
		Scanner getPlaintext = new Scanner(System.in);
		System.out.println("1.Please input the Plaintext  [Must be English letters]");
		String plaintext = getPlaintext.next();
		Scanner getKey = new Scanner(System.in);
		System.out.println("2.Please input the Key️_plain  [Must be 26 English letters]");
		String keyPlain = getKey.next();
		System.out.println("3.Please input the Key️_cipher [Must be 26 English letters]");
		String keyCipher = getKey.next();
		
		char [] plain = keyPlain.toCharArray();
		char [] cipher = keyCipher.toCharArray();
		
		String encrypted = encrypt(plaintext, plain, cipher);
		System.out.println("The Ciphertext Is:");
		System.out.println(encrypted);
		
//        char [] alphabet = {'z','y','x','w','v','u','t','s','r','q','p','o','n',
//                            'm','l','k','j','i','h','g','f','e','d','c','b','a'} ;
//        char [] cipher = {'M','N','B','V','C','X','Z','L','K','J','H','G','F',
//        					'D','S','A','P','O','I','U','Y','T','R','E','W','Q'};
	}
}
