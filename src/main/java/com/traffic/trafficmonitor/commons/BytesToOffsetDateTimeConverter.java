package com.traffic.trafficmonitor.commons;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.data.convert.ReadingConverter;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


@Component
@ReadingConverter
public class BytesToOffsetDateTimeConverter implements Converter<byte[], OffsetDateTime> {
    @Override
    public OffsetDateTime convert(final byte[] source) {
        return OffsetDateTime.parse(new String(source), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}

