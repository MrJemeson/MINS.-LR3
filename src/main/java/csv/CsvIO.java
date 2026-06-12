package csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import storage.FileStorage;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvIO {
    private CsvIO() {}

    public static <T> List<T> readAll(Path path, FileStorage storage, CsvMapper<T> mapper) {
        String content;
        try {
            content = storage.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV: " + path, e);
        }

        if (content.isBlank()) {
            return List.of();
        }

        List<T> values = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new StringReader(content))) {
            String[] parts;
            boolean firstLine = true;
            while ((parts = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (parts.length == 0) {
                    continue;
                }
                values.add(mapper.parse(parts));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to parse CSV: " + path, e);
        }

        return values;
    }

    public static <T> void writeAll(Path path, FileStorage storage, CsvMapper<T> mapper, List<T> values) {
        StringBuilder sb = new StringBuilder();
        sb.append(mapper.headerLine()).append(System.lineSeparator());
        for (T value : values) {
            sb.append(mapper.toLine(value)).append(System.lineSeparator());
        }

        try {
            storage.writeString(path, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV: " + path, e);
        }
    }
}

