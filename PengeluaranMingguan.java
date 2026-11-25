import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class PengeluaranMingguan extends JFrame {
    static final String [] HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    int [] pengeluaran = new int [7];
    JTextField [] tfHari = new JTextField [7];

    JTextField tfBatas = new JTextField("500000");
    JTextArea areaHasil = new JTextArea (10, 32);

    public PengeluaranMingguan() {
        super("Pencatat Pengeluaran Mingguan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8,8));


        JPanel form = new JPanel(new GridLayout(8,2,6,6));
        form.setBackground(new Color(200, 180, 194));
        form.setBorder(BorderFactory.createTitledBorder("Pengeluaran per Hari (Rp): "));
        for (int i = 0; i< HARI.length; i++) {
            tfHari[i] = new JTextField("0");
            form.add(new JLabel(HARI[i]));
            form.add(tfHari[i]);
        }
        form.add(new JLabel("Batas Mingguan"));
        form.add(tfBatas);
        add(form, BorderLayout.WEST);

        areaHasil.setEditable(false);
        areaHasil.setFont(new Font(Font.MONOSPACED, Font.BOLD,14));
        areaHasil.setBackground(new Color(228, 238, 237));
        JScrollPane sp = new JScrollPane(areaHasil);
        sp.setBackground(new Color(200, 180, 194));
        sp.setBorder(BorderFactory.createTitledBorder("Hasil"));
        add(sp, BorderLayout.CENTER);
        
        JButton btnHitung = new JButton("Hitung");
        JButton btnReset = new JButton("Reset");
        JPanel bawah = new JPanel();
        bawah.setBackground(new Color(228, 238, 237));
        bawah.add(btnHitung);
        bawah.add(btnReset);
        add(bawah, BorderLayout.SOUTH);
        
        btnHitung.addActionListener(e -> hitung());
        btnReset.addActionListener(e -> reset());
        
        pack();
        setLocationRelativeTo(null);
    }

    static int hitungTotal(int [] data) {
        int total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        return total;
    }

    static int cariTertinggi(int[] data) {
        int idx = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[i] > data[idx]) {
                idx = i;
            }
        }
        return idx;
    }

    static String tentukanStatus(int total, int batas) {
        if (total <= batas * 0.8) {
            return "KAMU HEMATT!! - Pengeluaran masih aman";
        } else if (total <= batas) {
            return "UANG MENIPISSS - Hampir mencapai batas";
        } else {
            return "BOROSSS BANGETT - Melebihi batas mingguan";
        }
    }

    static String rupiah(int angka){
        return String.format(Locale.forLanguageTag("id-ID"), "Rp%,d", angka);
    }

    void hitung() {
        try {
            for (int i = 0; i<HARI.length; i++) {
                String teks = tfHari[i].getText().trim();
                pengeluaran[i] = teks.isEmpty() ? 0 : Integer.parseInt(teks);
                if (pengeluaran [i] < 0) {
                    JOptionPane.showMessageDialog(this, "Pengeluaran hari " + HARI[i] + " tidak boleh negatif.");
                    return;
                }
            }
            int batas = Integer.parseInt(tfBatas.getText().trim());

            int total = hitungTotal(pengeluaran);
            int rataRata = total / HARI.length;
            int idxMax = cariTertinggi(pengeluaran);
            String status = tentukanStatus(total, batas);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < HARI.length; i++) {
                sb.append(String.format("%-7s %12s", HARI[i], rupiah(pengeluaran[i])));
                if (pengeluaran[i] == rataRata) {
                    sb.append(" (di atas rata-rata)");
                }
                sb.append("\n");
            }
            sb.append("--------------------------------\n");
            sb.append("Total        : ").append(rupiah(total)).append("\n");
            sb.append("Rata-rata    : ").append(rupiah(rataRata)).append("\n");
            sb.append("Tertinggi    : ").append(HARI[idxMax]).append(" (").append(rupiah(pengeluaran[idxMax])).append(")\n");
            sb.append("Status       : ").append(status).append("\n");
            areaHasil.setText(sb.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Semua isian harus berupa angka bulat.", "Input salah", JOptionPane.ERROR_MESSAGE);
        }
    }

    void reset() {
        for (int i = 0; i < tfHari.length; i++) {
            tfHari[i].setText("0");
        }
        tfBatas.setText("500000");
        areaHasil.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PengeluaranMingguan().setVisible(true));
    }
}
