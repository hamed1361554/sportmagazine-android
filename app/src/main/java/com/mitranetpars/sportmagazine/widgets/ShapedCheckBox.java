package com.mitranetpars.sportmagazine.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.mitranetpars.sportmagazine.R;

/**
 * Created by Hamed on 12/8/2016.
 */

public class ShapedCheckBox extends CheckBox {
    public ShapedCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean t){
        int color = this.getCurrentTextColor();

        if(t)
        {
            this.setBackgroundResource(R.drawable.checkbox_select);
        }
        else
        {
            this.setBackgroundResource(R.drawable.checkbox_deselect);
        }
        super.setChecked(t);

        this.setBackgroundColor(color);
        this.setTextColor(color);
    }
}
