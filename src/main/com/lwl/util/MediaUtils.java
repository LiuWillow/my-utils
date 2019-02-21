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
     * 将图片压缩到为指定大小，返回压缩后的byte流和图片大小
     */
    public static Map<String, Object> compressImage(MultipartFile multipartFile, int width, int height) {
        try {
            //读取图片并根据参数压缩
            Image src = ImageIO.read(multipartFile.getInputStream());
            BufferedImage image;
            if (width == 0 || height == 0) {
                image = (BufferedImage) src;
            } else {
                int oldHeight = ((BufferedImage) src).getHeight();
                int oldWidth = ((BufferedImage) src).getWidth();
                if (oldHeight > oldWidth) {
                    // 如果height比较大，就以高为准

                    float rate = height / (oldHeight * 1F);
                    width = (int) (rate * oldWidth);
                    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    image.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                            0, 0,null);

                } else {
                    // 如果width比较大，就以款宽为准
                    float rate = width / (oldWidth * 1F);
                    height = (int) (rate * oldHeight);
                    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    image.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                            0, 0,null);
                }
            }

            //获取图片后缀
            String originFileName = multipartFile.getOriginalFilename();
            String suffix = UtilTool.getMediaTypeByFileName(originFileName);

            //转为流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, suffix, os);

            Map<String, Object> map = new HashMap<>();
            byte[] bytes = os.toByteArray();
            map.put(Constants.KEY_INPUT_STREAM, new ByteArrayInputStream(bytes));
            map.put(Constants.KEY_CAPACITY_OF_INPUT_STREAM, (long) bytes.length);
            map.put(Constants.KEY_HEIGHT, image.getHeight());
            map.put(Constants.KEY_WIDTH, image.getWidth());

            return map;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}