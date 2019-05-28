package calibrationpattern.rings;

import core.PNG;
import java.awt.Color;

public class ModelApp {
    
    public static void main(String[] args) {
        
        // Render the calibration pattern model
        int nRows = 10;
        int nCols = 10;
        int borderWidth = 40;
        int shapeRadius = 200;
        Model model = new Model(nRows, nCols, borderWidth, shapeRadius);
        model.drawFill(Color.WHITE);
        model.drawBorder(Color.BLACK);
        int ringOuterRadius = 120;
        int ringInnerRadius = 80;
        model.drawRings(Color.BLACK, ringOuterRadius, ringInnerRadius);
        
        // Save the model
        String save_path = ".\\Test_Resources\\Debug\\calibrationPatternRings.png";
        PNG png = new PNG();
        int dpi = 300;
        png.setDPI(dpi);
        png.save(model.img, save_path);       
    }        
}
