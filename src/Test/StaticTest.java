package Test;

public class StaticTest {
    public StaticTest(){
        System.out.println("这是无参构造法");
    }
    static {
        System.out.println("这是static修饰的代码块");
    }
    public  static String ss(){
       String s = "这是方法";
       return s;
    }
}
