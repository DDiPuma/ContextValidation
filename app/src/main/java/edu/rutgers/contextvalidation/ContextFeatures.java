package edu.rutgers.contextvalidation;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by dominic on 11/14/17.
 */

@Entity
class ContextFeatures {
    @PrimaryKey
    public long timestamp;

    @ColumnInfo(name = "day_of_week")
    public int dayOfWeek;

    @ColumnInfo(name = "time_of_day")
    public int timeOfDay;

    @ColumnInfo(name = "battery_state")
    public int batteryState;

    @ColumnInfo(name = "battery_level")
    public int batteryLevel;

    @Embedded
    WifiNetwork wifiNetwork;

    /*
    // We can't store arrays in a database
    // I hate this, but there's no better way without serializing and that's going to cause
    // problems
    @Embedded(prefix = "wifi0")
    WifiNetwork wifiNetwork0;

    @Embedded(prefix = "wifi1")
    WifiNetwork wifiNetwork1;

    @Embedded(prefix = "wifi2")
    WifiNetwork wifiNetwork2;

    @Embedded(prefix = "wifi3")
    WifiNetwork wifiNetwork3;

    @Embedded(prefix = "wifi4")
    WifiNetwork wifiNetwork4;

    @Embedded(prefix = "wifi5")
    WifiNetwork wifiNetwork5;

    @Embedded(prefix = "wifi6")
    WifiNetwork wifiNetwork6;

    @Embedded(prefix = "wifi7")
    WifiNetwork wifiNetwork7;

    @Embedded(prefix = "wifi8")
    WifiNetwork wifiNetwork8;

    @Embedded(prefix = "wifi9")
    WifiNetwork wifiNetwork9;

    @Embedded(prefix = "wifi10")
    WifiNetwork wifiNetwork10;

    @Embedded(prefix = "wifi11")
    WifiNetwork wifiNetwork11;

    @Embedded(prefix = "wifi12")
    WifiNetwork wifiNetwork12;

    @Embedded(prefix = "wifi13")
    WifiNetwork wifiNetwork13;

    @Embedded(prefix = "wifi14")
    WifiNetwork wifiNetwork14;

    @Embedded(prefix = "wifi15")
    WifiNetwork wifiNetwork15;

    public void setWifiNetwork(int i, WifiNetwork network) {
        switch (i) {
            case 0:
                wifiNetwork0 = network;
                break;
            case 1:
                wifiNetwork1 = network;
                break;
            case 2:
                wifiNetwork2 = network;
                break;
            case 3:
                wifiNetwork3 = network;
                break;
            case 4:
                wifiNetwork4 = network;
                break;
            case 5:
                wifiNetwork5 = network;
                break;
            case 6:
                wifiNetwork6 = network;
                break;
            case 7:
                wifiNetwork7 = network;
                break;
            case 8:
                wifiNetwork8 = network;
                break;
            case 9:
                wifiNetwork9 = network;
                break;
            case 10:
                wifiNetwork10 = network;
                break;
            case 11:
                wifiNetwork11 = network;
                break;
            case 12:
                wifiNetwork12 = network;
                break;
            case 13:
                wifiNetwork13 = network;
                break;
            case 14:
                wifiNetwork14 = network;
                break;
            case 15:
                wifiNetwork15 = network;
                break;
        }
    }

    public WifiNetwork getWifiNetwork(int i) {
        switch (i) {
            case 0:
                return wifiNetwork0;
            case 1:
                return wifiNetwork1;
            case 2:
                return wifiNetwork2;
            case 3:
                return wifiNetwork3;
            case 4:
                return wifiNetwork4;
            case 5:
                return wifiNetwork5;
            case 6:
                return wifiNetwork6;
            case 7:
                return wifiNetwork7;
            case 8:
                return wifiNetwork8;
            case 9:
                return wifiNetwork9;
            case 10:
                return wifiNetwork10;
            case 11:
                return wifiNetwork11;
            case 12:
                return wifiNetwork12;
            case 13:
                return wifiNetwork13;
            case 14:
                return wifiNetwork14;
            case 15:
                return wifiNetwork15;
            default:
                return null;
        }
    }
    */

    @Embedded
    CellNetwork cellNetwork;

    /*
    @Embedded(prefix = "cell0")
    CellNetwork cellNetwork0;

    @Embedded(prefix = "cell1")
    CellNetwork cellNetwork1;

    @Embedded(prefix = "cell2")
    CellNetwork cellNetwork2;

    @Embedded(prefix = "cell3")
    CellNetwork cellNetwork3;

    @Embedded(prefix = "cell4")
    CellNetwork cellNetwork4;

    @Embedded(prefix = "cell5")
    CellNetwork cellNetwork5;

    @Embedded(prefix = "cell6")
    CellNetwork cellNetwork6;

    @Embedded(prefix = "cell7")
    CellNetwork cellNetwork7;

    @Embedded(prefix = "cell8")
    CellNetwork cellNetwork8;

    @Embedded(prefix = "cell9")
    CellNetwork cellNetwork9;

    @Embedded(prefix = "cell10")
    CellNetwork cellNetwork10;

    @Embedded(prefix = "cell11")
    CellNetwork cellNetwork11;

    @Embedded(prefix = "cell12")
    CellNetwork cellNetwork12;

    @Embedded(prefix = "cell13")
    CellNetwork cellNetwork13;

    @Embedded(prefix = "cell14")
    CellNetwork cellNetwork14;

    @Embedded(prefix = "cell15")
    CellNetwork cellNetwork15;

    public void setCellNetwork(int i, CellNetwork network) {
        switch (i) {
            case 0:
                cellNetwork0 = network;
                break;
            case 1:
                cellNetwork1 = network;
                break;
            case 2:
                cellNetwork2 = network;
                break;
            case 3:
                cellNetwork3 = network;
                break;
            case 4:
                cellNetwork4 = network;
                break;
            case 5:
                cellNetwork5 = network;
                break;
            case 6:
                cellNetwork6 = network;
                break;
            case 7:
                cellNetwork7 = network;
                break;
            case 8:
                cellNetwork8 = network;
                break;
            case 9:
                cellNetwork9 = network;
                break;
            case 10:
                cellNetwork10 = network;
                break;
            case 11:
                cellNetwork11 = network;
                break;
            case 12:
                cellNetwork12 = network;
                break;
            case 13:
                cellNetwork13 = network;
                break;
            case 14:
                cellNetwork14 = network;
                break;
            case 15:
                cellNetwork15 = network;
                break;
        }
    }

    public CellNetwork getCellNetwork(int i) {
        return cellNetwork;
        /*
        switch (i) {
            case 0:
                return cellNetwork0;
            case 1:
                return cellNetwork1;
            case 2:
                return cellNetwork2;
            case 3:
                return cellNetwork3;
            case 4:
                return cellNetwork4;
            case 5:
                return cellNetwork5;
            case 6:
                return cellNetwork6;
            case 7:
                return cellNetwork7;
            case 8:
                return cellNetwork8;
            case 9:
                return cellNetwork9;
            case 10:
                return cellNetwork10;
            case 11:
                return cellNetwork11;
            case 12:
                return cellNetwork12;
            case 13:
                return cellNetwork13;
            case 14:
                return cellNetwork14;
            case 15:
                return cellNetwork15;
            default:
                return null;
        }
    }
    */
}

@Entity
class CellNetwork {
    @ColumnInfo(name = "network_type")
    public int networkType;

    @ColumnInfo(name = "location_area_code")
    public int lac;

    @ColumnInfo(name = "cell_id")
    public int cid;

    @ColumnInfo(name = "network_id")
    public int nid;

    @ColumnInfo(name = "system_id")
    public int sid;

    @ColumnInfo(name = "basestation_id")
    public int bid;
}

@Entity(indices = {@Index(value = {"mac_address"}, unique = true)})
class WifiNetwork {
    @PrimaryKey
    @ColumnInfo(name = "mac_address")
    public String macAddress;

    @ColumnInfo(name = "rssi")
    public int rssi;
}