package org.lia.managers;


import org.lia.models.Coordinates;
import org.lia.models.Organization;
import org.lia.models.Product;
import org.lia.models.UnitOfMeasure;

import java.sql.*;


public class SqlManager {

    String username;
    String password;
    String url = "jdbc:postgresql://localhost:5432/studs";
    Connection connection;

    public SqlManager(String username, String password) {
        this.username = username;
        this.password = password;
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public CollectionManager getCollectionFromDatabase() {
        CollectionManager collection = new CollectionManager();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCT;");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                long id = result.getInt("id");
                String name = result.getString("name");
                java.sql.Date creation_date = result.getDate("creation_date");
                Integer price = result.getInt("price");
                String partNumber = result.getString("part_number");
                Integer manufactureCost = result.getInt("manufacture_cost");
                UnitOfMeasure unitOfMeasure = null;
                long userId = result.getLong("creator");
                try {
                    unitOfMeasure = UnitOfMeasure.valueOf(result.getString("unit_of_measure"));
                } catch (NullPointerException ignored) {}
                long x = result.getLong("coord_x");
                double y = result.getDouble("coord_y");
                String org_name = result.getString("org_name");
                String org_fullname = result.getString("org_fullname");
                Integer org_emplyeesCount = result.getInt("org_employees_count");
                Organization org = new Organization(org_name, org_fullname, org_emplyeesCount);
                Coordinates coords = new Coordinates(x, y);
                Product product = new Product(id, name, coords, price, partNumber, manufactureCost, unitOfMeasure, org, creation_date);
                product.setUserId(userId);
                collection.addToCollection(product);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return collection;
    }

    public long addElement(Product product) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Product(name, creation_date, price, part_number, manufacture_cost, coord_x, coord_y, org_name, org_fullname, org_employees_count, creator)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id"
        ); // TODO: починить unit of measure
        statement.setString(1, product.getName());
        statement.setDate(2, product.getCreationDate());
        statement.setInt(3, product.getPrice());
        statement.setString(4, product.getPartNumber());
        statement.setInt(5, product.getManufactureCost());
        statement.setLong(6, product.getCoordinates().getX());
        statement.setDouble(7, product.getCoordinates().getY());
        statement.setString(8, product.getManufacturer().getName());
        statement.setString(9, product.getManufacturer().getFullName());
        if (product.getManufacturer().getEmployeesCount() != null) {
            statement.setInt(10, product.getManufacturer().getEmployeesCount());
        } else {
            statement.setNull(10, Types.INTEGER);
        }
        statement.setLong(11, product.getUserId());
        // statement.setString(12, product.getUnitOfMeasure().toString().toUpperCase());
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt("id");
    }

    public void updateElement(Product product) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Product " +
                        "SET name=?, creation_date=?, price=?, part_number=?, manufacture_cost=?, coord_x=?, coord_y=?, org_name=?, org_fullname=?, org_employees_count=?, creator=? " +
                        "WHERE id=?"
        ); // TODO: починить unit of measure
        statement.setString(1, product.getName());
        statement.setDate(2, product.getCreationDate());
        statement.setInt(3, product.getPrice());
        statement.setString(4, product.getPartNumber());
        statement.setInt(5, product.getManufactureCost());
        statement.setLong(6, product.getCoordinates().getX());
        statement.setDouble(7, product.getCoordinates().getY());
        statement.setString(8, product.getManufacturer().getName());
        statement.setString(9, product.getManufacturer().getFullName());
        if (product.getManufacturer().getEmployeesCount() != null) {
            statement.setInt(10, product.getManufacturer().getEmployeesCount());
        } else {
            statement.setNull(10, Types.INTEGER);
        }
        statement.setLong(11, product.getUserId());
        statement.setLong(12, product.getId());
        // statement.setString(12, product.getUnitOfMeasure().toString().toUpperCase());
        statement.execute();
    }

    public long addUser(String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Users(username, password)" +
                        "VALUES (?, ?) RETURNING id"
        );
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt("id");
    }

    public long checkUser(String login, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id FROM USERS WHERE username=? and password=?"
            );
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getLong("id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteElementsByUserId(long userId) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM Product WHERE creator=?"
            );
            statement.setLong(1, userId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int deleteElementById(long productId, long userId) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM Product WHERE id=? AND creator=?"
            );
            statement.setLong(1, productId);
            statement.setLong(2, userId);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
