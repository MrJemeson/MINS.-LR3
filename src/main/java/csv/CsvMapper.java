package csv;

public interface CsvMapper<T> {
    String headerLine();

    T parse(String[] parts);

    String toLine(T value);
}

