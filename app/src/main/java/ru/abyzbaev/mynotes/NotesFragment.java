package ru.abyzbaev.mynotes;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import static ru.abyzbaev.mynotes.NoteFragment.SELECTED_NOTE;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;

public class NotesFragment extends Fragment {
    Note note;
    RecyclerView recyclerView;

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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recycle_view_lines);
        initRecycleView(recyclerView, Note.getNotes());
        return view;
    }



    private void initRecycleView(RecyclerView recyclerView, List<Note> notes){
        ListAdapter listAdapter = new ListAdapter(notes);
        recyclerView.setAdapter(listAdapter);
        listAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), String.format("%s - %d", ((TextView)view).getText(), position),Toast.LENGTH_SHORT).show();
                showNoteDetails(notes.get(position));
            }
            @Override
            public void onItemLongClick(View view, int position) {
                initPopupMenu(view, notes.get(position), position);
            }
        });
    }


    private void initPopupMenu(View view, Note note, int position) {
        Activity activity = requireActivity();
        PopupMenu popupMenu = new PopupMenu(activity, view);
        activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_popup_delete:
                        Note tempNote = note;
                        Note.deleteNote(position);
                        initRecycleView(recyclerView, Note.getNotes());
                        showSnackbar(position,tempNote);
                        return true;
                }
                return true;
            }
        });
        popupMenu.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(SELECTED_NOTE);
        }
    }

    /**
     * Добавить заметку
     */
    public void addNote(){
        Note.addNote();
        note = Note.getNotes().get(Note.getCounter()-1);
        initRecycleView();
        showNoteDetails(note);
    }


    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void initRecycleView(){
        initRecycleView(recyclerView, Note.getNotes());
    }


    public void showSnackbar(int position, Note tempNote){
        Snackbar.make(requireView(),"Заметка удалена", BaseTransientBottomBar.LENGTH_LONG).setAction("Return", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note.returnNote(position, tempNote);
                initRecycleView();
            }
        }).show();
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