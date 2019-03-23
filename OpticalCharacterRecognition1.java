package backUpPOCs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class OpticalCharacterRecognition1 {

    public static void main(String[] args) {
        try {
            //read the input image
            BufferedImage inputImg = ImageIO.read(new File(".\\GoogleApps1.png"));

            //create black and white image to improve the accuracy of OCR by tesseract
            BufferedImage blackAndWhiteImg = new BufferedImage(inputImg.getWidth(), inputImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

            //graphics, drawimage
            Graphics2D grBnW = blackAndWhiteImg.createGraphics();
            grBnW.drawImage(inputImg, 0, 0, null);
            //write bw image for ref
            ImageIO.write(blackAndWhiteImg, "png", new File(".\\GoogleApps1BNW.png"));

            //split in to cols and rows to improve the accuracy of OCR by tesseract
            int cols = 3;
            int rows = 4;
            int chunkWidth = inputImg.getWidth() / cols;
            int chunkHeight = inputImg.getHeight() / rows;
            BufferedImage[] img = new BufferedImage[cols * rows];
            int count = 0;
            for (int c = 0; c < cols; c++) {
                for (int r = 0; r < rows; r++) {
                    img[count] = new BufferedImage(chunkWidth, chunkHeight, inputImg.getType());
                    Graphics2D gr = img[count].createGraphics();
                    gr.drawImage(blackAndWhiteImg, 0, 0, chunkWidth, chunkHeight, chunkWidth * c, chunkHeight * r, chunkWidth * (c + 1), chunkHeight * (r + 1), null);
                    count++;
                }
            }

            //write subimages and use Tesseract to read the text
            String filename=".\\img";
            for (int i = 0; i < img.length; i++) {
                ImageIO.write(img[i], "png", new File(filename + i + ".png"));
                Runtime.getRuntime().exec(".\\tesseract.exe " +   filename+i + ".png " + filename + i );           

            }
        } catch (IOException ex) {
            Logger.getLogger(OpticalCharacterRecognition1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

