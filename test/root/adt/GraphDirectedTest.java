/*
 * Copyright 2006-2016 Edward Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.adt;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.adt.GraphDirected.Edge;
import root.adt.GraphDirected.EdgeList;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 * Test the {@link GraphDirected} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class GraphDirectedTest extends TestCase {

	private GraphDirected graph;

	public GraphDirectedTest() {
		super("GraphDirected");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.graph = new GraphDirected();
	}

	@Test
	public void testAdd() {
		assertEquals(0, this.graph.getSize());
		this.graph.add("foo");
		assertEquals(1, this.graph.getSize());
		assertTrue(this.graph.containsVertex("foo"));
	}

	@Test
	public void testAddAllVertices() {
		final ListArray<String> vertices = new ListArray<>("foo", "bar");

		assertEquals(0, this.graph.getSize());
		this.graph.addAll(vertices);

		assertEquals(2, this.graph.getSize());
		assertTrue(this.graph.containsVertex("foo"));
		assertTrue(this.graph.containsVertex("bar"));
	}

	@Test
	public void testAddAllVerticesWithEdges() {
		final ListArray<String> vertices = new ListArray<>("foo", "bar");

		assertEquals(0, this.graph.getSize());
		this.graph.addAll(vertices, 1);

		assertEquals(2, this.graph.getSize());
		assertTrue(this.graph.containsVertex("foo"));
		assertTrue(this.graph.containsVertex("bar"));
		assertTrue(this.graph.containsEdge("foo", "bar"));
		assertEquals(1, this.graph.getEdge("foo", "bar").getWeight());
	}

	@Test
	public void testAddEdge() {
		final ListArray<String> vertices = new ListArray<>("foo", "bar");

		this.graph.addAll(vertices);
		assertFalse(this.graph.containsEdge("foo", "bar"));

		this.graph.addEdge("foo", "bar", 3);
		final Edge edge = this.graph.getEdge("foo", "bar");
		assertNotNull(edge);
		assertEquals(3, edge.getWeight());
	}

	@Test
	public void testClear() {
		assertEquals(0, this.graph.getSize());
		assertTrue(this.graph.isEmpty());

		this.graph.add("foo");
		assertEquals(1, this.graph.getSize());
		assertFalse(this.graph.isEmpty());

		this.graph.add("bar");
		assertEquals(2, this.graph.getSize());

		this.graph.addEdge("foo", "bar", 1);
		assertTrue(this.graph.containsEdge("foo", "bar"));

		this.graph.clear();
		assertEquals(0, this.graph.getSize());
		assertTrue(this.graph.isEmpty());
	}

	@Test
	public void testConstructorCapacity() {
		final GraphDirected g = new GraphDirected(15);
		assertEquals(0, g.adjacencyMap.size);
		assertEquals(16, g.adjacencyMap.capacity);
		assertEquals(23, g.adjacencyMap.table.length);
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.graph.adjacencyMap.size);
		assertEquals(8, this.graph.adjacencyMap.capacity);
		assertEquals(11, this.graph.adjacencyMap.table.length);
	}

	@Test
	public void testContainsEdge() {
		final ListArray<String> vertices = new ListArray<>("foo", "bar");

		this.graph.addAll(vertices);
		assertFalse(this.graph.containsEdge("foo", "bar"));

		this.graph.addEdge("foo", "bar", 1);
		assertTrue(this.graph.containsEdge("foo", "bar"));
	}

	@Test
	public void testContainsVertex() {
		assertFalse(this.graph.containsVertex("foo"));
		this.graph.add("foo");
		assertTrue(this.graph.containsVertex("foo"));
	}

	@Test
	public void testEquals() {
		final GraphDirected testGraph = new GraphDirected();
		assertTrue(this.graph.equals(testGraph));

		testGraph.add("foo");
		assertFalse(this.graph.equals(testGraph));

		this.graph.add("foo");
		assertTrue(this.graph.equals(testGraph));

		testGraph.add("bar");
		assertFalse(this.graph.equals(testGraph));

		this.graph.add("bar");
		assertTrue(this.graph.equals(testGraph));

		testGraph.addEdge("foo", "bar", 1);
		assertFalse(this.graph.equals(testGraph));

		this.graph.addEdge("foo", "bar", 1);
		assertTrue(this.graph.equals(testGraph));

		testGraph.addEdge("foo", "bar", 3);
		assertFalse(this.graph.equals(testGraph));

		this.graph.addEdge("foo", "bar", 3);
		assertTrue(this.graph.equals(testGraph));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.graph.extract(extractor);
		assertEquals("{}", extractor.toString());

		this.graph.add("foo");

		extractor.clear();
		this.graph.extract(extractor);
		assertEquals("{foo : []}", extractor.toString());

		this.graph.add("bar");

		extractor.clear();
		this.graph.extract(extractor);
		assertEquals("{foo : []\nbar : []}", extractor.toString());

		this.graph.addEdge("foo", "bar", 3);

		extractor.clear();
		this.graph.extract(extractor);
		assertEquals("{foo : [(bar,3)]\nbar : []}", extractor.toString());
	}

	@Test
	public void testGetEdge() {
		final ListArray<String> vertices = new ListArray<>("foo", "bar");
		Edge edge;

		this.graph.addAll(vertices);
		edge = this.graph.getEdge("foo", "bar");
		assertNull(edge);

		this.graph.addEdge("foo", "bar", 1);
		edge = this.graph.getEdge("foo", "bar");

		assertNotNull(edge);
		assertEquals("bar", edge.getAdjacentVertex());
		assertEquals(1, edge.getWeight());
	}

	@Test
	public void testGetEdgeList() {
		final ListArray<String> vertices = new ListArray<>("foo", "bar");
		EdgeList edgeList;

		this.graph.addAll(vertices);
		edgeList = this.graph.getEdgeList("foo");
		assertNotNull(edgeList);
		assertEquals(0, edgeList.size);

		this.graph.addEdge("foo", "bar", 1);

		assertEquals(1, edgeList.size);
	}

	/**
	 * Returns the hash code of the graph.
	 *
	 * @return the hash code of the graph
	 */
	@Test
	public void testHashCode() {
		assertEquals(0, this.graph.hashCode());

		this.graph.add("foo");
		assertEquals(101572, this.graph.hashCode());

		this.graph.add("bar");
		assertEquals(157079, this.graph.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.graph.isEmpty());

		this.graph.add("foo");
		assertFalse(this.graph.isEmpty());
	}

	@Test
	public void testIterator() {
		Itemizer<MapEntry<String, EdgeList>> itemizer = this.graph.iterator();

		// Test empty collector
		assertEquals(-1, itemizer.getIndex());
		assertEquals(0, itemizer.getSize());
		assertFalse(itemizer.hasNext());
		assertTrue(itemizer == itemizer.iterator());

		try {
			itemizer.next();
			fail("Expected java.util.NoSuchElementException was not thrown");
		} catch (final NoSuchElementException e) {
		}

		try {
			itemizer.remove();
			fail("Expected java.lang.NullPointerException was not thrown");
		} catch (final NullPointerException e) {
		}

		this.graph.add("foo");
		this.graph.add("bar");
		itemizer = this.graph.iterator();

		assertEquals(-1, itemizer.getIndex());
		assertEquals(2, itemizer.getSize());
		assertTrue(itemizer.hasNext());

		MapEntry<String, EdgeList> vertexEntry = itemizer.next();
		assertEquals("foo", vertexEntry.getKey());
		assertEquals(0, vertexEntry.getValue().size);
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		vertexEntry = itemizer.next();
		assertEquals("bar", vertexEntry.getKey());
		assertEquals(0, vertexEntry.getValue().size);
		assertEquals(1, itemizer.getIndex());
		assertFalse(itemizer.hasNext());

		itemizer.reset();
		assertEquals(-1, itemizer.getIndex());
		assertTrue(itemizer.hasNext());
		itemizer.next();
		assertTrue(itemizer.hasNext());
		itemizer.next();
		assertFalse(itemizer.hasNext());
	}

	@Test
	public void testRemoveEdge() {
		final ListArray<String> vertices = new ListArray<>("foo", "bar");
		Edge edge;

		this.graph.addAll(vertices, 1);
		edge = this.graph.getEdge("foo", "bar");
		assertNotNull(edge);

		this.graph.removeEdge("foo", "bar");
		edge = this.graph.getEdge("foo", "bar");

		assertNull(edge);
	}

	@Test
	public void testToString() {
		assertEquals("{}", this.graph.toString());

		this.graph.add("foo");

		assertEquals("{foo : []}", this.graph.toString());

		this.graph.add("bar");

		assertEquals("{foo : []\nbar : []}", this.graph.toString());

		this.graph.addEdge("foo", "bar", 3);

		assertEquals("{foo : [(bar,3)]\nbar : []}", this.graph.toString());
	}

} // End GraphDirectedTest
