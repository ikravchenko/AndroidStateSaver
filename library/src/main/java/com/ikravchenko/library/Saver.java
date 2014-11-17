package com.ikravchenko.library;

import android.os.Bundle;

public interface Saver<T> {
    void save(T object, Bundle inState);
    void restore(T object, Bundle inState);
}
