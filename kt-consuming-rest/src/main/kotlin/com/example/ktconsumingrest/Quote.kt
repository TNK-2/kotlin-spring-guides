package com.example.ktconsumingrest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties
class Quote(
    var type: String,
    var value: Value
) {
    override fun toString(): String {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}