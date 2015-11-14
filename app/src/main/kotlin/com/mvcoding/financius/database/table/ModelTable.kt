package com.mvcoding.financius.database.table

abstract class ModelTable(name: String) : Table(name) {
    val id = column(this, "id", Column.Type.TextPrimaryKey);
    val modelState = column(this, "modelState", Column.Type.Text, ModelState.None.name);

    override fun columns(): Array<Column> {
        return arrayOf(id, modelState).plus(modelColumns())
    }

    abstract fun modelColumns(): Array<Column>

    enum class ModelState {
        None, Deleted
    }
}