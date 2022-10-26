package Test;

public class Test2 extends Test1{
    public void ss(){
        System.out.println("子类方法");
    }

    public static void main(String[] args) {
        Test2 t = new Test2();
        t.ss();
    }
}
