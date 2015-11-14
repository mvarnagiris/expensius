package com.mvcoding.financius.database.table

abstract class Table(val name: String) {
    fun createScript(): String {
        return "create table $name (${columns().joinToString { it.createScript() }})"
    }

    abstract fun columns(): Array<Column>
}