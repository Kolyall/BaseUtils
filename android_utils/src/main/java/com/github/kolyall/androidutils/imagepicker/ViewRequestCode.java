package com.github.kolyall.androidutils.imagepicker;

import android.support.annotation.IntDef;

/**
 * Created by Nick Unuchek on 26.09.2017.
 */
@IntDef({ViewRequestCode.LOGO})
public @interface ViewRequestCode {
    int LOGO = 200;
    int CROP_LOGO = 202;
}
