import java.util.HashMap;

public class DataPlaceholder {
    enum Kode {
        LPT,
        ACS,
        HW,
    }

    String kode;
    String nama;
    String deskripsi;
    double harga;
    double diskon;
    int stok;

    DataPlaceholder(String kode, String nama, String deskripsi, double harga, double diskon, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.diskon = diskon;
        this.stok = stok;
    }

    // The HashMap to store data
    static HashMap<String, DataPlaceholder> dataBarang = new HashMap<>();

    // Method to populate data
    public static void initializeData() {
        for (String[] rawData1 : rawData) {
            dataBarang.put(rawData1[0], new DataPlaceholder(rawData1[0], rawData1[1], rawData1[2], 
                Double.parseDouble(rawData1[3]), Double.parseDouble(rawData1[4]), Integer.parseInt(rawData1[5])));
        }
    }

    // Getter method to access the HashMap
    public static HashMap<String, DataPlaceholder> getDataBarang() {
        return dataBarang;
    }

    // The raw data array
    static String[][] rawData = {
        {
            Kode.LPT + "-001", 
            "Macbook Air 13 2020 (256GB) Chip M1 MGN63ID Grey", 
            "Apple M1 (CPU 8-Core, GPU 7-Core) | 8GB | SSD 256GB NVMe | 13\" QHD Retina Display With True Tone 400 Nit KB Backlight | Magic Keyboard w/ Touch ID | Wifi 6 | Two Thunderbolt | ForceTouchTrackpad|MacOS -GaransiIbox", 
            "11499000", 
            "5", 
            "5"
        },
        {
            Kode.LPT + "-002", 
            "Macbook Air 13 2022 (256GB) Chip M2 MLXW3ID Grey|MLY33ID MidNight", 
            "Apple M2 (CPU 8-Cores GPU 8-Cores) | 8GB | SSD 256GB NVMe | 13.6\" QHD Retina Display With True Tone 500 Nit | KB Backlight | Magic Keyboard w/ Touch ID | Wifi 6 | 1080P Facetime HDCamera | Two Thunderbolt | ForceTouchTrackpad|MacOS GaransiIbox", 
            "14499000", 
            "7", 
            "2"
        }
    };

    public static void main(String[] args) {
        // Initialize data
        initializeData();
    }
}
