/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parcial;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

public class DB {
    public Connection con;
    
    //metodo constructor
    public DB() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        String server = "jdbc:mysql://localhost:3306/";
        String database = "ventas";
        String user = "root";
        String password = "";
        this.con = DriverManager.getConnection(server + database, user, password);
    }

    public boolean isOpen(){
        return this.con != null;
    }
    
    
    public ResultSet getData() throws SQLException{
        if(this.isOpen()){
            String sql = "select * from producto";
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs;
        }
        else{
            return null;
        }
    }
    
    public void Insert(String codigo, String producto, String cantidad ,String precio) throws SQLException{
        double PRECIO = Double.valueOf(precio);
        int CANTIDAD = Integer.valueOf(cantidad);
        PRECIO = PRECIO * CANTIDAD;
        String sql = "insert into producto(Codigo, Nombre, Cantidad, Precio)values(?, ?, ?, ?)";
        PreparedStatement cursor = this.con.prepareCall(sql);
        cursor.setString(1, codigo);
        cursor.setString(2, producto);
        cursor.setInt(3, CANTIDAD);
        cursor.setDouble(4, PRECIO);
        int result = cursor.executeUpdate();
        if(result > 0){
            System.out.println("Se ha insertado el producto correctamente :D");
        }else{
            System.out.println("Error al insertar producto en la base de datos :C");
        }
    }
    
    
    public boolean exist(String nombre) throws SQLException{
        if(this.isOpen()){
            String sql = "select * from producto where Nombre=?";
            PreparedStatement preparedStmt = this.con.prepareStatement(sql);
            preparedStmt.setString(1,nombre);
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
                    
        }
        return false;
    }
    
    
    public void InsertExist(String codigo, String producto, String cantidad,String precio)
    {
        try {
            if(this.exist(producto)){
                this.Actualizar(producto, cantidad, precio);
            }
            else{
                this.Insert(codigo, producto, cantidad, precio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getCant(String nombre) throws SQLException{
        if(this.isOpen()){
            String sql = "select * from producto where Nombre=?";
            PreparedStatement preparedStmt = this.con.prepareStatement(sql);
            preparedStmt.setString(1,nombre);
            ResultSet result = preparedStmt.executeQuery();
            if(result.next()){
                return result.getInt(3);
            }
            else{
                return 0;
            }
        }
        else{
            return 0;
        }
    }
    
    public double getPrice(String nombre) throws SQLException{
        if(this.isOpen()){
            String sql = "select * from producto where Nombre=?";
            PreparedStatement preparedStmt = this.con.prepareStatement(sql);
            preparedStmt.setString(1,nombre);
            ResultSet result = preparedStmt.executeQuery();
            if(result.next()){
                return result.getDouble(4);
            }
            else{
                return 0;
            }
        }
        else{
            return 0;
        }
    }
    
    
    
        
    public void Actualizar(String producto, String cantidad, String precio) throws SQLException{
        double PRECIO = Double.valueOf(precio);
        int CANTIDAD = Integer.valueOf(cantidad);
        if(this.getCant(producto) != 0 && this.getPrice(producto) != 0){
            int current_cantidad = this.getCant(producto);
            double current_precio = this.getPrice(producto);
            CANTIDAD = CANTIDAD + current_cantidad;
            PRECIO = PRECIO*current_cantidad;
            PRECIO = PRECIO + current_precio;
            String sql = "update producto set Cantidad=?, Precio=? where Nombre=?";
            PreparedStatement cursor = this.con.prepareCall(sql);
            cursor.setInt(1, CANTIDAD);
            cursor.setDouble(2, PRECIO);
            cursor.setString(3, producto);
            int result = cursor.executeUpdate();
            if(result > 0){
                System.out.println("Se ha insertado el producto correctamente :D");
            }else{
                System.out.println("Error al insertar producto en la base de datos :C");
            }
        }
    }
    
    public void ActualizarProducto(String producto, String cantidad, String precio) throws SQLException{
        double PRECIO = Double.valueOf(precio);
        int CANTIDAD = Integer.valueOf(cantidad);
        PRECIO = CANTIDAD * PRECIO;
        String sql = "update producto set Cantidad=?, Precio=? where Codigo=?";
        PreparedStatement cursor = this.con.prepareCall(sql);
        cursor.setInt(1, CANTIDAD);
        cursor.setDouble(2, PRECIO);
        cursor.setString(3, producto);
        int result = cursor.executeUpdate();
        if(result > 0){
            JOptionPane.showMessageDialog(null, "Se ha modificado el producto correctamente :D");
        }else{
            JOptionPane.showMessageDialog(null,"Error al insertar producto en la base de datos :C");
        }
    }
    
    
    
    public void Eliminar(String codigo) throws SQLException{
        try{
            String query = "delete from producto where Codigo=?";
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setString(1, codigo);
            preparedStmt.execute();
        }catch(Exception e){
            showMessageDialog(null, e.getMessage());
        }
        
      
    }
    
    
    public void CloseConnection() throws SQLException{
        this.con.close();
    }


}

