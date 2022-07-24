package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private boolean isRatio;
    private boolean isSchema;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));

        if (isRatio) {
            double ratio = img.getWidth() / img.getHeight();
            if (ratio > maxRatio) {
                throw new BadImageSizeException(ratio, maxRatio);
            }
        }

        if (!isSchema) this.schema = new Schema();

        BufferedImage bwImg;
        ImageIO.write(img, "png", new File("out1.png"));
        if (maxWidth != 0 || maxHeight != 0) {
            if (maxWidth > img.getWidth() && maxHeight > img.getHeight()) {
                bwImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D graphics = bwImg.createGraphics();
                graphics.drawImage(img, 0, 0, null);
            } else {
                int newWidth = img.getWidth() >= img.getHeight() ? maxWidth : (int) (img.getWidth() / (double) (img.getHeight() / maxHeight));
                int newHeight = img.getHeight() >= img.getWidth() ? maxHeight : (int) (img.getHeight() / (double) (img.getWidth() / maxWidth));

                Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
                bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D graphics = bwImg.createGraphics();
                graphics.drawImage(scaledImage, 0, 0, null);
            }
        } else {
            bwImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics = bwImg.createGraphics();
            graphics.drawImage(img, 0, 0, null);
        }

        ImageIO.write(bwImg, "png", new File("out.png"));

        WritableRaster bwRaster = bwImg.getRaster();

        int arrColors[] = new int[3];

        StringBuilder schemaImgBuilder = new StringBuilder();

        for (int i = 0; i < bwImg.getHeight(); i++) {
            for (int j = 0; j < bwImg.getWidth(); j++) {
                int color = bwRaster.getPixel(j, i, arrColors)[0];
                char c = schema.convert(color);
                schemaImgBuilder.append(c);
                schemaImgBuilder.append(c);
            }
            schemaImgBuilder.append("\n");
        }

        return schemaImgBuilder.toString();

    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    public void setMaxHeight(int height) {
        maxHeight = height;
    }


    public void setMaxRatio(double maxRatio) {
        isRatio = true;
        this.maxRatio = maxRatio;
    }


    public void setTextColorSchema(TextColorSchema schema) {
        isSchema = true;
        this.schema = schema;
    }


}
