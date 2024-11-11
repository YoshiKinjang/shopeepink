import javax.swing.*;
import javax.swing.border.*;

import database.DatabaseConnection;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;
import java.awt.*;
import java.util.Timer;
import org.xml.sax.ErrorHandler;

public class RegisterPage extends JPanel {

    private MainPage mainPage;

    JPanel logoCon = new JPanel();
    JPanel formCon = new JPanel();
    JPanel pH = new JPanel();
    JPanel fH = new JPanel();
    JPanel buatPanel = new JPanel();
    ImageIcon imageIcon = new ImageIcon("img/logo.png");
    
    JTextField usernameTf = new JTextField();
    JTextField emailTf = new JTextField();
    JPasswordField passTf = new JPasswordField();

    JButton daftarBtn = new JButton("Daftar");

    JLabel buatLbl = new JLabel("Masuk");
    JLabel imageLabel = new JLabel(imageIcon);
    static JLabel errorHandler = new JLabel("");

    GridBagConstraints gbc;
    GridBagLayout thelayout;


    RegisterPage(MainPage mainPage) {

        this.mainPage = mainPage; // Initialize the main page reference
        setLayout(new CardLayout(50, 50));
        setBackground(Color.pink);

        // panel helper for parent
        pH.setLayout(new GridLayout());


        logoCon.setLayout(new CardLayout());
        logoCon.setBackground(Color.pink);
        
        logoCon.add(imageLabel);

        ///fh layout config
        gbc = new GridBagConstraints();
        thelayout = new GridBagLayout();

        formCon.setLayout(new CardLayout(50,50));
        formCon.setBackground(Color.white);
        fH.setLayout(thelayout);
        fH.setBackground(Color.white);
        ///populating fh component
        
        ///Setting masuk label
        JLabel masukAkun = new JLabel("Daftar akun");
        masukAkun.setFont(new Font("Arial", Font.PLAIN, 32));

        ///Setting username label
        JLabel usernameLbl = new JLabel("Username");
        usernameLbl.setFont(new Font("Arial", Font.PLAIN, 16));

        ///Setting email label
        JLabel emailLbl = new JLabel("Email");
        emailLbl.setFont(new Font("Arial", Font.PLAIN, 16));

        ///Setting password label
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Arial", Font.PLAIN, 16));

        //setting error handler
        errorHandler.setFont(new Font("Arial", Font.PLAIN, 14));
        errorHandler.setForeground(Color.RED);

        ///Setting buat akun label
        buatPanel.setLayout(new FlowLayout());
        buatPanel.setBackground(Color.white);

        JLabel buatPert = new JLabel("Sudah punya akun? ");
        buatPert.setFont(new Font("Arial", Font.PLAIN, 16));
        buatLbl.setFont(new Font("Arial", Font.PLAIN, 16));
        buatLbl.setForeground(Color.blue);
        buatPanel.add(buatPert);
        buatPanel.add(buatLbl);


        Border coloredBorder = BorderFactory.createLineBorder(Color.lightGray, 2);
        Border paddingBorder = new EmptyBorder(5, 10, 5, 10);

        addFocusListenerToTextField(usernameTf, usernameLbl);

        addFocusListenerToTextField(emailTf, emailLbl);

        addFocusListenerToTextField(passTf, passLbl);

        daftarBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        daftarBtn.setBackground(Color.pink);
        daftarBtn.setBorder(new EmptyBorder(10, 10, 10,10));
        

        ///adding component to fh layout
        gbc.fill = GridBagConstraints.BOTH;  
        gbc.weightx = 0.5;  
        gbc.gridx = 0;  
        gbc.gridy = 0; 
        gbc.insets = new Insets(20, 0, 50, 0);

        fH.add(masukAkun, gbc);
 
        gbc.gridy = 1; 
        gbc.insets = new Insets(0, 0, 5, 0);
        
        fH.add(usernameLbl, gbc);

        gbc.gridy = 2;
        fH.add(usernameTf, gbc);
  
        gbc.gridy = 3; 
        gbc.insets = new Insets(10, 0, 5, 0);
        
        fH.add(emailLbl, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        fH.add(emailTf, gbc);

        gbc.gridy = 5; 
        gbc.weighty = 0; 
        gbc.insets = new Insets(10, 0, 5, 0);

        fH.add(passLbl,gbc);

        gbc.gridy = 6; 
        gbc.insets = new Insets(0, 0, 0, 0);
        fH.add(passTf, gbc);

        gbc.gridy = 7; 
        gbc.insets = new Insets(10, 0, 0, 0);
        fH.add(errorHandler, gbc);

        gbc.gridy = 8; 
        gbc.weighty = 0; 
        gbc.insets = new Insets(20, 0, 20, 0);
        fH.add(daftarBtn, gbc);

        gbc.gridy = 9; 
        fH.add(buatPanel, gbc);

        formCon.add(fH);        

        pH.add(logoCon);
        pH.add(formCon);

        add(pH);

        switchLogin();
        registerAction();
    }

    public void switchLogin() {
        buatLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainPage.switchToLoginPage();
            }
        });
    }

    private void registerAction() {
        daftarBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameTf.getText();
                String user_email = emailTf.getText();
                String user_pasword = new String(passTf.getPassword());

                if(registerSystem(username, user_email, user_pasword)) {

                    JDialog successDialog = new JDialog(new MainPage(), "Daftar akun", true);
                    successDialog.setSize(700, 300);
                    successDialog.setLayout(new CardLayout(50,50));
                    successDialog.setLocationRelativeTo(new MainPage());

                    JPanel pp = new JPanel();
                    pp.setLayout(new BorderLayout());
                    successDialog.add(pp);

                    JLabel label = new JLabel("Akun berhasil didaftarkan");
                    label.setFont(new Font("Arial", Font.BOLD, 24));
                    pp.add(label, BorderLayout.CENTER);

                    JButton closeButton = new JButton("Lanjut login");
                    pp.add(closeButton, BorderLayout.SOUTH);
                    
                    closeButton.addActionListener(ev -> {
                        successDialog.dispose();
                        mainPage.switchToLoginPage();
                    });

                    successDialog.setVisible(true);

                } else {
                    ///remove error message
                    System.out.println("nooo");
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            errorHandler.setText("");
                        }
                    };
                    timer.schedule(task, 3000);
                }
            }
        });
        
    }

    private static boolean registerSystem(String username, String email, String password) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "INSERT INTO shopee_pink.customer (username,email,password)" +
                        "VALUES (?,?,?)";

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            int rowsAffrected = pstmt.executeUpdate();

            return rowsAffrected > 0;

        } catch (SQLException e) {
            errorHandler.setText(e.getLocalizedMessage());
        }

        return false;
    }


    private void addFocusListenerToTextField(JTextField textField, JLabel label) {
        Border defaultBorder = BorderFactory.createLineBorder(Color.lightGray, 2);
        Border focusedBorder = BorderFactory.createLineBorder(Color.pink, 2);

        Border paddingBorder = new EmptyBorder(5, 10, 5, 10);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(new CompoundBorder(defaultBorder, paddingBorder));

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                label.setForeground(Color.PINK);
                textField.setBorder(new CompoundBorder(focusedBorder, paddingBorder));
            }

            @Override
            public void focusLost(FocusEvent e) {
                label.setForeground(Color.black);
                textField.setBorder(new CompoundBorder(defaultBorder, paddingBorder));
            }
        });
    }
}
