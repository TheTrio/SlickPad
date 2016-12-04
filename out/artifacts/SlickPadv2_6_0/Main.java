import java.util.Scanner;
class Apples{
    public static void main(String args[]){
        Scanner x = new Scanner();
        int x = 0;
        long fact = 1;
        while(x!=-99){
        System.out.print("Enter Your Number : ");
        x = x.nextInt();
        if(x>0){
            for(int i=1;i<x;i++){
                fact*=i;
            }
         System.out.println("The Factorial is "+ fact);
         }
      }
        
    }
}
