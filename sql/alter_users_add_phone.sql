-- Add phone column to users table if not exists
ALTER TABLE users ADD COLUMN phone VARCHAR(15) NULL AFTER full_name;

