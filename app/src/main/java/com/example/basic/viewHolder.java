package com.example.basic;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder {
    View mview;

    public viewHolder(View itemView)
    {
        super(itemView);
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

    }
    public void setDetails(Context ctx,String jobTitle,String jobDescription,String linkLogo)
    {
        TextView cTitle=mview.findViewById(R.id.company_name);
        TextView cDesc=mview.findViewById(R.id.company_desc);
        ImageView logoImage=mview.findViewById(R.id.company_logo);
        //String Description=jobDescription.substring(0,20);
        Picasso.get().load(linkLogo).fit().centerCrop().placeholder(R.drawable.images).error(R.drawable.error).into(logoImage);
        cTitle.setText(jobTitle);
        cDesc.setText(jobDescription);

    }
    private viewHolder.ClickListener mClickListener;

    public interface ClickListener
    {
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
public void setOnClickListener(viewHolder.ClickListener clickListener)
{
    mClickListener=clickListener;
}


}
