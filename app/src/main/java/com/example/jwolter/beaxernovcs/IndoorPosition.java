package com.example.jwolter.beaxernovcs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Verwaltet die drei Koordinaten eines Beacons.
 * Diese Klasse muss das Interface Parcelable implementieren damit sie
 * über intent.putExtra als Broadcast verschickt bzw. von intent.getParcelableExtra
 * empfangen werden kann
 */
public class IndoorPosition implements Parcelable {

    private int x;
    private int y;
    private int z;

    public IndoorPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected IndoorPosition(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        //Test bb
    }

    public static final Creator<IndoorPosition> CREATOR = new Creator<IndoorPosition>() {

        @Override
        public IndoorPosition createFromParcel(Parcel in) {
            return new IndoorPosition(in);
        }

        @Override
        public IndoorPosition[] newArray(int size) {
            return new IndoorPosition[size];
        }

    };

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(z);
    }
}

