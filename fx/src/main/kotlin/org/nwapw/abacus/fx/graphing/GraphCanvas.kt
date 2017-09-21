package org.nwapw.abacus.fx.graphing

import javafx.beans.value.ChangeListener
import javafx.scene.canvas.Canvas

/**
 * A canvas that renders a graph.
 *
 * The GraphCanvas uses the provided [Graph] instance in order to draw the outputs on itself.
 * @param graph the graph used to render.
 */
class GraphCanvas(graph: Graph): Canvas() {

    /**
     * The graph that is currently being used to generate inputs / outputs.
     * The redraw is triggered if this graph is reset.
     */
    var graph: Graph = graph
        set(value) {
            field = value
            redraw()
        }

    init {
        val redrawListener = ChangeListener<Number> { _, _, _ -> redraw() }
        widthProperty().addListener(redrawListener)
        heightProperty().addListener(redrawListener)
        redraw()
    }

    /**
     * Redraws the graph onto the canvas.
     */
    fun redraw() {
        val graphicsContext = graphicsContext2D
        val outputs = graph.generateOutputs(graph.generateInputs())
    }

}