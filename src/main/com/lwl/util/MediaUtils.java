package main.com.lwl.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lwl
 * @date 2018/8/14 15:29
 * @description 媒体文件操作相关工具类
 */
public class MediaUtils {
    private MediaUtils(){}
    /**
     * 将前端传的图片压缩到为指定大小，返回压缩后的byte流和图片大小
     */
    public static Map<String, Object> compressImage(InputStream inputStream, int width, int height,
                                                    String fileName){
        try {
            //读取图片并根据参数压缩
            Image src = ImageIO.read(inputStream);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(src, 0, 0, width, height, null);

            //获取图片后缀
            String originFileName = fileName;
            String[] originFileNameArray = originFileName.split("\\.");
            String suffix = originFileNameArray[1];

            //转为流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, suffix, os);

            Map<String, Object> map = new HashMap<>();
            byte[] bytes = os.toByteArray();
            map.put("stream", new ByteArrayInputStream(bytes));
            map.put("capacity", (long) bytes.length);

            return map;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}