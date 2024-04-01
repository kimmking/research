/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/23 00:41
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("invoke Demo.main()");
        Demo demo = new Demo();
        demo.hello();
        System.out.println(" ======================== ");
        java.util.Date date = new java.util.Date();
        System.out.println(date.toString());
    }

    void hello(){
        System.out.println("==> hello.");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
