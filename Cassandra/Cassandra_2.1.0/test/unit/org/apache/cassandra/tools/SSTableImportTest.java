/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.cassandra.tools;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import static org.apache.cassandra.io.sstable.SSTableUtils.tempSSTableFile;
import static org.apache.cassandra.utils.ByteBufferUtil.hexToBytes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import org.apache.cassandra.SchemaLoader;
import org.apache.cassandra.Util;
import org.apache.cassandra.cql3.QueryProcessor;
import org.apache.cassandra.cql3.UntypedResultSet;
import org.apache.cassandra.cql3.UntypedResultSet.Row;
import org.apache.cassandra.db.*;
import org.apache.cassandra.db.columniterator.OnDiskAtomIterator;
import org.apache.cassandra.db.filter.QueryFilter;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.sstable.SSTableReader;

public class SSTableImportTest extends SchemaLoader
{
    @Test
    public void testImportSimpleCf() throws IOException, URISyntaxException
    {
        // Import JSON to temp SSTable file
        String jsonUrl = resourcePath("SimpleCF.json");
        File tempSS = tempSSTableFile("Keyspace1", "Standard1");
        new SSTableImport(true).importJson(jsonUrl, "Keyspace1", "Standard1", tempSS.getPath());

        // Verify results
        SSTableReader reader = SSTableReader.open(Descriptor.fromFilename(tempSS.getPath()));
        QueryFilter qf = QueryFilter.getIdentityFilter(Util.dk("rowA"), "Standard1", System.currentTimeMillis());
        OnDiskAtomIterator iter = qf.getSSTableColumnIterator(reader);
        ColumnFamily cf = cloneForAdditions(iter);
        while (iter.hasNext()) cf.addAtom(iter.next());
        assert cf.getColumn(Util.cellname("colAA")).value().equals(hexToBytes("76616c4141"));
        assert !(cf.getColumn(Util.cellname("colAA")) instanceof BufferDeletedCell);
        Cell expCol = cf.getColumn(Util.cellname("colAC"));
        assert expCol.value().equals(hexToBytes("76616c4143"));
        assert expCol instanceof ExpiringCell;
        assert ((ExpiringCell)expCol).getTimeToLive() == 42 && expCol.getLocalDeletionTime() == 2000000000;
    }

    private ColumnFamily cloneForAdditions(OnDiskAtomIterator iter)
    {
        return iter.getColumnFamily().cloneMeShallow(ArrayBackedSortedColumns.factory, false);
    }

    private String resourcePath(String name) throws URISyntaxException
    {
        // Naive resource.getPath fails on Windows in many cases, for example if there are spaces in the path
        // which get encoded as %20 which Windows doesn't like. The trick is to create a URI first, which satisfies all platforms.
        return new URI(getClass().getClassLoader().getResource(name).toString()).getPath();
    }

    @Test
    public void testImportUnsortedMode() throws IOException, URISyntaxException
    {
        String jsonUrl = resourcePath("UnsortedCF.json");
        File tempSS = tempSSTableFile("Keyspace1", "Standard1");

        new SSTableImport().importJson(jsonUrl, "Keyspace1", "Standard1", tempSS.getPath());

        SSTableReader reader = SSTableReader.open(Descriptor.fromFilename(tempSS.getPath()));
        QueryFilter qf = QueryFilter.getIdentityFilter(Util.dk("rowA"), "Standard1", System.currentTimeMillis());
        OnDiskAtomIterator iter = qf.getSSTableColumnIterator(reader);
        ColumnFamily cf = cloneForAdditions(iter);
        while (iter.hasNext())
            cf.addAtom(iter.next());
        assert cf.getColumn(Util.cellname("colAA")).value().equals(hexToBytes("76616c4141"));
        assert !(cf.getColumn(Util.cellname("colAA")) instanceof BufferDeletedCell);
        Cell expCol = cf.getColumn(Util.cellname("colAC"));
        assert expCol.value().equals(hexToBytes("76616c4143"));
        assert expCol instanceof ExpiringCell;
        assert ((ExpiringCell) expCol).getTimeToLive() == 42 && expCol.getLocalDeletionTime() == 2000000000;
    }

    @Test
    public void testImportWithDeletionInfoMetadata() throws IOException, URISyntaxException
    {
        // Import JSON to temp SSTable file
        String jsonUrl = resourcePath("SimpleCFWithDeletionInfo.json");
        File tempSS = tempSSTableFile("Keyspace1", "Standard1");
        new SSTableImport(true).importJson(jsonUrl, "Keyspace1", "Standard1", tempSS.getPath());

        // Verify results
        SSTableReader reader = SSTableReader.open(Descriptor.fromFilename(tempSS.getPath()));
        QueryFilter qf = QueryFilter.getIdentityFilter(Util.dk("rowA"), "Standard1", System.currentTimeMillis());
        OnDiskAtomIterator iter = qf.getSSTableColumnIterator(reader);
        ColumnFamily cf = cloneForAdditions(iter);
        assertEquals(cf.deletionInfo(), new DeletionInfo(0, 0));
        while (iter.hasNext())
            cf.addAtom(iter.next());
        assert cf.getColumn(Util.cellname("colAA")).value().equals(hexToBytes("76616c4141"));
        assert !(cf.getColumn(Util.cellname("colAA")) instanceof BufferDeletedCell);
        Cell expCol = cf.getColumn(Util.cellname("colAC"));
        assert expCol.value().equals(hexToBytes("76616c4143"));
        assert expCol instanceof ExpiringCell;
        assert ((ExpiringCell) expCol).getTimeToLive() == 42 && expCol.getLocalDeletionTime() == 2000000000;
    }

    @Test
    public void testImportCounterCf() throws IOException, URISyntaxException
    {
        // Import JSON to temp SSTable file
        String jsonUrl = resourcePath("CounterCF.json");
        File tempSS = tempSSTableFile("Keyspace1", "Counter1");
        new SSTableImport(true).importJson(jsonUrl, "Keyspace1", "Counter1", tempSS.getPath());

        // Verify results
        SSTableReader reader = SSTableReader.open(Descriptor.fromFilename(tempSS.getPath()));
        QueryFilter qf = QueryFilter.getIdentityFilter(Util.dk("rowA"), "Counter1", System.currentTimeMillis());
        OnDiskAtomIterator iter = qf.getSSTableColumnIterator(reader);
        ColumnFamily cf = cloneForAdditions(iter);
        while (iter.hasNext()) cf.addAtom(iter.next());
        Cell c = cf.getColumn(Util.cellname("colAA"));
        assert c instanceof CounterCell : c;
        assert ((CounterCell) c).total() == 42;
    }
    
    @Test
    /* 
     *  The schema is 
     *      CREATE TABLE cql_keyspace.table1 (k int PRIMARY KEY, v1 text, v2 int)
     * */
    public void shouldImportCqlTable() throws IOException, URISyntaxException
    {
        String cql_keyspace = "cql_keyspace";
        String cql_table = "table1";
        String jsonUrl = resourcePath("CQLTable.json");
        File tempSS = tempSSTableFile(cql_keyspace, cql_table);
        new SSTableImport(true).importJson(jsonUrl, cql_keyspace, cql_table, tempSS.getPath());
        SSTableReader reader = SSTableReader.open(Descriptor.fromFilename(tempSS.getPath()));
        Keyspace.open(cql_keyspace).getColumnFamilyStore(cql_table).addSSTable(reader);
        
        UntypedResultSet result = QueryProcessor.executeOnceInternal(String.format("SELECT * FROM %s.%s", cql_keyspace, cql_table));
        assertThat(result.size(), is(2));
        assertThat(result, hasItem(withElements(1, "NY", 1980)));
        assertThat(result, hasItem(withElements(2, "CA", 2014)));
    }

    @Test(expected=AssertionError.class)
    public void shouldRejectEmptyCellNamesForNonCqlTables() throws IOException, URISyntaxException
    {
        String jsonUrl = resourcePath("CQLTable.json");
        File tempSS = tempSSTableFile("Keyspace1", "Counter1");
        new SSTableImport(true).importJson(jsonUrl, "Keyspace1", "Counter1", tempSS.getPath());
    }
    
    private static Matcher<UntypedResultSet.Row> withElements(final int key, final String v1, final int v2) {
        return new TypeSafeMatcher<UntypedResultSet.Row>()
        {
            @Override
            public boolean matchesSafely(Row input)
            {
                if (!input.has("k") || !input.has("v1") || !input.has("v2"))
                    return false;
                return input.getInt("k") == key
                        && input.getString("v1").equals(v1)
                        && input.getInt("v2") == v2;
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText(String.format("a row containing: %s, %s, %s", key, v1, v2));
            }
        };
        
    }
}
