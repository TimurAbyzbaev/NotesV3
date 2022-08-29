package ru.abyzbaev.mynotes;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Note implements Parcelable {
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private static int counter = 1;
    private int id;
    //private static ArrayList<Note> notes = new ArrayList<>();
    private static HashMap<Integer, Note> notes = new HashMap<>();

    public static int getCounter() {
        return counter;
    }

    public static void deleteNote(int id) {
        notes.remove(id);
        //counter--;
    }

    /*public static void deleteNote(int id) {
        Note note = Note.getNotes().stream().filter(n -> n.getId() == id).findFirst().get();
        notes.remove(note);
        counter--;
    }*/

    /**
     * разобраться с добавление и вылетами приложения
     */
    public static void addNote() {
        counter++;

        notes.put(counter, new Note("Новая заметка", ""));
    }
    public static void addNote(int id,Note note) {
        notes.put(id, note);
    }

    {
        id = counter++;
    }

    static {

        for (int i = 1; i < 2; i++) {
            notes.put(i,getNote(i));
        }
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @SuppressLint("DefaultLocale")
    public static Note getNote(int index) {
        String name = String.format("Заметка %d", index);
        String descr = String.format("Описание %d", index);
        return new Note(name, descr);

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public static HashMap<Integer, Note> getNotes() {
        return notes;
    }

    /*public static ArrayList<Note> getNotes() {
        return notes;
    }*/

    protected Note(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getDescription());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
