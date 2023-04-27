package com.example.smartcity.model.DAO;

import com.example.smartcity.model.Bean.BookingBean;
import com.example.smartcity.model.Bean.UserBean;
import com.example.smartcity.service.CommandPrezzo.VeicoliEnum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static boolean controllaEmail(String email){
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = DatabaseConnection.getInstance().getConnection().prepareStatement( "SELECT email " +
                    "FROM Utenti " +
                    "WHERE email = (?)");
            stmt.setString(1, email);
            result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (stmt!=null)
                    stmt.close();

                if ( result != null ) {
                    result.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean controllaPassword(String email, String password){
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT email, password " +
                    "FROM Utenti " +
                    "WHERE email = (?) AND password = (?)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (stmt!=null)
                    stmt.close();

                if ( result != null ) {
                    result.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static UserBean getUserBean(String email){
        UserBean userBean = new UserBean();
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * " +
                    "FROM Utenti " +
                    "WHERE email = (?) ");
            stmt.setString(1, email);
            result = stmt.executeQuery();
            if (result.next()) {
                userBean.setNome(result.getString("nome"));
                userBean.setCognome(result.getString("cognome"));
                userBean.setEmail(result.getString("email"));
                userBean.setPassword(result.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (stmt!=null)
                    stmt.close();

                if ( result != null ) {
                    result.close();
                }
            }
            catch (SQLException e) {

                e.printStackTrace();
            }
        }
        return userBean;
    }


    public static boolean addRegistrazione(UserBean userBean) {
        PreparedStatement stmt = null;
        PreparedStatement query = null;
        ResultSet result = null;
        try {
            stmt = DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT email " +
                    "FROM Utenti " +
                    "WHERE email = (?) ");
            stmt.setString(1, userBean.getEmail());
            result = stmt.executeQuery();
            if (result.next()) {
                return false;
            } else {
                query = DatabaseConnection.getInstance().getConnection().prepareStatement("INSERT INTO Utenti (nome, cognome, email, password) " +
                        "VALUES(?, ?, ?, ?)");
                query.setString(1, userBean.getNome());
                query.setString(2, userBean.getCognome());
                query.setString(3, userBean.getEmail());
                query.setString(4, userBean.getPassword());
                query.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (stmt!=null)
                    stmt.close();

                if ( result != null ) {
                    result.close();
                }

                if ( query != null ){
                    query.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static List<UserBean> getListUsers(){
        PreparedStatement stmt = null;
        ResultSet result = null;
        List<UserBean> list = new ArrayList<UserBean>();
        try {
            stmt = DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * " +
                                                                                         "FROM Utenti " +
                                                                                         "WHERE email != 'admin@admin.com'");
            result = stmt.executeQuery();
            list = new ArrayList<>();
            while (result.next()) {
                UserBean userBean = new UserBean();
                userBean.setNome(result.getString(1));
                userBean.setCognome(result.getString(2));
                userBean.setEmail(result.getString(3));
                userBean.setPassword(result.getString(4));
                list.add(userBean);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (stmt!=null)
                    stmt.close();

                if ( result != null ) {
                    result.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

}