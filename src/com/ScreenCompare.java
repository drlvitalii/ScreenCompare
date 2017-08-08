package com;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ScreenCompare {
    private static String path1 = "F:\\image1.png";
    private static String path2 = "F:\\image2.png";
    private static String path3 = "F:\\image3.png";

    public static void main(String[] args) throws IOException {

        ScreenCompare screenCompare = new ScreenCompare(path1, path2);
        screenCompare.compare();
        if (ScreenCompare.isIsDifferent()) {
            System.out.println("no match");
            ScreenCompare.saveImage(screenCompare.getImageResult(), path3);
        } else {
            System.out.println("match");
        }
    }

    private static BufferedImage image1;
    private static BufferedImage image2;
    private static BufferedImage resultImage;
    private static boolean isDifferent;
    // a number of pieces along vertical and horizontal
    private static final int BLOCKS_AMOUNT_X = 40;
    private static int BLOCK_AMOUNT_Y = 40;

    // “tolerance” comparison parameter that allows to treat similar colors as the same
    private static double differentKoef = 0.10;

    public ScreenCompare(String image1Path, String image2Path) throws IOException {
        image1 = downloadImage(image1Path);
        image2 = downloadImage(image2Path);
    }

    public void compare() {
        resultImage = new BufferedImage(image2.getWidth(null), image2.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resultImage.createGraphics();
        g2.drawImage(image2, null, null);
        g2.setColor(Color.RED);
        int blockSizeX = (image1.getWidth() / BLOCKS_AMOUNT_X);
        int blockSizeY = (image1.getHeight() / BLOCK_AMOUNT_Y);
        isDifferent = false;
        for (int y = 0; y < BLOCK_AMOUNT_Y; y++) {
            for (int x = 0; x < BLOCKS_AMOUNT_X; x++) {
                int result1[][] = convertImageToArrayRGB(image1.getSubimage(x * blockSizeX, y * blockSizeY, blockSizeX - 1, blockSizeY - 1));
                int result2[][] = convertImageToArrayRGB(image2.getSubimage(x * blockSizeX, y * blockSizeY, blockSizeX - 1, blockSizeY - 1));
                for (int i = 0; i < result1.length; i++) {
                    for (int j = 0; j < result1[0].length; j++) {
                        int diff = Math.abs(result1[i][j] - result2[i][j]);
                        if (diff / Math.abs(result1[i][j]) > differentKoef) {
                            g2.drawRect(x * blockSizeX, y * blockSizeY, blockSizeX - 1, blockSizeY - 1);
                            isDifferent = true;
                        }
                    }
                }
            }
        }
    }

    public BufferedImage getImageResult() {
        return resultImage;
    }

    public int[][] convertImageToArrayRGB(BufferedImage subimage) {
        int width = subimage.getWidth();
        int height = subimage.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = subimage.getRGB(col, row);
            }
        }
        return result;
    }

    public static BufferedImage downloadImage(String filename) throws IOException {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;

    }

    public static void saveImage(BufferedImage bimg, String filename) {
        try {
            File outputfile = new File(filename);
            ImageIO.write(bimg, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isIsDifferent() {
        return isDifferent;
    }
}
