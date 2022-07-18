package ru.abyzbaev.mynotes;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class NoteFragment extends Fragment {

    static final String SELECTED_NOTE = "note";
    private Note note;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Не понял как избавиться от бага кроме как этим способом
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            Note paramNote = (Note) arguments.getParcelable(SELECTED_NOTE);
            if (paramNote == null) {
                //note = Note.getNotes().
                note = Note.getNotes().get(1);
            } else {
                //note = Note.getNotes().stream().filter(n -> n.getId() == paramNote.getId()).findFirst().get();
                note = Note.getNotes().get(paramNote.getId());
            }
            /**
             * Кнопка назад
             */
            Button buttonBack = view.findViewById(R.id.btnBack);
            buttonBack.setOnClickListener(v -> {
                requireActivity().getSupportFragmentManager().popBackStack();
            });

            /**
             * Удаление заметки
             */
            FloatingActionButton deleteButton = view.findViewById(R.id.btnDelete);
            deleteButton.setOnClickListener(v -> {
                //TODO удалить заметку
                requireActivity().getSupportFragmentManager().popBackStack();
                Note.deleteNote(note.getId());
                updateData();
            });

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                buttonBack.setClickable(false);
                buttonBack.setVisibility(View.INVISIBLE);
            }


            TextView tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText(note.getTitle());
            tvTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    note.setTitle(tvTitle.getText().toString());
                    updateData();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            TextView tvDescription = view.findViewById(R.id.tvDescription);
            tvDescription.setText(note.getDescription());
            tvDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    note.setDescription(tvDescription.getText().toString());
                    updateData();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }

    }

    private void updateData() {
        NotesFragment notesFragment = (NotesFragment) requireActivity()
                .getSupportFragmentManager()
                .getFragments().stream()
                .filter(fragment -> fragment instanceof NotesFragment)
                .findFirst()
                .get();
        notesFragment.initNotes();
    }

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }
}