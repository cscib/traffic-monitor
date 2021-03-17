package com.traffic.trafficmonitor.config;

import com.univocity.parsers.csv.CsvParserSettings;
import net.logstash.logback.encoder.org.apache.commons.lang3.builder.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfig {

    @Value("${parser.settings.headerExtractionEnabled}")
    private boolean headerEnabled;

    @Value("${parser.settings.lineSeparatorDetectionEnabled}")
    private boolean lineSeperatorEnabled;

    @Value("${parser.settings.skipEmptyLines}")
    private boolean skipEmptyLines;

    @Value("${parser.settings.delimiter}")
    private String delimiter;


    @Bean
    public CsvParserSettings csvParserSettings() {

        return ((Builder<CsvParserSettings>) () -> {
            CsvParserSettings parserSettings = new CsvParserSettings();
            parserSettings.setHeaderExtractionEnabled(headerEnabled);
            parserSettings.setLineSeparatorDetectionEnabled(lineSeperatorEnabled);
            parserSettings.setSkipEmptyLines(skipEmptyLines);
            parserSettings.getFormat().setDelimiter(delimiter);
            return parserSettings;
        }).build();
    }

}
