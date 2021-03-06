package com.example.bigchirfufa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainMenuRecyclerAdapter.ItemClickListener, RecyclerBuyAdapter.ItemClickListener, View.OnClickListener{
    public View active_view = null;
    public View incative_view = null;
    MainMenuRecyclerAdapter adapter;
    MenuRecyclerAdapter menu_recycler_adapter;
    RecyclerBuyAdapter  recycler_buy_adapter;
    private Button button66;
    private TextView textView;
    private ImageView korzina;



    private static MainActivity context;
    //current adapter
    // 0 - main menu
    // 1 - menu dishes
    // 2 - recycler dishes
    Integer current_adapter;

    RecyclerView menu_recycler_view;
    RecyclerView buy_recycler_view;

    ImageFactory factory;

    User user;

    public static MainActivity getAppContext()
    {
        return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        button66 = (Button) findViewById(R.id.button66);



        Typeface mFont = Typeface.createFromAsset(getAssets(), "fonts/calibril.ttf");
        ViewGroup root = (ViewGroup)findViewById(R.id.drawer_layout);
        setFont(root, mFont);



        ArrayList<com.example.bigchirfufa.MenuItem> animalNames = new ArrayList<com.example.bigchirfufa.MenuItem>();
        animalNames.add(new com.example.bigchirfufa.MenuItem("Мясо", R.drawable.meat));
        animalNames.add(new com.example.bigchirfufa.MenuItem("Супы", R.drawable.soap));
        animalNames.add(new com.example.bigchirfufa.MenuItem("Салаты", R.drawable.salts));
        animalNames.add(new com.example.bigchirfufa.MenuItem("Горячие блюда", R.drawable.seconddish));
        animalNames.add(new com.example.bigchirfufa.MenuItem("Выпечка", R.drawable.vypechka));
        animalNames.add(new com.example.bigchirfufa.MenuItem("Сладости", R.drawable.desert));
        animalNames.add(new com.example.bigchirfufa.MenuItem("Напитки", R.drawable.drinks));

        user = new User();
        user.first_name = findViewById(R.id.first_name_id);
        user.last_name = findViewById(R.id.last_name_id);
        user.phone_number = findViewById(R.id.phone_number_id);
        user.adress = findViewById(R.id.postal_address_id);
        user.dom = findViewById(R.id.postal_dom_id);
        user.kvartira = findViewById(R.id.postal_kv_id);
        user.padik = findViewById(R.id.postal_padik_id);
        user.ettage = findViewById(R.id.postal_ettage_id);


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        user.first_name.setText(settings.getString("first_name", "").toString());
        user.last_name.setText(settings.getString("last_name", "").toString());
        user.phone_number.setText(settings.getString("phone_number", "").toString());
        user.adress.setText(settings.getString("address", "").toString());
        user.dom.setText(settings.getString("dom", "").toString());
        user.kvartira.setText(settings.getString("kvartira", "").toString());
        user.padik.setText(settings.getString("padik", "").toString());
        user.ettage.setText(settings.getString("ettage", "").toString());




        menu_recycler_view = findViewById(R.id.menuRcV);
        menu_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainMenuRecyclerAdapter(this, animalNames);
        adapter.setClickListener(this);
        menu_recycler_view.setAdapter(adapter);
        current_adapter = 0;

        factory =  new ImageFactory(findViewById(R.id.drawer_layout), this);



        buy_recycler_view = findViewById(R.id.recycler_buy_id);
        buy_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_buy_adapter = new RecyclerBuyAdapter(findViewById(R.id.drawer_layout), factory);
        recycler_buy_adapter.setClickListener(this);

        menu_recycler_adapter = new MenuRecyclerAdapter(recycler_buy_adapter, factory, this);

        findViewById(R.id.button_buy).setOnClickListener(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            button66.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(R.id.menu);
            }
        });

            korzina = (ImageView) findViewById(R.id.korzina);
            korzina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeView(R.id.recycler_buy);

                }
            });

        findViewById(R.id.profile).setOnClickListener(this);
        changeView(R.id.menu);
           }
    /*
* Sets the font on all TextViews in the ViewGroup. Searches
recursively for all inner ViewGroups as well. Just add a
check for any other views you want to set as well (EditText,
etc.)
*/
    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView || v instanceof Button /*etc.*/)
                ((TextView)v).setTypeface(font);
            else if(v instanceof ViewGroup)
                setFont((ViewGroup)v, font);
        }
    }

    public void onDishClick(View view, Dish dish)
    {
        ImageView img_view = findViewById(R.id.image_dish_layout);
        TextView txt_view = findViewById(R.id.text_dish_layout);
        TextView titledish = findViewById(R.id.titledish);

        factory.set_image(img_view, dish.image + "big");
        txt_view.setText(dish.text);
        titledish.setText(dish.title);
        changeView(R.id.dish_layout);
    }

    public void stopAnimation(Boolean stop)
    {
        View view = findViewById(R.id.wait_anim_layout);
        if (view == null)
        {
            throw new NullPointerException("Animation view is null!");
        }
        if (stop)
            view.setVisibility(View.GONE);
        else
            view.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.button_buy)
        {
            if (user.isEmpty())
            {
                changeView(R.id.profile);
                Toast.makeText(this, "Пожалуйста, заполните личные данные", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences settings = getSharedPreferences("UserInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("first_name", user.first_name.getText().toString());
            editor.putString("last_name",user.last_name.getText().toString());
            editor.putString("phone_number",user.phone_number.getText().toString());
            editor.putString("address",user.adress.getText().toString());
            editor.putString("dom",user.dom.getText().toString());
            editor.putString("kvartira",user.kvartira.getText().toString());
            editor.putString("padik",user.padik.getText().toString());
            editor.putString("ettage",user.ettage.getText().toString());

            editor.commit();

            Toast.makeText(this, "Вы сделали покупку, заказ отправляется", Toast.LENGTH_SHORT).show();
            String body = "";
            ArrayList<Pair<Dish, Integer>> data = recycler_buy_adapter.mData;
            for (Pair<Dish, Integer> dish: data) {
                body += dish.first.title + " : в количестве " + dish.second.toString() + " шт " + '\n';
            }
            body += " Имя: " + user.first_name.getText().toString() + '\n' + " Фамилия: " + user.last_name.getText().toString() + '\n';
            body += " Номер телефона: " + user.phone_number.getText().toString() + '\n' + " Улица: " + user.adress.getText().toString() + '\n';
            body += " Номер дома: " + user.dom.getText().toString() + '\n' + " Квартира: " + user.kvartira.getText().toString() + '\n';
            body += " Подъезд: " + user.padik.getText().toString() + '\n' + " Этаж: " + user.ettage.getText().toString() + '\n';
            new MailSenderAsynс().execute(body);
            recycler_buy_adapter.update_dataset(new ArrayList<Dish>());
            menu_recycler_adapter.mDataBuy.clear();
            changeView(R.id.recycler_is_empty);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else {
            //super.onBackPressed();
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

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemRecyclerClick(View view, int pos)
    {
        //
    }

    public void changeView(Integer view_id)
    {
        if (active_view == null)
        {
            active_view = findViewById(R.id.menu);
        }

        active_view.setVisibility(View.GONE);
        incative_view = active_view;
        active_view = findViewById(view_id);
        active_view.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        stopAnimation(true);
        if (id == R.id.nav_mail) {
/////////////////////////////////////////////////////////////////////
        } else if (id == R.id.nav_profile)
        {
            changeView(R.id.profile);

        } else if (id == R.id.nav_menufood)
        {
            menu_recycler_view.setAdapter(adapter);
            changeView(R.id.menu);

        } else if (id == R.id.nav_geolocation)
        {
            //о ресторане;
        } else if (id == R.id.nav_car)
        {
            changeView(R.id.car);

        } else if (id == R.id.nav_app)
        {
/////////////////////////////////////////////////////////////////////
        } else if (id == R.id.nav_recycler_buy)
        {
            buy_recycler_view.getRecycledViewPool().clear();
            if ((menu_recycler_adapter.mDataBuy == null) || (menu_recycler_adapter.mDataBuy.size() == 0))
            {
                changeView(R.id.recycler_is_empty);
            }
            else {
                recycler_buy_adapter.update_dataset(menu_recycler_adapter.mDataBuy);
                buy_recycler_view.setAdapter(recycler_buy_adapter);
                current_adapter = 2;
                changeView(R.id.recycler_buy);
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            openQuitDialog();
        }
        return true;
    }

    private void openQuitDialog() {
        onBackPressed();
        if (active_view.getId() == R.id.recycler_is_empty)
        {
            changeView(R.id.menu);
            return;
        }
        if (active_view.getId() == R.id.menu)
        {
            if (current_adapter == 1)
            {
                menu_recycler_view.setAdapter(adapter);
                current_adapter = 0;
                changeView(R.id.menu);
                stopAnimation(true);
                return;
            }
            if (current_adapter == 0)
                finish();
            stopAnimation(true);
        }

        changeView(incative_view.getId());

    }

    @Override
    public void onItemClick(View view, int position) {
        if (menu_recycler_adapter.mDataset != null && !menu_recycler_adapter.mDataset.isEmpty())
        {
            stopAnimation(true);
        }
        else
        {
            stopAnimation(false);
        }
        menu_recycler_view.getRecycledViewPool().clear();
        menu_recycler_adapter.update_menu(position);
        menu_recycler_view.setAdapter(menu_recycler_adapter);
        current_adapter = 1;
        changeView(R.id.menu);
        Typeface mFont = Typeface.createFromAsset(MainActivity.getAppContext().getAssets(), "fonts/calibril.ttf");
        ViewGroup root = (ViewGroup) MainActivity.getAppContext().findViewById(R.id.menu);
        MainActivity.getAppContext().setFont(root, mFont);

    }
}

class User
{
    TextView first_name;
    TextView last_name;
    TextView phone_number;
    TextView adress;
    TextView dom;
    TextView kvartira;
    TextView padik;
    TextView ettage;

    public boolean isEmpty()
    {
        if (first_name.getText().toString().isEmpty())
        {
            return true;
        }
        if (last_name.getText().toString().isEmpty())
        {
            return true;
        }
        if (phone_number.getText().toString().isEmpty())
        {
            return true;
        }
        if (dom.getText().toString().isEmpty())
        {
            return true;
        }
        if (kvartira.getText().toString().isEmpty())
        {
            return true;
        }
        if (padik.getText().toString().isEmpty())
        {
            return true;
        }
        if (ettage.getText().toString().isEmpty())
        {
            return true;
        }
        return adress.getText().toString().isEmpty();
    }
}

