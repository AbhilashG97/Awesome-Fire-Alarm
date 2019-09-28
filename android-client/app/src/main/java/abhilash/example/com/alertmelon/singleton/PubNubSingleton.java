package abhilash.example.com.alertmelon.singleton;

import android.util.Log;

import com.google.gson.JsonElement;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class PubNubSingleton {

    public static PubNub mPubNub;
    public final static String ALARM_CHANNEL = "alarm";
    public final static String TEMPERATURE_CHANNEL = "temperature";
    public final static String USER_SETTINGS_CHANNEL = "user_settings";

    private static JsonElement receivedMessageObject;

    private PubNubSingleton() {

    }

    public static PubNub getInstance() {
        if(mPubNub == null) {
            PNConfiguration pnConfiguration = new PNConfiguration();
            pnConfiguration.setSubscribeKey("sub-c-0bbe0cb0-e2b6-11e8-a575-5ee09a206989");
            pnConfiguration.setPublishKey("pub-c-957647d4-c417-4a35-969f-95d00a04a33f");
            pnConfiguration.setSecure(true);

            Log.i("PUBNUB INSTANCE", "Instance created");

            mPubNub = new PubNub(pnConfiguration);
            return mPubNub;
        }

        return mPubNub;
    }

    /**
     * Listener for PubNub. It listens for fire alert and also sends
     * the signal to stop the buzzer.
     */
    public static void addListener(PubNub pubNub, String... args) {
        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    /**
                     * TODO: Handle event when radio/connectivity lost
                     */
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    // Connected to channel
                    Log.i("CONNECTED TO CHANNEL", "Yay!Connected to channel");
                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
                    /**
                     * TODO: Handle event when radio/connectivity is regained
                     */
                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
                    /**
                     * TODO: Handle decryption event
                     */
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                if (message.getChannel() != null) {
                    Log.i("CHANNEL MESSAGE", message.getChannel());
                } else {
                    Log.i("SUBSCRIPTION MESSAGE", message.getSubscription());
                }

                receivedMessageObject = message.getMessage();
                Log.i("RECEIVED MESSAGE", receivedMessageObject.toString());

                Log.i("MESSAGE CONTENT", message.getMessage()
                        .getAsJsonObject()
                        .get("watermelon").getAsString());
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
    }

}
