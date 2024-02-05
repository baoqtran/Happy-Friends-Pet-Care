package model;
/**
 * Model class for Animals
 *
 */

public class Animal {
    private int animalId;
    private String animalName;
    private String animalType;
    private int animalAge;
    private String animalBreed;
    private int animalWeight;
    private int animalOwner;
    private int animalNotesId;
    private String animalNotes;
    private int customerId;
    private String animalBreed2;
    private int breedTotal;

    public Animal(){

    }
    public String getAnimalBreed2() {
        return animalBreed2;
    }

    public void setAnimalBreed2(String animalBreed2) {
        this.animalBreed2 = animalBreed2;
    }

    public Animal(String animalBreed2, int breedTotal) {
        this.animalBreed2 = animalBreed2;
        this.breedTotal = breedTotal;
    }

    public int getBreedTotal() {
        return breedTotal;
    }

    public void setBreedTotal(int breedTotal) {
        this.breedTotal = breedTotal;
    }

    public Animal(int animalId, String animalName, String animalType, int animalAge, String animalBreed, int animalWeight, int animalOwner, String animalOwnerName) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.animalType = animalType;
        this.animalAge = animalAge;
        this.animalBreed = animalBreed;
        this.animalWeight = animalWeight;
        this.animalOwner = animalOwner;
        this.animalOwnerName = animalOwnerName;

    }

    public Animal(int animalId, String animalName, int customerId, String type, int age, String breed, int weight) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.customerId = customerId;
    }

    public Animal(int animalId, String animalName) {
        this.animalId = animalId;
        this.animalName = animalName;

    }


    public int getAnimalNotesId() {
        return animalNotesId;
    }

    public void setAnimalNotesId(int animalNotesId) {
        this.animalNotesId = animalNotesId;
    }

    public String getAnimalNotes() {
        return animalNotes;
    }

    public void setAnimalNotes(String animalNotes) {
        this.animalNotes = animalNotes;
    }

    public String getAnimalOwnerName() {
        return animalOwnerName;
    }

    public void setAnimalOwnerName(String animalOwnerName) {
        this.animalOwnerName = animalOwnerName;
    }

    private String animalOwnerName;

    public Animal(int animalId, String animalName, String animalType, int animalAge, String animalBreed, int animalWeight, int animalOwner, String animalOwnerName, int animalNotesId, String animalNotes) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.animalType = animalType;
        this.animalAge = animalAge;
        this.animalBreed = animalBreed;
        this.animalWeight = animalWeight;
        this.animalOwner = animalOwner;
        this.animalOwnerName = animalOwnerName;
        this.animalNotesId = animalNotesId;
        this.animalNotes = animalNotes;
    }

    public Animal(String animalName, String animalType, int animalAge, String animalBreed, int animalWeight, int animalOwner) {

        this.animalName = animalName;
        this.animalType = animalType;
        this.animalAge = animalAge;
        this.animalBreed = animalBreed;
        this.animalWeight = animalWeight;
        this.animalOwner = animalOwner;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public int getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(int animalAge) {
        this.animalAge = animalAge;
    }

    public String getAnimalBreed() {
        return animalBreed;
    }

    public void setAnimalBreed(String animalBreed) {
        this.animalBreed = animalBreed;
    }

    public int getAnimalWeight() {
        return animalWeight;
    }

    public void setAnimalWeight(int animalWeight) {
        this.animalWeight = animalWeight;
    }

    public int getAnimalOwner() {
        return animalOwner;
    }

    public void setAnimalOwner(int animalOwner) {
        this.animalOwner = animalOwner;
    }

    public boolean isValid() throws Exception {
        String weight = String.valueOf(this.animalWeight);
        try {
            if (this.animalName.isEmpty()) {
                throw new Exception(("Please enter animal name."));
            }
            if (this.animalBreed.isEmpty()) {
                throw new Exception(("Please enter animal breed."));
            }
            if (this.animalType.isEmpty()){
                throw new Exception(("Please enter an animal type."));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    @Override
    public String toString() {
        return "#" + Integer.toString(animalId) + " - " + animalName;
    }
}
