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
    public static List<PackageSetup> readFromFile(String file) {
        List<PackageSetup> list = null;
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            list = reader.lines()
                    .filter(line -> !(line.startsWith("#") || line.trim().equals("")))
                    .map(line -> line.split(","))
                    .map(token -> new PackageSetup(
                            token[1], 
                            Double.parseDouble(token[0]), 
                            Double.parseDouble(token[2]), 
                            Double.parseDouble(token[3]), 
                            Double.parseDouble(token[4]))
                    )
                    .collect(Collectors.toList());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}
