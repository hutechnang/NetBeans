package ftpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class frmHienThiServer extends javax.swing.JFrame {
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableDSUser;
    private javax.swing.JButton btnThoat;
    
    public frmHienThiServer() {
        initComponents();
        // Kết nối CSDL hiển thị danh sách tài khoản
        try {
            DBAccess acc = new DBAccess();
            String query = "select * from taikhoan";
            ResultSet rs;
            rs = acc.Query(query);
            if (rs == null) {
                System.out.println("rong");
                return;
            }
            Vector row;
            DefaultTableModel dm;
            dm = (DefaultTableModel) this.tableDSUser.getModel();
            while (rs.next()) {
                String user = rs.getString("username");
                String pass = rs.getString("password");
                String path = rs.getString("path");
                int per = rs.getInt("per");
                row = new Vector();
                row.add(user);
                row.add(pass);
                row.add(path);
                row.add(per);
                dm.addRow(row);
            }
            this.validate();
            this.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDSUser = new javax.swing.JTable();
        btnThoat = new javax.swing.JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Hiển thị tài khoản");
        
        tableDSUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Username", "Password", "Path", "Permission"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableDSUser);
        
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnThoat)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThoat)
                .addContainerGap())
        );
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }
}

