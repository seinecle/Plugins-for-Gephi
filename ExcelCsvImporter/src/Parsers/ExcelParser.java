/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parsers;

import Controller.MyFileImporter;
import Utils.Utils;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.Issue;
import org.gephi.io.importer.api.NodeDraft;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

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

 Contributor(s): Clement Levallois

 */
public class ExcelParser {

    String fileName;
    String sheetName;
    boolean headersPresent;
    Integer columnCount;
    Multiset<String> nodesFirst = HashMultiset.create();
    Multiset<String> nodesSecond = HashMultiset.create();
    Multiset<String> edges = HashMultiset.create();
    ContainerLoader container;
    public static int nbColumnFirstAgent;
    public static int nbColumnSecondAgent;

    public ExcelParser(String fileName, String sheetName) {
        this.fileName = fileName;
        this.sheetName = sheetName;
    }

    public ExcelParser(String fileName) {
        this.fileName = fileName;
    }

    public void parse() throws FileNotFoundException, IOException, InvalidFormatException {

        InputStream inp;
        inp = new FileInputStream(fileName);
        Workbook wb = WorkbookFactory.create(inp);

        Row row;
        Sheet sheet = wb.getSheet(sheetName);
        int startingRow = 0;
        boolean breakNow = false;
        for (int i = startingRow; i <= sheet.getLastRowNum(); i++) {
            if (breakNow) {
                break;
            }
            row = sheet.getRow(i);
            if (row == null) {
                break;
            }

            for (int j = 0; j < row.getLastCellNum(); j++) {
                if (row.getCell(j).getStringCellValue().isEmpty() || row.getCell(j).getStringCellValue() == null) {
                    breakNow = true;
                    break;
                }
//                category.setCategoryName(row.getCell(j).getStringCellValue());
            }
        }

        inp.close();
    }

    public void convertToNetwork() throws IOException, InvalidFormatException {

        container = MyFileImporter.container;
        container.setEdgeDefault(EdgeDefault.UNDIRECTED);

        String firstDelimiter;
        String secondDelimiter;
        firstDelimiter = Utils.getCharacter(MyFileImporter.firstConnectorDelimiter);
        secondDelimiter = Utils.getCharacter(MyFileImporter.secondConnectorDelimiter);
        boolean oneTypeOfAgent = MyFileImporter.getFirstConnectedAgent().equals(MyFileImporter.getSecondConnectedAgent());

        nbColumnFirstAgent = MyFileImporter.firstConnectedAgentIndex;
        nbColumnSecondAgent = MyFileImporter.secondConnectedAgentIndex;

        Integer lineCounter = 0;

        InputStream inp;
        inp = new FileInputStream(fileName);
        Workbook wb = WorkbookFactory.create(inp);

        Row row;
        Sheet sheet = wb.getSheet(sheetName);
        int startingRow;
        if (MyFileImporter.headersPresent) {
            startingRow = 1;
        } else {
            startingRow = 0;
        }
        Set<String> linesFirstAgent = new HashSet();
        Set<String> linesSecondAgent = new HashSet();
        for (int i = startingRow; i <= sheet.getLastRowNum(); i++) {

            row = sheet.getRow(i);
            if (row == null) {
                break;
            }

            Cell cell = row.getCell(nbColumnFirstAgent);
            if (cell == null) {
                Issue issue = new Issue("problem with line " + lineCounter + " (empty column " + MyFileImporter.getFirstConnectedAgent() + "). It was skipped in the conversion", Issue.Level.WARNING);
                MyFileImporter.getStaticReport().logIssue(issue);
                continue;
            }

            String firstAgent = row.getCell(nbColumnFirstAgent).getStringCellValue();

            if (firstAgent == null || firstAgent.isEmpty()) {
                Issue issue = new Issue("problem with line " + lineCounter + " (empty column " + MyFileImporter.getFirstConnectedAgent() + "). It was skipped in the conversion", Issue.Level.WARNING);
                MyFileImporter.getStaticReport().logIssue(issue);
                continue;
            }

            if (MyFileImporter.removeDuplicates) {
                boolean newLine = linesFirstAgent.add(firstAgent);
                if (!newLine) {
                    continue;
                }
            }

            String secondAgent = null;

            if (!oneTypeOfAgent) {
                cell = row.getCell(nbColumnSecondAgent);
                if (cell == null) {
                    Issue issue = new Issue("problem with line " + lineCounter + " (empty column " + MyFileImporter.getFirstConnectedAgent() + "). It was skipped in the conversion", Issue.Level.WARNING);
                    MyFileImporter.getStaticReport().logIssue(issue);
                    continue;
                }
                secondAgent = row.getCell(nbColumnSecondAgent).getStringCellValue();
                if (secondAgent == null || secondAgent.isEmpty()) {
                    Issue issue = new Issue("problem with line " + lineCounter + " (empty column " + MyFileImporter.getSecondConnectedAgent() + "). It was skipped in the conversion", Issue.Level.WARNING);
                    MyFileImporter.getStaticReport().logIssue(issue);
                    continue;
                }
                if (MyFileImporter.removeDuplicates) {
                    boolean newLine = linesFirstAgent.add(firstAgent);
                    if (!newLine) {
                        continue;
                    }
                }


            }
            lineCounter++;

            String[] firstAgentSplit;
            String[] secondAgentSplit;


            if (firstDelimiter != null) {
                firstAgentSplit = firstAgent.trim().split(firstDelimiter);
            } else {
                firstAgentSplit = new String[1];
                firstAgentSplit[0] = firstAgent;
            }
            for (String node : firstAgentSplit) {
                nodesFirst.add(node.trim());
            }

            if (!oneTypeOfAgent) {

                if (secondDelimiter != null) {
                    secondAgentSplit = secondAgent.trim().split(secondDelimiter);
                } else {
                    secondAgentSplit = new String[1];
                    secondAgentSplit[0] = secondAgent;
                }
                for (String node : secondAgentSplit) {
                    nodesSecond.add(node.trim());
                }
            } else {
                secondAgentSplit = null;
            }

            String[] both = ArrayUtils.addAll(firstAgentSplit, secondAgentSplit);
            //let's find all connections between all the tags for this picture
            Utils usefulTools = new Utils();
            List<String> connections = usefulTools.getListOfLinks(both,MyFileImporter.removeSelfLoops);
            edges.addAll(connections);
        }


        NodeDraft node;
        AttributeTable atNodes = container.getAttributeModel().getNodeTable();
        AttributeColumn acFrequency = atNodes.addColumn("frequency", AttributeType.INT);
        AttributeColumn acType = atNodes.addColumn("type", AttributeType.STRING);

        for (String n : nodesFirst.elementSet()) {
            node = container.factory().newNodeDraft();
            node.setId(n);
            node.setLabel(n);
            node.addAttributeValue(acFrequency, nodesFirst.count(n));
            node.addAttributeValue(acType, MyFileImporter.getFirstConnectedAgent());
            container.addNode(node);
        }

        for (String n : nodesSecond.elementSet()) {
            node = container.factory().newNodeDraft();
            node.setId(n);
            node.setLabel(n);
            node.addAttributeValue(acFrequency, nodesSecond.count(n));
            node.addAttributeValue(acType, MyFileImporter.getSecondConnectedAgent());
            container.addNode(node);
        }

        //loop for edges
        Integer idEdge = 0;
        EdgeDraft edge;
        for (String e : edges.elementSet()) {
            System.out.println("edge: " + e);

            String sourceNode = e.split("\\|")[0];
            String targetNode = e.split("\\|")[1];
            if (!MyFileImporter.innerLinksIncluded) {
                if ((nodesFirst.contains(sourceNode) & nodesFirst.contains(targetNode)) || (nodesSecond.contains(sourceNode) & nodesSecond.contains(targetNode))) {
                    continue;
                }
            }
            edge = container.factory().newEdgeDraft();
            idEdge = idEdge + 1;
            edge.setSource(container.getNode(sourceNode));
            edge.setTarget(container.getNode(targetNode));
            edge.setWeight((float) edges.count(e));
            edge.setId(String.valueOf(idEdge));
            edge.setType(EdgeDraft.EdgeType.UNDIRECTED);
            container.addEdge(edge);
        }
    }

    public String[] getHeaders() throws FileNotFoundException, IOException, InvalidFormatException {
        InputStream inp;
        inp = new FileInputStream(fileName);
        Workbook wb = WorkbookFactory.create(inp);
        List<String> listHeaders = new ArrayList();

        Row row;
        Sheet sheet = wb.getSheet(sheetName);
        row = sheet.getRow(0);
        for (int j = 0; j < row.getLastCellNum(); j++) {
            if (row.getCell(j).getStringCellValue().isEmpty() || row.getCell(j).getStringCellValue() == null) {
                break;
            }
            listHeaders.add(row.getCell(j).getStringCellValue());
        }
        inp.close();
        return listHeaders.toArray(new String[listHeaders.size()]);
    }

    public String[] getSheetsNames() throws FileNotFoundException, IOException, InvalidFormatException {
        InputStream inp;
        inp = new FileInputStream(fileName);
        Workbook wb = WorkbookFactory.create(inp);
        List<String> listSheetsNames = new ArrayList();

        for (int j = 0; j < wb.getNumberOfSheets(); j++) {
            listSheetsNames.add(wb.getSheetName(j));
        }
        inp.close();
        return listSheetsNames.toArray(new String[listSheetsNames.size()]);
    }

    public Integer getColumnCount() throws FileNotFoundException, IOException, InvalidFormatException {
        InputStream inp;
        inp = new FileInputStream(fileName);
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheet(sheetName);
        Row row = sheet.getRow(0);
        int numColumns = row.getLastCellNum();
        inp.close();
        return numColumns;
    }
}
