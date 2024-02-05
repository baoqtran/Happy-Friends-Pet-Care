package model;

/**
 * Model for VetTech
 *
 */
public class VetTech {
    private int vetTechId;
    private String vetTechName;





    public VetTech(int vetTechId, String vetTechName) {
        this.vetTechId = vetTechId;
        this.vetTechName = vetTechName;


    }

    public int getVetTechId() {
        return vetTechId;
    }

    public void setVetTechId(int vetTechId) {
        this.vetTechId = vetTechId;
    }

    public String getVetTechName() {
        return vetTechName;
    }

    public void setVetTechName(String vetTechName) {
        this.vetTechName = vetTechName;
    }

    /**
     * Override to display contactName as String
     *
     * @return contactName
     */
    @Override
    public String toString() {
        return (vetTechName);
    }
}