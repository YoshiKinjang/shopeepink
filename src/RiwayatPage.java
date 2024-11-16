import auth.Session;
import database.DatabaseConnection;
import java.awt.*;
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
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class RiwayatPage extends JPanel {

    MainPage mainPage;
    JPanel parent;
    JLabel tittle;
    JButton backBtn;
    JPanel riwayatContainer;
    JPanel riwayatParentCard;

    RiwayatPage(MainPage mainPage) {
        setLayout(new CardLayout());
        setBorder(new EmptyBorder(50,270,0,270));

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
        String query = "select * from pesanan p where customer_id = ?;";
        // String query2 = "select nama from product p where id = ?;";
        try {
            Connection conn = dtbs.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            // System.out.println(Session.getInstance().getId());
            pstmt.setInt(1, Session.getInstance().getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                int id          = rs.getInt("id_order");
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
            
                riwayatContainer.add(createRiwayatCardItem(id, resi, tanggal, alamat, harga, ekspedisi, status, payment_method, combinedArray));

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

    public JPanel createRiwayatCardItem(int id, String resi, String tanggal, String alamat, int harga, String ekspedisi, String status, String payment_method, int[][] produk) {
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

        if (diffInDays < 7) {
            JButton refundBtn = new JButton("Refund");
            footer.add(refundBtn, BorderLayout.WEST);
        } 

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

    public static String formatRupiah(int amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("id").setRegion("ID").build());
        return currencyFormat.format(amount);
    }
}
