package com.orsomob.coordinates.property;

/**
 * Created by LucasOrso on 6/17/17.
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.orsomob.coordinates.property.Option.EDIT_AIRPLANE;
import static com.orsomob.coordinates.property.Option.REMOVE_AIRPLANE;
import static com.orsomob.coordinates.property.Option.ROTATE_AIRPLANE;
import static com.orsomob.coordinates.property.Option.TRANSLATE_AIRPLANE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({EDIT_AIRPLANE, REMOVE_AIRPLANE, ROTATE_AIRPLANE, TRANSLATE_AIRPLANE})
public @interface Option {
    int EDIT_AIRPLANE = 0;
    int REMOVE_AIRPLANE = 1;
    int ROTATE_AIRPLANE = 2;
    int TRANSLATE_AIRPLANE = 3;
}