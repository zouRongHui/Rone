package org.rone.core.util;

/**
 * 地图相关工具
 * @author rone
 */
public class PlatUtils {

    /**
     * 通过经纬度来获取两地的直线距离
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 纬度2
     * @return
     */
    public static double getDistanceByLonLat(double lon1, double lat1, double lon2, double lat2) {
        // radius of earth
        double earthRadius = 6370693.5;

        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * Math.PI/180;
        ns1 = lat1 * Math.PI/180;
        ew2 = lon2 * Math.PI/180;
        ns2 = lat2 * Math.PI/180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > Math.PI) {
            dew = Math.PI*2 - dew;
        } else if (dew < -Math.PI) {
            dew = Math.PI*2 + dew;
        }
        // 东西方向长度(在纬度圈上的投影长度)
        dx = earthRadius * Math.cos(ns1) * dew;
        // 南北方向长度(在经度圈上的投影长度)
        dy = earthRadius * (ns1 - ns2);
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
}
