package com.taller.productos.service;

import com.taller.productos.exception.ProductoInvalidoException;
import com.taller.productos.exception.ProductoNoEncontradoException;
import com.taller.productos.model.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();
    private final AtomicLong contador = new AtomicLong(0);

    public ProductoService() {
        productos.add(new Producto(contador.incrementAndGet(), "Laptop", 850.0, 10));
        productos.add(new Producto(contador.incrementAndGet(), "Mouse", 15.5, 50));
        productos.add(new Producto(contador.incrementAndGet(), "Teclado", 30.0, 25));
    }

    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    public Producto obtenerPorId(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductoNoEncontradoException(
                        "No existe un producto con id " + id));
    }

    public Producto crear(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ProductoInvalidoException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() <= 0) {
            throw new ProductoInvalidoException("El precio debe ser mayor que cero");
        }
        if (producto.getStock() < 0) {
            throw new ProductoInvalidoException("El stock no puede ser negativo");
        }
        producto.setId(contador.incrementAndGet());
        productos.add(producto);
        return producto;
    }

    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productos.remove(producto);
    }
}