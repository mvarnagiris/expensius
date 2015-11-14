package com.mvcoding.financius.database

fun select(vararg columns: String): Query.Select = Query.Select(columns);

open class Query {
    class Select(private val columns: Array<out String>) {
        fun from(table: String): From = From(this, table)
    }

    class From(private val select: Select, private val table: String) : Query() {

    }
}