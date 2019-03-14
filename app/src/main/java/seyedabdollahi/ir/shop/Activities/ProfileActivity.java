package seyedabdollahi.ir.shop.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import seyedabdollahi.ir.shop.EventBus.EditUserSuccessful;
import seyedabdollahi.ir.shop.EventBus.LoginSuccessful;
import seyedabdollahi.ir.shop.EventBus.UserLogout;
import seyedabdollahi.ir.shop.Fragments.EditUserFragment;
import seyedabdollahi.ir.shop.Fragments.ProfileFragment;
import seyedabdollahi.ir.shop.Fragments.WelcomeFragment;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class ProfileActivity extends AppCompatActivity {

    FrameLayout fullFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user = MyPreferenceManager.getInstance(ProfileActivity.this).getUser();
        if (user.getId() == null){
            openWelcomeFragment();
        }else {
            openProfileFragment(user);
        }
    }

    private void findViews(){
        fullFrame = findViewById(R.id.profile_full_frame);
    }

    private void configViews(){
        //ViewCompat.setElevation(fullFrame , 5);
    }

    private void openProfileFragment(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user" , user);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_full_frame, profileFragment)
                .commit();
    }

    private void openWelcomeFragment(){
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_full_frame , welcomeFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_cart:
                startActivity(new Intent(this , CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccessful(LoginSuccessful event){
        MyPreferenceManager.getInstance(ProfileActivity.this).putUser(event.getUser());
        getSupportFragmentManager().popBackStack();
        openProfileFragment(event.getUser());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userLogout(UserLogout event){
        getSupportFragmentManager().popBackStack();
        Toast.makeText(ProfileActivity.this , "شما از حساب کاربری خارج شدید" , Toast.LENGTH_LONG).show();
        openWelcomeFragment();
    }

    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
