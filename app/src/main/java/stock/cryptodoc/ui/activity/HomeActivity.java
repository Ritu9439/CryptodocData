package stock.cryptodoc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import stock.cryptodoc.R;
import stock.cryptodoc.SessionData.SessionManagement;
import stock.cryptodoc.model.IndianMarket;
import stock.cryptodoc.ui.fragment.DashBoardFragment;
import stock.cryptodoc.ui.fragment.ForeignFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String email,photo;
    ArrayList<IndianMarket> arrayList=new ArrayList<>();
    TextView txtname;
    CircleImageView profilepic;
    SessionManagement sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sessionManagement=new SessionManagement(HomeActivity.this);
        arrayList=getIntent().getParcelableArrayListExtra("queries");
        Log.d("aaaaa",""+arrayList);
        Bundle args=new Bundle();
        DashBoardFragment dashboardFrgment=new DashBoardFragment();
        args.putParcelableArrayList("queries",arrayList);
        dashboardFrgment.setArguments(args);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,dashboardFrgment);
        fragmentTransaction.commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        txtname=headerLayout.findViewById(R.id.txtname);

        profilepic=headerLayout.findViewById(R.id.profilepic);
        if (sessionManagement.isLoggedIn()){
            HashMap<String,String> data=sessionManagement.getUserDetails();
            email=data.get(SessionManagement.KEY_EMAIL);
            photo=data.get(SessionManagement.KEY_PHOTOURI);
            Toast.makeText(this, ""+photo, Toast.LENGTH_SHORT).show();
            txtname.setText(email);
            Glide.with(HomeActivity.this).load(photo).into(profilepic);

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      /*  getMenuInflater().inflate(R.menu.home, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_indian) {
            // Handle the camera action
            DashBoardFragment dashboardFrgment=new DashBoardFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,dashboardFrgment);
            fragmentTransaction.addToBackStack("Dashboard");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_foreign) {

            ForeignFragment foreignFragment=new ForeignFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,foreignFragment);
            fragmentTransaction.addToBackStack("Foreign");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_news) {

            Intent intent=new Intent(HomeActivity.this,NewsActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
