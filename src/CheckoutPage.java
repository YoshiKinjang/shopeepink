import auth.Session;
import database.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;

import javax.swing.*;
import javax.swing.border.*;

class JasaEkspedisi {
    private int id;
    private String nama;
    private String layanan;
    private int tarif_pr_kg;

    JasaEkspedisi(int id, String nama, String layanan, int tarif_pr_kg) {
        this.id = id;
        this.nama = nama;
        this.layanan = layanan;
        this.tarif_pr_kg = tarif_pr_kg;
    }

    public void setNama(String nama) {
        this.nama = nama;
    } 

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    } 

    public void setTarif(int tarif_per_kg) {
        this.tarif_pr_kg = tarif_per_kg;
    } 

    public String getNama() {
        return nama;
    }

    public String getLayanan() {
        return layanan;
    }

    public int getTarif() {
        return tarif_pr_kg;
    }

    @Override
    public String toString() {
        return nama + " - " + layanan;
    }
}

class KuponBelanja {
    private int id;
    private int potongan;

    KuponBelanja(int id, int potongan) {
        this.id = id;
        this.potongan = potongan;
    }

    public void setId(int id) {
        this.id = id;
    } 

    public void setPotongan(int potongan) {
        this.potongan = potongan;
    } 

    public int getId() {
        return id;
    }

    public int getPotongan() {
        return potongan;
    }

    @Override
    public String toString() {
        return  "Potongan harga " + potongan + "%";
    }
}

class CalculateHarga {
    private int hProduk;
    private int hEkspedisi = 0;
    private int hKupon = 0;
    
    public void sethProduk(int hProduk) {
        this.hProduk = hProduk;
    }
    public void sethEkspedisi(int hEkspedisi) {
        this.hEkspedisi = hEkspedisi;
    }
    public void sethKupon(int hKupon) {
        this.hKupon = hKupon;
    }

    public int gethProduk() {
        return hProduk;
    }
    public int gethEkspedisi() {
        return hEkspedisi;
    }
    public int gethKupon() {
        return hKupon;
    }
    public void clearData() {
        hProduk = 0;
        hEkspedisi = 0;
        hKupon = 0;
    }
}

public class CheckoutPage extends JPanel {

    private static MainPage mainPage;
    private static Map<Integer, CheckoutInfo> dataCheckout = new HashMap<>();
    private static Session session = Session.getInstance();
    static JPanel infoItemParent = new JPanel();

    static String[] alamatArr = {session.getAlamat()[0], session.getAlamat()[1], session.getAlamat()[2]};
    static JComboBox<String> alamatCmBox = new JComboBox<>(alamatArr);
    static JComboBox<JasaEkspedisi> jasaCmBox = new JComboBox<>();
    static JComboBox<KuponBelanja> kuponCmBox = new JComboBox<>();
    static CalculateHarga calculateHarga = new CalculateHarga();

    static JLabel conProdLbl = new JLabel("-");
    static JLabel conKirimLbl = new JLabel("-");
    static JLabel conKuponLbl = new JLabel("-");
    static JLabel conTotalLbl = new JLabel("-");

    static JButton kembaliBtn = new JButton("Kembali");
    static ButtonGroup mtdGroup = new ButtonGroup();
    static String payment_method;

    CheckoutPage(MainPage mainPage, Map<Integer, CheckoutInfo> dataCheckout) {

        this.mainPage = mainPage;
        this.dataCheckout = dataCheckout;

        // for (Integer id : dataCheckout.keySet()) {
        //     CheckoutInfo cc = dataCheckout.get(id);
        //     System.out.print(cc.getIdBarang() + ",");
        // }
        
        setLayout(new CardLayout(0,0));

        JPanel parent = new JPanel();
        parent.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel topBarParent = new JPanel();
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

        JLabel checkoutTitle = new JLabel("Checkout");
        checkoutTitle.setFont(new Font("Arial", Font.BOLD, 24));
        

        ///logo icon
        // tbp_gbc.fill = GridBagConstraints.BOTH;  
        // tbp_gbc.weightx = 0.5;  
        // tbp_gbc.gridx = 0;  
        // tbp_gbc.gridy = 0; 
        // tbp_gbc.anchor = GridBagConstraints.LINE_START;
        // topBarParent.add(logoLabel, tbp_gbc);

        tbp_gbc.fill = GridBagConstraints.CENTER;  
        tbp_gbc.weightx = 1;  
        tbp_gbc.gridx = 1;  
        tbp_gbc.gridy = 0; 
        tbp_gbc.anchor = GridBagConstraints.CENTER;
        topBarParent.add(checkoutTitle, tbp_gbc);
      
        ///user icon
        // tbp_gbc.fill = GridBagConstraints.BOTH;  
        // tbp_gbc.weightx = 0.5; 
        // tbp_gbc.gridx = 2;  
        // tbp_gbc.gridy = 0; 
        // userLbl.setHorizontalAlignment(JTextField.CENTER);
        // topBarParent.add(userLbl, tbp_gbc);

        topBarParent.setBackground(Color.white);

        gbc.fill = GridBagConstraints.BOTH;  
        gbc.weightx = 1;  
        gbc.gridx = 0;  
        gbc.gridy = 0; 
        gbc.weighty = 0.2;
        parent.add(topBarParent, gbc);

        ///mainSection
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new CardLayout());
        mainContainer.add(mainPanel());

        gbc.fill = GridBagConstraints.BOTH;  
        gbc.weightx = 1;  
        gbc.gridx = 0;  
        gbc.gridy = 1; 
        gbc.weighty = 2;
        parent.add(mainContainer, gbc);

        add(parent);
    }

    public static JPanel mainPanel() {
        ///main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints mpGbc = new GridBagConstraints();

        //first child
        JPanel informasiBarangPanel = new JPanel();
        informasiBarangPanel.setLayout(new BorderLayout());
        informasiBarangPanel.setBackground(Color.white);
        informasiBarangPanel.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.lightGray));
        
        //populate first child component
        JLabel firstTitle = new JLabel("Informasi barang");
        firstTitle.setFont(new Font("Arial", Font.BOLD, 24));
        firstTitle.setBorder(new EmptyBorder(20,20,20,20));
        informasiBarangPanel.add(firstTitle, BorderLayout.NORTH);

        infoItemParent.setBackground(Color.white);
        infoItemParent.setLayout(new BoxLayout(infoItemParent, BoxLayout.Y_AXIS));

        // for (Integer elem : dataCheckout.keySet()) {
        //     System.out.println(elem);
        //     infoItemParent.add(checkoutItem("2", "2", 2, 2));
        // }
        
        JPanel wrapperInfo = new JPanel(new BorderLayout());
        wrapperInfo.setBackground(Color.white);
        wrapperInfo.add(infoItemParent, BorderLayout.NORTH);

        // Add the wrapper panel to a JScrollPane for scrolling
        JScrollPane scrollInfo = new JScrollPane(wrapperInfo);
        scrollInfo.setBackground(Color.white);
        scrollInfo.setBorder(BorderFactory.createLineBorder(Color.lightGray, 0));
        scrollInfo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollInfo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        informasiBarangPanel.add(scrollInfo, BorderLayout.CENTER);


        mpGbc.fill = GridBagConstraints.BOTH;
        mpGbc.weightx = 0.5;  
        mpGbc.gridx = 0;
        mpGbc.gridy = 0;
        mpGbc.weighty = 1;
        mainPanel.add(informasiBarangPanel, mpGbc);



        //second child
        JPanel checkoutForm = new JPanel();
        checkoutForm.setBackground(Color.decode("#FF659B"));
        checkoutForm.setLayout(new BorderLayout());
        mpGbc.weightx = 2;  
        mpGbc.gridx = 1;
        mpGbc.gridy = 0;
        mpGbc.weighty = 1;

        checkoutForm.add(checkoutForm());

        mainPanel.add(checkoutForm, mpGbc);

        showCheckoutItem();

        return mainPanel; 
    }

    public static void showCheckoutItem() {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query =  """
                        select gambar, nama from product p \r
                        where id = ?;""" //
        ;

        infoItemParent.removeAll();
        for (Integer id : dataCheckout.keySet()) {
            ResultSet resultSet = null;
            
            try {
                Connection conn  = dtbs.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                CheckoutInfo cc = dataCheckout.get(id);
                pstmt.setInt(1, cc.getIdBarang());
                resultSet = pstmt.executeQuery();
    
                while (resultSet.next()) { 
                    String gambar = resultSet.getString("gambar");
                    String nama = resultSet.getString("nama");
                    infoItemParent.add(checkoutItem(gambar, nama, cc.getTotalHarga(), cc.getTotalBarang()));
                    infoItemParent.revalidate();
                    infoItemParent.repaint();
                }
    
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static JPanel checkoutItem(String gambar, String namaBarang, int hargaBarang, int kuantitasBarang) {
        JPanel keranjangItem = new JPanel();
        keranjangItem.setBackground(Color.white);

        keranjangItem.setLayout(new GridBagLayout());
        keranjangItem.setBorder(new EmptyBorder(15,15,15,15));
        GridBagConstraints krGbc = new GridBagConstraints();

        JPanel imageCon = new JPanel();
        ImageIcon gambarBarang = new ImageIcon("img/" + gambar);
        JLabel gambarBarangLbl = new JLabel(gambarBarang);
        imageCon.setPreferredSize(new Dimension(80,80));
        imageCon.add(gambarBarangLbl);
        
        
        JPanel infoItemParent = new JPanel();
        infoItemParent.setBackground(Color.white);
        infoItemParent.setLayout(new GridLayout(3,1,10,0));

        JLabel namaBarangLbl = new JLabel(namaBarang);
        JLabel kuantitasBarangLbl = new JLabel("Kuantitas: " + kuantitasBarang);
        namaBarangLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel hargaLbl = new JLabel(formatRupiah(hargaBarang));
        hargaLbl.setFont(new Font("Arial", Font.BOLD, 16));
        hargaLbl.setForeground(Color.BLUE);

        infoItemParent.add(namaBarangLbl);
        infoItemParent.add(kuantitasBarangLbl);
        infoItemParent.add(hargaLbl);


        krGbc.fill = GridBagConstraints.BOTH;
        krGbc.weightx = 0;
        krGbc.gridx = 1;
        krGbc.gridy = 0;
        krGbc.insets = new Insets(0, 0, 0, 30);
        keranjangItem.add(imageCon, krGbc);

        krGbc.fill = GridBagConstraints.BOTH;
        krGbc.weightx = 1;
        krGbc.gridx = 2;
        krGbc.gridy = 0;
        krGbc.insets = new Insets(0, 0, 0, 0);
        keranjangItem.add(infoItemParent, krGbc);

        return keranjangItem;
    }

    public static JScrollPane checkoutForm() {
        JPanel vv = new JPanel();
        vv.setLayout(new CardLayout(30,30));
        JScrollPane ii = new JScrollPane(vv);
        ii.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ii.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JPanel container2 = new JPanel();
        container2.setLayout(new GridBagLayout());
        GridBagConstraints conGbc = new GridBagConstraints();
        JPanel conDetailHarga = new JPanel();
        conDetailHarga.setLayout(new GridBagLayout());
        GridBagConstraints cdhGbc = new GridBagConstraints();
        ///declare the component here
        JLabel alamatLbl = new JLabel("Alamat pengiriman");
        alamatLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel jasaLbl = new JLabel("Jasa pengiriman");
        jasaLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel kuponLbl = new JLabel("Kupon diskon");
        kuponLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel subProdLbl = new JLabel("Subtotal produk");
        subProdLbl.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel subKirimLbl = new JLabel("Subtotal pengiriman");
        subKirimLbl.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel subKuponLbl = new JLabel("Potongan diskon");
        subKuponLbl.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel subTotalLbl = new JLabel("Total Pembayaran");
        subTotalLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel mtdlLbl = new JLabel("Metode pembayaran");
        mtdlLbl.setFont(new Font("Arial", Font.BOLD, 16));
        conProdLbl.setFont(new Font("Arial", Font.BOLD, 16));
        conKirimLbl.setFont(new Font("Arial", Font.BOLD, 16));
        conKuponLbl.setFont(new Font("Arial", Font.BOLD, 16));
        conTotalLbl.setFont(new Font("Arial", Font.BOLD, 16));
        
        conProdLbl.setText(formatRupiah(getTotalHargaItem()));
        calculateHarga.sethProduk(getTotalHargaItem());

        getJasaEkspedisi();
        Timer timer = new Timer(1000, e -> jasaCmBox.setSelectedIndex(0)); // Select "Option 3" after 1 second
        timer.setRepeats(false);
        timer.start();

        getKupon();
        Timer timer2 = new Timer(1000, e -> kuponCmBox.setSelectedIndex(0)); // Select "Option 3" after 1 second
        timer2.setRepeats(false);
        timer2.start();

        Timer timer3 = new Timer(1000, e -> alamatCmBox.setSelectedIndex(0)); // Select "Option 3" after 1 second
        timer3.setRepeats(false);
        timer3.start();

        conTotalLbl.setText(formatRupiah(getTotalPembayaran()));
        

        JPanel rbCon = new JPanel();
        rbCon.setLayout(new FlowLayout(FlowLayout.CENTER));
        rbCon.setBackground(Color.white);
        rbCon.setBorder(new EmptyBorder(10,10,10,10));
        JRadioButton codRad = new JRadioButton("COD");
        codRad.setFont(new Font("Arial", Font.BOLD, 16));
        JRadioButton virRad = new JRadioButton("Qris");
        virRad.setFont(new Font("Arial", Font.BOLD, 16));
        mtdGroup.add(codRad);
        mtdGroup.add(virRad);
        rbCon.add(codRad);
        rbCon.add(virRad);

        JPanel tt = new JPanel();
        tt.setLayout(new BorderLayout());
        kembaliBtn.setFont(new Font("Arial", Font.BOLD, 16));
        JButton submitBtn = new JButton("Submit pesanan");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        tt.add(kembaliBtn, BorderLayout.WEST);
        tt.add(submitBtn, BorderLayout.EAST);

        cdhGbc.fill = GridBagConstraints.BOTH;
        cdhGbc.anchor = GridBagConstraints.WEST;
        cdhGbc.insets = new Insets(0, 20, 20, 0);
        cdhGbc.gridx = 0;
        cdhGbc.gridy = 0;
        conDetailHarga.add(subProdLbl, cdhGbc);
        cdhGbc.gridx = 1;
        conDetailHarga.add(conProdLbl, cdhGbc);
        cdhGbc.gridx = 0;
        cdhGbc.gridy = 1;
        conDetailHarga.add(subKirimLbl, cdhGbc);
        cdhGbc.gridx = 1;
        conDetailHarga.add(conKirimLbl, cdhGbc);
        cdhGbc.gridx = 0;
        cdhGbc.gridy = 2;
        conDetailHarga.add(subKuponLbl, cdhGbc);
        cdhGbc.gridx = 1;
        conDetailHarga.add(conKuponLbl, cdhGbc);
        cdhGbc.gridx = 0;
        cdhGbc.gridy = 3;
        conDetailHarga.add(subTotalLbl, cdhGbc);
        cdhGbc.gridx = 1;
        conDetailHarga.add(conTotalLbl, cdhGbc);


        conGbc.fill = GridBagConstraints.BOTH;
        conGbc.gridx = 0;
        conGbc.weightx = 1;
        conGbc.gridy = 0;
        conGbc.insets = new Insets(0, 0, 20, 0);
        container2.add(alamatLbl, conGbc);
        conGbc.gridy = 1;
        container2.add(alamatCmBox, conGbc);
        conGbc.gridy = 2;
        container2.add(jasaLbl, conGbc);
        conGbc.gridy = 3;
        container2.add(jasaCmBox, conGbc);
        conGbc.gridy = 4;
        container2.add(kuponLbl, conGbc);
        conGbc.gridy = 5;
        container2.add(kuponCmBox, conGbc);
        conGbc.fill = GridBagConstraints.NONE;
        conGbc.weightx = 0;
        conGbc.anchor = GridBagConstraints.EAST;
        conGbc.gridy = 6;
        container2.add(conDetailHarga, conGbc);
        conGbc.fill = GridBagConstraints.BOTH;
        conGbc.weightx = 1;
        conGbc.gridy = 7;
        container2.add(mtdlLbl, conGbc);
        conGbc.fill = GridBagConstraints.NONE;
        conGbc.gridy = 8;
        conGbc.weightx = 0;
        conGbc.anchor = GridBagConstraints.WEST;
        container2.add(rbCon, conGbc);
        conGbc.fill = GridBagConstraints.BOTH;
        conGbc.gridy = 9;
        conGbc.weightx = 1;
        conGbc.anchor = GridBagConstraints.CENTER;
        container2.add(tt, conGbc);

        

        container.add(container2, BorderLayout.NORTH);
        vv.add(container);

        ///event listener
        jasaCmBox.addActionListener(e -> {
            JasaEkspedisi selectedItem = (JasaEkspedisi) jasaCmBox.getSelectedItem();
            KuponBelanja selectedItemKupon = (KuponBelanja) kuponCmBox.getSelectedItem();
            if (selectedItem != null) {
                int potongan = selectedItemKupon.getPotongan();
                int tarif = selectedItem.getTarif();
                int beratTotal = getTotalBeratItem();

                int biayaPengiriman = tarif * beratTotal;
                conKirimLbl.setText(formatRupiah(biayaPengiriman));

                int biayaProduk = getTotalHargaItem();
                int totalPotongan = (int) ((biayaPengiriman + biayaProduk) * potongan/100.0);
                conKuponLbl.setText(formatRupiah(totalPotongan));

                calculateHarga.sethEkspedisi(biayaPengiriman);
                calculateHarga.sethKupon(totalPotongan);
                
                conTotalLbl.setText(formatRupiah(getTotalPembayaran()));
            } else {
                conKirimLbl.setText("-");
                calculateHarga.sethEkspedisi(0);
                conTotalLbl.setText(formatRupiah(getTotalPembayaran()));
            }
        });

        kuponCmBox.addActionListener(e -> {
            KuponBelanja selectedItemKupon = (KuponBelanja) kuponCmBox.getSelectedItem();
            JasaEkspedisi selectedItemEks = (JasaEkspedisi) jasaCmBox.getSelectedItem();
            if (selectedItemKupon != null) {
                int potongan = selectedItemKupon.getPotongan();
                int biayaProduk = getTotalHargaItem();
                if (selectedItemEks != null) {
                    int tarif = selectedItemEks.getTarif();
                    int beratTotal = getTotalBeratItem();
                    int biayaPengiriman = tarif * beratTotal;

                    int totalPotongan = (int) ((biayaPengiriman + biayaProduk) * potongan/100.0);
                    conKuponLbl.setText(formatRupiah(totalPotongan));
                    calculateHarga.sethKupon(totalPotongan);
                    conTotalLbl.setText(formatRupiah(getTotalPembayaran()));
                } else {
                    int totalPotongan = (int) ((biayaProduk) * potongan/100.0);
                    conKuponLbl.setText(formatRupiah(totalPotongan));
                    calculateHarga.sethKupon(totalPotongan);
                    conTotalLbl.setText(formatRupiah(getTotalPembayaran()));
                }
            } else {
                conKirimLbl.setText("-");
            }
        });

        kembaliBtn.addActionListener(e -> backHome());

        codRad.addActionListener(e ->  {
            payment_method = "COD";
        });

        virRad.addActionListener(e ->  {
            payment_method = "Qris";
        });

        submitBtn.addActionListener(e -> submitPesananan());

        return ii;
    }

    public static void getJasaEkspedisi() {
        DatabaseConnection dtbs = new DatabaseConnection();

        String query =  """
                        select * from jasa_ekspedisi je;""" //
        ;

        ResultSet resultSet = null;
        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            resultSet = pstmt.executeQuery();

            jasaCmBox.removeAllItems();
            while (resultSet.next()) { 
                int id = resultSet.getInt("id");
                String nama_ekspedisi = resultSet.getString("nama_ekspedisi");
                String layanan = resultSet.getString("layanan");
                int tarif = resultSet.getInt("tarif_per_kg");

                jasaCmBox.addItem(new JasaEkspedisi(id, nama_ekspedisi, layanan, tarif));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void getKupon() {
        DatabaseConnection dtbs = new DatabaseConnection();

        String query =  """
                        select * from kupon_belanja kb \r
                        where kb.customer_id = ? and isUse = 0;"""
        ;

        ResultSet resultSet = null;
        try {
            Connection conn  = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            pstmt.setInt(1, session.getId());
            resultSet = pstmt.executeQuery();

            kuponCmBox.removeAllItems();
            while (resultSet.next()) { 
                int id = resultSet.getInt("id");
                int potongan = resultSet.getInt("potongan_persen");
                kuponCmBox.addItem(new KuponBelanja(id, potongan)); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getTotalHargaItem() {
        int total = 0;
        for (Integer id : dataCheckout.keySet()) {
            CheckoutInfo cc = dataCheckout.get(id);
            total += cc.getTotalHarga();
        }
        return total;
    }

    public static int getTotalBeratItem() {
        int total = 0;
        for (Integer id : dataCheckout.keySet()) {
            CheckoutInfo cc = dataCheckout.get(id);
            total += cc.getTotalBerat();
        }
        return total;
    }

    public static int getTotalPembayaran() {
        return calculateHarga.gethProduk() + calculateHarga.gethEkspedisi() - calculateHarga.gethKupon();   
    }

    public static void backHome() {
        mainPage.switchToHomePage();
        calculateHarga.clearData();
        infoItemParent.removeAll();
    }

    public static void submitPesananan() {
        String nomor_resi = generateNomorResi();
        int customer_id = session.getId();
        String alamat_pengiriman = alamatCmBox.getSelectedItem().toString();
        int total_harga_pesanan = getTotalPembayaran();
        JasaEkspedisi selectedItemEks = (JasaEkspedisi) jasaCmBox.getSelectedItem();
        String jasa_ekspedisi = selectedItemEks.getNama() + " - " + selectedItemEks.getLayanan();
        String order_status = "Disiapkan";
        String payment = payment_method;
        StringJoiner produk = new StringJoiner(",", "[", "]");
        for (Integer id : dataCheckout.keySet()) {
            CheckoutInfo cc = dataCheckout.get(id);
            produk.add(String.valueOf(cc.getIdBarang()));
        }
        String order_produk  = produk.toString();
        StringJoiner quantity = new StringJoiner(",", "[", "]");
        for (Integer id : dataCheckout.keySet()) {
            CheckoutInfo cc = dataCheckout.get(id);
            quantity.add(String.valueOf(cc.getTotalBarang()));
        }
        String order_quantity  = quantity.toString();

        if(alamat_pengiriman == null || jasa_ekspedisi == null || payment == null) {
            JOptionPane.showMessageDialog(new MainPage(),"Lengkapi data checkout.","Kesalahan",JOptionPane.ERROR_MESSAGE); 
        } else {
            DatabaseConnection dtbs = new DatabaseConnection();
            String query = """
                       INSERT INTO shopee_pink.pesanan (nomor_resi,customer_id,order_date,alamat_pengiriman,total_harga_pesanan,jasa_ekspedisi,order_status,payment_method,order_produk,order_quantity)\r
                       \tVALUES (?,?,DEFAULT,?,?,?,?,?,?,?);""" //
            ;
            switch (payment) {
                case "COD":
                        try {
                            Connection conn = dtbs.getConnection();
                            PreparedStatement pstmt = conn.prepareStatement(query);
            
                            pstmt.setString(1, nomor_resi);
                            pstmt.setInt(2, session.getId());
                            pstmt.setString(3, alamat_pengiriman);
                            pstmt.setInt(4, total_harga_pesanan);
                            pstmt.setString(5, jasa_ekspedisi);
                            pstmt.setString(6, order_status);
                            pstmt.setString(7, payment);
                            pstmt.setString(8, order_produk);
                            pstmt.setString(9, order_quantity);
            
                            int rowsAffrected = pstmt.executeUpdate();
            
                            if(rowsAffrected > 0) {

                                for (Integer elem : dataCheckout.keySet()) {
                                    CheckoutInfo cc = dataCheckout.get(elem);
                                    deleteKeranjang(cc.getIdBarang(), session.getId());
                                    updateStok(cc.getIdBarang(), cc.getTotalBarang());
                                }

                                JDialog successDialog = new JDialog(new MainPage(), "Daftar akun", true);
                                successDialog.setSize(700, 300);
                                successDialog.setLayout(new CardLayout(50,50));
                                successDialog.setLocationRelativeTo(new MainPage());

                                JPanel pp = new JPanel();
                                pp.setLayout(new BorderLayout());
                                successDialog.add(pp);

                                JLabel label = new JLabel("<html><div style='text-align: center'><b>Pemesanan Berhasil<br>Siapkan uang tunai untuk membayar barang anda ketika sudah sampai ke rumah anda</b></div></html>");
                                label.setFont(new Font("Arial", Font.BOLD, 24));
                                pp.add(label, BorderLayout.CENTER);

                                JButton closeButton = new JButton("Konfirmasi");
                                pp.add(closeButton, BorderLayout.SOUTH);
                                
                                closeButton.addActionListener(ev -> {
                                    mainPage.resetHomePage();
                                    successDialog.dispose();
                                });

                                successDialog.setVisible(true);
                            }
            
                        } catch (SQLException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    break;
                case "Qris":
                        JDialog qrisDialog = new JDialog(new MainPage(), "Bayar Qris", true);
                        qrisDialog.setSize(700, 300);
                        qrisDialog.setLayout(new CardLayout(50,50));
                        qrisDialog.setLocationRelativeTo(new MainPage());

                        JPanel pp = new JPanel();
                        pp.setLayout(new BorderLayout());
                        qrisDialog.add(pp);

                        JLabel label = new JLabel("<html><div style='text-align: center'><b>Scan kode QR dibawah ini</b></div></html>");
                        label.setFont(new Font("Arial", Font.BOLD, 24));
                        pp.add(label, BorderLayout.NORTH);

                        ImageIcon qrisImage = new ImageIcon("img/qris.png");
                        JLabel qrisLbl = new JLabel(qrisImage);

                        pp.add(qrisLbl, BorderLayout.CENTER);

                        JButton closeButton = new JButton("Konfirmasi");
                        pp.add(closeButton, BorderLayout.SOUTH);
                        
                        closeButton.addActionListener(ev -> {
                            qrisDialog.dispose();
                            try {
                                Connection conn = dtbs.getConnection();
                                PreparedStatement pstmt = conn.prepareStatement(query);
                
                                pstmt.setString(1, nomor_resi);
                                pstmt.setInt(2, session.getId());
                                pstmt.setString(3, alamat_pengiriman);
                                pstmt.setInt(4, total_harga_pesanan);
                                pstmt.setString(5, jasa_ekspedisi);
                                pstmt.setString(6, order_status);
                                pstmt.setString(7, payment);
                                pstmt.setString(8, order_produk);
                                pstmt.setString(9, order_quantity);
                
                                int rowsAffrected = pstmt.executeUpdate();
                
                                if(rowsAffrected > 0) {
    
                                    for (Integer elem : dataCheckout.keySet()) {
                                        CheckoutInfo cc = dataCheckout.get(elem);
                                        deleteKeranjang(cc.getIdBarang(), session.getId());
                                        updateStok(cc.getIdBarang(), cc.getTotalBarang());
                                    }
    
                                    JDialog successDialog = new JDialog(new MainPage(), "Daftar akun", true);
                                    successDialog.setSize(700, 300);
                                    successDialog.setLayout(new CardLayout(50,50));
                                    successDialog.setLocationRelativeTo(new MainPage());
    
                                    JPanel tt = new JPanel();
                                    tt.setLayout(new BorderLayout());
                                    successDialog.add(tt);
    
                                    JLabel label2 = new JLabel("<html><div style='text-align: center'><b>Pemesanan Berhasil<br>Barang siap meluncur ke rumah anda</b></div></html>");
                                    label2.setFont(new Font("Arial", Font.BOLD, 24));
                                    tt.add(label2, BorderLayout.CENTER);
    
                                    JButton closeButton2 = new JButton("Konfirmasi");
                                    tt.add(closeButton2, BorderLayout.SOUTH);
                                    
                                    closeButton2.addActionListener(ev2 -> {
                                        mainPage.resetHomePage();
                                        successDialog.dispose();
                                    });
    
                                    successDialog.setVisible(true);
                                }
                
                            } catch (SQLException e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                        });

                        qrisDialog.setVisible(true);
                    
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }
    public static String generateNomorResi() {
        Random random = new Random();
        char randomLetter = (char) ('A' + random.nextInt(26));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String formattedDateTime = now.format(formatter);
        return randomLetter + formattedDateTime;
    }

    public static void deleteKeranjang(int product_id, int customer_id) {
        DatabaseConnection db = new DatabaseConnection();
        String query = """
                       DELETE FROM shopee_pink.keranjang\r
                       \tWHERE product_id=? and customer_id=?;""" //
        ;
        try {
            Connection conn = db.getConnection();
            PreparedStatement pstm = conn.prepareStatement(query);

            pstm.setInt(1, product_id);
            pstm.setInt(2, customer_id);


            int rowsAffected = pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void updateStok(int product_id, int quantity) {
        DatabaseConnection db = new DatabaseConnection();
        String query = """
                       update product \r
                       set stok = stok - ?\r
                       where id = ?;""" //
        //
        ;

        try {
            Connection conn = db.getConnection();
            PreparedStatement psmt = conn.prepareStatement(query);

            psmt.setInt(1, quantity);
            psmt.setInt(2, product_id);

            int rowsEffected = psmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static String formatRupiah(int amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("id").setRegion("ID").build());
        return currencyFormat.format(amount);
    }
}
