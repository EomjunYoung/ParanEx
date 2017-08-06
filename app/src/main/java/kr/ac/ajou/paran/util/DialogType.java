package kr.ac.ajou.paran.util;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

/**
 * Created by dream on 2017-08-06.
 */

public class DialogType extends Dialog {

    public DialogType(Context context, int layout) {
        super(context);

        setContentView(layout);
        setOwnerActivity((Activity)context);
        setCanceledOnTouchOutside(false);
    }

    public void showDialog() {
        show();
    }
}
