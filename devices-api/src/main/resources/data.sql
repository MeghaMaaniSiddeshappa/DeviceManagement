-- Ensure the 'device' table exists
CREATE TABLE IF NOT EXISTS device
(
    id            UUID PRIMARY KEY,
    name          VARCHAR(255)  NOT NULL,
    brand         VARCHAR(255)  NOT NULL,
    state         VARCHAR(255)  NOT NULL,
    creation_time TIMESTAMP     NOT NULL
    );

-- Insert well-known UUIDs for specific devices
INSERT INTO device (id, name, brand, state, creation_time)
SELECT '111e4567-e89b-12d3-a456-426614174000',
       'Laptop Pro 15',
       'TechNova',
       'AVAILABLE',
       '2024-01-10 09:15:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '111e4567-e89b-12d3-a456-426614174000');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '111e4567-e89b-12d3-a456-426614174001',
       'Smartphone X2',
       'MobilityOne',
       'IN_USE',
       '2024-02-05 14:30:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '111e4567-e89b-12d3-a456-426614174001');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '111e4567-e89b-12d3-a456-426614174002',
       'Tablet Air',
       'SkyTech',
       'INACTIVE',
       '2023-12-20 11:00:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '111e4567-e89b-12d3-a456-426614174002');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '111e4567-e89b-12d3-a456-426614174003',
       'Workstation Z5',
       'ComputeMax',
       'AVAILABLE',
       '2024-03-01 08:45:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '111e4567-e89b-12d3-a456-426614174003');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '111e4567-e89b-12d3-a456-426614174004',
       'Smartwatch Pulse',
       'FitGear',
       'IN_USE',
       '2024-01-18 16:20:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '111e4567-e89b-12d3-a456-426614174004');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '222e4567-e89b-12d3-a456-426614174005',
       'Router Ultra',
       'NetWave',
       'AVAILABLE',
       '2023-11-11 10:10:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '222e4567-e89b-12d3-a456-426614174005');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '222e4567-e89b-12d3-a456-426614174006',
       'Gaming Console GX',
       'PlayMaster',
       'IN_USE',
       '2024-02-22 12:00:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '222e4567-e89b-12d3-a456-426614174006');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '222e4567-e89b-12d3-a456-426614174007',
       'Monitor Vision 27',
       'DisplayTech',
       'INACTIVE',
       '2023-10-05 09:00:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '222e4567-e89b-12d3-a456-426614174007');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '222e4567-e89b-12d3-a456-426614174008',
       'Keyboard Pro',
       'KeyWorks',
       'AVAILABLE',
       '2024-03-10 13:40:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '222e4567-e89b-12d3-a456-426614174008');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '222e4567-e89b-12d3-a456-426614174009',
       'Mouse Swift',
       'KeyWorks',
       'IN_USE',
       '2024-01-25 15:55:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '222e4567-e89b-12d3-a456-426614174009');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '333e4567-e89b-12d3-a456-426614174010',
       'Printer Jet 500',
       'PrintAll',
       'INACTIVE',
       '2023-09-14 08:00:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '333e4567-e89b-12d3-a456-426614174010');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '333e4567-e89b-12d3-a456-426614174011',
       'Camera ProShot',
       'OptiCam',
       'AVAILABLE',
       '2024-02-28 17:25:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '333e4567-e89b-12d3-a456-426614174011');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '333e4567-e89b-12d3-a456-426614174012',
       'Drone SkyFly',
       'AeroTech',
       'IN_USE',
       '2024-03-12 10:50:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '333e4567-e89b-12d3-a456-426614174012');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '333e4567-e89b-12d3-a456-426614174013',
       'Speaker BoomX',
       'SoundWave',
       'AVAILABLE',
       '2024-01-03 19:00:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '333e4567-e89b-12d3-a456-426614174013');

INSERT INTO device (id, name, brand, state, creation_time)
SELECT '333e4567-e89b-12d3-a456-426614174014',
       'Projector Beam 4K',
       'VisionMax',
       'INACTIVE',
       '2023-12-29 07:30:00'
    WHERE NOT EXISTS (SELECT 1 FROM device WHERE id = '333e4567-e89b-12d3-a456-426614174014');
