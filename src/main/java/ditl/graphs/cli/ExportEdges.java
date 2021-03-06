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

import static ditl.graphs.cli.ExternalFormat.CRAWDAD;
import static ditl.graphs.cli.ExternalFormat.ONE;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import ditl.cli.App;
import ditl.cli.ExportApp;
import ditl.graphs.CRAWDADEdges;
import ditl.graphs.EdgeTrace;
import ditl.graphs.ONEContacts;

@App.Cli(pkg = "graphs", cmd = "export-edges", alias = "xe")
public class ExportEdges extends ExportApp {

    private final GraphOptions.CliParser graph_options = new GraphOptions.CliParser(GraphOptions.EDGES);
    private final ExternalFormat.CLIParser ext_fmt_parser = new ExternalFormat.CLIParser(CRAWDAD, ONE);
    private ExternalFormat ext_fmt;
    private Long dtps;

    @Override
    protected void initOptions() {
        super.initOptions();
        graph_options.setOptions(options);
        ext_fmt_parser.setOptions(options);
        options.addOption(null, destTimeUnitOption, true, "time unit of destination trace [s, ms, us, ns] (default: s)");
    }

    @Override
    protected void parseArgs(CommandLine cli, String[] args)
            throws ParseException, ArrayIndexOutOfBoundsException, HelpException {
        super.parseArgs(cli, args);
        graph_options.parse(cli);
        ext_fmt = ext_fmt_parser.parse(cli);
        dtps = getTicsPerSecond(cli.getOptionValue(destTimeUnitOption, "s"));
        if (dtps == null)
            throw new HelpException();
    }

    @Override
    protected void run() throws IOException {
        final EdgeTrace edges = _store.getTrace(graph_options.get(GraphOptions.EDGES));
        final long otps = edges.ticsPerSecond();
        final double timeMul = getTimeMul(otps, dtps);
        switch (ext_fmt) {
            case CRAWDAD:
                CRAWDADEdges.toCRAWDAD(edges, _out, timeMul);
                break;
            case ONE:
                ONEContacts.toONE(edges, _out, timeMul);
                break;
        }
    }
}
