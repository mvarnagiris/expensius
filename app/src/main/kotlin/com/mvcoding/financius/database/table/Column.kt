package com.mvcoding.financius.database.table

internal fun column(table: Table, name: String, type: Column.Type, defaultValue: String = ""): Column {
    return Column("${table.name}_$name", type, defaultValue)
}

data class Column(
        val name: String,
        private val type: Column.Type,
        private val defaultValue: String = "") {

    fun createScript() = "$name $type ${if (defaultValue.isBlank()) "" else "default $defaultValue"}"

    enum class Type(val dataType: String) {
        Text("text"),
        TextPrimaryKey("text primary key"),
        Integer("integer"),
        Real("real"),
        Boolean("boolean"),
        DateTime("datetime");

        override fun toString(): String {
            return dataType
        }
    }
}