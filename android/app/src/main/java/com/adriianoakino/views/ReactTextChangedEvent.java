package com.adriianoakino.views;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Event emitted by EditText native view when text changes.
 * VisibleForTesting from {@link TextInputEventsTestCase}.
 */
public class ReactTextChangedEvent extends Event<ReactTextChangedEvent> {

    public static final String EVENT_NAME = "topChange";

    private String mText;
    private int mContentWidth;
    private int mContentHeight;
    private int mEventCount;

    public ReactTextChangedEvent(
            int viewId,
            long timestampMs,
            String text,
            int contentSizeWidth,
            int contentSizeHeight,
            int eventCount) {
        super(viewId, timestampMs);
        mText = text;
        mContentWidth = contentSizeWidth;
        mContentHeight = contentSizeHeight;
        mEventCount = eventCount;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap eventData = Arguments.createMap();
        eventData.putString("text", mText);

        WritableMap contentSize = Arguments.createMap();
        contentSize.putDouble("width", mContentWidth);
        contentSize.putDouble("height", mContentHeight);
        eventData.putMap("contentSize", contentSize);
        eventData.putInt("eventCount", mEventCount);

        eventData.putInt("target", getViewTag());
        return eventData;
    }
}
