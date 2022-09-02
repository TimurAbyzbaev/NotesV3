package ru.abyzbaev.mynotes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class DatePickerFragment extends Fragment {

    private static final String ARG_NOTE = "ArgDate";

    private Note note;
    private Publisher publisher;

    private DatePicker datePicker;
    private Button okBtn;
    private Button backBtn;

    //Для редактирования данных
    public static DatePickerFragment newInstance(Note note) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);//тут что то не такъ
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);
        initView(view);
        if(note != null){
            populateView();
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        note = collectDate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }

//ТУТ ОБРАТИТЬ ВНИМАНИЕ
    private Note collectDate(){
        Date date = getDateFromDatePicker();
        return new Note(note.getTitle(),note.getDescription(),date);
    }

    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }

    private void initView(View view){
        datePicker.findViewById(R.id.datePicker);
        view.findViewById(R.id.okBtn);
        view.findViewById(R.id.backBtn);
    }

    private void populateView(){
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "OKOK", Toast.LENGTH_SHORT).show();
                Date newDate = getDateFromDatePicker();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        initDatePicker(note.getDate());
    }

    //Установка даты в DatePicker
    private void initDatePicker(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }

    /*public DatePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }*/
}