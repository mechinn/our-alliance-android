package com.mechinn.android.ouralliance.event;

import android.bluetooth.BluetoothAdapter;

import de.greenrobot.event.EventBusException;

/**
 * Created by mechinn on 2/7/2015.
 */
public class BluetoothEvent {
    public enum State {
        STATE_CONNECTING,
        STATE_CONNECTED,
        STATE_DISCONNECTING,
        STATE_DISCONNECTED,
        STATE_TURNING_OFF,
        STATE_OFF,
        STATE_TURNING_ON,
        STATE_ON
    }
    private State state;
    public BluetoothEvent(int state) throws EventException {
        switch(state) {
            case BluetoothAdapter.STATE_CONNECTING:
                this.state = State.STATE_CONNECTING;
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                this.state = State.STATE_CONNECTED;
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                this.state = State.STATE_DISCONNECTING;
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                this.state = State.STATE_DISCONNECTED;
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                this.state = State.STATE_TURNING_OFF;
                break;
            case BluetoothAdapter.STATE_OFF:
                this.state = State.STATE_OFF;
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                this.state = State.STATE_TURNING_ON;
                break;
            case BluetoothAdapter.STATE_ON:
                this.state = State.STATE_ON;
                break;
            default:
                throw new EventException("Unknown State");
        }
    }
    public State getState() {
        return state;
    }
}
