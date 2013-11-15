/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * C. Levallois
 */
public class FindAllPairs<T extends Comparable<? super T>> {

    private T t;

    public Set<DirectedPair<T, T>> getAllDirectedPairs(Set<T> setObjects) {
        Set<T> setObjectsProcessed = new HashSet<T>();
        Set<DirectedPair<T, T>> setPairs = new HashSet<DirectedPair<T, T>>();
        Iterator<T> setObjectsIteratorA = setObjects.iterator();
        Iterator<T> setObjectsIteratorB;
        T currTA;
        T currTB;
        while (setObjectsIteratorA.hasNext()) {
            currTA = setObjectsIteratorA.next();
            setObjectsIteratorB = setObjects.iterator();
            while (setObjectsIteratorB.hasNext()) {
                currTB = setObjectsIteratorB.next();
                if (!setObjectsProcessed.contains(currTB) && !currTA.equals(currTB)) {
                    setPairs.add(new DirectedPair(currTA, currTB));
                }
            }
            setObjectsProcessed.add(currTA);
        }
        return setPairs;

    }

    public Set<DirectedPair<T, T>> getAllDirectedPairsFromTwoSets(Set<T> setSources, Set<T> setTargets) {
        Set<DirectedPair<T, T>> setPairs = new TreeSet();
        Iterator<T> setSourcesIterator = setSources.iterator();
        Iterator<T> setTargetsIterator;
        T source;
        T target;
        while (setSourcesIterator.hasNext()) {
            source = setSourcesIterator.next();
            setTargetsIterator = setTargets.iterator();
            while (setTargetsIterator.hasNext()) {
                target = setTargetsIterator.next();
                if (!source.equals(target)) {
                    setPairs.add(new DirectedPair(source, target));
                }
            }
        }
        return setPairs;

    }

    public Set<UnDirectedPair<T>> getAllUndirectedPairs(Set<T> setObjects) {
        Set<T> setObjectsProcessed = new HashSet();
        Set<UnDirectedPair<T>> setPairs;
        setPairs = new HashSet();
        Iterator<T> setObjectsIteratorA = setObjects.iterator();
        Iterator<T> setObjectsIteratorB;
        T currTA;
        T currTB;

        while (setObjectsIteratorA.hasNext()) {
            currTA = setObjectsIteratorA.next();
//            setObjectsProcessed.add(currTA);
            setObjectsIteratorB = setObjects.iterator();
            while (setObjectsIteratorB.hasNext()) {
                currTB = setObjectsIteratorB.next();
                if (!setObjectsProcessed.contains(currTB) && !currTA.equals(currTB)) {
                    setPairs.add(new UnDirectedPair(currTA, currTB));
                }
            }
            setObjectsProcessed.add(currTA);
        }
        return setPairs;
    }

    public List<Pair<T>> getAllUndirectedPairsAsList(Set<T> setObjects) {
//        Clock findingAllPairsClock = new Clock("finding all pairs in a set of "+setObjects.size()+" objects");
        List<T> listObjects = new ArrayList();
        listObjects.addAll(setObjects);

        List<Pair<T>> listPairs = new ArrayList();
        Iterator<T> listIterator1 = listObjects.listIterator();
        Iterator<T> listIterator2;
        int count = 1;
        T object1;
        while (listIterator1.hasNext()) {
            object1 = listIterator1.next();
            listIterator2 = listObjects.listIterator(count++);
            while (listIterator2.hasNext()) {
                listPairs.add(new Pair(object1, listIterator2.next()));
            }
        }
//        System.out.println("number of pairs: "+listPairs.size());
//        findingAllPairsClock.closeAndPrintClock();
        return listPairs;
    }
}
