import java.math.BigInteger;
import java.util.Scanner;

public class DES_Encrypt {
	
	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static String[] hexChar2binary = { "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", 
												"1000","1001", "1010", "1011", "1100", "1101", "1110", "1111" };
	
	//找出陣列中對應元素的index
	public static int findIndex(char[] array,char value){
		for(int i = 0;i<array.length;i++){
			if(array[i]==value){
				return i;
			}
		}
		return -1;
	}
	
	public static String hex2binary(String str) { //16進制轉4位二進制
		char [] strChar = str.toCharArray();
		String result="";
		for (int i = 0; i < strChar.length; i++) {
			result += hexChar2binary[findIndex(hexChar, strChar[i])];
		}
		return new String(result);
	}
	
	public static String IP(String ptBinary) { //Initial Permutation
		int[] IPTable=
			{
			58, 50, 42, 34, 26, 18, 10, 2,  
			60, 52, 44, 36, 28, 20, 12, 4,
		    62, 54, 46, 38, 30, 22, 14, 6, 
		    64, 56, 48, 40, 32, 24, 16, 8,
		    57, 49, 41, 33, 25, 17, 9,  1,  
		    59, 51, 43, 35, 27, 19, 11, 3,
		    61, 53, 45, 37, 29, 21, 13, 5,  
		    63, 55, 47, 39, 31, 23, 15, 7,
		    };
		char[] ptBinaryChar = ptBinary.toCharArray();
		String result = "";
		for (int i = 0; i < ptBinaryChar.length; i++) {
			result += ptBinaryChar[IPTable[i]-1]; //從0開始計算，所以需要-1
		}
		return new String(result);
	}
	
	public static String PC_1(String keyBinary) {
		int[] PC1Table=
			{
			57,49,41,33,25,17,9,                                                
	        1,58,50,42,34,26,18,
	        10,2,59,51,43,35,27,
	        19,11,3,60,52,44,36,
	        63,55,47,39,31,23,15,
	        7,62,54,46,38,30,22,
	        14,6,61,53,45,37,29,
	        21,13,5,28,20,12,4
	        };
		char[] keyBinaryChar = keyBinary.toCharArray();
		String result = "";
		for (int i = 0; i < PC1Table.length; i++) {
			result += keyBinaryChar[PC1Table[i]-1];
		}
		return new String(result);
	}
	
	public static String split(String str,int option) { // 0->left block 1->right block
		char[] str2Char = str.toCharArray();
		String result = "";
		if (option == 0) {
			for (int i = 0; i < str2Char.length / 2; i++) {
				result += str2Char[i];
			}
		} else if (option == 1) {
			for (int i = str2Char.length / 2; i < str2Char.length ; i++) {
				result += str2Char[i];
			}
		}
		return new String(result);	
	}
	
	public static String keyShift(String keyStr, int round) {
		String result ="";
		if (round == 1 || round == 2 || round == 9 || round == 16) {
			result = keyStr.substring(1, keyStr.length()) + keyStr.charAt(0);	//round 1,2,9,16 shift one bit
		} else {
			result = keyStr.substring(2, keyStr.length()) + keyStr.charAt(0) + keyStr.charAt(1); //In all other rounds shift two bits
		}
		
		return new String(result);
		
	}
	
	public static String PC_2(String Lkey,String Rkey) {
		String combineKey = Lkey+Rkey;
		int PC2Table[] = 
			{ 
				14, 17, 11, 24, 1, 5, 3, 28, 
				15, 6, 21, 10, 23, 19, 12, 4, 
				26, 8, 16, 7, 27, 20, 13, 2, 
				41, 52, 31, 37, 47, 55, 30, 40, 
				51, 45, 33, 48, 44, 49, 39, 56, 
				34, 53, 46, 42, 50, 36, 29, 32 
				};
		char[] keyBinaryChar = combineKey.toCharArray();
		String result = "";
		for (int i = 0; i < PC2Table.length; i++) {
			result += keyBinaryChar[PC2Table[i]-1];
		}
		return new String(result);
	}
	
	public static int getBit(BigInteger b,int n) {
		return b.testBit(n)?1:0;
	}
	
	public static BigInteger f_fuction(String Rpt , String subKey) {
		int Expansion[]=
			{  
				32,1,2,3,4,5,
				4,5,6,7,8,9,
	            8,9,10,11,12,13,
	            12,13,14,15,16,17,
	            16,17,18,19,20,21,
	            20,21,22,23,24,25,
	            24,25,26,27,28,29,
	            28,29,30,31,32,1
			};
		int sbox[][][]={ //[8] [4] [16]
			{                                                                             
            {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
            {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
            {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
            {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
            },
           {
            {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
            {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
            {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
            {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
           },
           {
            {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
            {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
            {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
            {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
           },
           {
            {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
            {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
            {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
            {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
           },
           {
            {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
            {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
            {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
            {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}
           },
           {
            {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
            {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
            {9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
            {4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
           },
           {
            {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
            {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
            {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
            {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
           },
           {
            {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
            {1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
            {7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
            {2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
           }
					};
		int PTable[]=
			{
				16,7,20,21,                                                               
		        29,12,28,17,
		        1,15,23,26,
		        5,18,31,10,
		        2,8,24,14,
		        32,27,3,9,
		        19,13,30,6,
		        22,11,4,25
			};

		char[] RptChar = Rpt.toCharArray();
		String afterEx = "";
		
		for (int i = 0; i < Expansion.length; i++) {
			afterEx += RptChar[Expansion[i]-1];
		}
		BigInteger afterExInt = new BigInteger("1"+afterEx,2);
//		---output test---
//		System.out.println("afterEx:1"+afterEx);
//		System.out.println("afterExInt:"+afterExInt.toString(2));
		
		BigInteger subKeyInt = new BigInteger("1"+subKey,2);
//		---output test---
//		System.out.println("subKey:1"+subKey);
//		System.out.println("subkeyInt:"+subKeyInt.toString(2));
		
		BigInteger beforSbox = afterExInt.xor(subKeyInt); //是用xor 不是and
//		---output test---
//		System.out.println("K1+E(R0):"+beforSbox.toString(2));
		
//		000110110000001011101111111111000111000001110010 key
//		011110100001010101010101011110100001010101010101 E
//		错误->原因 使用的是and 而不是 xor
//		110100000000001000101011110000001000001010000
//		正确
//		011000010001011110111010100001100110010100100111
		
//		如果在前面加1？
		
//		case1
		
//		String beforeSboxStr = getBit(beforSbox, 48)+String.valueOf(beforSbox.toString(2));
		String beforeSboxStr ="";
//		防止biginteger類型的第一位是0
		for (int i = 0; i < 48; i++) { 
			beforeSboxStr += getBit(beforSbox, 47-i);
		}
//		---output test---
//		System.out.println("beforeSboxStr:"+beforeSboxStr);
		
		char[] goToSbox = beforeSboxStr.toCharArray();
		
//		System.out.println("goToSbox:");
//		for (int i = 0; i < goToSbox.length; i++) {
//			System.out.print(goToSbox[i]);
//		}
		
//		S1
//		String yStr = String.valueOf(goToSbox[0])+String.valueOf(goToSbox[5]);
//		int y = Integer.parseInt(yStr,2);
//		System.out.println("yStr:"+yStr);
//		System.out.println("y:"+y);
		
//		String xStr = String.valueOf(goToSbox[1])+String.valueOf(goToSbox[2])+String.valueOf(goToSbox[3])+String.valueOf(goToSbox[4]);
//		int x = Integer.parseInt(xStr,2);
//		System.out.println("xStr:"+xStr);
//		System.out.println("x:"+x);
		
//		int afterS1 = sbox[0][y][x];
//		String afterS1Binary = hexChar2binary[afterS1];
//		二進制少一個0問題待解決
//		利用前面使用的 hex2binary 方法
//		System.out.println("afterS1:"+afterS1);
//		System.out.println("afterS1Binary:"+afterS1Binary);
		
//		loop to create S1-S8
		String[] afterSbox = new String[8];
		String yStr,xStr = "";
		int y,x = 0;
		for (int i = 0 , f = 0 , l = 1; i < 8; i++ , f+=6 , l+=6) {
			yStr = String.valueOf(goToSbox[f])+String.valueOf(goToSbox[f+5]);
			y = Integer.parseInt(yStr, 2);
//			---output test---
//			System.out.println("yStr:"+yStr);
//			System.out.println("y:"+y);
			
			xStr = String.valueOf(goToSbox[l])+String.valueOf(goToSbox[l+1])+String.valueOf(goToSbox[l+2])+String.valueOf(goToSbox[l+3]);
			x = Integer.parseInt(xStr,2);
//			---output test---
//			System.out.println("xStr:"+xStr);
//			System.out.println("x:"+x);
			
			afterSbox[i] = hexChar2binary[sbox[i][y][x]];
//			---output test---
//			System.out.println("afterSbox:"+sbox[i][y][x]);
//			System.out.println("afterSboxBinary:"+afterSbox[i]);
		}
		String beforePStr ="";
		for (int i = 0; i < afterSbox.length; i++) {
			beforePStr += afterSbox[i];
		}
//		---output test---
//		System.out.println("beforePStr:"+beforePStr);
		
		char[] beforePChar = beforePStr.toCharArray();
		String resultStr = "";
		for (int i = 0; i < PTable.length; i++) {
			resultStr += beforePChar[PTable[i]-1];
		}
//		是否需要在这个地方+1
		BigInteger result = new BigInteger("1"+resultStr,2);
//		---output test---
//		System.out.println("resultStr:"+"1"+resultStr);
//		System.out.println("result:"+result.toString(2));
		
		return result;
	}
	
	public static String IP_reverse(String str) {
		int[] IP_reverseTable=
			{
				40, 8, 48, 16, 56, 24, 64, 32, 
				39, 7, 47, 15, 55, 23, 63, 31, 
		        38, 6, 46, 14, 54, 22, 62, 30, 
		        37, 5, 45, 13, 53, 21, 61, 29,
		        36, 4, 44, 12, 52, 20, 60, 28, 
		        35, 3, 43, 11, 51, 19, 59, 27,
		        34, 2, 42, 10, 50, 18, 58, 26, 
		        33, 1, 41, 9, 49, 17, 57, 25
		 };
		char[] str2Char = str.toCharArray();
		String result = "";
		for (int i = 0; i < str2Char.length; i++) {
			result += str2Char[IP_reverseTable[i]-1]; //從0開始計算，所以需要-1
		}
		return new String(result);
	}
	

	
	public static void main(String[] args) {
		System.out.println("< Data Encrypt Standard >");
		Scanner getPlaintext = new Scanner(System.in);
		System.out.println("1.Please input the Plaintext [Please input 16 Hexadecimal eg.ABCDEF0123456789]");
		String plaintext = getPlaintext.next();
//		plaintext(16) to binary
		String ptBinary = hex2binary(plaintext);
//		---output test---
//		System.out.println("ptBinary:" + ptBinary);
		
		Scanner getKey = new Scanner(System.in);
		System.out.println("2.Please input the Key️ [Please input 16 Hexadecimal eg.AFAFAFAFAFAFAFAF]");
		String key = getKey.next();
//		key(16) to binary
		String keyBinary = hex2binary(key);
//		---output test---
//		System.out.println("keyBinary:" + keyBinary);
		
		
	
//		key Schedule ----------------------------------------------------------------------
//		PC-1
		String afterPC_1KeyStr = PC_1(keyBinary);
//		---output test---
//		System.out.println("afterPC_1KeyStr:" + afterPC_1KeyStr);
		
//		Split after pc-1 key (56bits) to left & right block (each 28 bits)
		String key_leftBlock = split(afterPC_1KeyStr, 0) ;
		String key_rightBlock = split(afterPC_1KeyStr, 1);
//		---output test---
//		System.out.println("key_leftBlock:" + key_leftBlock);
//		System.out.println("key_rightBlock:" + key_rightBlock);
		
//		key shift
		String[] Lkey = new String[17];
		Lkey[0] = key_leftBlock;		//add master key to first position in array
		String[] Rkey = new String[17];
		Rkey[0] = key_rightBlock;
		for (int i = 1; i <= 16; i++) {
			Lkey[i] = keyShift(Lkey[i-1], i);
			Rkey[i] = keyShift(Rkey[i-1], i);
		}
		
//		---output test---
//		for (int i = 0; i < Lkey.length; i++) {
//			System.out.println("L"+i+":"+Lkey[i]);
//			System.out.println("R"+i+":"+Rkey[i]);
//		}
		
//		pc-2 and combine keys
		String[] subKey = new String[16];
		for (int i = 0; i < subKey.length; i++) {
			subKey[i] = PC_2(Lkey[i+1], Rkey[i+1]);
		}
		
//		---output test---
//		for (int i = 0; i < subKey.length; i++) {
//		System.out.println("subKey"+i+":"+subKey[i]);
//	}
//		key Schedule ----------------------------------------------------------------------
		

//		initial permutation
		String afterIPPtStr = IP(ptBinary);
//		---output test---
//		System.out.println("afterIPPtStr:" + afterIPPtStr);
		
//		Split plaintext to left block & right block (each 32bits)
		String pt_leftBlock = split(afterIPPtStr, 0) ;
		String pt_rightBlock = split(afterIPPtStr, 1);
		
//		---output test---
//		System.out.println("pt_leftBlock:" + pt_leftBlock);
//		System.out.println("pt_rightBlock:" + pt_rightBlock);

		String[] Lpt = new String[17];
		Lpt[0] = pt_leftBlock;
		String[] Rpt = new String[17];
		Rpt[0] = pt_rightBlock;
		for (int i = 1; i <= 16; i++) {
			Lpt[i] = Rpt[i-1];
			BigInteger lastLpt = new BigInteger(Lpt[i-1],2);
			String rpt= (lastLpt.xor(f_fuction(Rpt[i-1], subKey[i-1]))).toString(2); //將subkey[i-1] 改為 subkey[16-i] 就是從加密變為解密的過程
//			---output test---
//			System.out.println("rpt:"+rpt);
			
			Rpt[i]= rpt.substring(1, 33); 
//			---output test---
//			System.out.println(i+"LPT:"+Lpt[i]);
//			System.out.println(i+"RPT:"+Rpt[i]);
//			System.out.println(" ");
		}
		String beforeIpRev = Rpt[16]+Lpt[16];
		String finalBinary = IP_reverse(beforeIpRev);
//		---output test---
//		System.out.println("FinalBinary:"+finalBinary);
		
		String[] finalBinaryChar = new String[16];
		for (int i = 0 , j = 0; i < finalBinaryChar.length; i++ , j+=4) {
			finalBinaryChar[i] = finalBinary.substring(j, j+4);
//			---output test---
//			System.out.println("finalBinaryChar"+i+":"+finalBinaryChar[i]);
		}
		String finalResult = "";
		for (int i = 0; i < finalBinaryChar.length; i++) {
			switch (finalBinaryChar[i]) {
			case "0000":
				finalResult += "0";
				break;
			case "0001":
				finalResult += "1";
				break;
			case "0010":
				finalResult += "2";
				break;
			case "0011":
				finalResult += "3";
				break;
			case "0100":
				finalResult += "4";
				break;
			case "0101":
				finalResult += "5";
				break;
			case "0110":
				finalResult += "6";
				break;
			case "0111":
				finalResult += "7";
				break;
			case "1000":
				finalResult += "8";
				break;
			case "1001":
				finalResult += "9";
				break;
			case "1010":
				finalResult += "A";
				break;
			case "1011":
				finalResult += "B";
				break;
			case "1100":
				finalResult += "C";
				break;
			case "1101":
				finalResult += "D";
				break;
			case "1110":
				finalResult += "E";
				break;
			case "1111":
				finalResult += "F";
				break;
			default:
				System.out.println("error");
			}
		}
		System.out.println("Ciphertext:\n"+finalResult);
	}

}
