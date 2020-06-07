package com.example.basic;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder {
    View mview;
    Button del,edit;
    LinearLayout linearLayoutButton;
    CardView cardView;

    public viewHolder(View itemView)
    {
        super(itemView);
        cardView=itemView.findViewById(R.id.card);
        linearLayoutButton=(LinearLayout)itemView.findViewById(R.id.buttonPanelForUser);
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
        Picasso.get().load(linkLogo).resize(0,150).centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(logoImage);
        cTitle.setText(jobTitle);
        cDesc.setText(jobDescription);

    }
    /*public void setAppliedJobs(Context ctx,String jobTitle,String jobDescription)
    {
        TextView cTitle=mview.findViewById(R.id.companyTitle);
        TextView cDesc=mview.findViewById(R.id.companyDescription);
        cTitle.setText(jobTitle);
        cDesc.setText(jobDescription);

    }*/
    public void setSkills(Context ctx,String name)
    {
        TextView skill=mview.findViewById(R.id.skill_name);
        skill.setText(name);
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
