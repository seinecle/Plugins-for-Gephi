/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import ResourceLoaders.JournalAbbreviationsMapping;
import ResourceLoaders.JournalToISICategoriesMapping;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.data.attributes.type.DynamicFloat;
import org.gephi.data.attributes.type.Interval;
import org.gephi.datalab.api.AttributeColumnsController;
import org.gephi.dynamic.api.DynamicController;
import org.gephi.dynamic.api.DynamicModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.openide.util.Lookup;

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
    String ISIColumn;
    int ISIColumnIndex;
    String yearColumn;
    int yearColumnIndex;
    String entitiesColumn;
    int entitiesColumnIndex;
    String selectedSheet;
    static public Multiset<String> ISICategories;
    static public Multiset<String> ISICategoriesPerYear;
    public DefaultListModel listModel;
    JournalAbbreviationsMapping journalAbbreviationMapping;
    JournalToISICategoriesMapping journalToISICategoriesMapping;
    public static List<String> listUniqueValues;
    InputStream inp;
    Workbook wb;
    private boolean mapsLoaded = false;
    static public Multiset<String> allISICategories;
    
    public static float minDiff;
    public static float maxDiff;
    public static float averageDiff;


    public ExcelParser(String fileName) throws FileNotFoundException, IOException, InvalidFormatException {
        this.fileName = fileName;
        inp = new FileInputStream(fileName);
        wb = WorkbookFactory.create(inp);
    }

    public void countISICategories(boolean journalTitles, String ISIColumnArg, String entitiesColumnArg, String[] entities, String jTextFieldFieldSeparator) throws FileNotFoundException, IOException, InvalidFormatException {
        String[] headers = getHeaders(selectedSheet);
        ISICategories = HashMultiset.create();

        Set<String> entitiesToKeep = new HashSet();
        if (entities != null) {
            entitiesToKeep.addAll(Arrays.asList(entities));
        }

        if (journalTitles & !mapsLoaded) {
            journalAbbreviationMapping = new JournalAbbreviationsMapping();
            journalAbbreviationMapping.loadMap();

            journalToISICategoriesMapping = new JournalToISICategoriesMapping();
            journalToISICategoriesMapping.loadMap();
            mapsLoaded = true;
        }

        int index = 0;
        for (String header : headers) {
            if (header.equals(ISIColumnArg)) {
                ISIColumn = header;
                ISIColumnIndex = index;
            }
            index++;
        }

        index = 0;
        if (entitiesColumnArg != null) {
            for (String header : headers) {
                if (header.equals(entitiesColumnArg)) {
                    entitiesColumn = header;
                    entitiesColumnIndex = index;
                }
                index++;
            }
        }



        Row row;
        Sheet sheet = wb.getSheet(selectedSheet);
        int startingRow = 1;
        boolean breakNow = false;
        for (int i = startingRow; i <= sheet.getLastRowNum(); i++) {
            if (breakNow) {
                break;
            }
            row = sheet.getRow(i);
            if (row == null) {
                break;
            }

            if (!entitiesToKeep.contains("show all entities")) {
                if (entitiesColumnArg != null) {
                    Cell entityCell = row.getCell(entitiesColumnIndex);
                    if (entityCell == null) {
                        continue;
                    }

                    int cellType = entityCell.getCellType();
                    //Strings and mumbers are cell types 0 and 1
                    if (cellType > 1) {
                        continue;
                    }
                    String entity = null;
                    if (cellType == 1) {
                        entity = entityCell.getStringCellValue();
                    }
                    if (cellType == 0) {
                        entity = String.valueOf(entityCell.getNumericCellValue());
                    }

                    if (jTextFieldFieldSeparator != null & !jTextFieldFieldSeparator.trim().isEmpty()) {
                        boolean found = false;
                        String[] entitySplitted = entity.split(jTextFieldFieldSeparator);
                        for (int j = 0; j < entitySplitted.length; j++) {
                            if (entitiesToKeep.contains(entitySplitted[j])) {
                                found = true;
                            }
                        }
                        if (!found) {
                            continue;
                        }
                    } else if (!entitiesToKeep.contains(entity)) {
                        continue;
                    }
                }
            }
            Cell ISICell = row.getCell(ISIColumnIndex);
            if (ISICell == null) {
                continue;
            }
            int cellType = ISICell.getCellType();
            //Strings and mumbers are cell types 0 and 1
            if (cellType > 1) {
                continue;
            }
            String ISICellStringValue = null;
            if (cellType == 1) {
                ISICellStringValue = ISICell.getStringCellValue().toLowerCase();
            }
            if (cellType == 0) {
                ISICellStringValue = String.valueOf(ISICell.getNumericCellValue()).toLowerCase();
                System.out.println("cell with numerics type detected: " + ISICellStringValue);
            }

            ISICellStringValue = ISICellStringValue.toLowerCase();

            if (journalTitles) {
                if (!journalAbbreviationMapping.getJournalsToAbbrev().containsKey(ISICellStringValue)) {
                    Collection<String> possibleJournalsForThisAbbrev = journalAbbreviationMapping.getAbbrevToJournals().get(ISICellStringValue);
                    if (possibleJournalsForThisAbbrev.isEmpty()) {
                        continue;
                    } else {
                        ISICellStringValue = possibleJournalsForThisAbbrev.iterator().next();
                    }
                }
                Collection<String> ISICatsFotThisJournal = journalToISICategoriesMapping.getJournalsToISI().get(ISICellStringValue);
                for (String ISICat : ISICatsFotThisJournal) {
                    ISICategories.add((ISICat));
                }
            } else {
                if (row.getCell(ISIColumnIndex) != null) {
                    ISICategories.add(ISICellStringValue);
                }
            }
        }

    }

    public void countAllISICategories(boolean journalTitles, String ISIColumnArg) throws FileNotFoundException, IOException, InvalidFormatException {
        String[] headers = getHeaders(selectedSheet);
        allISICategories = HashMultiset.create();


        if (journalTitles & !mapsLoaded) {
            journalAbbreviationMapping = new JournalAbbreviationsMapping();
            journalAbbreviationMapping.loadMap();

            journalToISICategoriesMapping = new JournalToISICategoriesMapping();
            journalToISICategoriesMapping.loadMap();
            mapsLoaded = true;
        }

        int index = 0;
        for (String header : headers) {
            if (header.equals(ISIColumnArg)) {
                ISIColumn = header;
                ISIColumnIndex = index;
            }
            index++;
        }


        Row row;
        Sheet sheet = wb.getSheet(selectedSheet);
        int startingRow = 1;
        boolean breakNow = false;
        for (int i = startingRow; i <= sheet.getLastRowNum(); i++) {
            if (breakNow) {
                break;
            }
            row = sheet.getRow(i);
            if (row == null) {
                break;
            }

            Cell ISICell = row.getCell(ISIColumnIndex);
            if (ISICell == null) {
                continue;
            }
            int cellType = ISICell.getCellType();
            //Strings and mumbers are cell types 0 and 1
            if (cellType > 1) {
                continue;
            }
            String ISICellStringValue = null;
            if (cellType == 1) {
                ISICellStringValue = ISICell.getStringCellValue().toLowerCase();
            }
            if (cellType == 0) {
                ISICellStringValue = String.valueOf(ISICell.getNumericCellValue()).toLowerCase();
                System.out.println("cell with numerics type detected: " + ISICellStringValue);
            }

            ISICellStringValue = ISICellStringValue.toLowerCase();

            if (journalTitles) {
                if (!journalAbbreviationMapping.getJournalsToAbbrev().containsKey(ISICellStringValue)) {
                    Collection<String> possibleJournalsForThisAbbrev = journalAbbreviationMapping.getAbbrevToJournals().get(ISICellStringValue);
                    if (possibleJournalsForThisAbbrev.isEmpty()) {
                        continue;
                    } else {
                        ISICellStringValue = possibleJournalsForThisAbbrev.iterator().next();
                    }
                }
                Collection<String> ISICatsFotThisJournal = journalToISICategoriesMapping.getJournalsToISI().get(ISICellStringValue);
                for (String ISICat : ISICatsFotThisJournal) {
                    allISICategories.add((ISICat));
                }
            } else {
                if (row.getCell(ISIColumnIndex) != null) {
                    allISICategories.add(ISICellStringValue);
                }
            }
        }

    }

    public void countISICategoriesAndYears(boolean journalTitles, String ISIColumnArg, String yearColumnArg, String entitiesColumnArg, String[] entities, String jTextFieldFieldSeparator) throws FileNotFoundException, IOException, InvalidFormatException {
        String[] headers = getHeaders(selectedSheet);
        ISICategories = HashMultiset.create();
        ISICategoriesPerYear = HashMultiset.create();


        if (journalTitles & !mapsLoaded) {
            journalAbbreviationMapping = new JournalAbbreviationsMapping();
            journalAbbreviationMapping.loadMap();

            journalToISICategoriesMapping = new JournalToISICategoriesMapping();
            journalToISICategoriesMapping.loadMap();
            mapsLoaded = true;
        }

        Set<String> entitiesToKeep = new HashSet();
        if (entities != null) {
            entitiesToKeep.addAll(Arrays.asList(entities));
        }
        int index = 0;
        for (String header : headers) {
            if (header.equals(ISIColumnArg)) {
                ISIColumn = header;
                ISIColumnIndex = index;
            }
            index++;
        }

        index = 0;
        if (entitiesColumnArg != null) {
            for (String header : headers) {
                if (header.equals(entitiesColumnArg)) {
                    entitiesColumn = header;
                    entitiesColumnIndex = index;
                }
                index++;
            }
        }

        index = 0;
        for (String header : headers) {
            if (header.equals(yearColumnArg)) {
                yearColumn = header;
                yearColumnIndex = index;
            }
            index++;
        }


        Row row;
        Sheet sheet = wb.getSheet(selectedSheet);
        int startingRow = 1;
        boolean breakNow = false;
        for (int i = startingRow; i <= sheet.getLastRowNum(); i++) {
            if (breakNow) {
                break;
            }
            row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            if (!entitiesToKeep.contains("show all entities")) {

                if (entitiesColumnArg != null) {
                    Cell entityCell = row.getCell(entitiesColumnIndex);
                    if (entityCell == null) {
                        continue;
                    }

                    int cellType = entityCell.getCellType();
                    //Strings and mumbers are cell types 0 and 1
                    if (cellType > 1) {
                        continue;
                    }
                    String entity = null;
                    if (cellType == 1) {
                        entity = entityCell.getStringCellValue();
                    }
                    if (cellType == 0) {
                        entity = String.valueOf(entityCell.getNumericCellValue());
                    }

                    if (jTextFieldFieldSeparator != null & !jTextFieldFieldSeparator.trim().isEmpty()) {
                        boolean found = false;
                        String[] entitySplitted = entity.split(jTextFieldFieldSeparator);
                        for (int j = 0; i < entitySplitted.length; j++) {
                            if (entitiesToKeep.contains(entitySplitted[j])) {
                                found = true;
                            }
                        }
                        if (!found) {
                            continue;
                        }
                    } else if (!entitiesToKeep.contains(entity)) {
                        continue;
                    }
                }
            }
            if (row.getCell(ISIColumnIndex) != null & row.getCell(yearColumnIndex) != null) {
                Cell yearCell = row.getCell(yearColumnIndex);
                Cell ISIColCell = row.getCell(ISIColumnIndex);
                int cellType = ISIColCell.getCellType();
                //Strings and mumbers are cell types 0 and 1
                if (cellType > 1) {
                    continue;
                }
                String ISICol = null;
                if (cellType == 1) {
                    ISICol = ISIColCell.getStringCellValue().toLowerCase();
                }
                if (cellType == 0) {
                    ISICol = String.valueOf(ISIColCell.getNumericCellValue()).toLowerCase();
                }

                cellType = yearCell.getCellType();
                //Strings and mumbers are cell types 0 and 1
                if (cellType > 1) {
                    continue;
                }
                String year = null;
                if (cellType == 1) {
                    year = yearCell.getStringCellValue();
                }
                if (cellType == 0) {
                    year = String.valueOf(yearCell.getNumericCellValue());
                }
                if (journalTitles) {
                    if (!journalAbbreviationMapping.getJournalsToAbbrev().containsKey(ISICol)) {
                        Collection<String> possibleJournalsForThisAbbrev = journalAbbreviationMapping.getAbbrevToJournals().get(ISICol);
                        if (possibleJournalsForThisAbbrev.isEmpty()) {
                            continue;
                        } else {
                            ISICol = possibleJournalsForThisAbbrev.iterator().next();
                        }
                    }
                    Collection<String> ISICatsFotThisJournal = journalToISICategoriesMapping.getJournalsToISI().get(ISICol);
                    for (String ISICat : ISICatsFotThisJournal) {
                        ISICategoriesPerYear.add((ISICat + "|" + year.trim()));
                        ISICategories.add((ISICat));
                    }
                } else {
                    ISICategoriesPerYear.add((ISICol + "|" + year.trim()));
                    ISICategories.add((ISICol));
                }
            }
        }
    }

    public void overlay() throws IOException, InvalidFormatException {

        GraphController gc = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = gc.getModel();
        Graph graph = graphModel.getGraph();
        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        AttributeColumnsController acc = Lookup.getDefault().lookup(AttributeColumnsController.class);
        AttributeColumn freqCol = acc.addAttributeColumn(attributeModel.getNodeTable(), "freq", AttributeType.FLOAT);//Existence intervals column for nodes


        for (Node node : graph.getNodes().toArray()) {
            String nodeLabel = node.getNodeData().getLabel();
            if (ISICategories.contains(nodeLabel.toLowerCase())) {
                node.getNodeData().getAttributes().setValue("freq", (float) ISICategories.count(nodeLabel));
            } else {
                System.out.println("node label: " + node.getNodeData().getLabel());
                node.getAttributes().setValue("freq", 0f);
            }
        }
    }

    public void overlayRelativeToAverage() throws IOException, InvalidFormatException {

        GraphController gc = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = gc.getModel();
        Graph graph = graphModel.getGraph();
        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        AttributeColumnsController acc = Lookup.getDefault().lookup(AttributeColumnsController.class);
        AttributeColumn freqCol = acc.addAttributeColumn(attributeModel.getNodeTable(), "freq", AttributeType.FLOAT);//Existence intervals column for nodes


        minDiff = 1000f;
        maxDiff = -1000f;
        
        for (Node node : graph.getNodes().toArray()) {
            String nodeLabel = node.getNodeData().getLabel();
            if (ISICategories.contains(nodeLabel.toLowerCase())) {
                float relativeFreqInTotal = (float)allISICategories.count(nodeLabel)/allISICategories.size();
                float relativeFreqInSelection = (float)ISICategories.count(nodeLabel)/ISICategories.size();
                float diff = relativeFreqInTotal-relativeFreqInSelection;
                if (diff<minDiff){
                    minDiff = diff;
                }
                if (diff>maxDiff){
                    maxDiff = diff;
                }
                node.getNodeData().getAttributes().setValue("freq", diff);
            }
        }

        averageDiff = (maxDiff+minDiff)/2;
        for (Node node : graph.getNodes().toArray()) {
            String nodeLabel = node.getNodeData().getLabel();
            if (!ISICategories.contains(nodeLabel.toLowerCase())) {
                node.getNodeData().getAttributes().setValue("freq", averageDiff);
            }
        }
        
    }

    public void overlayDynamic() throws IOException, InvalidFormatException {

        GraphController gc = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = gc.getModel();
        Graph graph = graphModel.getGraph();
        DynamicModel dynamicModel = Lookup.getDefault().lookup(DynamicController.class).getModel();//Necessary to create dynamic model, otherwise dynamics usage won't work

        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        AttributeColumnsController acc = Lookup.getDefault().lookup(AttributeColumnsController.class);
        AttributeColumn freqCol = acc.addAttributeColumn(attributeModel.getNodeTable(), "freq", AttributeType.DYNAMIC_FLOAT);//Existence intervals column for nodes

//        AttributeColumn nodesTimeIntervalColumn = acc.addAttributeColumn(attributeModel.getNodeTable(), "Time Interval", AttributeType.TIME_INTERVAL);//Existence intervals column for nodes
        DynamicFloat dynFloat;
        Interval<Float> interval;
        List<Interval<Float>> listDynFloats;
        Double minYear = 10000d;
        Double maxYear = 0d;
        for (Node node : graph.getNodes().toArray()) {


            //if we woul want the nodes themselves to appear or disappear - not just their attributes
//            List<Interval> interval1List = new ArrayList<Interval>();
//            interval1List.add(new Interval(2000, 2005));
//            interval1List.add(new Interval(2007, 2010));
//            interval1List.add(new Interval(2012, 2013));
//            TimeInterval interval1 = new TimeInterval(interval1List);
//            node.getNodeData().getAttributes().setValue(nodesTimeIntervalColumn.getIndex(), interval1);
            //Time interval for node 1
//            System.out.println(node.getNodeData().getAttributes().getValue(nodesTimeIntervalColumn.getIndex()));



            String nodeLabel = node.getNodeData().getLabel().toLowerCase();
            listDynFloats = new ArrayList();
            if (ISICategories.contains(nodeLabel)) {
                for (String catYear : ISICategoriesPerYear.elementSet()) {
                    if (catYear.contains(nodeLabel)) {
                        Double timeBoundary = Double.parseDouble(catYear.split("\\|")[1]);
                        if (timeBoundary > maxYear) {
                            maxYear = timeBoundary;
                        }
                        if (timeBoundary < minYear) {
                            minYear = timeBoundary;
                        }
                        interval = new Interval(new Interval(timeBoundary, timeBoundary), (float) ISICategoriesPerYear.count(catYear));
                        listDynFloats.add(interval);
                    }
                }
                dynFloat = new DynamicFloat(listDynFloats);
                node.getNodeData().getAttributes().setValue("freq", dynFloat);
            }
        }
        for (Node node : graph.getNodes().toArray()) {
            String nodeLabel = node.getNodeData().getLabel().toLowerCase();
            if (!ISICategories.contains(nodeLabel)) {
                interval = new Interval(new Interval(minYear, maxYear), 0f);
                dynFloat = new DynamicFloat(interval);
                node.getNodeData().getAttributes().setValue("freq", dynFloat);
            }
        }
    }

    public String[] getHeaders(String selectedSheetArg) throws FileNotFoundException, IOException, InvalidFormatException {
        selectedSheet = selectedSheetArg;
        listModel = new DefaultListModel();
        List<String> listHeaders = new ArrayList();

        Row row;
        Sheet sheet = wb.getSheet(selectedSheet);
        row = sheet.getRow(0);
        for (int j = 0; j < row.getLastCellNum(); j++) {
            if (row.getCell(j) == null || row.getCell(j).getStringCellValue().isEmpty() || row.getCell(j).getStringCellValue() == null) {
                break;
            }
            listHeaders.add(row.getCell(j).getStringCellValue());
            listModel.addElement(row.getCell(j).getStringCellValue());

        }
        return listHeaders.toArray(new String[listHeaders.size()]);
    }

    public String[] getSheetsNames() throws FileNotFoundException, IOException, InvalidFormatException {
        List<String> listSheetsNames = new ArrayList();

        for (int j = 0; j < wb.getNumberOfSheets(); j++) {
            listSheetsNames.add(wb.getSheetName(j));
        }
        return listSheetsNames.toArray(new String[listSheetsNames.size()]);
    }

    public Integer getColumnCount() throws FileNotFoundException, IOException, InvalidFormatException {
        Sheet sheet = wb.getSheet(sheetName);
        Row row = sheet.getRow(0);
        int numColumns = row.getLastCellNum();
        return numColumns;
    }

    public String[] getUniqueValuesInColumn(Integer columnIndex, String jTextFieldFieldSeparator) throws FileNotFoundException, IOException, InvalidFormatException {

        Set<String> uniqueValues = new HashSet();
        Row row;
        Sheet sheet = wb.getSheet(selectedSheet);
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

            if (row.getCell(columnIndex) == null || row.getCell(columnIndex).getCellType() != 1) {
                continue;
            }

            String entity = row.getCell(columnIndex).getStringCellValue();

            if (jTextFieldFieldSeparator != null & !jTextFieldFieldSeparator.trim().isEmpty()) {
                String[] entitySplitted = entity.split(jTextFieldFieldSeparator);
                for (int j = 0; j < entitySplitted.length; j++) {
                    uniqueValues.add(entitySplitted[j].trim());
                }
            } else {
                uniqueValues.add(entity.trim());
            }
        }
        listUniqueValues = new ArrayList();
        listUniqueValues.addAll(uniqueValues);
        Collections.sort(listUniqueValues);
        return listUniqueValues.toArray(new String[listUniqueValues.size()]);
    }

    public DefaultListModel geHeadersAstListModel() {
        return listModel;
    }
}
