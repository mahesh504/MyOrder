package in.arula.myorder.validator;

import android.content.Context;


abstract class BaseValidation implements Validation {

    protected Context mContext;

    protected BaseValidation(Context context) {
        mContext = context;
    }

}