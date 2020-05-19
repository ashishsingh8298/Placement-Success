package com.example.basic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    ArrayList<Company> list=new ArrayList<>();
    private OnNoteListener mOnNoteListener;
    public AdapterClass(ArrayList<Company> list,OnNoteListener onNoteListener){
        this.list=list;
        this.mOnNoteListener=onNoteListener;
    }
    @NonNull
    @Override
    public AdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder,viewGroup,false);
        MyViewHolder vHolder=new MyViewHolder(view,mOnNoteListener);
//        return new MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClass.MyViewHolder myViewHolder, int i) {
        myViewHolder.id.setText(list.get(i).getJobTitle());
        myViewHolder.desc.setText(list.get(i).getJobDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, desc;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            id = itemView.findViewById(R.id.companyTitle);
            desc = itemView.findViewById(R.id.companyDescription);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
    }
}
