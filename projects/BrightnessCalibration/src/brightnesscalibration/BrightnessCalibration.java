package brightnesscalibration;

import core.ImageUtils;
import core.Print;
import static core.Print.println;
import core.TXT;
import core.XML;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrightnessCalibration {
    
     Map<String,Object> config;  
    
    public BrightnessCalibration(String configPath) {
        // Load the XML configuration file
        XML xml = new XML(configPath);        
        
        // Prepend the base directory to all relative sub directories
        xml.prependBaseDir();
        
        // Get the configuration data as a map
        config = xml.map;   
    }
    
    public List<Double> loadGivenValues() {
        // Enumerate the given gray values
        double minValue = (Double) config.get("minValue");
        double maxValue = (Double) config.get("maxValue");
        double stepValue = (Double) config.get("stepValue");
        List<Double> givenValues = new ArrayList<>();
        for (double value = minValue; value <= maxValue; value += stepValue) {
            givenValues.add(value);
        }
        return givenValues;
    }
    
    public void saveLookUpTable(List<List<Integer>> lookUpTable) {
        String brightnessCalibrationDataDir = (String) config.get("brightnessCalibrationDataDir");
        String brightnessCalibrationDataFilename = (String) config.get("brightnessCalibrationDataFilename");
        String formatString = (String) config.get("formatString");
        TXT.saveMatrix(lookUpTable, Integer.class, brightnessCalibrationDataDir, brightnessCalibrationDataFilename, formatString);
    }

    public static void main(String[] args) {
        println("Running the Brightness Calibration App:");

        // Validate arguments
        if (args.length == 0) {
            throw new IllegalArgumentException("Needs exactly one argument which is the path to the XML configuration file.");
        } 

        // Parse the arguments
        String configAbsPath = args[0];
        
        BrightnessCalibration app = new BrightnessCalibration(configAbsPath);
        
        // Load images
        String brightnessCalibrationImageDir = (String) app.config.get("brightnessCalibrationImageDir");         
        Map<String,BufferedImage> imgStack = ImageUtils.load_batch(brightnessCalibrationImageDir);  

        // Convert to Gray
        Map<String,BufferedImage> grayStack = ImageUtils.color2Gray_batch(imgStack);

        // Load the given values
        List<Double> givenValues = app.loadGivenValues();

        Print.println("Computing the look-up-table");
        List<List<Integer>> lookUpTable = BrightnessCalibrationLookUpTable.computeLookUpTable(grayStack, givenValues);

        Print.println("Saving the look-up-table");
        app.saveLookUpTable(lookUpTable);
        
                
    }
    
}
