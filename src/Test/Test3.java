package Test;

public class Test3 {
    public void ss(){
        int s = 1;
        System.out.println("这是if之前的");
        if(s == 1){
            System.out.println("这是if中的");
            return;
        }
        System.out.println("这是return之后的");
    }

    public static void main(String[] args) {
        int h2 = 2;
        new Test3().ss();
        if(h2 == 2){
            System.out.println("这是if中的main");
            return;
        }
        System.out.println("这是return之后的main");
    }
}
