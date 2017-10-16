import java.util.Scanner;
class Apples{
    public static void main(String args[]){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter String");
        String sen = s.nextLine();
        System.out.println("Enter Word");
        String word = s.next();
        int count = (sen.length() - sen.replace(word, "").length())/word.length();
        System.out.println(count);
        System.out.println(sen.replace(word, "").trim());
    }
}

