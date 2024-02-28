package com.example.helpgenic.Patient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.util.Log;

import com.example.helpgenic.R;

import io.agora.rtc2.Constants;
import io.agora.agorauikit_android.*;


public class JoinMeeting extends AppCompatActivity {


    // Object of AgoraVideoVIewer class
    private AgoraVideoViewer agView = null;
    // Fill the App ID of your project generated on Agora Console.
    private String appId = "722aac9c01b14f069b2d7a16dc35763e";
    // Fill the channel name.
    private String channelName = "sdaChannel";
    // Fill the temp token generated on Agora Console.
    private String token = "007eJxTYLhbHMGc6VUloq9ksVuM+xNDQfHmWPsdOr9uqFvJs4TEzFNgMDcySkxMtkw2MEwyNEkzMLNMMkoxTzQ0S0k2NjU3M04tnNyf3BDIyCDw8jIrIwMEgvhcDMUpic4ZiXl5qTkMDADXrR5c";


    private void initializeAndJoinChannel() {
        // Create AgoraVideoViewer instance
        try {
            agView = new AgoraVideoViewer(this, new AgoraConnectionData(appId, token), AgoraVideoViewer.Style.FLOATING, new AgoraSettings(), null);
        } catch (Exception e) {
            Log.e("AgoraVideoViewer",
                    "Could not initialize AgoraVideoViewer. Check that your app Id is valid.");
            Log.e("Exception", e.toString());
            return;
        }
        // Add the AgoraVideoViewer to the Activity layout
        this.addContentView(agView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT)
        );
        // Check permission and join a channel
        if (DevicePermissionsKt.requestPermissions(AgoraVideoViewer.Companion, this)) {
            joinChannel();
        }
        else {
            Button joinButton = new Button(this);
            joinButton.setText("Allow camera and microphone access, then click here");
            joinButton.setOnClickListener(new View.OnClickListener() {
                // When the button is clicked, check permissions again and join channel
                @Override
                public void onClick(View view) {
                    if (DevicePermissionsKt.requestPermissions(AgoraVideoViewer.Companion, getApplicationContext())) {
                        ((ViewGroup) joinButton.getParent()).removeView(joinButton);
                        joinChannel();
                    }
                }
            });
            this.addContentView(joinButton, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 200));
        }
    }


    void joinChannel(){

        agView.join(channelName, token, Constants.CLIENT_ROLE_BROADCASTER, 0);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_meeting);
        initializeAndJoinChannel();


    }
}