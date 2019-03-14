package seyedabdollahi.ir.shop.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import org.greenrobot.eventbus.EventBus;

import seyedabdollahi.ir.shop.Data.CreateCustomerController;
import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.EventBus.LoginSuccessful;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.R;

public class RegisterFragment extends Fragment {

    private CoordinatorLayout registerLayout;
    private EditText username;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private Button createBtn;
    private TextView error;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        configViews();
    }

    private void findViews(View view){
        username = view.findViewById(R.id.register_username);
        email = view.findViewById(R.id.register_email);
        firstName = view.findViewById(R.id.register_first_name);
        lastName = view.findViewById(R.id.register_last_name);
        password = view.findViewById(R.id.register_password);
        createBtn = view.findViewById(R.id.btn_register);
        registerLayout = view.findViewById(R.id.register_layout);
        error = view.findViewById(R.id.register_error);
        progressBar = view.findViewById(R.id.register_progress_bar);
    }

    private void configViews(){
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")){
                    error.setText("نام کاربری را وارد کنید");
                }else if(email.getText().toString().equals("")){
                    error.setText("ایمیل را وارد کنید");
                }else if(firstName.getText().toString().equals("")){
                    error.setText("نام خود را وارد کنید");
                }else if(lastName.getText().toString().equals("")){
                    error.setText("نام خانوادگی خود را وارد کنید");
                }else if (password.getText().toString().equals("")){
                    error.setText("رمز را وارد کنید");
                }else {
                    createCustomer();
                }
            }
        });
    }

    private void createCustomer(){
        progressBar.setVisibility(View.VISIBLE);
        User user = new User();
        user.setUsername(username.getText().toString());
        user.setEmail(email.getText().toString());
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setPassword(password.getText().toString());

        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        AuthenticationWoocommerce authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.POST , WoocommerceUrl.CUSTOMER);
        CreateCustomerController createCustomerController = new CreateCustomerController(new WoocommerceAPI.createCustomerCallback() {
            @Override
            public void onResponse(boolean successful, User user) {
                progressBar.setVisibility(View.INVISIBLE);
                if(successful){
                    EventBus.getDefault().post(new LoginSuccessful(user));
                }else {
                    showSnackBar(false , "خطا در ارتباط با سرور");
                }
            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.INVISIBLE);
                showSnackBar(false , "خطا در ارتباط با سرور");
            }
        });
        createCustomerController.start(authenticationWoocommerce , user);
    }

    private void showSnackBar(boolean successful , String text){
        TSnackbar snackbar = TSnackbar.make(registerLayout, text , TSnackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.WHITE);
        if(successful){
            view.setBackgroundColor(getResources().getColor(R.color.dark_blue));
        }else {
            snackbar.setDuration(TSnackbar.LENGTH_INDEFINITE);
            view.setBackgroundColor(getResources().getColor(R.color.red));
            snackbar.setAction("try Again", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createCustomer();
                }
            });
        }
        snackbar.show();
    }
}
