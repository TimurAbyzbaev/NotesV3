package ru.abyzbaev.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.net.LinkAddress;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.notes_container, new NotesFragment())
                    .commit();
        }
        //androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        initDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        LinearLayout profile = findViewById(R.id.profile_edit);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Настройки профиля!", Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initDrawer(){
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);




        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id){
                    case R.id.action_drawer_about:
                        drawer.close();
                        openAboutFragment();
                        return true;
                    case R.id.action_drawer_exit:
                        Toast.makeText(getApplicationContext(), "Note app closed", Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void openAboutFragment() {
       if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .add(R.id.note_container, new AboutFragment())
                    .commit();
       }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .add(R.id.notes_container, new AboutFragment())
                    .commit();
        }

    }


}