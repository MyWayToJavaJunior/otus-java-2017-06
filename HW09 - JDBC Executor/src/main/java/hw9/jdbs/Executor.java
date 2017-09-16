package hw9.jdbs;

import hw9.dataset.DataSet;
import hw9.util.NamePolicyHelper;
import hw9.util.ReflectionHelper;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public <T extends DataSet> void save(T dataSet) throws SQLException {
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (Field field : ReflectionHelper.getFieldsAnnotatedWith(dataSet.getClass(), Column.class)) {

            Object value = ReflectionHelper.getFieldValue(dataSet, field.getName());
            if (!field.isSynthetic() && value != null) {
                columns.add(field.getName());
                values.add(formatValue(value));
            }
        }

        if (dataSet.getId() != 0) {
            columns.add("id");
            values.add("" + dataSet.getId());
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(String.format("INSERT INTO %s (%s) VALUES (%s)",
                    getTableName(dataSet.getClass()),
                    String.join(", ", columns),
                    String.join(", ", values)
            ));
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(String.format("SELECT * FROM %s WHERE id = %d",
                    getTableName(clazz),
                    id
            ));

            ResultSet resultSet = stmt.getResultSet();
            if (resultSet.isLast()) return null;

            resultSet.next();

            T obj = clazz.newInstance();
            for (Field field : ReflectionHelper.getFieldsAnnotatedWith(clazz, Column.class)) {
                ReflectionHelper.setFieldValue(obj, field.getName(), resultSet.getObject(field.getName()));
            }
            return obj;
        }
    }

    private String getTableName(Class clazz) {
        if (clazz.isAnnotationPresent(Table.class)) return ((Table) clazz.getAnnotation(Table.class)).name();
        return NamePolicyHelper.get(clazz.getName());
    }

    private String formatValue(Object value) {
        Class valueClazz = value.getClass();
        if (valueClazz.equals(String.class)) return "'" + value + "'";
//        else if (valueClazz.equals(Date.class)) return ...;

        return value.toString();
    }
}
