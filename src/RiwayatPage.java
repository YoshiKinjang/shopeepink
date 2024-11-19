import auth.Session;
import database.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.naming.spi.DirStateFactory;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RiwayatPage extends JPanel {

    private MainPage mainPage;
    private JPanel parent;
    private JLabel tittle;
    private JButton backBtn;
    private JPanel riwayatContainer;
    private JPanel riwayatParentCard;
    private JButton refundBtn;
    private JButton showAjuan;
    private JLabel photoLabel;
    private final String saveFolder = "assets/refund";
    private File gambar_refund;
    private JDialog jd;

    RiwayatPage(MainPage mainPage) {
        setLayout(new CardLayout());
        setBorder(new EmptyBorder(50,200,0,200));

        this.mainPage = mainPage;
        parent = new JPanel();
        parent.setLayout(new BorderLayout());
        parent.setOpaque(false);

        JPanel cz = new JPanel();
        cz.setLayout(new BorderLayout());
        cz.setBorder(new EmptyBorder(0,10,30,0));

        backBtn = new JButton("Kembali ke Home");
        
        tittle = new JLabel("Riwayat Pemesanan");
        tittle.setFont(new Font("Arial", Font.BOLD, 24));

        cz.add(tittle, BorderLayout.WEST);
        cz.add(backBtn, BorderLayout.EAST);

        riwayatContainer = new JPanel();
        riwayatContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        riwayatContainer.setOpaque(false);

        getRiwayatItem();

        JScrollPane pn = new JScrollPane(riwayatContainer);
        pn.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pn.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pn.setBorder(null);

        pn.getViewport().addChangeListener(e -> adjustPreferredSize());
        riwayatContainer.addContainerListener(new java.awt.event.ContainerAdapter() {
            @Override
            public void componentAdded(java.awt.event.ContainerEvent e) {
                adjustPreferredSize();
            }
        });

        pn.getVerticalScrollBar().addAdjustmentListener(e -> {
            e.getAdjustable().setUnitIncrement(16); // Set an increment value that feels smooth and responsive
        });

        backBtn.addActionListener(e -> mainPage.switchToHomePage());

        parent.add(cz, BorderLayout.NORTH);
        parent.add(pn, BorderLayout.CENTER);
        add(parent);

        adjustPreferredSize();
    }

    private void adjustPreferredSize() {
        SwingUtilities.invokeLater(() -> {
            int viewportWidth = riwayatContainer.getParent().getWidth(); // Parent width
            if (viewportWidth <= 0) return; // Skip if parent not sized yet
    
            int totalHeight = 0;
            int rowHeight = 0;
            int usedWidth = 0;
    
            for (Component comp : riwayatContainer.getComponents()) {
                Dimension compSize = comp.getPreferredSize();
                if (usedWidth + compSize.width > viewportWidth) {
                    totalHeight += rowHeight; // Add row height
                    rowHeight = 0;
                    usedWidth = 0; // New row
                }
                usedWidth += compSize.width + 70; // Add gap
                rowHeight = Math.max(rowHeight, compSize.height);
            }
    
            totalHeight += rowHeight; // Add last row height
            riwayatContainer.setPreferredSize(new Dimension(viewportWidth, totalHeight));
            riwayatContainer.revalidate();
        });
    }

    private void getRiwayatItem() {
        // Wrapper panel to ensure full width
        DatabaseConnection dtbs = new DatabaseConnection();
        String query = """
                       select * from pesanan p\r
                       left join refund r on r.order_id = p.id_order \r
                       where customer_id = ?;""" 
        ;
        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            // System.out.println(Session.getInstance().getId());
            pstmt.setInt(1, Session.getInstance().getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                int id          = rs.getInt("id_order");
                int refund_id   = rs.getInt("id");
                int pengembalian_method = rs.getInt("pengembalian_method");
                int pengembalian_dana = rs.getInt("pengembalian_dana");
                String ket_admin = rs.getString("keterangan_refund_admin");
                String refund_status = rs.getString("refund_status");
                String resi     = rs.getString("nomor_resi");
                String tanggal  = rs.getString("order_date");
                String alamat   = rs.getString("alamat_pengiriman");
                int harga       = rs.getInt("total_harga_pesanan");
                String ekspedisi      = rs.getString("jasa_ekspedisi");
                String status   = rs.getString("order_status");
                String payment_method = rs.getString("payment_method");
                String order_produk   = rs.getString("order_produk");
                int[] produkArr = Arrays.stream(order_produk.substring(1, order_produk.length() - 1).split(","))
                                  .mapToInt(Integer::parseInt)
                                  .toArray();
                String order_quantity = rs.getString("order_quantity");
                int[] quantityArr = Arrays.stream(order_quantity.substring(1, order_quantity.length() - 1).split(","))
                                  .mapToInt(Integer::parseInt)
                                  .toArray();
                int[][] combinedArray = new int[produkArr.length][2];

                for (int i = 0; i < produkArr.length; i++) {
                    combinedArray[i][0] = produkArr[i];
                    combinedArray[i][1] = quantityArr[i];
                }
            
                riwayatContainer.add(createRiwayatCardItem(id, refund_id, pengembalian_method, pengembalian_dana, ket_admin, refund_status ,resi, tanggal, alamat, harga, ekspedisi, status, payment_method, combinedArray));

                riwayatContainer.revalidate();
                riwayatContainer.repaint();


                

            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public String getNamaBarang(int id_barang) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query = "select nama from product p where id = ?;";
        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, id_barang);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nama = rs.getString("nama");
                return nama;
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return "";
    }

    public JPanel createRiwayatCardItem(int id, int refund_id, int pengembalian_method, int pengembalian_dana, String ket_admin, String refund_status, String resi, String tanggal, String alamat, int harga, String ekspedisi, String status, String payment_method, int[][] produk) {

        ///helper
        JTextField cc = new JTextField(resi);
        JTextField idTF = new JTextField(String.valueOf(id));
        JTextField refId = new JTextField(String.valueOf(refund_id));
        JTextField danaDikembalikan = new JTextField(formatRupiah(pengembalian_dana));
        JTextField ketAdminTf = new JTextField(ket_admin);


        
        riwayatParentCard = new JPanel();
        riwayatParentCard.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20,20,20,20));
        header.setBackground(Color.decode("#FF659B"));
        JLabel tanggalLabel = new JLabel("<html>" + tanggal + "<html>");
        JLabel statusLabel = new JLabel(status);
        header.add(tanggalLabel, BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body ,BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20,20,20,20));
        body.setBackground(Color.white);

        String produk2[][] = new String[produk.length][2];
        for (int i = 0; i < produk.length; i++) {
            produk2[i][0] = getNamaBarang(produk[i][0]);
            produk2[i][1] = String.valueOf(produk[i][1]);

        }
        
        String column[]={"Nama Barang","Quantity"};         
        JTable jt=new JTable(produk2,column);       
        JScrollPane sp=new JScrollPane(jt);  
        sp.setPreferredSize(new Dimension(200, 100));

        JLabel hargaTotal = new JLabel("Total Pesanan: " + formatRupiah(harga));
        hargaTotal.setBorder(new EmptyBorder(30,0,0,0));
        JLabel alamatLabel = new JLabel("Alamat Pengiriman: " + alamat);
        JLabel ekspedisiLabel = new JLabel("Jasa ekspedisi:" + ekspedisi);
        JLabel payment = new JLabel("Pembayaran: " + payment_method);
        body.add(sp);
        body.add(hargaTotal);
        body.add(alamatLabel);
        body.add(ekspedisiLabel);
        body.add(payment);


        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout());
        Border topBorder = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.lightGray);
        Border leftPadding = new EmptyBorder(20, 20, 20, 20);
        footer.setBorder(BorderFactory.createCompoundBorder(topBorder, leftPadding));
        footer.setBackground(Color.white);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date inputDate = null;
        try {
            inputDate = sdf.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date();
        long diffInMillis = currentDate.getTime() - inputDate.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if(status.equals("diterima")) {
            JButton selesaiBtn = new JButton("Selesai");
            footer.add(selesaiBtn, BorderLayout.WEST);
            selesaiBtn.addActionListener(e -> {
                int a = JOptionPane.showConfirmDialog(this, "Anda yakin?");
                if(a == JOptionPane.YES_OPTION) 
                    selesaikanPesanan(Integer.parseInt(idTF.getText()));
            });
        }
        if (diffInDays < 7 && refund_id == 0 && status.equals("selesai")) {
            refundBtn = new JButton("Refund");
            footer.add(refundBtn, BorderLayout.WEST);
            refundBtn.addActionListener(e -> ajukanRefundDialog(Integer.parseInt(idTF.getText()), cc.getText()));
        } 
        if (refund_id != 0 && refund_status.equals("Pending")) {
            showAjuan = new JButton("Pending refund");
            footer.add(showAjuan, BorderLayout.WEST);
            showAjuan.addActionListener(e -> pendingRefundDialog(Integer.parseInt(refId.getText()), cc.getText()));
        }
        if(refund_id != 0 && refund_status.equals("Ditolak")) {
            JButton ditolakBtn = new JButton("Refund ditolak");
            footer.add(ditolakBtn, BorderLayout.WEST);
            ditolakBtn.addActionListener(e -> ditolakRefundDialog(Integer.parseInt(refId.getText()), cc.getText()));
        }
        if(refund_id != 0 && refund_status.equals("Diterima")) {
            JButton diterimaBtn = new JButton("Refund diterima");
            footer.add(diterimaBtn, BorderLayout.WEST);
            diterimaBtn.addActionListener(e -> diterimaRefundDialog(Integer.parseInt(refId.getText()), cc.getText()));
        }
        if(refund_id != 0 && refund_status.equals("Diterima") && (pengembalian_method != 0)) {
            JLabel hj = new JLabel("Refund sedang diproses");
            footer.add(hj, BorderLayout.WEST);
        }
        if(refund_id != 0 && refund_status.equals("Berhasil")) {
            JButton berhasilBtn = new JButton("Refunded");
            footer.add(berhasilBtn, BorderLayout.WEST);
            berhasilBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "<html><b><big>Barang berhasil direfund<big><b><br><b>Dana yang dikembalikan: <b>" + danaDikembalikan.getText() + "<br><b>Keterangan admin: <b>" + ketAdminTf.getText() +"<html>"));
        }

        System.out.println(refund_status);

        JLabel resiLabel = new JLabel(resi);
        footer.add(resiLabel, BorderLayout.EAST);


        riwayatParentCard.add(header, BorderLayout.NORTH);
        riwayatParentCard.add(body, BorderLayout.CENTER);
        riwayatParentCard.add(footer, BorderLayout.SOUTH);

        riwayatParentCard.setPreferredSize(null); // Remove any fixed preferred size
        riwayatParentCard.revalidate(); // Ensure it recalculates the layout
        riwayatParentCard.repaint(); // Ensure visual updates

        return riwayatParentCard;
    }

    ///not yet refund
    public void ajukanRefundDialog(int id_order, String resi) {

        ///helper
        JTextField id_orderFl = new JTextField(String.valueOf(id_order));

        jd = new JDialog(new MainPage(), "Ajukan refund", true);
        jd.setLayout(new CardLayout(20,20));
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel parent = new JPanel();
        parent.setLayout(new BorderLayout());
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        
        JLabel resiLabel = new JLabel("Resi: " + resi);
        resiLabel.setBorder(new EmptyBorder(0,0,20,0));
        JLabel tittle = new JLabel("Ajukan Refund Pesananan Anda");
        tittle.setBorder(new EmptyBorder(0,0,20,0));
        tittle.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(resiLabel);
        header.add(tittle);

        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());

        Border topBorder = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.lightGray);
        Border leftPadding = new EmptyBorder(20, 0, 0, 0);
        body.setBorder(BorderFactory.createCompoundBorder(topBorder, leftPadding));
        JLabel uploadFotoLabel = new JLabel("Foto Barang");
        JButton uploadButton = new JButton("Upload and Save Photo");

        photoLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        photoLabel.setPreferredSize(new Dimension(300, 300));
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel keteranganLabel = new JLabel("Alasan refund");
        JTextArea keteranganField = new JTextArea(5, 20); 
        keteranganField.setLineWrap(true);
        keteranganField.setWrapStyleWord(true);

        JScrollPane kscrollPane = new JScrollPane(keteranganField);
        kscrollPane.setPreferredSize(new Dimension(300, 100));
        kscrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(uploadFotoLabel);
        topPanel.add(uploadButton);

        body.add(topPanel, BorderLayout.NORTH); 

        JPanel centerPanel = new JPanel();
        centerPanel.add(photoLabel);
        body.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(keteranganLabel, BorderLayout.NORTH);
        bottomPanel.add(kscrollPane, BorderLayout.CENTER);

        body.add(bottomPanel, BorderLayout.SOUTH);
        
        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBorder(new EmptyBorder(20,0,0,0));
        JButton ajukanBtn = new JButton("Ajukan refund");


        parent.add(header, BorderLayout.NORTH);
        parent.add(body, BorderLayout.CENTER);
        footer.add(ajukanBtn, BorderLayout.EAST);
        parent.add(footer, BorderLayout.SOUTH);

        uploadButton.addActionListener(e -> {
            File pp = uploadAndSavePhoto();
            gambar_refund = null;
            gambar_refund = pp;
        });

        ajukanBtn.addActionListener(e -> {
            if(!"".equals(id_orderFl.getText()) && gambar_refund != null && !"".equals(keteranganField.getText())) {
                insertRefundData(Integer.parseInt(id_orderFl.getText()), gambar_refund, keteranganField.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Mohon lengkapi informasi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        
        jd.add(parent);
        jd.pack();
        jd.setLocationRelativeTo(new MainPage());
        jd.setVisible(true);
    }

    ///pending refund
    public void pendingRefundDialog(int refund_id, String resi) {

        DatabaseConnection dtbs = new DatabaseConnection();
        String query = "select * from refund r \r\n" + //
                        "where id = ?;";

        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, refund_id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                jd = new JDialog(new MainPage(), "Pending refund", true);
                jd.setLayout(new CardLayout(20,20));
                jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                
                JPanel parent = new JPanel();
                parent.setLayout(new BorderLayout());
                
                JPanel header = new JPanel();
                header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
                
                JLabel resiLabel = new JLabel("Resi: " + resi);
                resiLabel.setBorder(new EmptyBorder(0,0,20,0));
                JLabel tittle = new JLabel("Pending Refund Menunggu Respon Admin");
                tittle.setBorder(new EmptyBorder(0,0,20,0));
                tittle.setFont(new Font("Arial", Font.BOLD, 24));
                header.add(resiLabel);
                header.add(tittle);

                JPanel body = new JPanel();
                body.setLayout(new BorderLayout());

                Border topBorder = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.lightGray);
                Border leftPadding = new EmptyBorder(20, 0, 0, 0);
                body.setBorder(BorderFactory.createCompoundBorder(topBorder, leftPadding));
                JLabel uploadFotoLabel = new JLabel("Foto Barang");

                ImageIcon vb = new ImageIcon("assets/refund/" + rs.getString("gambar"));
                photoLabel = new JLabel(vb, SwingConstants.CENTER);
                photoLabel.setPreferredSize(new Dimension(300, 300));
                photoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                Image resizedImage = vb.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(resizedImage));

                JLabel keteranganLabel = new JLabel("Alasan refund");
                JTextArea keteranganField = new JTextArea(5, 20); 
                keteranganField.setLineWrap(true);
                keteranganField.setWrapStyleWord(true);
                keteranganField.setText(rs.getString("keterangan_refund_user"));

                JScrollPane kscrollPane = new JScrollPane(keteranganField);
                kscrollPane.setPreferredSize(new Dimension(300, 100));
                kscrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                topPanel.add(uploadFotoLabel);

                body.add(topPanel, BorderLayout.NORTH); 

                JPanel centerPanel = new JPanel();
                centerPanel.add(photoLabel);
                body.add(centerPanel, BorderLayout.CENTER);

                JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
                bottomPanel.add(keteranganLabel, BorderLayout.NORTH);
                bottomPanel.add(kscrollPane, BorderLayout.CENTER);

                body.add(bottomPanel, BorderLayout.SOUTH);
                
                JPanel footer = new JPanel();
                footer.setLayout(new BorderLayout());
                footer.setBorder(new EmptyBorder(20,0,0,0));
                JButton closeBtn = new JButton("Close");
                closeBtn.addActionListener(e -> jd.dispose());

                parent.add(header, BorderLayout.NORTH);
                parent.add(body, BorderLayout.CENTER);
                footer.add(closeBtn, BorderLayout.EAST);
                parent.add(footer, BorderLayout.SOUTH);

                jd.add(parent);
                jd.pack();
                jd.setLocationRelativeTo(new MainPage());
                jd.setVisible(true);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //refund ditolak
    public void ditolakRefundDialog(int refund_id, String resi) {

        DatabaseConnection dtbs = new DatabaseConnection();
        String query = "select * from refund r \r\n" + //
                        "where id = ?;";

        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, refund_id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                jd = new JDialog(new MainPage(), "Refund ditolak", true);
                jd.setLayout(new CardLayout(20,20));
                jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                
                JPanel parent = new JPanel();
                parent.setLayout(new BorderLayout());
                
                JPanel header = new JPanel();
                header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
                
                JLabel resiLabel = new JLabel("Resi: " + resi);
                resiLabel.setBorder(new EmptyBorder(0,0,20,0));
                JLabel tittle = new JLabel("Refund Ditolak, baca alasan");
                tittle.setBorder(new EmptyBorder(0,0,20,0));
                tittle.setFont(new Font("Arial", Font.BOLD, 24));
                header.add(resiLabel);
                header.add(tittle);

                JPanel body = new JPanel();
                body.setLayout(new BorderLayout());

                Border topBorder = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.lightGray);
                Border leftPadding = new EmptyBorder(20, 0, 0, 0);
                body.setBorder(BorderFactory.createCompoundBorder(topBorder, leftPadding));

                JLabel keteranganLabel = new JLabel("Alasan ditolak");
                JTextArea keteranganField = new JTextArea(5, 20); 
                keteranganField.setLineWrap(true);
                keteranganField.setWrapStyleWord(true);
                keteranganField.setText(rs.getString("alasan_tolak"));
                keteranganField.setEditable(false);

                JScrollPane kscrollPane = new JScrollPane(keteranganField);
                kscrollPane.setPreferredSize(new Dimension(300, 100));
                kscrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
                bottomPanel.add(keteranganLabel, BorderLayout.NORTH);
                bottomPanel.add(kscrollPane, BorderLayout.CENTER);

                body.add(bottomPanel, BorderLayout.SOUTH);
                
                JPanel footer = new JPanel();
                footer.setLayout(new BorderLayout());
                footer.setBorder(new EmptyBorder(20,0,0,0));
                JButton closeBtn = new JButton("Close");
                closeBtn.addActionListener(e -> jd.dispose());

                parent.add(header, BorderLayout.NORTH);
                parent.add(body, BorderLayout.CENTER);
                footer.add(closeBtn, BorderLayout.EAST);
                parent.add(footer, BorderLayout.SOUTH);

                jd.add(parent);
                jd.pack();
                jd.setLocationRelativeTo(new MainPage());
                jd.setVisible(true);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    ///refund diterima
    public void diterimaRefundDialog(int refund_id, String resi) {

        ///helper
        JTextField refundTf = new JTextField(String.valueOf(refund_id));

        jd = new JDialog(new MainPage(), "Ajukan refund", true);
        jd.setLayout(new CardLayout(20,20));
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel parent = new JPanel();
        parent.setLayout(new BorderLayout());
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        
        JLabel resiLabel = new JLabel("Resi: " + resi);
        resiLabel.setBorder(new EmptyBorder(0,0,20,0));
        JLabel tittle = new JLabel("Refund diterima, isi informasi berikut");
        tittle.setBorder(new EmptyBorder(0,0,20,0));
        tittle.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(resiLabel);
        header.add(tittle);

        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());

        Border topBorder = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.lightGray);
        Border leftPadding = new EmptyBorder(20, 0, 0, 0);
        body.setBorder(BorderFactory.createCompoundBorder(topBorder, leftPadding));

        JPanel ki = new JPanel();
        ki.setLayout(new FlowLayout());
        ki.add(new JLabel("Pilih metode pengembalian"));
        body.add(ki, BorderLayout.NORTH);

        JPanel kj = new JPanel();
        kj.setLayout(new BoxLayout(kj, BoxLayout.Y_AXIS)); // BoxLayout for vertical alignment

        // Align components to the left
        kj.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JRadioButton op1 = new JRadioButton("Antar ke toko");
        op1.setAlignmentX(Component.LEFT_ALIGNMENT); // Align radio button to the left

        JPanel kl = new JPanel();
        kl.setLayout(new FlowLayout(FlowLayout.LEFT)); // FlowLayout aligned to the left

        JRadioButton op2 = new JRadioButton("Jemput di alamat");
        JComboBox<String> ii = new JComboBox<>(Session.getInstance().getAlamat());
        kl.add(op2);
        kl.add(ii);

        // Ensure alignment for parent panel components
        op2.setAlignmentX(Component.LEFT_ALIGNMENT);
        ii.setAlignmentX(Component.LEFT_ALIGNMENT);

        ButtonGroup bg = new ButtonGroup(); 
        bg.add(op1);
        bg.add(op2);

        kj.add(op1);
        kj.add(kl);

        // Apply alignment for the panel containing kl
        kl.setAlignmentX(Component.LEFT_ALIGNMENT);
        

        body.add(kj, BorderLayout.CENTER);

        JPanel fi = new JPanel();
        fi.setLayout(new FlowLayout());
        fi.add(new JLabel("Waktu pengembalian"));
        JTextField waktuPengambilan = new JTextField();
        waktuPengambilan.setPreferredSize(new Dimension(200, 50));
        fi.add(waktuPengambilan);
        body.add(fi, BorderLayout.SOUTH);

        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBorder(new EmptyBorder(20,0,0,0));
        JButton konfirmasiBtn = new JButton("Konfirmasi");

        konfirmasiBtn.addActionListener(e -> {
            int pengembalian_method = 0;
            String alamat_pengembalian = null;
            if(op1.isSelected()) {
                pengembalian_method = 1;
            }
            if(op2.isSelected()) {
                pengembalian_method = 2;
                alamat_pengembalian = ii.getSelectedItem().toString();
            }
            updateRefundDiterima(Integer.parseInt(refundTf.getText()), pengembalian_method, waktuPengambilan.getText(), alamat_pengembalian);
        });


        parent.add(header, BorderLayout.NORTH);
        parent.add(body, BorderLayout.CENTER);
        footer.add(konfirmasiBtn, BorderLayout.EAST);
        parent.add(footer, BorderLayout.SOUTH);
        
        jd.add(parent);
        jd.pack();
        jd.setLocationRelativeTo(new MainPage());
        jd.setVisible(true);
    }

    private void updateRefundDiterima(int refund_id, int pengembalian_method, String waktu, String alamat) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query = """
                       update refund \r
                       set pengembalian_method = ?, waktu_pengembalian = ?, alamat_pengembalian = ? where id = ?;""" //
        ;
        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, pengembalian_method);
            pstmt.setString(2, waktu);
            pstmt.setString(3, alamat);
            pstmt.setInt(4, refund_id);

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Berhasil mengkonfirmasi refund", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                jd.dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengkonfirmasi refund: " + e.getLocalizedMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
        riwayatContainer.removeAll();
        getRiwayatItem();
        riwayatContainer.revalidate();
        riwayatContainer.repaint();
    }
    
    ///save foto
    private File uploadAndSavePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg", "gif"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Load and display the image
                BufferedImage image = ImageIO.read(selectedFile);
                Image resizedImage = image.getScaledInstance(photoLabel.getWidth(), photoLabel.getHeight(), Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(resizedImage));
                photoLabel.setText("");

                System.out.println("Upload berhasil");

                return selectedFile;
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }

        return null;
    }

    //insert new refund request
    private void insertRefundData(int id_order, File gambar, String keterangan) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query = """
                       INSERT INTO shopee_pink.refund (order_id,refund_status,gambar,keterangan_refund_user)\r
                       \tVALUES (?,'Pending',?,?);""" //
        ;
        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, id_order);
            pstmt.setString(2, gambar.getName());
            pstmt.setString(3, keterangan);

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0) {
                try {
                    File destinationFile = new File(saveFolder, gambar.getName());
                    Files.copy(gambar.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
                JOptionPane.showMessageDialog(this, "Berhasil mengajukan refund", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                jd.dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengajukan refund: " + e.getLocalizedMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
        riwayatContainer.removeAll();
        getRiwayatItem();
        riwayatContainer.revalidate();
        riwayatContainer.repaint();
    }

    //update status selesai by user
    public void selesaikanPesanan(int order_id) {
        DatabaseConnection dtbs = new DatabaseConnection();
        String query = """
                       update pesanan \r
                       set order_status = "selesai"\r
                       where id_order = ?;""" //
        //
        
        ;
        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, order_id);

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Berhasil mengubah status pesanan", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status pesanan: " + e.getLocalizedMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
        riwayatContainer.removeAll();
        getRiwayatItem();
        riwayatContainer.revalidate();
        riwayatContainer.repaint();
    }

    public static String formatRupiah(int amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("id").setRegion("ID").build());
        return currencyFormat.format(amount);
    }
}
