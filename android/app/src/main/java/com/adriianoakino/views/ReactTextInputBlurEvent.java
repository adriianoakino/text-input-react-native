package com.adriianoakino.views;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Event emitted by EditText native view when it loses focus.
 */
/* package */ class ReactTextInputBlurEvent extends Event<ReactTextInputBlurEvent> {

    private static final String EVENT_NAME = "topBlur";

    public ReactTextInputBlurEvent(
            int viewId,
            long timestampMs) {
        super(viewId, timestampMs);
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public boolean canCoalesce() {
        return false;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap eventData = Arguments.createMap();
        eventData.putInt("target", getViewTag());
        return eventData;
    }
}