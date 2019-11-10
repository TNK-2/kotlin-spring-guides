package com.example.ktconsumingrest

class Value(
    var id: Long,
    var quote: String
) {
    override fun toString(): String {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}