package com.orsomob.coordinates;

import com.orsomob.coordinates.module.PointDouble;
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
        PointDouble lPoint = Function.convertCartesianToPolar(5f, -4f);
    }

    @Test
    @Config(sdk = 23)
    public void polarToCartesina() {
        PointDouble lPoint = Function.convertPolarToCartesian(5.0, 60.0);
    }
}
