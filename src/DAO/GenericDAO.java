package DAO;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    private static GenericDAO<?> instance;
    private final String url = "jdbc:mysql://localhost:3306/testdb";
    private final String user = "root";
    private final String password = "";

    private GenericDAO() {}

    public static synchronized <T> GenericDAO<T> getInstance() {
        if (instance == null) {
            instance = new GenericDAO<>();
        }
        @SuppressWarnings("unchecked")
        GenericDAO<T> typedInstance = (GenericDAO<T>) instance;
        return typedInstance;
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }

    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        }
        return results;
    }
}

