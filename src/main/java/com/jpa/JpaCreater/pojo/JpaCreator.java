package com.jpa.JpaCreater.pojo;

import lombok.Data;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class JpaCreator {
    DataSource dataSource;
    FileWriter myWriter = new FileWriter("jpa.txt");
    String query = "SELECT * FROM `roles` LIMIT 1";


    public JpaCreator(DataSource dataSource) throws IOException {

        this.dataSource=dataSource;
    }

    public Connection getConntion() throws SQLException {
        return dataSource.getConnection();
    }
    public void printColumns(Connection conn) throws SQLException, IOException {

        myWriter.write("");

        Statement statement= conn.createStatement();
        ResultSet rs = statement.executeQuery(query);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while (rs.next()) {

            for (int i = 1; i <= columnsNumber; i++) {
//                String columnValue = rs.getString(i);
                System.out.println(rsmd.getColumnName(i) + " | " + rsmd.getColumnTypeName(i)+ " | " + changeToJavaDataTypes(rsmd.getColumnTypeName(i)) );
                myWriter.append((rsmd.getColumnName(i) + " | " + rsmd.getColumnTypeName(i)+ " | " + changeToJavaDataTypes(rsmd.getColumnTypeName(i))));
                myWriter.append("\n");

            }
        }


        myWriter.close();

    }
    public void writeJpaCoulmns(Connection conn) throws SQLException, IOException {

        myWriter.write("");
        Statement statement= conn.createStatement();
        ResultSet rs = statement.executeQuery(query);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while (rs.next()) {

            for (int i = 1; i <= columnsNumber; i++) {


                String columnValue = rs.getString(i);

                System.out.println(createColumnAnnotation(rsmd.getColumnName(i)));
                myWriter.append(createColumnAnnotation(rsmd.getColumnName(i)));
                myWriter.append("\n");
                System.out.println(createColumnValue(rsmd.getColumnName(i), rsmd.getColumnTypeName(i) ));
                myWriter.append(createColumnValue(rsmd.getColumnName(i), rsmd.getColumnTypeName(i) ));
                myWriter.append("\n");
                System.out.println("");
                myWriter.append("\n");
            }
        }
        myWriter.close();
    }
    public String createColumnValue(String columnName,String columnTypeName){

        return "private "+changeToJavaDataTypes(columnTypeName) +" "+ changeToJavaVariableName(columnName)  ;
    }
    public String createColumnAnnotation(String columnName){
        return "@Column(name = \""+columnName+"\")";
    }

    public String changeToJavaVariableName(String columnName){
        String pattern = "_[a-z]";  //Capturing the character after underscore

        String modifiedString = columnName.toLowerCase();
        Matcher matcher = Pattern.compile(pattern).matcher(modifiedString);
        if(matcher.find()){
            String matchedChar = matcher.group(0);
            modifiedString = modifiedString.replaceAll(pattern, "_" + matchedChar.toUpperCase());
        }

        modifiedString = modifiedString.replaceAll("_", "");
        return modifiedString;
    }

    public String changeToJavaDataTypes(String columnTypeName){
        if (columnTypeName == "VARCHAR" || columnTypeName == "VARCHAR2"){
            return "String";
        }
        if (columnTypeName == "INT" || columnTypeName == "NUMBER"){
            return "Integer";
        }
        if (columnTypeName == "DATETIME" || columnTypeName == "DATE" || columnTypeName == "TIMESTAMP"){
            return "Timestamp";
        }
        if (columnTypeName == "Long"){
            return "Long";
        }
        if (columnTypeName == "FLOAT"){
            return "Float";
        }
        return "error";
    }



}
