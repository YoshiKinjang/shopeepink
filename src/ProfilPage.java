import java.awt.*;
import javax.swing.*;

public class ProfilPage extends JFrame {
    public ProfilPage() {
        setTitle("Profil Saya");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel kiri dengan tombol Kembali dan Keluar
        JPanel panelKiri = new JPanel();
        panelKiri.setLayout(new BoxLayout(panelKiri, BoxLayout.Y_AXIS));
        panelKiri.setBackground(Color.PINK);

        JButton keluarButton = new JButton("KEMBALI");
        keluarButton.setForeground(Color.BLACK);

        panelKiri.add(Box.createVerticalStrut(10)); // Spasi antar tombol
        panelKiri.add(keluarButton);

        JPanel formBackgroundPanel = new JPanel();
        formBackgroundPanel.setBackground(Color.WHITE);
        formBackgroundPanel.setLayout(new GridBagLayout());
        formBackgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        formBackgroundPanel.setBorder(new javax.swing.border.AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.drawRoundRect(x, y, width - 1, height - 1, 20, 20); // 20 adalah radius sudut
            }
        });
        
        // Mengatur ukuran preferred
        formBackgroundPanel.setPreferredSize(new Dimension(500, 350));

      
        // Panel utama untuk konten profil
        JPanel panelKonten = new JPanel(new GridBagLayout());
        panelKonten.setBackground(Color.decode("#FFacac"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Judul
        JLabel judulLabel = new JLabel("Profil Saya");
        judulLabel.setBounds(1,1,1,1);
        judulLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        JTextField usernameField = new JTextField("", 20);
        formBackgroundPanel.add(usernameField, gbc);

        // Panel Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("EMAIL ");
        formBackgroundPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField emailField = new JTextField("", 20);
        formBackgroundPanel.add(emailField, gbc);

        // Panel Ganti Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("GANTI PASSWORD ");
        formBackgroundPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField passwordField = new JTextField(20);
        formBackgroundPanel.add(passwordField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        JButton konfirmasiButton = new JButton("KONFIRMASI");
        formBackgroundPanel.add(konfirmasiButton, gbc);

        // Panel Alamat
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel alamatLabel = new JLabel("ALAMAT");
        formBackgroundPanel.add(alamatLabel, gbc);
        formBackgroundPanel.setBackground(Color.white);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel panelAlamat = new JPanel();
        panelAlamat.setLayout(new BoxLayout(panelAlamat, BoxLayout.Y_AXIS));
        panelAlamat.setBackground(Color.white);

      

        JTextField alamatField1 = new JTextField("", 20);
        JTextField alamatField2 = new JTextField(20);
        JTextField alamatField3 = new JTextField(20);

        panelAlamat.add(alamatField1);
        panelAlamat.add(Box.createVerticalStrut(5)); // Spasi antar field
        panelAlamat.add(alamatField2);
        panelAlamat.add(Box.createVerticalStrut(5)); // Spasi antar field
        panelAlamat.add(alamatField3);

        formBackgroundPanel.add(panelAlamat, gbc);
          //button kembali
          JButton kembalibutton = new JButton("Log Out");
          gbc.gridx = 1; // Set posisi horizontal
          gbc.gridy = 10; // Set posisi vertikal
          gbc.gridwidth = 1;
          gbc.anchor = GridBagConstraints.CENTER;
          formBackgroundPanel.add(kembalibutton, gbc);
          kembalibutton.setForeground(Color.RED);
        
          formBackgroundPanel.add(panelKonten, new GridBagConstraints());
          JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          wrapperPanel.setBackground(Color.decode("#FFacac")); // Agar panel pembungkus memiliki warna pink gelap
          wrapperPanel.add(formBackgroundPanel);


        // Tambahkan panel kiri dan panel konten ke frame
        add(panelKiri, BorderLayout.WEST);
        add(new JPanel(),BorderLayout.NORTH);
        add(wrapperPanel, BorderLayout.CENTER);
        // Tampilkan frame
        setVisible(true);
    }

    public static void main(String[] args) {
        new ProfilPage();
    }
}
