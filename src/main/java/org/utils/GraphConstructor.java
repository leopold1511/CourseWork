package org.utils;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import org.core.datamodel.Product;
import org.core.datamodel.Stage;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphConstructor {
    private static final String UR_KEY = "УР";
    private static final String BRANCHING_KEY = "Разветвление производственной линии";
    private static final String END_KEY = "Прекращение разработки";

    private final Map<String, ArrayList<String>> mapOfSpecialStageKeys = new HashMap<>() {
        {
            put(UR_KEY, new ArrayList<>());
            put(BRANCHING_KEY, new ArrayList<>());
            put(END_KEY, new ArrayList<>());
        }
    };

    public void viewGraph(Product product) {
        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (Stage stage : product.getStages()) {
            graph.addVertex(stage.getId() + ":" + stage.getName());

            for (String nextStageId : stage.getNextStages()) {
                for (Stage nextStage : product.getStages()) {
                    if (nextStage.getId().equals(nextStageId)) {
                        graph.addVertex(nextStage.getId() + ":" + nextStage.getName());
                        try {
                            mapOfSpecialStageKeys.get(nextStage.getName()).add(nextStage.getId() + ":" + nextStage.getName());
                        } catch (Exception ignored) {
                        }
                        graph.addEdge(stage.getId() + ":" + stage.getName(), nextStage.getId() + ":" + nextStage.getName());
                        break;
                    }
                }
            }
        }

        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph) {
            @Override
            public String convertValueToString(Object cell) {
                if (cell instanceof mxCell) {
                    Object value = ((mxCell) cell).getValue();
                    if (((mxICell) cell).isVertex()) {
                        return value != null ? value.toString().split(":")[1] : "";
                    } else {
                        return "";
                    }
                }
                return super.convertValueToString(cell);
            }
        };


        Map<String, Object> style = graphAdapter.getStylesheet().getDefaultVertexStyle();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_FILLCOLOR, "#C3D9FF");
        style.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        style.put(mxConstants.STYLE_FONTSIZE, 10);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_SPACING_TOP, 30);
        style.put(mxConstants.STYLE_SPACING_LEFT, 30);
        style.put(mxConstants.STYLE_SPACING_RIGHT, 30);
        style.put(mxConstants.STYLE_SPACING_BOTTOM, 30);
        configureVertexStyles(graphAdapter);


        Map<String, Object> edgeStyle = graphAdapter.getStylesheet().getDefaultEdgeStyle();
        edgeStyle.put(mxConstants.STYLE_MOVABLE, 0);

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.setParallelEdgeSpacing(10);
        layout.setOrientation(SwingConstants.WEST);
        layout.execute(graphAdapter.getDefaultParent());

        JFrame frame = new JFrame();
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        graphComponent.getViewport().setOpaque(true);
        graphComponent.getViewport().setBackground(Color.WHITE);
        graphComponent.setAlignmentY(0.5f);
        graphComponent.setAlignmentX(0.5f);
        frame.getContentPane().add(graphComponent);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void configureVertexStyles(JGraphXAdapter<String, DefaultEdge> graphAdapter) {
        Object parent = graphAdapter.getDefaultParent();
        graphAdapter.getModel().beginUpdate();
        try {

            Map<String, Object> customUrStyle = new HashMap<>();
            customUrStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);
            customUrStyle.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
            customUrStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            customUrStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            customUrStyle.put(mxConstants.STYLE_RESIZABLE, 1);
            graphAdapter.getStylesheet().putCellStyle("customUrStyle", customUrStyle);


            for (String name : mapOfSpecialStageKeys.get(UR_KEY)) {
                graphAdapter.setCellStyle("customUrStyle", new Object[]{graphAdapter.getVertexToCellMap().get(name)});
                mxGeometry geometry = graphAdapter.getVertexToCellMap().get(name).getGeometry();
                geometry.setWidth(40);
                geometry.setHeight(40);
            }

            Map<String, Object> customBranchingStyle = new HashMap<>();
            customBranchingStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);
            customBranchingStyle.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
            customBranchingStyle.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
            customBranchingStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            customBranchingStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            graphAdapter.getStylesheet().putCellStyle("customBranchingStyle", customBranchingStyle);

            for (String name : mapOfSpecialStageKeys.get(BRANCHING_KEY)) {
                graphAdapter.setCellStyle("customBranchingStyle", new Object[]{graphAdapter.getVertexToCellMap().get(name)});
                mxGeometry geometry = graphAdapter.getVertexToCellMap().get(name).getGeometry();
                geometry.setHeight(40);
                geometry.setWidth(40);
            }
            Map<String, Object> customEndStyle = new HashMap<>();
            customEndStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
            customEndStyle.put(mxConstants.STYLE_FILLCOLOR, "#000000");
            customEndStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            customEndStyle.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
            customEndStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            customEndStyle.put(mxConstants.STYLE_RESIZABLE, 1);
            graphAdapter.getStylesheet().putCellStyle("customEndStyle", customEndStyle);

            for (String name : mapOfSpecialStageKeys.get(END_KEY)) {
                graphAdapter.setCellStyle("customEndStyle", new Object[]{graphAdapter.getVertexToCellMap().get(name)});
                mxGeometry geometry = graphAdapter.getVertexToCellMap().get(name).getGeometry();
                geometry.setHeight(40);
                geometry.setWidth(40);
            }

        } finally {
            graphAdapter.getModel().endUpdate();
        }
    }
}
