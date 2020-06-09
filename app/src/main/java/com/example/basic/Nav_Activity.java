package com.example.basic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Nav_Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference uRef;
    private ActionBarDrawerToggle mToggle;
    LinearLayoutManager mLinerLayoutManager;
    private RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseRecyclerAdapter<Company,viewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Company> options;
    private DatabaseReference mDatabase;
    ImageView u_image;
    FirebaseUser m_user;
    TextView u_name,u_email;
    String Uname,uid;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout loadingPanel,loadingPanelNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_);
        loadingPanel=findViewById(R.id.loadingPanel);

        checkConnection();
        mAuth = FirebaseAuth.getInstance();
        mLinerLayoutManager=new LinearLayoutManager(this);
        mLinerLayoutManager.setReverseLayout(true);
        mLinerLayoutManager.setStackFromEnd(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Company");
        mRecyclerView= findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);


        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                if(mUser!=null)
                {
                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
                    Database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String firstTime = dataSnapshot.child("firstTimeLogin").getValue(String.class);
                            Boolean isBlock=dataSnapshot.child("block").getValue(Boolean.class);
                            if(firstTime!=null)
                            {
                                if(firstTime.equals("true"))
                                {
                                    startActivity(new Intent(Nav_Activity.this,addPhoneNumber.class));
                                }
                            }
                            else{
                                startActivity(new Intent(Nav_Activity.this,addPhoneNumber.class));
                            }
                            if(isBlock)
                            {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if (mUser== null || mUser.isEmailVerified()==false) {
                    Intent i=new Intent(Nav_Activity.this, Login_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };

        m_user=mAuth.getCurrentUser();
        //uid=m_user.getUid();
        /*uRef=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Uname=dataSnapshot.child("Name").getValue(String.class);
                //Toast.makeText(Nav_Activity.this,Uname,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        showData();



        updateNavHeader();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(Nav_Activity.this, searchActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setTitle(("Feed"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.nav_logout) {
                    checkConnection();
                    AlertDialog.Builder builder=new AlertDialog.Builder(Nav_Activity.this);
                    builder.setMessage("Do you want to LogOut?");
                    builder.setTitle("LogOut");
                    builder.setIcon(R.drawable.ic_settings_power_black_24dp);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(Nav_Activity.this, Login_Activity.class));
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
                else if (id == R.id.nav_home) {
                    checkConnection();
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (id==R.id.nav_profile)
                {
                    checkConnection();
                    startActivity(new Intent(Nav_Activity.this,Profile_activity.class));
                }
                else if(id==R.id.nav_search_job)
                {
                    checkConnection();
                    startActivity(new Intent(Nav_Activity.this,searchActivity.class));
                }
                else if(id==R.id.nav_applied_job)
                {
                    checkConnection();
                    startActivity(new Intent(Nav_Activity.this,appliedJobsActivity.class));
                }
                else if(id==R.id.nav_whatsNew)
                {
                    checkConnection();
                    startActivity(new Intent(Nav_Activity.this,whatsNew.class));
                }
                else if(id==R.id.nav_recommended_job)
                {
                    checkConnection();
                    if(m_user!=null)
                    {
                        m_user=mAuth.getCurrentUser();
                        uRef=FirebaseDatabase.getInstance().getReference().child("Users").child(m_user.getUid());
                        uRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String skill = dataSnapshot.child("skills").getValue(String.class);
                                if (skill != null) {
                                    if (skill.equals("")) {
                                        startActivity(new Intent(Nav_Activity.this, skillNotSelected.class));
                                    } else {
                                        startActivity(new Intent(Nav_Activity.this, recommendedJobs.class));
                                    }
                                }
                                else
                                {
                                    startActivity(new Intent(Nav_Activity.this, skillNotSelected.class));
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
                else if(id==R.id.nav_selectSkills)
                {
                    checkConnection();
                    startActivity(new Intent(Nav_Activity.this,selectSkillsActivity.class));

                }
                else if(id==R.id.nav_setting)
                {
                    checkConnection();
                    startActivity(new Intent(Nav_Activity.this,SettingActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById((R.id.drawer_layout));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void  showData()
    {

        options=new FirebaseRecyclerOptions.Builder<Company>().setQuery(mDatabase,Company.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Company, viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Company model) {
                holder.setDetails(getApplicationContext(),model.getJobTitle(),model.getJobDescription(),model.getLinkLogo());
                loadingPanel.setVisibility(View.INVISIBLE);

            }
            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.company_row,parent,false);
                viewHolder vHolder=new viewHolder(itemView);
                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String temp=firebaseRecyclerAdapter.getRef(position).getKey();
                        Intent intent=new Intent(Nav_Activity.this,Company_Description.class);
                        intent.putExtra("str",temp);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(Nav_Activity.this,"long click",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDeleteClick(View view, int position) {

                    }

                    @Override
                    public void onEditClick(View view, int position) {

                    }
                });
                return vHolder;
            }
        };
        mRecyclerView.setLayoutManager(mLinerLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void updateNavHeader()
    {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);

        u_image=headerView.findViewById(R.id.user_image);
        u_name=headerView.findViewById(R.id.NameOfUser);
        u_email=headerView.findViewById(R.id.user_email);
        if (m_user!=null){
            m_user=mAuth.getCurrentUser();
            uRef=FirebaseDatabase.getInstance().getReference().child("Users").child(m_user.getUid());
            uRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String photo=dataSnapshot.child("profilePhoto").child("imageurl").getValue(String.class);
                    String name=dataSnapshot.child("Name").getValue(String.class);
                    String email=dataSnapshot.child("Email").getValue(String.class);
                    if(photo!=null) {
                        //Picasso.get().load(photo).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(profileImage);
                        Glide.with(Nav_Activity.this).load(photo).into(u_image);
                    }
                    else
                    {
                        Picasso.get().load(m_user.getPhotoUrl()).fit().centerCrop().placeholder(R.drawable.ic_profileimage).error(R.drawable.ic_profileimage).into(u_image);
                    }

                    u_name.setText(name);
                    u_email.setText(email);
//                    loadingPanelNav.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel channel=
                        new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

                NotificationManager manager=getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            FirebaseMessaging.getInstance().subscribeToTopic("general")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Successful";
                            if (!task.isSuccessful()) {
                                msg = "Failed";
                            }
//                            Toast.makeText(Nav_Activity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        loadingPanel.setVisibility(View.VISIBLE);
    }
    public void checkConnection()
    {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(null==activeNetwork)
        {
            startActivity(new Intent(Nav_Activity.this,noInternet.class));
        }
    }

}

