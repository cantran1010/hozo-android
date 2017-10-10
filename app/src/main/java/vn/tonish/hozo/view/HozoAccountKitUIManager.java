package vn.tonish.hozo.view;

import android.app.Fragment;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.ui.BaseUIManager;
import com.facebook.accountkit.ui.ButtonType;
import com.facebook.accountkit.ui.LoginFlowState;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.TextPosition;

import vn.tonish.hozo.fragment.HozoPlaceholderFragment;

/**
 * Created by CanTran on 10/6/17.
 */

public class HozoAccountKitUIManager extends BaseUIManager {

    private static final int HEADER_HEIGHT = 120;
    private final ButtonType confirmButton;
    private final ButtonType entryButton;
    private AccountKitError error;
    private LoginType loginType;
    private final TextPosition textPosition;

    @Deprecated
    public HozoAccountKitUIManager(
            final ButtonType confirmButton,
            final ButtonType entryButton,
            final TextPosition textPosition,
            final LoginType loginType) {
        super(-1);
        this.confirmButton = confirmButton;
        this.entryButton = entryButton;
        this.textPosition = textPosition;
        this.loginType = loginType;
    }

    private HozoAccountKitUIManager(final Parcel source) {
        super(source);
        this.loginType = LoginType.values()[source.readInt()];
        String s = source.readString();
        final ButtonType confirmButton = s == null ? null : ButtonType.valueOf(s);
        s = source.readString();
        final ButtonType entryButton = s == null ? null : ButtonType.valueOf(s);
        s = source.readString();
        final TextPosition textPosition = s == null ? null : TextPosition.valueOf(s);
        this.confirmButton = confirmButton;
        this.entryButton = entryButton;
        this.textPosition = textPosition;
    }


    @Override
    @Nullable
    public ButtonType getButtonType(final LoginFlowState state) {
        switch (state) {
            case PHONE_NUMBER_INPUT:
            case EMAIL_INPUT:
                return entryButton;
            case CODE_INPUT:
            case CONFIRM_ACCOUNT_VERIFIED:
                return confirmButton;
            default:
                return null;
        }
    }


    @Override
    @Nullable
    public Fragment getHeaderFragment(final LoginFlowState state) {
        if (state != LoginFlowState.ERROR) {
            return getPlaceholderFragment(state, HEADER_HEIGHT);
        }
        final String errorMessage = getErrorMessage();
        if (errorMessage == null) {
            return HozoPlaceholderFragment.create(HEADER_HEIGHT);
        } else {
            return HozoPlaceholderFragment.create(HEADER_HEIGHT);
        }
    }

    @Override
    @Nullable
    public TextPosition getTextPosition(final LoginFlowState state) {
        return textPosition;
    }

    @Override
    public void onError(final AccountKitError error) {
        this.error = error;
    }

    private String getErrorMessage() {
        if (error == null) {
            return null;
        }

        final String message = error.getUserFacingMessage();
        if (message == null) {
            return null;
        }

        return message;
    }

    @Nullable
    private HozoPlaceholderFragment getPlaceholderFragment(
            final LoginFlowState state,
            final int height) {
        return HozoPlaceholderFragment.create(height);
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(loginType.ordinal());
        dest.writeString(confirmButton != null ? confirmButton.name() : null);
        dest.writeString(entryButton != null ? entryButton.name() : null);
        dest.writeString(textPosition != null ? textPosition.name() : null);
    }

    public static final Creator<HozoAccountKitUIManager> CREATOR
            = new Creator<HozoAccountKitUIManager>() {
        @Override
        public HozoAccountKitUIManager createFromParcel(final Parcel source) {
            return new HozoAccountKitUIManager(source);
        }

        @Override
        public HozoAccountKitUIManager[] newArray(final int size) {
            return new HozoAccountKitUIManager[size];
        }
    };
}
