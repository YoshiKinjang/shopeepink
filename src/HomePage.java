import auth.Session;
import database.DatabaseConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
    static JPanel keranjangItemParent = new JPanel();
    private static Map<Integer, JTextField> jmlBarangMap = new HashMap<>();
    private static Map<Integer, JPanel> keranjangItemMap = new HashMap<>();
    private static Map<Integer, CheckoutInfo> checkoutItem = new HashMap<>();

    private static JLabel ketTotalBarang = new JLabel("Total (0 Produk): ");
    private static JLabel ketTotalHarga = new JLabel("Rp-");

    private static JButton checkOutBtn = new JButton("Checkout");
    private static JPanel contentAreaParent = new JPanel();

    HomePage(MainPage mainPage) {

        this.mainPage = mainPage;
        checkoutItem.clear();
        keranjangItemMap.clear();
        jmlBarangMap.clear();

        // for (Integer id : checkoutItem.keySet()) {
        //     CheckoutInfo cc = checkoutItem.get(id);
        //     System.out.print(cc.getIdBarang() + ",");
        // }

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

        JButton riwayatPesananBtn = new JButton("Riwayat Pesananan");
        
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

        ///riwayat button 
        tbp_gbc.weightx = 0; 
        tbp_gbc.gridx = 2;  
        tbp_gbc.gridy = 0; 
        tbp_gbc.insets = new Insets(0, 20, 0, 0);
        topBarParent.add(riwayatPesananBtn, tbp_gbc);

        ///user icon
        tbp_gbc.fill = GridBagConstraints.BOTH;  
        tbp_gbc.weightx = 0.5; 
        tbp_gbc.gridx = 3;  
        tbp_gbc.gridy = 0; 
        tbp_gbc.insets = new Insets(0, 0, 0, 0);
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
        
        contentAreaParent.setBackground(Color.decode("#FFC0CB"));
        contentAreaParent.setLayout(new GridLayout(0, 2, 20, 20));

        // int itemHeight = 180 + 20;

        // Add individual content panels with fixed sizes
        showProduct();
        
        
        // Wrap the content area in a FlowLayout panel to prevent resizing within the scroll pane
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.decode("#FFC0CB"));
        wrapperPanel.add(contentAreaParent, BorderLayout.CENTER);

        // Add the wrapper panel to a JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBackground(Color.decode("#FFC0CB"));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray, 0));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
        keranjangItemParent.setBackground(Color.white);
        keranjangItemParent.setLayout(new BoxLayout(keranjangItemParent, BoxLayout.Y_AXIS));

        showKeranjang();
        
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
        ketTotalBarang.setFont(new Font("Arial", Font.PLAIN, 16));
        ketTotalHarga.setFont(new Font("Arial", Font.BOLD, 18));
        spo.add(ketTotalBarang);
        spo.add(ketTotalHarga);
        summaryBar.add(spo, BorderLayout.NORTH);

        summaryBar.add(checkOutBtn, BorderLayout.SOUTH);
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

        checkOutBtn.setEnabled(false);

        checkoutNow();

        ///listener
        semuaBtn.addActionListener(e -> {showProduct();});
        komponenBtn.addActionListener(e -> { showProductKomponen(); });
        aksesorisBtn.addActionListener(e -> {showProductAksesoris();});
        laptopBtn.addActionListener(e -> {showProductLaptop();});
        searchTf.addActionListener(e -> {showProductByName(searchTf.getText());});

        userLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ProfilPage(mainPage);
            }
        });

        riwayatPesananBtn.addActionListener(e -> {mainPage.switchToRiwayatPage();});

        add(parent);
    }

    private static void showProduct() {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "SELECT * FROM product WHERE stok != 0;";
        ResultSet resultSet = null;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            resultSet = pstmt.executeQuery();
        
            contentAreaParent.removeAll();
            try {
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nama_barang = resultSet.getString("nama");
                    int harga_barang = resultSet.getInt("harga");
                    int potongan_barang = resultSet.getInt("potongan");
                    String deskripsi_barang = resultSet.getString("deskripsi");
                    String stok_barang = resultSet.getString("stok");
                    String gambar_barang = resultSet.getString("gambar");
                    contentAreaParent.add(itemCard(id, gambar_barang, nama_barang, harga_barang, stok_barang, potongan_barang, deskripsi_barang));
                    contentAreaParent.revalidate();
                    contentAreaParent.repaint();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showProductByName(String nama) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "SELECT * FROM product WHERE stok != 0 and nama like '%" + nama + "%';";
        ResultSet resultSet = null;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            resultSet = pstmt.executeQuery();
        
            contentAreaParent.removeAll();
            try {
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nama_barang = resultSet.getString("nama");
                    int harga_barang = resultSet.getInt("harga");
                    int potongan_barang = resultSet.getInt("potongan");
                    String deskripsi_barang = resultSet.getString("deskripsi");
                    String stok_barang = resultSet.getString("stok");
                    String gambar_barang = resultSet.getString("gambar");
                    contentAreaParent.add(itemCard(id, gambar_barang, nama_barang, harga_barang, stok_barang, potongan_barang, deskripsi_barang));
                    contentAreaParent.revalidate();
                    contentAreaParent.repaint();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showProductKomponen() {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "SELECT * FROM product WHERE stok != 0 and kategori = 'Komponen';";
        ResultSet resultSet = null;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            resultSet = pstmt.executeQuery();
                
            contentAreaParent.removeAll();
            try {
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nama_barang = resultSet.getString("nama");
                    int harga_barang = resultSet.getInt("harga");
                    int potongan_barang = resultSet.getInt("potongan");
                    String deskripsi_barang = resultSet.getString("deskripsi");
                    String stok_barang = resultSet.getString("stok");
                    String gambar_barang = resultSet.getString("gambar");
                    contentAreaParent.add(itemCard(id, gambar_barang, nama_barang, harga_barang, stok_barang, potongan_barang, deskripsi_barang));
                    contentAreaParent.revalidate();
                    contentAreaParent.repaint();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 

    private static void showProductAksesoris() {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "SELECT * FROM product WHERE stok != 0 and kategori = 'Aksesoris';";
        ResultSet resultSet = null;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            resultSet = pstmt.executeQuery();
                
            contentAreaParent.removeAll();
            try {
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nama_barang = resultSet.getString("nama");
                    int harga_barang = resultSet.getInt("harga");
                    int potongan_barang = resultSet.getInt("potongan");
                    String deskripsi_barang = resultSet.getString("deskripsi");
                    String stok_barang = resultSet.getString("stok");
                    String gambar_barang = resultSet.getString("gambar");
                    contentAreaParent.add(itemCard(id, gambar_barang, nama_barang, harga_barang, stok_barang, potongan_barang, deskripsi_barang));
                    contentAreaParent.revalidate();
                    contentAreaParent.repaint();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 

    private static void showProductLaptop() {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  "SELECT * FROM product WHERE stok != 0 and kategori = 'Laptop';";
        ResultSet resultSet = null;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            resultSet = pstmt.executeQuery();
                
            contentAreaParent.removeAll();
            try {
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nama_barang = resultSet.getString("nama");
                    int harga_barang = resultSet.getInt("harga");
                    int potongan_barang = resultSet.getInt("potongan");
                    String deskripsi_barang = resultSet.getString("deskripsi");
                    String stok_barang = resultSet.getString("stok");
                    String gambar_barang = resultSet.getString("gambar");
                    contentAreaParent.add(itemCard(id, gambar_barang, nama_barang, harga_barang, stok_barang, potongan_barang, deskripsi_barang));
                    contentAreaParent.revalidate();
                    contentAreaParent.repaint();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showKeranjang() {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  """
                        select *, k.id as keranjang_id,\r
                        \tcase \r
                        \t\twhen p.potongan = 0 then p.harga\r
                        \t\telse p.harga - (p.harga * (p.potongan / 100.0))\r
                        \tend as final_harga\r
                        from keranjang k\r
                        inner join product p on p.id = k.product_id\r
                        where k.customer_id = ?;""" //
        //
        //
        //
        //
        //
        //
        ;
        
        ResultSet resultSet = null;
        keranjangItemParent.removeAll();
        ketTotalBarang.setText("Total (0 Produk): ");
        ketTotalHarga.setText("Rp-");

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, session.getId());
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) { 
                int keranjangId = resultSet.getInt("keranjang_id");
                int beratBarang = resultSet.getInt("berat");
                int product_id = resultSet.getInt("product_id");
                String namaBarang = resultSet.getString("nama");
                int hargaBarang = resultSet.getInt("harga");
                int potongan = resultSet.getInt("potongan");
                int final_harga = potongan != 0 ? (int) (hargaBarang - (hargaBarang * potongan/100.0)) : hargaBarang;
                int quantity = resultSet.getInt("quantity");
                int stok = resultSet.getInt("stok");

                keranjangItemParent.add(keranjangItem(product_id, keranjangId, namaBarang, final_harga, quantity, stok, beratBarang));
                keranjangItemParent.revalidate();
                keranjangItemParent.repaint();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static JPanel keranjangItem(int product_id, int keranjang_id, String namaBarang, int hargaBarang, int quantity, int stok, int beratBarang) {

        ///jtextfield for storing key data
        JTextField kerItemId = new JTextField("" + keranjang_id + "");
        JTextField stokItem = new JTextField("" + stok + "");
        JTextField hargaItem = new JTextField(String.valueOf(hargaBarang));
        JTextField beratItem = new JTextField(String.valueOf(beratBarang));

        JPanel keranjangItem = new JPanel();
        keranjangItemMap.put(keranjang_id, keranjangItem);
        keranjangItem.setBackground(Color.white);

        keranjangItem.setLayout(new GridBagLayout());
        keranjangItem.setBorder(new EmptyBorder(15,15,15,15));
        GridBagConstraints krGbc = new GridBagConstraints();

        ///populate item card;
        JCheckBox c1 = new JCheckBox();
        c1.setBackground(Color.white);

        JPanel infoItemParent = new JPanel();
        infoItemParent.setBackground(Color.white);
        infoItemParent.setBorder(new EmptyBorder(0,20,0,0));
        infoItemParent.setLayout(new GridLayout(3,1,10,0));

        JLabel namaBarangLbl = new JLabel(namaBarang);
        namaBarangLbl.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel hargaLbl = new JLabel(formatRupiah(hargaBarang));
        hargaLbl.setFont(new Font("Arial", Font.BOLD, 14));
        // hargaLbl.setForeground(Color.BLUE);

        JPanel bPan = new JPanel();
        bPan.setLayout(new FlowLayout(FlowLayout.LEFT,0, 0));
        bPan.setBackground(Color.white);

        JButton minusBtn = new JButton("-");
        JTextField jmlBarang = new JTextField("" + quantity + "");
        jmlBarang.setPreferredSize(new Dimension(25, 25));
        jmlBarang.setEditable(false);
        jmlBarang.setHorizontalAlignment(JTextField.CENTER);

        jmlBarangMap.put(product_id, jmlBarang);

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

        plusBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int jmlKer = Integer.parseInt(jmlBarang.getText());
                int stokBarang = Integer.parseInt(stokItem.getText());
                int kerId = Integer.parseInt(kerItemId.getText());
                jmlKer += 1;
                int jmlHarga = Integer.parseInt(hargaItem.getText()) * jmlKer;
                int jmlBerat = Integer.parseInt(beratItem.getText()) * jmlKer;

                if(jmlKer <= stokBarang) {
                    updateKeranjangQuantity(kerId, jmlKer);
                    jmlBarang.setText("" + jmlKer + "");

                    if(checkoutItem.containsKey(kerId)) {
                        CheckoutInfo ci = checkoutItem.get(kerId);
                        ci.setTotalBarang(jmlKer);
                        ci.setTotalHarga(jmlHarga);
                        ci.setTotalBerat(jmlBerat);
                        hitungCheckout();
                    }
                }
            }
        });
        
        minusBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int jmlKer = Integer.parseInt(jmlBarang.getText());
                int kerId = Integer.parseInt(kerItemId.getText());
                jmlKer -= 1;
                int jmlHarga = Integer.parseInt(hargaItem.getText()) * jmlKer;
                int jmlBerat = Integer.parseInt(beratItem.getText()) * jmlKer;

                if(jmlKer > 0) {
                    updateKeranjangQuantity(kerId, jmlKer);
                    jmlBarang.setText("" + jmlKer + "");

                    if(checkoutItem.containsKey(kerId)) {
                        CheckoutInfo ci = checkoutItem.get(kerId);
                        ci.setTotalBarang(jmlKer);
                        ci.setTotalHarga(jmlHarga);
                        ci.setTotalBerat(jmlBerat);
                        hitungCheckout();
                    }

                }

                if(jmlKer <= 0) {
                    deleteKeranjang(kerId);
                    if(checkoutItem.containsKey(kerId)) {
                        checkoutItem.remove(kerId);
                        hitungCheckout();
                    }
                }
            }
        });

        trashBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteKeranjang(keranjang_id);
                int kerId = Integer.parseInt(kerItemId.getText());
                if(checkoutItem.containsKey(kerId)) {
                    checkoutItem.remove(kerId);
                    hitungCheckout();
                }
            }
        });

        c1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int kerId = Integer.parseInt(kerItemId.getText());
                int jmlKer = Integer.parseInt(jmlBarang.getText());
                int jmlHarga = Integer.parseInt(hargaItem.getText()) * jmlKer;
                int jmlBerat = Integer.parseInt(beratItem.getText()) * jmlKer;

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkoutItem.put(kerId, new CheckoutInfo(product_id, jmlKer, jmlHarga, Integer.parseInt(hargaItem.getText()), jmlBerat));
                    hitungCheckout();
                } else {
                    checkoutItem.remove(kerId);
                    hitungCheckout();
                }
            }
        });

        return keranjangItem;
    }

    public static void updateKeranjangQuantity(int id, int quantity) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  """
                        update keranjang \r
                        set quantity = ?\r
                        where id = ?;""" //
        //
        ;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();
    
            System.out.println("" + rowsAffected + "");

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean cekKeranjang(int product_id, int customer_id) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  """
                        SELECT * FROM keranjang k \r
                        WHERE product_id = ?\r
                        AND customer_id = ?""" //
        //
        ;

        Connection conn = null;
        try {
            conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, product_id);
            pstmt.setInt(2, customer_id);

            ResultSet resultSet = pstmt.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void insertProdukKeranjang(int product_id, int customer_id) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  """
                        INSERT INTO shopee_pink.keranjang (customer_id,product_id)\r
                        \tVALUES (?,?);""" //
        ;

        String query2 =  """
                        select *, k.id as keranjang_id,\r
                        \tcase \r
                        \t\twhen p.potongan = 0 then p.harga\r
                        \t\telse p.harga - (p.harga * (p.potongan / 100.0))\r
                        \tend as final_harga\r
                        from keranjang k\r
                        inner join product p on p.id = k.product_id\r
                        where k.id = ?;""" //
        ;

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, customer_id);
            pstmt.setInt(2, product_id);

            int rowsAffected = pstmt.executeUpdate();

            
            if(rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        
                        ResultSet resultSet = null;

                        try {
                            PreparedStatement pstmt2 = conn.prepareStatement(query2);

                            pstmt2.setInt(1, generatedId);
                            resultSet = pstmt2.executeQuery();

                            while (resultSet.next()) { 
                                int keranjangId = resultSet.getInt("keranjang_id");
                                int beratBarang = resultSet.getInt("berat");
                                int product_id2 = resultSet.getInt("product_id");
                                String namaBarang = resultSet.getString("nama");
                                int hargaBarang = resultSet.getInt("harga");
                                int potongan = resultSet.getInt("potongan");
                                int final_harga = potongan != 0 ? (int) (hargaBarang - (hargaBarang * potongan/100.0)) : hargaBarang;
                                int quantity = resultSet.getInt("quantity");
                                int stok = resultSet.getInt("stok");

                                keranjangItemParent.add(keranjangItem(product_id2, keranjangId, namaBarang, final_harga, quantity, stok, beratBarang));

                                keranjangItemParent.revalidate();
                                keranjangItemParent.repaint();
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // keranjangItemParent.add(keranjangItem)
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    
    public static void updateKeranjangQuantityFromItem(int product_id, int customer_id, int quantity) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  """
                        UPDATE keranjang
                        SET quantity = quantity + ?
                        WHERE product_id = ? 
                          AND customer_id = ?
                          AND (quantity + 1) <= (SELECT stok FROM product WHERE id = ?);""" //
        ;

        String query2 = """
                        SELECT * FROM keranjang k \r
                        WHERE product_id = ?\r
                        AND customer_id = ?;""";
        //
        

        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, product_id);
            pstmt.setInt(3, customer_id);
            pstmt.setInt(4, product_id);


            int rowsAffected = pstmt.executeUpdate();
    
            System.out.println("" + rowsAffected + "");

            if(rowsAffected > 0) {
                JTextField jmlBarangField = jmlBarangMap.get(product_id);
                if (jmlBarangField != null) {
                    int newQuantity = Integer.parseInt(jmlBarangField.getText()) + 1;
                    jmlBarangField.setText(String.valueOf(newQuantity));

                    try {
                        PreparedStatement pstmt2 = conn.prepareStatement(query2);
    
                        pstmt2.setInt(1, product_id);
                        pstmt2.setInt(2, customer_id);
    
                        ResultSet rs = pstmt2.executeQuery();
    
                        while (rs.next()) { 
                            int keranjangId = rs.getInt("id");
                            if(checkoutItem.containsKey(keranjangId)) {
                                CheckoutInfo ci = checkoutItem.get(keranjangId);
                                ci.setTotalBarang(ci.getTotalBarang() + 1);
                                ci.setTotalHarga(ci.getHargaSatuna() * (ci.getTotalBarang() + 1));
                                hitungCheckout();
                            }
                            
                        }
                        rs.close();
                    } catch(SQLException ex) {
                        ex.getLocalizedMessage();
                    }

                }

            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteKeranjang(int keranjang_id) {
        DatabaseConnection db = new DatabaseConnection();
        String query = """
                       DELETE FROM shopee_pink.keranjang\r
                       \tWHERE id=?;""" //
        ;
        try {
            Connection conn = db.getConnection();
            PreparedStatement pstm = conn.prepareStatement(query);

            pstm.setInt(1, keranjang_id);

            int rowsAffected = pstm.executeUpdate();

            if(rowsAffected > 0) {
                JPanel keranjangPanel = keranjangItemMap.get(keranjang_id);
                if (keranjangPanel != null) {
                    keranjangItemParent.remove(keranjangPanel);
                    keranjangItemParent.revalidate();
                    keranjangItemParent.repaint();
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static JPanel itemCard(int product_id, String imageUrl, String namaBarang, int harga, String stok, int potongan, String deskripsi) {

        ///Textfield helper
        JTextField productIdStore = new JTextField(String.valueOf(product_id));

        
        ///main code
        JPanel parent = new JPanel();
        parent.setLayout(new CardLayout(10,10));

        // Load the image and create JLabel
        ImageIcon originalIcon = new ImageIcon("img/" + imageUrl);
        JLabel imagelbl = new JLabel(originalIcon);

        // Adjust icon size dynamically based on JLabel size
        imagelbl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newSize = Math.min(imagelbl.getWidth(), imagelbl.getHeight()); // Use the smaller dimension
                if (newSize > 0) {
                    Image scaledImage = originalIcon.getImage().getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
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
        JTextPane deskripsiLbl = new JTextPane();
        deskripsiLbl.setContentType("text/html");
        deskripsiLbl.setText(deskripsi);
        deskripsiLbl.setEditable(false); // Make it non-editable
        deskripsiLbl.setOpaque(false);   // Make background transparent if needed

        // Limit the width
        int maxWidth = 200; // Set maximum width in pixels
        deskripsiLbl.setPreferredSize(new Dimension(maxWidth, deskripsiLbl.getPreferredSize().height));
        deskripsiLbl.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
        JScrollPane deskripsiScrollPane = new JScrollPane(deskripsiLbl);
        deskripsiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        deskripsiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        int maxHeight = 100; // Adjust maxHeight for the visible area height
        deskripsiScrollPane.setPreferredSize(new Dimension(maxWidth, maxHeight));
        
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
        imagePanel.setBackground(Color.white);
        cardPanel.add(imagePanel, ibc);
  
        ///nama barang
        ibc.gridx = 0;  
        ibc.gridy = 1; 
        ibc.weighty = 0;
        namaBarangLbl.setBorder(new EmptyBorder(5,0,0,0));
        cardPanel.add(namaBarangLbl, ibc);

        ibc.gridy = 2;
        deskripsiLbl.setBorder(new EmptyBorder(5,0,0,0));
        cardPanel.add(deskripsiScrollPane, ibc);

        ///harga barang
        ibc.gridx = 0;  
        ibc.gridy = 3; 
        ibc.weighty = 0;
        hargaLbl.setFont(new Font("Arial", Font.BOLD, 15));
        hargaLbl.setBorder(new EmptyBorder(5,0,5,0));
        hargaLbl.setForeground(Color.decode("#29A1F1"));
        cardPanel.add(hargaLbl, ibc);

        ///stok barang
        ibc.gridx = 0;  
        ibc.gridy = 4; 
        ibc.weighty = 0;
        cardPanel.add(stokLbl, ibc);

        ///add button
        JPanel oo = new JPanel();
        oo.setOpaque(false);
        oo.setLayout(new FlowLayout(FlowLayout.CENTER));
        oo.add(addBtn);
        ibc.gridy = 5; 
        cardPanel.add(oo, ibc);

        parent.add(cardPanel);
        parent.setBackground(Color.white);


        //event listener
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isBarangAda = cekKeranjang(Integer.parseInt(productIdStore.getText()), session.getId());
                int produk_id = Integer.parseInt(productIdStore.getText());

                if(!isBarangAda) {
                    insertProdukKeranjang(produk_id, session.getId());
                } else {
                    updateKeranjangQuantityFromItem(produk_id, session.getId(), 1);
                }
            }
        });

        return parent;
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

    public static void hitungCheckout() {
        if(!checkoutItem.isEmpty()) {
            int totalBarang = 0;
            int totalHarga = 0;
            for (Integer id : checkoutItem.keySet()) {
                CheckoutInfo cc = checkoutItem.get(id);
                totalBarang += cc.getTotalBarang();
                totalHarga += cc.getTotalHarga();
            }
            ketTotalBarang.setText("Total (" + totalBarang + " Produk): ");
            ketTotalHarga.setText(formatRupiah(totalHarga));
            checkOutBtn.setEnabled(true);
        } else {
            ketTotalBarang.setText("Total (0 Produk): ");
            ketTotalHarga.setText("Rp-");
            checkOutBtn.setEnabled(false);
        }
    }

    private void checkoutNow() {
        checkOutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if( session.getAlamat()[0] == null) {
                    JOptionPane.showMessageDialog(mainPage, "Alamat anda kosong : Tolong Isi alamat anda di bagian Profil terlebih dahulu!","Checkout Invalid",JOptionPane.WARNING_MESSAGE);
                } else {
                    mainPage.switchToCheckoutPage(checkoutItem);
                }
            }
        });
    }
}
