package com.java8.string;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Aa {
    //问题图片特征
    private static final String HEX_STRING = "46466D70656709536572766963653031777C43CAFFFFFFFFFFFFFFFFFFFFFF";
    private static final byte[] HEX_BYTES = hexStringToByteArray(HEX_STRING);

    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<String>();


        //测试用例：
        //正常的图
        list.add("http://www3.autoimg.cn/newsdfs/g26/M03/E1/95/autohomecar__ChxkjmQP4P-AWX7HAASdD58Ng_Q497.png");
        list.add("http://www3.autoimg.cn/newsdfs/g25/M01/A1/01/ChtliGOwbEOAZHrsAAAOtfzL5hw760.jpg");
        list.add("http://www3.autoimg.cn/bdpdfs/private/g25/M01/A1/01/ChtliGOwbEOAZHrsAAAOtfzL5hw760.jpg");



        //有问题的图
        list.add("http://www2.autoimg.cn/chejiahaodfs/http://www2.autoimg.cn/chejiahaodfs/g30/M0B/FD/2C/ChxknGQVjmKASaOzAFQwoCG_dJ073.jpeg");
        list.add("http://www2.autoimg.cn/chejiahaodfs/http://www2.autoimg.cn/chejiahaodfs/g30/M0B/FD/2A/ChxknGQVjlqAT77CAFE3FNLIVEQ18.jpeg");
        list.add("http://www2.autoimg.cn/chejiahaodfs/http://www2.autoimg.cn/chejiahaodfs/g30/M04/FD/2B/ChxknGQVjl6AA41kAEtMEJNKsag35.jpeg");
        //list.add("http://www2.autoimg.cn/chejiahaodfs/g25/M07/6F/B3/ChxkqWQHdOaAUsQzABpWxBOwWsA926.png");
        //list.add("http://www2.autoimg.cn/chejiahaodfs/g28/M02/4F/C7/autohomecar__CjIFVGQdHjKAOHygAAV4oczMR0w91.jpeg");
        //404的图片
        list.add("http://www2.autoimg.cn/chejiahaodfs/g27/M04/C6/48/autohomecar__ChxkmWQc9euAQW_NAAOIMZc_cAw07.jpeg");

        list.forEach((imageUrl) -> {
            int result = FindPic(imageUrl);
            if (result == 1) {
                System.out.println("找到 +++ " + imageUrl);
            } else if (result == 0) {
                System.out.println("正常 --- " + imageUrl);
            } else {
                System.out.println("出错 !!! " + imageUrl);
            }
        });
    }


    //1找到，0未找到，-1出错
    public static int FindPic(String imageUrl) {
        byte[] imageData = null;
        try {
            imageData = downloadImageData(imageUrl);
        } catch (IOException ex) {
            System.out.println(ex); //todo: 出错要记日志
            return -1;
        }
        return containsHexBytes(imageData, HEX_BYTES) ? 1 : 0;
    }


    private static byte[] downloadImageData(String urlStr) throws IOException {
        //urlStr="http://www2.autoimg.cn/chejiahaodfs/g24/M02/D0/E2/Chtk3WP-6uaAPmXDACR3HJexK7g233.png";
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();
        try (InputStream inputStream = connection.getInputStream()) {
            //BufferedImage image = ImageIO.read(inputStream);
            //int srcWidth = image.getWidth();      // 源图宽度
            //int srcHeight = image.getHeight();    // 源图高度
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int count = 0;
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                count += length;
                if (count >= 4096)
                    break;//只读前4k就够用了
            }
            inputStream.close();
            return outputStream.toByteArray();
        }
    }

    private static boolean containsHexBytes(byte[] data, byte[] hexBytes) {
        for (int i = 0; i < data.length - hexBytes.length + 1; i++) {
            boolean found = true;
            for (int j = 0; j < hexBytes.length; j++) {
                if (data[i + j] != hexBytes[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return true;
            }
        }
        return false;
    }

    private static byte[] hexStringToByteArray(String hexStr) {
        int len = hexStr.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) ((Character.digit(hexStr.charAt(i), 16) << 4) + Character.digit(hexStr.charAt(i + 1), 16));
        }
        return result;
    }
}
