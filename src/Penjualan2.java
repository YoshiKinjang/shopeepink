import java.util.HashMap;
import java.util.Scanner;

public class Penjualan2 {

    
    public static void main(String[] args) {

        System.out.println("kevkev");
        ///main data
        DataPlaceholder.initializeData();

        HashMap<String, DataPlaceholder> data = DataPlaceholder.getDataBarang();
        
        String format = "| %-10s | %-50s | %-10s | %-11s | %-10s |%n";
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf(format, "Kode", "Nama", "Harga ", "Diskon", "Stok");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        // Print the data rows
        for (String kode : data.keySet()) {
            DataPlaceholder item = data.get(kode);
            System.out.printf(format, 
                item.kode, 
                item.nama.length() > 50 ? item.nama.substring(0, 47) + "..." : item.nama, 
                String.format("%.0f", item.harga), 
                String.format("%.2f", item.diskon) + "%", 
                item.stok, 
                item.deskripsi.length() > 5 ? item.deskripsi.substring(0, 5) + "..." : item.deskripsi
            );
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.println();

        ///string keranjang
        HashMap<String, Integer> keranjang = new HashMap<>();

        System.out.println("Masukkan kode barang untuk memesan : ");
        try {
            String kodeBarangPesan = scanner.nextLine();
            // if()
            // data.get(kodeBarangPesan)
            
        } catch (Exception e) {

        }

        System.out.println();
    }
    

    static Scanner scanner  = new Scanner(System.in);
}
