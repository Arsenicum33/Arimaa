package cz.cvut.fel.pjv.utils;

import cz.cvut.fel.pjv.Models.PieceType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * class used to process images of pieces
 */
public class ImageHandler implements IImageHandler
{
    private static final Dictionary<PieceType, Image> data = new Hashtable<>();
    private void fetchImages()
    {
        File imagesDir = new File(Constants.imagesDirPath);
        File[] files = imagesDir.listFiles();
        for (File f:files)
        {
            try
            {
                var fileName = f.getName().split("\\.")[0];
                Image img = ImageIO.read(f).getScaledInstance(Constants.SQUARE_SIZE,Constants.SQUARE_SIZE,Image.SCALE_DEFAULT);
                PieceType pt = switch (fileName) {
                    case "cs" -> PieceType.CAT_S;
                    case "cg" -> PieceType.CAT_G;
                    case "ds" -> PieceType.DOG_S;
                    case "dg" -> PieceType.DOG_G;
                    case "rg" -> PieceType.RABBIT_G;
                    case "rs" -> PieceType.RABBIT_S;
                    case "cmg" -> PieceType.CAMEL_G;
                    case "cms" -> PieceType.CAMEL_S;
                    case "eg" -> PieceType.ELEPHANT_G;
                    case "es" -> PieceType.ELEPHANT_S;
                    case "hg" -> PieceType.HORSE_G;
                    case "hs" -> PieceType.HORSE_S;
                    default -> throw new IllegalStateException("Unexpected value: " + fileName);
                };
                data.put(pt,img);

            }
            catch (IOException e)
            {
                throw new RuntimeException();
            }
        }
    }

    public Image getImage(PieceType pt)
    {
        if (data.isEmpty())
            fetchImages();
        return data.get(pt);
    }


}
