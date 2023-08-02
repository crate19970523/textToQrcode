package com.crater;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodePanel extends JPanel {
    private BufferedImage image;

    public QRCodePanel(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) throws WriterException, IOException {
        String text = args[0];
        if (checkIsFile(text)) {
            text = readFile(args[1]);
        }
        int width = 500;
        int height = 500;
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        JFrame frame = new JFrame();
        frame.getContentPane().add(new QRCodePanel(image));
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private static boolean checkIsFile(String args0) {
        return "-f".equals(args0);
    }

    private static String readFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
