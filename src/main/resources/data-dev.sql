-- =========================================================
-- ACOES Fleet Management - Development Seed Data
-- =========================================================
-- Este script inserta datos de prueba para:
-- 1. Garages (talleres)
-- 2. Vehicles (vehículos)
-- 3. Workshop Orders (órdenes de taller)
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
('WO-2026-000001',
 1,
 'PAB1234',
 'OPEN',
 '2026-05-01',
 NULL,
 true),

-- Orden pendiente de piezas para furgoneta
('WO-2026-000002',
 2,
 'TCD5678',
 'PENDING_PARTS',
 '2026-05-03',
 NULL,
 true),

-- Orden cerrada para furgoneta
('WO-2026-000003',
 2,
 'TCD5678',
 'CLOSED',
 '2026-04-10',
 '2026-04-12',
 true),

-- Orden cancelada para vehículo fuera de servicio
('WO-2026-000004',
 3,
 'MXY9012',
 'CANCELLED',
 '2026-03-15',
 NULL,
 true);


-- =========================================================
-- FIN DEL SCRIPT
-- =========================================================