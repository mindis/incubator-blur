/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.blur.shell;

import java.io.PrintWriter;
import java.util.List;

import org.apache.blur.thirdparty.thrift_0_9_0.TException;
import org.apache.blur.thrift.generated.Blur;
import org.apache.blur.thrift.generated.BlurException;
import org.apache.blur.thrift.generated.TableDescriptor;

public class TruncateTableCommand extends Command implements TableFirstArgCommand {
  @Override
  public void doit(PrintWriter out, Blur.Iface client, String[] args) throws CommandException, TException,
      BlurException {
    if (args.length != 2) {
      throw new CommandException("Invalid args: " + help());
    }
    String tablename = args[1];
    List<String> tableList = client.tableList();
    if (!tableList.contains(tablename)) {
      out.println("Table does not exist.");
      return;
    }
    TableDescriptor tableDescriptor = client.describe(tablename);
    if (tableDescriptor.isEnabled()) {
      out.println("Disabling table.");
      out.flush();
      client.disableTable(tablename);
    }
    out.println("Removing table.");
    out.flush();
    client.removeTable(tablename, true);
    out.println("Creating table.");
    out.flush();
    client.createTable(tableDescriptor);
  }

  @Override
  public String description() {
    return "Truncate the named table.";
  }

  @Override
  public String usage() {
    return "<tablename>";
  }

  @Override
  public String name() {
    return "truncate";
  }
}
