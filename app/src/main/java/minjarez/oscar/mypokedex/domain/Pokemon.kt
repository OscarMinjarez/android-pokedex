package minjarez.oscar.mypokedex.domain

data class Pokemon(
    var documentId: String = "",
    var name: String = "",
    var number: Long = 0L,
    var imageUrl: String = ""
)