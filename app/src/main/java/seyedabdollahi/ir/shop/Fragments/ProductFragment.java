package seyedabdollahi.ir.shop.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import seyedabdollahi.ir.shop.Activities.CartActivity;
import seyedabdollahi.ir.shop.Adapters.CommentAdapter;
import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.SendCommentController;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.Dialogs.SendCommentDialog;
import seyedabdollahi.ir.shop.EventBus.SendComment;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.Comment;
import seyedabdollahi.ir.shop.Models.CommentList;
import seyedabdollahi.ir.shop.Models.Item;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.Models.Product;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class ProductFragment extends Fragment {

    private ImageView imageView;
    private ConstraintLayout layoutTitle;
    private TextView title;
    private TextView price;
    private Button buy;
    private WebView webView;
    private Product product;
    private CommentList commentList;
    private RecyclerView commentsRecycle;
    private ConstraintLayout commentLayout;
    private ConstraintLayout sendComment;
    private ConstraintLayout virtualLayout;
    private SendCommentDialog sendCommentDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.product_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getArgum();
        findViews(view);
        configViews();
    }

    private void getArgum(){
        Bundle bundle = getArguments();
        product = (Product) bundle.getSerializable("product");
        commentList = (CommentList) bundle.getSerializable("comments");
        Log.d("TAG" , "product clicked is: " + product.getName());
    }

    private void findViews(View view){
        virtualLayout = view.findViewById(R.id.product_fragment_virtual_layout);
        imageView = view.findViewById(R.id.product_fragment_image);
        layoutTitle = view.findViewById(R.id.product_fragment_layout_title);
        title = view.findViewById(R.id.product_fragment_title);
        price = view.findViewById(R.id.product_fragment_price);
        buy = view.findViewById(R.id.product_fragment_buy_btn);
        webView = view.findViewById(R.id.product_fragment_web_view);
        commentsRecycle = view.findViewById(R.id.product_fragment_recycle);
        commentLayout = view.findViewById(R.id.product_fragment_comment_layout);
        sendComment = view.findViewById(R.id.product_fragment_send_comment);
    }

    private void configViews(){
        int height = MyPreferenceManager.getInstance(getActivity()).getRealSize().y;
        imageView.getLayoutParams().height = height / 3;
        layoutTitle.getLayoutParams().height = (int) Math.round(height / 7);

        if (!product.getVirtual().equals("true")){
            virtualLayout.setVisibility(View.GONE);
        }
        if (product.getImages().size() > 0){
            Glide.with(imageView.getContext()).load(product.getImages().get(0).getSrc()).into(imageView);
        }
        title.setText(product.getName());
        price.setText(product.getPrice() + " تومان");
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToCart();
            }
        });

        webView.loadData("<body dir=\"rtl\">" + product.getDescription() + "</body>" , "text/html" , "UTF-8");

        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        commentsRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsRecycle.setAdapter(commentAdapter);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSendCommentDialog();
            }
        });
    }

    private void addItemToCart(){
        User user = MyPreferenceManager.getInstance(getActivity()).getUser();
        if (user.getId() == null){
            Toast.makeText(getActivity() , "ابتدا وارد حساب کاربری شوید" , Toast.LENGTH_SHORT).show();
        }else {
            Item item = new Item();
            item.setName(product.getName());
            item.setQuantity("1");
            item.setPrice(product.getPrice());
            item.setProductId(product.getId());
            item.setTotalPrice(product.getPrice());
            item.setVirtual(product.getVirtual());

            if (product.getImages().size() > 0){
                item.setImage(product.getImages().get(0).getSrc());
            }

            ItemsList itemsList = MyPreferenceManager.getInstance(getActivity()).getCartItems();
            if (isDuplicate(itemsList , item)){
                itemsList.getItemList().add(item);
                MyPreferenceManager.getInstance(getActivity()).putCartItems(itemsList);
                openCartActivity();
            }else {
                Toast.makeText(getActivity() , "این محصول قبلا افزوده شده است" , Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void openCartActivity(){
        startActivity(new Intent(getActivity() , CartActivity.class));
    }

    private boolean isDuplicate(ItemsList itemsList , Item item){
        for (Item item2 : itemsList.getItemList()){
            if (item.getProductId().equals(item2.getProductId())){
                return false;
            }
        }
        return true;
    }

    private void openSendCommentDialog(){
        sendCommentDialog = new SendCommentDialog(getActivity() , product.getId());
        sendCommentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for old api
        sendCommentDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void sendComment(SendComment event){
        Toast.makeText(getActivity() , "ارسال نظر..." , Toast.LENGTH_SHORT).show();
        sendCommentDialog.dismiss();
        Comment comment = new Comment();
        comment.setProductId(event.getProductId());
        comment.setName(event.getName());
        comment.setEmail(event.getEmail());
        comment.setText(event.getReview());
        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        AuthenticationWoocommerce authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.POST , WoocommerceUrl.COMMENTS);
        SendCommentController sendCommentController = new SendCommentController(new WoocommerceAPI.sendCommentCallback() {
            @Override
            public void onResponse(boolean successful) {
                if (successful){
                    Toast.makeText(getActivity() , "نظر با موفقیت ارسال شد" , Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity() , "خطا در ارسال نظر" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getActivity() , "خطا در ارسال نظر" , Toast.LENGTH_SHORT).show();
            }
        });
        sendCommentController.start(authenticationWoocommerce , comment);
    }
}
