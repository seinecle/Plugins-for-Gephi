/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gaze;

import com.google.common.collect.TreeMultimap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author C. Levallois
 */
public class JgraphTBuilder {

    static DirectedGraph<Integer, DefaultEdge> g = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
    HashSet<Integer> vertices = new HashSet();

    JgraphTBuilder(TreeMultimap<Integer, Integer> mapUndirected) {



        Iterator<Entry<Integer, Integer>> ITmap = mapUndirected.entries().iterator();

        while (ITmap.hasNext()) {
            Entry<Integer, Integer> currEntry = ITmap.next();

            g.addVertex(currEntry.getKey());
            g.addVertex(currEntry.getValue());

            g.addEdge(currEntry.getKey(), currEntry.getValue());




        }

    }

    DirectedGraph<Integer, DefaultEdge> getGraph() {

        return g;
    }
}
