/*
 Copyright 2008-2011 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package ToolUI;

import GUI.MainGUIWindow;
import static GUI.MainGUIWindow.jListMaps;
import static GUI.MainGUIWindow.jRadioButtonStaticOverlay;
import GephiMethods.GexfImporter;
import Parser.ExcelParser;
import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.layout.spi.Layout;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.tools.spi.MouseClickEventListener;
import org.gephi.tools.spi.NodeClickEventListener;
import org.gephi.tools.spi.Tool;
import org.gephi.tools.spi.ToolEventListener;
import org.gephi.tools.spi.ToolSelectionType;
import org.gephi.tools.spi.ToolUI;
import org.openide.util.Exceptions;
//import org.gephi.visualization.VizController;
//import org.gephi.visualization.opengl.text.ColorMode;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Tool.class)
public class UpdateFilterEntities implements Tool {

    private final ShowLabelsToolUI ui = new ShowLabelsToolUI();
    public static int[] itemsSelected;
    public static DefaultListModel model;

    @Override
    public void select() {
        try {
            MainGUIWindow.excelParser.countAllISICategories(MainGUIWindow.jRadioButtonJournals.isSelected(), MainGUIWindow.ISIcolumn);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidFormatException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void unselect() {
    }

    @Override
    public ToolEventListener[] getListeners() {
        return new ToolEventListener[]{
            new MouseClickEventListener() {
                @Override
                public void mouseClick(int[] positionViewport, float[] position3d) {
                }
            },
            new NodeClickEventListener() {
                @Override
                public void clickNodes(Node[] nodes) {
                }
            }};
    }

    @Override
    public ToolUI getUI() {
        return ui;
    }

    @Override
    public ToolSelectionType getSelectionType() {
        return ToolSelectionType.SELECTION;
    }

    private static class ShowLabelsToolUI implements ToolUI {

        @Override
        public JPanel getPropertiesBar(Tool tool) {
            JPanel labelsUI = new JPanel();

            // add a selector for communities
            final DefaultListModel model = new DefaultListModel();
            List<String> entities = new ArrayList();
            if (ExcelParser.listUniqueValues == null) {
                return labelsUI;
            }
            for (String entity : ExcelParser.listUniqueValues) {
                entities.add(entity);
            }
            model.addElement("show all entities");
            for (String entity : entities) {
                model.addElement(entity);
            }
            JScrollPane scrollpane;
            JList jList = new JList(model);
            final JLabel jLabel = new JLabel();
            final JCheckBox jCheckBox = new JCheckBox("relative view");
            jList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    itemsSelected = ((JList) e.getSource()).getSelectedIndices();
                }
            });
            JButton button = new JButton("Update");
            JButton buttonReload = new JButton("Reload initial map");
            buttonReload.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GraphController gc = Lookup.getDefault().lookup(GraphController.class);
                    GraphModel graphModel = gc.getModel();
                    Graph graph = graphModel.getGraph();
                    graph.clear();
                    int firstSelIx = MainGUIWindow.jListMaps.getSelectedIndex();
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
                }
            });
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GraphController gc = Lookup.getDefault().lookup(GraphController.class);
                    GraphModel graphModel = gc.getModel();
                    Graph graph = graphModel.getGraph();
                    AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
                    List<String> elementsKept = new ArrayList();
                    if (itemsSelected != null) {
                        for (int i : itemsSelected) {
                            elementsKept.add((String) model.get(i));
                        }
                    }
                    if (!jCheckBox.isSelected()) {
                        if (MainGUIWindow.jRadioButtonStaticOverlay.isSelected()) {
                            try {
                                MainGUIWindow.excelParser.countISICategories(MainGUIWindow.jRadioButtonJournals.isSelected(), MainGUIWindow.ISIcolumn, MainGUIWindow.selectedItemToFilterOn, elementsKept.toArray(new String[elementsKept.size()]), MainGUIWindow.jTextFieldFieldSeparator.getText());
                                MainGUIWindow.excelParser.overlay();

                                AttributeColumn freqColumn = attributeModel.getNodeTable().getColumn("freq");
                                RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
                                Ranking freqRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, freqColumn.getId());

                                AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
                                sizeTransformer.setMinSize(5);
                                sizeTransformer.setMaxSize(50);
                                rankingController.transform(freqRanking, sizeTransformer);
                            } catch (FileNotFoundException ex) {
                                Exceptions.printStackTrace(ex);
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            } catch (InvalidFormatException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        } else {
                            try {
                                MainGUIWindow.excelParser.countISICategoriesAndYears(MainGUIWindow.jRadioButtonJournals.isSelected(), MainGUIWindow.ISIcolumn, MainGUIWindow.yearColumn, MainGUIWindow.selectedItemToFilterOn, elementsKept.toArray(new String[elementsKept.size()]), MainGUIWindow.jTextFieldFieldSeparator.getText());
                                MainGUIWindow.excelParser.overlay();
                            } catch (FileNotFoundException ex) {
                                Exceptions.printStackTrace(ex);
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            } catch (InvalidFormatException ex) {
                                Exceptions.printStackTrace(ex);
                            }

                        }
                        jLabel.setText(Arrays.toString(elementsKept.toArray(new String[elementsKept.size()])));
                    } else {
                        try {
                            MainGUIWindow.excelParser.countISICategories(MainGUIWindow.jRadioButtonJournals.isSelected(), MainGUIWindow.ISIcolumn, MainGUIWindow.selectedItemToFilterOn, elementsKept.toArray(new String[elementsKept.size()]), MainGUIWindow.jTextFieldFieldSeparator.getText());
                            MainGUIWindow.excelParser.overlayRelativeToAverage();

                            for (Node node : graph.getNodes().toArray()) {
                                node.getNodeData().setSize(5f);
                            }

                            AttributeColumn freqColumn = attributeModel.getNodeTable().getColumn("freq");
                            RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
                            Ranking relativeFreqRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, freqColumn.getId());

                            AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);

                            float[] positions = {0f, 0.5f, 1f};
                            colorTransformer.setColorPositions(positions);
                            colorTransformer.setColors(new Color[]{Color.RED, Color.GRAY, Color.CYAN});
                            rankingController.transform(relativeFreqRanking, colorTransformer);

                        } catch (FileNotFoundException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (InvalidFormatException ex) {
                            Exceptions.printStackTrace(ex);
                        }

                    }
                }
            });
            jList.setVisibleRowCount(3);
            scrollpane = new JScrollPane(jList);
            labelsUI.add(buttonReload);
            labelsUI.add(scrollpane);
            labelsUI.add(button);
            labelsUI.add(jCheckBox);


            return labelsUI;
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(getClass().getResource("/Resources/plus.png"));
        }

        @Override
        public String getName() {
            return "Select entity";
        }

        @Override
        public String getDescription() {
            return "Select an entity to Overlay";
        }

        @Override
        public int getPosition() {
            return 1000;
        }
    }
}
