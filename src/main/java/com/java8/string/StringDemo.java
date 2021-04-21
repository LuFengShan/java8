package com.java8.string;

import com.alibaba.fastjson.JSON;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 */
public class StringDemo {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        // 任意数量的字符串连接到具有给定分隔符的单个字符串
        String join = String.join(":", "name", "guang", "age");// => foobar:foo:bar
        System.out.println(join);

        // 也可以把字符集合中的字符给拼接起来
        List<String> strings = new LinkedList<>();
        strings.add("车系_分类");
        strings.add("宝马5x_新车上市");
        strings.add("sg_fx");
        String message = String.join(",", strings); // "Java is cool"
        System.out.println(message);

        List<Integer> ints = new ArrayList<>();
        String.join(",", (CharSequence) ints); // "Java is cool"
        System.out.println(message);

        Set<String> strings1 = new LinkedHashSet<>();
        strings1.add("Java");
        strings1.add("is");
        strings1.add("very");
        strings1.add("cool");
        String message1 = String.join("-", strings1); // "Java-is-very-cool"
        System.out.println(message1);

        // chars为字符串的所有字符创建一个流
        String str = "dklja:孙:wang";
        String collect = str.chars()
                .distinct()
                .mapToObj(c -> String.valueOf((char) c))
                .sorted()
                .collect(Collectors.joining("、"));
        System.out.println(collect);

        // 不仅字符串而且正则表达式模式都受益于流。我们可以为任何模式拆分字符串，并创建一个要处理的流，而不是将字符串拆分为每个字符的流
        String bar = Pattern.compile(":") // 传入正则表达式
                .splitAsStream("foobar:foo:bar") // 根据正则表达式，把字符拆分为foobar,foo,bar三个子字符串
                .filter(s -> s.contains("bar"))
                .sorted()
                .collect(Collectors.joining(":"));// bar:foobar
        System.out.println(bar);

        // 可以将正则表达式模式转换为谓词。例如，这些谓词可用于过滤字符串流：
        Pattern pattern = Pattern.compile(".*@qq\\.com");
        long count = Stream.of("58298932@gmail.com", "66897825@hotmail.com", "google@qq.com")
                .filter(pattern.asPredicate())
                .count();
        System.out.println(count); // 1

        //
        String dem = "https://k3.autoimg.cn/koubeidfs/g3/M0B/FA/CC/240x180_c42_autohomecar__ChcCRV25gOaAGc4kAAQ6m_VDyoI09..jpg";
        Map<String, Object> map = new HashMap();
        map.put("aa", "a,b,c");
        map.put("bb", dem);
        System.out.println(map.get("123"));
        System.out.println(map.get("aa"));
        Object graphicImgList = map.get("bb");
        if (Objects.nonNull(graphicImgList)) {
            String[] array = Pattern.compile(",")
                    .splitAsStream(String.valueOf(graphicImgList))
                    .toArray(String[]::new);
            if (array.length > 2) {
                System.out.println(array);
            }

        }
        String st = null;
        System.out.println(st.toString());
    }

    @Test
    public void tes() {
        String sql = "select \n" +
                "v.*\n" +
                "FROM\n" +
                "(\n" +
                "SELECT\n" +
                "v.PROVINCE_DEPT as provinceDept,\n" +
                "START_LONGITUDE AS startLng,\n" +
                "START_LATITUDE AS startLat,\n" +
                "END_LONGITUDE AS endLng,\n" +
                "END_LATITUDE AS endLat,\n" +
                "row_number() over (partition by v.PROVINCE_DEPT order by c.km desc) rn\n" +
                "FROM\n" +
                "IMPALA_VEH_DAY_SINGLE_RUN c,\n" +
                "SYS_VEHICLE_NATIONAL v\n" +
                "WHERE 1 = 1 \n" +
                "AND c.VID = V.UUID\n" +
                "AND c.km > 1\n" +
                "AND c.km < 200\n" +
                "AND\n" +
                "c.REPORT_DATE >= '2019-08-10' \n" +
                "AND\n" +
                "c.REPORT_DATE <= '2019-08-11' \n" +
                ") v\n" +
                "where v.rn <= 3000;\n";
        System.out.println(sql);
    }

    @Test
    public void test22() {
        Map<String, String> map = new HashMap<>(20);
        // 充电开始时间
        int start = Integer.valueOf("0").intValue();
        // 充电结束时间
        int end = Integer.valueOf("2").intValue();
        System.out.println(start);
        System.out.println(end);

        IntStream.range(start, end + 1)
                // 时间段和充电的车进行映射存放
                .forEach(i -> map.put(String.valueOf(i >> 1), "i" + i));

        map.entrySet()
                .forEach(entyt -> System.out.println(entyt.getKey() + " : " + entyt.getValue()));
    }

    @Test
    public void testc() {
        String str = "0.3682_1.0588_-0.5869_-0.5473_0.1765_0.3015_-0.434_0.7884_0.4992_0.7487_-0.1397_0.2432_0.9142_-2.2131_1.0858_1.0266_-0.6108_-1.1997_0.9608_-0.3757_1.3428_-0.434_-0.3128_-1.7622_-0.2311_0.3211_0.0986_-1.4138_-1.1983_0.0049_1.4477_-0.2311_0.3794_0.8154_0.6125_-0.822_-0.1374_0.1974_-0.2311_0.3015_-0.0595_-0.1262_-0.9666_-0.8304_-0.8835_0.8933_-0.3113_0.9599_0.4325_-0.5838_-0.2311_1.0649_0.9599_1.4281_-5.2467_-1.258_-0.8356_-0.8565_-0.8835_0.3075_-0.3757_1.5457_0.7884_0.8154_0.6904_0.4409_-0.2978_-0.1262_-0.5202_1.5061_0.8154_0.9016_-1.2393_-0.7119_-0.4423_0.8153_-0.4027_-0.8491_1.3615_0.9329_-0.3696_0.9921_-0.2311_0.4349_-0.634_1.0266_1.1045_-1.0924_1.4281_-0.1532_0.3015_-1.8498_-0.0865_-0.1644_-1.1008_0.96";
        List<String> collect = Pattern.compile("_")
                .splitAsStream(str)
                .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect));
    }

    @Test
    public void streamPeek() {
        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());

        List<Object> objectList = new ArrayList<>();
        objectList.add("a");
        objectList.add(1);
        objectList.add(1.1);
        List<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        integerList.add(20);
        integerList.add(200);
//		integerList.addAll(objectList);

        objectList.addAll(integerList);
        objectList.forEach(System.out::println);

        Long l1 = 2345565L;
        Long l2 = 234L;
        System.out.println(l1 - l2 > 0L);
        System.out.println(l1 - l2 > 0);
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Long.MAX_VALUE);
        String num = "3845284030";
        int i = Integer.parseInt(num);
        System.out.println(i);

    }

    @Test
    public void stringFormat() {
        String format = String.format("主feed推荐实验日报-%s", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        logger.error(() -> format);

        System.out.println(Objects.equals(new Integer(1), 1));
        Integer integer = Integer.valueOf("1");
        System.out.println(Objects.equals(1, integer));
        String a = "q~tb\u007Fyt>q``>QsdyfydiDxbuqt";
        System.out.println(a(a));

        String tableHeader = String.format("<tr><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th></tr>",
                "序号", "最新操作时间", "实验创建时间", "实验名称", "分桶名称", "实验/对照", "效果", "差异", "最新操作", "下一步计划", "CTR", "流量占比", "大盘基线CTR");
        System.out.println(tableHeader);
        String col = String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                "序号", "最新操作时间", "实验创建时间", "实验名称", "分桶名称", "实验/对照", "效果", "差异", "最新操作", "下一步计划", "CTR", "流量占比", "大盘基线CTR");
        System.out.println(col);

        System.out.println(String.format("%.2f", Float.valueOf("-0.1272") * 100) + "%");
        System.out.println(isNumeric("09[]]"));
        System.out.println(isNumeric("912121212"));
        System.out.println(isNumeric("-09"));
        System.out.println(isNumeric("[{09"));
        Float f = null;
        System.out.println(f);
        Map<String, Float> map = new HashMap<>();
        map.put("aa", f);
        map.put("aa", f);
        System.out.println(map);
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (charArray[i] ^ 16);
        }
        return String.valueOf(charArray);
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

}
