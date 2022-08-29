package ru.abyzbaev.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Note> notes;
    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ListAdapter(List<Note> notes){
        this.notes = notes;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //notes.get(position)

        if(notes.get(position).getTitle() == null){
            System.out.println("null");
        }
        else{
            holder.getTextView().setText(notes.get(position).getTitle());
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_frame,viewGroup,false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.card_text);

            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if(itemClickListener != null)
                        itemClickListener.onItemLongClick(view, position);
                    return true;
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(itemClickListener != null)
                        itemClickListener.onItemClick(view, position);
                }
            });

        }

        public TextView getTextView() {
            return textView;
        }
    }



}
