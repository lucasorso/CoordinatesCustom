package com.orsomob.coordinates;

import android.graphics.Point;

import com.orsomob.coordinates.util.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;

/**
 * Created by LucasOrso on 6/2/17.
 */
@RunWith(RobolectricTestRunner.class)
@Implements
public class AirplaneTest {

    @Test
    @Config(sdk = 23)
    public void cartesianToPolar() {
        Point lPoint = Function.convertCartesianToPolar(5f, -4f);
    }

    @Test
    @Config(sdk = 23)
    public void polarToCartesina() {
        Point lPoint = Function.convertPolarToCartesian(5f, 60f);
    }
}
