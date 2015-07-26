/*
 * Copyright (C) 2015 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.financius.data.database;

import com.mvcoding.financius.BaseTest;

import org.assertj.core.data.Index;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseQueryTest extends BaseTest {
    private static final String SQL_REGEX = "(SELECT )|( FROM )|( WHERE )";
    private DatabaseQuery databaseQuery;

    @Override public void setUp() {
        super.setUp();
        databaseQuery = new DatabaseQuery();
    }

    @Test public void getSql_useAstrixForColumns_whenColumnsAreEmpty() throws Exception {
        databaseQuery.from("table");

        final String sql = databaseQuery.getSql();

        final String[] parts = sql.split(SQL_REGEX);
        assertThat(parts[1]).isEqualTo("*");
    }

    @Test public void getSql_useProvidedColumns_whenColumnsAreNotEmpty() throws Exception {
        databaseQuery.select("column1", "column2").from("table");

        final String sql = databaseQuery.getSql();

        final String[] parts = sql.split(SQL_REGEX);
        assertThat(parts[1]).isEqualTo("column1,column2");
    }

    @Test public void getSql_useProvidedColumns_whenSelectIsCalledMoreThanOnce() throws Exception {
        databaseQuery.select("column1", "column2").select("column3").from("table");

        final String sql = databaseQuery.getSql();

        final String[] parts = sql.split(SQL_REGEX);
        assertThat(parts[1]).isEqualTo("column1,column2,column3");
    }

    @Test public void getSql_useProvidedTable() throws Exception {
        databaseQuery.from("table");

        final String sql = databaseQuery.getSql();

        final String[] parts = sql.split(SQL_REGEX);
        assertThat(parts[2]).isEqualTo("table");
    }

    @Test public void getSql_doNotHaveWhereClause_whenWhereClauseIsNotProvided() throws Exception {
        databaseQuery.from("table");

        final String sql = databaseQuery.getSql();

        assertThat(sql).doesNotContain("WHERE");
    }

    @Test public void getSql_useProvidedWhereClause_whenWhereClauseIsProvided() throws Exception {
        databaseQuery.from("table").where("column1=1");

        final String sql = databaseQuery.getSql();

        final String[] parts = sql.split(SQL_REGEX);
        assertThat(parts[3]).isEqualTo("column1=1");
    }

    @Test public void from_doNotThrowIllegalStateException_whenTableIsNotAlreadySet() throws Exception {
        databaseQuery.from("table");
    }

    @Test(expected = IllegalStateException.class) public void from_throwIllegalStateException_whenTableIsAlreadySet() throws Exception {
        databaseQuery.from("table");
        databaseQuery.from("table");
    }

    @Test public void where_doNotThrowIllegalStateException_whenTableIsNotAlreadySet() throws Exception {
        databaseQuery.from("table").where("column1=1");
    }

    @Test(expected = IllegalStateException.class) public void where_throwIllegalStateException_whenWhereIsAlreadySet() throws Exception {
        databaseQuery.from("table").where("column1=1");
        databaseQuery.from("table").where("column1=1");
    }

    @Test public void getArgs_returnEmptyArray_whenNoArgsWereProvided() throws Exception {
        databaseQuery.from("table");

        final String[] args = databaseQuery.getArgs();

        assertThat(args).isNotNull();
        assertThat(args).isEmpty();
    }

    @Test public void getArgs_returnProvidedArgs_whenArgsWereProvided() throws Exception {
        databaseQuery.from("table").where("column1=1", "1", "2", "3");

        final String[] args = databaseQuery.getArgs();

        assertThat(args).isNotNull();
        assertThat(args).hasSize(3);
        assertThat(args).contains("1", Index.atIndex(0));
        assertThat(args).contains("2", Index.atIndex(1));
        assertThat(args).contains("3", Index.atIndex(2));
    }

    @Test public void getTables_returnProvidedTables_whenTablesWereProvided() throws Exception {
        databaseQuery.from("table");

        final List<String> tables = databaseQuery.getTables();

        assertThat(tables).isNotNull();
        assertThat(tables).hasSize(1);
        assertThat(tables).contains("table");
    }

    @Test(expected = IllegalStateException.class)
    public void getTables_throwIllegalStateException_whenTablesWereNotProvided() throws Exception {
        databaseQuery.getTables();
    }
}