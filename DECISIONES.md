# DECISIONES TÉCNICAS Y DE ARQUITECTURA - CATÁLOGO EXPRESS

## 1. Arquitectura Utilizada
Se implementó una arquitectura basada en microservicios desacoplados utilizando Spring Boot 3.x:
* **Catalog-Service (Puerto 8081):** Encargado de agregar los datos del producto, consultar al servicio de inventario mediante REST y calcular el score dinámicamente.
* **Inventory-Service (Puerto 8082):** Administra de forma exclusiva el stock y disponibilidad[cite: 1].
* **Frontend (Angular 19 + Bootstrap):** Consume únicamente las APIs expuestas por el backend[cite: 1].

## 2. Normalización y Cálculo de Score
Para el cálculo del `score` se aplicó la fórmula oficial[cite: 1]:
`score = (rating * ln(stock + 1)) / max(price, 1)`

**Criterios de Normalización:**
* Si `rating` o `stock` son `null` o negativos, se asume `0`[cite: 1].
* Para evitar división entre cero en `price`, se aplica `max(price, 1)`, asegurando un valor mínimo de `1.0` en el denominador[cite: 1].
* El listado se retorna ordenado descendentemente priorizando productos con mejor equilibrio entre alta valoración, disponibilidad y precio accesible[cite: 1].

## 3. Estrategia de Despliegue en AWS
Para el entorno de producción en nube se optó por una arquitectura en AWS EC2[cite: 1]:
* **Base de Datos:** Instancia EC2 ejecutando SQL Server 2022 en un contenedor Docker[cite: 1].
* **Backend:** Instancia EC2 ejecutando los microservicios Spring Boot expuestos en sus respectivos puertos[cite: 1].
* **Frontend:** Instancia EC2 ejecutando Nginx como servidor web de producción para la app compilada en Angular[cite: 1].

## 4. Manejo de Errores y Fallbacks
Se implementaron DTOs para evitar exponer las entidades JPA[cite: 1]. En caso de que `inventory-service` no se encuentre disponible, `catalog-service` captura la excepción HTTP asignando un stock por defecto de `0` para no interrumpir el flujo del catálogo al cliente final[cite: 1].