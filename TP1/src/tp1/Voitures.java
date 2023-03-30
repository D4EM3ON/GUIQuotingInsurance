package tp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Justin Quirion
 */
public class Voitures {
    
    private HashMap<String, HashMap<String, Integer>> mapMakeModel;
    private List<Path> paths;
    private ArrayList<String> toutesLesMarques = new ArrayList<>(Arrays.asList("Veuillez choisir..."));
    private HashMap<String, Integer> choix;
    private String makeString;
    
    public ArrayList<String> getToutesLesMarques() {
        return toutesLesMarques;
    }

    public HashMap<String, HashMap<String, Integer>> getMapMakeModel() {
        return mapMakeModel;
    }
    
    public Integer getPrice(String make, String model){
        return mapMakeModel.get(make).get(model);
    }
    
    public Voitures() {
        
        try {            
            String endsWith = "modeles.txt";
            paths = Files.walk(Paths.get("C:/Temp/Voitures"))
                    .filter(line -> line.toString().endsWith(endsWith))
                    .collect((Collectors.toList()));
        } catch (IOException ex) {
            System.out.println("ERROR");
        }
        mapMakeModel = new HashMap<String, HashMap<String, Integer>>();
        for (Path filepath : paths) {
            this.makeString = filepath.toString().replace("C:\\Temp\\Voitures\\", "").replace("_modeles.txt","");
                        
            if (!this.makeString.equals("gmc")) this.makeString = this.makeString.substring(0,1).toUpperCase() + this.makeString.substring(1);
            else this.makeString = this.makeString.toUpperCase();
                        
            choix = new HashMap<>();
            
            mapMakeModel.put(this.makeString, this.choix);
            toutesLesMarques.add(this.makeString);
            readFile(filepath);
        } 
    }
    
    private void readFile(Path filepath){
        File file = filepath.toFile();
        Scanner myScanner;
        String make = filepath.toString().split("_")[0].split(Pattern.quote(File.separator))[3];
        String model = "";
        int price = 0;
        try {
            myScanner = new Scanner(file);
            while (myScanner.hasNext()) {
           
                model = myScanner.next();
                price = (int) myScanner.nextFloat();
                        
            this.choix.put(model, price);
            
            mapMakeModel.put(this.makeString, this.choix);
        }
        } catch (FileNotFoundException ex) {
            System.err.print("Il y a eu une erreur lors de la lecture du fichier!");
        }
        
        
    }
}
