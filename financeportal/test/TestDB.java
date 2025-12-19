package com.financeportal.test;

import com.financeportal.dao.DBConnection;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection()) {
            System.out.println("Connected: " + (c != null && !c.isClosed()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
