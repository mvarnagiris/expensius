/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.provider.database

import com.mvcoding.expensius.provider.database.OrderDirection.ASC
import com.mvcoding.expensius.provider.database.OrderDirection.DESC
import com.mvcoding.expensius.provider.database.table.Table
import com.mvcoding.expensius.provider.database.table.ValueColumn
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryRequestTest {
    val tableFirst = TableFirst()
    val tableSecond = TableSecond()

    @Test
    fun select() {
        val sql = select(tableFirst).from(tableFirst).sql()

        assertEquals("SELECT tableFirst_id, tableFirst_value FROM tableFirst", sql)
    }

    @Test
    fun selectWhere() {
        val sql = select(tableFirst)
                .from(tableFirst)
                .where("${tableFirst.id.name}=1")
                .or("${tableFirst.id.name}=2")
                .and("${tableFirst.value.name}=?")
                .sql()

        assertEquals("SELECT tableFirst_id, tableFirst_value FROM tableFirst WHERE tableFirst_id=1 OR tableFirst_id=2 AND tableFirst_value=?",
                     sql)
    }

    @Test
    fun selectWhereGroupBy() {
        val sql = select(tableFirst)
                .from(tableFirst)
                .where("${tableFirst.id.name}=1")
                .or("${tableFirst.id.name}=2")
                .and("${tableFirst.value.name}=?")
                .groupBy(tableFirst.id, tableSecond.id)
                .sql()

        assertEquals("SELECT tableFirst_id, tableFirst_value FROM tableFirst WHERE tableFirst_id=1 OR tableFirst_id=2 AND tableFirst_value=? GROUP BY tableFirst_id, tableSecond_id",
                     sql)
    }

    @Test
    fun selectWhereGroupByOrderBy() {
        val sql = select(tableFirst)
                .from(tableFirst)
                .where("${tableFirst.id.name}=1")
                .or("${tableFirst.id.name}=2")
                .and("${tableFirst.value.name}=?")
                .groupBy(tableFirst.id, tableSecond.id)
                .orderBy(Order(tableFirst.id, ASC), Order(tableSecond.id, DESC))
                .sql()

        assertEquals("SELECT tableFirst_id, tableFirst_value FROM tableFirst WHERE tableFirst_id=1 OR tableFirst_id=2 AND tableFirst_value=? GROUP BY tableFirst_id, tableSecond_id ORDER BY tableFirst_id ASC, tableSecond_id DESC",
                     sql)
    }

    @Test
    fun leftJoin() {
        val sql = select(tableFirst, tableSecond)
                .from(tableFirst)
                .leftJoin(tableSecond, "${tableFirst.id.name}=${tableSecond.id.name}")
                .sql()

        assertEquals("SELECT tableFirst_id, tableFirst_value, tableSecond_id, tableSecond_value FROM tableFirst LEFT JOIN tableSecond ON tableFirst_id=tableSecond_id",
                     sql)
    }

    @Test
    fun leftJoinWhere() {
        val sql = select(tableFirst, tableSecond)
                .from(tableFirst)
                .leftJoin(tableSecond, "${tableFirst.id.name}=${tableSecond.id.name}")
                .where("${tableFirst.id.name}=1")
                .or("${tableFirst.id.name}=2")
                .and("${tableFirst.value.name}=?")
                .sql()

        assertEquals("SELECT tableFirst_id, tableFirst_value, tableSecond_id, tableSecond_value FROM tableFirst LEFT JOIN tableSecond ON tableFirst_id=tableSecond_id WHERE tableFirst_id=1 OR tableFirst_id=2 AND tableFirst_value=?",
                     sql)
    }

    @Test
    fun containsAllPassedValues() {
        val sql = select(tableFirst, tableSecond)
                .from(tableFirst)
                .leftJoin(tableSecond, "${tableFirst.id.name}=${tableSecond.id.name}")
                .where("${tableFirst.id.name}=1")
                .or("${tableFirst.id.name}=2")
                .and("${tableFirst.value.name}=?", "3", "4")

        assertArrayEquals(arrayOf(tableFirst, tableSecond), sql.tables)
        assertArrayEquals(arrayOf(tableFirst.id, tableFirst.value, tableSecond.id, tableSecond.value), sql.columns)
        assertArrayEquals(arrayOf("3", "4"), sql.arguments)
    }

    class TableFirst : Table("tableFirst") {
        val id = ValueColumn(this, "id", ValueColumn.Type.TEXT_PRIMARY_KEY);
        val value = ValueColumn(this, "value", ValueColumn.Type.TEXT);

        override fun idColumns() = arrayOf(id)
        override fun columns() = arrayOf(id, value)
    }

    class TableSecond : Table("tableSecond") {
        val id = ValueColumn(this, "id", ValueColumn.Type.TEXT_PRIMARY_KEY);
        val value = ValueColumn(this, "value", ValueColumn.Type.TEXT);

        override fun idColumns() = arrayOf(id)
        override fun columns() = arrayOf(id, value)
    }
}