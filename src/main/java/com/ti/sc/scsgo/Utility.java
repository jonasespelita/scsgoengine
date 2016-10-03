/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ti.sc.scsgo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author a0285126
 */
public class Utility {
    
    private double manpower;
    private String datestr;
    private List<PackageSetup> packageSetups;
    
    public void readFromFile(String file) {
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            this.manpower = Double.parseDouble(reader.readLine());
            this.datestr = reader.readLine();
            this.packageSetups = reader.lines()
                    .filter(line -> !(line.startsWith("#") || line.trim().equals("")))
                    .map(line -> line.split(","))
                    .map(token -> new PackageSetup(
                            token[1],                           // demand
                            Double.parseDouble(token[0]),       // name
                            Double.parseDouble(token[2]),       // equipment count
                            Double.parseDouble(token[3]),       // pph
                            Double.parseDouble(token[4]))       // epp
                    )
                    .collect(Collectors.toList());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * @return the manpower
     */
    public double getManpower() {
        return manpower;
    }

    /**
     * @return the packageSetups
     */
    public List<PackageSetup> getPackageSetups() {
        return packageSetups;
    }
    
    /**
     * Get the date header specified in the input file.
     * @return 
     */
    public String getDateHeader(){
        return this.datestr;
    }
}
