import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/7/5 10:14
 */
public class DateTest {

    public static void main(String[] args) throws Exception {

        String dts =  "20230705102217011455";
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS");//.withResolverStyle(ResolverStyle.STRICT);
//        TemporalAccessor ret = df.parse(dts);
//        System.out.println(ret);

        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("uuuuMMddHHmmssnnnnnn");//.withResolverStyle(ResolverStyle.STRICT);
        TemporalAccessor ret1 = df1.parse(dts);
        System.out.println(ret1);

    }


}
