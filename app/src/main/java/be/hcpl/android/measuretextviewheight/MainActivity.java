package be.hcpl.android.measuretextviewheight;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * This calculation method uses a TextView with given context and relies on MeasureSpec to
     * get a calculated height for given text. Pay good attention to units of dimensions passed
     * here as arguments; all should be in pixels!
     * <p>
     * Source: http://stackoverflow.com/questions/19908003/getting-height-of-text-view-before-rendering-to-layout
     */
    public static int method2UsingTextViewAndMeasureSpec(
            final Context context,
            final CharSequence text,
            final int textSize, // in pixels
            final int deviceWidth, // in pixels
            final int padding // in pixels
    ) {

        TextView textView = new TextView(context);
        textView.setPadding(padding, padding, padding, padding); // optional apply paddings here
        textView.setTypeface(Typeface.DEFAULT);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        // use this if you want to pass text in SP unit, check other units, default is SP
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    /**
     * This method relies on a TextPaint and StaticLayout which also gives reliable results on
     * all API levels I've tested so far. Pay good attention to units of dimensions passed here
     * as arguments; all should be in pixels!
     * <p>
     * Source: http://stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android
     */
    public static int method1UsingTextPaintAndStaticLayout(
            final CharSequence text,
            final int textSize, // in pixels
            final int deviceWidth, // in pixels
            final int padding // in pixels
    ) {

        TextPaint myTextPaint = new TextPaint();
        myTextPaint.setAntiAlias(true);
        // this is how you would convert sp to pixels based on screen density
        //myTextPaint.setTextSize(16 * context.getResources().getDisplayMetrics().density);
        myTextPaint.setTextSize(textSize);
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        float spacingMultiplier = 1;
        float spacingAddition = padding; // optionally apply padding here
        boolean includePadding = padding != 0;
        StaticLayout myStaticLayout = new StaticLayout(text, myTextPaint, deviceWidth, alignment, spacingMultiplier, spacingAddition, includePadding);
        return myStaticLayout.getHeight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views to work on
        TextView text1 = (TextView) findViewById(R.id.text1);
        View text2 = findViewById(R.id.text2);
        View text3 = findViewById(R.id.text3);

        // some specs here to take into account
        final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final Point displaySize = new Point();
        wm.getDefaultDisplay().getSize(displaySize);
        // this Point already uses pixels
        final int deviceWidth = displaySize.x;
        // to retrieve resources we need to use the getDimensionPixelSize method to retrieve pixels
        // rather than other units.
        final int margins = getResources().getDimensionPixelSize(R.dimen.margin_padding_normal);
        final int textSize = getResources().getDimensionPixelSize(R.dimen.text_medium);

        // measure attempts using 2 techniques
        final int heightMeasuredWithMethod1 = method1UsingTextPaintAndStaticLayout(text1.getText(), textSize, deviceWidth - 2 * margins, 0);
        final int heightMeasuredWithMethod2 = method2UsingTextViewAndMeasureSpec(text1.getContext(), text1.getText(), textSize, deviceWidth - 2 * margins, 0);

        // apply them
        text2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightMeasuredWithMethod1));
        text3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightMeasuredWithMethod2));
    }

}
