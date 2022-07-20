package ru.abyzbaev.mynotes;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.print.PrintAttributes;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import static ru.abyzbaev.mynotes.NoteFragment.SELECTED_NOTE;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class NotesFragment extends Fragment {
    Note note;
    View dataContainer;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SELECTED_NOTE, note);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        /**
         * Кнопка "добавить" заметку
         */
        FloatingActionButton addButton = view.findViewById(R.id.btnAdd);
        addButton.setOnClickListener(n -> {
            Note.addNote();
            initNotes();
            note = Note.getNotes().get(Note.getCounter() - 1);
            showNoteDetails(note);
        });

        if (savedInstanceState != null) {
            note = (Note) savedInstanceState.getParcelable(SELECTED_NOTE);
        }
        dataContainer = view.findViewById(R.id.data_container);
        initNotes(dataContainer);
        if (isLandscape()) {
            showLandNoteDetails(note);
        }
    }



    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void initNotes() {
        initNotes(dataContainer);
    }

    private void initNotes(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        layoutView.removeAllViews();
        HashMap<Integer,Note> notes = Note.getNotes();
        int i = 1;
        for (Map.Entry<Integer, Note> note: notes.entrySet())
        {
            TextView tv = new TextView(getContext());
            tv.setText(note.getValue().getTitle());
            tv.setTextSize(24);
            tv.setPadding(50,15,5,5);
            layoutView.addView(tv);
            //final int index = i;
            tv.setOnClickListener(V -> {
                showNoteDetails(note.getValue());
                    });
            /*tv.setOnClickListener(v ->
                    showNoteDetails(Note.getNotes().get(index)));

            );*/
            initPopupMenu(tv, note);

            i++;
        }
    }

    private void initPopupMenu(TextView tv, Map.Entry<Integer, Note> note) {
        tv.setOnLongClickListener(n -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity,n);
            activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.action_popup_delete:
                            Note.deleteNote(note.getValue().getId());
                            initNotes();
                            return true;
                    }
                    return true;
                }
            });
            popupMenu.show();
            return true;
        });
    }

    private void showNoteDetails(Note note) {
        this.note = note;
        if (isLandscape()) {
            showLandNoteDetails(note);
        } else {
            showPortNoteDetails(note);
        }
    }

    private void showPortNoteDetails(Note note) {
        NoteFragment noteFragment = NoteFragment.newInstance(note);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.notes_container, noteFragment)
                .addToBackStack("")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }


    private void showLandNoteDetails(Note note) {
        NoteFragment noteFragment = NoteFragment.newInstance(note);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.note_container, noteFragment)
                .addToBackStack("")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}