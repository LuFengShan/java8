package com.java8.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * fastjson示例
 */
public class FastjsonDemo {
    private static final Logger logger = LoggerFactory.getLogger(FastjsonDemo.class);
    private Person p;
    private Person p1;

    @BeforeEach
    public void initPerson() {
        p = new Person();
        p.setFirstName("san");
        p.setLastName("zhang");
        p1 = new Person();
        p1.setFirstName("si");
        p1.setLastName("li");
    }

    @Test
    public void testObjectMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", "582989326");
        Map<String, String> map1 = new HashMap<>();
        map1.put("chenghu", "sgx");
        map1.put("bandName", "wow");
        String s = JSON.toJSONString(map1);
        s = s.replace('\"', '\'');
        map.put("spliceMap", s);
        System.out.println(JSON.toJSONString(map));
    }

    /**
     * 直接把对象序列化成json格式
     */
    @Test
    public void testObjectMapJson() {
        logger.info(() -> JSON.toJSONString(p));
        logger.info(() -> JSON.toJSONString(p1));
    }

    /**
     * 把对象转化为json格式 ，但是没有key值，只存value
     */
    @Test
    public void testObjectMapJsonWithSerializerFeature() {
        String string = JSON.toJSONString(p, SerializerFeature.BeanToArray);
        logger.info(() -> string); // ["san", "zhang"]
        if (string.startsWith("["))
            logger.info(() -> "true");
    }

    /**
     * 一对多的一个转换
     */
    @Test
    public void testArraysMapJson() {
        PersonList personList = new PersonList();

        List<Person> list = new ArrayList<>();
        list.add(p);
        list.add(p1);

        personList.setCone(1001);
        personList.setList(list);

        logger.info(() -> JSON.toJSONString(personList)); // {"cone":1001,"list":[["san","zhang"],["si","li"]]}

        JSONObject parEmpty = new JSONObject();
        parEmpty.put("UserIs24hTo7d", "0:否");
        parEmpty.put("UserInterestShortSeri", Collections.EMPTY_LIST);
        parEmpty.put("rtSeries", Collections.EMPTY_LIST);
        logger.info(() -> parEmpty.toJSONString()); // {"cone":1001,"list":[["san","zhang"],["si","li"]]}

    }

    @Test
    public void testGPSToBd() {
        String dir = "C:\\Users\\Administrator\\Desktop\\经纬度\\h2data";
        String tarDir = "d:/经纬度";
        List<String> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) { // 判断这个文件是不是一个文件夹，如果不是文件夹就存入文件的名字
                    list.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String pathFile : list) {
            // 读取文件的位置
            Path pathRead = Paths.get(dir + File.separator + pathFile);
            logger.info(() -> "读取文件的位置 ： " + pathRead.toString());
            // 生成文件的位置
            Path pathWrite = Paths.get(tarDir + File.separator + pathFile);
            logger.info(() -> "生成文件的位置 ： " + pathWrite.toString());
            try (BufferedReader reader = Files.newBufferedReader(pathRead);
                 BufferedWriter writer = Files.newBufferedWriter(pathWrite)) {
                // 读取文档中的所有内容
                List<List<Double>> lonLats = reader.lines()
                        .skip(1)
                        .map(s -> {
                            List<String> strings = Pattern.compile(",")
                                    .splitAsStream(s)
                                    .collect(Collectors.toList());
                            Gps gps = PositionUtil.gcj02_To_Bd09(Double.parseDouble(strings.get(3)), Double.parseDouble(strings.get(2)));
                            return Arrays.asList(gps.getWgLon(), gps.getWgLat());
                            // return String.join(",", String.valueOf(gps.getWgLon()), String.valueOf(gps.getWgLat()));
                        })
                        .collect(Collectors.toList());
                // 处理后以转换成json
                String result = JSON.toJSONString(lonLats);
                // 把json存入文档中
                writer.write(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.info(() -> "文件转换完毕！");
    }

    @Test
    public void testGPSToGd() {
        String dir = "C:/Users/Administrator/Documents/异地行驶(2)";
        String tarDir = "d:/异地行驶(2)";
        List<String> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) { // 判断这个文件是不是一个文件夹，如果不是文件夹就存入文件的名字
                    list.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String pathFile : list) {

            // 读取文件的位置
            // String pathFile = "C:\\Users\\Administrator\\Documents\\异地行驶(2)\\js180.json";
            Path pathRead = Paths.get(dir + File.separator + pathFile);
            logger.info(() -> "读取json文件的位置 ： " + pathRead.toString());
            // 生成文件的位置
            Path pathWrite = Paths.get(tarDir + File.separator + pathFile);
            logger.info(() -> "生成文件的位置 ： " + pathWrite.toString());
            try (BufferedReader reader = Files.newBufferedReader(pathRead);
                 BufferedWriter writer = Files.newBufferedWriter(pathWrite)) {
                // 读取json文档中的所有内容
                String collect = reader.lines()
                        .collect(Collectors.joining());
                // json内容转换成对象
                List<GpsDome> gpsDomeList = JSON.toJavaObject(JSONObject.parseObject(collect), JsonRootBean.class)
                        // 取得所有的坐标
                        .getDots()
                        .stream()
                        // 转换成我们要的高德的坐标
                        .map(dots -> PositionUtil.gps84_To_Gcj021(dots.getType()
                                , div(String.valueOf(dots.getY()), String.valueOf(1000000.00), 6).doubleValue()
                                , div(String.valueOf(dots.getX()), String.valueOf(1000000.00), 6).doubleValue()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                // 处理后以转换成json
                String result = JSON.toJSONString(gpsDomeList);
                // 把json存入文档中
                writer.write(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        logger.info(() -> "文件转换完毕！");
    }

    public static BigDecimal div(String value1, String value2, int scale) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_EVEN);
    }


    @Test
    public void demo() {
        String abVoltage = "-0.8407_-0.7713_-0.7785_-1.8508_-0.9836_0.1431_-1.116_1.2101_0.4105_-0.6777_0.5846_-1.5804_0.59_-0.2254_1.276_0.2406_-1.0498_0.1518_-0.4241_-0.2254_-1.0498_-0.0998_0.0633_-0.4071_0.5846_-1.1017_-1.1409_-1.6047_-0.0727_-0.699_-0.5476_-0.2254_-0.5464_-0.0905_0.0783_-0.2108_0.4295_0.1678_0.7175_-0.8913_-0.1567_-2.1825_2.1573_0.9555_0.6824_-0.5911_0.9569_-0.4775_1.8271_1.8032_0.1685_0.6004_-0.773_-0.2332_-0.3162_0.4732_0.6824_0.6004_0.7449_1.6444_0.5342_0.7587_0.2848_0.4172_0.2572_0.9757_0.5846_0.052_-0.0844_0.3369_-0.67_0.6056_0.9432_1.7249_0.069_0.0502_0.6776_0.069_-0.2026_-0.1567_0.8307_0.1935_2.0751_0.7666_-5.1494_-1.6331_-0.3849_-0.6155_-1.2301_-0.0673_1.0256_-0.069_0.6135_0.0278_-0.0243_0.411";
        String[] abString = abVoltage.split("_");
        HashMap<Integer, Double> map;
        for (int i = 0; i < abString.length; i++) {
            map = new HashMap<>();
            map.put(i + 1, Double.valueOf(abString[i]));
            System.out.println(JSON.toJSONString(map));
        }
    }

    @Test
    public void sanxian() {
        String testText = "[{'anchorOperateText': '测试推送kafka2', 'contentId': 83, 'contentName': '七步买车-初始推荐', 'createUser': 'wangpengfeiwb3347', 'createdStime': 1616482204000, 'id': 62, 'isDel': 0, 'itemId': '456', 'modifiedStime': 1616482204000, 'productId': '102', 'productName': '用户产品中心', 'productTypeId': '1021', 'productTypeName': '七步买车', 'updateUser': 'wangpengfeiwb3347'}, {'anchorOperateText': '测试推送kafka1', 'contentId': 70024, 'contentName': '品牌-现代', 'createUser': 'wangpengfeiwb3347', 'createdStime': 1616482204000, 'id': 61, 'isDel': 0, 'itemId': '123', 'modifiedStime': 1616482204000, 'productId': '10002', 'productName': '品牌', 'productTypeId': '1027', 'productTypeName': '品牌', 'updateUser': 'wangpengfeiwb3347'}, {'anchorOperateText': '8206758', 'contentId': 14, 'contentName': '车家号视频', 'createUser': 'liujingli', 'createdStime': 1616397819000, 'id': 60, 'isDel': 0, 'itemId': '8206758', 'itemTitle': '乘联会：3月国内汽车零售量预计174万辆', 'modifiedStime': 1616397819000, 'productId': '207', 'productName': '车家号', 'productTypeId': '1003', 'productTypeName': '车家号', 'updateUser': 'liujingli'}, {'anchorOperateText': '8206794', 'contentId': 12, 'contentName': '车家号长文', 'createUser': 'liujingli', 'createdStime': 1616397819000, 'id': 59, 'isDel': 0, 'itemId': '8206794', 'itemTitle': '动感时尚的造型 奔驰C43 AMG实车 网友：爱了爱了！', 'modifiedStime': 1616397819000, 'productId': '207', 'productName': '车家号', 'productTypeId': '1003', 'productTypeName': '车家号', 'updateUser': 'liujingli'}, {'anchorOperateText': '8206813', 'contentId': 13, 'contentName': '车家号短文', 'createUser': 'liujingli', 'createdStime': 1616397819000, 'id': 58, 'isDel': 0, 'itemId': '8206813', 'itemTitle': '保时捷在中国赚得盆满钵盈。日前保时捷发布2020年财务报告显示，去年全球范围内累计交付了27.2万辆汽车，营业收入较2019年增长了1亿元欧元。此外中国市场贡献最大，数据显示，2020年保时捷在华交付量为88,968辆，同', 'modifiedStime': 1616397819000, 'productId': '207', 'productName': '车家号', 'productTypeId': '1003', 'productTypeName': '车家号', 'updateUser': 'liujingli'}, {'anchorOperateText': '2193641', 'contentId': 3, 'contentName': '视频', 'createUser': 'liujingli', 'createdStime': 1616397819000, 'id': 57, 'isDel': 0, 'itemId': '2193641', 'itemTitle': '大众帕萨特  售价18.59-28.29万元', 'modifiedStime': 1616397819000, 'productId': '203', 'productName': '资讯部', 'productTypeId': '1001', 'productTypeName': '资讯', 'updateUser': 'liujingli'}, {'anchorOperateText': '1129486', 'contentId': 1, 'contentName': '文章', 'createUser': 'liujingli', 'createdStime': 1616397819000, 'id': 56, 'isDel': 0, 'itemId': '1129486', 'itemLongTitle': '富有美国军队气息 Jeep自由光Freedom版', 'itemTitle': '致敬美国军队 Jeep自由光推出Freedom版 这个涂装你喜欢吗？', 'modifiedStime': 1616397819000, 'productId': '203', 'productName': '资讯部', 'productTypeId': '1001', 'productTypeName': '资讯', 'updateUser': 'liujingli'}, {'anchorOperateText': '49', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 53, 'isDel': 0, 'itemId': '919643', 'itemTitle': '东风悦达起亚起亚K3PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '48', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 52, 'isDel': 0, 'itemId': '919702', 'itemTitle': '比亚迪宋MAXPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '47', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 51, 'isDel': 0, 'itemId': '919741', 'itemTitle': '开瑞汽车开瑞K60PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '46', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 50, 'isDel': 0, 'itemId': '919632', 'itemTitle': 'MINI JCWMINI JCW CLUBMANPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '45', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 49, 'isDel': 0, 'itemId': '919635', 'itemTitle': '一汽马自达阿特兹PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '44', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 48, 'isDel': 0, 'itemId': '919633', 'itemTitle': '玛莎拉蒂LevantePK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '43', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 47, 'isDel': 0, 'itemId': '919634', 'itemTitle': '玛莎拉蒂总裁PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '42', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 46, 'isDel': 0, 'itemId': '919740', 'itemTitle': '一汽奔腾奔腾T99PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '41', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 45, 'isDel': 0, 'itemId': '919749', 'itemTitle': '上汽通用五菱宝骏310WPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '40', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 44, 'isDel': 0, 'itemId': '919748', 'itemTitle': '上汽通用五菱宝骏310PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '39', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 43, 'isDel': 0, 'itemId': '919639', 'itemTitle': '广汽讴歌讴歌RDXPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '38', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 42, 'isDel': 0, 'itemId': '919747', 'itemTitle': '上汽通用五菱五菱宏光S3PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '37', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 41, 'isDel': 0, 'itemId': '919750', 'itemTitle': '上汽通用五菱宝骏510PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '36', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 40, 'isDel': 0, 'itemId': '919746', 'itemTitle': '上汽通用五菱五菱宏光PLUSPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '35', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 39, 'isDel': 0, 'itemId': '919631', 'itemTitle': 'MINI JCWMINI JCWPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '34', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 38, 'isDel': 0, 'itemId': '919627', 'itemTitle': '雷克萨斯雷克萨斯UXPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '33', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 37, 'isDel': 0, 'itemId': '919625', 'itemTitle': '雷克萨斯雷克萨斯NXPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '32', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 36, 'isDel': 0, 'itemId': '919622', 'itemTitle': '雷克萨斯雷克萨斯ESPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '31', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 35, 'isDel': 0, 'itemId': '919623', 'itemTitle': '雷克萨斯雷克萨斯LCPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '30', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 34, 'isDel': 0, 'itemId': '919661', 'itemTitle': '上汽大众斯柯达柯迪亚克PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '29', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 33, 'isDel': 0, 'itemId': '919752', 'itemTitle': '上汽通用五菱宝骏730PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '28', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 32, 'isDel': 0, 'itemId': '919626', 'itemTitle': '雷克萨斯雷克萨斯RXPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '27', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 31, 'isDel': 0, 'itemId': '919751', 'itemTitle': '上汽通用五菱宝骏530PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '26', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 30, 'isDel': 0, 'itemId': '919744', 'itemTitle': '东风乘用车东风风神AX7PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '25', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 29, 'isDel': 0, 'itemId': '919756', 'itemTitle': '东风日产启辰T90PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '24', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 28, 'isDel': 0, 'itemId': '919760', 'itemTitle': '观致汽车观致5PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '23', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 27, 'isDel': 0, 'itemId': '919630', 'itemTitle': 'MINIMINI CLUBMANPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '22', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 26, 'isDel': 0, 'itemId': '919637', 'itemTitle': '长安马自达马自达CX-5PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '21', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 25, 'isDel': 0, 'itemId': '919621', 'itemTitle': '林肯(进口)领航员PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '20', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 24, 'isDel': 0, 'itemId': '919743', 'itemTitle': '野马汽车斯派卡PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '19', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 23, 'isDel': 0, 'itemId': '919759', 'itemTitle': '迈凯伦迈凯伦720SPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '18', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 22, 'isDel': 0, 'itemId': '919742', 'itemTitle': '野马汽车博骏PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '17', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 21, 'isDel': 0, 'itemId': '919754', 'itemTitle': '东风日产启辰T60PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '16', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 20, 'isDel': 0, 'itemId': '919636', 'itemTitle': '一汽马自达马自达CX-4PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '15', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 19, 'isDel': 0, 'itemId': '919638', 'itemTitle': '长安马自达马自达CX-8PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '14', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 18, 'isDel': 0, 'itemId': '919624', 'itemTitle': '雷克萨斯雷克萨斯LSPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '13', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 17, 'isDel': 0, 'itemId': '919620', 'itemTitle': '林肯(进口)林肯大陆PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '12', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 16, 'isDel': 0, 'itemId': '919755', 'itemTitle': '东风日产启辰T70PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '11', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 15, 'isDel': 0, 'itemId': '919757', 'itemTitle': '迈凯伦迈凯伦540CPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '10', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 14, 'isDel': 0, 'itemId': '919781', 'itemTitle': '长城汽车哈弗H2PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '9', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 13, 'isDel': 0, 'itemId': '919782', 'itemTitle': '长城汽车哈弗H4PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '8', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 12, 'isDel': 0, 'itemId': '919660', 'itemTitle': '斯巴鲁斯巴鲁XVPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '7', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 11, 'isDel': 0, 'itemId': '919628', 'itemTitle': '劳斯莱斯幻影PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '6', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 10, 'isDel': 0, 'itemId': '919629', 'itemTitle': 'MINIMINIPK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '5', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 9, 'isDel': 0, 'itemId': '919745', 'itemTitle': '东风乘用车奕炫PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '4', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 8, 'isDel': 0, 'itemId': '919758', 'itemTitle': '迈凯伦迈凯伦570PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '3', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 7, 'isDel': 0, 'itemId': '919753', 'itemTitle': '东风日产启辰D60PK之战还未终止，置换补贴PK又来了', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '2', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 6, 'isDel': 0, 'itemId': '919377', 'itemTitle': '奥迪A6L不保值了？               ', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '1', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616393550000, 'id': 5, 'isDel': 0, 'itemId': '919378', 'itemTitle': '个人车源究竟多便宜              ', 'modifiedStime': 1616393550000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '测试', 'contentId': 31, 'contentName': '', 'createUser': 'liujingli', 'createdStime': 1616392413000, 'id': 4, 'isDel': 0, 'itemId': '919379', 'itemTitle': '展览车也有好品质                ', 'modifiedStime': 1616392413000, 'productId': '104', 'productName': '二手车', 'productTypeId': '1015', 'productTypeName': '二手车', 'updateUser': 'liujingli'}, {'anchorOperateText': '测试修改', 'contentId': 600042, 'contentName': '智能营销AGC', 'createUser': 'wangpengfeiwb3347', 'createdStime': 1615795405000, 'id': 2, 'isDel': 0, 'itemId': '18839518', 'modifiedStime': 1615802088000, 'productId': '108', 'productName': '商业运营中心', 'productTypeId': '1019', 'productTypeName': '智能营销', 'updateUser': 'wangpengfeiwb3347'}]\n";
//        JSONArray.parseArray(testText)
//                .forEach(item -> JSONObject.parseObject(String.valueOf(item)).getInteger("contentId"));
        JSONArray jsonArray = JSONArray.parseArray(testText);
        for (Object o : jsonArray) {
            System.out.println(String.valueOf(o));
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(o));
            System.out.println(jsonObject.getString("contentId"));
        }
    }

    @Test
    public void jsonToLinkedHashMap() {
//        String jsonText = "[{\"isTestLhy0101\":50,\"isNoTestLhy0101\":50,\"hashKey\":[\"deviceid\"]}]";
        String jsonText = "[{\"isNoOPUOptimize7\":0,\"isNoOPUTypeOptimize4\":50,\"isOPUOptimize7\":0,\"isOPUTypeOptimize4\":50,\"isOPUTypeOptimize5\":0,\"isOPUTypeOptimize6\":0,\"hashKey\":[\"deviceid\"]}]";
        jsonText = jsonText.substring(1, jsonText.length() - 1);
        LinkedHashMap<String, Object> json = JSONObject.parseObject(jsonText, LinkedHashMap.class, Feature.OrderedField);
        List<String> collect = json.keySet()
                .stream()
                .skip(json.size() - 2)
                .collect(Collectors.toList());
        collect.forEach(str -> json.remove(str));
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            System.out.println("key:" + entry.getKey() + "   value:" + entry.getValue());
        }

        System.out.println("88888888888888888888");

        JSONObject reqJSON = JSONObject.parseObject(jsonText, Feature.OrderedField);
        for (Map.Entry<String, Object> entry : reqJSON.entrySet()) {
            System.out.println("key:" + entry.getKey() + "   value:" + entry.getValue());
        }


    }


}
