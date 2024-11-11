
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainPage extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private HomePage homePage;

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

        add(mainPanel);

        setVisible(true);
    }

    public void switchToLoginPage() {
        cardLayout.show(mainPanel, "LoginPage");
    }

    public void switchToRegisterPage() {
        if(registerPage == null) {
            registerPage = new RegisterPage(this);
        }
        cardLayout.show(mainPanel, "RegisterPage");
    }

    public void switchToHomePage() {
        if(homePage == null) {
            homePage = new HomePage(this);
            mainPanel.add(homePage, "HomePage");
        }
        cardLayout.show(mainPanel, "HomePage");
    }

    public static void main(String[] args) {
        MainPage vv = new MainPage();
        vv.generate();
    }
}
