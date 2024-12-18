/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.icet.thogakade.controller;

import lk.icet.thogakade.model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import lk.icet.thogakade.db.DBConnection;
import lk.icet.thogakade.model.OrderDetail;


/**
 *
 * @author niroth
 */
public class ItemController {
    
    public static ArrayList<String> loadAllItemCodes() throws SQLException, ClassNotFoundException{
        ResultSet set = DBConnection.getInstance().getConnection().prepareStatement("SELECT code FROM Item").executeQuery();
        ArrayList<String> ids = new ArrayList<>();
        while (set.next()) {            
            ids.add(set.getString(1));
        }
        return ids;
    }

    public static boolean updateItemStock(ArrayList <OrderDetail> orderDetailList) throws ClassNotFoundException, SQLException{
        for (OrderDetail orderDetail : orderDetailList) {
            boolean isUpdate=updateItemStock(orderDetail);
            if(!isUpdate){
                return false;
            }
        }
        return true;
    }
    public static boolean updateItemStock(String itemCode, int qty) throws ClassNotFoundException, SQLException{
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("Update Item set QtyOnHand=qtyOnHand-? where code=?");
        stm.setObject(1,qty);
        stm.setObject(2, itemCode);
        return stm.executeUpdate()>0;
    }
    public static boolean updateItemStock(OrderDetail orderDetail) throws ClassNotFoundException, SQLException{
        return updateItemStock(orderDetail.getItemCode(),orderDetail.getQty());
    }
    public static boolean addItem(Item item) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");

        pstm.setObject(1, item.getCode());
        pstm.setObject(2, item.getDescription());
        pstm.setObject(3, item.getUnitPrice());
        pstm.setObject(4, item.getQtyOnHand());

        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;

    }
    public static ArrayList<Item> loadAllItems() throws ClassNotFoundException, SQLException {

        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM Item");

        ArrayList<Item> alItems = new ArrayList<>();

        while (rst.next()) {

            Item item = new Item();
            item.setCode(rst.getString(1));
            item.setDescription(rst.getString(2));
            item.setUnitPrice(rst.getDouble(3));
            item.setQtyOnHand(rst.getInt(4));

            alItems.add(item);
        }

        return alItems;

    }
    public static Item searchItem(String itemCode) throws ClassNotFoundException, SQLException {

        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM Item WHERE code='" + itemCode + "'");

        if (rst.next()) {
            Item item = new Item();
            item.setCode(rst.getString(1));
            item.setDescription(rst.getString(2));
            item.setUnitPrice(rst.getDouble(3));
            item.setQtyOnHand(rst.getInt(4));

            return item;
        } else {
            return null;
        }

    }
 
}
