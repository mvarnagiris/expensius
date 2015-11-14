package com.mvcoding.financius.database.table

class TagsTable : ModelTable("tags") {
    val title = column(this, "title", Column.Type.Text);
    val color = column(this, "color", Column.Type.Integer, "0");

    override fun modelColumns(): Array<Column> {
        return arrayOf(title, color)
    }
}