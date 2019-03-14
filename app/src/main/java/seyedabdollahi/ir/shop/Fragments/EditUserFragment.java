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
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import org.greenrobot.eventbus.EventBus;

import seyedabdollahi.ir.shop.Data.EditCustomerController;
import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.EventBus.EditUserSuccessful;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class EditUserFragment extends Fragment {

    private User user;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private Button editBtn;
    private CoordinatorLayout editLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_user_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        getArgum();
        configViews();
    }

    private void findViews(View view){
        firstName = view.findViewById(R.id.edit_user_first_name);
        lastName = view.findViewById(R.id.edit_user_last_name);
        email = view.findViewById(R.id.edit_user_email);
        phone = view.findViewById(R.id.edit_user_phone);
        editBtn = view.findViewById(R.id.edit_user_confirm_btn);
        editLayout = view.findViewById(R.id.edit_layout);
    }

    private void configViews(){
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCustomer();
            }
        });
    }

    private void editCustomer(){
        User updatedUser = new User();
        updatedUser.setFirstName(firstName.getText().toString());
        updatedUser.setLastName(lastName.getText().toString());
        updatedUser.setEmail(email.getText().toString());
        //phone

        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        Log.d("TAG" , "BASEURL IS: " + WoocommerceUrl.CUSTOMER +  "/" + user.getId());
        AuthenticationWoocommerce authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.POST , WoocommerceUrl.CUSTOMER + "/" + user.getId());

        EditCustomerController editCustomerController = new EditCustomerController(new WoocommerceAPI.editCustomerCallback() {
            @Override
            public void onResponse(boolean successful, User user) {
                if (successful){
                    EventBus.getDefault().post(new EditUserSuccessful(user));
                }else {
                    showSnackBar(false , "خطا در ارتباط با سرور");
                }
            }

            @Override
            public void onFailure() {
                showSnackBar(false , "خطا در ارتباط با سرور");
            }
        });
        editCustomerController.start(authenticationWoocommerce , updatedUser , user.getId());
    }

    private void getArgum(){
        user = (User) getArguments().getSerializable("user");
    }

    private void showSnackBar(boolean successful , String text){
        TSnackbar snackbar = TSnackbar.make(editLayout, text , TSnackbar.LENGTH_LONG);
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
                    editCustomer();
                }
            });
        }
        snackbar.show();
    }
}
