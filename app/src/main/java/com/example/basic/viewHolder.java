package com.example.basic;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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
    public void setDetails(Context ctx,String jobTitle,String jobDescription)
    {
        TextView cTitle=mview.findViewById(R.id.company_name);
        TextView cDesc=mview.findViewById(R.id.company_desc);
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
