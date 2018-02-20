/*

Based on WithAutomaticLayout.

Original copyright & license:

Copyright 2008-2010 Gephi
Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.toolkit.demos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.generator.plugin.RandomGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;


public class ForceAtlasVisualisation {

    private String input_file;
    private Double gravity;
    private Double scale;
    private Integer duration_seconds;
    private Float fast_proportion;

    private String output_directory;

    public ForceAtlasVisualisation(String input_file, Double gravity, Double scale,
                                   Integer duration_seconds, Float fast_proportion,
                                   String output_directory) {
        this.input_file = input_file;
        this.gravity = gravity;
        this.scale = scale;
        this.duration_seconds = duration_seconds;
        this.fast_proportion = fast_proportion;
        this.output_directory = output_directory;

    }

    public void script() {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();


        //Import file
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        Container container;
        File file = new File(this.input_file);
        try {
            container = importController.importFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        String basename = file.getName();


        //Append container to graph structure
        importController.process(container, new DefaultProcessor(), workspace);

        //See if graph is well imported
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
        DirectedGraph graph = graphModel.getDirectedGraph();
        System.out.println("Nodes: " + graph.getNodeCount());
        System.out.println("Edges: " + graph.getEdgeCount());


        AutoLayout autoLayout = new AutoLayout(this.duration_seconds, TimeUnit.SECONDS);
        autoLayout.setGraphModel(graphModel);



        ForceAtlas2 fa2_fast = new ForceAtlas2Builder().buildLayout();
        fa2_fast.setScalingRatio(this.scale);
        fa2_fast.setGravity(this.gravity);
        fa2_fast.setBarnesHutOptimize(Boolean.TRUE);
        fa2_fast.setAdjustSizes(Boolean.FALSE);


        ForceAtlas2 fa2_adjustment = new ForceAtlas2Builder().buildLayout();
        fa2_adjustment.setScalingRatio(this.scale);
        fa2_adjustment.setGravity(this.gravity);
        fa2_adjustment.setBarnesHutOptimize(Boolean.FALSE);
        fa2_adjustment.setAdjustSizes(Boolean.TRUE);

        autoLayout.addLayout(fa2_fast, this.fast_proportion);
        autoLayout.addLayout(fa2_adjustment, 1 - this.fast_proportion);
        autoLayout.execute();

        fa2_fast.endAlgo();
        fa2_adjustment.endAlgo();


        System.out.println("Layout finished");

        //Set 'show labels' option in Preview - and disable node size influence on text size
        PreviewModel previewModel = Lookup.getDefault().lookup(PreviewController.class).getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.FALSE);

        //Export
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);

        try {
            ec.exportFile(new File(this.output_directory, basename + ".pdf"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            ec.exportFile(new File(this.output_directory, basename + ".gexf"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
