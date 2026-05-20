package com.java8.string;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StringViewDemo {

    public static void main(String[] args) throws UnsupportedEncodingException {

        String param = URLDecoder.decode("%E5%8D%A1%E7%BD%97%E6%8B%89", "utf-8");
        System.out.println(param);
        String encode = URLEncoder.encode(param, "utf-8");
        System.out.println(encode);

//        insert into dim_upload_app_version_list_outmedia (app_version, version_index) values ('11.32.0', 113200), ('11.21.0', 112100)

        StringBuilder output = new StringBuilder(1000);
        output.append("insert into dim_upload_app_version_list_outmedia (app_version, version_index) values ");
        List<String> list = new ArrayList<>(100);
        for (int i = 1; i < 100; i++) {
            StringBuilder sb = new StringBuilder(10);
            sb.append("11.32.");
            if (i < 10) {
                sb.append("0");
            }
            sb.append(i);
            String version = sb.toString();
            list.add(String.format("('%s', %s)", version, version.replace(".", "")));
        }
        String join = String.join(",", list);
        output.append(join);
        System.out.println(output);

        System.out.println("---------------");
        System.out.println(Integer.SIZE);
        System.out.println(Integer.BYTES);

        System.out.println(Objects.equals("628134176410828800", null));
    }
}
