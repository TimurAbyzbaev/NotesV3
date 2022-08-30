package ru.abyzbaev.mynotes;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_menu, menu);

        MenuItem menuItemAdd = menu.findItem(R.id.action_add);
        if(menuItemAdd != null && !isLandscape())
            menuItemAdd.setVisible(false);

    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Удаление заметки
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete)
        {
            //TODO: Удаление заметки ...
            new AlertDialog.Builder(requireContext()).setTitle("Внимание!")
                    .setMessage("Вы действительно хотите удалить заметку?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Note tempNote = note;
                            int tempId = note.getId();
                            requireActivity().getSupportFragmentManager().popBackStack();
                            Note.deleteNote(note.getId());
                            updateData();
                            getNotesFragment().showSnackbar(tempId, tempNote);

                        }
                    })
                    .setNegativeButton("Нет", null)
                    .setNeutralButton("Отмена", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState == null){
            setHasOptionsMenu(true);
        }
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
                //note = Note.getNotes().get(paramNote.getId());
                note  = paramNote;
            }
            /**
             * Кнопка назад
             */
            Button buttonBack = view.findViewById(R.id.btnBack);
            buttonBack.setOnClickListener(v -> {
                requireActivity().getSupportFragmentManager().popBackStack();
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



    private NotesFragment getNotesFragment(){
        return (NotesFragment) requireActivity().getSupportFragmentManager()
                .getFragments().stream().filter(fragment -> fragment instanceof NotesFragment).findFirst().get();
    }


    private void updateData() {
        //getNotesFragment().initNotes();
        getNotesFragment().initRecycleView();
    }

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }
}