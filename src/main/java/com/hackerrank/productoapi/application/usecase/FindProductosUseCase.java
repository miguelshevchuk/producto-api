package com.hackerrank.productoapi.application.usecase;

import java.util.Arrays;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.domain.port.ProductoRepository;

@Service
@AllArgsConstructor
@Slf4j
public class FindProductosUseCase {

    private ProductoRepository productoRepository;

    public Iterable<Producto> findProductos(String ids){


        if(Objects.isNull(ids) || ids.trim().isBlank()){
            log.info("Buscando todos los productos");
            return this.productoRepository.findAll();
        }else{
            var idsList = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .toList();
            log.info("Buscando productos con ids: {}", idsList);
            return this.productoRepository.findByIds(idsList);
        }

    }

}
