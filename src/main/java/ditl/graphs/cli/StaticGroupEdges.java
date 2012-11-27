/*******************************************************************************
 * This file is part of DITL.                                                  *
 *                                                                             *
 * Copyright (C) 2011-2012 John Whitbeck <john@whitbeck.fr>                    *
 *                                                                             *
 * DITL is free software: you can redistribute it and/or modify                *
 * it under the terms of the GNU General Public License as published by        *
 * the Free Software Foundation, either version 3 of the License, or           *
 * (at your option) any later version.                                         *
 *                                                                             *
 * DITL is distributed in the hope that it will be useful,                     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of              *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               *
 * GNU General Public License for more details.                                *
 *                                                                             *
 * You should have received a copy of the GNU General Public License           *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.       *
 *******************************************************************************/
package ditl.graphs.cli;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import ditl.Store.LoadTraceException;
import ditl.Store.NoSuchTraceException;
import ditl.WritableStore.AlreadyExistsException;
import ditl.cli.Command;
import ditl.cli.ConvertApp;
import ditl.graphs.EdgeTrace;
import ditl.graphs.Group;
import ditl.graphs.GroupTrace;
import ditl.graphs.StaticGroupEdgeConverter;

@Command(pkg = "graphs", cmd = "group-edges", alias = "ge")
public class StaticGroupEdges extends ConvertApp {

    private final GraphOptions.CliParser graph_options = new GraphOptions.CliParser(GraphOptions.GROUPS, GraphOptions.EDGES);
    private String groupEdgesName;

    @Override
    protected void run() throws IOException, AlreadyExistsException, LoadTraceException, NoSuchTraceException {
        final EdgeTrace edges = (EdgeTrace) orig_store.getTrace(graph_options.get(GraphOptions.EDGES));
        final GroupTrace groups = (GroupTrace) orig_store.getTrace(graph_options.get(GraphOptions.GROUPS));
        final Set<Group> static_groups = groups.staticGroups();
        final EdgeTrace group_edges = (EdgeTrace) dest_store.newTrace(groupEdgesName, EdgeTrace.class, force);
        new StaticGroupEdgeConverter(group_edges, edges, static_groups).convert();
    }

    @Override
    protected void parseArgs(CommandLine cli, String[] args) throws ParseException, HelpException {
        super.parseArgs(cli, args);
        graph_options.parse(cli);
        groupEdgesName = args[1];
    }

    @Override
    protected String getUsageString() {
        return "[OPTIONS] STORE GROUP_EDGES_NAME";
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        graph_options.setOptions(options);
    }

}
