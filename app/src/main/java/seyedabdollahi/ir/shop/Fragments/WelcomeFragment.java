package seyedabdollahi.ir.shop.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import seyedabdollahi.ir.shop.R;

public class WelcomeFragment extends Fragment {

    private Button btnLogin;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        configViews();
    }

    private void findViews(View view){
        btnLogin = view.findViewById(R.id.welcome_btn_login);
        btnRegister = view.findViewById(R.id.welcome_btn_register);
    }

    private void configViews(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginFragment();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterFragment();
            }
        });
    }

    private void openLoginFragment(){
        LoginFragment loginFragment = new LoginFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.profile_full_frame , loginFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRegisterFragment(){
        RegisterFragment registerFragment = new RegisterFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.profile_full_frame , registerFragment)
                .addToBackStack(null)
                .commit();
    }
}
