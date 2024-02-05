package model;

import java.time.LocalDateTime;
/**
 * Model class for Notes
 *
 */
public class Note {
    private int noteId;
    private String notes;
    private LocalDateTime created;
    private LocalDateTime lastUpdated;
    private int animalId;

    public Note(int noteId, String notes, LocalDateTime created, LocalDateTime lastUpdated, int animalId) {
        this.noteId = noteId;
        this.notes = notes;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.animalId = animalId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }
}