package ru.abyzbaev.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    NotesFragment notesFragment = new NotesFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar(isLandscape());

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.notes_container, notesFragment)
                    .commit();
        }
    }
    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initToolbar(boolean isLandscape){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!isLandscape)
            initDrawer(toolbar);
        //initDrawer(toolbar);
    }

    private void initDrawer(Toolbar toolbar){
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    private void openAboutFragment() {
       //if(isLandscape()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .add(R.id.notes_container, new AboutFragment())
                    .commit();
       //}
        /*else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .add(R.id.notes_container, new AboutFragment())
                    .commit();
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                notesFragment.addNote();

                break;

        }

        return super.onOptionsItemSelected(item);
    }
}