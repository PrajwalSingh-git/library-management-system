package dbms30;
import javax.swing.*;
import java.awt.*;

public class SystemSettingsGUI extends JFrame {
    private JTextField lendingPeriodField, fineRateField;

    public SystemSettingsGUI() {
        setTitle("System Settings");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(new JLabel("Lending Period (days):"));
        lendingPeriodField = new JTextField();
        panel.add(lendingPeriodField);

        panel.add(new JLabel("Overdue Fine Rate (per day):"));
        fineRateField = new JTextField();
        panel.add(fineRateField);

        JButton saveBtn = new JButton("Save Settings");
        panel.add(saveBtn);

        add(panel);
        setVisible(true);

        // TODO: Save settings to database
    }
}
