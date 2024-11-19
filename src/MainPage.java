
import auth.Session;
import java.awt.*;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainPage extends JFrame {

    static Session session = new Session().getInstance();
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private HomePage homePage;
    private CheckoutPage checkoutPage;
    private RiwayatPage riwayatPage;

    public void generate() {
        setTitle("Shopee Pink - Computer Online Marketplace");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPage = new LoginPage(this);
        mainPanel.add(loginPage, "LoginPage");

        // riwayatPage = new RiwayatPage(this);
        // mainPanel.add(riwayatPage, "RiwayatPage");

        // cardLayout = new CardLayout();
        // mainPanel = new JPanel(cardLayout);

        // checkoutPage = new CheckoutPage(this, null);
        // mainPanel.add(checkoutPage, "CheckoutPage");

        add(mainPanel);

        setVisible(true);
    }

    public void switchToLoginPage() {
        if(loginPage == null) {
            loginPage = new LoginPage(this);
            mainPanel.add(loginPage, "LoginPage");
        }
        cardLayout.show(mainPanel, "LoginPage");
    }

    public void switchToRegisterPage() {
        if(registerPage == null) {
            registerPage = new RegisterPage(this);
            mainPanel.add(registerPage, "RegisterPage");
        }
        cardLayout.show(mainPanel, "RegisterPage");
    }

    public void switchToHomePage() {
        System.out.println("dewa");
        if(homePage == null) {
            homePage = new HomePage(this);
            mainPanel.add(homePage, "HomePage");
        }
        cardLayout.show(mainPanel, "HomePage");
    }

    public void resetHomePage() {
        mainPanel.remove(homePage);
        mainPanel.revalidate();  
        mainPanel.repaint();

        JPanel homePage = new HomePage(this);
        mainPanel.add(homePage, "HomePage");
        cardLayout.show(mainPanel, "HomePage");
    }

    public void switchToCheckoutPage(Map<Integer, CheckoutInfo> dataCheckout) {
        checkoutPage = new CheckoutPage(this, dataCheckout);
        mainPanel.add(checkoutPage, "CheckoutPage");
    
        cardLayout.show(mainPanel, "CheckoutPage");
    }

    public void switchToRiwayatPage() {
        riwayatPage = new RiwayatPage(this);
        mainPanel.add(riwayatPage, "RiwayatPage");
        cardLayout.show(mainPanel, "RiwayatPage");
    }

    public void logout() {
        session.clearData();

        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        
        loginPage = null;
        homePage = null;
        registerPage = null;
        checkoutPage = null;
        riwayatPage = null;

        loginPage = new LoginPage(this);
        mainPanel.add(loginPage, "LoginPage");

        switchToLoginPage();
    }

    public static void main(String[] args) {
        MainPage vv = new MainPage();
        vv.generate();
    }
}
