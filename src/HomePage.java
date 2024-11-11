import auth.Session;
import database.DatabaseConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;

public class HomePage extends JPanel {

    private MainPage mainPage;
    private static Session session = Session.getInstance();

    JPanel parent = new JPanel();
    JPanel topBarParent = new JPanel();
    JPanel mainSectionParent = new JPanel();
    JButton semuaBtn = new JButton("Semua");
    JButton komponenBtn = new JButton("Komponen");
    JButton aksesorisBtn = new JButton("Aksesoris");
    JButton laptopBtn = new JButton("Laptop");
    int contentRows = 4;

    HomePage(MainPage mainPage) {

        this.mainPage = mainPage;

        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout thelayout = new GridBagLayout();
        setLayout(new CardLayout(0,0));

        parent.setLayout(thelayout);
        parent.setBackground(Color.red);

        ///top bar
        gbc.fill = GridBagConstraints.BOTH;  
        gbc.weightx = 1;  
        gbc.gridx = 0;  
        gbc.gridy = 0; 
        gbc.weighty = 0.2;
        // gbc.insets = new Insets(20, 0, 50, 0);
        topBarParent.setLayout(new GridBagLayout());
        topBarParent.setBorder(new EmptyBorder(8,8,8,8));
        GridBagConstraints tbp_gbc = new GridBagConstraints();

        ///populate top bar component
        ImageIcon logoIcon = new ImageIcon("img/logo.png");
        Image resizedImage = logoIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel logoLabel = new JLabel(resizedIcon);

        ImageIcon userIcon = new ImageIcon("img/user.png");
        Image resizedUser = userIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        ImageIcon resizedUIcon = new ImageIcon(resizedUser);
        JLabel userLbl = new JLabel(resizedUIcon);
        
        JTextField searchTf = new JTextField();
        addFocusListenerToTextField(searchTf);

        ///logo icon
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

        topBarParent.setBackground(Color.white);
        parent.add(topBarParent, gbc);


        ///populate main section
        mainSectionParent.setLayout(new CardLayout(0,0));

        JPanel mainSection = new JPanel();
        mainSection.setLayout(new GridBagLayout());
        GridBagConstraints msp = new GridBagConstraints();


        JPanel scrollArea = new JPanel();
        scrollArea.setBackground(Color.decode("#FFC0CB"));
        scrollArea.setBorder(new EmptyBorder(20,20,20,20));
        scrollArea.setLayout(new BorderLayout(20,15));

        ///populate filter parent
        JPanel filterParent = new JPanel();
        filterParent.setOpaque(false);
        filterParent.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        filterParent.add(semuaBtn);
        filterParent.add(komponenBtn);
        filterParent.add(aksesorisBtn);
        filterParent.add(laptopBtn);

        scrollArea.add(filterParent, BorderLayout.NORTH);

        // Populate content area with scrolling support
        JPanel contentAreaParent = new JPanel();
        contentAreaParent.setBackground(Color.decode("#FFC0CB"));
        contentAreaParent.setLayout(new GridLayout(0, 4, 20, 20));

        // int itemHeight = 180 + 20;
        // contentAreaParent.setPreferredSize(new Dimension(900, contentRows * itemHeight));

        // Add individual content panels with fixed sizes
        
        ResultSet rs = showProduct();
        try {
            while(rs.next()) {
                int id = rs.getInt("id");
                String nama_barang = rs.getString("nama");
                int harga_barang = rs.getInt("harga");
                int potongan_barang = rs.getInt("potongan");
                String deskripsi_barang = rs.getString("deskripsi");
                String stok_barang = rs.getString("stok");
                String gambar_barang = rs.getString("gambar");
                contentAreaParent.add(itemCard(gambar_barang, nama_barang, harga_barang, stok_barang, potongan_barang));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Wrap the content area in a FlowLayout panel to prevent resizing within the scroll pane
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.decode("#FFC0CB"));
        wrapperPanel.add(contentAreaParent, BorderLayout.CENTER);

        // Add the wrapper panel to a JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBackground(Color.decode("#FFC0CB"));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray, 0));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollArea.add(scrollPane, BorderLayout.CENTER);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            e.getAdjustable().setUnitIncrement(16); // Set an increment value that feels smooth and responsive
        });

        msp.fill = GridBagConstraints.BOTH;  
        msp.weightx = 1;  
        msp.gridx = 0;  
        msp.gridy = 0;
        msp.weighty = 1;
        // msp.insets = new Insets(20, 0, 50, 0);
        mainSection.add(scrollArea, msp);
        
        ///keranjang
        JPanel keranjangParent = new JPanel();
        Border topBorder = BorderFactory.createMatteBorder(5, 0, 0, 0, Color.lightGray);
        Border leftPadding = new EmptyBorder(0, 20, 0, 20);
        keranjangParent.setBorder(BorderFactory.createCompoundBorder(topBorder, leftPadding));
        keranjangParent.setLayout(new BorderLayout());

        ///keranjang title
        JLabel keranjangTitle = new JLabel("Keranjang");
        keranjangTitle.setFont(new Font("Arial", Font.BOLD, 24));
        keranjangTitle.setBorder(new EmptyBorder(20,20,20,20));

        keranjangParent.add(keranjangTitle, BorderLayout.NORTH);

        //keranjang item
        JPanel keranjangItemParent = new JPanel();
        keranjangItemParent.setBackground(Color.white);
        keranjangItemParent.setLayout(new BoxLayout(keranjangItemParent, BoxLayout.Y_AXIS));

        for (int i = 0; i < 10; i++) {
            
            keranjangItemParent.add(keranjangItem("Apple macbook air", "15.000"));
        }
        
        JPanel wrapperKeranjang = new JPanel(new BorderLayout());
        wrapperKeranjang.setBackground(Color.white);
        wrapperKeranjang.add(keranjangItemParent, BorderLayout.NORTH);

        // Add the wrapper panel to a JScrollPane for scrolling
        JScrollPane scrollKeranjang = new JScrollPane(wrapperKeranjang);
        scrollKeranjang.setBackground(Color.white);
        scrollKeranjang.setBorder(BorderFactory.createLineBorder(Color.lightGray, 0));
        scrollKeranjang.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollKeranjang.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollKeranjang.getVerticalScrollBar().addAdjustmentListener(e -> {
            e.getAdjustable().setUnitIncrement(16); // Set an increment value that feels smooth and responsive
        });

        keranjangParent.add(scrollKeranjang,BorderLayout.CENTER);

        ///add bottom bar
        JPanel summaryBar = new JPanel();
        summaryBar.setLayout(new BorderLayout());
        summaryBar.setBorder(new EmptyBorder(20,20,20,20));
        summaryBar.setBackground(Color.white);
        
        JPanel spo = new JPanel();
        spo.setBackground(Color.white);
        JLabel ketTotalBarang = new JLabel("Total (3 Produk): ");
        JLabel ketTotalHarga = new JLabel("Rp63.000");
        ketTotalBarang.setFont(new Font("Arial", Font.BOLD, 16));
        ketTotalHarga.setFont(new Font("Arial", Font.BOLD, 24));
        spo.add(ketTotalBarang);
        spo.add(ketTotalHarga);
        summaryBar.add(spo, BorderLayout.WEST);

        JButton checkOutBtn = new JButton("Checkout");
        summaryBar.add(checkOutBtn, BorderLayout.EAST);
        keranjangParent.add(summaryBar,BorderLayout.SOUTH);

        

        keranjangParent.setBackground(Color.white);
        msp.fill = GridBagConstraints.BOTH;  
        msp.weightx = 0.2;  
        msp.gridx = 1;  
        msp.gridy = 0;
        // msp.insets = new Insets(20, 0, 50, 0);
        mainSection.add(keranjangParent, msp);

        ///addAll
        mainSectionParent.add(mainSection);


        ///main section
        gbc.fill = GridBagConstraints.BOTH;  
        gbc.weightx = 1;  
        gbc.gridx = 0;  
        gbc.gridy = 1; 
        gbc.weighty = 2 ;
        // gbc.insets = new Insets(20, 0, 50, 0);
        parent.add(mainSectionParent, gbc);


        add(parent);
    }

    private static ResultSet showProduct() {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "SELECT * FROM product WHERE stok != 0;";
        ResultSet resultSet = null;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            resultSet = pstmt.executeQuery();
        
            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static JPanel itemCard(String imageUrl, String namaBarang, int harga, String stok, int potongan) {
        JPanel parent = new JPanel();
        parent.setLayout(new CardLayout(10,10));

        // Load the image and create JLabel
        ImageIcon originalIcon = new ImageIcon("img/" + imageUrl);
        JLabel imagelbl = new JLabel(originalIcon);

        // Adjust icon size dynamically based on JLabel size
        imagelbl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = imagelbl.getWidth();
                int height = imagelbl.getHeight();
                if (width > 0 && height > 0) {
                    Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    imagelbl.setIcon(new ImageIcon(scaledImage));
                }
            }
        });

        // Set a preferred size for the image label
        imagelbl.setPreferredSize(new Dimension(150, 150));

        // Other components
        JLabel namaBarangLbl = new JLabel(namaBarang);

        String hargaString = "";
        if(potongan != 0) {
            int harga_diskon = harga -  (int) (harga * (potongan / 100.0));
            hargaString = "<html><b>" + formatRupiah(harga_diskon)  + "</b> <small><s>" + formatRupiah(harga) + "</s></small></html>";
        } else {
            hargaString = formatRupiah(harga);
        }

        JLabel hargaLbl = new JLabel(hargaString);
        JLabel stokLbl = new JLabel("Stok: " + stok);
        JButton addBtn = new JButton("+");
        addBtn.setBackground(Color.decode("#FF659B"));
        addBtn.setForeground(Color.white);

        // Panel setup
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.white);
        GridBagConstraints ibc = new GridBagConstraints();

        // Image section with dynamic resizing
        ibc.fill = GridBagConstraints.BOTH;
        ibc.weightx = 1;
        ibc.weighty = 1;
        ibc.gridx = 0;
        ibc.gridy = 0;

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imagelbl, BorderLayout.CENTER);
        imagePanel.setBackground(Color.lightGray);
        cardPanel.add(imagePanel, ibc);
  
        ///nama barang
        ibc.gridx = 0;  
        ibc.gridy = 1; 
        ibc.weighty = 0;
        namaBarangLbl.setBorder(new EmptyBorder(5,0,0,0));
        cardPanel.add(namaBarangLbl, ibc);

        ///harga barang
        ibc.gridx = 0;  
        ibc.gridy = 2; 
        ibc.weighty = 0;
        hargaLbl.setFont(new Font("Arial", Font.BOLD, 15));
        hargaLbl.setBorder(new EmptyBorder(5,0,5,0));
        hargaLbl.setForeground(Color.decode("#29A1F1"));
        cardPanel.add(hargaLbl, ibc);

        ///stok barang
        ibc.gridx = 0;  
        ibc.gridy = 3; 
        ibc.weighty = 0;
        cardPanel.add(stokLbl, ibc);

        ///add button
        JPanel oo = new JPanel();
        oo.setOpaque(false);
        oo.setLayout(new FlowLayout(FlowLayout.CENTER));
        oo.add(addBtn);
        ibc.gridy = 4; 
        cardPanel.add(oo, ibc);

        parent.add(cardPanel);
        parent.setBackground(Color.white);

        return parent;
    }

    public static JPanel keranjangItem(String namaBarang, String hargaBarang) {
        JPanel keranjangItem = new JPanel();
        keranjangItem.setBackground(Color.white);

        keranjangItem.setLayout(new GridBagLayout());
        keranjangItem.setBorder(new EmptyBorder(15,15,15,15));
        GridBagConstraints krGbc = new GridBagConstraints();

        // keranjangItem.setAlignmentX(Component.CENTER_ALIGNMENT);
        // keranjangItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, keranjangItem.getPreferredSize().height));
        // tes2.setAlignmentX(Component.CENTER_ALIGNMENT);

        ///populate item card;
        JCheckBox c1 = new JCheckBox();
        c1.setBackground(Color.white);

        JPanel infoItemParent = new JPanel();
        infoItemParent.setBackground(Color.white);
        infoItemParent.setBorder(new EmptyBorder(0,20,0,0));
        infoItemParent.setLayout(new GridLayout(3,1,10,0));
        JLabel namaBarangLbl = new JLabel(namaBarang);
        namaBarangLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel hargaLbl = new JLabel("Rp" + hargaBarang);
        hargaLbl.setFont(new Font("Arial", Font.BOLD, 16));
        hargaLbl.setForeground(Color.BLUE);
        JPanel bPan = new JPanel();
        bPan.setLayout(new FlowLayout(FlowLayout.LEFT,0, 0));
        bPan.setBackground(Color.white);

        JButton minusBtn = new JButton("-");
        JTextField jmlBarang = new JTextField("1");

        jmlBarang.setPreferredSize(new Dimension(25, 25));
        jmlBarang.setEditable(false);
        jmlBarang.setHorizontalAlignment(JTextField.CENTER);
        JPanel jmlBarangTxt = new JPanel();
        jmlBarangTxt.setBackground(Color.white);

        jmlBarangTxt.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        jmlBarangTxt.add(jmlBarang);

        JButton plusBtn = new JButton("+");
        JButton trashBtn = new JButton("Hapus");
        JPanel trashBtnPanel = new JPanel();
        trashBtnPanel.setBackground(Color.white);

        trashBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        trashBtnPanel.add(trashBtn);
        
        bPan.add(minusBtn);
        bPan.add(jmlBarangTxt);
        bPan.add(plusBtn);
        bPan.add(trashBtnPanel);

        infoItemParent.add(namaBarangLbl);
        infoItemParent.add(hargaLbl);
        infoItemParent.add(bPan);

        krGbc.fill = GridBagConstraints.BOTH;
        krGbc.weightx = 0;
        krGbc.gridx = 1;
        krGbc.gridy = 0;
        keranjangItem.add(c1, krGbc);

        krGbc.fill = GridBagConstraints.BOTH;
        krGbc.weightx = 1;
        krGbc.gridx = 2;
        krGbc.gridy = 0;
        keranjangItem.add(infoItemParent, krGbc);

        return keranjangItem;
    }

    private void addFocusListenerToTextField(JTextField textField) {
        Border defaultBorder = BorderFactory.createLineBorder(Color.lightGray, 2);
        Border focusedBorder = BorderFactory.createLineBorder(Color.pink, 2);

        Border paddingBorder = new EmptyBorder(5, 10, 5, 10);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(new CompoundBorder(defaultBorder, paddingBorder));

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(new CompoundBorder(focusedBorder, paddingBorder));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(new CompoundBorder(defaultBorder, paddingBorder));
            }
        });
    }

    public static String formatRupiah(int amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("id").setRegion("ID").build());
        return currencyFormat.format(amount);
    }
}
