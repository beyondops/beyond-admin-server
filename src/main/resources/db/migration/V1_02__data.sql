INSERT INTO sys_user (
  username
  , password
  , fullname
  , gender
  , auth_type
  , email
  , phone_number
  , status
  , last_login
  , remark
  , created_at
  , updated_at
) VALUES (
  'admin',
  '2619b778b832467a825cf0406ca3c9c6',
  'Administrator',
  1,
  1,
  '',
  '',
  1,
  NULL,
  '',
  NOW(),
  NOW()
);

INSERT INTO sys_role (
  role_name,
  role_config,
  created_at,
  updated_at
) VALUES
  ('ROLE_ADMIN', '', NOW(), NOW()),
  ('ROLE_USER', '', NOW(), NOW());

INSERT INTO sys_user_role
(user_id,
 role_id,
 created_at,
 updated_at)
VALUES
  (1, 1, NOW(), NOW());

