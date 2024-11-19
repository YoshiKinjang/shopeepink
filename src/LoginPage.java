import auth.Session;
import database.DatabaseConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.*;

public class LoginPage extends JPanel {

    private MainPage mainPage;
    private static Session session = Session.getInstance();

    JPanel logoCon = new JPanel();
    JPanel formCon = new JPanel();
    JPanel pH = new JPanel();
    JPanel fH = new JPanel();
    JPanel buatPanel = new JPanel();
    ImageIcon imageIcon = new ImageIcon("img/logoSBaru.png");
    JLabel imageLabel = new JLabel(imageIcon);
    JLabel buatLbl = new JLabel("Buat");
    JLabel buatPert = new JLabel("Belum punya akun? ");
    JLabel errorHandler = new JLabel("");

    JTextField emailTf = new JTextField();
    JPasswordField passTf = new JPasswordField();
    
    JButton loginBtn = new JButton("Masuk");
    GridBagConstraints gbc;
    GridBagLayout thelayout;

    LoginPage(MainPage mainPage) {
        this.mainPage = mainPage;
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
        
        ///Setting masuk label
        JLabel masukAkun = new JLabel("Masuk akun");
        masukAkun.setFont(new Font("Arial", Font.PLAIN, 32));

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

        buatPert.setFont(new Font("Arial", Font.PLAIN, 16));
        buatLbl.setFont(new Font("Arial", Font.PLAIN, 16));
        buatLbl.setForeground(Color.blue);
        buatPanel.add(buatPert);
        buatPanel.add(buatLbl);
        
        addFocusListenerToTextField(emailTf, emailLbl);
        addFocusListenerToTextField(passTf, passLbl);

        loginBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        loginBtn.setBackground(Color.pink);
        loginBtn.setBorder(new EmptyBorder(10, 10, 10,10));
        

        ///adding component to fh layout
        gbc.fill = GridBagConstraints.BOTH;  
        gbc.gridy = 0; 
        gbc.weightx = 1;
        gbc.insets = new Insets(20, 0, 50, 0);
        fH.add(masukAkun, gbc);

        gbc.gridy = 1; 
        gbc.insets = new Insets(0, 0, 10, 0);
        fH.add(emailLbl, gbc);
 
        gbc.gridy = 2;
        fH.add(emailTf, gbc);

        gbc.gridy = 3; 
        fH.add(passLbl,gbc);

        gbc.gridy = 4; 
        gbc.insets = new Insets(0, 0, 0, 0);
        fH.add(passTf, gbc);

        gbc.gridy = 5; 
        gbc.insets = new Insets(10, 0, 0, 0);
        fH.add(errorHandler, gbc);

        gbc.gridy = 6; 
        gbc.insets = new Insets(20, 0, 20, 0);
        fH.add(loginBtn, gbc);

        gbc.gridy = 7; 
        fH.add(buatPanel, gbc);

        formCon.add(fH);        
        pH.add(logoCon);
        pH.add(formCon);

        add(pH);

        switchRegister();
        
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user_email = emailTf.getText();
                String user_pasword = new String(passTf.getPassword());
                if(authSystem(user_email, user_pasword)) {
                    mainPage.switchToHomePage();
                    // System.out.println("dewa");
                } else {
                    errorHandler.setText("Akun tidak ditemukan, periksa kembali email dan password anda");

                    ///remove error message
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

    private void switchRegister() {
        buatLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainPage.switchToRegisterPage();
            }
        });
    }

    private static boolean authSystem(String email, String password) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "SELECT * FROM customer " + //
                        "WHERE email = ? AND password = ?";

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            System.out.println(pstmt);

            ResultSet resultSet = pstmt.executeQuery();
            boolean isUserExist = resultSet.next();

            if(isUserExist) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email_1 = resultSet.getString("email");
                String password_1 = resultSet.getString("password");
                String alamat_1 = resultSet.getString("alamat_1");
                String alamat_2 = resultSet.getString("alamat_2");
                String alamat_3 = resultSet.getString("alamat_3");
                String[] full_alamat = {alamat_1, alamat_2, alamat_3};
                session.setSession(id, username, email_1, password_1, full_alamat);
            }

            return isUserExist;

        } catch (SQLException e) {
            e.printStackTrace();
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
