package com.example.basic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Nav_Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ActionBarDrawerToggle mToggle;
    LinearLayoutManager mLinerLayoutManager;
    private RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseRecyclerAdapter<Company,viewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Company> options;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_);

        mAuth = FirebaseAuth.getInstance();
        mLinerLayoutManager=new LinearLayoutManager(this);
        mLinerLayoutManager.setReverseLayout(true);
        mLinerLayoutManager.setStackFromEnd(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Company");
        mRecyclerView= findViewById(R.id.list);
        //mCompanylist.setLayoutManager(new LinearLayoutManager(this));
        //mCompanylist.setHasFixedSize(true);
        showData();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                if (mUser == null) {
                    Intent i = new Intent(Nav_Activity.this, Login_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //startActivity(new Intent(Nav_Activity.this, Dashboard.class));
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
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Nav_Activity.this, Login_Activity.class));
                }
                else if (id == R.id.nav_home) {
                    startActivity(new Intent(Nav_Activity.this, Nav_Activity.class));
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
                holder.setDetails(getApplicationContext(),model.getJobTitle(),model.getJobDescription());
            }

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.company_row,parent,false);
                viewHolder vHolder=new viewHolder(itemView);
                vHolder.setOnClickListener(new viewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Nav_Activity.this,"hello",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Nav_Activity.this,Company_Description.class));
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(Nav_Activity.this,"long click",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Nav_Activity.this,Company_Description.class));
                    }
                });
                return vHolder;
            }
        };
        mRecyclerView.setLayoutManager(mLinerLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
