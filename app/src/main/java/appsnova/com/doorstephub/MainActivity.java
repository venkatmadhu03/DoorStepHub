package appsnova.com.doorstephub;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import appsnova.com.doorstephub.adapters.IntroSliderAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private LinearLayout dotsLinearLayout;
    private IntroSliderAdapter introSliderAdapter;

    private TextView[] mdots;
    private Button backBtn,nextBtn;

    private int mCureentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
