package com.mechinn.android.ouralliance.event;

import android.bluetooth.BluetoothAdapter;

import de.greenrobot.event.EventBusException;

/**
 * Created by mechinn on 2/7/2015.
 */
public class BluetoothEvent {
    public enum State {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,
        TURNING_OFF,
        OFF,
        TURNING_ON,
        ON,
        DISABLED
    }
    private State state;
    public BluetoothEvent(int state) {
        switch(state) {
            case BluetoothAdapter.STATE_CONNECTING:
                this.state = State.CONNECTING;
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                this.state = State.CONNECTED;
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                this.state = State.DISCONNECTING;
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                this.state = State.DISCONNECTED;
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                this.state = State.TURNING_OFF;
                break;
            case BluetoothAdapter.STATE_OFF:
                this.state = State.OFF;
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                this.state = State.TURNING_ON;
                break;
            case BluetoothAdapter.STATE_ON:
                this.state = State.ON;
                break;
            default:
                this.state = State.DISABLED;
        }
    }
    public State getState() {
        return state;
    }
    public boolean isEnabled() {
        return !state.equals(State.DISABLED);
    }
    public boolean isDisabled() {
        return state.equals(State.DISABLED);
    }
    public boolean isConnected() {
        return state.equals(State.CONNECTED);
    }
    public boolean isOn() {
        return state.equals(State.ON);
    }
}
