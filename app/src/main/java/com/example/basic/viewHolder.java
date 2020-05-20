package com.example.basic;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder {
    View mview;
    Button del,edit;

    public viewHolder(View itemView)
    {
        super(itemView);
        del=itemView.findViewById(R.id.Delete);
        edit=itemView.findViewById(R.id.Edit);
        mview=itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view,getAdapterPosition());
                return true;
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onDeleteClick(view,getAdapterPosition());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onEditClick(view,getAdapterPosition());
            }
        });


    }
    public void setDetails(Context ctx,String jobTitle,String jobDescription,String linkLogo)
    {
        TextView cTitle=mview.findViewById(R.id.company_name);
        TextView cDesc=mview.findViewById(R.id.company_desc);
        ImageView logoImage=mview.findViewById(R.id.company_logo);
        //String Description=jobDescription.substring(0,20);
        Picasso.get().load(linkLogo).fit().centerCrop().placeholder(R.drawable.logo).error(R.drawable.error).into(logoImage);
        cTitle.setText(jobTitle);
        cDesc.setText(jobDescription);

    }
    public void setComments(Context ctx,String userId,String Comment,String Date)
    {
        TextView user_Id=mview.findViewById(R.id.user_name);
        TextView userComment=mview.findViewById(R.id.comment);
        TextView comment_date=mview.findViewById(R.id.date);
        user_Id.setText(userId);
        userComment.setText(Comment);
        comment_date.setText(Date);

    }
    private viewHolder.ClickListener mClickListener;

    public interface ClickListener
    {
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
        void onDeleteClick(View view,int position);
        void onEditClick(View view,int position);
    }
public void setOnClickListener(viewHolder.ClickListener clickListener)
{
    mClickListener=clickListener;
}


}
