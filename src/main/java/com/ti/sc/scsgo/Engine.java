/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ti.sc.scsgo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Emulate an engine for manpower allocation.
 * @author a0285126
 */
public class Engine {

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());
    
    private List<PackageSetup> pkgs;
    private double manpower;

    public Engine(){
        this(null, 0);
    }

    /**
     * @param pkgs list of package setups
     * @param manpower the total manpower available
     */
    public Engine(List<PackageSetup> pkgs, double manpower){
        this.pkgs = pkgs;
        this.manpower = manpower;
    }
    
    /**
     * Start analysis to get the desired manpower allocation.
     */
    public void run(){
        LOGGER.log(Level.INFO, "Calculating the desired manpower allocation.");
        
        LOGGER.log(Level.INFO, "Computing initial allocation...");
        // the initial manpower will be the average: total manpower/number of packages
        double imanpower = this.manpower / this.pkgs.size();        
        LOGGER.log(Level.INFO, "Initial Allocation={0}", imanpower);

        LOGGER.log(Level.INFO, "Computing correct allocation...");
        
        // first pass
        LOGGER.log(Level.INFO, "First pass...");
        this.pkgs.stream()
                .parallel()
                .peek(p -> p.setManpower(imanpower))    // set the initial manpower to be the average
                .peek(Engine::computeMinimumManpower)                  // start decision
                .collect(Collectors.toList());
        
        // let's get the sum of those unmatched so we can create the proportional distribution
        double unmatchedSum = this.pkgs.stream()
                .filter(p -> p.getManpower() == 0)
                .mapToDouble(p -> p.getMSD())
                .sum();
        LOGGER.log(Level.INFO, "UNMATCHED SUM={0}", unmatchedSum);
        
        // get the matched items as well so we can deduct this amount to the overall ratio for distribution
        double matchedSum = this.pkgs.stream()
                .filter(p -> p.getManpower() > 0)
                .mapToDouble(p -> p.getManpower())
                .sum();
        LOGGER.log(Level.INFO, "MATCHED SUM={0}", matchedSum);
        
        // second pass
        // determine the allocated manpower based on ratio
        LOGGER.log(Level.INFO, "Second and final pass...");
        this.pkgs.stream()
                .filter(p -> p.getManpower() == 0)
                .peek(p -> p.setManpower((p.getMSD()/unmatchedSum)*(this.manpower-matchedSum)))
                .collect(Collectors.toList());       
     }

    // this is where computation of optimal allocation happens
    private static void computeMinimumManpower(PackageSetup p){
        double min = Math.min(p.getMaxManpower(), p.getMSD());
        p.setManpower( min < p.getManpower() ? min : 0);
    }
    
    /**
     * Get the list of original or optimal package setup.
     * @return list of package setup
     */
    public List<PackageSetup> getPackageSetups(){
        return this.pkgs;
    }
    
    /**
     * Set the list of package setups
     * @param pkgs list of package setups
     */
    public void setPackageSetups(final List<PackageSetup> pkgs){
        this.pkgs = pkgs;
    }
    
    /**
     * Set the total manpower to be allocated
     * @param manpower total manpower to be allocated
     */
    public void setTotalManpower(final double manpower){
        this.manpower = manpower;
    }
    
    /**
     * Get the original total number of manpower allocated.
     * @return total manpower
     */
    public double getTotalManpower(){
        return this.manpower;
    }    
}
