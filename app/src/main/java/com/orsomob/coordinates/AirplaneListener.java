package com.orsomob.coordinates;

import com.orsomob.coordinates.module.Airplane;

/**
 * Created by LucasOrso on 6/3/17.
 */

public interface AirplaneListener {
    void onReceiveAirplane(Airplane aAirplane);
    void onUpdateAirplane(Airplane aAirplane);
}
