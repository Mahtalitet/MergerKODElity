import javax.swing.*;

public class MergerUtilityMainSettingsGUI {
    private JPanel MainPanel;
    private JTabbedPane mSettingsTabbedPane;
    private JButton mOkButton;
    private JButton mSaveButton;
    private JButton mCancelButton;
    private JLabel mEncodingLable;
    private JList list1;
    private JButton addButton;
    private JButton removeButton;
    private JTextField ENGTextField;
    private JRadioButton originalAndTranslatedRadioButton;
    private JRadioButton onlyTranslatedRadioButton;

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public static void showModalForm(JFrame parentFrame, String title) {
        JDialog frame = new JDialog(parentFrame, title, true);
        frame.setContentPane(new MergerUtilityMainSettingsGUI().MainPanel);
//        frame.setDefaultCloseOperation();
        frame.pack();
        frame.setLocationRelativeTo(parentFrame);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
