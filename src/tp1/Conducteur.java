package tp1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Justin Quirion
 * permet de créer un conducteur, et à partir de ses informations calculer une soumission et mettre le tout dans un fichier texte.
 */
public class Conducteur {
    private String nom;
    private String prenom;
    private String sexe;
    private String noTelephone;
    private String dateNaissance;
    private ArrayList<String> adresse; // no civique, rue, app = "", ville, code postal
    private String courriel;
    private boolean specialConditions;
    private int antivols;
    private boolean assuranceHabitation;
    private HashMap<String, String> voiture; // make, model, année, prix, 
    private float primeTotale = 0;
    private String pathName;

    /**
     * Retourne le String du path pour aller au fichier ou est stocké les données du conducteur
     * @return String du fichier avec la soumission
     */
    public String getPathName() {
        return pathName;
    }
    
    /**
     * Permet de créer un Conducteur avec un prenom, un nom, un sexe, un numéro de téléphone et une date de naissance. Tous les autres paramètres auraient aussi pu marcher, mais 
     * ca devenait trop lourd
     * @param prenom String du prenom du Conducteur
     * @param nom String du nom du Conducteur
     * @param sexe String du sexe du Conducteur
     * @param noTelephone String du numéro de téléphone du Conducteur
     * @param dateNaissance String de la date de naissance du Conducteur
     */
    public Conducteur(String prenom, String nom, String sexe, String noTelephone, String dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.noTelephone = noTelephone;
        this.dateNaissance = dateNaissance;
        voiture = new HashMap<>();
    }
    
    /**
     * Permet d'ajouter une adresse physique au Conducteur
     * @param noCivique String du numéro civique du Conducteur
     * @param rue String de la rue du Conducteur
     * @param app String de l'appartement (s'il y a lieu) du Conducteur
     * @param ville String de la ville du Conducteur
     * @param codePostal String du code postal du Conducteur
     */
    public void setAdresse(String noCivique, String rue, String app, String ville, String codePostal) {
        this.adresse = new ArrayList<>(Arrays.asList(noCivique, rue, app, ville, codePostal));
    }
    
    /**
     * Permet d'ajouter une adresse courriel au Conducteur
     * @param courriel String de l'adresse courriel du Conducteur
     */
    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }
    
    /**
     * Permet de voir si le conducteur a des conditions particulières, tel que déterminé à la deuxième fenêtre de la soumission
     * @param specialConditions boolean s'il y a des conditions spéciales
     */
    public void setSpecialConditions(boolean specialConditions){
        this.specialConditions = specialConditions;
    }
    
    /**
     * Permet de set combien d'antivols ont été sélectionnés
     * @param antivols int du nombre d'antivols qui ont été sélectionnés
     */
    public void setAntivols(int antivols){
        this.antivols = antivols;
    }
    
    /**
     * Permet de set si l'habitation est aussi assurée avec nous 
     * @param assuranceHabitation boolean de si l'assurance est assurée = true, sinon false
     */
    public void setAssuranceHabitation(boolean assuranceHabitation){
        this.assuranceHabitation = assuranceHabitation;
    }
    
    /**
     * Permet de set la voiture, avec marque, modèle, année, prix et kilomètre
     * @param make String de la marque de la voiture
     * @param model String du modèle de la voiture
     * @param year String de l'année de la voiture
     * @param price String du prix de la voiture
     * @param km String du kilométrage annuel de la voiture
     */
    public void setVoiture(String make, String model, String year, String price, String km){
        voiture.put("Make", make);
        voiture.put("Model", model);
        voiture.put("Year", year);
        voiture.put("Price", price);
        voiture.put("Km", km);
    }
    
    /**
     * Permet de calculer la prime pour le conducteur
     * @return float du prix de la prime d'assurance automobile du conducteur
     */
    public float calculerPrime(){
        int primeBase;
        
        int age = getAge();
        
        if (age <= 20) primeBase = 500;
        else if (age <= 25) primeBase = 450;
        else if (age <= 30) primeBase = 400;
        else if (age <= 40) primeBase = 350;
        else if (age <= 50) primeBase = 300;
        else if (age <= 60) primeBase = 250;
        else if (age <= 70) primeBase = 200;
        else primeBase = 100;
        
        primeTotale = (int) primeBase;
                
        if (this.sexe == "Homme") primeTotale += primeBase * 0.15;
                
        if (this.specialConditions) primeTotale += primeBase * 0.30;
                
        Calendar currentDate = Calendar.getInstance();
        int yearsOld = currentDate.get(Calendar.YEAR) - Integer.parseInt(this.voiture.get("Year"));
        if (yearsOld < 5) primeTotale += (0.1 - yearsOld * 0.02) * Integer.parseInt(this.voiture.get("Price")); 
        
        if (Integer.parseInt(this.voiture.get("Km")) > 15000){
            int multOver = (int) Math.floor(Integer.parseInt(this.voiture.get("Km"))/5000 - 3);
            primeTotale += primeBase * (0.1 * multOver);
        }
        
        primeTotale -= primeBase * (0.05 * this.antivols);
                
        if (this.assuranceHabitation) primeTotale *= 0.8;
        
        toFile();
        
        return primeTotale;
    }
    
    /**
     * Retourne l'âge de la personne qui fait la soumission avec sa date de naissance et le jour présent
     * @return int de l'âge de la personne
     */
    private int getAge(){
        Date d = new Date();
        String[] splitDate = this.dateNaissance.split("-");
        Date birthday = new Date(Integer.parseInt(splitDate[0]) - 1900, Integer.parseInt(splitDate[1]) - 1, Integer.parseInt(splitDate[2]));
        
        long dateBeforeInMs = birthday.getTime();
        long dateAfterInMs = d.getTime();
        
        long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
        
        float yearsDiff = ((float) timeDiff / 1000f / 60f / 60f / 24f / 365f);

        
        return (int) yearsDiff;
    }
    
    /**
     * Envoie toutes les informations du conducteur et de la prime dans un nouveau fichier texte
     */
    private void toFile(){
        pathName = "soumission" + this.nom + this.prenom;
        File myObj = new File(pathName + ".txt");
        try {
            int i = 0;
            while (myObj.exists()) { // put this to File.exists and create after
                i++;
                myObj = new File(pathName + "V" + i + ".txt");
            } 
            
            if (i != 0) pathName = pathName + "V" + i + ".txt";
            FileWriter myWriter = new FileWriter (myObj);
            
            Calendar currentDate = Calendar.getInstance();
            myWriter.write("Soumission du " + currentDate.get(Calendar.YEAR) + "-" + (currentDate.get(Calendar.MONTH) + 1) + "-" + currentDate.get(Calendar.DATE) + "\n\n");
            
            myWriter.write("Prénom : " + this.prenom + "\n");
            myWriter.write("Nom : " + this.nom + "\n");
            myWriter.write("Sexe : " + this.sexe + "\n");
            myWriter.write("Age : " + getAge() + "\n");
            myWriter.write("Date de naissance : " + this.dateNaissance + "\n\n");
            
            myWriter.write("Numéro de tel. : " + this.noTelephone + "\n");
            myWriter.write("Adresse courriel : " + this.courriel + "\n");
            myWriter.write("Adresse : " + this.adresse.get(0) + " " + this.adresse.get(1));
            if (!this.adresse.get(2).isEmpty()) myWriter.write(" app. " + this.adresse.get(2));
            myWriter.write("\n");
            myWriter.write("Ville : " + this.adresse.get(3) + "\n");// fill this up
            myWriter.write("Code postal : " + this.adresse.get(4) + "\n\n");
            
            myWriter.write("Condition(s) particulières : "); // put like assurance
            if (this.specialConditions) myWriter.write("Oui");
            else myWriter.write("Non");
            myWriter.write("\n");
            myWriter.write("Marque de la voiture : " + this.voiture.get("Make") + "\n");
            myWriter.write("Modèle de la voiture : " + this.voiture.get("Model") + "\n");
            myWriter.write("Année de la voiture : " + this.voiture.get("Year") + "\n");
            
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
            String priceFormatted = formatter.format(Double.parseDouble(this.voiture.get("Price")));
            myWriter.write("Valeur de la voiture : " + priceFormatted + "\n");
            myWriter.write("Système(s) antivol : " + this.antivols + "\n");
            myWriter.write("Kilométrage annuel : " + this.voiture.get("Km") + "km\n");
            myWriter.write("Rabais applicable si la maison est assurée : ");
            if (this.assuranceHabitation) myWriter.write("Oui\n\n");
            else myWriter.write("Non\n\n");
            
            String primeFormatted = formatter.format((double) this.primeTotale);
            myWriter.write("Montant de la soumission : " + primeFormatted);
            
            myWriter.close();
            
            
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
