/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.icet.thogakade.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lk.icet.thogakade.db.DBConnection;
import lk.icet.thogakade.model.Order;


/**
 *
 * @author niroth
 */
public class OrderController {
    
    public static String getLastOrderId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT id FROM Orders ORDER BY id DESC LIMIT 1");
        return rst.next() ? rst.getString("id") : null;
    }
    public static boolean placeOrder(Order order) throws ClassNotFoundException, SQLException{
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("Insert into Orders values(?,?,?)");
        stm.setObject(1,order.getId());
        stm.setObject(2,order.getDate());
        stm.setObject(3,order.getCustomerId());
        boolean isAddedOrder=stm.executeUpdate()>0;
        if(isAddedOrder){
            boolean isAddOrderDetails=OrderDetailController.addOrderDetail(order.getOrderDetailList());
            if(isAddOrderDetails){
                boolean isUpdate=ItemController.updateItemStock(order.getOrderDetailList());
                if(isUpdate){
                    return true;
                }
            }
        }
        return false;
    }
    
}
