alter table charging_points
    add column created_at timestamptz;

alter table charging_points
    add column updated_at timestamptz;