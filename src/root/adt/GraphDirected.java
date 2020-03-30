/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.adt;

import java.util.List;

import root.annotation.NeedsTesting;
import root.annotation.Todo;
import root.annotation.Unfinished;
import root.lang.Extractable;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 * This class models a directed graph. There are no duplicate vertices allowed in this graph. At most one edge per vertex pair is allowed in this
 * graph. Self-loops, which is an edge from a vertex to itself, are permitted.
 * <p>
 * Because directed graphs are typically sparse, this directed graph uses an adjacency map to keep track of the edges defined between vertices.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@NeedsTesting
@Unfinished
@Todo({ "Once this is finished, I should probably make it generic so that it works with any class as a vertex" })
public final class GraphDirected implements Extractable {

	public static final class Edge implements Extractable {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		final String adjacentVertex;
		int weight;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private Edge(final String adjacentVertex, final int weight) {
			this.adjacentVertex = adjacentVertex;
			this.weight = weight;
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final boolean equals(final Object obj) {
			if (Root.equalToClass(obj, Edge.class)) {
				final Edge edge = (Edge) obj;

				return this.adjacentVertex.equals(edge.adjacentVertex) && this.weight == edge.weight;
			}

			return false;
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append('(');
			extractor.append(this.adjacentVertex);
			extractor.append(',');
			extractor.append(this.weight);
			extractor.append(')');
		}

		public final String getAdjacentVertex() {
			return this.adjacentVertex;
		}

		public final int getWeight() {
			return this.weight;
		}

		@Override
		public final String toString() {
			final StringExtractor extractor = new StringExtractor();
			this.extract(extractor);
			return extractor.toString();
		}

	} // End Edge

	/**
	 * Simple list of edges.
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	public static final class EdgeList implements Extractable {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		int size;
		Edge[] edges;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private EdgeList() {
			this.edges = new Edge[2];
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		public final boolean contains(final String endVertex) {
			for (int i = 0; i < this.size; i++) {
				if (this.edges[i].adjacentVertex.equals(endVertex)) {
					return true;
				}
			}

			return false;
		}

		@Override
		public final boolean equals(final Object obj) {
			if (Root.equalToClass(obj, EdgeList.class)) {
				final EdgeList edgeList = (EdgeList) obj;

				if (this.size == edgeList.size) {
					for (int i = 0; i < this.size; i++) {
						if (!this.edges[i].equals(edgeList.edges[i])) {
							return false;
						}
					}

					return true;
				}
			}

			return false;
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append('[');

			if (this.size > 0) {
				extractor.append(this.edges[0]);

				for (int i = 1; i < this.size; i++) {
					extractor.addSeparator().append(this.edges[i]);
				}
			}

			extractor.append(']');
		}

		public final Edge get(final int index) {
			return this.edges[index];
		}

		@Override
		public final String toString() {
			final StringExtractor extractor = new StringExtractor();
			this.extract(extractor);
			return extractor.toString();
		}

		private void add(final Edge edge) {
			if (this.size == this.edges.length) {
				final Edge[] edges = new Edge[this.size << 1];
				System.arraycopy(this.edges, 0, edges, 0, this.size);
				this.edges = edges;
			}

			this.edges[this.size++] = edge;
		}

		private Edge get(final String endVertex) {
			Edge edge;

			for (int i = 0; i < this.size; i++) {
				edge = this.edges[i];

				if (edge.adjacentVertex.equals(endVertex)) {
					return edge;
				}
			}

			return null;
		}

		private boolean remove(final String endVertex) {
			for (int i = 0; i < this.size; i++) {
				if (this.edges[i].adjacentVertex.equals(endVertex)) {
					this.size--;

					if (i < this.size) {
						System.arraycopy(this.edges, i + 1, this.edges, i, this.size - i);
					}

					this.edges[this.size] = null;

					return true;
				}
			}

			return false;
		}

	} // End EdgeList

	public static void main(final String[] args) {
		final GraphDirected navigationGraph = new GraphDirected();

		// Add all prequal.purchase.workflow.screens
		final ListArray<String> prequalPurchaseWorkflow = new ListArray<>("borrowerinfo", "propertyinfo", "pricing", "pricingproduct", "login",
				"addressinfo", "legalconsent", "ssninfo", "creditpullesignature", "creditpullesignatureconfirmation", "creditreportpayment",
				"creditreportpaymentconfirmation", "assets", "expensessummary", "income", "appproduct", "hmda", "declaration", "reo",
				"employmentinfo", "employmentsummary", "personaldetails", "addresshistory", "subjectpropertydetails", "subjectpropertyclosing",
				"subjectpropertytransaction", "dashboard");
		navigationGraph.addAll(prequalPurchaseWorkflow, 1);

		// Add all application.purchase.workflow.screens
		navigationGraph.addEdge("creditreportpaymentconfirmation", "appproduct", 2);
		navigationGraph.addEdge("declaration", "assets", 2);
		navigationGraph.addEdge("income", "reo", 2);

		// Add all application.refinance.workflow.screens
		navigationGraph.addEdge("creditreportpaymentconfirmation", "hmda", 3);
		navigationGraph.addEdge("subjectpropertyclosing", "dashboard", 3);

		// System.out.println(navigationGraph);
		final EdgeList pricingEdgeList = navigationGraph.getEdgeList("pricing");
		System.out.println(pricingEdgeList);
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final MapHashed<String, EdgeList> adjacencyMap;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor.
	 */
	public GraphDirected() {
		this.adjacencyMap = new MapHashed<>();
	}

	/**
	 * A constructor that accepts a predetermined number of distinct vertices to expect within the graph.
	 *
	 * @param numVertices
	 *            the number of distinct vertices to expect within the graph
	 */
	public GraphDirected(final int numVertices) {
		this.adjacencyMap = new MapHashed<>(numVertices);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Adds a vertex to the graph.
	 *
	 * @param vertex
	 *            the vertex to add to the graph
	 */
	public final void add(final String vertex) {
		if (!this.adjacencyMap.containsKey(vertex)) {
			this.adjacencyMap.put(vertex, new EdgeList());
		}
	}

	/**
	 * Adds all the vertices to the graph.
	 *
	 * @param vertices
	 *            the vertices to add to the graph
	 */
	public final void addAll(final Iterable<String> vertices) {
		for (final String vertex : vertices) {
			if (!this.adjacencyMap.containsKey(vertex)) {
				this.adjacencyMap.put(vertex, new EdgeList());
			}
		}
	}

	/**
	 * Adds all the vertices to the graph. Also, each vertex pair in the list [(0, 1), (1, 2), (2, 3), ...] has an edge added to the graph with the
	 * specified weight.
	 *
	 * @param vertices
	 *            the vertices to add to the graph
	 * @param weight
	 *            the weight of the edges
	 */
	public final void addAll(final List<String> vertices, final int weight) {
		if (vertices.size() > 1) {
			String beginVertex, endVertex;

			// Add first vertex to the graph, if not already present
			beginVertex = vertices.get(0);
			if (!this.adjacencyMap.containsKey(beginVertex)) {
				this.adjacencyMap.put(beginVertex, new EdgeList());
			}

			for (int beginIndex = 0, endIndex = 1; endIndex < vertices.size(); beginIndex++, endIndex++) {
				beginVertex = vertices.get(beginIndex);
				endVertex = vertices.get(endIndex);

				// Add endVertex to the graph, if not already present
				if (!this.adjacencyMap.containsKey(endVertex)) {
					this.adjacencyMap.put(endVertex, new EdgeList());
				}

				// Manage the edge list for the beginVertex
				final EdgeList edgeList = this.adjacencyMap.get(beginVertex);
				Edge edge = edgeList.get(endVertex);

				if (edge == null) {
					// Add new edge to the graph
					edge = new Edge(endVertex, weight);
					edgeList.add(edge);
				} else {
					// Update weight of existing edge
					edge.weight = weight;
				}
			}
		}
	}

	/**
	 * Adds an edge to the graph between the {@code beginVertex} and {@code endVertex} with the specified weight.
	 * <ul>
	 * <li>If {@code beginVertex} does not exist in the graph, it is added to the graph</li>
	 * <li>If {@code endVertex} does not exist in the graph, it is added to the graph</li>
	 * <li>If an edge between {@code (beginVertex, endVertex)} does not already exist, one is created using the specified weight</li>
	 * <li>However, if an edge between {@code (beginVertex, endVertex)} does exist, its weight is updated with the specified weight</li>
	 * </ul>
	 *
	 * @param beginVertex
	 *            the starting vertex of the edge
	 * @param endVertex
	 *            the ending vertex of the edge
	 * @param weight
	 *            the weight of the edge
	 * @return {@code true} if an edge was created between {@code (beginVertex, endVertex)}, {@code false} if an edge already existed
	 */
	public final boolean addEdge(final String beginVertex, final String endVertex, final int weight) {
		// Add beginVertex to the graph, if not already present
		if (!this.adjacencyMap.containsKey(beginVertex)) {
			this.adjacencyMap.put(beginVertex, new EdgeList());
		}

		// Add endVertex to the graph, if not already present
		if (!this.adjacencyMap.containsKey(endVertex)) {
			this.adjacencyMap.put(endVertex, new EdgeList());
		}

		// Manage the edge list for the beginVertex
		final EdgeList edgeList = this.adjacencyMap.get(beginVertex);
		Edge edge = edgeList.get(endVertex);

		if (edge == null) {
			// Add new edge to the graph
			edge = new Edge(endVertex, weight);
			edgeList.add(edge);

			return true;
		}

		// Update weight of existing edge
		edge.weight = weight;

		return false;
	}

	/**
	 * Removes all of the vertices and edges from the graph.
	 */
	public final void clear() {
		this.adjacencyMap.clear();
	}

	/**
	 * Returns {@code true} if this graph contains the edge {@code (beginVertex, endVertex)}.
	 *
	 * @param beginVertex
	 *            the starting vertex of the edge
	 * @param endVertex
	 *            the ending vertex of the edge
	 * @return {@code true} if this graph contains the edge {@code (beginVertex, endVertex)}
	 */
	public final boolean containsEdge(final String beginVertex, final String endVertex) {
		final EdgeList edges = this.adjacencyMap.get(beginVertex);

		return edges == null ? false : edges.contains(endVertex);
	}

	/**
	 * Returns {@code true} if this graph contains the specified vertex.
	 *
	 * @param vertex
	 *            the specified vertex
	 * @return {@code true} if this graph contains the specified vertex
	 */
	public final boolean containsVertex(final String vertex) {
		return this.adjacencyMap.containsKey(vertex);
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. The specified {@link Object} is equal to {@code this}
	 * object if:
	 * <ul>
	 * <li>The {@link Class} of the specified {@link Object} is equal to {@link GraphDirected}</li>
	 * <li>The {@code adjacencyMap} of the specified {@link GraphDirected} and {@code this} object are equal</li>
	 * </ul>
	 *
	 * @param obj
	 *            the specified {@link Object} to compare for equality to {@code this} object
	 * @return {@code true} if the specified {@link Object} is equal to {@code this} object, false otherwise
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (Root.equalToClass(obj, GraphDirected.class)) {
			return ((GraphDirected) obj).adjacencyMap.equals(this.adjacencyMap);
		}

		return false;
	}

	/**
	 * Extracts a {@link String} representation of the graph.
	 *
	 * @param extractor
	 *            the {@link StringExtractor} to populate
	 */
	@Override
	public final void extract(final StringExtractor extractor) {
		int i = 0;

		extractor.append('{');
		for (final MapEntry<String, EdgeList> mapEntry : this.adjacencyMap) {
			if (i++ > 0) {
				extractor.addSeparator('\n');
			}

			extractor.append(mapEntry.key).append(' ').append(':').append(' ').append(mapEntry.value);
		}
		extractor.append('}');
	}

	/**
	 * Returns the {@link Edge} {@code (beginVertex, endVertex)}.
	 *
	 * @param beginVertex
	 *            the starting vertex of the edge
	 * @param endVertex
	 *            the ending vertex of the edge
	 * @return the {@link Edge} {@code (beginVertex, endVertex)}
	 */
	public final Edge getEdge(final String beginVertex, final String endVertex) {
		final EdgeList edges = this.adjacencyMap.get(beginVertex);

		return edges == null ? null : edges.get(endVertex);
	}

	/**
	 * Returns the list of edges adjacent to the specified vertex.
	 *
	 * @param vertex
	 *            the specified vertex
	 * @return the list of edges adjacent to the specified vertex
	 */
	public final EdgeList getEdgeList(final String vertex) {
		return this.adjacencyMap.get(vertex);
	}

	/**
	 * Returns the number of vertices in the graph.
	 *
	 * @return the number of vertices in the graph
	 */
	public final int getSize() {
		return this.adjacencyMap.size;
	}

	/**
	 * Returns the hash code of the graph.
	 *
	 * @return the hash code of the graph
	 */
	@Override
	public final int hashCode() {
		return this.adjacencyMap.hashCode();
	}

	/**
	 * Returns {@code true} if this graph contains no vertices.
	 *
	 * @return {@code true} if this graph contains no vertices, {@code false} otherwise
	 */
	public final boolean isEmpty() {
		return this.adjacencyMap.isEmpty();
	}

	/**
	 * Returns an {@link Itemizer} for the graph.
	 *
	 * @return an {@link Itemizer} for the graph
	 */
	public final Itemizer<MapEntry<String, EdgeList>> iterator() {
		return this.adjacencyMap.iterator();
	}

	/**
	 * Removes an edge from the graph between the {@code beginVertex} and {@code endVertex}.
	 *
	 * @param beginVertex
	 *            the starting vertex of the edge
	 * @param endVertex
	 *            the ending vertex of the edge
	 * @return {@code true} if an edge was removed between {@code (beginVertex, endVertex)}, {@code false} if no edge existed between
	 *         {@code (beginVertex, endVertex)}
	 */
	public final boolean removeEdge(final String beginVertex, final String endVertex) {
		// Manage the edge list for the beginVertex
		final EdgeList edgeList = this.adjacencyMap.get(beginVertex);

		if (edgeList != null) {
			return edgeList.remove(endVertex);
		}

		return false;
	}

	/**
	 * Returns a {@link String} representation of the graph.
	 *
	 * @return a {@link String} representation of the graph
	 */
	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.adjacencyMap.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

} // End GraphDirected
