package com.example.basic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Company_Description extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner choose_spinner,show_spinner;
    private static final String TAG="Company_Description";
    private FirebaseAuth mAuth;
    TextView c_name,c_desc,c_res,c_ctc,c_eligibility,c_skills,c_criteria,start_date,end_date,feed;
    Button apply,postComment;
//    linear layout for edit and delete button
    LinearLayout linearLayoutButton;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef,cRef,uRef,genRef,qaRef,allRef;
    FirebaseUser mUser;
    ImageView c_logo;
    String temp,user_id,date,u_name,u_id,user_name,apply_link;
    Calendar calendar;
    SimpleDateFormat df;
    LinearLayoutManager mLinerLayoutManager;
    private RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<Comment,viewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Comment> options;
    Comment c;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company__description);
        checkConnection();
        /*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotifications")
                .setContentTitle("Title")
                .setSmallIcon(R.drawable.ic_error_black_24dp)
                .setAutoCancel(true)
                .setContentText("This is my text");

        NotificationManagerCompat manager= NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());*/
        choose_spinner=findViewById(R.id.spinner);
        show_spinner=findViewById(R.id.showSpinner);
        String[] companyType=getResources().getStringArray(R.array.comment_type);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,companyType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose_spinner.setAdapter(adapter);
        String[] companyTypeShow=getResources().getStringArray(R.array.comment_type_show);
        ArrayAdapter adapterShow=new ArrayAdapter(this,android.R.layout.simple_spinner_item,companyTypeShow);
        adapterShow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        show_spinner.setAdapter(adapterShow);
        String item=show_spinner.getSelectedItem().toString();
        //Toast.makeText(Company_Description.this,item,Toast.LENGTH_SHORT).show();

        Intent intent=getIntent();
        String temp=intent.getStringExtra("str");
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        user_id=mUser.getUid();
        user_name=mUser.getDisplayName();
        calendar=Calendar.getInstance();
        df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date=df.format(calendar.getTime());

        mDatabase= FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Company").child(temp);
        cRef=FirebaseDatabase.getInstance().getReference("Company").child(temp).child("Comments");
        genRef=FirebaseDatabase.getInstance().getReference("Company").child(temp).child("Comments").child("General");
        qaRef=FirebaseDatabase.getInstance().getReference("Company").child(temp).child("Comments").child("QA");
        allRef=FirebaseDatabase.getInstance().getReference("Company").child(temp).child("Comments").child("All");
        uRef=mDatabase.getReference("Users");
        mLinerLayoutManager=new LinearLayoutManager(this);
        mLinerLayoutManager.setReverseLayout(true);
        mLinerLayoutManager.setStackFromEnd(true);
        mRecyclerView= findViewById(R.id.list);
        //mRecyclerView.setHasFixedSize(true);
       swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);


        feed=findViewById(R.id.Feedback);
        c_logo=findViewById(R.id.Company_logo);
        postComment=findViewById(R.id.post);
        c_name=findViewById(R.id.company_name);
        c_desc=findViewById(R.id.Company_desc_content);
        c_res=findViewById(R.id.Company_res_content);
        c_ctc=findViewById(R.id.Company_ctc_content);
        c_eligibility=findViewById(R.id.Company_branch_content);
        c_skills=findViewById(R.id.Company_skills_content);
        c_criteria=findViewById(R.id.Company_critera_content);
        start_date=findViewById(R.id.Company_start_content);
        end_date=findViewById(R.id.Company_end_content);

        //showAllData();
        /*if(item.equals("General"))
        {
            showGeneralData();
        }
        else if(item.equals("Question/Answer"))
        {
            showQaData();
        }
        else
            showAllData();*/
        show_spinner.setOnItemSelectedListener(this);

        apply=findViewById(R.id.apply);

       mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String logo=dataSnapshot.child("linkLogo").getValue(String.class);
                String name = dataSnapshot.child("jobTitle").getValue(String.class);
                String desc = dataSnapshot.child("jobDescription").getValue(String.class);
                String res =dataSnapshot.child("jobResponsibility").getValue(String.class);
                String ctc =dataSnapshot.child("jobCtc").getValue(String.class);
                String branch =dataSnapshot.child("elegibleBranch").getValue(String.class);
                String skills =dataSnapshot.child("requiredSkills").getValue(String.class);
                String criteria =dataSnapshot.child("elegibilityCriteria").getValue(String.class);
                String s_date =dataSnapshot.child("applyDateFrom").getValue(String.class);
                String e_date =dataSnapshot.child("applyDateTo").getValue(String.class);
                apply_link=dataSnapshot.child("link").getValue(String.class);
                Picasso.get().load(logo).resize(0,200).centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(c_logo);
                    c_name.setText(name);
                    c_desc.setText(desc);
                    c_res.setText(res);
                    c_ctc.setText(ctc);
                    c_eligibility.setText(branch);
                    c_skills.setText(skills);
                    c_criteria.setText(criteria);
                    start_date.setText(s_date);
                    end_date.setText(e_date);

                SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
                String cur_date=sdf.format(calendar.getTime());

                if(e_date.compareTo(cur_date)<0)
                {
                    apply.setEnabled(false);
                    apply.setBackgroundColor(Color.GRAY);
                    apply.setText("Expired");
                }
                if(s_date.compareTo(cur_date)>0)
                {
                    apply.setEnabled(false);
                    apply.setBackgroundColor(Color.GRAY);
                    apply.setText("Coming Soon");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String appliedJob=dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("appliedJobs").child(temp).getValue(String.class);
                if(appliedJob!=null) {
                    if (appliedJob.equals("applied")) {
                        apply.setEnabled(false);
                        apply.setText("Applied");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Company_Description.this);
                builder.setMessage("Do you want to apply for this job?");
                builder.setTitle("Apply");
                builder.setIcon(R.drawable.ic_check_circle_black_24dp);
                builder.setCancelable(false);
                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference u_id=uRef.child(user_id).child("appliedJobs");
                        u_id.child(temp).setValue("applied");
                        apply.setEnabled(false);
                        apply.setText("Applied");
                        Uri uri=Uri.parse(apply_link);
                        Intent in=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(in);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                    }
                });
                alertDialog.show();
            }
        });

       postComment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String comment=feed.getText().toString().trim();

               if(TextUtils.isEmpty(comment))
               {
                   Toast.makeText(Company_Description.this,"Write your Feedback",Toast.LENGTH_LONG).show();
               }
               else {
                   String item=choose_spinner.getSelectedItem().toString();
                   Toast.makeText(Company_Description.this, "Feedback received.", Toast.LENGTH_LONG).show();
                   uRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           String user_id = mAuth.getCurrentUser().getUid();
                           String name = dataSnapshot.child(user_id).child("Name").getValue(String.class);
                           if(item.equals("General")) {
                               DatabaseReference ref = cRef.child("General").push();
                               ref.child("userId").setValue(user_id);
                               ref.child("Comment").setValue(comment);
                               ref.child("Date").setValue(date);
                               ref.child("Name").setValue(name);
                               allRef.child(ref.getKey()).child("userId").setValue(user_id);
                               allRef.child(ref.getKey()).child("Comment").setValue(comment);
                               allRef.child(ref.getKey()).child("Date").setValue(date);
                               allRef.child(ref.getKey()).child("Name").setValue(name);
                           }
                           else
                           {
                               DatabaseReference ref = cRef.child("QA").push();
                               ref.child("userId").setValue(user_id);
                               ref.child("Comment").setValue(comment);
                               ref.child("Date").setValue(date);
                               ref.child("Name").setValue(name);
                               allRef.child(ref.getKey()).child("userId").setValue(user_id);
                               allRef.child(ref.getKey()).child("Comment").setValue(comment);
                               allRef.child(ref.getKey()).child("Date").setValue(date);
                               allRef.child(ref.getKey()).child("Name").setValue(name);
                           }

                           feed.setText("");
                           //mRecyclerView.setAdapter(firebaseRecyclerAdapter);

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

               }
           }

       });
       swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               //mRecyclerView.setAdapter(firebaseRecyclerAdapter);
               swipeRefreshLayout.setRefreshing(false);
           }
       });

    }
    private  void  showGeneralData()
    {

        options=new FirebaseRecyclerOptions.Builder<Comment>().setQuery(genRef,Comment.class).build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Comment, viewHolder>(options) {

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row,parent,false);

                viewHolder vHolder=new viewHolder(itemView);
                linearLayoutButton=(LinearLayout)findViewById(R.id.buttonPanelForUser);
                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                    @Override
                    public void onDeleteClick(View view, int position) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(Company_Description.this);
                        builder.setMessage("Do you want to delete this comment?");
                        builder.setTitle("Delete");
                        builder.setIcon(R.drawable.ic_delete_black_24dp);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String del=firebaseRecyclerAdapter.getRef(position).getKey();
                                genRef.child(del).removeValue();
                                allRef.child(del).removeValue();
                                notifyItemRemoved(position);
                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                                Toast.makeText(Company_Description.this,"Comment successfully deleted",Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                            }
                        });
                        alertDialog.show();

                    }

                    @Override
                    public void onEditClick(View view, int position) {
                        String edit=firebaseRecyclerAdapter.getRef(position).getKey();
                        AlertDialog.Builder builder=new AlertDialog.Builder(Company_Description.this);

                        builder.setMessage("Do you want to edit this comment?");
                        builder.setTitle("Alert !");
                        builder.setIcon(R.drawable.ic_edit_black_24dp);
                        builder.setCancelable(false);
                        final EditText input=new EditText(Company_Description.this);
                        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        input.setLayoutParams(lp);
                        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newComment=input.getText().toString();
                                genRef.child(edit).child("Comment").setValue(newComment);
                                allRef.child(edit).child("Comment").setValue(newComment);
                                notifyDataSetChanged();
                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                                Toast.makeText(Company_Description.this,"Comment edited.",Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        DatabaseReference t=genRef.child(edit);
                        t.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String oldComment=dataSnapshot.child("Comment").getValue(String.class);
                                input.setText(oldComment);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        AlertDialog alertDialog=builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                            }
                        });
                        alertDialog.setView(input);
                        alertDialog.show();
                    }
                });
                return vHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Comment model) {
                linearLayoutButton=(LinearLayout) findViewById(R.id.buttonPanelForUser);

                    if (mUser.getUid().equals(model.getUserId())){
                        holder.linearLayoutButton.setVisibility(LinearLayout.VISIBLE);
                    }
                    else {
                        holder.linearLayoutButton.setVisibility(LinearLayout.INVISIBLE);
                    }
                String dateStr = model.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
                Date date = null;
                try {
                    date = dateFormat.parse(dateStr);
                    String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
                    holder.setComments(getApplicationContext(),model.getName(),model.getComment(),niceDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        mRecyclerView.setLayoutManager(mLinerLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    private  void  showQaData()
    {

        options=new FirebaseRecyclerOptions.Builder<Comment>().setQuery(qaRef,Comment.class).build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Comment, viewHolder>(options) {

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row,parent,false);

                viewHolder vHolder=new viewHolder(itemView);
                linearLayoutButton=(LinearLayout)findViewById(R.id.buttonPanelForUser);
                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                    @Override
                    public void onDeleteClick(View view, int position) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(Company_Description.this);
                        builder.setMessage("Do you want to delete this comment?");
                        builder.setTitle("Delete");
                        builder.setIcon(R.drawable.ic_delete_black_24dp);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String del=firebaseRecyclerAdapter.getRef(position).getKey();
                                qaRef.child(del).removeValue();
                                allRef.child(del).removeValue();
                                notifyItemRemoved(position);
                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                                Toast.makeText(Company_Description.this,"Comment successfully deleted",Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                            }
                        });
                        alertDialog.show();

                    }

                    @Override
                    public void onEditClick(View view, int position) {
                        String edit=firebaseRecyclerAdapter.getRef(position).getKey();
                        AlertDialog.Builder builder=new AlertDialog.Builder(Company_Description.this);

                        builder.setMessage("Do you want to edit this comment?");
                        builder.setTitle("Alert !");
                        builder.setIcon(R.drawable.ic_edit_black_24dp);
                        builder.setCancelable(false);
                        final EditText input=new EditText(Company_Description.this);
                        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        input.setLayoutParams(lp);
                        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newComment=input.getText().toString();
                                qaRef.child(edit).child("Comment").setValue(newComment);
                                allRef.child(edit).child("Comment").setValue(newComment);
                                notifyDataSetChanged();
                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                                Toast.makeText(Company_Description.this,"Comment edited.",Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        DatabaseReference t=qaRef.child(edit);
                        t.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String oldComment=dataSnapshot.child("Comment").getValue(String.class);
                                input.setText(oldComment);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        AlertDialog alertDialog=builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                            }
                        });
                        alertDialog.setView(input);
                        alertDialog.show();
                    }
                });
                return vHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Comment model) {
                linearLayoutButton=(LinearLayout) findViewById(R.id.buttonPanelForUser);

                if (mUser.getUid().equals(model.getUserId())){
                    holder.linearLayoutButton.setVisibility(LinearLayout.VISIBLE);
                }
                else {
                    holder.linearLayoutButton.setVisibility(LinearLayout.INVISIBLE);
                }
                String dateStr = model.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
                Date date = null;
                try {
                    date = dateFormat.parse(dateStr);
                    String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
                    holder.setComments(getApplicationContext(),model.getName(),model.getComment(),niceDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        mRecyclerView.setLayoutManager(mLinerLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private  void  showAllData()
    {

        options=new FirebaseRecyclerOptions.Builder<Comment>().setQuery(allRef,Comment.class).build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Comment, viewHolder>(options) {

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row,parent,false);

                viewHolder vHolder=new viewHolder(itemView);
                linearLayoutButton=(LinearLayout)findViewById(R.id.buttonPanelForUser);
                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                    @Override
                    public void onDeleteClick(View view, int position) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(Company_Description.this);
                        builder.setMessage("Do you want to delete this comment?");
                        builder.setTitle("Delete");
                        builder.setIcon(R.drawable.ic_delete_black_24dp);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String del=firebaseRecyclerAdapter.getRef(position).getKey();
                                allRef.child(del).removeValue();
                                if(genRef.child(del)!=null)
                                    genRef.child(del).removeValue();
                                else
                                    qaRef.child(del).removeValue();
                                notifyItemRemoved(position);
                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                                Toast.makeText(Company_Description.this,"Comment successfully deleted",Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                            }
                        });
                        alertDialog.show();

                    }

                    @Override
                    public void onEditClick(View view, int position) {
                        String edit=firebaseRecyclerAdapter.getRef(position).getKey();
                        AlertDialog.Builder builder=new AlertDialog.Builder(Company_Description.this);

                        builder.setMessage("Do you want to edit this comment?");
                        builder.setTitle("Alert !");
                        builder.setIcon(R.drawable.ic_edit_black_24dp);
                        builder.setCancelable(false);
                        final EditText input=new EditText(Company_Description.this);
                        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        input.setLayoutParams(lp);
                        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newComment=input.getText().toString();
                                allRef.child(edit).child("Comment").setValue(newComment);
                                if(genRef.child(edit)!=null)
                                    genRef.child(edit).child("Comment").setValue(newComment);
                                else
                                    qaRef.child(edit).child("Comment").setValue(newComment);
                                notifyDataSetChanged();
                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                                Toast.makeText(Company_Description.this,"Comment edited.",Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        DatabaseReference t=allRef.child(edit);
                        t.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String oldComment=dataSnapshot.child("Comment").getValue(String.class);
                                input.setText(oldComment);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        AlertDialog alertDialog=builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor((R.color.colorPrimaryDark)));
                            }
                        });
                        alertDialog.setView(input);
                        alertDialog.show();
                    }
                });
                return vHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Comment model) {
                linearLayoutButton=(LinearLayout) findViewById(R.id.buttonPanelForUser);

                if (mUser.getUid().equals(model.getUserId())){
                    holder.linearLayoutButton.setVisibility(LinearLayout.VISIBLE);
                }
                else {
                    holder.linearLayoutButton.setVisibility(LinearLayout.INVISIBLE);
                }
                String dateStr = model.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
                try {
                    Date date = dateFormat.parse(dateStr);
                    String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
                    holder.setComments(getApplicationContext(),model.getName(),model.getComment(),niceDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        mRecyclerView.setLayoutManager(mLinerLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public void checkConnection()
    {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(null==activeNetwork)
        {
            startActivity(new Intent(Company_Description.this,noInternet.class));
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()==R.id.showSpinner)
        {
            String valueFromSpinner=adapterView.getItemAtPosition(i).toString();
            if(valueFromSpinner.equals("All"))
                showAllData();
            else if(valueFromSpinner.equals("General"))
                showGeneralData();
            else
                showQaData();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
