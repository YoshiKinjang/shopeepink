import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProfilPage extends JFrame {
    public ProfilPage() {
        setTitle("Profil Saya");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Atas yang memanjang dari kiri ke kanan
        /// Panel atas (Top Bar) pada HomePage
JPanel topBarParent = new JPanel();
GridBagConstraints tbp_gbc = new GridBagConstraints();

// Set layout dan border untuk topBarParent
topBarParent.setLayout(new GridBagLayout());
topBarParent.setBorder(new EmptyBorder(8, 8, 50, 8));
topBarParent.setBackground(Color.white);

// Logo
ImageIcon logoIcon = new ImageIcon("img/logo.png");
Image resizedImage = logoIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
ImageIcon resizedIcon = new ImageIcon(resizedImage);
JLabel logoLabel = new JLabel(resizedIcon);

// Ikon user
ImageIcon userIcon = new ImageIcon("img/user.png");
Image resizedUser = userIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
ImageIcon resizedUIcon = new ImageIcon(resizedUser);
JLabel userLbl = new JLabel(resizedUIcon);

// Text field untuk pencarian

JTextField searchTf = new JTextField();

tbp_gbc.fill = GridBagConstraints.BOTH;  
        tbp_gbc.weightx = 0.5;  
        tbp_gbc.gridx = 0;  
        tbp_gbc.gridy = 0; 
        tbp_gbc.anchor = GridBagConstraints.LINE_START;
        topBarParent.add(logoLabel, tbp_gbc);

        ///search field
        tbp_gbc.fill = GridBagConstraints.BOTH;  
        tbp_gbc.weightx = 1; 
        tbp_gbc.gridx = 1;  
        tbp_gbc.gridy = 0; 
        topBarParent.add(searchTf, tbp_gbc);

        ///user icon
        tbp_gbc.fill = GridBagConstraints.BOTH;  
        tbp_gbc.weightx = 0.5; 
        tbp_gbc.gridx = 2;  
        tbp_gbc.gridy = 0; 
        topBarParent.add(userLbl, tbp_gbc);


// Menambah komponen logo ke topBarParent
tbp_gbc.fill = GridBagConstraints.BOTH;  
tbp_gbc.weightx = 0.5;  
tbp_gbc.gridx = 0;  
tbp_gbc.gridy = 0; 
tbp_gbc.anchor = GridBagConstraints.LINE_START;
topBarParent.add(logoLabel, tbp_gbc);

// Menambah komponen pencarian ke topBarParent
tbp_gbc.weightx = 1; 
tbp_gbc.gridx = 1;
topBarParent.add(searchTf, tbp_gbc);

// Menambah komponen user icon ke topBarParent
tbp_gbc.weightx = 0.5; 
tbp_gbc.gridx = 2;
topBarParent.add(userLbl, tbp_gbc);

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
        JTextField usernameField = new JTextField("", 20);
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
        JTextField emailField = new JTextField("", 20);
        formBackgroundPanel.add(emailField, gbc);

        // Panel Ganti Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("GANTI PASSWORD ");
        formBackgroundPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField passwordField = new JTextField(20);
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

        JTextField alamatField1 = new JTextField("", 20);
        JTextField alamatField2 = new JTextField(20);
        JTextField alamatField3 = new JTextField(20);

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
        add(topBarParent, BorderLayout.NORTH); // Menambahkan panel di atas
        add(wrapperPanel, BorderLayout.CENTER);

        // Tampilkan frame
        setVisible(true);
    }

    // Kelas custom JPanel dengan sudut melengkung
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

    public static void main(String[] args) {
        new ProfilPage();
    }
}
