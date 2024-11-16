public class CheckoutInfo {
    private int id_barang;
    private int harga_satuan;
    private int total_barang;
    private int total_harga;
    private int total_berat;

    public CheckoutInfo(int id_barang, int total_barang, int total_harga, int harga_satuan, int total_berat) {
        this.id_barang = id_barang;
        this.total_barang = total_barang;
        this.total_harga = total_harga;
        this.harga_satuan = harga_satuan;
        this.total_berat = total_berat;
    }

    public void setIdBarang(int id_barang) {
        this.id_barang = id_barang;
    }

    public void setHargaSatuan(int harga_satuan) {
        this.harga_satuan = harga_satuan;
    }

    public void setTotalBarang(int total_barang) {
        this.total_barang = total_barang;
    }

    public void setTotalHarga(int total_harga) {
        this.total_harga = total_harga;
    }

    public void setTotalBerat(int total_berat) {
        this.total_berat = total_berat;
    }

    public int getIdBarang() {
        return id_barang;
    }

    public int getHargaSatuna() {
        return harga_satuan;
    }

    public int getTotalBarang() {
        return total_barang;
    }

    public int getTotalHarga() {
        return total_harga;
    }

    public int getTotalBerat() {
        return total_berat;
    }

    public void displayData() {
        System.out.println("Id: " + id_barang);
        System.out.println("Total barang: " + total_barang);
        System.out.println("Total harga: " + total_harga);
        System.out.println("Total berat: " + total_berat);
    }
}
