package com.java8.image;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Demo {

    public static void main(String[] args) throws Exception {
        String imageUrl = "https://img3.autoimg.cn/soudfs/g31/M0A/76/E9/autohomecar__ChxoHmYKVkCAXUgeAAFyJt5lmE8639.jpg";
        //String[] split = imageUrl.split("\\.");
        //BufferedImage image = ImageIO.read(new URL(imageUrl));
        //
        //// 获取图片大小
        //int width = image.getWidth();
        //int height = image.getHeight();
        //System.out.println("图片大小: " + width + " x " + height);
        //
        //// 获取像素数量
        //int pixels = width * height;
        //System.out.println("像素数量: " + pixels);
        //
        //// 获取图片格式
        //String format = split[split.length - 1];
        //System.out.println("图片格式: " + format);

        info2(imageUrl);
    }

    private static void info2(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream in = url.openStream();


            // 读取图片到BufferedImage对象
            BufferedImage image = ImageIO.read(in);

            // 获取图片格式
            String[] formatNames = ImageIO.getWriterFormatNames();
            String format = formatNames[0]; // 如果有多个格式，可能需要更精确的方法来确定

            // 获取图片宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();

            // 获取图片文件大小（这需要重新打开连接来获取字节流大小）
            URLConnection conn = url.openConnection();
            conn.connect();
            long fileSize = conn.getContentLengthLong();

            // 关闭流
            in.close();

            ImageInfo info = new ImageInfo(imageUrl, format, width, height, fileSize);
            System.out.println(JSON.toJSONString(info, SerializerFeature.PrettyFormat));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class ImageInfo {
    String url;
    String format;
    int width;
    int height;
    long fileSize;

    public ImageInfo(String url, String format, int width, int height, long fileSize) {
        this.url = url;
        this.format = format;
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}