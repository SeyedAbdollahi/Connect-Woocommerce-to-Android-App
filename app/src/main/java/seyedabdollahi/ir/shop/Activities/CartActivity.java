package seyedabdollahi.ir.shop.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import seyedabdollahi.ir.shop.Fragments.CartFragment;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class CartActivity extends AppCompatActivity {

    private Button openProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        User user = MyPreferenceManager.getInstance(CartActivity.this).getUser();
        if (user.getId() == null){
            findViews();
            configViews();
        }else {
            openCartFragment();
        }
    }

    private void findViews(){
        openProfileBtn = findViewById(R.id.cart_activity_btn);
    }

    private void configViews(){
        openProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this , ProfileActivity.class));
            }
        });
    }

    private void openCartFragment(){
        CartFragment cartFragment = new CartFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cart_activity_full_frame , cartFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_profile:
                startActivity(new Intent(this , ProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
