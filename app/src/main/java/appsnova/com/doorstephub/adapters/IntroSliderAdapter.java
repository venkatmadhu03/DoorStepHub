package appsnova.com.doorstephub.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import appsnova.com.doorstephub.R;

public class IntroSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    //Array
    public int[] list_images={

            R.drawable.intro_image1,
            R.drawable.intro_image2,
            R.drawable.intro_image3,
            R.drawable.intro_image4
    };

    public String[] list_title={

            "Good Service Makes the difference",
            "Easiest Way To Get Doorstep Services",
            "Timely Service",
            "Take A Cup Of Tea! And Stay Relaxed"
    };

    public String[] list_description={

            " ",
            "Jut spend few Seconds of your \n valuable time to share your \n requirement and Schedule time for Us.",
            "Within few minutes of your request, \n you will receive a call from our \n professional we follow-up on the \n customer call.",
            "We send our expertsat your \n proximate location within time, \n that's our exclusivity."
    };
  /*  public int[] list_color={

            Color.rgb(193, 66, 44),
            Color.rgb(193, 172, 44),
            Color.rgb(193, 41, 249),
            Color.rgb(68, 83, 242)

    };*/


    public IntroSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_title.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view==(LinearLayout)obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.intro_slider,container,false);

        LinearLayout linearLayout = view.findViewById(R.id.slidelinearlayout);
        ImageView img = view.findViewById(R.id.slideimg);
        //TextView txt1 = view.findViewById(R.id.slidetitle);
        //TextView txt2 = view.findViewById(R.id.slidedescription);

        img.setImageResource(list_images[position]);
       // txt1.setText(list_title[position]);
       // txt2.setText(list_description[position]);
       // linearLayout.setBackgroundColor(list_color[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}
