package com.example.basic;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.MyViewHolder> {

    ArrayList<Company> list=new ArrayList<>();
    private notificationAdapter.OnNoteListener mOnNoteListener;
    public notificationAdapter(ArrayList<Company> list, notificationAdapter.OnNoteListener onNoteListener){
        this.list=list;
        this.mOnNoteListener=onNoteListener;
    }
    @NonNull
    @Override
    public notificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_holder,viewGroup,false);
        notificationAdapter.MyViewHolder vHolder=new notificationAdapter.MyViewHolder(view,mOnNoteListener);
//        return new MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull notificationAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.id.setText(list.get(i).getJobTitle());
        myViewHolder.desc.setText(list.get(i).getJobDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        String postDate=list.get(i).getDatetime();
        Date date = null;
        try {
            date = dateFormat.parse(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
        myViewHolder.dateTime.setText("Posted : "+niceDateStr);
        Picasso.get().load(list.get(i).getLinkLogo()).fit().centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(myViewHolder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, desc,dateTime;
        ImageView img;
        notificationAdapter.OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, notificationAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            id = itemView.findViewById(R.id.comTitle);
            img=itemView.findViewById(R.id.comLogo);
            desc = itemView.findViewById(R.id.comDescription);
            dateTime=itemView.findViewById(R.id.comPostDate);
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
