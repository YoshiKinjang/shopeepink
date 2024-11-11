package auth;

import java.util.Arrays;

public class Session {
    private static Session instance;
    private int id;
    private String username;
    private String email;
    private String password;
    private String[] alamat = new String[3];

    public Session(){}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    public void setSession(int id, String username, String email, String password, String[] alamat) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.alamat = alamat;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String[] getAlamat() {
        return alamat;
    }

    public void dislayData() {
        System.out.println("id\t" + id);
        System.out.println("username\t" + username);
        System.out.println("email\t" + email);
        System.out.println("password\t" + password);
        System.out.println("alamat\t" + Arrays.toString(alamat));
    }

    public void clearData() {
        this.id = 0;
        this.username = null;
        this.email = null;
        this.password = null;
        this.alamat = new String[3];

    }
}
