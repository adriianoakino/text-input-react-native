package com.adriianoakino.views;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Event emitted by EditText native view when the text selection changes.
 */
/* package */ class ReactTextInputSelectionEvent
        extends Event<ReactTextInputSelectionEvent> {

    private static final String EVENT_NAME = "topSelectionChange";

    private int mSelectionStart;
    private int mSelectionEnd;

    public ReactTextInputSelectionEvent(
            int viewId,
            long timestampMs,
            int selectionStart,
            int selectionEnd) {
        super(viewId, timestampMs);
        mSelectionStart = selectionStart;
        mSelectionEnd = selectionEnd;
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

        WritableMap selectionData = Arguments.createMap();
        selectionData.putInt("start", mSelectionStart);
        selectionData.putInt("end", mSelectionEnd);

        eventData.putMap("selection", selectionData);
        return eventData;
    }
}