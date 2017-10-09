/**
 * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package vn.tonish.hozo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.tonish.hozo.R;

import static vn.tonish.hozo.common.Constants.HEIGHT_KEY;

/**
 * Created by CanTran on 10/6/17.
 */

public class HozoPlaceholderFragment extends Fragment {

    public static HozoPlaceholderFragment create(final int height) {
        final HozoPlaceholderFragment fragment = new HozoPlaceholderFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt(HEIGHT_KEY, height);
        fragment.setArguments(arguments);
        return fragment;
    }


    public void setOnClickListener(final View.OnClickListener onClickListener) {
        final View view = getView();
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hozo_placeholder, container, false);
        }

        final Bundle arguments = getArguments();
        if (arguments != null) {
            final int height = arguments.getInt(HEIGHT_KEY, -1);
            if (height >= 0) {
                view.getLayoutParams().height = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        height,
                        getResources().getDisplayMetrics());
            }

        }


        return view;
    }
}
