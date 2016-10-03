/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ti.sc.scsgo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
/**
 *
 * @author a0285126
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
        Utility util = new Utility();
        util.readFromFile(args[0]);
        
        Engine engine = new Engine(util.getGroupSetups(), util.getManpower());
        
        LOGGER.log(Level.INFO, "START_TIME={0}", LocalDateTime.now());
        engine.run();
        LOGGER.log(Level.INFO, "END_TIME={0}", LocalDateTime.now());
        List<GroupSetup> ps = engine.getGroupSetups();
        ps = ps.stream().sorted((p1, p2) -> Double.compare(p1.getEquipmentUtilization(), p2.getEquipmentUtilization())).collect(Collectors.toList());
        System.out.printf("TOTAL MANPOWER: %.2f\n", engine.getTotalManpower());
        System.out.printf("EXCESS MANPOWER: %.2f\n", engine.getExcessManpower());
        System.out.println("======================================================================================================================================================");
        System.out.printf("%20s \t%16s\t%8s\t%8s\t%10s\t%8s\t%8s\t%16s \n", 
                "NAME",
                "MAN ("+util.getDateHeader()+") ",
                "MAX",
                "SMIN",
                "E-UTILD",
                "E-UTILN",
                "DEM_SAT",
                "TTL_OUT");
        System.out.println("======================================================================================================================================================");
        ps.stream().forEach(p -> System.out.printf("%20s\t%16.3f\t%8.3f\t%8.3f\t%10.3f\t%8.2f\t%8.2f\t%16.4f \n", 
                p.getName(),
                p.getManpower(),
                p.getMaxManpower(),
                p.getSuggestedMinManpower(),
                p.getEquipmentUtilized(),
                p.getEquipmentUtilization(),
                p.getDemandSatisfaction(),
                p.getTotalOutput()));
    }
}
