-- =========================================================
-- ACOES Fleet Management - Development Seed Data
-- =========================================================
-- Este script inserta datos de prueba para:
-- 1. Garages (talleres)
-- 2. Vehicles (vehículos)
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

INSERT INTO garages (
    id,
    name,
    address_street,
    address_city,
    address_province,
    address_postal_code,
    address_country,
    contact_name,
    phone,
    email,
    notes,
    active
) VALUES
-- Taller interno principal
(1,
 'Taller Central ACOES',
 'Calle Principal 123',
 'Madrid',
 'Madrid',
 '28001',
 'España',
 'Carlos Gómez',
 '600123456',
 'taller.central@acoes.org',
 'Taller interno principal de la ONG',
 true
),

-- Taller externo colaborador
(2,
 'Taller Mecánica Rápida',
 'Avenida del Motor 45',
 'Madrid',
 'Madrid',
 '28020',
 'España',
 'Laura Pérez',
 '600654321',
 'mecanica.rapida@example.com',
 'Taller externo para reparaciones rápidas',
 true
);


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

INSERT INTO vehicles (
    id,
    plate_number,
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
    active
) VALUES

-- Vehículo operativo (circulando)
(
    1,
    '1234ABC',
    '1HGCM82633A123456',
    'Toyota',
    'Corolla',
    'Blanco',
    'CAR',
    'OPERATIONAL',
    125000,
    '2018-05-12',
    '2027-05-12',
    NULL,  -- No está en taller
    'Vehículo en uso normal',
    true
),

-- Vehículo en reparación (en taller 1)
(
    2,
    '5678DEF',
    'WVWZZZ1JZXW000001',
    'Ford',
    'Transit',
    'Azul',
    'VAN',
    'IN_REPAIR',
    210000,
    '2016-09-20',
    '2027-09-20',
    1,  -- Taller Central ACOES
    'En taller por revisión mecánica',
    true
),

-- Vehículo fuera de servicio
(
    3,
    '9012GHI',
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
    true
);


-- =========================================================
-- FIN DEL SCRIPT
-- =========================================================