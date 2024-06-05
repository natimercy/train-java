package com.self.geometry.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.self.geometry.jackson.util.GeomUtil;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

/**
 * 空间数据Jackson序列化类
 */
public class GeometryWKTSerializer extends JsonSerializer<Geometry> {

    @Override
    public void serialize(Geometry geometry, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(GeomUtil.toWKT(geometry));
    }

    @Override
    public void serializeWithType(Geometry value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        this.serialize(value, gen, serializers);
    }
}
