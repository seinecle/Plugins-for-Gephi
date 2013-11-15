/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GephiMethods.GexfImporter;
import Parser.ExcelParser;
import Utils.LogUpdate;
import Utils.OpenURI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jfree.util.StringUtils;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//Control//twitterPanel//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "Sciento. Overlay Toolkit",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "graphmode", openAtStartup = true)
@ActionID(category = "Window", id = "Control.twitterPanelTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_twitterPanelAction",
        preferredID = "Sciento. Overlay Toolkit")
@Messages({
    "CTL_twitterPanelAction=Sciento. Overlay Toolkit",
    "CTL_twitterPanelTopComponent=Sciento. Overlay Toolkit",
    "HINT_twitterPanelTopComponent=Sciento. Overlay Toolkit"
})
public final class MainGUIWindow extends TopComponent {

    public static boolean refreshProgress;
    boolean now;
    public String workingDirectory;
    public boolean fileSelected;
    public String fileSelectedPathANdName;
    public String fileSelectedName;
    public static String ISIcolumn;
    public static String yearColumn;
    public String selectedSheet;
    public static String selectedItemToFilterOn;
    public int selectedIndexItemToFilterOn;
    public int[] itemsFilteredInIndex;
    DefaultListModel listModelFullListEntitiesToFilterOn;
    public String[] itemsFilteredInString;
    File f;
    public static ExcelParser excelParser;
    ListSelectionListener listItemsToFilterActionListener;

    public MainGUIWindow() {

        initComponents();
        setName(Bundle.CTL_twitterPanelTopComponent());
        setToolTipText(Bundle.HINT_twitterPanelTopComponent());
        jListSheetsList.setVisible(false);
        jScrollPane7.setVisible(false);
        jScrollPane5.setVisible(false);
        jLabelSheets.setVisible(false);
        jLabelFieldSeparator.setVisible(false);
        jTextFieldFieldSeparator.setVisible(false);
        jListMaps.setSelectedIndex(0);


        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);

        jListFieldsInFile.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ISIcolumn = (String) ((JList) e.getSource()).getSelectedValue();
            }
        });
        jListFields.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                yearColumn = (String) ((JList) e.getSource()).getSelectedValue();
            }
        });
        jListSheetsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedSheet = (String) ((JList) e.getSource()).getSelectedValue();
            }
        });
        listItemsToFilterActionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    selectedItemToFilterOn = (String) ((JList) e.getSource()).getSelectedValue();
                    selectedIndexItemToFilterOn = ((JList) e.getSource()).getSelectedIndex();
                    if (selectedItemToFilterOn != null) {
                        jLabelChooseFieldsFilteredIn.setVisible(true);
                        jListEntitiesFilteredIn.setVisible(true);
                        jScrollPane8.setVisible(true);
                    } else {
                        jLabelChooseFieldsFilteredIn.setVisible(false);
                        jListEntitiesFilteredIn.setVisible(false);
                        jScrollPane8.setVisible(false);

                    }

                    itemsFilteredInString = excelParser.getUniqueValuesInColumn(selectedIndexItemToFilterOn, jTextFieldFieldSeparator.getText());
                    listModelFullListEntitiesToFilterOn = new DefaultListModel();
                    for (String string : itemsFilteredInString) {
                        listModelFullListEntitiesToFilterOn.addElement(string);
                    }
                    jListEntitiesFilteredIn.setModel(listModelFullListEntitiesToFilterOn);

                } catch (FileNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (InvalidFormatException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        };
        jListEntitiesFilteredIn.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                itemsFilteredInIndex = ((JList) e.getSource()).getSelectedIndices();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListMaps = new javax.swing.JList();
        jButtonToPanel2 = new javax.swing.JButton();
        jButtonCredits = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButtonOpenFile = new javax.swing.JButton();
        jButtonToPanel3 = new javax.swing.JButton();
        jRadioButtonJournals = new javax.swing.JRadioButton();
        jRadioButtonISI = new javax.swing.JRadioButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jListSheetsList = new javax.swing.JList();
        jLabelSheets = new javax.swing.JLabel();
        jButtonBackToPanel1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelPanelFieldContainingISIOrJournalNames = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListFieldsInFile = new javax.swing.JList();
        jButtonToPanel4 = new javax.swing.JButton();
        jButtonBackToPanel2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jRadioButtonStaticOverlay = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabelWhatFieldForTime = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListFields = new javax.swing.JList();
        jButtonToPanel5 = new javax.swing.JButton();
        jButtonBackToPanel3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListItemsToFilter = new javax.swing.JList();
        jLabelSelectEntityWhereFilter = new javax.swing.JLabel();
        jButtonRun = new javax.swing.JButton();
        jRadioButtonFilters = new javax.swing.JRadioButton();
        jRadioButtonNoFilter = new javax.swing.JRadioButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jListEntitiesFilteredIn = new javax.swing.JList();
        jLabelChooseFieldsFilteredIn = new javax.swing.JLabel();
        jButtonBackToPanel4 = new javax.swing.JButton();
        jLabelFieldSeparator = new javax.swing.JLabel();
        jTextFieldFieldSeparator = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1000, 1000));

        jTextAreaLog.setColumns(20);
        jTextAreaLog.setRows(3);
        jTextAreaLog.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jTextAreaLog.border.title"))); // NOI18N
        jScrollPane1.setViewportView(jTextAreaLog);

        jListMaps.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "ISI categories 2007" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListMaps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jListMaps);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonToPanel2, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonToPanel2.text")); // NOI18N
        jButtonToPanel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToPanel2ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonCredits, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonCredits.text")); // NOI18N
        jButtonCredits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreditsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButtonCredits, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 386, Short.MAX_VALUE)
                .addComponent(jButtonToPanel2)
                .addGap(26, 26, 26))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonToPanel2)
                    .addComponent(jButtonCredits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonOpenFile, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonOpenFile.text")); // NOI18N
        jButtonOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenFileActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonToPanel3, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonToPanel3.text")); // NOI18N
        jButtonToPanel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToPanel3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonJournals);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonJournals, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jRadioButtonJournals.text")); // NOI18N

        buttonGroup1.add(jRadioButtonISI);
        jRadioButtonISI.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonISI, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jRadioButtonISI.text")); // NOI18N

        jListSheetsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(jListSheetsList);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelSheets, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jLabelSheets.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBackToPanel1, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonBackToPanel1.text")); // NOI18N
        jButtonBackToPanel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToPanel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonISI)
                            .addComponent(jRadioButtonJournals)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jButtonOpenFile)))
                .addGap(115, 115, 115)
                .addComponent(jLabelSheets)
                .addGap(44, 44, 44)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonBackToPanel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonToPanel3)
                .addGap(47, 47, 47))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonOpenFile)
                            .addComponent(jLabelSheets))
                        .addGap(39, 39, 39)
                        .addComponent(jRadioButtonJournals)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonISI))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonToPanel3)
                    .addComponent(jButtonBackToPanel1))
                .addContainerGap())
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelPanelFieldContainingISIOrJournalNames, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jLabelPanelFieldContainingISIOrJournalNames.text")); // NOI18N

        jListFieldsInFile.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jListFieldsInFile);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonToPanel4, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonToPanel4.text")); // NOI18N
        jButtonToPanel4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToPanel4ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBackToPanel2, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonBackToPanel2.text")); // NOI18N
        jButtonBackToPanel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToPanel2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelPanelFieldContainingISIOrJournalNames)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(199, 199, 199))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonBackToPanel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonToPanel4)
                .addGap(43, 43, 43))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPanelFieldContainingISIOrJournalNames)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonToPanel4)
                    .addComponent(jButtonBackToPanel2))
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        buttonGroup2.add(jRadioButtonStaticOverlay);
        jRadioButtonStaticOverlay.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonStaticOverlay, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jRadioButtonStaticOverlay.text")); // NOI18N
        jRadioButtonStaticOverlay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButtonStaticOverlayStateChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton2);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton2, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jRadioButton2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelWhatFieldForTime, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jLabelWhatFieldForTime.text")); // NOI18N

        jListFields.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(jListFields);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonToPanel5, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonToPanel5.text")); // NOI18N
        jButtonToPanel5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToPanel5ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBackToPanel3, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonBackToPanel3.text")); // NOI18N
        jButtonBackToPanel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToPanel3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonStaticOverlay)
                    .addComponent(jRadioButton2))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButtonBackToPanel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonToPanel5))
                            .addComponent(jLabelWhatFieldForTime, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(75, 75, 75))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonStaticOverlay)
                    .addComponent(jLabelWhatFieldForTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton2)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonToPanel5)
                    .addComponent(jButtonBackToPanel3))
                .addContainerGap())
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jListItemsToFilter.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jListItemsToFilter);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelSelectEntityWhereFilter, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jLabelSelectEntityWhereFilter.text")); // NOI18N

        jButtonRun.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButtonRun, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonRun.text")); // NOI18N
        jButtonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRunActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButtonFilters);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonFilters, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jRadioButtonFilters.text")); // NOI18N

        buttonGroup3.add(jRadioButtonNoFilter);
        jRadioButtonNoFilter.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonNoFilter, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jRadioButtonNoFilter.text")); // NOI18N
        jRadioButtonNoFilter.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jRadioButtonNoFilterPropertyChange(evt);
            }
        });

        jListEntitiesFilteredIn.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane8.setViewportView(jListEntitiesFilteredIn);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelChooseFieldsFilteredIn, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jLabelChooseFieldsFilteredIn.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBackToPanel4, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jButtonBackToPanel4.text")); // NOI18N
        jButtonBackToPanel4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToPanel4ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelFieldSeparator, org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jLabelFieldSeparator.text")); // NOI18N

        jTextFieldFieldSeparator.setText(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jTextFieldFieldSeparator.text")); // NOI18N
        jTextFieldFieldSeparator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldFieldSeparatorKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButtonBackToPanel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonRun))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonFilters)
                            .addComponent(jRadioButtonNoFilter))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabelFieldSeparator)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldFieldSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelSelectEntityWhereFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane5))
                        .addGap(72, 72, 72)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelChooseFieldsFilteredIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane8))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSelectEntityWhereFilter)
                    .addComponent(jLabelChooseFieldsFilteredIn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jRadioButtonNoFilter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonFilters))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFieldSeparator)
                    .addComponent(jTextFieldFieldSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRun)
                    .addComponent(jButtonBackToPanel4))
                .addContainerGap())
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 767, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(183, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(536, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(MainGUIWindow.class, "MainGUIWindow.jTabbedPane1.AccessibleContext.accessibleDescription")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonToPanel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToPanel2ActionPerformed

        int firstSelIx = jListMaps.getSelectedIndex();
        String map;
        System.out.println("index: " + firstSelIx);
        if (firstSelIx == 0) {
            map = "overlay_nude_2007.gexf";
        } else {
            map = "overlay_nude_2010.gexf";

        }
        GexfImporter importer = new GexfImporter();
        if (jRadioButtonStaticOverlay.isSelected()) {
            importer.importDynamicGexf(map);
        } else {
            importer.importGexf(map);
        }
        if (jRadioButtonJournals.isSelected()) {
            jLabelPanelFieldContainingISIOrJournalNames.setText("What field contains the journals names?");
        } else {
            jLabelPanelFieldContainingISIOrJournalNames.setText("What field contains ISI categories?");
        }
        excelParser = null;
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButtonToPanel2ActionPerformed

    private void jButtonOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenFileActionPerformed

        JFileChooser chooser = new JFileChooser();
        jListSheetsList.setVisible(false);
        jScrollPane7.setVisible(false);
        jLabelSheets.setVisible(false);

        chooser.setCurrentDirectory(f);
        //chooser.setCurrentDirectory(new java.io.File("D:\\Docs Pro Clement\\E-humanities\\TextMining\\Exported Items\\"));
        chooser.setDialogTitle("Select a file");
        //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                System.out.println("Current directory: " + chooser.getCurrentDirectory().getAbsolutePath());

                workingDirectory = chooser.getCurrentDirectory().getAbsolutePath().toString();
                fileSelected = true;
                fileSelectedPathANdName = chooser.getSelectedFile().toString();
                if (fileSelectedPathANdName == null || !StringUtils.endsWithIgnoreCase(fileSelectedPathANdName, ".xlsx")) {
                    LogUpdate.update("No file selected or wrong file format. It should be an Excel file in format .xlsx");
                    return;
                }
                fileSelectedName = chooser.getSelectedFile().getName();
                System.out.println("Selected File: " + fileSelectedPathANdName);
                LogUpdate.update("Selected File: " + fileSelectedPathANdName);

                excelParser = new ExcelParser(fileSelectedPathANdName);
                String[] sheetsNames = excelParser.getSheetsNames();
                DefaultListModel listModel = new DefaultListModel();
                for (String string : sheetsNames) {
                    listModel.addElement(string);
                }
                jListSheetsList.setModel(listModel);
                jListSheetsList.setSelectedIndex(0);
                jListSheetsList.setVisible(true);
                jScrollPane7.setVisible(true);
                jLabelSheets.setVisible(true);

            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvalidFormatException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            LogUpdate.update("No file selected.");
            System.out.println("No Selection");
        }
    }//GEN-LAST:event_jButtonOpenFileActionPerformed

    private void jButtonToPanel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToPanel3ActionPerformed

        if (fileSelectedPathANdName == null || !StringUtils.endsWithIgnoreCase(fileSelectedPathANdName, ".xlsx")) {
            LogUpdate.update("No file selected or wrong file format. It should be an Excel file in format .xlsx");
            return;
        }
        if (jRadioButtonISI.isSelected()) {
            jLabelPanelFieldContainingISIOrJournalNames.setText("What field contains ISI categories?");
        } else {
            jLabelPanelFieldContainingISIOrJournalNames.setText("What field contains journals names?");
        }
        try {
            excelParser.getHeaders(selectedSheet);
            String[] sheetsNames = excelParser.getSheetsNames();
            DefaultListModel listModel = new DefaultListModel();
            for (String string : sheetsNames) {
                listModel.addElement(string);
            }

            jListFieldsInFile.setModel(excelParser.geHeadersAstListModel());
            jTabbedPane1.setSelectedIndex(2);

        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidFormatException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_jButtonToPanel3ActionPerformed

    private void jButtonToPanel4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToPanel4ActionPerformed
        jListFields.setModel(excelParser.geHeadersAstListModel());
        jListFields.setVisible(false);
        jLabelWhatFieldForTime.setVisible(false);
        jScrollPane6.setVisible(false);

        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButtonToPanel4ActionPerformed

    private void jButtonRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRunActionPerformed

        List<String> elementsKept = new ArrayList();
        if (itemsFilteredInIndex != null) {
            for (int i : itemsFilteredInIndex) {
                elementsKept.add((String) listModelFullListEntitiesToFilterOn.get(i));
            }
        }
        try {
            if (jRadioButtonStaticOverlay.isSelected()) {
                excelParser.countISICategories(jRadioButtonJournals.isSelected(), ISIcolumn, selectedItemToFilterOn, elementsKept.toArray(new String[elementsKept.size()]), jTextFieldFieldSeparator.getText());
                excelParser.overlay();

            } else {
                excelParser.countISICategoriesAndYears(jRadioButtonJournals.isSelected(), ISIcolumn, yearColumn, selectedItemToFilterOn, itemsFilteredInString, jTextFieldFieldSeparator.getText());
                excelParser.overlayDynamic();
            }
            LogUpdate.update("Done. You can now switch to the graph view.");
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidFormatException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_jButtonRunActionPerformed

    private void jButtonToPanel5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToPanel5ActionPerformed
        if (!jRadioButtonStaticOverlay.isSelected() & yearColumn == null) {
            return;
        }
        jListItemsToFilter.setModel(excelParser.geHeadersAstListModel());
        jListItemsToFilter.addListSelectionListener(listItemsToFilterActionListener);

        jListItemsToFilter.setVisible(false);
        jLabelSelectEntityWhereFilter.setVisible(false);
        jScrollPane5.setVisible(false);
        jLabelFieldSeparator.setVisible(false);
        jTextFieldFieldSeparator.setVisible(false);



        jLabelChooseFieldsFilteredIn.setVisible(false);
        jListEntitiesFilteredIn.setVisible(false);
        jScrollPane8.setVisible(false);

        jTabbedPane1.setSelectedIndex(4);

    }//GEN-LAST:event_jButtonToPanel5ActionPerformed

    private void jRadioButtonStaticOverlayStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButtonStaticOverlayStateChanged
        if (!jRadioButtonStaticOverlay.isSelected()) {
            jListFields.setVisible(true);
            jLabelWhatFieldForTime.setVisible(true);
            jScrollPane6.setVisible(true);

        } else {
            jListFields.setVisible(false);
            jLabelWhatFieldForTime.setVisible(false);
            jScrollPane6.setVisible(false);
        }
    }//GEN-LAST:event_jRadioButtonStaticOverlayStateChanged

    private void jButtonBackToPanel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToPanel1ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonBackToPanel1ActionPerformed

    private void jButtonCreditsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreditsActionPerformed
        try {
            OpenURI.open(new URI("http://www.leydesdorff.net/overlaytoolkit/"));
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_jButtonCreditsActionPerformed

    private void jButtonBackToPanel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToPanel2ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButtonBackToPanel2ActionPerformed

    private void jButtonBackToPanel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToPanel3ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButtonBackToPanel3ActionPerformed

    private void jButtonBackToPanel4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToPanel4ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButtonBackToPanel4ActionPerformed

    private void jRadioButtonNoFilterPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jRadioButtonNoFilterPropertyChange
        if (jRadioButtonFilters.isSelected()) {
            LogUpdate.update("Please be patient while it loads...");
            jListItemsToFilter.setVisible(true);
            jLabelSelectEntityWhereFilter.setVisible(true);
            jScrollPane5.setVisible(true);
            jLabelFieldSeparator.setVisible(true);
            jTextFieldFieldSeparator.setVisible(true);

        } else {
            jListItemsToFilter.setVisible(false);
            jLabelSelectEntityWhereFilter.setVisible(false);
            jScrollPane5.setVisible(false);
            jLabelFieldSeparator.setVisible(false);
            jTextFieldFieldSeparator.setVisible(false);


            jLabelChooseFieldsFilteredIn.setVisible(false);
            jListEntitiesFilteredIn.setVisible(false);
            jScrollPane8.setVisible(false);
        }
    }//GEN-LAST:event_jRadioButtonNoFilterPropertyChange

    private void jTextFieldFieldSeparatorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFieldSeparatorKeyTyped
        try {
            itemsFilteredInString = excelParser.getUniqueValuesInColumn(selectedIndexItemToFilterOn, jTextFieldFieldSeparator.getText());
            listModelFullListEntitiesToFilterOn = new DefaultListModel();
            for (String string : itemsFilteredInString) {
                listModelFullListEntitiesToFilterOn.addElement(string);
            }
            jListEntitiesFilteredIn.setModel(listModelFullListEntitiesToFilterOn);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidFormatException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_jTextFieldFieldSeparatorKeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButtonBackToPanel1;
    private javax.swing.JButton jButtonBackToPanel2;
    private javax.swing.JButton jButtonBackToPanel3;
    private javax.swing.JButton jButtonBackToPanel4;
    private javax.swing.JButton jButtonCredits;
    private javax.swing.JButton jButtonOpenFile;
    private javax.swing.JButton jButtonRun;
    private javax.swing.JButton jButtonToPanel2;
    private javax.swing.JButton jButtonToPanel3;
    private javax.swing.JButton jButtonToPanel4;
    private javax.swing.JButton jButtonToPanel5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelChooseFieldsFilteredIn;
    private javax.swing.JLabel jLabelFieldSeparator;
    private javax.swing.JLabel jLabelPanelFieldContainingISIOrJournalNames;
    private javax.swing.JLabel jLabelSelectEntityWhereFilter;
    private javax.swing.JLabel jLabelSheets;
    private javax.swing.JLabel jLabelWhatFieldForTime;
    private javax.swing.JList jListEntitiesFilteredIn;
    private javax.swing.JList jListFields;
    private javax.swing.JList jListFieldsInFile;
    private javax.swing.JList jListItemsToFilter;
    public static javax.swing.JList jListMaps;
    private javax.swing.JList jListSheetsList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButtonFilters;
    private javax.swing.JRadioButton jRadioButtonISI;
    public static javax.swing.JRadioButton jRadioButtonJournals;
    private javax.swing.JRadioButton jRadioButtonNoFilter;
    public static javax.swing.JRadioButton jRadioButtonStaticOverlay;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JTextArea jTextAreaLog;
    public static javax.swing.JTextField jTextFieldFieldSeparator;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
