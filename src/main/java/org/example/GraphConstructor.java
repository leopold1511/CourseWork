package org.example;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import datamodel.Product;
import datamodel.Stage;
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
    Map<String, ArrayList<String>> mapOfSpecialStageKeys = new HashMap<>() {
        {
            put("УР", new ArrayList<>());
            put("Разветвление производственной линии", new ArrayList<>());
            put("Прекращение разработки", new ArrayList<>());
        }
    };
    public void viewGraph(Product product) {
        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

            for (Stage stage : product.getStages()) {
                graph.addVertex(stage.getStageId() + ":" + stage.getStageName());

                for (String nextStageId : stage.getNextStages()) {
                    for (Stage nextStage : product.getStages()) {
                        if (nextStage.getStageId().equals(nextStageId)) {
                            graph.addVertex(nextStage.getStageId() + ":" + nextStage.getStageName());
                            try {
                                mapOfSpecialStageKeys.get(nextStage.getStageName()).add(nextStage.getStageId() + ":" + nextStage.getStageName());
                            } catch (Exception ignored) {
                            }
                            graph.addEdge(stage.getStageId() + ":" + stage.getStageName(), nextStage.getStageId() + ":" + nextStage.getStageName());
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configureVertexStyles(JGraphXAdapter<String, DefaultEdge> graphAdapter) {
        Object parent = graphAdapter.getDefaultParent();
        graphAdapter.getModel().beginUpdate();
        try {

            Map<String, Object> style1 = new HashMap<>();
            style1.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);
            style1.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
            style1.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            style1.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            style1.put(mxConstants.STYLE_RESIZABLE, 1);
            graphAdapter.getStylesheet().putCellStyle("customStyle1", style1);


            for (String name : mapOfSpecialStageKeys.get("УР")) {
                System.out.println(1);
                graphAdapter.setCellStyle("customStyle1", new Object[]{graphAdapter.getVertexToCellMap().get(name)});
                mxGeometry geometry= graphAdapter.getVertexToCellMap().get(name).getGeometry();
                geometry.setWidth(40);
                geometry.setHeight(40);
            }

            Map<String, Object> style2 = new HashMap<>();
            style2.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);
            style2.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
            style2.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
            style2.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            style2.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            graphAdapter.getStylesheet().putCellStyle("customStyle2", style2);

            for (String name : mapOfSpecialStageKeys.get("Разветвление производственной линии")) {
                graphAdapter.setCellStyle("customStyle2", new Object[]{graphAdapter.getVertexToCellMap().get(name)});
                mxGeometry geometry= graphAdapter.getVertexToCellMap().get(name).getGeometry();
                geometry.setHeight(40);
                geometry.setWidth(40);
            }
            Map<String, Object> style3 = new HashMap<>();
            style3.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
            style3.put(mxConstants.STYLE_FILLCOLOR, "#000000");
            style3.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            style3.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
            style3.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            style3.put(mxConstants.STYLE_RESIZABLE, 1);
            graphAdapter.getStylesheet().putCellStyle("customStyle3", style3);

            for (String name : mapOfSpecialStageKeys.get("Прекращение разработки")) {
                graphAdapter.setCellStyle("customStyle3", new Object[]{graphAdapter.getVertexToCellMap().get(name)});
                mxGeometry geometry= graphAdapter.getVertexToCellMap().get(name).getGeometry();
                geometry.setHeight(40);
                geometry.setWidth(40);
            }



        } finally {
            graphAdapter.getModel().endUpdate();
        }
    }
}
