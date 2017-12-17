package security.bercy.com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Bercy on 7/30/17.
 */

public class FocusedTextView extends TextView {

    //use new object will call this
    public FocusedTextView(Context context) {
        super(context);
    }

    //if there is attributes call this
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //if there is style will call this method
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /*
    跑马灯要运行必须调用此函数判断是否有焦点，是true的话跑马灯才会有效果
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
