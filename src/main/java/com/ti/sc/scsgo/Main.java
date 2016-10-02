/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ti.sc.scsgo;

import java.util.List;
/**
 *
 * @author a0285126
 */
public class Main {
    public static void main(String[] args) {
        Utility util = new Utility();
        util.readFromFile(args[0]);
        
        Engine engine = new Engine(util.getPackageSetups(), util.getManpower());
        engine.run();
        List<PackageSetup> ps = engine.getPackageSetups();
        System.out.println("==========================================================================================");
        System.out.printf("%8s \t%8s\t%6s\t%10s\t%6s\t%6s\t%16s \n", 
                "NAME",
                "MAN",
                "MAX",
                "E-UTILD",
                "E-UTILN",
                "DEM SAT",
                "TTL OUT");
        System.out.println("==========================================================================================");
        ps.stream().forEach(p -> System.out.printf("%8s:\t%8.3f\t%8.3f\t%10.3f\t%6.2f\t%6.2f\t%16.4f \n", 
                p.getName(),
                p.getManpower(),
                p.getMaxManpower(),
                p.getEquipmentUtilized(),
                p.getEquipmentUtilization(),
                p.getDemandSatisfaction(),
                p.getTotalOutput()));
    }
}
