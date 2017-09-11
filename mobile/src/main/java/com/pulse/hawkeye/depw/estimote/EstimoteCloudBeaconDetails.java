package com.pulse.hawkeye.depw.estimote;


import com.estimote.coresdk.cloud.model.Color;
import com.pulse.hawkeye.depw.utils.IConst;

import java.util.UUID;

public class EstimoteCloudBeaconDetails {

    private String beaconName;
    private Color beaconColor;
    private UUID uuid;

    public EstimoteCloudBeaconDetails(String beaconName, Color beaconColor, UUID uuid) {
        this.beaconName = beaconName;
        this.beaconColor = beaconColor;
        this.uuid = uuid;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public Color getBeaconColor() {
        return beaconColor;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "[beaconName: " + getBeaconName() + ", beaconColor: " + getBeaconColor() +
                "beaconUUID :" + getUuid() != null ? getUuid().toString() : IConst.EMPTY_STRING + "]";
    }
}
