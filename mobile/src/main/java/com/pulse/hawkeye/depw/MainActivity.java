package com.pulse.hawkeye.depw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pulse.hawkeye.depw.estimote.BeaconID;
import com.pulse.hawkeye.depw.estimote.EstimoteCloudBeaconDetails;
import com.pulse.hawkeye.depw.estimote.EstimoteCloudBeaconDetailsFactory;
import com.pulse.hawkeye.depw.estimote.ProximityContentManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ProximityContentManager proximityContentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(
                        // TODO: replace with UUIDs, majors and minors of your own beacons
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 1, 1),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 2, 2),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 3, 3)),
                new EstimoteCloudBeaconDetailsFactory());
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;
                Integer backgroundColor;
                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;
                    text = "You're in " + beaconDetails.getBeaconName() + "'s range!";
                    backgroundColor = BACKGROUND_COLORS.get(beaconDetails.getBeaconColor());
                } else {
                    text = "No beacons in range.";
                    backgroundColor = null;
                }
                ((TextView) findViewById(R.id.textView)).setText(text);
                findViewById(R.id.relativeLayout).setBackgroundColor(
                        backgroundColor != null ? backgroundColor : BACKGROUND_COLOR_NEUTRAL);
            }
        });
    }
}
