package com.self.geometry.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.self.geometry.jackson.util.GeomUtil;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

/**
 * 空间数据Jackson反序列化类
 *
 * @param <T>
 */
public class GeometryWKTDeSerializer<T extends Geometry> extends JsonDeserializer<T> {

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return (T) GeomUtil.toGeom(jsonParser.getText());
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
            throws IOException {
        return this.deserialize(p, ctxt);
    }
}
