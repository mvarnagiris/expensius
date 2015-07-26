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

import org.junit.Test;

import static org.assertj.core.api.Assertions.fail;

public class DatabaseQueryTest extends BaseTest {
    private DatabaseQuery databaseQuery;

    @Override public void setUp() {
        super.setUp();
        databaseQuery = new DatabaseQuery();
    }

    @Test public void getSql_useAstrixForColumns_whenColumnsAreEmpty() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getSql_useProvidedColumns_whenColumnsAreNotEmpty() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getSql_useProvidedColumns_whenSelectIsCalledMoreThanOnce() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getSql_useProvidedTable() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getSql_doNotHaveWhereClause_whenWhereClauseIsNotProvided() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getSql_useProvidedWhereClause_whenWhereClauseIsProvided() throws Exception {
        fail("Not implemented.");
    }

    @Test public void from_doNotThrowIllegalStateException_whenTableIsNotAlreadySet() throws Exception {
        fail("Not implemented.");
    }

    @Test(expected = IllegalStateException.class) public void from_throwIllegalStateException_whenTableIsAlreadySet() throws Exception {
        fail("Not implemented.");
    }

    @Test public void where_doNotThrowIllegalStateException_whenTableIsNotAlreadySet() throws Exception {
        fail("Not implemented.");
    }

    @Test(expected = IllegalStateException.class) public void where_throwIllegalStateException_whenWhereIsAlreadySet() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getArgs_returnEmptyArray_whenNoArgsWereProvided() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getArgs_returnProvidedArgs_whenArgsWereProvided() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getTables_returnProvidedTable_whenTableWasProvided() throws Exception {
        fail("Not implemented.");
    }

    @Test(expected = IllegalStateException.class)
    public void getTables_throwIllegalStateException_whenTableWasNotProvided() throws Exception {
        fail("Not implemented.");
    }
}