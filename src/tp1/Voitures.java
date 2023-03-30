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
import java.util.stream.Collectors;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Justin Quirion
 * Permet de de stocker toutes les voitures dans une classe
 */
public class Voitures {
    
    private HashMap<String, HashMap<String, Integer>> mapMakeModel;
    private List<Path> paths;
    private ArrayList<String> toutesLesMarques = new ArrayList<>(Arrays.asList("Veuillez choisir..."));
    private HashMap<String, Integer> choix;
    private String makeString;
    
    /**
     * Retourne toutes les marques possibles des voitures
     * @return les marques
     */
    public ArrayList<String> getToutesLesMarques() {
        return toutesLesMarques;
    }

    /**
     * Retourne un HashMap de HashMap, avec les marques en clé qui vont donner un autre HashMap, qui lui prendra comme clé le modèle, et qui a comme valeur le prix du véhicule
     * @return Marque, modèle et prix des véhicules
     */
    public HashMap<String, HashMap<String, Integer>> getMapMakeModel() {
        return mapMakeModel;
    }
    
    /**
     * Retourne le prix d'un véhicule, avec la marque et le modèle du dit véhicule
     * @param make marque du véhicule
     * @param model modèle du véhicule
     * @return prix du véhicule
     */
    public Integer getPrice(String make, String model){
        return mapMakeModel.get(make).get(model);
    }
    
    /**
     * Initialisateur pour la classe voitures, permet de lire tous les fichiers dans le directoire spécifié plus bas
     */
    public Voitures() {
        
        try {            
            String endsWith = "modeles.txt";
            paths = Files.walk(Paths.get("C:/Temp/Voitures")) // CHANGE HERE TO WHERE YOUR CARS ARE
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
    
    /**
     * Permet la lecture du fichier à l'emplacement filepath. Le fichier doit être formaté de la facon: String model int price. Si ce 
     * n'est pas comme cela, mon code ne marchera pas. Va stocker les modèles et les prix selon la référence de la marque
     * @param filepath emplacement/nom du fichier
     */
    private void readFile(Path filepath){
        File file = filepath.toFile();
        Scanner myScanner;
        String model;
        int price;
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
