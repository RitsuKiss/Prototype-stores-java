import java.io.*;
import java.util.*;

public class PanjiStore {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, String> userCredentials = new HashMap<>();
    private static Map<String, Long> userSaldo = new HashMap<>();
    private static List<String> struk = new ArrayList<>();
    private static Map<String, Item> items = new HashMap<>();

    public static void main(String[] args) {
        loadAkun();
        loadBarang();

        while (true) {
            displayMainMenu();
        }
    }

    private static void displayMainMenu() {
        System.out.println("Selamat datang di Panji store silahkan login terlebih dahulu");
        System.out.println("==============");
        System.out.println("1. login");
        System.out.println("2. register");
        System.out.println("Silahkan pilih menu");
        int menu = scanner.nextInt();

        switch (menu) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 223:
                adminakun();
                break;
            default:
                System.out.println("Pilihan tidak valid");
        }
    }

    private static void adminakun() {
        System.out.println("==============");
        System.out.println("Verivikasi 2 langkah");
        System.out.println("First Code");
        String username = scanner.next();
        System.out.println("Second Code");
        String password = scanner.next();

        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            System.out.println("==============");
            System.out.println("Login berhasil");
            adminMenu(username);
        } else {
            System.out.println("==============");
            System.out.println("Code salah");
        }
    }

    private static void adminMenu(String username) {
        while (true) {
            System.out.println("==============");
            System.out.println("1. Tambah Stok Barang");
            System.out.println("2. Edit Barang");
            System.out.println("3. Hapus Barang");
            System.out.println("4. Logout");
            System.out.println("Silahkan pilih menu");
            int menu = scanner.nextInt();

            switch (menu) {
                case 1:
                    manageStock();
                    break;
                case 2:
                    editItem();
                    break;
                case 3:
                    deleteItem();
                    break;
                case 4:
                    System.out.println("Logout berhasil");
                    return;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        }
    }

    private static void manageStock() {
        while (true) {
            System.out.println("==============");
            List<String> itemNames = new ArrayList<>(items.keySet());
            for (int i = 0; i < itemNames.size(); i++) {
                Item item = items.get(itemNames.get(i));
                System.out.println((i + 1) + ". " + item.name + " stok: " + item.stock);
            }
            System.out.println((itemNames.size() + 1) + ". Tambah Barang Baru");
            System.out.println("Silahkan pilih barang yang akan ditambahkan stoknya");

            int choice = scanner.nextInt();
            if (choice == itemNames.size() + 1) {
                addNewItem();
                continue;
            } else if (choice < 1 || choice > itemNames.size()) {
                System.out.println("Pilihan tidak valid");
                continue;
            }

            Item selectedItem = items.get(itemNames.get(choice - 1));
            System.out.println("Masukkan jumlah stok yang akan ditambahkan");
            int addedStock = scanner.nextInt();
            selectedItem.stock += addedStock;
            saveBarang();
            System.out.println(addedStock + " unit ditambahkan ke stok " + selectedItem.name + ", stok sekarang: " + selectedItem.stock);

            System.out.println("Apakah ada stok lagi yang akan ditambahkan? (y/g)");
            char moreStock = scanner.next().charAt(0);
            if (moreStock == 'g') {
                break;
            }
        }
    }

    private static void addNewItem() {
        System.out.println("Masukkan nama barang");
        String name = scanner.next();
        System.out.println("Masukkan harga barang");
        int price = scanner.nextInt();
        System.out.println("Masukkan stok awal barang");
        int stock = scanner.nextInt();
        items.put(name, new Item(name, price, stock));
        saveBarang();
        System.out.println("Barang baru berhasil ditambahkan");
    }

    private static void editItem() {
        while (true) {
            System.out.println("==============");
            List<String> itemNames = new ArrayList<>(items.keySet());
            for (int i = 0; i < itemNames.size(); i++) {
                Item item = items.get(itemNames.get(i));
                System.out.println((i + 1) + ". " + item.name + " harga: " + item.price + " stok: " + item.stock);
            }
            System.out.println("Silahkan pilih barang yang akan di-edit");

            int choice = scanner.nextInt();
            if (choice < 1 || choice > itemNames.size()) {
                System.out.println("Pilihan tidak valid");
                continue;
            }

            Item selectedItem = items.get(itemNames.get(choice - 1));
            System.out.println("Masukkan nama baru (atau tekan Enter untuk mempertahankan nama lama)");
            scanner.nextLine(); 
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                items.remove(selectedItem.name);
                selectedItem.name = newName;
                items.put(newName, selectedItem);
            }

            System.out.println("Masukkan harga baru (atau 0 untuk mempertahankan harga lama)");
            int newPrice = scanner.nextInt();
            if (newPrice != 0) {
                selectedItem.price = newPrice;
            }

            System.out.println("Masukkan stok baru (atau -1 untuk mempertahankan stok lama)");
            int newStock = scanner.nextInt();
            if (newStock != -1) {
                selectedItem.stock = newStock;
            }

            saveBarang();
            System.out.println("Barang berhasil di-edit");
            System.out.println("Apakah ada barang lagi yang akan di-edit? (y/g)");
            char moreEdit = scanner.next().charAt(0);
            if (moreEdit == 'g') {
                break;
            }
        }
    }

    private static void deleteItem() {
        while (true) {
            System.out.println("==============");
            List<String> itemNames = new ArrayList<>(items.keySet());
            for (int i = 0; i < itemNames.size(); i++) {
                Item item = items.get(itemNames.get(i));
                System.out.println((i + 1) + ". " + item.name + " harga: " + item.price + " stok: " + item.stock);
            }
            System.out.println("Silahkan pilih barang yang akan dihapus");

            int choice = scanner.nextInt();
            if (choice < 1 || choice > itemNames.size()) {
                System.out.println("Pilihan tidak valid");
                continue;
            }

            String itemName = itemNames.get(choice - 1);
            items.remove(itemName);
            saveBarang();
            System.out.println("Barang berhasil dihapus");
            System.out.println("Apakah ada barang lagi yang akan dihapus? (y/g)");
            char moreDelete = scanner.next().charAt(0);
            if (moreDelete == 'g') {
                break;
            }
        }
    }

    private static void loadAkun() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/akun.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        String username = parts[0];
                        String password = parts[1];
                        long saldo = Long.parseLong(parts[2]);
                        userCredentials.put(username, password);
                        userSaldo.put(username, saldo);
                    } catch (NumberFormatException e) {
                    }
                } else {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/admin.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];
                    userCredentials.put(username, password);
                } else {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadBarang() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/barang.txt"))) {
            String line;
                        while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    int stock = Integer.parseInt(parts[2]);
                    items.put(name, new Item(name, price, stock));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveAkun() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/akun.txt"))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                String username = entry.getKey();
                String password = entry.getValue();
                Long saldo = userSaldo.get(username);
                bw.write(username + "," + password + "," + saldo);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveBarang() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/barang.txt"))) {
            for (Item item : items.values()) {
                bw.write(item.name + "," + item.price + "," + item.stock);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveStruk() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/struk.txt"))) {
            for (String entry : struk) {
                bw.write(entry);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void login() {
        System.out.println("==============");
        System.out.println("Masukan username");
        String username = scanner.next();
        System.out.println("Masukan password");
        String password = scanner.next();

        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            System.out.println("==============");
            System.out.println("Login berhasil");
            memberMenu(username);
        } else {
            System.out.println("==============");
            System.out.println("Username atau password salah");
        }
    }

    private static void memberMenu(String username) {
        while (true) {
            shopping(username);
            System.out.println("Apakah kamu ingin melakukan pembelian lagi? (Y/G) atau Logout (L)");
            char continueShopping = scanner.next().charAt(0);
            if (continueShopping == 'L' || continueShopping == 'l') {
                System.out.println("Logout berhasil");
                return;
            } else if (continueShopping != 'Y' && continueShopping != 'y') {
                break;
            }
        }
        System.out.println("Terimakasih telah mengunjungi Panji store sampai jumpa di lain waktu");
    }

    private static void register() {
        System.out.println("==============");
        System.out.println("Masukan username");
        String username = scanner.next();
        if (userCredentials.containsKey(username)) {
            System.out.println("Username sudah digunakan");
            return;
        }

        String password;
        while (true) {
            System.out.println("Masukan password");
            password = scanner.next();
            if (password.length() >= 8) {
                break;
            } else {
                System.out.println("mohon masukan password minimal 8 digit");
            }
        }

        System.out.println("Masukan saldo awal");
        long saldo = scanner.nextLong();

        userCredentials.put(username, password);
        userSaldo.put(username, saldo);
        saveAkun();
        System.out.println("Registrasi berhasil");
        memberMenu(username);
    }

    private static void shopping(String username) {
    long saldo = userSaldo.get(username);
    long total = 0;
    StringBuilder purchasedItems = new StringBuilder();

    while (true) {
        System.out.println("==============");
        System.out.println("Saldo kamu " + saldo);
        System.out.println("+--+---------------+----------------+----------");
        System.out.println("|No|  Nama Barang  |     Harga      |   stok   ");
        System.out.println("+--+---------------+----------------+----------");
        int index = 1;
        Map<Integer, String> itemChoices = new HashMap<>();
        for (Item item : items.values()) {
            System.out.println("|" + index + ".|__" + item.name + "__|  Rp." + item.price + " | " + item.stock);
            itemChoices.put(index, item.name); 
            index++;
        }
        System.out.println("Silahkan pilih barang yang akan kamu beli (ketik Nomor)");

        int choice = scanner.nextInt();
        if (!itemChoices.containsKey(choice)) {
            System.out.println("Pilihan tidak valid");
            continue;
        }

        String item = itemChoices.get(choice);
        int price = items.get(item).price;

        System.out.println("==============");
        System.out.println("Silahkan masukkan jumlah yang kamu inginkan");
        int quantity = scanner.nextInt();

        if (items.get(item).stock < quantity) {
            System.out.println("Stok barang tidak mencukupi");
            continue;
        }

        long itemTotal = price * quantity;
        total += itemTotal;
        purchasedItems.append(item).append(" ").append(quantity).append("x").append(" Rp.").append(itemTotal).append("\n");
        items.get(item).stock -= quantity;

        System.out.println("==============");
        System.out.println("Apakah ada lagi barang yang kamu inginkan? (y/g)");
        char answer = scanner.next().charAt(0);

        if (answer == 'g') {
            break;
        }
    }

    System.out.println("==============");
    System.out.println("Total harga semua barang yang dibeli: Rp." + total);
    System.out.println("Ok mari kita lanjutkan pembayaran (y/g)");
    char payment = scanner.next().charAt(0);

    if (payment == 'y') {
        if (saldo >= total) {
            saldo -= total;
            userSaldo.put(username, saldo);
            purchasedItems.append("Total: Rp.").append(total).append("\n");
            struk.add(purchasedItems.toString());
            saveAkun();
            saveBarang();
            saveStruk();
            System.out.println("==============");
            System.out.println("Barang berhasil dibeli");
            System.out.println(purchasedItems.toString());
            System.out.println("Saldo kamu sisa " + saldo);
        } else {
            System.out.println("==============");
            System.out.println("Saldo tidak mencukupi, saldo kamu hanya " + saldo );
        }
    } else {
        System.out.println("==============");
        System.out.println("Pembayaran dibatalkan");
    }
}


    static class Item {
        String name;
        int price;
        int stock;

        Item(String name, int price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }
}