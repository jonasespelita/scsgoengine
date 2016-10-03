/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ti.sc.scsgo;

/**
 * Represent a group setup used to feed the engine.
 * @author a0285126
 */
public class GroupSetup {
    // this is the group name
    private String name;
    // number of equipment assigned
    private double equipments;
    // number of equipments a person can handle (Equipment per Person)
    private double epp;
    // number of tentative manpower allocated
    private double manpower;
    // parts per hour per Equipment
    private double pph;
    // demand assigned
    private double demand;
    
    /**
     * @param name the name of the group
     */
    public GroupSetup(String name){
        this.name = name;
    }

    /**
     * 
     * @param name the name of the group
     * @param demand the expected demand
     * @param equipments the number of equipments (equipment count)
     * @param epp number of equipments per person
     * @param pph parts per hour
     */
    public GroupSetup(String name, double demand, double equipments, double pph, double epp){
        this.name = name;
        this.demand = demand;
        this.equipments = equipments;
        this.epp = epp;
        this.pph = pph;
    }
    
    /**
     * Get the name of the group.
     * @return the name of the group
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the group.
     * @param name the name of the group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the number of equipments (equipment count).
     * @return the equipment count
     */
    public double getEquipments() {
        return equipments;
    }

    /**
     * Set the number of equipments (equipment count).
     * @param equipments the number of equipments (equipment count)
     */
    public void setEquipments(double equipments) {
        this.equipments = equipments;
    }

    /**
     * Get the number of equipments per person.
     * @return the number of equipments per person
     */
    public double getEPP() {
        return epp;
    }

    /**
     * Set the number of equipments per person.
     * @param epp the number of equipments per person
     */
    public void setEPP(double epp) {
        this.epp = epp;
    }

    /**
     * Get the allocated manpower.
     * @return the allocated manpower
     */
    public double getManpower() {
        return manpower;
    }

    /**
     * Set the allocated manpower.
     * @param manpower the assigned manpower
     */
    public void setManpower(double manpower) {
        this.manpower = manpower;
    }

    /**
     * Get the parts per hour per equipment.
     * @return the parts per hour
     */
    public double getPPH() {
        return pph;
    }

    /**
     * Set the parts per hour per equipment.
     * @param pph the parts per hour
     */
    public void setPPH(double pph) {
        this.pph = pph;
    }

    /**
     * Get the current demand.
     * @return the current demand
     */
    public double getDemand() {
        return demand;
    }

    /**
     * Set the current demand.
     * @param demand the current demand
     */
    public void setDemand(double demand) {
        this.demand = demand;
    }
    
    // ----------------------------------------------------------------------------------
    // This section is for methods that uses derived values from the initial setup
    // ----------------------------------------------------------------------------------
    
    /**
     * Get the Parts per Week.
     * Computed based on PPH x 7 days X 24 hours.
     * @return 
     */
    public double getPPW(){
        return this.pph * 7 * 24;
    }
    
    /**
     * Get the maximum manpower allocation possible for this group setup
     * @return the maximum manpower to be assigned
     */
    public double getMaxManpower(){
        return this.equipments/this.epp;
    }
    
    /**
     * Get the total number of equipments needed to satisfy the demand.
     * @return total number of equipments
     */
    public double getESD(){
        return this.demand / this.getPPW();
    }
    
    /**
     * Get the total number of manpower needed to satisfy the demand.
     * @return total number of manpower
     */
    public double getMSD(){
        return this.getESD()/this.epp;
    }
    
    /**
     * Get the number of equipment utilized.
     * @return number of equipments
     */
    public double getEquipmentUtilized(){
        return this.manpower * this.epp;
    }
    
    /**
     * Get the equipment utilization factor.
     * @return equipment utilization factor
     */
    public double getEquipmentUtilization(){
        return this.getEquipmentUtilized()/this.equipments;
    }
    
    /**
     * Get the total output in units.
     * @return Total output in units.
     */
    public double getTotalOutput(){
        return this.getEquipmentUtilized() * this.getPPW();
    }
    
    /**
     * Get the overall demand satisfaction factor.
     * @return demand satisfaction factor
     */
    public double getDemandSatisfaction(){
        if(this.demand == 0){
            return 0;
        }else{
            return this.getTotalOutput()/this.demand;
        }
    }
    
    /**
     * Get the suggested minimum manpower to satisfy demand.
     * @return suggested minimum manpower
     */
    public double getSuggestedMinManpower(){
        return Math.min(this.getMaxManpower(), this.getMSD());
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
