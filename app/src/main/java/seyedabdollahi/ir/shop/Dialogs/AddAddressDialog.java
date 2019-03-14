package seyedabdollahi.ir.shop.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import seyedabdollahi.ir.shop.EventBus.ChangeAddressList;
import seyedabdollahi.ir.shop.Models.Address;
import seyedabdollahi.ir.shop.R;

public class AddAddressDialog extends Dialog {

    private int width;
    private int height;
    private EditText state;
    private EditText city;
    private EditText postCode;
    private EditText address;
    private TextView error;
    private Button addAddress;
    private Address saveAddress;
    private String action = "ADD";

    public AddAddressDialog(@NonNull Context context, Address saveAddress) {
        super(context);
        setWidthHeight(context);
        this.saveAddress = saveAddress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_dialog);
        findViews();
        getWindow().setLayout(width , height);
        configViews();
    }

    private void setWidthHeight(Context context){
        width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.65);
    }

    private void findViews(){
        state = findViewById(R.id.add_address_state);
        city = findViewById(R.id.add_address_city);
        postCode = findViewById(R.id.add_address_post_code);
        address = findViewById(R.id.add_address_address);
        addAddress = findViewById(R.id.add_address_btn);
        error = findViewById(R.id.add_address_error);
    }

    private void configViews(){
        if (saveAddress != null){
            state.setText(saveAddress.getState());
            city.setText(saveAddress.getCity());
            postCode.setText(saveAddress.getPostCode());
            address.setText(saveAddress.getAddress());
            action = "EDITED";
        }
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.getText().toString().equals("")){
                    error.setText("استان را وارد کنید");
                    error.setVisibility(View.VISIBLE);
                }else if (city.getText().toString().equals("")){
                    error.setText("شهر را وارد کنید");
                    error.setVisibility(View.VISIBLE);
                }else if (postCode.getText().toString().equals("")){
                    error.setText("کدپستی را وارد کنید");
                    error.setVisibility(View.VISIBLE);
                }else if (address.getText().toString().equals("")){
                    error.setText("آدرس را وارد کنید");
                    error.setVisibility(View.VISIBLE);
                }else {
                    EventBus.getDefault().post(new ChangeAddressList(
                            action ,
                            new Address(
                            state.getText().toString() ,
                            city.getText().toString() ,
                            postCode.getText().toString() ,
                            address.getText().toString() ,
                            false)
                    , 0));
                }
            }
        });
    }
}
