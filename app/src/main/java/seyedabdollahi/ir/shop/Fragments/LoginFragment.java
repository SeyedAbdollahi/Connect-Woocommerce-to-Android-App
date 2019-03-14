package seyedabdollahi.ir.shop.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import org.greenrobot.eventbus.EventBus;

import seyedabdollahi.ir.shop.Data.CheckUserPassController;
import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.GetAllCustomersController;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.EventBus.LoginSuccessful;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.Token;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class LoginFragment extends Fragment {

    private CoordinatorLayout loginLayout;
    private EditText username;
    private EditText password;
    private Button loginBtn;
    private TextView error;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        configViews();
    }

    private void findViews(View view){
        username = view.findViewById(R.id.login_edt_username);
        password = view.findViewById(R.id.login_edt_password);
        loginBtn = view.findViewById(R.id.login_btn_login);
        loginLayout = view.findViewById(R.id.login_layout);
        error = view.findViewById(R.id.login_error);
        progressBar = view.findViewById(R.id.login_progress_bar);
    }

    private void configViews(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")){
                    error.setText("نام کاربری را وارد کنید");
                }else if(password.getText().toString().equals("")){
                    error.setText("رمز را وارد کنید");
                }else {
                    checkUserPass();
                }
            }
        });
    }

    private void checkUserPass(){
        progressBar.setVisibility(View.VISIBLE);
        CheckUserPassController checkUserPassController = new CheckUserPassController(new WoocommerceAPI.checkUserPasswordCallback() {
            @Override
            public void onResponse(boolean successful , Token token) {
                progressBar.setVisibility(View.INVISIBLE);
                if (successful){
                    getAllCustomers(token.getEmail());
                }else {
                    showSnackBar(true , "نام کاربری یا رمز عبور اشتباه است");
                }
            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.INVISIBLE);
                showSnackBar(false , "خطا در دریافت اطلاعات");
            }
        });
        checkUserPassController.start(username.getText().toString() , password.getText().toString());
    }

    private void getAllCustomers(String email){
        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        AuthenticationWoocommerce authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.GET , WoocommerceUrl.CUSTOMER);
        GetAllCustomersController getAllCustomersController = new GetAllCustomersController(new WoocommerceAPI.getAllCustomersCallback() {
            @Override
            public void onResponse(boolean successful, User user) {
                if (successful){
                    if (user.getId() != null){
                        EventBus.getDefault().post(new LoginSuccessful(user));
                    }
                    else {
                        showSnackBar(true , "نام کاربری در فروشگاه ثبت نشده است");
                    }
                }
                else {
                    showSnackBar(false , "خطا در دریافت اطلاعات");
                }
            }

            @Override
            public void onFailure() {
                showSnackBar(false , "خطا در دریافت اطلاعات");
            }
        } , email);
        getAllCustomersController.start(authenticationWoocommerce);
    }

    private void showSnackBar(boolean successful , String text){
        TSnackbar snackbar = TSnackbar.make(loginLayout, text , TSnackbar.LENGTH_LONG);
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
                    checkUserPass();
                }
            });
        }
        snackbar.show();
    }
}
