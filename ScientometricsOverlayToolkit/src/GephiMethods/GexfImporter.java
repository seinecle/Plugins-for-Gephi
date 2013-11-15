/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GephiMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.datalab.api.AttributeColumnsController;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.filesystems.FileUtil;
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
public class GexfImporter {

    public void importGexf(String filePath) {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        if (pc.getCurrentProject() == null) {
            pc.newProject();
        }

        Workspace workspace = pc.getCurrentWorkspace();

        GraphController gc = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = gc.getModel();
        Graph graph = graphModel.getGraph();

//Get models and controllers for this new workspace - will be useful later
        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();

        ImportController importController = Lookup.getDefault().lookup(ImportController.class);

//Import file       
        Container container = null;
        try {
            InputStream mapStream = GexfImporter.class.getResourceAsStream("/Resources/" + filePath);
            File tempFile = File.createTempFile("mapFile", ".gexf");
            tempFile = FileUtil.normalizeFile(tempFile);
            tempFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tempFile);
            IOUtils.copy(mapStream, out);
            container = importController.importFile(tempFile);
            container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);   //Force DIRECTED
        } catch (Exception ex) {
            System.out.println("Exception in the resource loading phase (gexf importer)");
        }

//Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

//See if graph is well imported
        System.out.println(
                "Nodes: " + graph.getNodeCount());
        System.out.println(
                "Edges: " + graph.getEdgeCount());

    }

    public void importDynamicGexf(String filePath) {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        if (pc.getCurrentProject() == null) {
            pc.newProject();
        }

        Workspace workspace = pc.getCurrentWorkspace();

        //Add dynamic properties        

        GraphController gc = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = gc.getModel();
        AttributeColumnsController acc = Lookup.getDefault().lookup(AttributeColumnsController.class);

        Graph graph = graphModel.getGraph();

//Get models and controllers for this new workspace - will be useful later
        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();

//        AttributeColumn nodesDynFreqColumn = acc.addAttributeColumn(attributeModel.getNodeTable(), "freq", AttributeType.DYNAMIC_FLOAT);//Attribute intervals column for nodes


        ImportController importController = Lookup.getDefault().lookup(ImportController.class);

//Import file       
        Container container = null;
        try {
            InputStream mapStream = GexfImporter.class.getResourceAsStream("/Resources/" + filePath);
            File tempFile = File.createTempFile("mapFile", ".gexf");
            tempFile = FileUtil.normalizeFile(tempFile);
            tempFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tempFile);
            IOUtils.copy(mapStream, out);
            container = importController.importFile(tempFile);
            container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);   //Force DIRECTED
        } catch (Exception ex) {
            System.out.println("Exception in the resource loading phase (gexf importer)");
        }

//Append dynamic imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);




//See if graph is well imported
        System.out.println(
                "Nodes: " + graph.getNodeCount());
        System.out.println(
                "Edges: " + graph.getEdgeCount());

    }
}
