package com.farkas.familymealmate.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvReaderUtil {

    public static <T> List<T> readCsvFile(String filePath, String delimiter, Function<String[], T> rowMapper) {
        Resource resource = new ClassPathResource(filePath);

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            return reader.lines().filter(
                            line -> !line.trim().isEmpty())
                    .skip(1L)
                    .map(line -> line.split(delimiter))
                    .map(rowMapper)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }

}
