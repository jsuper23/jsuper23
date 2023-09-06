import java.util.Scanner;

public class CaesarCipher {

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please choose: 1 = encrypt or 2 = decrypt ");
        int choice = input.nextInt();

        // String code1 = CaesarCipher.encode("", 0);
        // String code2 = CaesarCipher.decode("", 0);

        // If user inputs 1, the encode method will run
        if (choice == 1) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter a message to encrypt: ");
            String str = scan.nextLine();
            System.out.println("Enter a shift value: ");
            int num = scan.nextInt();
            System.out.println("The encrypted message is: " + encode(str, num));
            scan.close();
        }
        // If user inputs 2, the decode method will run
        if (choice == 2) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter a message to decode: ");
            String str = scan.nextLine();
            System.out.println("Enter a shift value: ");
            int num = scan.nextInt();
            System.out.println("The decrypted message is: " + decode(str, num));
            scan.close();
        }
        // If user does not enter 1 or 2, choice will be displayed
        else {
            System.out.println(choice);
        }
        input.close();
    }

    public static String encode(String msg, int shift) {

        String encryptMessage = "";
        char alpha;
        // Shifts character
        for (int i = 0; i < msg.length(); i++) {
            alpha = msg.charAt(i);
            // Includes alphabet from a to z
            if (alpha >= 32 && alpha <= 126) {
                // Shift characters by user input value
                alpha = (char) (alpha + shift);
                // Repositions starting point to a if value is greater than z
                if (alpha > 126) {
                    alpha = (char) (alpha + 32 - 126 - 1);
                }
                encryptMessage = encryptMessage + alpha;
            }

        }
        return encryptMessage;
    }
    /**This is the correction for the cipher code
     * public static String encode(String msg, int key){
     * String encodeMsg = "";
     * 
     * for(int i = 0; i < msg.length(); i++){
     *  encodeMsg += (char)(msg.charAt(i)+ key);
     * }
     * return encoding.toString();
     * }
     */

    public static String decode(String msg, int shift) {

        String decodeMessage = "";
        char alpha;
        for (int i = 0; i < msg.length(); i++) {
            alpha = msg.charAt(i);
            // Includes alphabet from A to Z
            if (alpha >= 32 && alpha <= 126) {
                alpha = (char) (alpha + shift);
                if (alpha < 32) {
                    alpha = (char) (alpha - 32 + 126 + 1);
                }
                decodeMessage = decodeMessage + alpha;
            }
        }
        return decodeMessage;

    }
}