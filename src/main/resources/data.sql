INSERT INTO "user" (id, username, password, role)
VALUES (1, 'admin', '$2a$10$PZzzqD4qHp8owFyExC96HOB6NxsPDDhGHJ1APd7nZQ3X.jkcTRfAu', 'ROLE_ADMIN')
    ON CONFLICT (id) DO NOTHING;
