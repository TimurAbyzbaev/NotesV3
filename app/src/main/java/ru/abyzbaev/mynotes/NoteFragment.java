package ru.abyzbaev.mynotes;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NoteFragment extends Fragment implements Parcelable {

    static final String SELECTED_NOTE = "note";

    private Note note;
    private Publisher publisher;
    private Navigation navigation;

    private TextView tvTitle;
    private TextView datePickerTextView;
    private TextView tvDescription;


    //Для редактирования данных
    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    //Для добавления новых данных
    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    /**
     * Не понял как избавиться от бага кроме как этим способом
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (savedInstanceState != null)
            requireActivity().getSupportFragmentManager().popBackStack();*/
        if(getArguments() != null) {
            note = getArguments().getParcelable(SELECTED_NOTE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        //publisher = mainActivity.getPublisher();
    }

    @Override
    public void onDetach() {
        //publisher = null;
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState == null){
            setHasOptionsMenu(true);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        navigation = new Navigation(requireActivity().getSupportFragmentManager());
        initView(view);
        if (note != null){
            populateView();
        }

        return view;
    }

    //Сбор данных из View
    /*@Override
    public void onStop() {
        super.onStop();
        note = collectNoteData();
    }*/

    /*//Передача данных в Publisher
    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }*/

    /*private Note collectNoteData() {
        String title = this.tvTitle.getText().toString();
        String description = this.tvDescription.getText().toString();
        //Date date = getDateFromDatePicker();
        //String tvDate = this.datePickerTextView.getText().toString();

        return new Note(title,description,date);
    }*/

    /*private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }*/

    private void initView(View view){
        tvTitle = view.findViewById(R.id.tvTitle);
        datePickerTextView= view.findViewById(R.id.inputDate);
        tvDescription = view.findViewById(R.id.tvDescription);
    }

    private void populateView(){
        tvTitle.setText(note.getTitle());
        tvDescription.setText(note.getDescription());

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        datePickerTextView.setText(formatter.format(note.getDate()));

    }

























    public NoteFragment() {
        // Required empty public constructor
    }

    protected NoteFragment(Parcel in) {
        note = in.readParcelable(Note.class.getClassLoader());
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            Note paramNote = (Note) arguments.getParcelable(SELECTED_NOTE);
            if (paramNote == null) {
                note = Note.getNotes().get(1);
            } else {
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


            datePickerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO открыть фрагмент с пикером
                    Toast.makeText(requireContext(),"датапикер", Toast.LENGTH_SHORT).show();
                    initDatePicker(note);
                }
            });

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
    private void initDatePicker(Note note){
        navigation.addFragment(DatePickerFragment.newInstance(note), true);
        publisher.subscribe(new Observer() {
            @Override
            public void updateNoteData(Note note) {
                Toast.makeText(requireContext(), "UpdateNoteData", Toast.LENGTH_SHORT).show();

            }
        });
        /*DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(note);
        requireActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("")
                                .replace(R.id.notes_container, datePickerFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .commit();*/

        //navigation.addFragment(datePickerFragment,true);
        /*
        NoteFragment noteFragment = NoteFragment.newInstance(note);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.note_container, noteFragment)
                .addToBackStack("")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        */
    }

    private NotesFragment getNotesFragment(){
        return (NotesFragment) requireActivity().getSupportFragmentManager()
                .getFragments().stream().filter(fragment -> fragment instanceof NotesFragment).findFirst().get();
    }


    private void updateData() {
        getNotesFragment().initRecycleView();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(note, flags);
    }

    public static final Creator<NoteFragment> CREATOR = new Creator<NoteFragment>() {
        @Override
        public NoteFragment createFromParcel(Parcel in) {
            return new NoteFragment(in);
        }

        @Override
        public NoteFragment[] newArray(int size) {
            return new NoteFragment[size];
        }
    };

    public Date getDate() {
        return note.getDate();
    }

}