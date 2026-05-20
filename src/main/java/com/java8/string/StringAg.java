package com.java8.string;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class StringAg {
    public static void main(String[] args) {
        String a = "a1";
        String b = "a1";
        String s1 = new String("sunguangxu");
        String s2 = new String("sunguangxu");
        System.out.println(s1 == s2);
        System.out.println(a == b);

        int i = 100 / 10;
        System.out.println(i);

        String str = "123.12312.4567.90mm";
        char[] chars = str.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            char c = chars[j];
            if (c != 46 && (c < 48 || c > 57)) {
                System.out.println(false);
            }
            System.out.println(c);
        }

        int lg4A = 0;
        Integer showPvInt = Integer.valueOf("200000");
        if (showPvInt > 100000) {
            lg4A++;
        }
        System.out.println(lg4A);

        BigDecimal multiply = new BigDecimal("1000")
                .divide(new BigDecimal(String.valueOf(4)))
                .multiply(new BigDecimal("0.8"));
        System.out.println(multiply);
        lg4A = -1;
        System.out.println(Objects.equals(-1, lg4A));

        Double va = Double.valueOf("375") / Double.valueOf("87") * 100;
        System.out.println(va);

        System.out.println(String.format("%s/%s", 100, 100));
        System.out.println("aaa".replace("-", "_"));
        System.out.println("aaa-bb".replace("-", "_"));
        List<String> ids = Pattern.compile(",").splitAsStream("12483").collect(toList());
        System.out.println(ids.size());

        System.out.println("--------------------------");
        String NATURAL_NUMBER_PATTERN = "[1-9][0-9]*";
        String RANGE_PATTERN = NATURAL_NUMBER_PATTERN + "-" + NATURAL_NUMBER_PATTERN;
        String STRING_PATTERN = "^(" + RANGE_PATTERN + ")(,(" + RANGE_PATTERN + "))*$";
        Pattern pattern = Pattern.compile(STRING_PATTERN);
        Matcher matcher = pattern.matcher("1-12345");
        System.out.println(matcher.matches());
        //if (!matcher.matches()) {
        //    return fail(501, "内容格式错误,正确格式为:1-12345,2-56789");
        //}

        Long pageNum = 20L;
        Long pageSize = 0L;
        if (pageNum < 1) {
            pageNum = 1L;
        }
        if (pageSize < 10) {
            pageSize = 10L;
        }
        // 原始物料数据列表
        List<String> materialList = Arrays.asList(
                "1-1304493", "1-1286220", "1-938435", "1-98461", "1-98465", "1-90486", "1-90487", "1-98471", "1-98490", "1-98482",
                "1-1307555", "1-1307564", "1-1307472", "1-1307701", "1-1143570", "1-1307702", "1-1307988", "1-1308021", "1-1310271", "1-1243541",
                "1-1310355", "1-1310412", "1-1310427", "1-1310440", "1-1310518", "1-1310557", "1-1309908", "1-1310613", "1-1310593", "3-2255327",
                "1-1310768", "1-1310872", "5-113158575", "5-113316929", "5-113379225", "5-113337435", "5-113404629", "5-113404467", "5-113505346", "5-113412557",
                "5-113448314", "5-113546987", "5-113572652", "5-113714731", "5-113710489", "5-113709969", "14-24462861", "1-1310971", "14-24467919", "14-24469143",
                "3-2255590", "1-1311014", "1-1311023", "1-1311026", "3-2255580", "1-1311013", "3-2255574", "1-1311006", "3-2255565", "3-2255563",
                "3-2255587", "3-2255586", "14-24476492", "14-24475957", "14-24482308", "1-1311060", "1-20283", "14-24479714", "14-24482992", "1-890419",
                "14-24480212", "1-1311091", "14-24474132", "14-24478160", "14-24479813", "14-24482862", "14-24481663", "14-24481309", "14-24480038", "14-24482765",
                "1-1311101", "3-2255614", "3-2255638", "3-2255570", "3-2255588", "3-2255637", "1-1311103", "3-2255639", "1-1311051", "3-2255585",
                "14-24485801", "14-24484910", "14-24483678", "14-24483624", "14-24487700", "14-24487051", "14-24486565", "1-1311122", "1-1311127", "3-2255654",
                "3-2255647", "14-24486557", "14-24490377", "1-1311128", "5-113709254", "14-24488094", "14-24487722", "14-24492376", "14-24495107", "14-24494630",
                "14-24490131", "14-24491356", "14-24490958", "3-2255661", "14-24496124", "1-1311071"
        );

        long totalPage = materialList.size() / pageSize + 1;
        System.out.printf("总页数：%d, pageNum: %d", totalPage, pageNum);
        pageNum = pageNum > totalPage ? totalPage : pageNum;
        System.out.printf("总页数：%d, pageNum: %d", totalPage, pageNum);
        List<String> list = materialList.stream()
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(toList());

        System.out.println(list);



        // 待解析的JSON字符串
        String jsonStr = "{\n" +
                "\"1\":[{\"ctr\":0.0,\"biz_type\":14,\"contentid\":7070,\"operationid\":\"5|2|A|1|948|0|00034\",\"position\":1,\"biz_id\":\"19852274\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":1,\"contentid\":6702,\"operationid\":\"5|2|A|1|948|0|00034\",\"position\":1,\"biz_id\":\"1285019\",\"market_plan_id\":7923},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6689,\"operationid\":\"5|2|A|1|948|0|00034\",\"position\":1,\"biz_id\":\"12576080\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":3,\"contentid\":6678,\"operationid\":\"5|2|A|1|948|0|00034\",\"position\":1,\"biz_id\":\"2237031\",\"market_plan_id\":7923},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6672,\"operationid\":\"5|2|A|1|948|0|00034\",\"position\":1,\"biz_id\":\"12537024\",\"market_plan_id\":7924}],\n" +
                "\"2\":[{\"ctr\":0.0,\"biz_type\":14,\"contentid\":7070,\"operationid\":\"5|2|A|1|948|0|00035\",\"position\":2,\"biz_id\":\"19852274\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6689,\"operationid\":\"5|2|A|1|948|0|00035\",\"position\":2,\"biz_id\":\"12576080\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":3,\"contentid\":6686,\"operationid\":\"5|2|A|1|948|0|00035\",\"position\":2,\"biz_id\":\"2237031\",\"market_plan_id\":7921},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6672,\"operationid\":\"5|2|A|1|948|0|00035\",\"position\":2,\"biz_id\":\"12537024\",\"market_plan_id\":7924}],\n" +
                "\"3\":[{\"ctr\":0.0,\"biz_type\":14,\"contentid\":7070,\"operationid\":\"5|2|A|1|948|0|00036\",\"position\":3,\"biz_id\":\"19852274\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6689,\"operationid\":\"5|2|A|1|948|0|00036\",\"position\":3,\"biz_id\":\"12576080\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6672,\"operationid\":\"5|2|A|1|948|0|00036\",\"position\":3,\"biz_id\":\"12537024\",\"market_plan_id\":7924}],\n" +
                "\"4\":[{\"ctr\":0.0,\"biz_type\":14,\"contentid\":7070,\"operationid\":\"5|2|A|1|948|0|00037\",\"position\":4,\"biz_id\":\"19852274\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6689,\"operationid\":\"5|2|A|1|948|0|00037\",\"position\":4,\"biz_id\":\"12576080\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6670,\"operationid\":\"5|2|A|1|948|0|00037\",\"position\":4,\"biz_id\":\"12537024\",\"market_plan_id\":7919}],\n" +
                "\"5\":[{\"ctr\":0.0,\"biz_type\":14,\"contentid\":7070,\"operationid\":\"5|2|A|1|948|0|00038\",\"position\":5,\"biz_id\":\"19852274\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6689,\"operationid\":\"5|2|A|1|948|0|00038\",\"position\":5,\"biz_id\":\"12576080\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6672,\"operationid\":\"5|2|A|1|948|0|00038\",\"position\":5,\"biz_id\":\"12537024\",\"market_plan_id\":7924}],\n" +
                "\"6\":[{\"ctr\":0.0,\"biz_type\":14,\"contentid\":7070,\"operationid\":\"5|2|A|1|948|0|00039\",\"position\":6,\"biz_id\":\"19852274\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6689,\"operationid\":\"5|2|A|1|948|0|00039\",\"position\":6,\"biz_id\":\"12576080\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6672,\"operationid\":\"5|2|A|1|948|0|00039\",\"position\":6,\"biz_id\":\"12537024\",\"market_plan_id\":7924}],\n" +
                "\"7\":[{\"ctr\":0.0,\"biz_type\":14,\"contentid\":7070,\"operationid\":\"5|2|A|1|948|0|00040\",\"position\":7,\"biz_id\":\"19852274\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6689,\"operationid\":\"5|2|A|1|948|0|00040\",\"position\":7,\"biz_id\":\"12576080\",\"market_plan_id\":7924},{\"ctr\":0.0,\"biz_type\":14,\"contentid\":6672,\"operationid\":\"5|2|A|1|948|0|00040\",\"position\":7,\"biz_id\":\"12537024\",\"market_plan_id\":7924}]\n" +
                "}";

        JSONArray output = new JSONArray();
        // 第一步：将JSON字符串解析为JSONObject对象
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        // 第二步：遍历JSONObject中的所有键（1,2,3,4,5,6,7）
        for (String key : jsonObject.keySet()) {
            System.out.println("===== 处理键：" + key + " =====");
            // 第三步：将对应键的数组解析为List<BizContent>
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            System.out.println("===== 数量：" + jsonArray.size() + " =====");
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject object = jsonArray.getJSONObject(j);
                object.put("itemId", String.join("-", object.getString("biz_type"), object.getString("biz_id")));
                object.put("focusPicUrl", "");
            }
            output.addAll(jsonArray);
        }

        System.out.println(output.size());
        System.out.println(JSON.toJSONString(output, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.PrettyFormat));
    }
}
