package com.ikravchenko.instancesaver;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleObject implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SimpleObject createFromParcel(Parcel in) {
            return new SimpleObject(in);
        }

        public SimpleObject[] newArray(int size) {
            return new SimpleObject[size];
        }
    };
    private final int min;
    private final int max;
    private final double result;

    public SimpleObject(int min, int max, double result) {
        this.min = min;
        this.max = max;
        this.result = result;
    }

    public SimpleObject(Parcel in) {
        this.min = in.readInt();
        this.max = in.readInt();
        this.result = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.min);
        dest.writeInt(this.max);
        dest.writeDouble(this.result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleObject)) return false;

        SimpleObject that = (SimpleObject) o;

        if (max != that.max) return false;
        if (min != that.min) return false;
        if (Double.compare(that.result, result) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1;
        long temp;
        result1 = min;
        result1 = 31 * result1 + max;
        temp = Double.doubleToLongBits(result);
        result1 = 31 * result1 + (int) (temp ^ (temp >>> 32));
        return result1;
    }
}
