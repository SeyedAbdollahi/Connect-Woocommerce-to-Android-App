package seyedabdollahi.ir.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.androidadvance.topsnackbar.TSnackbar;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Response;
import seyedabdollahi.ir.shop.Activities.CartActivity;
import seyedabdollahi.ir.shop.Activities.ProfileActivity;
import seyedabdollahi.ir.shop.Adapters.ProductAdapter;
import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.GetAllCommentsController;
import seyedabdollahi.ir.shop.Data.GetProductsController;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.EventBus.ProductClicked;
import seyedabdollahi.ir.shop.Fragments.ProductFragment;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.Comment;
import seyedabdollahi.ir.shop.Models.CommentList;
import seyedabdollahi.ir.shop.Models.Product;

public class MainActivity extends AppCompatActivity {

    //private String method = "GET";//change API method eg POST,PUT, DELETE etc (ONLY FOR THIS EXAMPLE FOR LIB LIKE RETROFIT,OKHTTP, The Are Dynamic Way)

    private CoordinatorLayout mainLayout;
    private RecyclerView recyclerView;
    private Point point;
    private List<Comment> commentList;
    private CommentList commentListProduct;
    private ProgressBar progressBar;
    private GenerateAuthentication generateAuthentication = new GenerateAuthentication();
    private AuthenticationWoocommerce authenticationWoocommerce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        getDisplayRealSize();
        getProducts();
    }

    private void findViews(){
        recyclerView = findViewById(R.id.main_recycler_view);
        progressBar = findViewById(R.id.main_progress);
        mainLayout = findViewById(R.id.main_layout);
    }

    private void getDisplayRealSize(){
        point = MyPreferenceManager.getInstance(MainActivity.this).getRealSize();
        if (point.x == 1) {
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getRealSize(point);
            MyPreferenceManager.getInstance(MainActivity.this).putRealSize(point);
        }
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
    public void onProductClicked(ProductClicked event){
        String productId = event.getProduct().getId();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product" , event.getProduct());
        commentListProduct = new CommentList();
        commentListProduct.clear();
        for(Comment comment : commentList){
            if (comment.getProductId().equals(productId)){
                commentListProduct.add(comment);
            }
        }
        bundle.putSerializable("comments" , commentListProduct);
        ProductFragment productFragment = new ProductFragment();
        productFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_frame_layout , productFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_profile:
                startActivity(new Intent(this , ProfileActivity.class));
                break;
            case R.id.menu_cart:
                startActivity(new Intent(this , CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getComments(){
        authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.GET , WoocommerceUrl.COMMENTS);
        GetAllCommentsController getAllCommentsController = new GetAllCommentsController(new WoocommerceAPI.getAllCommentsCallback() {
            @Override
            public void onResponse(boolean successful, List<Comment> comments) {
                if(successful){
                    commentList = comments;
                }
                else {
                    commentList = new ArrayList<Comment>();
                }
            }

            @Override
            public void onFailure() {
                commentList = new ArrayList<Comment>();
            }
        });
        getAllCommentsController.start(authenticationWoocommerce);
    }

    private void showSnackBar(boolean successful , String text){
        TSnackbar snackbar = TSnackbar.make(mainLayout, text , TSnackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.WHITE);
        if(successful){
            view.setBackgroundColor(getResources().getColor(R.color.green));
        }else {
            snackbar.setDuration(TSnackbar.LENGTH_INDEFINITE);
            view.setBackgroundColor(getResources().getColor(R.color.red));
            snackbar.setAction("try Again", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProducts();
                }
            });
        }
        snackbar.show();
    }

    private void getProducts(){
        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.GET , WoocommerceUrl.PRODUCTS);

        // ***** START GET PRODUCTS *****
        GetProductsController getProductsController = new GetProductsController(new WoocommerceAPI.getProductsCallback() {
            @Override
            public void onResponse(boolean successful, Response<List<Product>> products) {
                if (successful){
                    ProductAdapter productAdapter = new ProductAdapter(products.body());
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(productAdapter);
                }
                else{
                    showSnackBar(false , "خطا در دریافت اطلاعات");
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure() {
                showSnackBar(false , "خطا در دریافت اطلاعات");
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        getProductsController.start(authenticationWoocommerce);
        getComments();
    }

    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
