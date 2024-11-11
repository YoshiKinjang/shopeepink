import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class CheckoutPage extends JPanel {
    CheckoutPage() {
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
        tbp_gbc.fill = GridBagConstraints.BOTH;  
        tbp_gbc.weightx = 0.5;  
        tbp_gbc.gridx = 0;  
        tbp_gbc.gridy = 0; 
        tbp_gbc.anchor = GridBagConstraints.LINE_START;
        topBarParent.add(logoLabel, tbp_gbc);

        tbp_gbc.fill = GridBagConstraints.BOTH;  
        tbp_gbc.weightx = 0.5;  
        tbp_gbc.gridx = 1;  
        tbp_gbc.gridy = 0; 
        tbp_gbc.anchor = GridBagConstraints.LINE_END;
        topBarParent.add(checkoutTitle, tbp_gbc);
      
        ///user icon
        tbp_gbc.fill = GridBagConstraints.BOTH;  
        tbp_gbc.weightx = 0.5; 
        tbp_gbc.gridx = 2;  
        tbp_gbc.gridy = 0; 
        userLbl.setHorizontalAlignment(JTextField.CENTER);
        topBarParent.add(userLbl, tbp_gbc);

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

        JPanel infoItemParent = new JPanel();
        infoItemParent.setBackground(Color.white);
        infoItemParent.setLayout(new BoxLayout(infoItemParent, BoxLayout.Y_AXIS));

        for (int i = 0; i < 10; i++) {
            
            infoItemParent.add(checkoutItem("Apple macbook air", "15.000", "1"));
        }
        
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

        return mainPanel; 
    }

    public static JPanel checkoutItem(String namaBarang, String hargaBarang, String kuantitasBarang) {
        JPanel keranjangItem = new JPanel();
        keranjangItem.setBackground(Color.white);

        keranjangItem.setLayout(new GridBagLayout());
        keranjangItem.setBorder(new EmptyBorder(15,15,15,15));
        GridBagConstraints krGbc = new GridBagConstraints();

        JPanel imageCon = new JPanel();
        ImageIcon gambarBarang = new ImageIcon("img/tes.png");
        JLabel gambarBarangLbl = new JLabel(gambarBarang);
        imageCon.setPreferredSize(new Dimension(80,80));
        imageCon.add(gambarBarangLbl);
        
        
        JPanel infoItemParent = new JPanel();
        infoItemParent.setBackground(Color.white);
        infoItemParent.setLayout(new GridLayout(3,1,10,0));

        JLabel namaBarangLbl = new JLabel(namaBarang);
        JLabel kuantitasBarangLbl = new JLabel("Kuantitas: " + kuantitasBarang);
        namaBarangLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel hargaLbl = new JLabel("Rp" + hargaBarang);
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
        JLabel conProdLbl = new JLabel("Rp16.000");
        conProdLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel conKirimLbl = new JLabel("Rp16.000");
        conKirimLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel conKuponLbl = new JLabel("Rp16.000");
        conKuponLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel conTotalLbl = new JLabel("Rp16.000");
        conTotalLbl.setFont(new Font("Arial", Font.BOLD, 16));

        JComboBox alamatCmBox = new JComboBox<>();
        JComboBox jasaCmBox = new JComboBox<>();
        JComboBox kuponCmBox = new JComboBox<>();

        JPanel rbCon = new JPanel();
        rbCon.setLayout(new FlowLayout(FlowLayout.CENTER));
        rbCon.setBackground(Color.white);
        rbCon.setBorder(new EmptyBorder(10,10,10,10));
        JRadioButton codRad = new JRadioButton("COD");
        codRad.setFont(new Font("Arial", Font.BOLD, 16));
        JRadioButton virRad = new JRadioButton("Virtual account");
        virRad.setFont(new Font("Arial", Font.BOLD, 16));
        ButtonGroup mtdGroup = new ButtonGroup();
        mtdGroup.add(codRad);
        mtdGroup.add(virRad);
        rbCon.add(codRad);
        rbCon.add(virRad);

        JPanel tt = new JPanel();
        tt.setLayout(new BorderLayout());
        JButton kembaliBtn = new JButton("Kembali");
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

        return ii;
    }
}
