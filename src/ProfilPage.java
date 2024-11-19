import auth.Session;
import database.DatabaseConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class ProfilPage extends JDialog {
    private static Session session = Session.getInstance();
    MainPage mainPage = new MainPage();
    static JTextField passwordField = new JTextField(20);
    public ProfilPage(MainPage owner) {
        super(owner, "Profil Saya", true);
        this.mainPage = owner;
        setSize(600, 400);
        setLocationRelativeTo(new MainPage());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        // Panel kiri dengan tombol Kembali
        JPanel panelKiri = new JPanel();
        panelKiri.setLayout(new BoxLayout(panelKiri, BoxLayout.Y_AXIS));
        panelKiri.setBackground(Color.PINK);

        JButton keluarButton = new JButton("KEMBALI");
        keluarButton.setForeground(Color.BLACK);

        panelKiri.add(Box.createVerticalStrut(10)); // Spasi antar tombol
        panelKiri.add(keluarButton);

        // Form background panel dengan sudut melengkung
        RoundedPanel formBackgroundPanel = new RoundedPanel(30, Color.WHITE); // Radius sudut melengkung
        formBackgroundPanel.setLayout(new GridBagLayout());
        formBackgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        formBackgroundPanel.setPreferredSize(new Dimension(500, 350));

        // Panel utama untuk konten profil
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Judul
        JLabel judulLabel = new JLabel("Profil Saya");
        judulLabel.setFont(new Font("Serif", Font.BOLD, 30));
        gbc.insets = new Insets(5, 5, 25, 8);
        formBackgroundPanel.add(judulLabel, gbc);
        gbc.insets = new Insets(2, 2, 6, 2); // Margin antar komponen

        // Reset gridwidth untuk komponen berikutnya
        gbc.gridwidth = 1;

        // Panel Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("USERNAME ");
        formBackgroundPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JTextField usernameField = new JTextField(session.getUsername(), 20);
        usernameField.setEditable(false);
        formBackgroundPanel.add(usernameField, gbc);

        // Panel Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel emailLabel = new JLabel("EMAIL ");
        formBackgroundPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JTextField emailField = new JTextField(session.getEmail(), 20);
        emailField.setEditable(false);
        formBackgroundPanel.add(emailField, gbc);

        // Panel Ganti Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("GANTI PASSWORD ");
        formBackgroundPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formBackgroundPanel.add(passwordField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        JButton konfirmasiButton = new JButton("KONFIRMASI");
        gbc.insets = new Insets(2, 2, 6, 2);
        formBackgroundPanel.add(konfirmasiButton, gbc);
        gbc.insets = new Insets(2, 2, 6, 2);

        // Panel Alamat
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel alamatLabel = new JLabel("ALAMAT");
        formBackgroundPanel.add(alamatLabel, gbc);
        formBackgroundPanel.setBackground(Color.WHITE);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel panelAlamat = new JPanel();
        panelAlamat.setLayout(new BoxLayout(panelAlamat, BoxLayout.Y_AXIS));
        panelAlamat.setBackground(Color.WHITE);

        JTextField alamatField1 = new JTextField(session.getAlamat()[0], 20);
        JTextField alamatField2 = new JTextField(session.getAlamat()[1], 20);
        JTextField alamatField3 = new JTextField(session.getAlamat()[2], 20);

        panelAlamat.add(alamatField1);
        panelAlamat.add(Box.createVerticalStrut(5)); // Spasi antar field
        panelAlamat.add(alamatField2);
        panelAlamat.add(Box.createVerticalStrut(5)); // Spasi antar field
        panelAlamat.add(alamatField3);

        formBackgroundPanel.add(panelAlamat, gbc);

        // Button Log Out
        JButton kembalibutton = new JButton("Log Out");
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        formBackgroundPanel.add(kembalibutton, gbc);
        kembalibutton.setForeground(Color.RED);

        // Wrapper panel untuk pusatkan formBackgroundPanel
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.decode("#FFacac")); // Agar panel pembungkus memiliki warna pink gelap
        wrapperPanel.add(formBackgroundPanel, new GridBagConstraints());

        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0;
        gbcLogo.gridy = 5;
        gbcLogo.anchor = GridBagConstraints.SOUTH;
        gbcLogo.insets = new Insets(10, 10, 10, 10);

        // Tambahkan panelAtas, panelKiri, dan wrapperPanel ke frame
        add(wrapperPanel, BorderLayout.CENTER);

        //listener
        alamatField1.addActionListener(e -> {updateAlamat(1, alamatField1.getText());});
        alamatField2.addActionListener(e -> {updateAlamat(2, alamatField2.getText());});
        alamatField3.addActionListener(e -> {updateAlamat(3, alamatField3.getText());});
        kembalibutton.addActionListener(e-> { 
            MainPage mainPage = (MainPage) SwingUtilities.getWindowAncestor(this);
            this.dispose();
            mainPage.logout();
        });
        konfirmasiButton.addActionListener(e -> {updatePassword(passwordField.getText());});


        // Tampilkan frame
        setVisible(true);
    }

    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false); // Supaya tidak ada background persegi default
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
    }

    private static void updateAlamat(int urutan, String alamat) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query = "";

        switch (urutan) {
            case 1:
                query = """
                    update customer \r
                    set alamat_1 = ?\r
                    where id = ?;""";
                break;
            case 2:
                query = """
                        update customer \r
                        set alamat_2 = ?\r
                        where id = ?;""";
                break;
            case 3:
                query = """
                    update customer \r
                    set alamat_3 = "?"\r
                    where id = ?;""";
                break;
            default:
                throw new AssertionError();
        }
    
        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, alamat);
            pstmt.setInt(2, session.getId());

            int rowsAffected = pstmt.executeUpdate();
            if ( rowsAffected > 0 ) { 
                switch (urutan) {
                    case 1:
                        session.setAlamat1(alamat);
                        break;
                    case 2:
                        session.setAlamat2(alamat);
                        break;
                    case 3:
                        session.setAlamat3(alamat);   
                        break;
                    default:
                        throw new AssertionError();
                }
            }
    
            System.out.println("" + rowsAffected + "");

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatePassword(String password) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query = """
                        update customer \r
                        set password = ?\r
                        where id = ?;""";
        if(!password.equals("")) {
            try {
                Connection conn  = dtbs.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
    
                pstmt.setString(1, password);
                pstmt.setInt(2, session.getId());
    
                int rowsAffected = pstmt.executeUpdate();

                if(rowsAffected > 0) {
                    passwordField.setText("");
                }
        
                System.out.println("" + rowsAffected + "");
    
                conn.close();
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
