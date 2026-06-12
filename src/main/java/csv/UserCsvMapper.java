package csv;

import object.User;

import java.util.StringJoiner;

public class UserCsvMapper implements CsvMapper<User> {
    @Override
    public String headerLine() {
        return "\"ID\",\"UserName\"";
    }

    @Override
    public User parse(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        int id = Integer.parseInt(parts[0].trim());
        String userName = parts[1].trim();
        return new User(id, userName);
    }

    @Override
    public String toLine(User user) {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(Integer.toString(user.getId()))
                .add(user.getUserName());
        return joiner.toString();
    }
}

