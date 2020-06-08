package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.squareup.picasso.Picasso;

public class aboutUs extends AppCompatActivity {
    TextView know;
    TextView about_Aprajita,about_Ashish,about_Ashima,about_Atul,about_Mentor;
    CircleImageView imageaprajita,imageashish,imageashima,imageatul,imagementor;
    Button editaprajita,editashish,editashima,editatul,editmentor;
    ShowMoreTextView netaji;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        know=findViewById(R.id.knowMore);
        netaji=findViewById(R.id.netajiDesc);
        netaji.setShowingLine(2);
        netaji.addShowLessText("less");
        netaji.addShowMoreText("see more");
        netaji.setShowLessTextColor(Color.BLACK);
        netaji.setShowMoreColor(Color.BLACK);
        editaprajita=findViewById(R.id.editAprajita);
        editashish=findViewById(R.id.editAshish);
        editashima=findViewById(R.id.editAshima);
        editatul=findViewById(R.id.editAtul);
        editmentor=findViewById(R.id.editMentor);
        imageaprajita=findViewById(R.id.aprajita);
        about_Aprajita=findViewById(R.id.aboutAprajita);
        about_Ashish=findViewById(R.id.aboutAshish);
        /*about_Ashish.setShowingLine(2);
        about_Ashish.addShowLessText("less");
        about_Ashish.addShowMoreText("see more");
        about_Ashish.setShowLessTextColor(Color.BLACK);
        about_Ashish.setShowMoreColor(Color.BLACK);*/
        about_Ashima=findViewById(R.id.aboutAshima);
        /*about_Ashima.setShowingLine(2);
        about_Ashima.addShowLessText("less");
        about_Ashima.addShowMoreText("see more");
        about_Ashima.setShowLessTextColor(Color.BLACK);
        about_Ashima.setShowMoreColor(Color.BLACK);*/
        about_Atul=findViewById(R.id.aboutAtul);
        /*about_Atul.setShowingLine(2);
        about_Atul.addShowLessText("less");
        about_Atul.addShowMoreText("see more");
        about_Atul.setShowLessTextColor(Color.BLACK);
        about_Atul.setShowMoreColor(Color.BLACK);*/
        about_Mentor=findViewById(R.id.aboutMentor);
        /*about_Mentor.setShowingLine(2);
        about_Mentor.addShowLessText("less");
        about_Mentor.addShowMoreText("see more");
        about_Mentor.setShowLessTextColor(Color.BLACK);
        about_Mentor.setShowMoreColor(Color.BLACK);*/
        Picasso.get().load(R.drawable.aprajita).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(imageaprajita);
        imageashish=findViewById(R.id.ashish);
        Picasso.get().load(R.drawable.ashish).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(imageashish);
        imageashima=findViewById(R.id.ashima);
        Picasso.get().load(R.drawable.ashima).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(imageashima);
        imageatul=findViewById(R.id.atul);
        Picasso.get().load(R.drawable.ic_profileimage).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(imageatul);
        imagementor=findViewById(R.id.mentor);
        Picasso.get().load(R.drawable.sumanta).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(imagementor);
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("https://www.nsec.ac.in/");
                Intent in=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(in);
            }
        });
        DatabaseReference aboutDevelopers=FirebaseDatabase.getInstance().getReference().child("Developers");
        DatabaseReference Database = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        aboutDevelopers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ap=dataSnapshot.child("aprajitashailie88").child("about").getValue(String.class);
                about_Aprajita.setText(ap);
                /*about_Aprajita.setShowingLine(2);
                about_Aprajita.addShowLessText("less");
                about_Aprajita.addShowMoreText("see more");
                about_Aprajita.setShowLessTextColor(Color.BLACK);
                about_Aprajita.setShowMoreColor(Color.BLACK);*/
                String as=dataSnapshot.child("ashsihs41").child("about").getValue(String.class);
                about_Ashish.setText(as);
                String ash=dataSnapshot.child("ashimasingh24ashi").child("about").getValue(String.class);
                about_Ashima.setText(ash);
                String at=dataSnapshot.child("atultiwari435").child("about").getValue(String.class);
                about_Atul.setText(at);
                String me=dataSnapshot.child("sumanta_wbut").child("about").getValue(String.class);
                about_Mentor.setText(me);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email=dataSnapshot.child("Email").getValue(String.class);
                if(email.equals("aprajitashailie88@gmail.com"))
                {
                    editaprajita.setVisibility(View.VISIBLE);
                }
                else if(email.equals("ashsihs41@gmail.com"))
                {
                    editashish.setVisibility(View.VISIBLE);
                }
                else if(email.equals("ashimasingh24ashi@gmail.com"))
                {
                    editashima.setVisibility(View.VISIBLE);
                }
                else if(email.equals("atultiwari435@gmail.com"))
                {
                    editatul.setVisibility(View.VISIBLE);
                }
                else if(email.equals("sumanta.wbut@gmail.com"))
                {
                    editaprajita.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        editaprajita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(aboutUs.this);

                builder.setMessage("Write Something about You.");
                builder.setTitle("Message from you");
                builder.setIcon(R.drawable.ic_edit_black_24dp);
                builder.setCancelable(false);
                final EditText input=new EditText(aboutUs.this);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                input.setLayoutParams(lp);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String about=input.getText().toString();
                        aboutDevelopers.child("aprajitashailie88").child("about").setValue(about);
                       about_Aprajita.setText(about);
                       /* about_Aprajita.setShowingChar(50);
                        about_Aprajita.addShowLessText("less");
                        about_Aprajita.addShowMoreText("see more");
                        about_Aprajita.setShowLessTextColor(Color.BLACK);
                        about_Aprajita.setShowMoreColor(Color.BLACK);*/
                        Toast.makeText(aboutUs.this,"Added successfully.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                aboutDevelopers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldAbout=dataSnapshot.child("aprajitashailie88").child("about").getValue(String.class);
                        input.setText(oldAbout);
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

        editashish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(aboutUs.this);

                builder.setMessage("Write Something about You.");
                builder.setTitle("Message from you");
                builder.setIcon(R.drawable.ic_edit_black_24dp);
                builder.setCancelable(false);
                final EditText input=new EditText(aboutUs.this);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                input.setLayoutParams(lp);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String about=input.getText().toString();
                        aboutDevelopers.child("ashsihs41").child("about").setValue(about);
                        Toast.makeText(aboutUs.this,"Added successfully.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                aboutDevelopers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldAbout=dataSnapshot.child("ashsihs41").child("about").getValue(String.class);
                        input.setText(oldAbout);
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
        editashima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(aboutUs.this);

                builder.setMessage("Write Something about You.");
                builder.setTitle("Message from you");
                builder.setIcon(R.drawable.ic_edit_black_24dp);
                builder.setCancelable(false);
                final EditText input=new EditText(aboutUs.this);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                input.setLayoutParams(lp);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String about=input.getText().toString();
                        aboutDevelopers.child("ashimasingh24ashi").child("about").setValue(about);
                        Toast.makeText(aboutUs.this,"Added successfully.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                aboutDevelopers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldAbout=dataSnapshot.child("ashimasingh24ashi").child("about").getValue(String.class);
                        input.setText(oldAbout);
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
        editatul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(aboutUs.this);

                builder.setMessage("Write Something about You.");
                builder.setTitle("Message from you");
                builder.setIcon(R.drawable.ic_edit_black_24dp);
                builder.setCancelable(false);
                final EditText input=new EditText(aboutUs.this);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                input.setLayoutParams(lp);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String about=input.getText().toString();
                        aboutDevelopers.child("atultiwari435").child("about").setValue(about);
                        Toast.makeText(aboutUs.this,"Added successfully.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                aboutDevelopers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldAbout=dataSnapshot.child("atultiwari435").child("about").getValue(String.class);
                        input.setText(oldAbout);
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
        editmentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(aboutUs.this);

                builder.setMessage("Write Something about You.");
                builder.setTitle("Message from you");
                builder.setIcon(R.drawable.ic_edit_black_24dp);
                builder.setCancelable(false);
                final EditText input=new EditText(aboutUs.this);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                input.setLayoutParams(lp);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String about=input.getText().toString();
                        aboutDevelopers.child("sumanta_wbut").child("about").setValue(about);
                        Toast.makeText(aboutUs.this,"Added successfully.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                aboutDevelopers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldAbout=dataSnapshot.child("sumanta_wbut").child("about").getValue(String.class);
                        input.setText(oldAbout);
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
    }
}
