import java.util.ArrayList;
import java.util.Scanner;

public class PlayFairCipher {
	public static char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public static char[][] genMatrix(String key) {
		key = key.toLowerCase();
		char[] keyChars = key.toCharArray();
		char[][] mat = new char[5][5];

		// fill the keyword ahead
		for (int i = 0; i < keyChars.length; i++) {
			mat[i / 5][i % 5] = keyChars[i];
		}
		// fill the rest
		for (int i = keyChars.length, j = 0; i < 25; i++, j++) {
			char letter = alphabet[j];
			// omit keyword letters
			if (key.contains(letter + "")) {
				i--;
				continue;
			}
			// omit J
			if (letter == 'j') {
				i--;
				continue;
			}
			mat[i / 5][i % 5] = letter;
		}

		return mat;
	}

	public static int[] genIndexMap(char[][] mat) {
		// generate a map for fast indexing
		int offset = (int) ('a');
		int[] re = new int[26];
		for (int i = 0; i < 25; i++) {
			char letter = mat[i / 5][i % 5];
			re[letter - offset] = i;
		}
		return re;
	}

	public static void printMatrix(char[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				System.out.print(mat[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static char[] encryptPair(char[][] mat, int[] idxMap, char[] pair) {

		int offset = (int) ('a');
		char[] re = new char[2];
		char a = pair[0];
		char b = pair[1];

		// unary index
		int idxA = idxMap[a - offset];
		int idxB = idxMap[b - offset];

		// coordinate indices
		int ay = idxA / 5;
		int ax = idxA % 5;
		int by = idxB / 5;
		int bx = idxB % 5;

		// rule0: same letters, go for the right one;
		if (ax == bx && ay == by) {
			int ax_ = (ax + 1) % 5;
			int ay_ = ay;
			re[0] = mat[ay_][ax_];
			re[1] = mat[ay_][ax_];
		}
		// rule1: same column, go down for each
		else if (ax == bx) {
			int ax_ = ax;
			int ay_ = (ay + 1) % 5;
			int bx_ = bx;
			int by_ = (by + 1) % 5;
			re[0] = mat[ay_][ax_];
			re[1] = mat[by_][bx_];
		}
		// rule2: same row, go right for each
		else if (ay == by) {
			int ax_ = (ax + 1) % 5;
			int ay_ = ay;
			int bx_ = (bx + 1) % 5;
			int by_ = by;
			re[0] = mat[ay_][ax_];
			re[1] = mat[by_][bx_];

		}
		// rule3: rectangle, go diagonal
		else {
			int ax_ = bx;
			int ay_ = ay;
			int bx_ = ax;
			int by_ = by;
			re[0] = mat[ay_][ax_];
			re[1] = mat[by_][bx_];

		}

		return re;
	}

	public static String encrypt(char[][] mat, int[] idxMap, String text) {
		// replace J with I
		text = text.toLowerCase();
		text = text.replace('j', 'i');
		// append x if necessary
		text = (text.length() % 2 == 1) ? text + 'x' : text;

		// treat pair by pair
		char[] textChars = text.toCharArray();
		char[] ciphertext = new char[text.length()];
		for (int i = 0; i < textChars.length; i += 2) {
			char[] pair = { textChars[i], textChars[i + 1] };
			char[] enPair = encryptPair(mat, idxMap, pair);
			ciphertext[i] = enPair[0];
			ciphertext[i + 1] = enPair[1];
		}

		return new String(ciphertext);
	}

	public static void main(String args[]) {
		// Special treatments:
		// 1. replace J with I
		// 2. adding X if it is odd length
		// 3. all operated on lower case

		System.out.println("< PlayFair Cipher >");
		Scanner getPlaintext = new Scanner(System.in);
		System.out.println("1.Please input the Plaintext  [Must be English letters]");
		String plaintext = getPlaintext.next();
		Scanner getKey = new Scanner(System.in);
		System.out.println("2.Please input the Keyword  [Must be Some English letters]");
		String key = getKey.next();
		key = key.toLowerCase();

		char[][] mat = genMatrix(key);
		int[] idxMap = genIndexMap(mat);

		String encrypted = encrypt(mat, idxMap, plaintext);
		System.out.println("The Ciphertext Is:");
		System.out.println(encrypted);
	}
}
