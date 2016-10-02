/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ti.sc.scsgo;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author a0285126
 */
public class Main {
    public static void main(String[] args) {
        List<PackageSetup> pkgs = Utility.readFromFile("input.csv");
        
        Engine engine = new Engine(pkgs, 143);
        engine.run();
        List<PackageSetup> ps = engine.getPackageSetups();
        ps.stream().forEach(p -> System.out.printf("%10s:\t%10.1f\t%10.3f\t%6.2f\t%6.2f\t%20.4f \n", 
                p.getName(),
                p.getManpower(),
                p.getEquipmentUtilized(),
                p.getEquipmentUtilization(),
                p.getDemandSatisfaction(),
                p.getTotalOutput()));
    }
}
