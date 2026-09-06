-- =========================================================
-- ACOES Fleet Management - Development Seed Data
-- =========================================================
-- Este script inserta datos de prueba para:
-- 1. Garages (talleres)
-- 2. Vehicles (vehículos)
-- 3. Workshop Orders (órdenes de taller)
-- 4. Workshop Order Lines (líneas de órdenes de taller)
-- 5. Workshop Executions (ejecuciones reales)
-- 6. Execution Lines (líneas reales de ejecución)
--
-- Se ejecuta automáticamente al arrancar la aplicación
-- en entorno DEV (H2)
-- =========================================================


-- =========================================================
-- 1. GARAGES
-- =========================================================
-- Nota:
-- Address está embebido → se expande en columnas address_*
-- =========================================================

INSERT INTO garages (name,
                     address_street,
                     address_city,
                     address_province,
                     address_postal_code,
                     address_country,
                     contact_name,
                     phone,
                     email,
                     notes,
                     active)
VALUES
-- Taller interno principal
('Taller Central ACOES',
 'Calle Principal 123',
 'Madrid',
 'Madrid',
 '28001',
 'España',
 'Carlos Gómez',
 '600123456',
 'taller.central@acoes.org',
 'Taller interno principal de la ONG',
 true),

-- Taller externo colaborador
('Taller Mecánica Rápida',
 'Avenida del Motor 45',
 'Madrid',
 'Madrid',
 '28020',
 'España',
 'Laura Pérez',
 '600654321',
 'mecanica.rapida@example.com',
 'Taller externo para reparaciones rápidas',
 true);


-- =========================================================
-- 2. VEHICLES
-- =========================================================
-- IMPORTANTE:
-- - plate_number → único
-- - vin → único (17 caracteres, sin I,O,Q)
-- - vehicle_type → enum STRING (CAR, VAN, etc.)
-- - status → enum STRING (OPERATIONAL, IN_REPAIR, OUT_OF_SERVICE)
-- - current_garage_id → NULL si el vehículo está circulando
-- =========================================================

INSERT INTO vehicles (plate_number,
                      vin,
                      brand,
                      model,
                      color,
                      vehicle_type,
                      status,
                      current_mileage,
                      official_registration_date,
                      insurance_expiration_date,
                      current_garage_id,
                      notes,
                      active)
VALUES

-- Vehículo operativo (circulando)
('PAB1234',
 '1HGCM82633A123456',
 'Toyota',
 'Corolla',
 'Blanco',
 'CAR',
 'OPERATIONAL',
 125000,
 '2018-05-12',
 '2027-05-12',
 NULL, -- No está en taller
 'Vehículo en uso normal',
 true),

-- Vehículo en reparación (en taller 1)
('TCD5678',
 'WVWZZZ1JZXW000001',
 'Ford',
 'Transit',
 'Azul',
 'VAN',
 'IN_REPAIR',
 210000,
 '2016-09-20',
 '2027-09-20',
 1, -- Taller Central ACOES
 'En taller por revisión mecánica',
 true),

-- Vehículo fuera de servicio
('MXY9012',
 'JH4KA4650MC000002',
 'Nissan',
 'Navara',
 'Gris',
 'TRUCK',
 'OUT_OF_SERVICE',
 300000,
 '2014-03-10',
 '2026-03-10',
 NULL,
 'Pendiente de baja definitiva',
 true);

-- =========================================================
-- 3. WORKSHOP ORDERS
-- =========================================================
-- IMPORTANTE:
-- - order_number → único
-- - vehicle_id → relación con vehicles.id
-- - vehicle_plate_number → snapshot de la matrícula al abrir la orden
-- - status → enum STRING (OPEN, PENDING_PARTS, CLOSED, CANCELLED)
-- =========================================================

INSERT INTO workshop_orders (order_number,
                             vehicle_id,
                             vehicle_plate_number,
                             status,
                             opening_date,
                             closing_date,
                             active)
VALUES

-- Orden abierta para vehículo operativo
('WO-2025-12-000001',
 1,
 'PAB1234',
 'OPEN',
 '2025-12-01',
 NULL,
 true),

-- Orden pendiente de piezas para furgoneta
('WO-2025-12-000002',
 2,
 'TCD5678',
 'PENDING_PARTS',
 '2025-12-03',
 NULL,
 true),

-- Orden cerrada para furgoneta
('WO-2025-12-000003',
 2,
 'TCD5678',
 'CLOSED',
 '2025-12-10',
 '2025-12-12',
 true),

-- Orden cancelada para vehículo fuera de servicio
('WO-2025-12-000004',
 3,
 'MXY9012',
 'CANCELLED',
 '2025-12-15',
 NULL,
 true);

-- =========================================================
-- 4. WORKSHOP ORDER LINES
-- =========================================================
-- IMPORTANTE:
-- - workshop_order_id → relación con workshop_orders.id
-- - line_number → número de línea dentro de cada orden
-- - work_description → trabajo solicitado / motivo de entrada
-- - priority → enum STRING (LOW, MEDIUM, HIGH, URGENT)
-- - line_number es único dentro de cada orden
-- =========================================================

INSERT INTO workshop_order_lines (workshop_order_id,
                                  line_number,
                                  work_description,
                                  priority,
                                  active)
VALUES

-- Líneas para orden WO-2026-000001
(1,
 1,
 'Mantenimiento programado',
 'MEDIUM',
 true),

(1,
 2,
 'Revisión general de niveles y filtros',
 'LOW',
 true),

-- Líneas para orden WO-2026-000002
(2,
 1,
 'Ruido en motor durante aceleración',
 'HIGH',
 true),

(2,
 2,
 'Revisión de sistema de refrigeración',
 'MEDIUM',
 true),

-- Líneas para orden WO-2026-000003
(3,
 1,
 'Cambio preventivo de aceite y filtro',
 'MEDIUM',
 true),

(3,
 2,
 'Revisión de frenos delanteros',
 'HIGH',
 true),

-- Líneas para orden WO-2026-000004
(4,
 1,
 'Diagnóstico de avería grave en motor',
 'URGENT',
 true),

(4,
 2,
 'Evaluar viabilidad de reparación',
 'HIGH',
 true);

-- =========================================================
-- 5. WORKSHOP EXECUTIONS
-- =========================================================
-- IMPORTANTE:
-- - execution_number → único
-- - workshop_order_id → relación 1:1 con workshop_orders.id
-- - workshop_order_number → snapshot del número de orden
-- - status → enum STRING (OPEN, PENDING, CLOSED, CANCELLED)
-- - start_date → fecha de inicio real de ejecución
-- - end_date → fecha de cierre real, NULL si sigue abierta
-- =========================================================

INSERT INTO workshop_executions (execution_number,
                                 workshop_order_id,
                                 workshop_order_number,
                                 status,
                                 start_date,
                                 end_date,
                                 active)
VALUES

-- Ejecución abierta para orden WO-2025-12-000001
('EX-2025-12-000001',
 1,
 'WO-2025-12-000001',
 'OPEN',
 '2025-12-01',
 NULL,
 true),

-- Ejecución pendiente para orden WO-2025-12-000002
('EX-2025-12-000002',
 2,
 'WO-2025-12-000002',
 'PENDING',
 '2025-12-03',
 NULL,
 true),

-- Ejecución cerrada para orden WO-2025-12-000003
('EX-2025-12-000003',
 3,
 'WO-2025-12-000003',
 'CLOSED',
 '2025-12-10',
 '2025-12-12',
 true),

-- Ejecución cancelada para orden WO-2025-12-000004
('EX-2025-12-000004',
 4,
 'WO-2025-12-000004',
 'CANCELLED',
 '2025-12-15',
 NULL,
 true);

-- =========================================================
-- 6. EXECUTION LINES
-- =========================================================
-- IMPORTANTE:
-- - execution_id → relación con executions.id
-- - line_number → único dentro de una ejecución
-- - type → enum STRING (LABOR, PART, EXTERNAL_SERVICE)
-- - status → enum STRING (PENDING, COMPLETED)
-- =========================================================

INSERT INTO execution_lines (execution_id,
                             line_number,
                             description,
                             type,
                             quantity,
                             status,
                             active)
VALUES

-- =====================================================
-- Líneas para EX-2025-12-000001
-- =====================================================

-- Mano de obra
(1,
 1,
 'Cambio de aceite y filtros',
 'LABOR',
 2.50,
 'COMPLETED',
 true),

-- Recambio
(1,
 2,
 'Filtro de aceite Toyota Corolla',
 'PART',
 1.00,
 'COMPLETED',
 true),

-- Trabajo externo
(1,
 3,
 'Rectificado de discos delanteros',
 'EXTERNAL_SERVICE',
 1.00,
 'PENDING',
 true),

-- =====================================================
-- Líneas para EX-2025-12-000002
-- =====================================================

(2,
 1,
 'Diagnóstico de fallo eléctrico',
 'LABOR',
 1.50,
 'PENDING',
 true),

(2,
 2,
 'Batería Ford Transit 12V',
 'PART',
 1.00,
 'PENDING',
 true),

-- =====================================================
-- Líneas para EX-2025-12-000003
-- =====================================================

(3,
 1,
 'Cambio de neumáticos traseros',
 'LABOR',
 2.00,
 'COMPLETED',
 true),

(3,
 2,
 'Neumático 215/65R16',
 'PART',
 2.00,
 'COMPLETED',
 true),

-- =====================================================
-- Líneas para EX-2025-12-000004
-- =====================================================

(4,
 1,
 'Inspección inicial de vehículo siniestrado',
 'LABOR',
 1.00,
 'COMPLETED',
 true);


-- =========================================================
-- FIN DEL SCRIPT
-- =========================================================