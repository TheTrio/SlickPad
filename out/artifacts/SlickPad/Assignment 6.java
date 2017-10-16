import java.util.Scanner;
class Apples{
    public static void main(String args[]){
       Scanner sc = new Scanner(System.in);
       System.out.println("Enter String 1");
       String str1 = sc.next();
       System.out.println("Enter String 2");
       String str2 = sc.next();
       String str3 = "";
       for(int i=0;i<str1.length() | i<str2.length();i++) {
	char c;
	   if(i<str1.length()){
           c= Character.toLowerCase(str1.charAt(i));
           if(c=='a' || c=='e' || c=='i' || c=='o' || c=='u'){
               str3+=str1.charAt(i);
           }
}
	 if(i<str2.length()){
           c = Character.toLowerCase(str2.charAt(i));
           if(!(c=='a' || c=='e' || c=='i' || c=='o' || c=='u')){
               str3+=str2.charAt(i);
           }
}
       }
	System.out.println(str3);
       str3 = str3.toLowerCase();
       for(char c = 'a';c<='z';c++){
           int count = str3.length() - str3.replace(""+ c, "").length();
           if(count!=0)   
	System.out.println(c + "--------- > " + count);
       }
       
    }
}
