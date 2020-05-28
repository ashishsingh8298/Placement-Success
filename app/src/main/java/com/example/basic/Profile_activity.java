package com.example.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class Profile_activity extends AppCompatActivity {
    private static final int PICK_IMAGE=1;
    private FirebaseAuth mAuth;
    CircleImageView profileImage ;
    FirebaseUser mUser;
    TextView fullName,phoneNumber,stream,roll,dob,year,email;
    private StorageReference Folder;
    DatabaseReference uRef;
    Uri imageUri;
    String photo;
    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        Folder= FirebaseStorage.getInstance().getReference().child("Users").child(mUser.getUid());
        uRef=FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        mDialog = new ProgressDialog(this);

        profileImage = findViewById(R.id.imageview_profile);
        fullName = findViewById(R.id.NameOfUser);
        email=findViewById(R.id.email);
        phoneNumber=findViewById(R.id.phone);
        stream=findViewById(R.id.stream);
        year=findViewById(R.id.year);
        roll=findViewById(R.id.roll);
        dob=findViewById(R.id.dob);
        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String u_name=dataSnapshot.child("Name").getValue(String.class);
                String phn_no=dataSnapshot.child("Phone Number").getValue(String.class);
                String u_email=dataSnapshot.child("Email").getValue(String.class);
                String u_dob=dataSnapshot.child("dob").getValue(String.class);
                photo=dataSnapshot.child("profilePhoto").child("imageurl").getValue(String.class);
                //Uri uri=Uri.parse(photo);
                fullName.setText(u_name);
                if(phn_no==null)
                    phoneNumber.setText("Phone Number");
                else
                    phoneNumber.setText(phn_no);
                email.setText(u_email);
                if(u_dob==null)
                    dob.setText("14-09-1998");
                else
                    dob.setText(u_dob);
                if(photo!=null) {
                    //Picasso.get().load(photo).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(profileImage);
                    Glide.with(Profile_activity.this).load(photo).into(profileImage);
                }
                else
                {
                    //Glide.with(Profile_activity.this).load(mUser.getPhotoUrl()).into(profileImage);
                    Picasso.get().load(mUser.getPhotoUrl()).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Glide.with(this).load(mUser.getPhotoUrl()).into(profileImage);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_activity.this, editProfile.class));
            }
        });



      profileImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent gallery=new Intent();
              gallery.setType("image/*");
              gallery.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);

          }
      });

    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
       mDialog.setMessage("Processing...");
       mDialog.show();
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null)
        {

            imageUri=data.getData();
            try
                {
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    profileImage.setImageBitmap((bitmap));

                }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            StorageReference Imagename=Folder.child("image"+imageUri.getLastPathSegment());
            Imagename.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference imagestore=uRef.child("profilePhoto");
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("imageurl",String.valueOf(uri));

                                imagestore.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Profile_activity.this,"Profile Photo Updated",Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                    }
                                });

                            }
                        });
                }

            });

            uRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    photo=dataSnapshot.child("profilePhoto").child("imageurl").getValue(String.class);
                    Picasso.get().load(mUser.getPhotoUrl()).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(profileImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            mDialog.dismiss();
            Toast.makeText(Profile_activity.this,"No Photo Selected",Toast.LENGTH_LONG).show();
        }
    }


}
