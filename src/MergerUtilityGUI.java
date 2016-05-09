import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MergerUtilityGUI implements ParsingErrorHandler {

//    private static MergerUtilityGUI mergerUtilityGUI;

    private JPanel MainPanel;
    private JRadioButton mResToCsvRB;
    private JRadioButton mCsvToResRB;
    private ButtonGroup mRadioButtonGroup;
    private JButton mGenerateButton;
    private JButton mDeleteResourceButton;
    private JButton mAddResourcesButton;
    private JList mResourcesList;
    private DefaultListModel<File> mListModel;
    private JScrollPane mResourcesListScrollPane;
    private JFileChooser mFileChooser;
    private JFileChooser mDirectoryChooser;

    public MergerUtilityGUI() {
        mRadioButtonGroup = new ButtonGroup();
        mRadioButtonGroup.add(mResToCsvRB);
        mRadioButtonGroup.add(mCsvToResRB);

        mFileChooser = returnOpenFileChooser(MergerUtilityStaticVariables.Gui.FILE_CHOOSER_TITLE_FOR_RESOURCES,
                MergerUtilityStaticVariables.Gui.FILE_CHOOSER_EXTENSIONS_TITLE_RESOURCES,
                MergerUtilityStaticVariables.File.FILE_EXTENSION_STRING,
                MergerUtilityStaticVariables.File.FILE_EXTENSION_XML);
        mDirectoryChooser = returnOpenDirectoryChooser(MergerUtilityStaticVariables.Gui.FILE_CHOOSER_TITLE_CSV_DIRECTORY);

        mListModel = new DefaultListModel();
        mResourcesList.setModel(mListModel);

        mResToCsvRB.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mGenerateButton.setText(MergerUtilityStaticVariables.Gui.TEXT_GENERATE_CSV);
                mDeleteResourceButton.setText(MergerUtilityStaticVariables.Gui.TEXT_DELETE_RESOURCES);
                mAddResourcesButton.setText(MergerUtilityStaticVariables.Gui.TEXT_ADD_RESOURCES);
                clearSelectedFilesInFileChooser(mFileChooser);
                clearSelectedFilesInFileChooser(mDirectoryChooser);
                setFileChooserTitleAndFilter(MergerUtilityStaticVariables.Gui.FILE_CHOOSER_TITLE_FOR_RESOURCES,
                        MergerUtilityStaticVariables.Gui.FILE_CHOOSER_EXTENSIONS_TITLE_RESOURCES,
                        MergerUtilityStaticVariables.File.FILE_EXTENSION_STRING,
                        MergerUtilityStaticVariables.File.FILE_EXTENSION_XML);
                mListModel.removeAllElements();
                disableDeleteButton();
                disableGenerateButton();
            }
        });

        mCsvToResRB.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mGenerateButton.setText(MergerUtilityStaticVariables.Gui.TEXT_GENERATE_RESOURCES);
                mDeleteResourceButton.setText(MergerUtilityStaticVariables.Gui.TEXT_DELETE_CSV);
                mAddResourcesButton.setText(MergerUtilityStaticVariables.Gui.TEXT_ADD_CSV);
                clearSelectedFilesInFileChooser(mFileChooser);
                clearSelectedFilesInFileChooser(mDirectoryChooser);
                setFileChooserTitleAndFilter(MergerUtilityStaticVariables.Gui.FILE_CHOOSER_TITLE_FOR_CSV,
                        MergerUtilityStaticVariables.Gui.FILE_CHOOSER_EXTENSIONS_TITLE_CSV,
                        MergerUtilityStaticVariables.File.FILE_EXTENSION_CSV);
                mListModel.removeAllElements();
                disableDeleteButton();
                disableGenerateButton();
            }
        });

        mAddResourcesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int returnVal = mFileChooser.showOpenDialog(MainPanel);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    addFileElementsToList(mFileChooser.getSelectedFiles());
                    disableDeleteButton();
                    if (mListModel.isEmpty()) {
                        disableGenerateButton();
                    } else {
                        enableGenerateButton();
                        copyFileDirectoryFromTo(mFileChooser, mDirectoryChooser);
                    }
                }
            }
        });

        mResourcesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if(e.getValueIsAdjusting() == false) {
                    enableDeleteButton();
                }
            }
        });
        mDeleteResourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFileElementsFromList(mResourcesList.getSelectedValuesList());
                disableDeleteButton();
                if (mListModel.isEmpty()) {
                    disableGenerateButton();
                }
            }
        });
        mGenerateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = mDirectoryChooser.showOpenDialog(MainPanel);

                if(returnVal == JFileChooser.APPROVE_OPTION) {

                    mDirectoryChooser.setCurrentDirectory(mDirectoryChooser.getSelectedFile());
                    File directoryToSave = mDirectoryChooser.getCurrentDirectory();
                    Object[] filesToParese = reternCurrentFileElementsFromList();
                    File[] files = Arrays.copyOf(filesToParese, filesToParese.length, File[].class);

                    boolean parsingResult = false;
                    if (mResToCsvRB.isSelected()) {
//                        disableGuiWhileSaveToCsv();
                        System.out.println("We will generate CSV...");
                        parsingResult = new ParserManager(MergerUtilityGUI.this).parseBanchOfFilesToCSV(files, directoryToSave);
                        resetGuiToNormalStateAfterSaveToCsv();
                    }

                    if (mCsvToResRB.isSelected()) {
//                        disableGuiWhileSaveToRes();
                        System.out.println("We will generate RES...");
                        parsingResult = new ParserManager(MergerUtilityGUI.this).parseBanchOfFilesFromCSV(files, directoryToSave);
//                        resetGuiToNormalStateAfterSaveToRes();
                    }
                }
            }
        });
    }

    private void disableGuiWhileSaveToCsv() {
        mCsvToResRB.setEnabled(false);
        mAddResourcesButton.setEnabled(false);
        mGenerateButton.setEnabled(false);
        mGenerateButton.setText("Parsing Resources...");
    }

    private void disableGuiWhileSaveToRes() {
        mResToCsvRB.setEnabled(false);
        mAddResourcesButton.setEnabled(false);
        mGenerateButton.setEnabled(false);
        mGenerateButton.setText("Parsing CSV...");
    }

    private void resetGuiToNormalStateAfterSaveToCsv() {
        mCsvToResRB.setEnabled(true);
        mAddResourcesButton.setEnabled(true);
        mGenerateButton.setEnabled(true);
        mGenerateButton.setText(MergerUtilityStaticVariables.Gui.TEXT_GENERATE_CSV);
    }

    private void resetGuiToNormalStateAfterSaveToRes() {
        mResToCsvRB.setEnabled(true);
        mAddResourcesButton.setEnabled(true);
        mGenerateButton.setEnabled(true);
        mGenerateButton.setText(MergerUtilityStaticVariables.Gui.TEXT_GENERATE_RESOURCES);
    }

    private void addFileElementsToList(File[] selectedFiles) {
        for (File directory: selectedFiles) {
            if (mListModel.contains(directory) == false) {
                mListModel.addElement(directory);
            }
        }
    }

    private void deleteFileElementsFromList(List<File> valuesList) {
        for (File list : valuesList) {
            mListModel.removeElement(list);
        }
    }

    private Object[] reternCurrentFileElementsFromList() {
        return mListModel.toArray();
    }

    private void disableDeleteButton() {
        mDeleteResourceButton.setEnabled(false);
    }

    private void enableDeleteButton() {
        mDeleteResourceButton.setEnabled(true);
    }

    private void disableGenerateButton() {
        mGenerateButton.setEnabled(false);
    }

    private void enableGenerateButton() {
        mGenerateButton.setEnabled(true);
    }

    private JFileChooser returnOpenFileChooser(String dialogTitle, String extensionsTitle, String... extensions) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = returnFileChooserFilter(extensionsTitle, extensions);
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(dialogTitle);
        return chooser;
    }

    private JFileChooser returnOpenDirectoryChooser(String dialogTitle) {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(dialogTitle);
        return chooser;
    }

    private FileNameExtensionFilter returnFileChooserFilter(String extensionsTitle, String... extensions) {
        return new FileNameExtensionFilter(
                extensionsTitle, extensions);
    }

    private void setFileChooserTitleAndFilter(String title, String extensionsTitle, String... extensions ) {
        mFileChooser.setDialogTitle(title);
        mFileChooser.removeChoosableFileFilter(mFileChooser.getFileFilter());
        mFileChooser.setFileFilter(returnFileChooserFilter(extensionsTitle, extensions));
    }

    private void clearSelectedFilesInFileChooser(JFileChooser fileChooser) {
        File currentDirectory = fileChooser.getCurrentDirectory();
        fileChooser.setSelectedFile(new File(""));
        fileChooser.setCurrentDirectory(currentDirectory);
    }

    private void copyFileDirectoryFromTo(JFileChooser oneFileChooser, JFileChooser anotherFileChooser) {
        File currentDirectory = oneFileChooser.getCurrentDirectory();
        anotherFileChooser.setSelectedFile(new File(""));
        anotherFileChooser.setCurrentDirectory(currentDirectory);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame(MergerUtilityStaticVariables.Gui.APP_NAME+" "+MergerUtilityStaticVariables.Gui.APP_VERSION);
        frame.setContentPane(new MergerUtilityGUI().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMenuBarInFrame(frame);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void addMenuBarInFrame(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu(MergerUtilityStaticVariables.Gui.MENU_SETTINGS_TITLE);
        JMenuItem mainSettingsMenuItem = new JMenuItem(MergerUtilityStaticVariables.Gui.MENUITEM_MAIN_SETTINGS_TITLE);
        JMenuItem resToCsvSettingsMenuItem = new JMenuItem(MergerUtilityStaticVariables.Gui.MENUITEM_RES_TO_CSV_TITLE);
        JMenuItem csvToResSettingsMenuItem = new JMenuItem(MergerUtilityStaticVariables.Gui.MENUITEM_CSV_TO_RES_TITLE);

        settingsMenu.add(mainSettingsMenuItem);
        settingsMenu.add(resToCsvSettingsMenuItem);
        settingsMenu.add(csvToResSettingsMenuItem);

        menuBar.add(settingsMenu);
        frame.setJMenuBar(menuBar);
    }


    @Override
    public void onSuccessEventCreated(String message) {
        JOptionPane.showMessageDialog(MainPanel, message, "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onErrorEventCreated(String message) {
        JOptionPane.showMessageDialog(MainPanel, message, "Sorry :(", JOptionPane.ERROR_MESSAGE);
    }
}
