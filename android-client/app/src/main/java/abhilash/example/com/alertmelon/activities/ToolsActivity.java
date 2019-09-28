package abhilash.example.com.alertmelon.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import abhilash.example.com.alertmelon.R;
import abhilash.example.com.alertmelon.singleton.PubNubSingleton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolsActivity extends AppCompatActivity {

    @BindView(R.id.switch_buzzer)
    Switch buzzerSwitch;

    @BindView(R.id.switch_alarm_LED)
    Switch ledAlarmSwitch;

    private PubNub mPubNub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        ButterKnife.bind(this);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String isBuzzerOn = intent.getStringExtra("BUZZER");
                Log.i("BUZZER FROM INTENT", intent.getStringExtra("BUZZER"));
                if (!isBuzzerOn.equals("ON")) {
                    buzzerSwitch.setClickable(false);
                } else {

                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("BUZZER_STATE"));


        mPubNub = PubNubSingleton.getInstance();

        addBuzzerSwitchListener();
        addLedSwitchListener();
    }

    public void addLedSwitchListener() {
        ledAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isButtonChecked) {
                if (isButtonChecked) {
                    // Switch is turned on

                } else {
                    // Switch is turned off
                }
            }
        });
    }

    public void addBuzzerSwitchListener() {
        buzzerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isButtonChecked) {
                if (isButtonChecked) {
                    // Switch off the buzzer
                    mPubNub.addListener(new SubscribeCallback() {
                        @Override
                        public void status(PubNub pubnub, PNStatus status) {

                        }

                        @Override
                        public void message(PubNub pubnub, PNMessageResult message) {

                        }

                        @Override
                        public void presence(PubNub pubnub, PNPresenceEventResult presence) {

                        }
                    });

                } else {
                    // Buzzer is off, do nothing
                }
            }
        });
    }
}
