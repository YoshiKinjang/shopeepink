import java.util.*;

class Barang {
    String kode;
    String nama;
    String deskripsi;
    double harga;
    double diskon;
    int stok;

    Barang(String kode, String nama, String deskripsi, double harga, double diskon, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.diskon = diskon;
        this.stok = stok;
    }

    double hargaAkhir() {
        return harga - (harga * diskon / 100);
    }
}

public class Penjualan {
    private static Map<String, Barang> katalog = new HashMap<>();
    private static Map<String, Integer> keranjang = new HashMap<>();
    
    public static void main(String[] args) {
        // Inisialisasi data barang
        initBarang();

        Scanner scanner = new Scanner(System.in);
        boolean lanjut = true;

        while (lanjut) {
            System.out.println("Daftar Barang:");
            tampilkanBarang();
            
            System.out.print("Masukkan kode barang yang ingin dipesan: ");
            String kode = scanner.nextLine();
            
            if (!katalog.containsKey(kode)) {
                System.out.println("Kode barang tidak valid. Silakan coba lagi.");
                continue;
            }
            
            System.out.print("Masukkan jumlah barang yang ingin dipesan: ");
            int jumlah = scanner.nextInt();
            scanner.nextLine(); 
            
            Barang barang = katalog.get(kode);
            if (jumlah > barang.stok) {
                System.out.println("Jumlah melebihi stok. Silakan coba lagi.");
                continue;
            }
            
            keranjang.put(kode, keranjang.getOrDefault(kode, 0) + jumlah);
            barang.stok -= jumlah;

            // Tampilkan keranjang
            System.out.println("\nBarang yang dipesan:");
            tampilkanKeranjang();
            
            // Update jumlah barang
            System.out.print("Ingin update jumlah barang? (ya/tidak): ");
            String update = scanner.nextLine();
            
            if (update.equalsIgnoreCase("ya")) {
                System.out.print("Masukkan kode barang untuk update: ");
                String kodeUpdate = scanner.nextLine();
                
                if (!keranjang.containsKey(kodeUpdate)) {
                    System.out.println("Kode barang tidak ada dalam keranjang.");
                    continue;
                }
                
                System.out.print("Masukkan jumlah barang terbaru: ");
                int jumlahUpdate = scanner.nextInt();
                scanner.nextLine();  // Consume newline
                
                Barang barangUpdate = katalog.get(kodeUpdate);
                if (jumlahUpdate > barangUpdate.stok + keranjang.get(kodeUpdate)) {
                    System.out.println("Jumlah melebihi stok. Silakan coba lagi.");
                    continue;
                }
                
                barangUpdate.stok += keranjang.get(kodeUpdate) - jumlahUpdate;
                keranjang.put(kodeUpdate, jumlahUpdate);
            }
            
            // Hitung total harga
            double totalHarga = hitungTotalHarga();
            System.out.printf("\nTotal harga: %.2f\n", totalHarga);
            
            // Konfirmasi pembelian
            System.out.print("Konfirmasi pembelian? (ya/tidak): ");
            String konfirmasi = scanner.nextLine();
            
            if (konfirmasi.equalsIgnoreCase("ya")) {
                System.out.println("\nPembelian berhasil.");
                break;
            }
        }
        
        scanner.close();
    }

    private static void initBarang() {
        // Contoh data barang
        katalog.put("LPT-001", new Barang("LPT-001", "Macbook Air 13 2020 (256GB) Chip M1 MGN63ID Grey", "Apple M1 (CPU 8-Core, GPU 7-Core) | 8GB | SSD 256GB NVMe | 13 QHD Retina Display With True Tone 400 Nit KB Backlight | Magic Keyboard w/ Touch ID | Wifi 6 | Two Thunderbolt | ForceTouchTrackpad|MacOS -GaransiIbox", 11499000, 10, 20));
        katalog.put("LPT-002", new Barang("LPT-002", "LEGION 7 16IRX9H-1CID Glacier White C OHS (RTX 4090)", "core i9-14900HX Up To 5,8Ghz 24 Core 32 Threads | 32GB DDR5 5600Mhz (2x16GB) | SSD 2TB NVMe Gen 4 | 16 WQXGA IPS 500nits , 100% DCI-P3, 240Hz, Display HDRTM 400, Dolby VisionTM, G-SYNC, Low Blue Light| RTX 4090 16GB |KB Backlight RGB (with Lighting Lens) | Numeric Pad | Wifi 6E", 65999000, 5, 50));
        katalog.put("ACS-001", new Barang("ACS-001", "Mouse Gaming", "Mouse Wireless & Bluetooth Logitech MX Master 3S Graphite / Pale Gray (1 th) 8000 DPI, MagSpeed SmartShift, Thumbwheel, 7 Buttons, Silent Clicks", 1530000, 8, 30));
        katalog.put("ACS-002", new Barang("ACS-001", "Mouse Pad HyperX Pulsefire Mat", "(Size L - 450x400x3mm) Cloth Materials Thight, For Precision, Smooth Stitches, can be rolled and easy to carry, Anti Slip Rubber Base", 248000, 8, 30));
        katalog.put("HW-001", new Barang("HW-001", "Core i7-14700F", "5,4Ghz/20 Cores 28 Threads (3th) Non GPU", 6370000, 8, 30));
        // Tambahkan lebih banyak barang sesuai kebutuhan
    }

    private static void tampilkanBarang() {
        for (Barang barang : katalog.values()) {
            System.out.printf("Kode: %s, Nama: %s, Deskripsi: %s, Harga: %.2f, Diskon: %.2f%%, Stok: %d\n",
                              barang.kode, barang.nama, barang.deskripsi, barang.harga, barang.diskon, barang.stok);
        }
    }

    private static void tampilkanKeranjang() {
        for (String kode : keranjang.keySet()) {
            Barang barang = katalog.get(kode);
            int jumlah = keranjang.get(kode);
            System.out.printf("Kode: %s, Nama: %s, Jumlah: %d, Harga Satuan: %.2f, Harga Akhir: %.2f\n",
                              barang.kode, barang.nama, jumlah, barang.harga, barang.hargaAkhir());
        }
    }

    private static double hitungTotalHarga() {
        double total = 0;
        for (String kode : keranjang.keySet()) {
            Barang barang = katalog.get(kode);
            int jumlah = keranjang.get(kode);
            total += barang.hargaAkhir() * jumlah;
        }
        return total;
    }
}
