package com.self.geometry.jackson.util;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Geometry 工具类
 * <p><a href="https://www.cnblogs.com/qdhotel/p/4887032.html">WKT和WKB表现形式</a></p>
 * <p><a href="https://blog.csdn.net/u010945668/article/details/119910970">Geometry相关方法</a></p>
 * <p>在Mysql中，Geometry类型的数据可以通过ST_AsWKT()函数转换为WKT格式的字符串。</p>
 * <p>默认的经纬度顺序是：纬度在前，经度在后，可以通过axis-order参数来指定经纬度的顺序。</p>
 * <p>SELECT id, ST_ASWKT(location, 'axis-order=long-lat') FROM geom; </p>
 */
public class GeomUtil {

    /**
     * 坐标系ID (空间引用标识符，Spatial Reference ID)
     * <p>WGS 84(GPS)：4326  ---地理坐标</p>
     * <p>Web Mercator(墨卡托)：3857  ---投影坐标</p>
     * <p>CGCS2000 (北斗系统-大地2000)：4490  ---地理坐标</p>
     */
    public static final Integer SRID = 4326;

    /**
     * MySQL使用 {@link ByteOrder#LITTLE_ENDIAN} 存储
     * <p>读写时都需要使用 {@link ByteOrder#LITTLE_ENDIAN} </p>
     */
    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    private static final int BYTE_ORDER_INT = ByteOrderValues.LITTLE_ENDIAN;

    public static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);

    public static final WKTReader wktReader = new WKTReader(geometryFactory);

    public static final WKTWriter wktWriter = new WKTWriter();

    public static final WKBReader wkbReader = new WKBReader();

    public static final WKBWriter wkbWriter = new WKBWriter(2, BYTE_ORDER_INT);

    private GeomUtil() {
    }

    /**
     * WKT转Geometry
     */
    public static Geometry toGeom(String wkt) {
        try {
            return wktReader.read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException("WKT -> Geometry ERR" + e.getMessage());
        }
    }

    /**
     * Geometry转WKT
     */
    public static String toWKT(Geometry geometry) {
        return wktWriter.write(geometry);
    }

    /**
     * MySQL_WKB -> Geometry
     **/
    public static Geometry fromMysqlWkb(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        byte[] geomBytes = ByteBuffer.allocate(bytes.length - 4)
                .order(BYTE_ORDER)
                .put(bytes, 4, bytes.length - 4)
                .array();
        try {
            Geometry geometry = wkbReader.read(geomBytes);
            geometry.setSRID(readSRID(bytes));
            return geometry;
        } catch (Exception e) {
            throw new RuntimeException("WKB -> Geometry ERR" + e.getMessage());
        }
    }

    /**
     * 从wkb中读取srid
     *
     * @see <a href="http://www.dev-garden.org/2011/11/27/loading-mysql-spatial-data-with-jdbc-and-jts-wkbreader/">参考</a>
     */
    private static int readSRID(byte[] bytes) {
        // 大小端判断
        // ByteOrder byteOrder = bytes[4] == 0x00 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN
        return ByteOrderValues.getInt(bytes, BYTE_ORDER_INT);
    }

    /**
     * Geometry -> MySQL_WKB
     **/
    public static byte[] toMysqlWkb(Geometry geometry) {
        if (geometry == null) {
            return new byte[0];
        }
        if (geometry.getSRID() == 0) {
            geometry.setSRID(SRID);
        }
        byte[] geomBytes = wkbWriter.write(geometry);
        return ByteBuffer.allocate(geomBytes.length + 4)
                .order(BYTE_ORDER)
                .putInt(geometry.getSRID())
                .put(geomBytes)
                .array();
    }

    /**
     * 地球半径，单位m
     */
    private static final double EARTH_RADIUS = 6378137;

    /**
     * 根据经纬度，计算两点间的距离
     *
     * @param left  第一个点
     * @param right 第二个点
     * @return 返回距离，单位m
     */
    public static double distanceSphere(Point left, Point right) {
        if (left == null || right == null) {
            return 0;
        }
        // 纬度
        double lat1 = Math.toRadians(left.getY());
        double lat2 = Math.toRadians(right.getY());
        // 经度
        double lng1 = Math.toRadians(left.getX());
        double lng2 = Math.toRadians(right.getX());
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 米
        return s * EARTH_RADIUS;
    }

    /**
     * 根据经纬度，计算两点间的距离
     *
     * @param left  第一个点
     * @param right 第二个点
     * @return 返回距离，单位m
     */
    public static double distanceSphere(Geometry left, Geometry right) {
        if (left == null || right == null) {
            return 0d;
        }

        if (left instanceof Point && right instanceof Point) {
            Point first = (Point) left;
            Point second = (Point) right;
            return distanceSphere(first, second);
        }

        return 0d;
    }

}
