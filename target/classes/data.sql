INSERT INTO productos (id, nombre, url_imagen, descripcion, precio, calificacion) VALUES
  (1, 'Producto 1', 'https://okhosting.com/wp-content/uploads/2019/01/java-400x250.jpg', 'Producto de prueba 1', 5.5, 2.5),
  (2, 'Producto 2', 'https://okhosting.com/wp-content/uploads/2019/01/java-400x250.jpg', 'Producto de prueba 2', 3, 5),
  (3, 'Producto 3', 'https://okhosting.com/wp-content/uploads/2019/01/java-400x250.jpg', 'Producto de prueba 3', 7.1, 1.8);

INSERT INTO producto_especificaciones(producto_id, clave, valor) VALUES
    (1, 'color', 'rojo'),
    (1, 'marca', 'IOS'),
    (1, 'peso', '5'),
    (2, 'color', 'Verde'),
    (2, 'marca', 'Android'),
    (2, 'peso', '1'),
    (3, 'color', 'Azul'),
    (3, 'marca', 'Nokia'),
    (3, 'peso', '3'),
    (3, 'tamanio', '10');