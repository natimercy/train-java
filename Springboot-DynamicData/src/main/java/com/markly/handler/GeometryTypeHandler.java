package com.markly.handler;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * GeometryTypeHandler
 *
 * @author natimercy
 * @since 1.0.0
 */
@MappedTypes(value = {String.class})
@MappedJdbcTypes(value = {JdbcType.ARRAY})
public class GeometryTypeHandler extends BaseTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        WKTReader wktReader = SpringUtil.getBean(WKTReader.class);
        try {
            Geometry pointGeo = wktReader.read(parameter.toString());
            WKBWriter wkbWriter = SpringUtil.getBean(WKBWriter.class);
            byte[] writer = wkbWriter.write(pointGeo);
            byte[] wkb = new byte[writer.length + 4];
            ByteOrderValues.putInt(4326, wkb, ByteOrderValues.LITTLE_ENDIAN);
            System.arraycopy(writer, 0, wkb, 4, writer.length);
            ps.setBytes(i, wkb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            /**
             * 从ResultSet中读取二进制转换为SqlServer的Geometry对象
             * 使用jts的WKTReader将wkt文本转成jts的Geometryd对象
             */
            WKBReader wkbReader = SpringUtil.getBean(WKBReader.class);
            byte[] bytes = rs.getBytes(columnName);
            byte[] bytesN = new byte[bytes.length - 4];
            System.arraycopy(bytes, 4, bytesN, 0, bytes.length - 4);
            Geometry geometry = wkbReader.read(bytesN);
            return geometry.toText();
        } catch (Exception ignore) {

        }
        return null;
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
