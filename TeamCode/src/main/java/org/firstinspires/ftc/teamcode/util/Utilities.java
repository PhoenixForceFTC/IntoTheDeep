package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

public final class Utilities {
    @NonNull
    @SafeVarargs
    public static <E> ArrayList<E> listFromParams(@NonNull E... elements) {
        final ArrayList<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }
}
