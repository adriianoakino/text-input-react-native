package com.adriianoakino.views;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.csslayout.CSSNode;
import com.facebook.csslayout.MeasureOutput;
import com.facebook.csslayout.Spacing;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.common.annotations.VisibleForTesting;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIViewOperationQueue;
import com.facebook.react.uimanager.ViewDefaults;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.text.ReactTextShadowNode;
import com.facebook.react.views.text.ReactTextUpdate;

import javax.annotation.Nullable;

@VisibleForTesting
public class ReactTextInputShadowNode extends ReactTextShadowNode implements
        CSSNode.MeasureFunction {

    @SuppressLint("Range")
    private static final int MEASURE_SPEC = View.MeasureSpec.makeMeasureSpec(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            View.MeasureSpec.UNSPECIFIED);

    private @Nullable EditText mEditText;
    private @Nullable float[] mComputedPadding;
    private int mJsEventCount = UNSET;

    public ReactTextInputShadowNode() {
        super(false);
        setMeasureFunction(this);
    }

    @Override
    public void setThemedContext(ThemedReactContext themedContext) {
        super.setThemedContext(themedContext);

        // TODO #7120264: cache this stuff better
        mEditText = new EditText(getThemedContext());
        // This is needed to fix an android bug since 4.4.3 which will throw an NPE in measure,
        // setting the layoutParams fixes it: https://code.google.com/p/android/issues/detail?id=75877
        mEditText.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        setDefaultPadding(Spacing.LEFT, mEditText.getPaddingLeft());
        setDefaultPadding(Spacing.TOP, mEditText.getPaddingTop());
        setDefaultPadding(Spacing.RIGHT, mEditText.getPaddingRight());
        setDefaultPadding(Spacing.BOTTOM, mEditText.getPaddingBottom());
        mComputedPadding = spacingToFloatArray(getPadding());
    }

    @Override
    public void measure(CSSNode node, float width, float height, MeasureOutput measureOutput) {
        // measure() should never be called before setThemedContext()
        EditText editText = Assertions.assertNotNull(mEditText);

        measureOutput.width = width;
        editText.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mFontSize == UNSET ?
                        (int) Math.ceil(PixelUtil.toPixelFromSP(ViewDefaults.FONT_SIZE_SP)) : mFontSize);
        mComputedPadding = spacingToFloatArray(getPadding());
        editText.setPadding(
                (int) Math.ceil(getPadding().get(Spacing.LEFT)),
                (int) Math.ceil(getPadding().get(Spacing.TOP)),
                (int) Math.ceil(getPadding().get(Spacing.RIGHT)),
                (int) Math.ceil(getPadding().get(Spacing.BOTTOM)));

        if (mNumberOfLines != UNSET) {
            editText.setLines(mNumberOfLines);
        }

        editText.measure(MEASURE_SPEC, MEASURE_SPEC);
        measureOutput.height = editText.getMeasuredHeight();
    }

    @Override
    public void onBeforeLayout() {
        // We don't have to measure the text within the text input.
        return;
    }

    @ReactProp(name = "mostRecentEventCount")
    public void setMostRecentEventCount(int mostRecentEventCount) {
        mJsEventCount = mostRecentEventCount;
    }

    @Override
    public void onCollectExtraUpdates(UIViewOperationQueue uiViewOperationQueue) {
        super.onCollectExtraUpdates(uiViewOperationQueue);
        if (mComputedPadding != null) {
            uiViewOperationQueue.enqueueUpdateExtraData(getReactTag(), mComputedPadding);
            mComputedPadding = null;
        }

        if (mJsEventCount != UNSET) {
            Spannable preparedSpannableText = fromTextCSSNode(this);
            ReactTextUpdate reactTextUpdate =
                    new ReactTextUpdate(preparedSpannableText, mJsEventCount, mContainsImages);
            uiViewOperationQueue.enqueueUpdateExtraData(getReactTag(), reactTextUpdate);
        }
    }

    @Override
    public void setPadding(int spacingType, float padding) {
        super.setPadding(spacingType, padding);
        mComputedPadding = spacingToFloatArray(getPadding());
        markUpdated();
    }

    private static float[] spacingToFloatArray(Spacing spacing) {
        return new float[] {
                spacing.get(Spacing.LEFT),
                spacing.get(Spacing.TOP),
                spacing.get(Spacing.RIGHT),
                spacing.get(Spacing.BOTTOM),
        };
    }
}