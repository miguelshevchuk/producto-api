package com.hackerrank.productoapi.domain.command;

import java.util.List;

public record CompareProductosCommand(
        List<Integer> ids,
        List<String> especificaciones
) {

}
