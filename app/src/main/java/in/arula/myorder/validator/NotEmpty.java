package in.arula.myorder.validator;

import android.content.Context;
import android.text.TextUtils;

import in.arula.myorder.R;


public class NotEmpty extends BaseValidation {

    public static Validation build(Context context) {
        return new NotEmpty(context);
    }

    private NotEmpty(Context context) {
        super(context);
    }

    @Override
    public String getErrorMessage() {
        return mContext.getString(R.string.validator_empty);
    }

    @Override
    public boolean isValid(String text) {
        return !TextUtils.isEmpty(text.trim());
    }
}