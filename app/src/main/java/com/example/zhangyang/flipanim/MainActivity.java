package com.example.zhangyang.flipanim;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CustomizedCircleView circle1;
    private CustomizedCircleView circle2;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        circle1=(CustomizedCircleView)findViewById(R.id.circle1);
        circle2=(CustomizedCircleView)findViewById(R.id.circle2);
        btn=(Button)findViewById(R.id.btn);
        initControl();
    }

    private void initControl() {
        final long[] a=new long[3];
        a[0]=10000;
        a[1]=500;
        a[2]=1200;
        final long[] b=new long[3];
        b[0]=100;
        b[1]=100;
        b[2]=100;
        CustomizedCircleView.setSweep(true,circle1, -90, new int[]{getResources().getColor(R.color.assets_cicrle_finance_color),
                getResources().getColor(R.color.assets_cicrle_yiqianbao_color), getResources().getColor(R.color.color_charview_gradient_begin)}, a, 8);
        CustomizedCircleView.setSweep(true,circle2, -90, new int[]{getResources().getColor(R.color.assets_cicrle_finance_color),
                                    getResources().getColor(R.color.assets_cicrle_yiqianbao_color), getResources().getColor(R.color.color_charview_gradient_begin)}, b, 8);
        final FlipAnimation animation=new FlipAnimation(circle1,circle2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animation.flip();
                    }
                }, 100);
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(circle1.getVisibility()== View.VISIBLE){
                    CustomizedCircleView.setSweep(true,circle1, -90, new int[]{getResources().getColor(R.color.assets_cicrle_finance_color),
                            getResources().getColor(R.color.assets_cicrle_yiqianbao_color), getResources().getColor(R.color.color_charview_gradient_begin)}, a, 8);
                }else{
                    CustomizedCircleView.setSweep(true,circle2, -90, new int[]{getResources().getColor(R.color.assets_cicrle_finance_color),
                            getResources().getColor(R.color.assets_cicrle_yiqianbao_color), getResources().getColor(R.color.color_charview_gradient_begin)}, b, 8);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
