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

import seyedabdollahi.ir.shop.EventBus.SendComment;
import seyedabdollahi.ir.shop.R;

public class SendCommentDialog extends Dialog {

    private String productId;
    private int width;
    private int height;
    private EditText name;
    private EditText email;
    private EditText review;
    private TextView error;
    private Button sendBtn;

    public SendCommentDialog(@NonNull Context context, String productId) {
        super(context);
        setWidthHeight(context);
        this.productId = productId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_comment_dialog);
        findViews();
        getWindow().setLayout(width , height);
        configViews();
    }

    private void setWidthHeight(Context context){
        width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
    }

    private void findViews(){
        sendBtn = findViewById(R.id.send_comment_dialog_btn);
        name = findViewById(R.id.send_comment_dialog_name);
        email = findViewById(R.id.send_comment_dialog_email);
        review = findViewById(R.id.send_comment_dialog_review);
        error = findViewById(R.id.send_comment_dialog_error);
    }

    private void configViews(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")){
                    error.setText("نام خود را وارد کنید");
                    error.setVisibility(View.VISIBLE);
                }else if(email.getText().toString().equals("")){
                    error.setText("ایمیل خود را وارد کنید");
                    error.setVisibility(View.VISIBLE);
                }else if (review.getText().toString().equals("")){
                    error.setText("نظر خود را وارد کنید");
                    error.setVisibility(View.VISIBLE);
                }else{
                    EventBus.getDefault().post(new SendComment(productId , name.getText().toString() , email.getText().toString() , review.getText().toString()));
                }
            }
        });
    }
}
