package ru.abyzbaev.mynotes;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Note implements Parcelable {
    private String title;
    private String description;
    private Date date;
    private static int counter = 0;
    private int id;
    private static List<Note> notes = new ArrayList<>();
    public static int getCounter() {
        return counter;
    }

    public static void deleteNote(int id) {
        notes.remove(id);
        for (Note note : notes) {
            System.out.println(note.getId());
        }
        counter--;
    }

    /**
     * разобраться с добавление и вылетами приложения
     */
    public static void addNote() {
        notes.add(new Note("Новая заметка", "", Calendar.getInstance().getTime()));
    }
    public static void addNote(int id,Note note) {
        notes.add(note);
    }

    public static void returnNote(int position, Note note){
        notes.add(position, note);
    }

    {
        id = counter++;
    }

    static {
        for (int i = 0; i < 4; i++) {
            notes.add(getNote(i));
        }
    }

    public Note(String title, String description,Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    @SuppressLint("DefaultLocale")
    public static Note getNote(int index) {
        String name = String.format("Заметка %d", index);
        String descr = String.format("Описание %d", index);
        Date date = Calendar.getInstance().getTime();

        return new Note(name, descr, date);

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

    public Date getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static List<Note> getNotes() {
        return notes;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeLong(date.getTime());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
