INSERT INTO role (role_id, role)
VALUES (1, 'ADMIN'), (2, 'USER')
ON CONFLICT (role_id) DO UPDATE
  SET role = excluded.role;
