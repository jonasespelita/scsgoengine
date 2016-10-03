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
    
    private List<GroupSetup> groups;
    private double manpower;

    public Engine(){
        this(null, 0);
    }

    /**
     * @param groups list of group setups
     * @param manpower the total manpower available
     */
    public Engine(List<GroupSetup> groups, double manpower){
        this.groups = groups;
        this.manpower = manpower;
    }
    
    public void run() {
        LOGGER.info("BEGIN Engine Run");
        
        LOGGER.info("Setting initial allocation");
        // initial allocation will be the average from the total manpower
        double imp = this.manpower / this.groups.size();
        this.groups.stream().peek( (GroupSetup p) -> p.setManpower(imp)).collect(Collectors.toList());

        LOGGER.info("Computing first pass allocation");
        // get minimum between maximum people assignment and maximum people to satisfy demand
        this.groups.stream().peek(Engine::computeMinimumManpower).collect(Collectors.toList());
        this.groups.stream().forEach(p -> LOGGER.log(Level.FINER, "\t{0}: {1}", new Object[]{p.getName(), p.getManpower()} ));
        
        LOGGER.info("Computing iterative proportional distribution");
        // the pool will serve as the stopping criterion
        
        double pool = 0;
        int iteration = 1;
        for(iteration=1; iteration<this.groups.size()-1; iteration++){
            LOGGER.log(Level.FINER, "ITERATION={0}=====", iteration);    
            LOGGER.log(Level.FINER, "--Computing MATCHED/UNMATCHED manpower");
            // let's get the total manpower that has been allocated, and those still unallocated
            double matched = this.groups.stream().filter(p -> p.getManpower() > 0).mapToDouble(p -> p.getManpower()).sum();
            double unmatched = this.groups.stream().filter(p -> p.getManpower() == 0).mapToDouble(p -> p.getMSD()).sum();
            LOGGER.log(Level.FINER, "\t**MATCHED={0}, UNMATCHED={1}, POOL={2}", new Object[]{matched, unmatched, this.manpower-matched});
            
            LOGGER.log(Level.FINER, "--Computing proportional distribution");
            // get the ratio and the corresponding distribution based on the unmatched demand
            this.groups.stream()
                    .parallel()
                    .filter(p -> p.getManpower() == 0)
                    .peek((GroupSetup p) -> Engine.computeProportionalDistribution(p, unmatched, this.manpower-matched))
                    .collect(Collectors.toList());
            this.groups.stream().forEach(p -> LOGGER.log(Level.FINEST, "\t{0}: {1}", new Object[]{p.getName(), p.getManpower()} ));
            
            // if the previous pool is the same as the current poool
            // then we can break the iteration since it means we are no longer getting improvements
            if(pool == (this.manpower - matched)){
                break;
            }else{
                pool = this.manpower - matched;
            }
        }
        LOGGER.log(Level.FINE, "Number of iterations: {0}", iteration);
        LOGGER.info("Finalizing allocation");
        // get the ratio and the corresponding distribution based on the unmatched demand
        double unmatched = this.groups.stream().filter(p -> p.getManpower() == 0).mapToDouble(p -> p.getMSD()).sum();
        double matched = this.groups.stream().filter(p -> p.getManpower() > 0).mapToDouble(p -> p.getManpower()).sum();
        this.groups.stream()
                .parallel()
                .filter(p -> p.getManpower() == 0)
                .peek((GroupSetup p) -> p.setManpower((p.getMSD()/unmatched)*(this.manpower-matched)))
                .collect(Collectors.toList());
        this.groups.stream().forEach(p -> LOGGER.log(Level.FINEST, "\t{0}: {1}", new Object[]{p.getName(), p.getManpower()} ));
        
        LOGGER.info("END Engine Run");
    }
    
    // STEP #7
    private static void computeProportionalDistribution(GroupSetup p, double unmatched, double pool){
        double ratio = p.getMSD()/unmatched;
        double dist = ratio * pool;
        if(dist >= p.getMaxManpower()){
            p.setManpower(Math.min(p.getMaxManpower(), p.getMSD()));
        }
    }
    
    // STEP #5
    private static void computeMinimumManpower(GroupSetup p){
        double min = Math.min(p.getMaxManpower(), p.getMSD());
        p.setManpower(min < p.getManpower() ? min : 0);
    }
    
    /**
     * Get the list of original or optimal group setup.
     * @return list of group setup
     */
    public List<GroupSetup> getGroupSetups(){
        return this.groups;
    }
    
    /**
     * Set the list of group setups
     * @param groups list of group setups
     */
    public void setGroupSetups(final List<GroupSetup> groups){
        this.groups = groups;
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

    /**
     * Get the excess manpower from the computed allocation
     * @return excess manpower
     */
    public double getExcessManpower(){
        return this.manpower - this.groups.stream().filter(p -> p.getManpower() > 0).mapToDouble(p -> p.getManpower()).sum();
    }
}
