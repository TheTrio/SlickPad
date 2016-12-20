import java.util.*;
class Apples{
    public static void main(String args[]){
       
       String[] things = {"Apples", "noobs", "pwnge", "bacon", "goATS"};
       List<String> list1 = new LinkedList<String>();
       
       for(String x : things){
           list.add(x);
       }
       
       String[] things2 = {"sausage", "bacon", "goats", "harry"};
       
       List<String> list2 = new LinkedList<String>();
       
       for(String y : things2){
           list2.add(y);
       }
       
       list1.addAll(list2);
       list = null;
       
       printMe(list1);
       /*removeStuff(list1, 2,5);
       printMe(list1);
       reverseMe(list);*/
       
       
   }
   
   public static void printMe(List<String> l1){
       Iterator<String> it = l1.iterator();
       
       while(it.hasNext()){
           System.out.println(it.next());
       }
       
   }
}