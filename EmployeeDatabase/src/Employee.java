import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JTable table1;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtid;
    private JScrollPane table_1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;
    public void connect(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost/employeedatabase1", "root", "");
            System.out.println("Success");
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    void table_load(){
        try{
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empname, salary, mobile;
                empname = txtName.getText();
                salary = txtSalary.getText();
                mobile = txtMobile.getText();

                try{
                    pst = con.prepareStatement("insert into employee(empname, salary, mobile) values (?,?,?)");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.executeUpdate();

                    table_load();

                    //Now we need to clear all the text fields after it has been added to the database

                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();

                    JOptionPane.showMessageDialog(null, "Record Added!");
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String empid = txtid.getText();
                    pst = con.prepareStatement("select empname, salary, mobile from employee where id = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true){
                        String empname = rs.getString(1);
                        String salary = rs.getString(2);
                        String mobile = rs.getString(3);

                        txtName.setText(empname);
                        txtSalary.setText(salary);
                        txtMobile.setText(mobile);

                        JOptionPane.showMessageDialog(null, "Record found!");
                    }
                    else {
                        txtName.setText("");
                        txtSalary.setText("");
                        txtMobile.setText("");
                        JOptionPane.showMessageDialog(null, "Record not found!");
                    }
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empid, empname, salary, mobile;
                empname = txtName.getText();
                salary = txtSalary.getText();
                mobile = txtMobile.getText();
                empid = txtid.getText();

                try{
                    pst = con.prepareStatement("update employee set empname = ?, salary = ?, mobile=? where id=?");
                    pst.setString(1,empname);
                    pst.setString(2,salary);
                    pst.setString(3,mobile);
                    pst.setString(4,empid);

                    pst.executeUpdate();

                    table_load();

                    txtName.setText("");
                    txtName.requestFocus();
                    txtMobile.setText("");
                    txtSalary.setText("");
                    txtid.setText("");

                    JOptionPane.showMessageDialog(null, "Record updated!");
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empid;
                empid = txtid.getText();

                try{
                    pst = con.prepareStatement("delete from employee where id = ?");
                    pst.setString(1,empid);
                    pst.executeUpdate();

                    table_load();

                    txtName.setText("");
                    txtName.requestFocus();
                    txtMobile.setText("");
                    txtSalary.setText("");
                    txtid.setText("");

                    JOptionPane.showMessageDialog(null, "Record deleted!");
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }
}
