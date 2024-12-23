package org.example.codiblybackend.util;

public class PhotovoltaicsUtil {
    private static final float INSTALLATION_POWER = 2.5f;
    private static final float PANEL_EFFICIENCY = 0.2f;
    private static final float SECONDS_IN_HOUR = 3600;

    public static float calcGeneratedEnergy(float sunshineDuration) {
        return INSTALLATION_POWER * sunshineDuration / SECONDS_IN_HOUR * PANEL_EFFICIENCY;
    }
}
