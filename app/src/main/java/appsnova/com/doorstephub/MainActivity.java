package appsnova.com.doorstephub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import appsnova.com.doorstephub.activities.HomeActivity;
import appsnova.com.doorstephub.activities.LoginActivity;
import appsnova.com.doorstephub.activities.SplashScreenActivity;
import appsnova.com.doorstephub.adapters.IntroSliderAdapter;
import appsnova.com.doorstephub.utilities.SharedPref;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private LinearLayout dotsLinearLayout;
    private IntroSliderAdapter introSliderAdapter;

    private TextView[] mdots;
    private Button backBtn,nextBtn;

    private int mCureentPage;

    SharedPref sharedPref;

    //for onbackpress delay for 2seconds
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        sharedPref = new SharedPref(MainActivity.this);
        if(!sharedPref.getStringValue("isFirstOpen").isEmpty()){
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        else{
           sharedPref.setStringValue("isFirstOpen","1");
        }
        viewpager=(ViewPager)findViewById(R.id.viewpager);
        dotsLinearLayout=(LinearLayout)findViewById(R.id.dotsLinearLayout);

        nextBtn=(Button)findViewById(R.id.nextBtn);
        backBtn=(Button)findViewById(R.id.backBtn);

        introSliderAdapter=new IntroSliderAdapter(this);
        viewpager.setAdapter(introSliderAdapter);
        adddots(0);

        viewpager.addOnPageChangeListener(viewlistener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage+1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage-1);
            }
        });
    } //end of onCreate

    public void adddots(int i){
        mdots=new TextView[4];
        dotsLinearLayout.removeAllViews();

        for (int x=0;x<mdots.length;x++){

            mdots[x]=new TextView(this);
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(35);
            mdots[x].setTextColor(getResources().getColor(R.color.colorGray));

            dotsLinearLayout.addView(mdots[x]);
        }
        if (mdots.length>0){

            mdots[i].setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            adddots(position);
            mCureentPage = position;

            if (position==0){
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("NEXT");
                backBtn.setText("");
            }
            else if(position==mdots.length-1){

                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("FINISH");
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                       startActivity(intent);
                    }
                });
                backBtn.setText("BACK");

            }
            else {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true );
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("NEXT");
                backBtn.setText("BACK");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
