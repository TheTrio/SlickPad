class Apples{
    public static void main(String args[]){
        Double d1 = new Double(12.0);
        Double d2 = d1;
        d1 = d1 + 1;
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d1 == d2);
   }
}